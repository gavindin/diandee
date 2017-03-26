package com.diandee.cache.utils;

import io.protostuff.LinkedBuffer;
import io.protostuff.ProtostuffIOUtil;
import io.protostuff.Schema;
import io.protostuff.runtime.RuntimeSchema;

import com.diandee.cache.serializer.vo.ValueHolder;

public final class ProtostuffSerializeUtils {
	public static final int DEFAULT_BUFFER_SIZE = LinkedBuffer.DEFAULT_BUFFER_SIZE;

	/**
	 * 序列化对象
	 * 
	 * @param obj
	 *            obj 序列化对象。一般对象，不能为List或map等（Protostuff不能直接序列化map、list）
	 * @return
	 */
	public static byte[] serialize(Object obj) {
		return serialize(obj, DEFAULT_BUFFER_SIZE);
	}

	/**
	 * 序列化对象
	 * 
	 * @param obj
	 *            序列化对象。一般对象，不能为List或map等（Protostuff不能直接序列化map、list）
	 * @param bufferSize
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> byte[] serialize(T obj, int bufferSize) {
		if (obj == null) {
			return new byte[0];
		}
		Schema<T> schema = RuntimeSchema.getSchema((Class<T>) (obj.getClass()));
		LinkedBuffer buffer = LinkedBuffer.allocate(bufferSize);
		try {
			return ProtostuffIOUtil.toByteArray(obj, schema, buffer);
		} finally {
			buffer.clear();
		}
	}

	/**
	 * 序列化对象.
	 * 
	 * @param obj
	 *            所有对象
	 * @return
	 */
	public static byte[] serializeByHolder(Object obj) {
		return serializeByHolder(obj, DEFAULT_BUFFER_SIZE);
	}

	/**
	 * 序列化对象。<br/>
	 * 解决序化对象类型限制，字符串类型序列化byte更小,其它会更大,大约一倍，效率没有明显变化。
	 * 
	 * @param obj
	 *            所有对象
	 * @param bufferSize
	 * @return
	 */
	public static byte[] serializeByHolder(Object obj, int bufferSize) {
		if (obj == null) {
			return new byte[0];
		}
		Schema<ValueHolder> schema = RuntimeSchema.getSchema(ValueHolder.class);
		LinkedBuffer buffer = LinkedBuffer.allocate(bufferSize);
		try {
			return ProtostuffIOUtil.toByteArray(new ValueHolder(obj), schema, buffer);
		} finally {
			buffer.clear();
		}

	}

	/**
	 * 反序列化。必须是使用serializeByHolder序列化。
	 * <br/>
	 * 不用指定泛型类型。
	 * @param bytes
	 * @return
	 */
	public static Object deserializeByHolder(byte[] bytes) {
		if (bytes == null || bytes.length == 0) {
			return null;
		}
		ValueHolder holder = new ValueHolder();
		deserializeTo(bytes, holder);
		return holder.value;
	}

	public static <T> T deserialize(byte[] bytes, Class<T> type) {
		if (bytes == null || bytes.length == 0) {
			return null;
		}

		Schema<T> schema = RuntimeSchema.getSchema(type);
		T target;
		try {
			target = type.newInstance();
		} catch (Exception e) {
			throw new RuntimeException("Failed to new " + type.getName(), e);
		}
		ProtostuffIOUtil.mergeFrom(bytes, target, schema);
		return target;
	}

	public static <T> void deserializeTo(byte[] bytes, T target) {
		if (bytes == null || bytes.length == 0) {
			return;
		}
		if (target == null) {
			throw new IllegalArgumentException("target not allow null.");
		}
		@SuppressWarnings("unchecked")
		Schema<T> schema = RuntimeSchema.getSchema((Class<T>) (target.getClass()));
		ProtostuffIOUtil.mergeFrom(bytes, target, schema);
	}
}
