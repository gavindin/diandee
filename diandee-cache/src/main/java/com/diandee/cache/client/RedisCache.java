package com.diandee.cache.client;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.annotation.Resource;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.util.SafeEncoder;

import com.diandee.cache.AbstractCacheOprator;
import com.diandee.cache.client.config.RedisPoolConfig;

/**
 * Redis緩存
 * 
 * @author Gavin Ding
 * 
 */
@Resource
public class RedisCache extends AbstractCacheOprator {
	private final Object LOCK = new Object();

	private RedisPoolConfig poolConfig;

	private static JedisPool pool;

	private static RedisExecutors redisExecutors = new RedisExecutors();

	public Jedis getJedis() {
		return getPool().getResource();
	}

	/**
	 * 
	 * @param host
	 *            redis主机（IP）
	 * @param port
	 *            redis端口
	 * @param timeout
	 *            连接超时时间
	 */
	public RedisCache(String host, int port, int timeout) {
		this(host, port, timeout, null, null);
	}

	/**
	 * 
	 * @param host
	 *            redis主机（IP）
	 * @param port
	 *            redis端口
	 * @param timeout
	 *            连接超时时间
	 * @param poolConfig
	 *            redis配置
	 */
	public RedisCache(String host, int port, int timeout, RedisPoolConfig poolConfig) {
		this(host, port, timeout, null, poolConfig);
	}

	/**
	 * 
	 * @param host
	 *            redis主机（IP）
	 * @param port
	 *            redis端口
	 * @param timeout
	 *            连接超时时间
	 * @param auth
	 *            redis密码
	 * @param poolConfig
	 *            redis配置
	 */
	public RedisCache(String host, int port, int timeout, String auth, RedisPoolConfig poolConfig) {
		this.host = host;
		this.port = port;
		this.timeout = timeout;
		this.password = auth;
		this.poolConfig = poolConfig;
	}

	@Override
	public void set(final String key, final Object value, final int saveSeconds) {
		redisExecutors.doExecute(getPool(), new RedisCallBack<String>() {
			@Override
			public String execute(Jedis jedis) {
				return jedis.setex(SafeEncoder.encode(key), saveSeconds, serialize(value));
			}
		});

	}

	@Override
	public Object get(final String key) {
		return redisExecutors.doExecute(getPool(), new RedisCallBack<Object>() {
			@Override
			public Object execute(Jedis jedis) {
				return deserialize(getJedis().get(SafeEncoder.encode(key)));
			}
		});
	}

	/**
	 * 批量获取
	 * 
	 * @param keys
	 *            key
	 * @return {@code Map<key,value>}
	 */
	public Map<String, Object> gets(final String... keys) {
		return redisExecutors.doExecute(getPool(), new RedisCallBack<Map<String, Object>>() {
			@Override
			public Map<String, Object> execute(Jedis jedis) {
				List<byte[]> list = getJedis().mget(SafeEncoder.encodeMany(keys));
				Map<String, Object> map = new HashMap<String, Object>(list.size());
				for (int i = 0; i < list.size(); i++) {
					map.put(keys[i], deserialize(list.get(i)));
				}
				return map;
			}
		});
	}

	/**
	 * 删除缓存。只针对KV类型
	 */
	@Override
	public void remove(final String key) {
		redisExecutors.doExecute(getPool(), new RedisCallBack<Long>() {
			@Override
			public Long execute(Jedis jedis) {
				return getJedis().del(key);
			}
		});
	}

	@Override
	public void clear() {
		redisExecutors.doExecute(getPool(), new RedisCallBack<String>() {
			@Override
			public String execute(Jedis jedis) {
				return getJedis().flushDB();
			}
		});
	}

	/**
	 * 判断是否存在key.只针对KV类型
	 */
	@Override
	public boolean exists(final String key) {
		return redisExecutors.doExecute(getPool(), new RedisCallBack<Boolean>() {
			@Override
			public Boolean execute(Jedis jedis) {
				return getJedis().exists(key);
			}
		});
	}

	/**
	 * 不存在时设置缓存
	 * 
	 * @param key
	 * @param value
	 */
	public void setnx(final String key, final Object value) {
		redisExecutors.doExecute(getPool(), new RedisCallBack<Long>() {
			@Override
			public Long execute(Jedis jedis) {
				return getJedis().setnx(serialize(key), serialize(value));
			}
		});
	}

	/**
	 * 移除
	 * 
	 * @param keys
	 */
	public void remove(final String... keys) {
		redisExecutors.doExecute(getPool(), new RedisCallBack<Long>() {
			@Override
			public Long execute(Jedis jedis) {
				return getJedis().del(keys);
			}
		});
	}

	/**
	 * 自增.返回自增后值
	 */
	public long incr(final String key, final long val) {
		return redisExecutors.doExecute(getPool(), new RedisCallBack<Long>() {
			@Override
			public Long execute(Jedis jedis) {
				return getJedis().incrBy(key, val);
			}
		});
	}

	/**
	 * 自减.返回自减后值
	 */
	public long decr(final String key, final long val) {
		return redisExecutors.doExecute(getPool(), new RedisCallBack<Long>() {
			@Override
			public Long execute(Jedis jedis) {
				return getJedis().decrBy(key, val);
			}
		});
	}

	/**
	 * 设置失效时间
	 * 
	 * @param key
	 * @param seconds
	 *            保存多少秒
	 */
	public void expire(final String key, final int seconds) {
		redisExecutors.doExecute(getPool(), new RedisCallBack<Long>() {
			@Override
			public Long execute(Jedis jedis) {
				return getJedis().expire(key, seconds);
			}
		});
	}

