package com.jt.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.dubbo.config.annotation.Reference;
import com.jt.Service.UserService;
import com.jt.pojo.User;
import com.jt.service.DubboUserService;
import com.jt.vo.SysResult;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.ShardedJedis;

@Controller
@RequestMapping("/user")
public class UserController {
	
	//@Autowired
	//private UserService userService;
	
	//導入dubbo的接口
	@Reference(timeout = 3000, check=false)
	private DubboUserService userService;
	//@Autowired
	//private Jedis jedis;
	@Autowired
	private JedisCluster jedis;
	//@Autowired(required = false)	
	//private ShardedJedis jedis;
	
	
	//http://www.jt.com/user/login.html
	//http://www.jt.com/user/register.html
	@RequestMapping("/{moduleName}")
	public String index(@PathVariable String moduleName) {
		
		return moduleName;
	}
	
	/**
	 * url : "/user/doRegister",
	 * data : {password:_password,username:_username,phone:_phone},
	 * @param user
	 * @return
	 */
	//使用dubbo實現用戶註冊
	@RequestMapping("/doRegister")
	@ResponseBody
	public SysResult saveUser(User user) {
		try {
			userService.saveUser(user);
			return SysResult.ok();		
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.fail();
		}
	}
	
	/**
	 * 實現用戶登錄
	 * 說明:利用Response對象將Cookie數據寫入客戶端
	 * Cookie的生命周期:
	 * cookie.setMaxAge(0);	立即刪除
	 * cookie.setMaxAge(值>0);	能夠存活多久 單位/秒
	 * cookie.setMaxAge(-1);	當會話結束後刪除
	 * 
	 * cookie.setPath()	cookie的權限 一般都寫"/"
	 * 例子:
	 * 頁面位置:www.jd.com/abc/a.html
	 *         www.jd.com/b.html
	 * cookie.setPath("/") 兩個頁面都能獲取
	 * cookie.setPath("/abc")	只有a.html能獲取	 
	 * @param user
	 * @return
	 */
	@RequestMapping("/doLogin")
	@ResponseBody
	public SysResult login(User user, HttpServletResponse response) {
		try {
			//調用SSO系統獲取秘鑰
			String token = userService.findUserByUP(user);
			//如果數據不為null時，將數據保存到cookie中
			//cookie中的key固定值 JT_TICKET
			if(!StringUtils.isEmpty(token)) {
				Cookie cookie = new Cookie("JT_TICKET", token);
				cookie.setMaxAge(7*24*60*60);	//生命周期 7天
				//要求所有的xxx.jt.com 實現數據共享
				cookie.setDomain("jt.com");	
				cookie.setPath("/");
				response.addCookie(cookie);
				return SysResult.ok();
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}
		return SysResult.fail();
	}
	
	/**實現用戶登出操作
	 * 1.刪除redis  拿到key->在cookie中(JT_TICKET)->透過request對象拿cookie
	 * 2.刪除cookie
	 * @return
	 */
	@RequestMapping("/logout")
	public String logout(HttpServletRequest request, HttpServletResponse response) {
		Cookie[] cookies = request.getCookies();
		String token = null;
		if(cookies.length != 0) {
			for(Cookie cookie : cookies) {
				if("JT_TICKET".equals(cookie.getName())) {
					token = cookie.getValue();
					break;
				}
			}
			//判斷token數據是否有值
			if(!StringUtils.isEmpty(token)) {
				//刪除redis的數據
				jedis.del(token);
				//刪除cookie
				Cookie cookie = new Cookie("JT_TICKET", ""); 
				//Cookie cookie = new Cookie("JT_TICKET", null); null值在有的瀏覽器可能會讓這個cookie無效
				cookie.setMaxAge(0);	//設為0 立即刪除cookie
				cookie.setPath("/");
				cookie.setDomain("jt.com");
				response.addCookie(cookie);
			}
		}
		//當用戶登出時，頁面重定向到系統首面
		return "redirect:/";
	}
	
}
