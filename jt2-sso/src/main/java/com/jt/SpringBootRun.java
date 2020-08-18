package com.jt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.jt.mapper") //为mapper接口创建代理对象
//@DubboComponentScan("com.jt")	//同application.yml scan: basePackages: com.jt 兩者用其中一個
public class SpringBootRun {
	
	
	public static void main(String[] args) {
		
		SpringApplication.run(SpringBootRun.class, args);
	}

}