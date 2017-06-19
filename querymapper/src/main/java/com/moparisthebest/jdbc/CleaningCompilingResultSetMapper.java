package com.moparisthebest.jdbc;

import java.sql.ResultSet;
import java.util.Calendar;
import java.util.Map;

/**
 * Created by mopar on 5/18/17.
 */
public class CleaningCompilingResultSetMapper<E> extends CompilingResultSetMapper {

	private final Cleaner<E> cleaner;

	public CleaningCompilingResultSetMapper(final Calendar cal, final int arrayMaxLength, final CompilingRowToObjectMapper.Cache cache, final Cleaner<E> cleaner) {
		super(cal, arrayMaxLength, cache);
		this.cleaner = cleaner;
	}

	public CleaningCompilingResultSetMapper(final CompilingRowToObjectMapper.Cache cache, final Cleaner<E> cleaner) {
		super(cache);
		this.cleaner = cleaner;
	}

	public CleaningCompilingResultSetMapper(final int arrayMaxLength, final CompilingRowToObjectMapper.Cache cache, final Cleaner<E> cleaner) {
		super(arrayMaxLength, cache);
		this.cleaner = cleaner;
	}

	public CleaningCompilingResultSetMapper(final Cleaner<E> cleaner) {
		this.cleaner = cleaner;
	}

	@Override
	@SuppressWarnings({"unchecked"})
	public <K, T> RowMapper<K, T> getRowMapper(final ResultSet resultSet, final Class<T> returnTypeClass, final Calendar cal, final Class<?> mapValType, final Class<K> mapKeyType) {
		return new CleaningRowToObjectMapper((Cleaner<T>)cleaner, super.getRowMapper(resultSet, returnTypeClass, cal, mapValType, mapKeyType));
	}
}
