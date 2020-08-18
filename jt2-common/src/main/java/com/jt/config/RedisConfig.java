package com.jt.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;

//Redis配置類 放在jt-common是因為其它的模組可能也會用到redis做緩存
@Configuration
@PropertySource("classpath:/properties/redis.properties")
public class RedisConfig {
	
	/*
	//單台redis
	@Value("${jedis.host}") 
	private String host;

	@Value("${jedis.port}") 
	private Integer port;

	@Bean 
	public Jedis jedis() {
		return new Jedis(host, port); 
	}
	*/
	
	
	/*
	//redis分片
	@Value("${redis.nodes}")
	private String redisNodes;

	@Bean	//<bean id="" class=""> 我們這邊沒有指定bean id，默認是方法名
	public ShardedJedis shardedJedis() {
		List<JedisShardInfo> shards = new ArrayList<>();	
		String[] nodes = redisNodes.split(",");
		for(String node: nodes) {
			String host = node.split(":")[0];
			int port = Integer.parseInt(node.split(":")[1]);
			JedisShardInfo info = new JedisShardInfo(host, port);
			shards.add(info);
		}
		return new ShardedJedis(shards);
	}
	
	*/

	/*
	//通過哨兵機制實現redis操作
	@Value("${redis.sentinels}")
	private String sentinelNodes;
	@Value("${redis.sentinel.masterName}")
	private String masterName;
	
	@Bean
	public JedisSentinelPool jedisSentinelPool() {
		Set<String> sentinels = new HashSet<>();
		sentinels.add(sentinelNodes);
		return new JedisSentinelPool(masterName, sentinels);
	}
	*/
	
	
	//redis集群
	@Value("${redis.nodes}")
	private String redisNodes;

	@Bean
	public JedisCluster jedisCluster() {
		Set<HostAndPort> nodes = new HashSet<>();
		String[] strNode = redisNodes.split(",");
		for(String node : strNode) {
			String host = node.split(":")[0];
			int port = Integer.valueOf(node.split(":")[1]);
			nodes.add(new HostAndPort(host, port));
		}
		return new JedisCluster(nodes);
	}
	
	
}
