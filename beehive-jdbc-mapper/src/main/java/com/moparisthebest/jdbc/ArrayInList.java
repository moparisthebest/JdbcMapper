package com.moparisthebest.jdbc;

import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

/**
 * Created by mopar on 4/29/15.
 */
public class ArrayInList implements InList {

	private static final InList instance = new ArrayInList();

	public static InList instance() {
		return instance;
	}

	protected ArrayInList() {
	}

	protected String columnAppend() {
		return " = ANY(?)";
	}

	protected <T> Array toArray(final Connection conn, final Collection<T> values) throws SQLException {
		return conn.createArrayOf(
				values.iterator().next() instanceof Number ? "number" : "text",
				values.toArray()
		);
	}

	public <T> InListObject inList(final Connection conn, final String columnName, final Collection<T> values) throws SQLException {
		return values == null || values.isEmpty() ? InListObject.empty : new ArrayListObject(
				columnName + columnAppend(),
				toArray(conn, values)
		);
	}

	class ArrayListObject extends InListObject {
		private final Array array;

		public ArrayListObject(final String sql, final Array array) {
			super(sql);
			this.array = array;
		}

		public Array getArray() {
			return array;
		}
	}
}
