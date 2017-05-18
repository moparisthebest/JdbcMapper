package com.moparisthebest.jdbc;

import java.sql.ResultSet;
import java.util.Calendar;

/**
 * Created by mopar on 5/18/17.
 */
public interface RowMapperProvider {
	<K, T> RowMapper<K, T> getRowMapper(ResultSet resultSet, Class<T> returnTypeClass, Calendar cal, Class<?> mapValType, Class<K> mapKeyType);
}
