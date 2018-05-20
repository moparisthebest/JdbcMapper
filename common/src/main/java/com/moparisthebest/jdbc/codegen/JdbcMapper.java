package com.moparisthebest.jdbc.codegen;

import java.io.Closeable;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.Connection;

/**
 * Created by mopar on 5/24/17.
 */
public interface JdbcMapper extends Closeable {

	Connection getConnection();

	@Override
	void close();

	@Retention(RetentionPolicy.SOURCE)
	@Target({ElementType.TYPE})
	public @interface Mapper {
		/**
		 * The jndi name of the DataSource. This is a required element for this annotation.
		 */
		String jndiName() default "";

		/**
		 * This defaults to true if a close() method exists to override/implement, false otherwise
		 */
		OptionalBool cachePreparedStatements() default OptionalBool.DEFAULT;

		/**
		 * This defaults to SimpleSQLParser, PrestoSQLParser is another option for Java 8, or implement your own
		 * @return
		 */
		Class<? extends SQLParser> sqlParser() default SQLParser.class;

		/**
		 * This defaults to false
		 */
		OptionalBool allowReflection() default OptionalBool.DEFAULT;

		/**
		 * This defaults to SimpleSQLParser, PrestoSQLParser is another option for Java 8, or implement your own
		 * @return
		 */
		DatabaseType databaseType() default DatabaseType.DEFAULT;
		String arrayNumberTypeName() default "";
		String arrayStringTypeName() default "";
	}

	@Retention(RetentionPolicy.SOURCE)
	@Target({ElementType.METHOD})
	/**
	 * Instead of a compile-time error this will only warn on unused params, mainly for debugging or presenting an unfinished API
	 */
	public @interface WarnOnUnusedParams {}

	@Retention(RetentionPolicy.SOURCE)
	@Target({ElementType.METHOD})
	/**
	 * Run this method in a transaction, useless on @SQL methods because they only run single statements, helpful on default or abstract methods that chain calls
	 */
	public @interface RunInTransaction {}

	@Retention(RetentionPolicy.SOURCE)
	@Target({ElementType.METHOD})
	public @interface SQL {
		/**
		 * The SQL statement to send to the database. Required annotation element.
		 */
		String value();

		/**
		 * This avoids parsing SQL for select statements by specifying column names directly, implies SELECT statement, ie isSelect = true
		 */
		String[] columnNames() default {};

		/**
		 * This avoids parsing SQL for non-select statements, set to false if executeUpdate should be ran instead of selection
		 */
		boolean isSelect() default true;

		/**
		 * This defaults to the value of the class-level @JdbcMapper.Mapper.cachePreparedStatements annotation, but can be configured on a per-method level here
		 */
		OptionalBool cachePreparedStatement() default OptionalBool.DEFAULT;

		/**
		 * This defaults to the value of the class-level @JdbcMapper.Mapper.allowReflection annotation, but can be configured on a per-method level here
		 */
		OptionalBool allowReflection() default OptionalBool.DEFAULT;

		/**
		 * Maximum rows returned in collections/maps/arrays/etc, < 1 mean no limit
		 *
		 * Use with care because you normally do not want truncated query results
		 *
		 * Can also send in dynamically to function as a primitive integer (long/int/short/byte) named one of the options
		 * in JdbcMapper.allowedMaxRowParamNames (default one of maxRows,rowLimit,arrayMaxLength if not set)
		 */
		long maxRows() default -1;
	}

	/**
	 * Equivalent to QueryMapper.toObject for arrays, and .toSingleMap for maps
	 */
	@Retention(RetentionPolicy.SOURCE)
	@Target({ElementType.METHOD})
	public @interface SingleRow {}

	public enum OptionalBool {
		DEFAULT,
		TRUE,
		FALSE;

		public boolean combine(final boolean def) {
			switch (this) {
				case TRUE:
					return true;
				case FALSE:
					return false;
			}
			return def;
		}
	}

	public enum DatabaseType {
		DEFAULT(null, null),
		STANDARD("numeric", "text"),
		UNNEST("NUMERIC", "VARCHAR"),
		ORACLE("ARRAY_NUM_TYPE", "ARRAY_STR_TYPE");

		public final String arrayNumberTypeName, arrayStringTypeName;

		private DatabaseType(final String arrayNumberTypeName, final String arrayStringTypeName) {
			this.arrayNumberTypeName = arrayNumberTypeName;
			this.arrayStringTypeName = arrayStringTypeName;
		}
	}
}
