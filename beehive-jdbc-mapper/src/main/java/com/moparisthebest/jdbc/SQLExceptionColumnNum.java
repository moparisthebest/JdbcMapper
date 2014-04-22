package com.moparisthebest.jdbc;

import java.sql.SQLException;

public class SQLExceptionColumnNum extends SQLException {
	
	private final int columnNum;

	public SQLExceptionColumnNum(Throwable cause, int columnNum) {
		super("At column number "+columnNum, cause);
		this.columnNum = columnNum;
	}

	public int getColumnNum() {
		return columnNum;
	}
}
