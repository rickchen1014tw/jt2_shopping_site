package com.jt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.amqp.core.Queue;

@SpringBootApplication
@MapperScan("com.jt.mapper") //为mapper接口创建代理对象
//@DubboComponentScan("com.jt")	同application.yml scan: basePackages: com.jt
public class SpringBootRun {
	
	
	public static void main(String[] args) {
		
		SpringApplication.run(SpringBootRun.class, args);
	}
	
	/*
	 * 新建一個Queue，此時並沒有在RabbitMQ服務器上創建這個消息隊列
	 * 當創建RabbiitMQ連接和信道後，Spring的RabbitMQ工具會自動在服務器上創建隊列
	 */
	@Bean
	public Queue getQueue() {
		Queue q = new Queue("orderQueue", true);
		return q;
	}

}