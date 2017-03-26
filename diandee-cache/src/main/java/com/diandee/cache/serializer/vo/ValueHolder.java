package com.diandee.cache.serializer.vo;

/**
 * 因Protostuff不能直接序列化map、list，但是map和list包含在普通对象中可正常序列化，so 利用ValueHolder就可序列化所有的对象了
 * @author Gavin Ding
 *
 */
public class ValueHolder {

	public Object value;

	public ValueHolder() {

	}

	public ValueHolder(Object value) {
		this.value = value;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((value == null) ? 0 : value.hashCode());
		return result;
	}

	public String toString(){
		return String.format("{value:%s}", value);
	}
}
