package com.moparisthebest.jdbc.util;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by mopar on 6/19/17.
 */
public abstract class CacheUtil {

	public static <K, V> Map<K, V> getCache(final int maxEntries) {
		if (maxEntries > 0) { // we want a limited cache
			final float loadFactor = 0.75f; // default for HashMaps
			// if we set the initialCapacity this way, nothing should ever need re-sized
			final int initialCapacity = ((int) Math.ceil(maxEntries / loadFactor)) + 1;
			return new LinkedHashMap<K, V>(initialCapacity, loadFactor, true) {
				@Override
				protected boolean removeEldestEntry(final Map.Entry<K, V> eldest) {
					return size() > maxEntries;
				}
			};
		} else
			return new HashMap<K, V>();
	}

	public static <K, V> Map<K, V> getCache(final boolean threadSafe) {
		return threadSafe ?
				new ConcurrentHashMap<K, V>()
				:
				new HashMap<K, V>();
	}

}
