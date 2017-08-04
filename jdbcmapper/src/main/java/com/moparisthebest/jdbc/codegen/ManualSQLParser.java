package com.moparisthebest.jdbc.codegen;

/**
 * Created by mopar on 5/25/17.
 */
class ManualSQLParser extends AbstractSQLParser {

	private static final SQLParser notSelect = new ManualSQLParser(null, false);

	private ManualSQLParser(final String[] columnNames, final boolean isSelect) {
		super(columnNames, isSelect);
	}

	public static SQLParser getSQLParser(final JdbcMapper.SQL sql, final SQLParser parser, final String sqlStatement) {
		if(sql.columnNames().length != 0) {
			final String[] newColumnNames = new String[sql.columnNames().length + 1];
			int count = 0;
			for (final String columnName : sql.columnNames())
				newColumnNames[++count] = columnName.toUpperCase();
			return new ManualSQLParser(newColumnNames, true);
		} else if(!sql.isSelect()) {
			return notSelect;
		} else {
			return parser.parse(sqlStatement);
		}
	}

	@Override
	public SQLParser parse(final String sql) {
		throw new UnsupportedOperationException("use getDefinedColumns()");
	}
}
