package com.moparisthebest.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

/**
 * For a column name and a Collection, return a Object usable by QueryMapper for binding to a PreparedStatement and
 * ListQueryMapper for substituting in the query
 */
public interface InList {

	/**
	 * Returns an Object who's .toString returns a String for a query, and QueryMapper knows how to bind to a PreparedStatement
	 * @param columnName Column name for query
	 * @param values values for in list
	 * @return object
	 */
	public <T> InListObject inList(final Connection conn, final String columnName, final Collection<T> values) throws SQLException;

	/**
	 * Returns an Object who's .toString returns a String for a query, and QueryMapper knows how to bind to a PreparedStatement
	 * @param columnName Column name for query
	 * @param values values for not in list
	 * @return object
	 */
	public <T> InListObject notInList(final Connection conn, final String columnName, final Collection<T> values) throws SQLException;

	class InListObject {
		static final InListObject inEmpty = new InListObject("(0=1)");
		static final InListObject notInEmpty = new InListObject("(1=1)");

		private final String sql;

		public InListObject(final String sql) {
			this.sql = sql;
		}

		@Override
		public final String toString() {
			return sql;
		}
	}
}
