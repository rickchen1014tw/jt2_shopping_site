package com.jt.test;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

public class TestJedisCluster {

	@Test
	public void test01() {
		Set<HostAndPort> sets = new HashSet<>();
		sets.add(new HostAndPort("10.211.55.6", 7000));
		sets.add(new HostAndPort("10.211.55.6", 7001));
		sets.add(new HostAndPort("10.211.55.6", 7002));
		sets.add(new HostAndPort("10.211.55.6", 7003));
		sets.add(new HostAndPort("10.211.55.6", 7004));
		sets.add(new HostAndPort("10.211.55.6", 7005));
		JedisCluster cluster = new JedisCluster(sets);
		
		cluster.set("1902", "集群搭建完成");
		System.out.println("獲取集群數據:" + cluster.get("1902"));
	}
}
