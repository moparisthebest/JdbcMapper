package com.moparisthebest.jdbc;

import com.moparisthebest.jdbc.codegen.JdbcMapper;
import com.moparisthebest.jdbc.util.Bindable;
import com.moparisthebest.jdbc.util.InListUtil;
import com.moparisthebest.jdbc.util.PreparedStatementUtil;

import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

/**
 * For a column name and a Collection, return a Object usable by QueryMapper for binding to a PreparedStatement and
 * ListQueryMapper for substituting in the query
 */
public interface InList {

	InList defaultInList = InListObject.getDefaultInListInstance();

	/**
	 * Returns an InList instance for use with this connection
	 * @param conn connection which may be inspected to determine best InList to use
	 * @return InList instance
	 */
	public InList instance(final Connection conn);

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

	class InListObject implements Bindable {
		static final InListObject inEmpty = new InListObject(InListUtil.inEmpty, null);
		static final InListObject notInEmpty = new InListObject(InListUtil.notInEmpty, null);

        public static InListObject inEmpty() {
            return inEmpty;
        }

        public static InListObject notInEmpty() {
            return notInEmpty;
        }

        private final String sql;
		private final Object bindObject;

		public InListObject(final String sql, final Object bindObject) {
			this.sql = sql;
			this.bindObject = bindObject;
		}

		@Override
		public final String toString() {
			return sql;
		}

		@Override
		public Object getBindObject() {
			return bindObject;
		}

		private static InList getDefaultInListInstance() {
			try {
				final String inListClassName = System.getProperty("QueryMapper.defaultInList.class");
				if(inListClassName != null) {
					final Class<?> inListClass = Class.forName(inListClassName);
					final Method method = inListClass.getMethod(System.getProperty("QueryMapper.defaultInList.method", "instance"));
					return (InList) method.invoke(null);
				} else {
					// todo: change default to OPTIMAL ?
					final String type = System.getProperty("queryMapper.databaseType", System.getProperty("jdbcMapper.databaseType", "BIND"));
					if(type.equals("OPTIMAL")) {
						return OptimalInList.instance();
					} else {
						switch (JdbcMapper.DatabaseType.valueOf(type)) {
							case DEFAULT:
							case BIND:
								return BindInList.instance();
							case ANY:
								return ArrayInList.instance();
							case ORACLE:
								return OracleArrayInList.instance();
							case UNNEST:
								return UnNestArrayInList.instance();
							default:
								throw new RuntimeException("Invalid queryMapper.databaseType: " + type);
						}
					}
				}
			} catch (Throwable e) {
				// NEVER ignore
				throw new RuntimeException(e);
			}
		}
	}
}
