package com.jt.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.druid.util.StringUtils;
import com.fasterxml.jackson.databind.util.JSONPObject;
import com.jt.pojo.User;
import com.jt.service.UserService;
import com.jt.util.ObjectMapperUtil;
import com.jt.vo.SysResult;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;

@RestController
@RequestMapping("/user")
public class UserController {

	@Autowired
	private UserService userService;
	//@Autowired
	//private Jedis jedis;
	@Autowired
	private JedisCluster jedis;

	/**
	 * 業務說明:
	 * 校驗用戶是否存在
	 *http://sso.jt.com/user/check/{param}/{type}
	 * 返回值:SysResult
	 * 由於跨域請求，所以返回值必須特殊處理callback(json)
	 $.ajax({
            	url : "http://sso.jt.com/user/check/"+escape(pin)+"/1?r=" + Math.random(),
            	dataType : "jsonp",
            	success : function(data) {
 	 在參數列接收callback值，再利用JSONObject拼callback值(jason串)
	 */

	@RequestMapping("/check/{param}/{type}")
	public JSONPObject checkUser(@PathVariable String param, 
			                     @PathVariable Integer type,
			                                   String callback) {
		JSONPObject object = null;
		try {
			//SysResult sys = userService.checkUser(param, type);
			//上面這樣做不規範，要回傳的vo，應該在controller層進行封裝
			boolean flag = userService.checkUser(param, type);
			//JSONObject幫我們把對象轉成JSON串再拼接callback函數名
			object = new JSONPObject(callback, SysResult.ok(flag));	
		} catch (Exception e) {
			e.printStackTrace();
			object = new JSONPObject(callback, SysResult.fail());
		}	
		return object;	
	}

	//http://sso.jt.com/user/register
	//httpClient.doPost(url, Map<String,String> params);
	//參數userJSON的值如何傳遞進去的?
	//我們的工具類的doPost方法把map參數轉化成post請求的表單數據把參數傳給url
	@RequestMapping("/register")
	public SysResult saveUser(String userJSON) {
		try {
			User user = ObjectMapperUtil.toObject(userJSON, User.class);
			userService.saveUser(user);
			return SysResult.ok();
		} catch (Exception e) {
			e.printStackTrace();
			return SysResult.fail();
		}
	}

	/**利用跨域實現用戶信息回顯
	 * http://sso.jt.com/user/query/cf6d62b3743bb2fd6335fd9677d729fc?callback=jsonp1576642862753&_=1576642862812
	 * @return
	 */
	@RequestMapping("/query/{ticket}")
	public JSONPObject findUserByTicket(@PathVariable String ticket, String callback) {
		String userJSON = jedis.get(ticket);
		//String userJSON = jedisCluster.get(ticket);	
		if(StringUtils.isEmpty(userJSON)) {
			//回傳數據需要經過status==200判斷   SysResult對象
			return new JSONPObject(callback, SysResult.fail());
		}else {
			//這裡我們會把用戶信息的json串回傳給前台，所以這是為什麼要把敏感資訊脫敏處理
			return new JSONPObject(callback, SysResult.ok(userJSON));
		}	
	}
/*	
		$.ajax({
			url : "http://sso.jt.com/user/query/" + _ticket,
			dataType : "jsonp",
			type : "GET",
			success : function(data){
				if(data.status == 200){
					//把json串转化为js对象
					var _data = JSON.parse(data.data);
					var html =_data.username+"，欢迎来到京淘！<a href=\"http://www.jt.com/user/logout.html\" class=\"link-logout\">[退出]</a>";
					$("#loginbar").html(html);
				}
			}
		});	
*/	
	
	
}
