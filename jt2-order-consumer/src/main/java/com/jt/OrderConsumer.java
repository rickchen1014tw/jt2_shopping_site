package com.jt;

import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jt.pojo.Order;
import com.jt.service.OrderServiceConsumer;
import com.rabbitmq.client.Channel;

@Component
public class OrderConsumer {
	//從RabbitMQ收到訂單消息後，會調用訂單的業務代碼，把訂單保存到數據庫
	@Autowired
	private OrderServiceConsumer orderService;

	//添加該註解後，會從指定的orderQueue接收消息，
	//並把數據轉為order實例傳遞到此方法
	@RabbitListener(queues="orderQueue")
	public void save(Order Order, Channel channel, Message message)
	{
		System.out.println("消費者收到消息:");
		System.out.println(Order.toString());
		try {
			orderService.insertOrder(Order);
			
			channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}