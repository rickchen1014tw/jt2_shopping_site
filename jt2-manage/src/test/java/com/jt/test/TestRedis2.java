package com.jt.test;

import java.util.Map;

import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.Transaction;

//測試hash/list/事務控制
public class TestRedis2 {
	
	@Test
	public void testHash() {
		Jedis jedis = new Jedis("10.211.55.6", 6379);
		jedis.hset("user1", "id", "200");
		jedis.hset("user1", "name", "tomcat服務器");
		String result = jedis.hget("user1", "name");
		System.out.println(result);
		
		Map<String, String> map = jedis.hgetAll("user1");
		System.out.println(map);		
	}
	
	//list類型
	@Test
	public void testList() {
		Jedis jedis = new Jedis("10.211.55.6", 6379);
		jedis.lpush("list", "1", "2", "3", "4", "5");
		System.out.println(jedis.rpop("list"));
	}
	
	//redis事務控制
	//在對redis進行多次寫入的操作時，進行事務的控制
	//若有一次失敗的話，進行事務的回滾
	@Test
	public void testTx() {
		Jedis jedis = new Jedis("10.211.55.6", 6379);
		Transaction transaction = jedis.multi();//開啟事務
		try {	
			transaction.set("ww", "www");
			//transaction.set("dd", "ddd");
			transaction.set("dd", null); //報錯，回滾
			transaction.exec();
		} catch (Exception e) {
			transaction.discard();
		}
	}
}
