package com.threegene.cache.ssdb.test;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;

import redis.clients.jedis.Protocol;

import com.diandee.cache.client.RedisCache;
import com.diandee.cache.client.config.RedisPoolConfig;
/**
 * jedis测试
 * @author Gavin Ding
 *
 */
public class RedisTest {
	private static final RedisCache REDIS_CACHE = new RedisCache("192.168.10.152", Protocol.DEFAULT_PORT, 2000, "diandee", new RedisPoolConfig());
	private static final String KEY = "diandee-key-";
	private static final String LINE_BREAK = "\n";
	private static final String BLANK = "                ";

	@Test 
	@Ignore
	public void testSet() {
		StringBuilder builder = new StringBuilder("call redis set ").append(LINE_BREAK);
		for (int i = 0; i < 10; i++) {
			builder.append(BLANK + "key:").append(KEY + i).append("  value:").append("value_" + i).append(LINE_BREAK);
			REDIS_CACHE.set(KEY + i, "value_" + i, 2000);
		}
		System.out.println(builder.toString());
	}

	@Test 
	@Ignore
	public void testKeys() {
		StringBuilder builder = new StringBuilder("call redis keys and get keys as follow:").append(LINE_BREAK);
		Set<String> keys = REDIS_CACHE.keys("*");
		for (String key : keys) {
			builder.append(BLANK + key).append(LINE_BREAK);
		}
		System.out.println(builder.toString());
	}

	@Test 
	@Ignore
	public void testGets() {
		String[] keys = { KEY + "1", KEY + "2" };
		StringBuilder builder = new StringBuilder("call redis gets and get map:").append(LINE_BREAK);
		Map<String, Object> map = REDIS_CACHE.gets(keys);
		for (String key : keys) {
			builder.append(BLANK + key + ":").append(map.get(key)).append(LINE_BREAK);
		}
		System.out.println(builder.toString());
	}

	@Test
	@Ignore
	public void testGet() {
		System.out.println("call redis get key:" + KEY + "1 value:" + REDIS_CACHE.get(KEY + "1"));
	}

	@Test
	@Ignore
	public void testClear() {
		REDIS_CACHE.clear();
		System.out.println("clear all cache in redis.");
	}

	@Test @Ignore
	public void testexists() {
		System.out.println("call exist key:diandee-key-1 result:" + REDIS_CACHE.exists("diandee-key-1"));
	}

	@Test @Ignore
	public void testExpire() throws InterruptedException {
		String expireKey = "diandee-key-1";
		System.out.println("call get key:" + expireKey + " value:" + REDIS_CACHE.get(expireKey));
		REDIS_CACHE.expire(expireKey, 1);
		System.out.println("call expire key:" + expireKey + " 1 second then sleep 1 second...");
		Thread.sleep(1000);
		System.out.println("call get key:" + expireKey + " value:" + REDIS_CACHE.get(expireKey));

	}

	private static final String hkey = KEY + "h";

	@Test @Ignore
	public void testHset() {
		StringBuilder builder = new StringBuilder("call redis hset ").append(LINE_BREAK);
		for (int i = 0; i < 10; i++) {
			builder.append(BLANK + "field:").append(hkey + i).append("  value:").append("value_" + i).append(LINE_BREAK);
			REDIS_CACHE.hset(hkey, hkey + i, "Value" + i);
		}
		System.out.println(builder.toString());
	}

	@Test @Ignore
	public void testHexists() {
		System.out.println("call hexists key:" + hkey + " field:" + hkey + "1 result:" + REDIS_CACHE.hexists(hkey, hkey + "1"));
	}

	@Test @Ignore
	public void testHget() {
		System.out.println("call hget key:" + hkey + " field:" + hkey + "1 result:" + REDIS_CACHE.hget(hkey, hkey + "1"));
	}

	@Test @Ignore
	public void testHkey() {
		System.out.println("call hkey key:" + hkey + " result keys:" + LINE_BREAK);
		for (String field : REDIS_CACHE.hkeys(hkey)) {
			System.out.println(BLANK + field);
		}
	}

	@Test @Ignore
	public void testHdel() {
		System.out.println("call hget key:" + hkey + " field:" + hkey + "1 result:" + REDIS_CACHE.hget(hkey, hkey + "1"));
		REDIS_CACHE.hdel(hkey, hkey + "1");
		System.out.println("hkey:" + hkey + " filed:" + hkey + "1 delete...");
		System.out.println("call hget key:" + hkey + " field:" + hkey + "1 result:" + REDIS_CACHE.hget(hkey, hkey + "1"));
	}

	@Test @Ignore
	public void testHmget() {
		System.out.println("call hmget key:" + hkey + " field:" + hkey + "1 " + hkey + "2 result values:" + LINE_BREAK);
		for (Object o : REDIS_CACHE.hmget(hkey, hkey + "1", hkey + "2")) {
			System.out.println(BLANK + o);
		}
	}

	@Test @Ignore
	public void testHmset() {
		StringBuilder builder = new StringBuilder("call hmset key:" + hkey + " field:" + hkey + "1" + hkey + "2 result values:" + LINE_BREAK);
		Map<String, String> map = new HashMap<String, String>(10);
		for (int i = 10; i < 20; i++) {
			map.put(hkey + i, "value_" + i);
			builder.append(BLANK + "field:" + hkey + i + " value:value_" + i).append(LINE_BREAK);
		}
		REDIS_CACHE.hmset(hkey, map);

		builder.append("call hmget key:" + hkey + " field:" + hkey + "11 " + hkey + "12 result values:" + LINE_BREAK);
		for (Object o : REDIS_CACHE.hmget(hkey, hkey + "11", hkey + "12")) {
			builder.append(BLANK + o + LINE_BREAK);
		}
		System.out.println(builder.toString());
	}
/**
 * 取值decr时请使用原始方式，封装的序列化方式和原始的不一致，解析异常
 */
	@Test @Ignore
	public void testDecr() { 
		System.out.println("call decr key:redisDecr result:"+REDIS_CACHE.decr("redisDecr", 1));
		System.out.println("call jedi.get key: redisDecr value:" + REDIS_CACHE.getJedis().get("redisDecr"));
	}
}
