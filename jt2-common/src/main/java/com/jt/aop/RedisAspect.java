package com.jt.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.jt.anno.Cache_Find;
import com.jt.enu.KEY_ENUM;
import com.jt.util.ObjectMapperUtil;
import com.jt.util.RedisService;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisSentinelPool;
import redis.clients.jedis.ShardedJedis;

@Component	//將對象交給spring容器管理
@Aspect	//標識切面
public class RedisAspect {
	/*當RedisAspect在實例化的時候，必須注入Jedis對象，
	若有一天做一個功能模塊，不需要使用Redis，所以沒有配置redis.properties，
	但引用了jt-common，但這個功能模塊啟動的時候，解析到這行代碼，會因為沒有配置redis.properties
	沒有Jedis對象而出現錯誤。所以一般注入的工具類屬性，要標示required=false，
	表示程序啟動時不需要注入對象
	容器初始化時不需要初始化，只有用戶使用時才初始化
	一般工具類中添加該註解*/
	
	//單台redis
	//@Autowired(required = false)	
	//private Jedis jedis;
	

	//redis分片
	//@Autowired(required = false)	
	//private ShardedJedis jedis;
	
	//注入redis哨兵工具API              題外話: 數據庫連接池定義多大性能比較優，通常企業設定多少? 設定1000個連接
	//這裡如果引入private JedisSentinelPool jedis; 直接操作JedisSentinelPool對象，則必須先getResource()，再set()，再close()
	//步驟太多，我們另外寫一個工具類把他額外的步驟封裝起來
	//@Autowired(required = false)
	//private RedisService jedis;
	
	//redis集群
	@Autowired(required = false)
	private JedisCluster jedis;

	//使下列的寫法可以直接獲取註解的對象
	//攔載的註解是Cache_find就滿足切入點表達式(這裡的cache_find是一個註解變量名稱，必須要跟方法上的參數名一致)
	//寫@Around("@annotation(com.jt.anno.Cache_find)")攔得到，但沒辦法把註解對象賦值給一個變量用在方法裡取值
	//@Around通知，Spring規定參數必須要有ProceedingJoinPoint joinPoint，接口名跟參數名固定不可變，且必須擺在第一個
	@SuppressWarnings("unchecked")
	@Around("@annotation(cache_find)")
	public Object around(ProceedingJoinPoint joinPoint, Cache_Find cache_find) {
		//1.獲取key的值
		String key = getKey(joinPoint, cache_find);
		//2.根據key查詢緩存
		String result = jedis.get(key);
		Object data = null;
		try {
			if(StringUtils.isEmpty(result)) {
				//如果結果為null，表示緩存中沒有數據
				//查詢數據庫
				data = joinPoint.proceed();	//表示執行業務方法，執行完方法後把返回的值assign給data
				//將數據轉化為JSON串
				String json = ObjectMapperUtil.toJSON(data);
				//判斷用戶是否設定超時時間
				if(cache_find.seconds() == 0) {
					//表示不設定超時時間
					jedis.set(key, json);
				}else {
					jedis.setex(key, cache_find.seconds(), json);
				}
				System.out.println("第一次查詢數據庫!把查詢結果也存入Redis中");
			}else {
				//如果緩存中有數據，則將json串轉化為對象
				Class targetClass = getClass(joinPoint);
				data = ObjectMapperUtil.toObject(result, targetClass);
				System.out.println("AOP查詢緩存!");
			}
		} catch (Throwable e) {
			e.printStackTrace();
			//Spring只會針對runtime exception做回滾，所以要轉成runtime exception
			throw new RuntimeException(e);
		}

		return data;
	}

	//獲取返回值類型
	private Class getClass(ProceedingJoinPoint joinPoint) {
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();
		return signature.getReturnType();
	}

	/**
	 * 1.判斷用戶key類型 auto empty
	 * @param joinPoint
	 * @param cache_find
	 * @return
	 */
	private String getKey(ProceedingJoinPoint joinPoint, Cache_Find cache_find) {
		//1.獲取key類型
		KEY_ENUM keyType = cache_find.keyType();
		//2.判斷key類型
		if(keyType.equals(KEY_ENUM.EMPTY)) {
			//表示使用用戶自己的key
			return cache_find.key();
		}
		//表示用戶的key需要拼接 key+"_"+第一個參數 如ITEM_CAT_0、ITEM_CAT_56
		Object[] args = joinPoint.getArgs();
		String strArg = String.valueOf(args[0]); //用戶的參數可能是任意的類型，不能在這裡用(String)強轉
		String key = cache_find.key() + "_" + strArg;

		return key;
	}

}
