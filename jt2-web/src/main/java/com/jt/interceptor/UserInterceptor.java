package com.jt.interceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.alibaba.druid.util.StringUtils;
import com.jt.pojo.User;
import com.jt.util.ObjectMapperUtil;
import com.jt.util.UserThreadLocal;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.ShardedJedis;

//將攔截器交給spring容器管理
@Component
public class UserInterceptor implements HandlerInterceptor{
	
	
	@Autowired
	private JedisCluster jedis;
	
	//@Autowired
	//private Jedis jedis;
	
	//@Autowired(required = false)	
	//private ShardedJedis jedis;
	/**
	 * 在spring4版本中要求必須重寫3個方法，不管是否需要
	 * 在spring5版本中在接口中方法添加了default屬性，則可以省略不寫，只重寫需要的方法
	 */
	/**
	 * 返回值結果:
	 * true:攔截放行
	 * false:請求攔截，重定向到登錄頁面
	 * 業務邏輯:
	 * 1.獲取cookie數據
	 * 2.從cookie中獲取token/ticket
	 * 3.判斷redis緩存服務器中是否有數據
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		System.out.println("攔載器開始工作!");
		String token = null;
		//1.獲取cookie信息
		Cookie[] cookies = request.getCookies();
		for(Cookie cookie : cookies) {
			if("JT_TICKET".equals(cookie.getName())) {
				token = cookie.getValue();
				break;
			}
		}
		//2.判斷token是否有效
		if(!StringUtils.isEmpty(token)) {
			//4.判斷redis中是否有數據
			String userJSON = jedis.get(token);
			if(!StringUtils.isEmpty(userJSON)) {
				//redis中有用戶數據，攔載器放行
				//將userJSON轉化為user對象
				User user = ObjectMapperUtil.toObject(userJSON, User.class);
				
				/*
				//將user數據保存到request域對象中
				request.setAttribute("JT_USER", user);
				*/
				
				/*
				//用戶每次請求都將user數據都保存到session中，切記在afterCompleion()中用完刪除
				//如果業務需求，其它業務邏輯其它地方需要獲得用戶信息，就會存在session域中
				//我的想法: 當用戶發起請求，查session域對象看有沒有用戶信息的對象，
				//如果沒有就根據用戶的token去redis查詢用戶信息回來，再把對象存到session裡，
				//當用戶結束對話，session對象也會銷毀
				//request.getSession().setAttribute("JT_USER", user);
				*/
				
				//用ThreadLocal來做
				UserThreadLocal.set(user);
				return true;
			}
		}
		
		//3.重定向到用戶登錄頁面
		response.sendRedirect("/user/login.html");
		return false; //表示攔截
	}
	
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		//request.getSession().removeAttribute("JT_USER");
		//用完thread-local variable把他刪掉
		UserThreadLocal.remove();
	}
}
