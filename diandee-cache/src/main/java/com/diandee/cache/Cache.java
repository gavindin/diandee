package com.diandee.cache;

/**
 * 缓存
 * @author Gavin Ding
 *
 */
public interface Cache {

	/**
	 * 设置缓存
	 * 
	 * @param key
	 * @param value
	 * @param saveSeconds
	 *            保存时间（秒）
	 */
	void set(String key, Object value, int saveSeconds);

	/**
	 * 获取缓存
	 * 
	 * @param key
	 * @return 值
	 */
	Object get(String key);

	/**
	 * 移除缓存
	 * 
	 * @param key
	 */
	void remove(String key);
	
	/**
	 * 判断是否存在key
	 */
	boolean exists(String key);

	/**
	 * 清除所有缓存
	 */
	void clear();

}
