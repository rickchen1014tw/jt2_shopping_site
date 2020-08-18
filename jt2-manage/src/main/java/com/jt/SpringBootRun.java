package com.jt;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//@SpringBootApplication(exclude = DataSourceAutoConfiguration.class) 啟動時不加載數據源
@SpringBootApplication
@MapperScan("com.jt.mapper")
public class SpringBootRun {
	
	public static void main(String[] args) {
		
		SpringApplication.run(SpringBootRun.class, args);
	}
}
