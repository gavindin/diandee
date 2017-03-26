package com.diandee.cache.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DefaultSerializeUtils {
	/**
	 * 序列化对象
	 * 
	 */
	public static byte[] serialize(Object obj) {
		ByteArrayOutputStream baos = null;
		ObjectOutputStream out = null;
		try {
			baos = new ByteArrayOutputStream();
			out = new ObjectOutputStream(baos);
			out.writeObject(obj);
			return baos.toByteArray();
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (baos != null)
					baos.close();
				if (out != null)
					out.close();
			} catch (Exception e) {
			}
		}
	}

	/**
	 * 反序列化对象
	 * 
	 */
	public static Object deserialize(byte[] in) {
		if (in.length == 0) {
			return null;
		}
		ByteArrayInputStream bais = null;
		ObjectInputStream ois = null;
		try {
			bais = new ByteArrayInputStream(in);
			ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (ClassNotFoundException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} finally {
			try {
				if (ois != null)
					ois.close();
				if (bais != null)
					bais.close();
			} catch (Exception e) {
			}
		}
	}
}
