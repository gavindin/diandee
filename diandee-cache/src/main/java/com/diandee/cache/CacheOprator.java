package com.diandee.cache;


/**
 * 
 * @author Gavin Ding
 *
 */
public interface CacheOprator extends Cache {

	/**
	 * 设置序列化器
	 */
	void setSerializer(Serializer serializer);

	/**
	 * 获取序列化器
	 */
	Serializer getSerializer();
	
}
