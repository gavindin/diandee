package com.diandee.cache;



/**
 * 序列化器接口
 * @author Gavin Ding
 *
 */
public interface Serializer {

	/**
	 * 序列化
	 */
	byte[] serialize(Object obj);

	/**
	 * 反序列化
	 */
	Object deserialize(byte[] bytes);
}
