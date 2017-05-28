package com.moparisthebest.classgen;

import java.util.regex.Pattern;

/**
 * Created by mopar on 5/25/17.
 */
public class SimpleSQLParser extends AbstractSQLParser {

	private static final Pattern aliasPattern = Pattern.compile("^.*\\.");

	public SimpleSQLParser() {
		super(null, false);
	}


	private SimpleSQLParser(final String[] columnNames, final boolean isSelect) {
		super(columnNames, isSelect);
	}

	@Override
	public SQLParser parse(String sql) {
		sql = sql.toUpperCase();
		final boolean isSelect = sql.startsWith("SELECT");
		String[] columnNames = null;
		if (isSelect) {
			final String columns = sql.substring(sql.indexOf("SELECT") + 6, sql.indexOf("FROM")).trim();
			final String[] splitColumns = columns.split(",");
			columnNames = new String[splitColumns.length + 1];
			int index = 0;
			for (String column : splitColumns) {
				final String[] singleSplit = column.split("\\s");
				// trim and remove leading table/alias
				columnNames[++index] = aliasPattern.matcher(singleSplit[singleSplit.length - 1].trim()).replaceFirst("");
			}
		}
		return new SimpleSQLParser(columnNames, isSelect);
	}
}
