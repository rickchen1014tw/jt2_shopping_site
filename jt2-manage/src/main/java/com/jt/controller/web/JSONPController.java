package com.jt.controller.web;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.pojo.User;
import com.jt.util.ObjectMapperUtil;

@RestController
public class JSONPController {

	/*
	//http://manage.jt.com/web/testJSONP?callback=jQuery111106584504265791875_1576305748737&_=1576305748738
	//返回值要求: 回調函數(json數據)
	@RequestMapping("/web/testJSONP")
	public String testJSONP(String callback) {
		User user = new User();
		user.setId(100);
		user.setName("tomcat");
		String json = ObjectMapperUtil.toJSON(user);
		return callback + "(" + json + ")";
	}
	*/
	
	
	@RequestMapping("/web/testJSONP")
	public JSONPObject jsonp(String callback) {
		User user = new User();
		user.setId(100);
		user.setName("JSONP");
		//JSONObject幫我們把對象轉成JSON串再拼接callback函數名
		JSONPObject object = new JSONPObject(callback, user);
		return object;
	}
}
