/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * $Header:$
 */

package org.apache.beehive.controls.system.jdbc;

import org.apache.beehive.controls.api.bean.AnnotationConstraints;
import org.apache.beehive.controls.api.bean.AnnotationMemberTypes;
import org.apache.beehive.controls.api.bean.ControlInterface;
import org.apache.beehive.controls.api.properties.PropertySet;

import javax.naming.NamingException;
import javax.naming.Context;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.sql.SQLData;
import java.util.Calendar;
import java.util.List;
import java.util.Arrays;

/**
 * Simplifies access to a relational database from your Java code using SQL commands.
 * The Jdbc Control handles the work of connecting to, sending queries to, and ResultSet mapping from
 * the database. You don't need to know how to use JDBC in order to use the Jdbc Control, just basic SQL.
 * <p/>
 * To use a Jdbc Control create a .jcx file (java file with a .jcx extension) which extends this interface.
 * Add annotations to the jcx to tell the Jdbc Control how to connect to your database instance (either
 * ConnectionDataSource or ConnectionDriver), then add methods which include SQL annotations to access the database.
 */
@ControlInterface( checker = JdbcControlChecker.class )
public interface JdbcControl {


    /**
     * Returns a database connection to the server associated
     * with the control. It is typically not necessary to call this method
     * when using the control.
     *
     * @return A Connection a database.
     */
    public Connection getConnection() throws SQLException;

    /**
     * Sets the Calendar instance that should be used when setting and getting
     * {@link java.sql.Date Date}, {@link java.sql.Time Time}, and
     * {@link java.sql.Timestamp Timestamp} values.
     *
     * @see java.sql.ResultSet#getDate(int, Calendar) java.sql.ResultSet#getDate(int, Calendar)
     * @see java.sql.ResultSet#getTime(int, Calendar) java.sql.ResultSet#getTime(int, Calendar)
     * @see java.sql.ResultSet#getTimestamp(int, Calendar) java.sql.ResultSet#getTimestamp(int, Calendar)
     * @see java.sql.PreparedStatement#setDate(int, java.sql.Date, Calendar) java.sql.PreparedStatement#setDate(int, Date, Calendar)
     * @see java.sql.PreparedStatement#setTime(int, java.sql.Time, Calendar) java.sql.PreparedStatement#setTime(int, Time, Calendar)
     * @see java.sql.PreparedStatement#setTimestamp(int, java.sql.Timestamp, Calendar) java.sql.PreparedStatement#setTimestamp(int, Timestamp, Calendar)
     */
    public void setDataSourceCalendar(Calendar cal);

    /**
     * Gets the Calendar instance used when setting and getting
     * {@link java.sql.Date Date}, {@link java.sql.Time Time}, and
     * {@link java.sql.Timestamp Timestamp} values. This is the Calendar
     * set by the setDataSourceCalendar method.
     *
     * @return The Calendar instance.
     */
    public Calendar getDataSourceCalendar();




// ********************************************************************************************************************
// ********************************************************************************************************************
//                          Class-level Database Connection Annotations and Supporting Constructs
// ********************************************************************************************************************
// ********************************************************************************************************************


    /**
     * Abstract base class for a user defined Jndi Context factory which can be used
     * as a value for the jndiContextFactory member of the ConnectionDataSource
     * annotation.
     */
    public static abstract class JndiContextFactory {

        /**
         * Get a JNDI InitialContext instance.
         *
         * @return InitialContext instance
         * @throws NamingException if context could not be found.
         */
        public abstract Context getContext() throws NamingException;
    }

