package com.moparisthebest.jdbc.util;

import java.util.HashMap;

/**
 * Created by mopar on 5/18/17.
 */
public class CaseInsensitiveHashMap<String, V> extends HashMap<String, V> {
	@Override
	public V get(Object key) {
		return super.get(key instanceof java.lang.String ? ((java.lang.String) key).toLowerCase() : key);
	}

	@Override
	public boolean containsKey(Object key) {
		return super.containsKey(key instanceof java.lang.String ? ((java.lang.String) key).toLowerCase() : key);
	}
}
