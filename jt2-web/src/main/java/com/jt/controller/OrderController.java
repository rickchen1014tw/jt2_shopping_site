package com.jt.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.pojo.Cart;
import com.jt.pojo.Order;
import com.jt.service.DubboCartService;
import com.jt.service.DubboOrderService;
import com.jt.util.UserThreadLocal;
import com.jt.vo.SysResult;

@Controller
@RequestMapping("/order")
public class OrderController {

	@Reference(timeout = 3000, check = false)
	private DubboOrderService orderService;
	//訂單業務裡需要用到購物車的業務
	@Reference(timeout = 3000, check = false)
	private DubboCartService cartService;
	
	/**
	 * 跳轉訂單確認頁面
	 * url:http://www.jt.com/order/create.html
	 * 頁面名稱:order-cart.jsp
	 * 頁面取值:item="${carts}"
	 */
	@RequestMapping("create")
	public String orderCreate(Model model) {
		//用戶請求這個頁面/order/create的時候，被攔截器攔到了，攔截器會把user對象放到thread-local variable裡
		//先取得用戶的id
		Long userId = UserThreadLocal.get().getId();
		List<Cart> carts = cartService.findCardListByUserId(userId);
		model.addAttribute("carts", carts);
		return "order-cart";
	}
	
	/**
	 * 實現訂單入庫
	 */
	@RequestMapping("/submit")
	@ResponseBody
	public SysResult insertOrder(Order order) {
		try {
			Long userId = UserThreadLocal.get().getId();
			order.setUserId(userId); //把orderId值補上，入庫需要
			String orderId = orderService.insertOrder(order);
			if(!StringUtils.isEmpty(orderId)) {
				return SysResult.ok(orderId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SysResult.fail();
	}
	
	/**
	 * 根據訂單信息，查詢數據
	 * url:http://www.jt.com/order/success.html?id=111576887996901
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping("/success")
	public String findOrderById(String id, Model model) {
		Order order = orderService.findOrderById(id);
		model.addAttribute("order", order);
		return "success";
	}
	
	@RequestMapping("/myorder")
	public String findOrderListByUserId(Model model) {
		System.out.println("查詢訂單");
		Long userId = UserThreadLocal.get().getId();
		List<Order> orderList = orderService.findOrderListByUserId(userId);
		model.addAttribute("orderList", orderList);
		return "myorder";
	}
	
}