    /**
     * Class-level annotation for making a DataSource available for use with the Jdbc Control. Either this annotation or
     * the ConnectionDriver annotation must be set for a jcx which extends the JdbcControl interface.
     */
    @PropertySet(prefix = "ConnectionDataSource")
    @Inherited
    @AnnotationConstraints.AllowExternalOverride
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.FIELD})
    public @interface ConnectionDataSource {

        /**
         * The jndi name of the DataSource. This is a required element for this annotation.
         */
        @AnnotationMemberTypes.JndiName(resourceType = AnnotationMemberTypes.JndiName.ResourceType.DATASOURCE)
        String jndiName();

        /**
         * The name of a class which implements the IJndiContextFactory interface. This is an optional element of this annotation.
         */
        @AnnotationMemberTypes.Optional
        Class<? extends JndiContextFactory> jndiContextFactory() default DefaultJndiContextFactory.class;
    }


    /**
     * Class-level annotation for making a ConnectionDriver available for use with the Jdbc Control. Either this
     * annotation or the ConnectionDataSource annotation must be set for a jcx which extends the JdbcControl interface.
     * See java.sql.DatabaseConnection for additional information about the elements of this annotation.
     */
    @PropertySet(prefix = "ConnectionDriver")
    @Inherited
    @AnnotationConstraints.AllowExternalOverride
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.FIELD})
    public @interface ConnectionDriver {

        /**
         * A String containing the fully qualified name of the database driver class. Required element.
         */
        String databaseDriverClass();

        /**
         * A String containing the database URL to connect to. Required element.
         */
        String databaseURL();

        /**
         * A String containing the user name to connect to the database as. Optional element.
         */
        @AnnotationMemberTypes.Optional
        String userName()                               default "";

        /**
         * A String containing the password associated with userName. Optional element.
         */
        @AnnotationMemberTypes.Optional
        String password()                               default "";

        /**
         * A String containing a semicolon seperated list of name/value pairs for the DatabaseConnection.
         * The string must have the format of propertyName=propertyValue;propertyName=propertyValue;...
         * The properties will only be used if the userName and password elements of this annotation are
         * NOT set.
         * 
         * Optional element.
         */
        @AnnotationMemberTypes.Optional
        String properties()                             default "";
    }


    /**
     * Class level annotation used to set options on the JDBC connnection.
     */
    @PropertySet(prefix = "ConnectionOptions")
    @Inherited
    @AnnotationConstraints.AllowExternalOverride
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.FIELD})
    public @interface ConnectionOptions {

        /**
         * If set to true, database connection will optimize for read only queries, writes still permitted.
         * Optional, defaults to false.
         */
        @AnnotationMemberTypes.Optional
        boolean readOnly()                              default false;

        /**
         * Specifies ResultSet holdability for the connection.  May be overridden at method level.
         * Optional, defaults to jdbc driver's default setting.
         */
        @AnnotationMemberTypes.Optional
        HoldabilityType resultSetHoldability()          default HoldabilityType.DRIVER_DEFAULT;

        /**
         * Specifies type mappings for SQL user defined types (UDTs).  Any type mappings set here will be used
         * by the underlying JDBC Connection for UDT type mappings.  These mappings can be overridden by using
         * the SQL annotations methodTypeMappers element.  Optional element.
         */
        @AnnotationMemberTypes.Optional
        TypeMapper[] typeMappers()                      default {};
    }

    /**
     * Class / method level annotation for mapping SQL user defined types (UDTs) to and from java objects.
     * The mapper class element must implement the java.sql.SQLData interface.
     */
    @PropertySet(prefix = "TypeMapper")
    @Inherited
    @AnnotationConstraints.AllowExternalOverride
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.TYPE, ElementType.METHOD})
    public @interface TypeMapper {
        String UDTName();
        Class<? extends SQLData> mapperClass();
    }




