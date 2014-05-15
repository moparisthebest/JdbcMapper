package com.moparisthebest.jdbc;

import java.sql.ResultSet;
import java.util.Calendar;

/**
 * Created by mopar on 5/15/14.
 */
public class CaseInsensitiveMapResultSetMapper extends ResultSetMapper {
	@Override
	protected <T> RowToObjectMapper<T> getRowMapper(ResultSet resultSet, Class<T> returnTypeClass, Calendar cal, Class<?> mapValType) {
		return new CaseInsensitiveMapRowToObjectMapper<T>(resultSet, returnTypeClass, cal, mapValType);
	}
}
