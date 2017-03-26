package com.diandee.cache.serializer;

import javax.annotation.Resource;

import com.diandee.cache.Serializer;
import com.diandee.cache.utils.ProtostuffSerializeUtils;

/**
 * Protostuff序列器
 * 
 * @author Gavin Ding
 * 
 *         Mar 19, 2017 9:31:35 AM
 */
@Resource
public class ProtostuffSerializer implements Serializer {

	private int bufferSize = ProtostuffSerializeUtils.DEFAULT_BUFFER_SIZE;

	public int getBufferSize() {
		return bufferSize;
	}

	public void setBufferSize(int bufferSize) {
		this.bufferSize = bufferSize;
	}

	public ProtostuffSerializer() {
		// Nothig to do
	}

	public ProtostuffSerializer(int bufferSize) {
		setBufferSize(bufferSize);
	}

	@Override
	public byte[] serialize(Object obj) {
		if (obj == null) {
			return new byte[0];
		}
		return ProtostuffSerializeUtils.serializeByHolder(obj, bufferSize);
	}

	@Override
	public Object deserialize(byte[] bytes) {
		if (bytes == null || bytes.length == 0) {
			return null;
		}
		return ProtostuffSerializeUtils.deserializeByHolder(bytes);
	}
}
