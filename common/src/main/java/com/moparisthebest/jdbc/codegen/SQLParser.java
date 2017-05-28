package com.moparisthebest.jdbc.codegen;

/**
 * Created by mopar on 5/25/17.
 */
public interface SQLParser {

	/**
	 * Return SQLParser instance for given SQL
	 *
	 * @param sql SQL to parse
	 * @return instance with string parsed
	 */
	SQLParser parse(String sql);

	/**
	 * @return column names for select, with 1-based index like ResultSet, index 0 is always null, not used if isSelect() returns false
	 */
	String[] columnNames();

	/**
	 * Return
	 *
	 * @return true for Select, if we will map to an object, false to call executeUpdate (insert/update/merge/?)
	 */
	boolean isSelect();
}
