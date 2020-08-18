package com.jt.service;

import java.util.List;

import com.jt.pojo.Order;

public interface DubboOrderService {

	String insertOrder(Order order);

	Order findOrderById(String id);
	
	List<Order> findOrderListByUserId(Long userId);

}
