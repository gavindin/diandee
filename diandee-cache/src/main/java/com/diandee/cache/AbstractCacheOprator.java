package com.diandee.cache;

import javax.annotation.Resource;

import com.diandee.cache.serializer.DefaultSerializer;

/**
 * 
 * @author Gavin Ding
 * 
 */
@Resource
public abstract class AbstractCacheOprator implements CacheOprator {
	protected final Object LOCK = new Object();

	private Serializer serializer;
	
	protected String host = "127.0.0.1";
	protected int port;
	protected int timeout = 2000;
	protected String password;

	@Override
	public void setSerializer(Serializer serializer) {
		this.serializer = serializer;
	}

	/**
	 * 获取序列化器。 如果没有指定，则返回默认序列化器{@link DefaultSerializer}.
	 */
	@Override
	public Serializer getSerializer() {
		if (serializer == null) {
			synchronized (LOCK) {
				if (serializer != null) {
					return serializer;
				}
				serializer = new DefaultSerializer();
			}
		}
		return serializer;
	}

	/**
	 * 序列化对象
	 */
	public byte[] serialize(Object obj) {
		return getSerializer().serialize(obj);
	}

	/**
	 * 序列化对象
	 */
	public byte[][] serialize(Object[] obj) {
		byte[][] bytes = new byte[obj.length][];
		for (int i = 0; i < obj.length; i++) {
			bytes[i] = getSerializer().serialize(obj);
		}
		return bytes;
	}

	/**
	 * 反序列化对象
	 */
	public Object deserialize(byte[] bytes) {
		return getSerializer().deserialize(bytes);
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

	public String getAuth() {
		return password;
	}

	public void setAuth(String password) {
		this.password = password;
	}

}
