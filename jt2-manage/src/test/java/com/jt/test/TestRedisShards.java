package com.jt.test;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;

public class TestRedisShards {
	
	/**
	 * 操作時需要將多台Redis當作1台使用
	 */
	@Test
	public void testShards() {
		List<JedisShardInfo> shards = new ArrayList<JedisShardInfo>();
		JedisShardInfo info1 = new JedisShardInfo("10.211.55.6", 6379);
		JedisShardInfo info2 = new JedisShardInfo("10.211.55.6", 6380);
		JedisShardInfo info3 = new JedisShardInfo("10.211.55.6", 6381);
		shards.add(info1);
		shards.add(info2);
		shards.add(info3);
		//操作分片Redis的對象工具類 利用哈希一致性算法(Consistent Hashing)
		ShardedJedis shardedJedis = new ShardedJedis(shards);
		shardedJedis.set("1902", "1902班");
		System.out.println(shardedJedis.get("1902"));
	}
}
