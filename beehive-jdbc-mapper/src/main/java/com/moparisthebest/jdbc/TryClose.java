package com.moparisthebest.jdbc;

import javax.naming.Context;
import java.io.Closeable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class TryClose { 	// tries to close certain object types

	public static void tryClose(ResultSet obj) {
		if (obj == null)
			return;
		try {
			obj.close();
		} catch (Throwable e) {
			// ignore...
		}
	}

	public static void tryClose(Context obj) {
		if (obj == null)
			return;
		try {
			obj.close();
		} catch (Throwable e) {
			// ignore...
		}
	}

	public static void tryClose(Connection obj) {
		if (obj == null)
			return;
		try {
			obj.close();
		} catch (Throwable e) {
			// ignore...
		}
	}

	public static void tryClose(Statement obj) {
		if (obj == null)
			return;
		try {
			obj.close();
		} catch (Throwable e) {
			// ignore...
		}
	}

	public static void tryClose(Closeable obj) {
		if (obj == null)
			return;
		try {
			obj.close();
		} catch (Throwable e) {
			// ignore...
		}
	}
}
