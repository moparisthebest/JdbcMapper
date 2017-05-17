package com.moparisthebest.jdbc;

import java.sql.ResultSet;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CachingResultSetMapper extends ResultSetMapper {

	protected final Map<CachingRowToObjectMapper.ResultSetKey, CachingRowToObjectMapper.FieldMapping<?>> cache = new HashMap<CachingRowToObjectMapper.ResultSetKey, CachingRowToObjectMapper.FieldMapping<?>>();

	public CachingResultSetMapper(Calendar cal, int arrayMaxLength) {
		super(cal, arrayMaxLength);
	}

	public CachingResultSetMapper() {
		super();
	}

	@Override
	protected <T> RowToObjectMapper<T> getRowMapper(ResultSet resultSet, Class<T> returnTypeClass, Calendar cal, Class<?> mapValType) {
		return new CachingRowToObjectMapper<T>(cache, resultSet, returnTypeClass, cal, mapValType);
	}
}
