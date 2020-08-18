package com.jt.test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jt.pojo.Item;
import com.jt.pojo.ItemDesc;

import redis.clients.jedis.Jedis;

public class TestRedis {

	//String類型操作方式 
	//IP地址:端口號
	@Test
	public void testString() {
		Jedis jedis = new Jedis("10.211.55.6", 6379);
		jedis.set("1902", "1902JAVA班");
		jedis.expire("1902", 10);
		System.out.println(jedis.get("1902"));
	}
	
	//設定數據的超時方法 2種
	//分布式鎖   會用到這兩個方法	setex	setnx
	@Test
	public void testTimeOut() throws Exception {
		Jedis jedis = new Jedis("10.211.55.6", 6379);
		jedis.setex("aa", 10, "aavalue");
		//jedis.setex("aa", 2, "aavalue");
		System.out.println(jedis.get("aa"));
		Thread.sleep(3000);
		//當key不存在時操作正常，當key存在時，則操作失敗
		Long result = jedis.setnx("aa", "aanewvalue");
		System.out.println("result:" + result + " value:" + jedis.get("aa"));
	}
	
	/**
	 * 利用Redis保存業務數據 數據庫
	 * 數據庫數據:回傳對象Object
	 * String類型 要求只能存儲字符串類型
	 * item<===>JSON<===>字符串
	 */
	//實現對象轉化JSON
	@Test
	public void objectToJSON() throws Exception {
		ItemDesc itemDesc = new ItemDesc();
		itemDesc.setItemId(100L).setItemDesc("測試方法");
		ObjectMapper mapper = new ObjectMapper();
		//轉化JSON時 對象必須要有get/set方法 
		String json = mapper.writeValueAsString(itemDesc);
		System.out.println(json);
		
		//將JSON串轉化為對象
		ItemDesc itemDesc2 = mapper.readValue(json, ItemDesc.class);
		System.out.println(itemDesc2);
	}
	
	//實現List集合與JSON轉換
	@Test
	public void listToJSON() throws Exception {
		ItemDesc itemDesc1 = new ItemDesc();
		itemDesc1.setItemId(100L).setItemDesc("測試方法");
		
		ItemDesc itemDesc2 = new ItemDesc();
		itemDesc2.setItemId(100L).setItemDesc("測試方法");
		
		List<ItemDesc> list = new ArrayList<ItemDesc>();
		list.add(itemDesc1);
		list.add(itemDesc2);
		
		ObjectMapper mapper = new ObjectMapper();
		String json = mapper.writeValueAsString(list);
		System.out.println("List轉成JSON: " + json);
		
		//將數據保存到redis中
		Jedis jedis = new Jedis("10.211.55.6", 6379);
		jedis.set("itemDescList", json);
		
		//從redis中獲取數據
		String result = jedis.get("itemDescList");
		@SuppressWarnings("unused")
		List<ItemDesc> descList= mapper.readValue(result, list.getClass());
		System.out.println("JSON轉回LIST: " + descList);
		
	}
	
	//user轉化JSON串
	/**
	 * 1.首先獲取對象的getxxx方法 (轉化JSON時 對象必須要有get/set方法 若缺少某個屬性的get方法，最後的json串會缺少該某屬性  )
	 * 2.將get去掉，首字母小寫獲取屬性的名稱
	 * 3.將屬性名稱:屬性值進行拼接
	 * 4.形成json串(字符串)
	 * @throws Exception
	 */
	@Test
	public void userToJSON() throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		User user = new User();
		user.setId(100);
		user.setName("json測試");
		user.setAge(18);
		user.setGender("男");	
		String json = mapper.writeValueAsString(user);
		System.out.println(json);		
	}
	
	/**
	 * 1.獲取user的JSON串
	 * 2.通過JSON串獲取key
	 * 3.根據class類型使用反射實例化對象
	 * 4.根據key調用對象的setKey方法設置屬性值
	 * 5.最終生成對象
	 * 6.可以利用@JsonIgnoreProperties(ignoreUnknown = true)忽略未知屬性(在類上加註解)
	 * {"id":100,"name":"json測試","age":18,"gender":"男", "ids": 1000}
	 * 若多屬性ids，底層會調用setIds()方法，因為User類裡沒有定義這個方法會報錯
	 * @throws Exception
	 */
	@Test
	public void jsonToUser() throws Exception {
		ObjectMapper mapper = new ObjectMapper();	
		User user = new User();
		user.setId(100);
		user.setName("json測試");
		user.setAge(18);
		user.setGender("男");	
		String json = mapper.writeValueAsString(user);
		
		User user2 = mapper.readValue(json, User.class);
		System.out.println(user2);
	}
	
}
