package com.moparisthebest.jdbc;

import java.sql.SQLException;

/**
 * Created by mopar on 5/18/17.
 */
public interface RowMapper<K,T> {
	/**
	 * Map a ResultSet row to the return type class
	 * @return An instance of class, if _mapKeyType is not null and _columnCount is 2, return only index 2
	 */
	T mapRowToReturnType() throws SQLException;

	/**
	 * key for map
	 * @return index number 1, with type of _mapKeyType
	 * @throws MapperException if _mapKeyType is null
	 */
	K getMapKey() throws SQLException;
}
