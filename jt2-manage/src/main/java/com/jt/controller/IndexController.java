package com.jt.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class IndexController {

	/*
	 * 分析:如果能夠獲取url裡的值，而這個值就是跳轉頁面的名稱
	 * 思路:獲取url中指定參數
	 * RESTful:
	 * 	1.請求參數必須使用/分割
	 * 	2.參數位置必須固定
	 * 	3.接收參數時必須使用{}標識參數，
	 *    並且使用特定的注解@PathVariable，並且名稱最好一致
	 */
	/*
	//根據用戶請求，跳轉頁面
	@RequestMapping("/page/item-add")
	public String itemAdd() {
		return "item-add";
	}*/
	
	//根據用戶請求，跳轉頁面
	@RequestMapping("/page/{moduleName}")
	public String itemAdd(@PathVariable String moduleName) {
		return moduleName;
	}
	
	/*
	 * url:page/item-add/rick
	@RequestMapping("/page/{moduleName}/{name}")
	public String itemAdd(@PathVariable String moduleName, @PathVariable String name) {
	...
	}
	 */
	
}
