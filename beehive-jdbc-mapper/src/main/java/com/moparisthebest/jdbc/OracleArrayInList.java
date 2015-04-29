package com.moparisthebest.jdbc;

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

	protected OracleArrayInList() {
	}

	protected String columnAppend() {
		return " IN(select column_value from table(?))";
	}

	protected <T> Array toArray(final Connection conn, final Collection<T> values) throws SQLException {
		/*
		return conn.unwrap(oracle.jdbc.OracleConnection.class).createOracleArray(
				values.iterator().next() instanceof Number ? "ARRAY_NUM_TYPE" : "ARRAY_STR_TYPE",
				values.toArray()
		);
		*/
		try {
			return (Array) createArray.invoke(conn.unwrap(oracleConnection),
					values.iterator().next() instanceof Number ? "ARRAY_NUM_TYPE" : "ARRAY_STR_TYPE",
					values.toArray()
			);
		} catch (SQLException e) {
			throw e;
		} catch (Exception e) {
			throw new RuntimeException(e); // should never happen, but if it does don't hide it
		}
	}
}
