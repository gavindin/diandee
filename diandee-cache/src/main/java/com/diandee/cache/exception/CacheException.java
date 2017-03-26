package com.diandee.cache.exception;

/**
 * 缓存异常
 */
public class CacheException extends RuntimeException {
	private static final long serialVersionUID = 3523893678877542140L;

	public CacheException() {
		super();
	}

	public CacheException(String message) {
		super(message);
	}

	public CacheException(String message, Throwable cause) {
		super(message, cause);
	}
}
