package com.diandee.cache.client.config;

import java.io.Serializable;

import javax.annotation.Resource;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;

import redis.clients.jedis.JedisPoolConfig;

@Resource
public class RedisPoolConfig implements Serializable {

	private static final long serialVersionUID = 1126898439305692664L;

	private int maxIdle = GenericObjectPoolConfig.DEFAULT_MAX_IDLE;

	private int minIdle = GenericObjectPoolConfig.DEFAULT_MIN_IDLE;

	private boolean testOnBorrow = GenericObjectPoolConfig.DEFAULT_TEST_ON_BORROW;

	private boolean testOnReturn = GenericObjectPoolConfig.DEFAULT_TEST_ON_RETURN;

	private boolean testWhileIdle = GenericObjectPoolConfig.DEFAULT_TEST_WHILE_IDLE;

	private long timeBetweenEvictionRunsMillis = GenericObjectPoolConfig.DEFAULT_TIME_BETWEEN_EVICTION_RUNS_MILLIS;

	private int numTestsPerEvictionRun = GenericObjectPoolConfig.DEFAULT_NUM_TESTS_PER_EVICTION_RUN;

	private long minEvictableIdleTimeMillis = GenericObjectPoolConfig.DEFAULT_MIN_EVICTABLE_IDLE_TIME_MILLIS;

	private long softMinEvictableIdleTimeMillis = GenericObjectPoolConfig.DEFAULT_SOFT_MIN_EVICTABLE_IDLE_TIME_MILLIS;

	private boolean lifo = GenericObjectPoolConfig.DEFAULT_LIFO;

	private int maxTotal = GenericObjectPoolConfig.DEFAULT_MAX_TOTAL;

	private long maxWaitMillis = GenericObjectPoolConfig.DEFAULT_MAX_WAIT_MILLIS;

	private boolean blockWhenExhausted = GenericObjectPoolConfig.DEFAULT_BLOCK_WHEN_EXHAUSTED;

	public int getMaxIdle() {
		return maxIdle;
	}

	public void setMaxIdle(int maxIdle) {
		this.maxIdle = maxIdle;
	}

	public int getMinIdle() {
		return minIdle;
	}

	public void setMinIdle(int minIdle) {
		this.minIdle = minIdle;
	}

	public boolean isTestOnBorrow() {
		return testOnBorrow;
	}

	public void setTestOnBorrow(boolean testOnBorrow) {
		this.testOnBorrow = testOnBorrow;
	}

	public boolean isTestOnReturn() {
		return testOnReturn;
	}

	public void setTestOnReturn(boolean testOnReturn) {
		this.testOnReturn = testOnReturn;
	}

	public boolean isTestWhileIdle() {
		return testWhileIdle;
	}

	public void setTestWhileIdle(boolean testWhileIdle) {
		this.testWhileIdle = testWhileIdle;
	}

	public long getTimeBetweenEvictionRunsMillis() {
		return timeBetweenEvictionRunsMillis;
	}

	public void setTimeBetweenEvictionRunsMillis(long timeBetweenEvictionRunsMillis) {
		this.timeBetweenEvictionRunsMillis = timeBetweenEvictionRunsMillis;
	}

	public int getNumTestsPerEvictionRun() {
		return numTestsPerEvictionRun;
	}

	public void setNumTestsPerEvictionRun(int numTestsPerEvictionRun) {
		this.numTestsPerEvictionRun = numTestsPerEvictionRun;
	}

	public long getMinEvictableIdleTimeMillis() {
		return minEvictableIdleTimeMillis;
	}

	public void setMinEvictableIdleTimeMillis(long minEvictableIdleTimeMillis) {
		this.minEvictableIdleTimeMillis = minEvictableIdleTimeMillis;
	}

	public long getSoftMinEvictableIdleTimeMillis() {
		return softMinEvictableIdleTimeMillis;
	}

	public void setSoftMinEvictableIdleTimeMillis(long softMinEvictableIdleTimeMillis) {
		this.softMinEvictableIdleTimeMillis = softMinEvictableIdleTimeMillis;
	}

	public boolean isLifo() {
		return lifo;
	}

	public void setLifo(boolean lifo) {
		this.lifo = lifo;
	}

	public int getMaxTotal() {
		return maxTotal;
	}

	public void setMaxTotal(int maxTotal) {
		this.maxTotal = maxTotal;
	}

	public long getMaxWaitMillis() {
		return maxWaitMillis;
	}

	public void setMaxWaitMillis(long maxWaitMillis) {
		this.maxWaitMillis = maxWaitMillis;
	}

	public boolean isBlockWhenExhausted() {
		return blockWhenExhausted;
	}

	public void setBlockWhenExhausted(boolean blockWhenExhausted) {
		this.blockWhenExhausted = blockWhenExhausted;
	}

	public JedisPoolConfig toPoolConfig() {
		JedisPoolConfig poolConfig = new JedisPoolConfig();
		poolConfig.setLifo(lifo);
		poolConfig.setMaxTotal(maxTotal);
		poolConfig.setMaxIdle(maxIdle);
		poolConfig.setMaxWaitMillis(maxWaitMillis);
		poolConfig.setMinEvictableIdleTimeMillis(minEvictableIdleTimeMillis);
		poolConfig.setMinIdle(minIdle);
		poolConfig.setNumTestsPerEvictionRun(numTestsPerEvictionRun);
		poolConfig.setSoftMinEvictableIdleTimeMillis(softMinEvictableIdleTimeMillis);
		poolConfig.setTestOnBorrow(testOnBorrow);
		poolConfig.setTestOnReturn(testOnReturn);
		poolConfig.setTestWhileIdle(testWhileIdle);
		poolConfig.setTimeBetweenEvictionRunsMillis(timeBetweenEvictionRunsMillis);
		poolConfig.setBlockWhenExhausted(blockWhenExhausted);
		return poolConfig;
	}

}
