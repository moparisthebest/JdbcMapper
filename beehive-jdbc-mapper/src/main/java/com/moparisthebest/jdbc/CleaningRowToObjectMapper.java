package com.moparisthebest.jdbc;

import java.sql.ResultSet;
import java.util.Calendar;

public class CleaningRowToObjectMapper<T> extends RowToObjectMapper<T> {

	private final Cleaner<T> cleaner;

	public CleaningRowToObjectMapper(Cleaner<T> cleaner, ResultSet resultSet, Class<T> returnTypeClass, Calendar cal, Class<?> mapValType) {
		super(resultSet, returnTypeClass, cal, mapValType);
		if (cleaner == null)
			throw new NullPointerException("cleaner cannot be null!");
		this.cleaner = cleaner;
	}

	@Override
	public T mapRowToReturnType() {
		return cleaner.clean(super.mapRowToReturnType());
	}
}
