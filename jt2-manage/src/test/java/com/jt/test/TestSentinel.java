package com.jt.test;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisSentinelPool;

public class TestSentinel {
	
	//測試哨兵get/set操作
	@Test
	public void test01() {
		
		//sentinels Set<String> IP:端口
		Set<String> sentinels = new HashSet<>();
		sentinels.add("10.211.55.6:26379");
		//masterName 代表主機的變量名稱
		//定義在sentinel.conf
		//sentinel monitor mymaster 10.211.55.6 6379 1
		JedisSentinelPool sentinelPool = new JedisSentinelPool("mymaster", sentinels);
		
		Jedis jedis = sentinelPool.getResource(); 
		jedis.set("cc", "端午節之後沒假了~~~!");
		System.out.println(jedis.get("cc"));
		jedis.close();
	}

}
