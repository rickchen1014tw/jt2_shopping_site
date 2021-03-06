package com.jt.service;

import java.util.Date;
import java.util.List;

import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.jt.mapper.OrderItemMapper;
import com.jt.mapper.OrderMapper;
import com.jt.mapper.OrderShippingMapper;
import com.jt.pojo.Order;
import com.jt.pojo.OrderItem;
import com.jt.pojo.OrderShipping;

//雖然有三張表，但屬於同一個業務，一個業務寫一個service就夠了
@Service
public class OrderServiceConsumerImpl implements OrderServiceConsumer {

	@Autowired
	private OrderMapper orderMapper;
	@Autowired
	private OrderItemMapper orderItemMapper;
	@Autowired
	private OrderShippingMapper orderShippingMapper;
	//RabbitAutoConfiguration自動配置類對RabbitMQ進行自動配置，
	//這個類會創建AmpqTemplate實例，
	//且其中的RabbitAdmin類中的declareQueue()方法，會創建隊列
	//取出spring容器中的Queue對象，一個一個在RabbitMQ服務器上創出這些隊列出來
	//使用SpringBoot，Spring整合RabbitMQ後，RabbitMQ的自動配置類會把相關配置封裝起來
	//如連接RabbitMQ，創建隊列，什麼時候去發送msg，都底層已封裝好了
	@Autowired
	AmqpTemplate amqpTemplate;
	
	/**
	 * 一個業務邏輯需要操作3張表數據
	 * 1.添加事務控制(要是某張表某個操作出錯了，進行回滾)
	 * 2.表數據分析 order對象封裝了三張表的數據 order orderItems orderShipping
	 * 3.準備orderId "訂單號: 登錄的用戶id+當前時間戳"
	 * 4.操作操作三張表3個mapper分別入庫
	 */
	@Override
	@Transactional
	public String insertOrder(Order order) {
		//1.取出訂單id
		String orderId = order.getOrderId();
		Date date = new Date();	
		//2.入庫訂單
		order.setStatus(1) //設為 未付款 狀態
		     .setCreated(date)
		     .setUpdated(date);
		orderMapper.insert(order);
		System.out.println("訂單入庫成功!");
		//3.入庫訂單物流
		OrderShipping shipping = order.getOrderShipping();
		shipping.setOrderId(orderId)
		        .setCreated(date)
		        .setUpdated(date);
		orderShippingMapper.insert(shipping);
		System.out.println("訂單物流入庫成功!");
		//4.入庫訂單商品 方式1:自己遍歷集合分別入庫 方式2:自己編輯sql語句批量入庫
		//下列的批量插入速度比較快 通過一個insert同時入庫n條數據比一條一條入庫快很多
		//insert into order_items values(xxx,xxx,xxx,...) values(xxx,xxx,xxx,...),(xxx,xxx,xxx,...),(xxx,xxx,xxx,...)
		//下列遍歷集合分別入庫
		List<OrderItem> orderItems = order.getOrderItems();
		for(OrderItem orderItem : orderItems) {
			orderItem.setOrderId(orderId)
			         .setCreated(date)
			         .setUpdated(date);
			orderItemMapper.insert(orderItem);
		}
		System.out.println("訂單商品入庫成功!");
		
		return orderId;

	}

}
