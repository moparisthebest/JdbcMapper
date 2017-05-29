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
	}

	@Retention(RetentionPolicy.SOURCE)
	@Target({ElementType.METHOD})
	public @interface SQL {
		/**
		 * The SQL statement to send to the database. Required annotation element.
		 */
		String value();

		/**
		 * This defaults to the value of the class-level @JdbcMapper.Mapper.cachePreparedStatements annotation, but can be configured on a per-method level here
		 */
		OptionalBool cachePreparedStatement() default OptionalBool.DEFAULT;

		/**
		 * Maximum array length.
		 * Optional element.
		 * This element has no effect on the call unless the method return type is an array.
		 * When used in conjunction with the maxRows element, the size of the array generated
		 * from the result set will be the smaller of maxRows and arrayMaxLength.
		 * <p>
		 * arrayMaxLength's default value is 1024, but may be set to zero to specify that
		 * there is no size limit for the array generated from the ResultSet.
		 * Since the generated array is stored in-memory, care should be taken when dealing
		 * with very large ResultSets when the value of this element is set to zero.
		 */
		int arrayMaxLength() default -1;
	}

	// these are to annotate parameters for special bind to PreparedStatement behavior

	/**
	 * Use PreparedStatement.setBlob
	 *
	 * Only required for String
	 *
	 * Also valid for byte[], InputStream, Blob, File. But for those .setBlob is called even without this annotation
	 */
	@Retention(RetentionPolicy.SOURCE)
	@Target({ElementType.PARAMETER})
	public @interface Blob {
		/**
		 * The charsetName sent into String.getBytes()
		 */
		String charsetName() default "UTF-8";
	}

	/**
	 * Use PreparedStatement.setClob
	 *
	 * Only valid for String
	 *
	 * Also valid for Clob, Reader. But for those .setClob is called even without this annotation
	 */
	@Retention(RetentionPolicy.SOURCE)
	@Target({ElementType.PARAMETER})
	public @interface Clob {}

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
}
