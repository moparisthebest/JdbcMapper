package com.moparisthebest.classgen;

/**
 * Created by mopar on 5/25/17.
 */
public abstract class AbstractSQLParser implements SQLParser {

	private final String[] columnNames;
	private final boolean isSelect;

	protected AbstractSQLParser(final String[] columnNames, final boolean isSelect) {
		this.columnNames = columnNames;
		this.isSelect = isSelect;
	}

	@Override
	public final String[] columnNames() {
		return columnNames;
	}

	@Override
	public final boolean isSelect() {
		return isSelect;
	}

}