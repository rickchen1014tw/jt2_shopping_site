package com.jt.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.jt.mapper.UserMapper;
import com.jt.pojo.User;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private UserMapper userMapper;

	/**
	 * true表示已經用戶存在 false表示用戶可以使用
	 * 1.param 用戶參數
	 * 2.type 1:username 2:phone 3:email
	 *   將type轉化為具體字段
	 */
	@Override
	public boolean checkUser(String param, Integer type) {
		String column = type==1?"username":(type==2?"phone":"email"); 
		QueryWrapper<User> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq(column, param);
		int count = userMapper.selectCount(queryWrapper);
		return count==1?true:false;
	}

	@Transactional
	@Override
	public void saveUser(User user) {
		//E-mail暫時用電話來代替
		user.setEmail(user.getPhone())
		.setCreated(new Date())
		.setUpdated(user.getCreated());
		userMapper.insert(user);
		//int a = 1/0; 测试当用户入库失败后用户展现
	}
}
