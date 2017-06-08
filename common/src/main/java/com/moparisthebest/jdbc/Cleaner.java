package com.moparisthebest.jdbc;

public interface Cleaner<T> {

	public <E extends T> E clean(E dto);
}
