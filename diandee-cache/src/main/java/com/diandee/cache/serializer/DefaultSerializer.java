package com.diandee.cache.serializer;

import javax.annotation.Resource;

import com.diandee.cache.Serializer;
import com.diandee.cache.utils.DefaultSerializeUtils;

/**
 * 默认序列化器。基于jdk自带
 * @author Gavin Ding
 *
 */
@Resource
public class DefaultSerializer implements Serializer {

	@Override
	public byte[] serialize(Object obj) {
		if (obj == null) {
			return new byte[0];
		}
		return DefaultSerializeUtils.serialize(obj);
	}

	@Override
	public Object deserialize(byte[] bytes) {
		if (bytes == null || bytes.length == 0) {
			return null;
		}
		return DefaultSerializeUtils.deserialize(bytes);
	}
}
