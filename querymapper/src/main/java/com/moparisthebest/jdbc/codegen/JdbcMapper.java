package com.moparisthebest.jdbc.codegen;

import com.moparisthebest.classgen.AbstractSQLParser;
import com.moparisthebest.classgen.SQLParser;

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

		boolean cachePreparedStatements() default true;

		Class<? extends SQLParser> sqlParser() default AbstractSQLParser.class;
	}

	@Retention(RetentionPolicy.SOURCE)
	@Target({ElementType.METHOD})
	public @interface SQL {
		/**
		 * The SQL statement to send to the database. Required annotation element.
		 */
		String value();

		OptionalBool cachePreparedStatement() default OptionalBool.INHERIT;

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

	public enum OptionalBool {
		INHERIT,
		TRUE,
		FALSE;

		public boolean combine(final boolean inherited) {
			switch (this) {
				case TRUE:
					return true;
				case FALSE:
					return false;
			}
			return inherited;
		}
	}
}
