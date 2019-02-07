package com.moparisthebest.jdbc;

import com.moparisthebest.jdbc.codegen.JdbcMapper;

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

	protected final String numericType, otherType;

	public ArrayInList(final String numericType, final String otherType) {
		this.numericType = numericType;
		this.otherType = otherType;
	}

	public ArrayInList() {
		this(JdbcMapper.DatabaseType.ANY.arrayNumberTypeName, JdbcMapper.DatabaseType.ANY.arrayStringTypeName);
	}

	@Override
	public InList instance(Connection conn) {
		return this;
	}

	protected String columnAppendIn(final String columnName) {
		return "(" + columnName + " = ANY(?))";
	}

	protected String columnAppendNotIn(final String columnName) {
		return "(NOT(" + columnName + " = ANY(?)))";
	}

	public Array toArray(final Connection conn, final String typeName, final Object[] elements) throws SQLException {
		return conn.createArrayOf(typeName, elements);
	}

	public Array toArray(final Connection conn, final boolean numeric, final Object[] elements) throws SQLException {
		return toArray(conn, numeric ? numericType : otherType, elements);
	}

	protected <T> Array toArray(final Connection conn, final Collection<T> values) throws SQLException {
		return toArray(conn, values.iterator().next() instanceof Number, values.toArray());
	}

	public <T> InListObject inList(final Connection conn, final String columnName, final Collection<T> values) throws SQLException {
		return values == null || values.isEmpty() ? InListObject.inEmpty : new InListObject(
				columnAppendIn(columnName),
				toArray(conn, values)
		);
	}

	public <T> InListObject notInList(final Connection conn, final String columnName, final Collection<T> values) throws SQLException {
		return values == null || values.isEmpty() ? InListObject.notInEmpty : new InListObject(
				columnAppendNotIn(columnName),
				toArray(conn, values)
		);
	}
}
