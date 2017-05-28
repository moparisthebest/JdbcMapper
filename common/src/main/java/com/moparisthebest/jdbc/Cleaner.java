package com.moparisthebest.jdbc;

public interface Cleaner<T> {

	public T clean(T dto);
}
