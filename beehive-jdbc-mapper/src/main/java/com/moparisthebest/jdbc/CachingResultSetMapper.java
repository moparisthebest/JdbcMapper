package com.moparisthebest.jdbc;

import java.sql.ResultSet;
import java.util.Calendar;

public class CachingResultSetMapper extends ResultSetMapper {

	public CachingResultSetMapper(Calendar cal, int arrayMaxLength) {
		super(cal, arrayMaxLength);
	}

	public CachingResultSetMapper() {
		super();
	}

	@Override
	protected <T> RowToObjectMapper<T> getRowMapper(ResultSet resultSet, Class<T> returnTypeClass, Calendar cal, Class<?> mapValType) {
		return new CachingRowToObjectMapper<T>(resultSet, returnTypeClass, cal, mapValType);
	}
}
