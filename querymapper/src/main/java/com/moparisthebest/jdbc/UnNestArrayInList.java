package com.moparisthebest.jdbc;

/**
 * HSQLDB requires array in lists to be implemented this way:
 * https://stackoverflow.com/questions/35939489/createarrayof-string-in-hsqldb-jdbc-returns-abstractmethoderror/35964424#35964424
 */
public class UnNestArrayInList extends ArrayInList {

	private static final InList instance = new UnNestArrayInList();

	public static InList instance() {
		return instance;
	}

	public UnNestArrayInList(final String numberType, final String otherType) {
		super(numberType, otherType);
	}

	public UnNestArrayInList() {
		super();
	}

	protected String columnAppend(final String columnName) {
		return "(" + columnName + " IN(UNNEST(?)))";
	}
}