// ********************************************************************************************************************
// ********************************************************************************************************************
//                          SQL Method-level Annotation and Supporting Constructs
// ********************************************************************************************************************
// ********************************************************************************************************************

    /**
     * This constant can be used as the value for the maxRows element of the SQL annotation.
     * It indicates that all rows should be returned (i.e. no limit)
     */
    public final int MAXROWS_ALL = 0;

    /**
     * The default fetch size for result sets, indicates the database should determine the fetch size.
     */
    public final int DEFAULT_FETCH_SIZE = 0;

    /**
     * Default value for the iteratorElementType element of the
     * SQL annotation.  It signals that no type has been defined for the method
     * (common if the method return type isn't itself an iterator)
     */
    public interface UndefinedIteratorType {
    }

    /**
     * Default value for the resultSetMapper element of the
     * SQL annotation.  It signals that no type has been defined for the method.
     */
    public interface UndefinedResultSetMapper {
    }

    /**
     * Enumeration of supported types of scrolling ResultSets
     */
    public enum ScrollType {
        DRIVER_DEFAULT (-1, -1),
        FORWARD_ONLY (ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY),
        SCROLL_INSENSITIVE (ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY),
        SCROLL_SENSITIVE (ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_READ_ONLY),
        FORWARD_ONLY_UPDATABLE (ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE),
        SCROLL_INSENSITIVE_UPDATABLE (ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE),
        SCROLL_SENSITIVE_UPDATABLE (ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);

        private final int _type;
        private final int _concurrencyType;

        ScrollType(int scrollType, int concurrencyType) {
            _type = scrollType;
            _concurrencyType = concurrencyType;
        }

        public int getType() { return _type; }

        public int getConcurrencyType() { return _concurrencyType; }

        public String toString() {
            StringBuilder sb = new StringBuilder();
            if (_type == ResultSet.TYPE_FORWARD_ONLY) {
                sb.append("Foward Only, ");
            } else if (_type == ResultSet.TYPE_SCROLL_INSENSITIVE) {
                sb.append("Scroll Insensitive, ");
            } else if (_type == ResultSet.TYPE_SCROLL_SENSITIVE) {
                sb.append("Scroll Sensitive, ");
            } else {
                sb.append("Jdbc Driver Default Direction");
            }

            if (_concurrencyType == ResultSet.CONCUR_READ_ONLY) {
                sb.append("Read Only");
            } else if (_concurrencyType == ResultSet.CONCUR_UPDATABLE) {
                sb.append("Updatable");
            } else {
                sb.append("Jdbc Driver Default");
            }
            return sb.toString();
        }
    }

    /**
     * Enumeration of supported fetch directions.
     */
    public enum FetchDirection {
        FORWARD (ResultSet.FETCH_FORWARD),
        REVERSE (ResultSet.FETCH_REVERSE),
        UNKNOWN (ResultSet.FETCH_UNKNOWN);

        private final int _direction;

        FetchDirection(int direction) {
            _direction = direction;
        }

        public int getDirection() { return _direction; }
    }

    /**
     * Enumeration of supported fetch directions.
     */
    public enum HoldabilityType {
        DRIVER_DEFAULT (0),
        HOLD_CURSORS (ResultSet.HOLD_CURSORS_OVER_COMMIT),
        CLOSE_CURSORS (ResultSet.CLOSE_CURSORS_AT_COMMIT);

        private final int _holdability;

        HoldabilityType(int holdability) {
            _holdability = holdability;
        }

        public int getHoldability() { return _holdability; }

        public String toString() {
            if (_holdability == ResultSet.HOLD_CURSORS_OVER_COMMIT) {
                return "HOLD_CURSORS_OVER_COMMIT";
            } else if (_holdability == ResultSet.CLOSE_CURSORS_AT_COMMIT) {
                return "CLOSE_CURSORS_AT_COMMIT";
            } else {
                return "Default driver holdability";
            }
        }
    }

    /**
     * Method-level annotation for methods in a jcx which wish to access a database instance.
     */
    @PropertySet(prefix = "SQL")
    @Inherited
    @Retention(RetentionPolicy.RUNTIME)
    @AnnotationConstraints.AllowExternalOverride
    @Target({ElementType.METHOD})
    public @interface SQL {

        /**
         * The SQL statement to send to the database. Required annotation element.
         */
        String statement();


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
        @AnnotationMemberTypes.Optional
        int arrayMaxLength()                            default 1024;


        /**
         * Max number of ResultSet rows to return.
         * If used with arrayMaxLength the smaller value is used.
         * Optional element, default value is no limit on number of rows returned.
         */
        @AnnotationMemberTypes.Optional
        int maxRows()                                   default MAXROWS_ALL;


        /**
         * Execute the SQL statement as a batch update.
         * Methods which have this element set to true must return an array of ints.
         * Optional element, defaults to false.
         */
        @AnnotationMemberTypes.Optional
        boolean batchUpdate()                           default false;


        /**
         * Specify the fetch size for the ResultSet. Optional element, defaults to 0.
         */
        @AnnotationMemberTypes.Optional
        int fetchSize()                                 default DEFAULT_FETCH_SIZE;


        /**
         * Specify the fetch direction for the ResultSEt. Optional element, defaults to FORWARD.
         */
        @AnnotationMemberTypes.Optional
        FetchDirection fetchDirection()                 default FetchDirection.FORWARD;


        /**
         * Return the generated key values generated by the SQL statement. Optional element, defaults to false.
         */
        @AnnotationMemberTypes.Optional
        boolean getGeneratedKeys()                      default false;


        /**
         * Specify generated key columns by column names to return when the getGeneratedKeys element is true.
         * May only be set if getGeneratedKeys is set to true, otherwise a compile time error is generated.
         * Optional element.
         */
        @AnnotationMemberTypes.Optional
        String[] generatedKeyColumnNames()              default {};


        /**
         * Specify generated key columns by column number to return when the getGeneratedKeys element is true.
         * May only be set if getGeneratedKeys is set to true, otherwise a compile time error is generated
         * Optional element.
         */
        @AnnotationMemberTypes.Optional
        int[] generatedKeyColumnIndexes()               default {};


        /**
         * Specify the holdability type for the annotated method.  Overrides the holability annotation element
         * of the ConnectionOptions annotation.  The holdability type will be in effect for the duration of this
         * method call. Optional, defaults to DRIVER_DEFAULT.
         */
        @AnnotationMemberTypes.Optional
        HoldabilityType resultSetHoldabilityOverride()  default HoldabilityType.DRIVER_DEFAULT;


        /**
         * Specifies type mappings for SQL user defined types (UDTs).  Any type mappings set here will be used
         * by the underlying JDBC Connection for UDT type mappings. These type mappings will REPLACE any set on
         * the JDBC connection for the duration of the method call. Optional element.
         */
        @AnnotationMemberTypes.Optional
        TypeMapper[] typeMappersOverride()              default {};


        /**
         * Specify the type of element to be interated over when the method's return type is java.util.Iterator.
         * Optional element.
         */
        @AnnotationMemberTypes.Optional
        Class iteratorElementType()                     default UndefinedIteratorType.class;


        /**
         * Specify a custom result set mapper for the ResultSet generated by the SQL statement.
         * ResultSet mappers must extend the ResultSetMapper abstract base class.  If a value is specified
         * it will be used to map the ResultSet of the query to the return type of the method.
         * See org.apache.beehive.controls.system.jdbc.ResultSetMapper for additional information.
         * Optional element.
         */
        @AnnotationMemberTypes.Optional
        Class resultSetMapper()                         default UndefinedResultSetMapper.class;


        /**
         * Specify that the ResultSet returned by the method is scrollable. Valid only for methods which
         * return a ResultSet, otherwise a compile-time error will occur.  Valid element values
         * are defined by the ScrollType enumeration.
         * Optional element, defaults to JDBC driver's default setting.
         */
        @AnnotationMemberTypes.Optional
        ScrollType scrollableResultSet()                default ScrollType.DRIVER_DEFAULT;
    } // SQL annotation declaration


