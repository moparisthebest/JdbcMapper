package com.moparisthebest.jdbc;

import java.sql.ResultSet;
import java.util.Calendar;

/**
 * Created by mopar on 5/15/14.
 */
public class CaseInsensitiveMapResultSetMapper extends ResultSetMapper {
	public CaseInsensitiveMapResultSetMapper() {
	}

	public CaseInsensitiveMapResultSetMapper(int arrayMaxLength) {
		super(arrayMaxLength);
	}

	@Override
	public <K, T> RowMapper<K, T> getRowMapper(ResultSet resultSet, Class<T> returnTypeClass, Calendar cal, Class<?> mapValType, Class<K> mapKeyType) {
		return new RowToObjectMapper<K, T>(resultSet, returnTypeClass, cal, mapValType, mapKeyType, true);
	}
}
