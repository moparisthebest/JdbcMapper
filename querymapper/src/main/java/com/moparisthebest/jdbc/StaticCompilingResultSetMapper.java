package com.moparisthebest.jdbc;

import com.moparisthebest.jdbc.util.CacheUtil;

import java.util.Calendar;

/**
 * Global for entire application, hopefully you know what you are doing.
 */
public class StaticCompilingResultSetMapper extends CompilingResultSetMapper {

	private static final CompilingRowToObjectMapper.Cache cache = new CompilingRowToObjectMapper.Cache(CacheUtil.<CompilingRowToObjectMapper.ResultSetKey, CompilingRowToObjectMapper.ResultSetToObject<?,?>>getCache(true), true);

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
