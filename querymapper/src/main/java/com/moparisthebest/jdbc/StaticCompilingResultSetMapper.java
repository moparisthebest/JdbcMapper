package com.moparisthebest.jdbc;

import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Global for entire application, hopefully you know what you are doing.
 */
public class StaticCompilingResultSetMapper extends CompilingResultSetMapper {

	private static final Map<CachingRowToObjectMapper.ResultSetKey, CompilingRowToObjectMapper.ResultSetToObject<?,?>> cache = new ConcurrentHashMap<CachingRowToObjectMapper.ResultSetKey, CompilingRowToObjectMapper.ResultSetToObject<?,?>>();

	public static final StaticCompilingResultSetMapper instance = new StaticCompilingResultSetMapper();

	public StaticCompilingResultSetMapper(final Calendar cal, final int arrayMaxLength) {
		super(cal, arrayMaxLength, cache);
	}

	public StaticCompilingResultSetMapper(final int arrayMaxLength) {
		super(arrayMaxLength, cache);
	}

	public StaticCompilingResultSetMapper() {
		super(cache);
	}

}
