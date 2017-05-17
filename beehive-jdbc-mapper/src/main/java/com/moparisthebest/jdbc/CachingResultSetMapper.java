package com.moparisthebest.jdbc;

import java.sql.ResultSet;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Maps the same as ResultSetMapper except caches constructor and field mappings
 *
 * @see ResultSetMapper
 */
public class CachingResultSetMapper extends ResultSetMapper {

	protected final Map<CachingRowToObjectMapper.ResultSetKey, CachingRowToObjectMapper.FieldMapping<?>> cache;
	/**
	 * CachingResultSetMapper with optional maxEntries, expiring old ones in LRU fashion
	 *
	 * @param cal            optional calendar for date/time values
	 * @param arrayMaxLength max array/list/map length, a value of less than 1 indicates that all rows from the ResultSet should be included
	 * @param maxEntries     max cached compiled entries to keep in cache, < 1 means unlimited
	 */
	public CachingResultSetMapper(final Calendar cal, final int arrayMaxLength, final int maxEntries) {
		super(cal, arrayMaxLength);
		if (maxEntries > 0) { // we want a limited cache
			final float loadFactor = 0.75f; // default for HashMaps
			// if we set the initialCapacity this way, nothing should ever need re-sized
			final int initialCapacity = ((int) Math.ceil(maxEntries / loadFactor)) + 1;
			cache = new LinkedHashMap<CachingRowToObjectMapper.ResultSetKey, CachingRowToObjectMapper.FieldMapping<?>>(initialCapacity, loadFactor, true) {
				@Override
				protected boolean removeEldestEntry(final Map.Entry<CachingRowToObjectMapper.ResultSetKey, CachingRowToObjectMapper.FieldMapping<?>> eldest) {
					return size() > maxEntries;
				}
			};
		} else
			cache = new HashMap<CachingRowToObjectMapper.ResultSetKey, CachingRowToObjectMapper.FieldMapping<?>>();
	}

	/**
	 * CachingResultSetMapper with unlimited cache
	 *
	 * @param cal            optional calendar for date/time values
	 * @param arrayMaxLength max array/list/map length, a value of less than 1 indicates that all rows from the ResultSet should be included
	 */
	public CachingResultSetMapper(final Calendar cal, final int arrayMaxLength) {
		this(cal, arrayMaxLength, 0); // default unlimited cache
	}

	/**
	 * CachingResultSetMapper with unlimited cache
	 *
	 * @param arrayMaxLength max array/list/map length, a value of less than 1 indicates that all rows from the ResultSet should be included
	 */
	public CachingResultSetMapper(final int arrayMaxLength) {
		this(null, arrayMaxLength);
	}

	/**
	 * CachingResultSetMapper with unlimited cache
	 */
	public CachingResultSetMapper() {
		this(-1);
	}

	/**
	 * CachingResultSetMapper with custom cache implementation
	 *
	 * @param cal            optional calendar for date/time values
	 * @param arrayMaxLength max array/list/map length, a value of less than 1 indicates that all rows from the ResultSet should be included
	 * @param cache          any Map implementation for cache you wish, does not need to handle null keys or values
	 */
	public CachingResultSetMapper(final Calendar cal, final int arrayMaxLength, final Map<CachingRowToObjectMapper.ResultSetKey, CachingRowToObjectMapper.FieldMapping<?>> cache) {
		super(cal, arrayMaxLength);
		if (cache == null)
			throw new IllegalArgumentException("cache cannot be null");
		this.cache = cache;
	}

	/**
	 * CachingResultSetMapper with custom cache implementation
	 *
	 * @param arrayMaxLength max array/list/map length, a value of less than 1 indicates that all rows from the ResultSet should be included
	 * @param cache          any Map implementation for cache you wish, does not need to handle null keys or values
	 */
	public CachingResultSetMapper(final int arrayMaxLength, final Map<CachingRowToObjectMapper.ResultSetKey, CachingRowToObjectMapper.FieldMapping<?>> cache) {
		this(null, arrayMaxLength, cache);
	}

	/**
	 * CachingResultSetMapper with custom cache implementation
	 *
	 * @param cache any Map implementation for cache you wish, does not need to handle null keys or values
	 */
	public CachingResultSetMapper(final Map<CachingRowToObjectMapper.ResultSetKey, CachingRowToObjectMapper.FieldMapping<?>> cache) {
		this(-1, cache);
	}

	/**
	 * CachingResultSetMapper with optionally threadSafe cache implementation
	 *
	 * @param cal            optional calendar for date/time values
	 * @param arrayMaxLength max array/list/map length, a value of less than 1 indicates that all rows from the ResultSet should be included
	 * @param threadSafe     true uses a thread-safe cache implementation (currently ConcurrentHashMap), false uses regular HashMap
	 */
	public CachingResultSetMapper(final Calendar cal, final int arrayMaxLength, final boolean threadSafe) {
		this(cal, arrayMaxLength, threadSafe ?
				new ConcurrentHashMap<CachingRowToObjectMapper.ResultSetKey, CachingRowToObjectMapper.FieldMapping<?>>()
				:
				new HashMap<CachingRowToObjectMapper.ResultSetKey, CachingRowToObjectMapper.FieldMapping<?>>()
		);
	}

	/**
	 * CachingResultSetMapper with optionally threadSafe cache implementation
	 *
	 * @param arrayMaxLength max array/list/map length, a value of less than 1 indicates that all rows from the ResultSet should be included
	 * @param threadSafe     true uses a thread-safe cache implementation (currently ConcurrentHashMap), false uses regular HashMap
	 */
	public CachingResultSetMapper(final int arrayMaxLength, final boolean threadSafe) {
		this(null, arrayMaxLength, threadSafe);
	}

	/**
	 * CachingResultSetMapper with optionally threadSafe cache implementation
	 *
	 * @param threadSafe true uses a thread-safe cache implementation (currently ConcurrentHashMap), false uses regular HashMap
	 */
	public CachingResultSetMapper(final boolean threadSafe) {
		this(-1, threadSafe);
	}

	@Override
	protected <K, T> RowToObjectMapper<K, T> getRowMapper(ResultSet resultSet, Class<T> returnTypeClass, Calendar cal, Class<?> mapValType, Class<K> mapKeyType) {
		return new CachingRowToObjectMapper<K, T>(cache, resultSet, returnTypeClass, cal, mapValType, mapKeyType);
	}
}