	/**
	 * 查找key
	 * 
	 * @param pattern
	 * @return
	 */
	public Set<String> keys(final String pattern) {
		return redisExecutors.doExecute(getPool(), new RedisCallBack<Set<String>>() {
			@Override
			public Set<String> execute(Jedis jedis) {
				return jedis.keys(pattern);
			}
		});
	}

	/**
	 * 设置Map
	 * 
	 * @param key
	 *            map key
	 * @param filed
	 *            字段Key
	 * @param value
	 */
	public void hset(final String key, final String filed, final Object value) {
		redisExecutors.doExecute(getPool(), new RedisCallBack<Long>() {
			@Override
			public Long execute(Jedis jedis) {
				return jedis.hset(SafeEncoder.encode(key), SafeEncoder.encode(filed), serialize(value));
			}
		});
	}

	/**
	 * 批量设置Map
	 * 
	 * @param key
	 *            map key
	 * @param filedValues
	 *            字段key-value
	 */
	public <T> void hmset(final String key, Map<String, T> filedValues) {
		if (filedValues == null || filedValues.isEmpty()) {
			return;
		}
		final Map<byte[], byte[]> pairs = new HashMap<byte[], byte[]>(filedValues.size());
		for (Entry<String, T> kv : filedValues.entrySet()) {
			pairs.put(SafeEncoder.encode(kv.getKey()), serialize(kv.getValue()));
		}

		redisExecutors.doExecute(getPool(), new RedisCallBack<String>() {
			@Override
			public String execute(Jedis jedis) {
				return jedis.hmset(SafeEncoder.encode(key), pairs);
			}
		});
	}

	/**
	 * 获取map值
	 * 
	 * @param key
	 *            map key
	 * @param field
	 *            字段Key
	 * @return
	 */
	public Object hget(final String key, final String field) {
		return redisExecutors.doExecute(getPool(), new RedisCallBack<Object>() {
			@Override
			public Object execute(Jedis jedis) {
				return deserialize(jedis.hget(SafeEncoder.encode(key), SafeEncoder.encode(field)));
			}
		});
	}

	/**
	 * 批量获取 map值
	 * 
	 * @param key
	 *            map key
	 * @param fields
	 *            字段Key
	 * @return {@code Map<field,value>}
	 */
	public List<Object> hmget(final String key, final String... fields) {
		if (fields == null || fields.length == 0) {
			throw new IllegalArgumentException("fields not allow null or empty.");
		}
		List<byte[]> list = redisExecutors.doExecute(getPool(), new RedisCallBack<List<byte[]>>() {
			@Override
			public List<byte[]> execute(Jedis jedis) {
				return jedis.hmget(SafeEncoder.encode(key), SafeEncoder.encodeMany(fields));
			}
		});
		List<Object> objs = new ArrayList<Object>(list.size());
		for (byte[] bs : list) {
			objs.add(deserialize(bs));
		}
		return objs;
	}

	/**
	 * 判断map的字段是否存在
	 * 
	 * @param key
	 * @param filed
	 * @return
	 */
	public boolean hexists(final String key, final String field) {
		return redisExecutors.doExecute(getPool(), new RedisCallBack<Boolean>() {
			@Override
			public Boolean execute(Jedis jedis) {
				return jedis.hexists(key, field);
			}
		});
	}
/**
 * 删除
 * @param key
 * @param fields
 */
	public void hdel(final String key, final String... fields) {
		redisExecutors.doExecute(getPool(), new RedisCallBack<Long>() {
			@Override
			public Long execute(Jedis jedis) {
				return jedis.hdel(key, fields);
			}
		});
	}
/**
 * 获取map key
 * @param key
 * @return
 */
	public List<String> hkeys(final String key) {
		Set<byte[]> bytes = redisExecutors.doExecute(getPool(), new RedisCallBack<Set<byte[]>>() {
			@Override
			public Set<byte[]> execute(Jedis jedis) {
				return jedis.hkeys(SafeEncoder.encode(key));
			}
		});
		if (bytes == null) {
			return null;
		}
		if (bytes.isEmpty()) {
			return new ArrayList<String>();
		}
		List<String> keys = new ArrayList<String>(bytes.size());
		for (byte[] bs : bytes) {
			keys.add(SafeEncoder.encode(bs));
		}
		return keys;
	}

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getTimeout() {
		return timeout;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public RedisPoolConfig getPoolConfig() {
		return poolConfig;
	}

	public void setPoolConfig(RedisPoolConfig poolConfig) {
		this.poolConfig = poolConfig;
	}

	/**
	 * 执行器，统一处理将用过的jedis放回jedisPool,jetis3.0以后使用方法jedis.close()
	 * 
	 * @author Gavin Ding
	 * 
	 */
	private static class RedisExecutors  {
		public <T> T doExecute(final JedisPool pool, RedisCallBack<T> callBack) {
			Jedis jedis = pool.getResource();
			try {
				return callBack.execute(jedis);
			} catch (Exception e) {
				throw new RuntimeException("jedis run error.", e);
			} finally {
				jedis.close();
			}
		}
	}
/**
 * 回调
 * @author Gavin Ding
 *
 */
	private interface RedisCallBack<T> {
		T execute(Jedis jedis);
	}
	
	private JedisPool getPool() {
		if (pool == null) {
			synchronized (LOCK) {
				if (pool == null) {
					pool = new JedisPool(poolConfig.toPoolConfig(), host, port, timeout, password);
				}
			}
		}
		return pool;
	}

}
