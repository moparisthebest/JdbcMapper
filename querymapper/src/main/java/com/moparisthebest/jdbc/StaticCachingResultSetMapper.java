package com.moparisthebest.jdbc;

import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Global for entire application, hopefully you know what you are doing.
 */
public class StaticCachingResultSetMapper extends CachingResultSetMapper {

	private static final Map<CachingRowToObjectMapper.ResultSetKey, CachingRowToObjectMapper.FieldMapping<?>> cache = new ConcurrentHashMap<CachingRowToObjectMapper.ResultSetKey, CachingRowToObjectMapper.FieldMapping<?>>();

	public static final StaticCachingResultSetMapper instance = new StaticCachingResultSetMapper();

	public StaticCachingResultSetMapper(final Calendar cal, final int arrayMaxLength) {
		super(cal, arrayMaxLength, cache);
	}

	public StaticCachingResultSetMapper(final int arrayMaxLength) {
		super(arrayMaxLength, cache);
	}

	public StaticCachingResultSetMapper() {
		super(cache);
	}

}
