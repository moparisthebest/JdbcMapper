package com.moparisthebest.jdbc;

import com.moparisthebest.jdbc.codegen.JdbcMapper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Array;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

/**
 * Created by mopar on 4/29/15.
 */
public class OracleArrayInList extends ArrayInList {

	private static final InList instance = new OracleArrayInList();

	public static InList instance() {
		return instance;
	}

	private static final Class<?> oracleConnection;
	private static final Method createArray;

	static {
		Class<?> oc;
		Method ca;
		try {
			oc = Class.forName("oracle.jdbc.OracleConnection");
			ca = oc.getDeclaredMethod("createOracleArray", String.class, Object.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		oracleConnection = oc;
		createArray = ca;
	}

	public OracleArrayInList(final String numberType, final String otherType) {
		super(numberType, otherType);
	}

	public OracleArrayInList() {
		this(JdbcMapper.DatabaseType.ORACLE.arrayNumberTypeName, JdbcMapper.DatabaseType.ORACLE.arrayStringTypeName);
	}

	protected String columnAppendIn(final String columnName) {
		return "(" + columnName + " IN(select column_value from table(?)))";
	}

	protected String columnAppendNotIn(final String columnName) {
		return "(" + columnName + " NOT IN(select column_value from table(?)))";
	}

	public Array toArray(final Connection conn, final String typeName, final Object[] elements) throws SQLException {
		//return conn.unwrap(oracle.jdbc.OracleConnection.class).createOracleArray(typeName, elements);
		try {
			return (Array) createArray.invoke(conn.unwrap(oracleConnection), typeName, elements);
		} catch (SQLException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e); // should never happen, but if it does don't hide it
		}
	}
}
