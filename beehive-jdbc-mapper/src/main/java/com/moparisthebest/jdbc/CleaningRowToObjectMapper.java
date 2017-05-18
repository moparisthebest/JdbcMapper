package com.moparisthebest.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

public class CleaningRowToObjectMapper<K, T> implements RowMapper<K, T> {

	private final Cleaner<T> cleaner;
	private final RowMapper<K,T> delegate;

	public CleaningRowToObjectMapper(Cleaner<T> cleaner, ResultSet resultSet, Class<T> returnTypeClass, Calendar cal, Class<?> mapValType, Class<K> mapKeyType) {
		this(cleaner, new RowToObjectMapper<K, T>(resultSet, returnTypeClass, cal, mapValType, mapKeyType));
	}

	public CleaningRowToObjectMapper(final Cleaner<T> cleaner, final RowMapper<K,T> delegate) {
		this.cleaner = cleaner;
		this.delegate = delegate;
	}

	@Override
	public T mapRowToReturnType() throws SQLException {
		return cleaner.clean(delegate.mapRowToReturnType());
	}

	@Override
	public K getMapKey() throws SQLException {
		return delegate.getMapKey();
	}
}
