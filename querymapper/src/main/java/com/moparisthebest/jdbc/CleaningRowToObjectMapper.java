package com.moparisthebest.jdbc;

import com.moparisthebest.jdbc.util.ResultSetToObject;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

public class CleaningRowToObjectMapper<K, T> implements RowMapper<K, T>, ResultSetToObject<T> {

	private final Cleaner<T> cleaner;
	private final RowMapper<K,T> delegate;
	private ResultSetToObject<T> delegateRsto = null;

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

	@Override
	public T toObject(final ResultSet rs, final Calendar cal) throws SQLException {
		return cleaner.clean(this.delegateRsto.toObject(rs, cal));
	}

	@Override
	public ResultSetToObject<T> getResultSetToObject() {
		if(this.delegateRsto == null)
			this.delegateRsto = delegate.getResultSetToObject();
		return this;
	}
}