// ********************************************************************************************************************
// ********************************************************************************************************************
//                                               Inner Classes
// ********************************************************************************************************************
// ********************************************************************************************************************

    /**
     * Nested class used for specifing parameters for a callable statement.  If a method in a control extension takes an array of
     * SQLParameter, the JdbcControl treats the SQL as a CallableStatement and inserts values into the statement from
     * the SQLParameter array.  After the CallableStatement executes, results are mapped into OUT type parameters found
     * int the SQLParameter array.
     * NOTE: To invoke a callable statement which does not take any arguments, an SQLParameter array of size zero must
     * be passed to the JDBCControl method.
     */
    public static class SQLParameter {
        /**
         * IN direction constant.
         */
        public static final int IN = 1;
        /**
         * OUT direction constant.
         */
        public static final int OUT = 2;
        /**
         * IN and OUT directions constant.
         */
        public static final int INOUT = IN | OUT;

        /**
         * Parameter value. For parameters of type OUT this value should be set to null.
         */
        public Object value = null;

        /**
         * Parameter SQL data type. See java.sql.Types.
         */
        public int type = Types.NULL;

        /**
         * Parameter direction.
         */
        public int dir = IN;

        /**
         * Create a new SQLParameter with the specified value.
         *
         * @param value The parameter value.
         */
        public SQLParameter(Object value) {
            this.value = value;
        }

        /**
         * Create a new SQLParameter with the specified value and SQL data type.
         *
         * @param value The parameter value.
         * @param type  SQL data type.
         */
        public SQLParameter(Object value, int type) {
            this(value);
            this.type = type;
        }

        /**
         * Create a new SQLParameter with the specified value, SQL data type and direction.
         *
         * @param value The parameter value.
         * @param type  SQL data type.
         * @param dir   IN / OUT or INOUT
         */
        public SQLParameter(Object value, int type, int dir) {
            this(value, type);
            this.dir = dir;
        }

        /**
         * Clone this parameter.
         *
         * @return A copy of this parameter.
         */
        public Object clone() {
            return new SQLParameter(value, type, dir);
        }
    }

    /**
     * A ComplexSqlFragment can be used as a return value from a parameter reflection operation for
     * return values which contain BOTH SQL text and parameters.  For Example, the text portion
     * could be something like 'where NAME = ?' and the parameter value is 'Fred'.
     */
    public static class ComplexSqlFragment {

        protected CharSequence sql;
        protected List<SQLParameter> parameters;

        /**
         * Create a new SQLFragment.
         */
        public ComplexSqlFragment() {
            sql = null;
            parameters = null;
        }

        /**
         * Create a new SQLFragment with the specified SQL and parameter list.
         *
         * @param sql        SQL contents of the fragment.
         * @param parameters Substitution parameters.
         */
        public ComplexSqlFragment(String sql, SQLParameter[] parameters) {
            this.sql = sql;
            if (null != parameters)
                this.parameters = Arrays.asList(parameters);
        }

        /**
         * Get the SQL of this fragment.
         *
         * @return String.
         */
        public String getSQL() {
            return sql.toString();
        }

        /**
         * Get the parameters contained within this fragment.
         * Returns a zero-based array.
         *
         * @return SQLParameter array.
         */
        public SQLParameter[] getParameters() {
            if (null == parameters)
                return new SQLParameter[0];
            return parameters.toArray(new SQLParameter[parameters.size()]);
        }

        /**
         * Get the SQL string contained within this fragment.
         * @return String.
         */
        public String toString() {
            return sql.toString();
        }
    }
}
