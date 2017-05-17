package com.moparisthebest.jdbc;

import java.sql.ResultSet;
import java.util.Calendar;

public class CleaningResultSetMapper<E> extends ResultSetMapper {

	private final Cleaner<E> cleaner;

	public CleaningResultSetMapper(Cleaner<E> cleaner, Calendar cal, int arrayMaxLength) {
		super(cal, arrayMaxLength);
		this.cleaner = cleaner;
	}

	public CleaningResultSetMapper(Cleaner<E> cleaner) {
		super();
		this.cleaner = cleaner;
	}

	@Override
	@SuppressWarnings({"unchecked"})
	protected <K, T> RowToObjectMapper<K, T> getRowMapper(ResultSet resultSet, Class<T> returnTypeClass, Calendar cal, Class<?> mapValType, Class<K> mapKeyType) {
		return new CleaningRowToObjectMapper<K, T>((Cleaner<T>)cleaner, resultSet, returnTypeClass, cal, mapValType, mapKeyType);
	}
}
