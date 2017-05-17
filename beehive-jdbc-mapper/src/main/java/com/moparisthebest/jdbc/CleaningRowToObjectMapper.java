package com.moparisthebest.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

public class CleaningRowToObjectMapper<K, T> extends RowToObjectMapper<K, T> {

	private final Cleaner<T> cleaner;

	public CleaningRowToObjectMapper(Cleaner<T> cleaner, ResultSet resultSet, Class<T> returnTypeClass, Calendar cal, Class<?> mapValType, Class<K> mapKeyType) {
		super(resultSet, returnTypeClass, cal, mapValType, mapKeyType);
		if (cleaner == null)
			throw new NullPointerException("cleaner cannot be null!");
		this.cleaner = cleaner;
	}

	@Override
	public T mapRowToReturnType() throws SQLException {
		return cleaner.clean(super.mapRowToReturnType());
	}
}
