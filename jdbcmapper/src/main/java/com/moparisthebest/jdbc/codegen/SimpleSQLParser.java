package com.moparisthebest.jdbc.codegen;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by mopar on 5/25/17.
 */
public class SimpleSQLParser extends AbstractSQLParser {

	private static final Pattern aliasPattern = Pattern.compile("^.*\\.");
	private static final Pattern parenPattern = Pattern.compile("\\([^)]+\\)");
	private static final Pattern fromPattern = Pattern.compile("[\\s)]+FROM[\\s(]+");

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
			// remove anything in parens, examples:
			// COALESCE(some_tom, 'UNKNOWN') as tom -> COALESCE() as tom
			// (SELECT some_column from some_table where other_column = 'YAY') as tom -> () as tom
			sql = parenPattern.matcher(sql).replaceAll("()").trim();
			// 6 is length of "SELECT" which we already verified it starts with...
			final Matcher fromMatcher = fromPattern.matcher(sql);
			// is a SELECT without a FROM valid? I guess maybe...
			final String columns = sql.substring(6, fromMatcher.find() ? fromMatcher.start() : sql.length());
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
