package com.jt.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;
import com.jt.util.ObjectMapperUtil;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

@Service(timeout = 3000)
public class DubboUserServiceImpl implements DubboUserService {

	@Autowired
	private UserMapper userMapper;
	//@Autowired
	//private Jedis jedis;
	@Autowired
	private JedisCluster jedis;

	@Transactional	//添加事務控制
	@Override
	public void saveUser(User user) {
		//1.將密碼加密
		String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md5Pass);
		//2.補齊入庫數據email暫時使用電話代替
		user.setEmail(user.getPhone())
		    .setCreated(new Date())
		    .setUpdated(user.getCreated());
		//3.插入數據庫
		userMapper.insert(user);	
	}

	/**
	 * 1.校驗用戶名或密碼是否正確
	 * 2.如果數據不正確 返回null (也可以拋異常，這裡沒有採用這個做法)
	 * (之前是單台的系統，調Service層異常可以被上層直接捕獲，現在是分佈式系統，
	  *   後台系統拋異常，會拋給dubbo的容器，之後再由容器拋給消費者)
	 * 3.如果數據正確 :
	 * 	1)生成加密秘鑰: MD5(JT_TICKET+username+當前毫秒數) (這樣可以保證每次秘鑰都不一樣)
	 * 	2)將use數據庫的數據轉化為userJSON
	 * 	3)將數據保存到redis中7天超時
	 * 4.返回token
	 */
	@Override
	public String findUserByUP(User user) {
		String token = null;
		//1.將密碼進行加密 (要記得註冊的時候加密跟登錄的時候加密的方式要一樣，不然之後會登不進系統)
		String md5Pass = DigestUtils.md5DigestAsHex(user.getPassword().getBytes());
		user.setPassword(md5Pass);
		//傳入的user對象裡只有username跟password不為空，而我們也只要求根據username跟password進行查詢，所以可以把user做為構造的參數傳入做為查詢條件
		QueryWrapper<User> queryWrapper = new QueryWrapper<User>(user);
		//QueryWrapper<User> queryWrapper = new QueryWrapper<User>();
		//queryWrapper.eq("username", user.getUsername()).eq("password", user.getPassword());
		User userDB = userMapper.selectOne(queryWrapper);
		
		//2.判斷數據正確執行下列代碼
		if(userDB != null) {
			token = "JT_TICKET_" + userDB.getUsername() + System.currentTimeMillis();
			token = DigestUtils.md5DigestAsHex(token.getBytes());
			//敏感數據進行脫敏處理  userDB含有所有數據
			userDB.setPassword("你猜猜");
			String userJSON= ObjectMapperUtil.toJSON(userDB);
			//將數據保存到Redis中
			//jedisCluster.setex(token, 7*24*60*60, userJSON);
			jedis.setex(token, 7*24*60*60, userJSON);
			
		}
		
		return token;
	}
}
