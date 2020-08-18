package com.jt.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.dubbo.config.annotation.Service;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jt.mapper.CartMapper;
import com.jt.pojo.Cart;

//編輯提供者jt-cart中的實現類
@Service(timeout = 3000)
public class DubboCartServiceImpl implements DubboCartService {
	@Autowired
	private CartMapper cartMapper;

	@Override
	public List<Cart> findCardListByUserId(Long userId) {
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("user_id", userId);
		List<Cart> list = cartMapper.selectList(queryWrapper);
		return list;
	}

	/**
	 * 更新數據信息: num、updated
	 * 判斷條件: where user_id and item_id
	 */
	@Override
	@Transactional
	public void updateCartNum(Cart cart) {
		Cart tempCart = new Cart();
		tempCart.setNum(cart.getNum())
				.setUpdated(new Date());
		UpdateWrapper<Cart> updateWrapper = new UpdateWrapper<>();
		updateWrapper.eq("user_id", cart.getUserId())
		             .eq("item_id", cart.getItemId());
		cartMapper.update(tempCart, updateWrapper);	
	}

	/**
	 * 條件構造器將對象中不為null的屬性當作where條件
	 * 這個例子必須保證只有itemId跟userId不為null
	 */
	@Override
	@Transactional
	public void deleteCart(Cart cart) {
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<>(cart);
		cartMapper.delete(queryWrapper); //根據userId跟itemId刪除
	}

	/**
	 * 新增業務實現
	 * 1.用戶第一次新增可以直接入庫
	 * 2.用戶不是第一次入庫，應該只做數量的修改
	 * 
	 * 根據:itemId和userId查詢數據是否已放入購物車
	 */
	@Override
	@Transactional
	public void insertCart(Cart cart) {
		QueryWrapper<Cart> queryWrapper = new QueryWrapper<>();
		queryWrapper.eq("user_id", cart.getUserId())
		            .eq("item_id", cart.getItemId());
		Cart cartDB = cartMapper.selectOne(queryWrapper);
		if(cartDB == null) {
			//用戶第一次購物這個商品 可以直接入庫
			cart.setCreated(new Date())
			    .setUpdated(cart.getCreated());
			cartMapper.insert(cart);	
		}else {
			//表示用戶多次添加購物車 只做數量的修改
			int num = cart.getNum() + cartDB.getNum();
			cartDB.setNum(num)
			      .setUpdated(new Date());
			cartMapper.updateById(cartDB);
			
			/*
			Cart cartUpdate = new Cart();
			cartUpdate.setNum(num).setUpdated(new Date());
			UpdateWrapper<Cart> updateWrapper = new UpdateWrapper<Cart>();
			updateWrapper.eq("id", cartDB.getId());
			cartMapper.update(cartUpdate, updateWrapper);
			/**
			 * 衍生知識 上面的updateById()方法等同操作什麼sql語句?
			 * 這裡更新操作時，我們先查詢出購物車表的某列數據，這列數據每個欄位都不為null，
			 * 而updateById(cartDB)方法底層除了主鍵之外不為null的數據都要參與更新 
			 * 但這裡其實只要更新num跟update，如果所有欄位都更新會拖慢速度
			 * sql: update tb_cart 
			 *      set num=#{num}, update=#{updated},
			 *          user_id=#{userId},item_id=#{itemId},
			 *          ....
			 *      where id=#{id}
			 *結論:有時候自己手寫sql可能效果更好!
			 *這裡可以優化的方式:
			 *1.自己手寫sql
			 *2.把要更新的數據獨立生成一個新的對象
			 */
		}
	}
}
