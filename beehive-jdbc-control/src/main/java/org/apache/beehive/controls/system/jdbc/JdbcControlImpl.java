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

import java.lang.reflect.Method;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Vector;
import javax.naming.NamingException;
import javax.naming.Context;
import javax.sql.DataSource;

import org.apache.beehive.controls.api.ControlException;
import org.apache.beehive.controls.api.bean.ControlImplementation;
import org.apache.beehive.controls.api.bean.Extensible;
import org.apache.beehive.controls.api.context.ControlBeanContext;
import org.apache.beehive.controls.api.context.ResourceContext;
import org.apache.beehive.controls.api.context.ResourceContext.ResourceEvents;
import org.apache.beehive.controls.api.events.EventHandler;
import org.apache.beehive.controls.system.jdbc.parser.SqlParser;
import org.apache.beehive.controls.system.jdbc.parser.SqlStatement;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * The implementation class for the database controller.
 */
@ControlImplementation
public class JdbcControlImpl implements JdbcControl, Extensible, java.io.Serializable {

    //
    // contexts provided by the beehive controls runtime
    //
    @org.apache.beehive.controls.api.context.Context
    protected ControlBeanContext _context;
    @org.apache.beehive.controls.api.context.Context
    protected ResourceContext _resourceContext;

    protected transient Connection _connection;
    protected transient ConnectionDataSource _connectionDataSource;
    protected transient DataSource _dataSource;
    protected transient ConnectionDriver _connectionDriver;

    private Calendar _cal;
    private transient Vector<PreparedStatement> _resources;

    private static final String EMPTY_STRING = "";
    private static final Log LOGGER = LogFactory.getLog(JdbcControlImpl.class);
    private static final ResultSetMapper DEFAULT_MAPPER = new DefaultObjectResultSetMapper();
    private static final SqlParser _sqlParser = new SqlParser();

    protected static final HashMap<Class, ResultSetMapper> _resultMappers = new HashMap<Class, ResultSetMapper>();
    protected static Class<?> _xmlObjectClass;

    //
    // initialize the result mapper table
    //
    static {
        _resultMappers.put(ResultSet.class, new DefaultResultSetMapper());
        _resultMappers.put(Iterator.class, new DefaultIteratorResultSetMapper());

        try {
            _xmlObjectClass = Class.forName("org.apache.xmlbeans.XmlObject");
            _resultMappers.put(_xmlObjectClass, new DefaultXmlObjectResultSetMapper());
        } catch (ClassNotFoundException e) {
            // noop: OK if not found, just can't support mapping to an XmlObject
        }
    }

    /**
     * Constructor
     */
    public JdbcControlImpl() { }

    /**
     * Invoked by the controls runtime when a new instance of this class is aquired by the runtime
     */
    @EventHandler(field = "_resourceContext", eventSet = ResourceEvents.class, eventName = "onAcquire")
    public void onAquire() {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Enter: onAquire()");
        }

        try {
            getConnection();
        } catch (SQLException se) {
            throw new ControlException("SQL Exception while attempting to connect to database.", se);
        }
    }

    /**
     * Invoked by the controls runtime when an instance of this class is released by the runtime
     */
    @EventHandler(field = "_resourceContext", eventSet = ResourceContext.ResourceEvents.class, eventName = "onRelease")
    public void onRelease() {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Enter: onRelease()");
        }

        for (PreparedStatement ps : getResources()) {
            try {
                ps.close();
            } catch (SQLException sqe) {
            }
        }
        getResources().clear();

        if (_connection != null) {
            try {
                _connection.close();
            } catch (SQLException e) {
                throw new ControlException("SQL Exception while attempting to close database connection.", e);
            }
        }

        _connection = null;
        _connectionDataSource = null;
        _connectionDriver = null;
    }

    /**
     * Returns a database connection to the server associated with the control.
     * The connection type is specified by a ConnectionDataSource or ConnectionDriver annotation on the control class
     * which extends this control.
     * <p/>
     * It is typically not necessary to call this method when using the control.
     */
    public Connection getConnection() throws SQLException {

        if (_connection == null) {

            _connectionDataSource = _context.getControlPropertySet(ConnectionDataSource.class);
            _connectionDriver = _context.getControlPropertySet(ConnectionDriver.class);
            final ConnectionOptions connectionOptions = _context.getControlPropertySet(ConnectionOptions.class);

            if (_connectionDataSource != null && _connectionDataSource.jndiName() != null) {
                _connection = getConnectionFromDataSource(_connectionDataSource.jndiName(),
                                                          _connectionDataSource.jndiContextFactory());

            } else if (_connectionDriver != null && _connectionDriver.databaseDriverClass() != null) {
                _connection = getConnectionFromDriverManager(_connectionDriver.databaseDriverClass(),
                                                             _connectionDriver.databaseURL(),
                                                             _connectionDriver.userName(),
                                                             _connectionDriver.password(),
                                                             _connectionDriver.properties());
            } else {
                throw new ControlException("no @\'" + ConnectionDataSource.class.getName()
                                           + "\' or \'" + ConnectionDriver.class.getName() + "\' property found.");
            }

            //
            // set any specifed connection options
            //
            if (connectionOptions != null) {

                if (_connection.isReadOnly() != connectionOptions.readOnly()) {
                    _connection.setReadOnly(connectionOptions.readOnly());
                }

                DatabaseMetaData dbMetadata = _connection.getMetaData();

                final HoldabilityType holdability = connectionOptions.resultSetHoldability();
                if (holdability != HoldabilityType.DRIVER_DEFAULT) {
                    if (dbMetadata.supportsResultSetHoldability(holdability.getHoldability())) {
                        _connection.setHoldability(holdability.getHoldability());
                    } else {
                        throw new ControlException("Database does not support ResultSet holdability type: "
                                                   + holdability.toString());
                    }
                }

                setTypeMappers(connectionOptions.typeMappers());
            }
        }

        return _connection;
    }


    /**
     * Called by the Controls runtime to handle calls to methods of an extensible control.
     *
     * @param method The extended operation that was called.
     * @param args   Parameters of the operation.
     * @return The value that should be returned by the operation.
     * @throws Throwable any exception declared on the extended operation may be
     *                   thrown.  If a checked exception is thrown from the implementation that is not declared
     *                   on the original interface, it will be wrapped in a ControlException.
     */
    public Object invoke(Method method, Object[] args) throws Throwable {

        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Enter: invoke()");
        }
        assert _connection.isClosed() == false : "invoke(): JDBC Connection has been closed!!!!";
        return execPreparedStatement(method, args);
    }

    /**
     * Sets the {@link Calendar} used when working with time/date types
     */
    public void setDataSourceCalendar(Calendar cal) {
        _cal = (Calendar) cal.clone();
    }

    /**
     * Returns the {@link Calendar} used when working with time/date types.
     *
     * @return the {@link Calendar} to use with this {@link DataSource}
     */
    public Calendar getDataSourceCalendar() {
        return _cal;
    }

// /////////////////////////////////////////// Protected Methods ////////////////////////////////////////////


    /**
     * Create and exec a {@link PreparedStatement}
     *
     * @param method the method to invoke
     * @param args the method's arguments
     * @return the return value from the {@link PreparedStatement}
     * @throws Throwable any exception that occurs; the caller should handle these appropriately
     */
    protected Object execPreparedStatement(Method method, Object[] args)
            throws Throwable {

        final SQL methodSQL = (SQL) _context.getMethodPropertySet(method, SQL.class);
        if (methodSQL == null || methodSQL.statement() == null) {
            throw new ControlException("Method " + method.getName() + " is missing @SQL annotation");
        }

        setTypeMappers(methodSQL.typeMappersOverride());

        //
        // build the statement and execute it
        //

        PreparedStatement ps = null;
        try {
            Class returnType = method.getReturnType();

            SqlStatement sqlStatement = _sqlParser.parse(methodSQL.statement());
            ps = sqlStatement.createPreparedStatement(_context, _connection, _cal, method, args);

            if (LOGGER.isInfoEnabled()) {
                LOGGER.info("PreparedStatement: "
                            + sqlStatement.createPreparedStatementString(_context, _connection,  method, args));
            }

            //
            // special processing for batch updates
            //
            if (sqlStatement.isBatchUpdate()) {
                return ps.executeBatch();
            }

            //
            // execute the statement
            //
            boolean hasResults = ps.execute();


            //
            // callable statement processing
            //
            if (sqlStatement.isCallableStatement()) {
                SQLParameter[] params = (SQLParameter[]) args[0];
                for (int i = 0; i < params.length; i++) {
                    if (params[i].dir != SQLParameter.IN) {
                        params[i].value = ((CallableStatement) ps).getObject(i + 1);
                    }
                }
                return null;
            }


            //
            // process returned data
            //
            ResultSet rs = null;
            int updateCount = ps.getUpdateCount();

            if (hasResults) {
                rs = ps.getResultSet();
            }

            if (sqlStatement.getsGeneratedKeys()) {
                rs = ps.getGeneratedKeys();
                hasResults = true;
            }

            if (!hasResults && updateCount > -1) {
                boolean moreResults = ps.getMoreResults();
                int tempUpdateCount = ps.getUpdateCount();

                while ((moreResults && rs == null) || tempUpdateCount > -1) {
                    if (moreResults) {
                        rs = ps.getResultSet();
                        hasResults = true;
                        moreResults = false;
                        tempUpdateCount = -1;
                    } else {
                        moreResults = ps.getMoreResults();
                        tempUpdateCount = ps.getUpdateCount();
                    }
                }
            }

            Object returnObject = null;
            if (hasResults) {

                //
                // if a result set mapper was specified in the methods annotation, use it
                // otherwise find the mapper for the return type in the hashmap
                //
                final Class resultSetMapperClass = methodSQL.resultSetMapper();
                final ResultSetMapper rsm;
                if (!UndefinedResultSetMapper.class.isAssignableFrom(resultSetMapperClass)) {
                    if (ResultSetMapper.class.isAssignableFrom(resultSetMapperClass)) {
                        rsm = (ResultSetMapper) resultSetMapperClass.newInstance();
                    } else {
                        throw new ControlException("Result set mappers must be subclasses of ResultSetMapper.class!");
                    }
                } else {
                    if (_resultMappers.containsKey(returnType)) {
                        rsm = _resultMappers.get(returnType);
                    } else {
                        if (_xmlObjectClass != null && _xmlObjectClass.isAssignableFrom(returnType)) {
                            rsm = _resultMappers.get(_xmlObjectClass);
                        } else {
                            rsm = DEFAULT_MAPPER;
                        }
                    }
                }

                returnObject = rsm.mapToResultType(_context, method, rs, _cal);
                if (rsm.canCloseResultSet() == false) {
                    getResources().add(ps);
                }

                //
                // empty ResultSet
                //
            } else {
                if (returnType.equals(Void.TYPE)) {
                    returnObject = null;
                } else if (returnType.equals(Integer.TYPE)) {
                    returnObject = new Integer(updateCount);
                } else if (!sqlStatement.isCallableStatement()) {
                    throw new ControlException("Method " + method.getName() + "is DML but does not return void or int");
                }
            }
            return returnObject;

        } finally {
            // Keep statements open that have in-use result sets
            if (ps != null && !getResources().contains(ps)) {
                ps.close();
            }
        }
    }

// /////////////////////////////////////////// Private Methods ////////////////////////////////////////////

    /**
     * Get a connection from a DataSource.
     *
     * @param jndiName    Specifed in the subclasse's ConnectionDataSource annotation
     * @param jndiFactory Specified in the subclasse's ConnectionDataSource Annotation.
     * @return null if a connection cannot be established
     * @throws SQLException
     */
    private Connection getConnectionFromDataSource(String jndiName,
                                                   Class<? extends JdbcControl.JndiContextFactory> jndiFactory)
            throws SQLException
    {

        Connection con = null;
        try {
            JndiContextFactory jf = (JndiContextFactory) jndiFactory.newInstance();
            Context jndiContext = jf.getContext();
            _dataSource = (DataSource) jndiContext.lookup(jndiName);
            con = _dataSource.getConnection();
        } catch (IllegalAccessException iae) {
            throw new ControlException("IllegalAccessException:", iae);
        } catch (InstantiationException ie) {
            throw new ControlException("InstantiationException:", ie);
        } catch (NamingException ne) {
            throw new ControlException("NamingException:", ne);
        }
        return con;
    }

    /**
     * Get a JDBC connection from the DriverManager.
     *
     * @param dbDriverClassName Specified in the subclasse's ConnectionDriver annotation.
     * @param dbUrlStr          Specified in the subclasse's ConnectionDriver annotation.
     * @param userName          Specified in the subclasse's ConnectionDriver annotation.
     * @param password          Specified in the subclasse's ConnectionDriver annotation.
     * @return null if a connection cannot be established.
     * @throws SQLException
     */
    private Connection getConnectionFromDriverManager(String dbDriverClassName, String dbUrlStr,
                                                      String userName, String password, String propertiesString)
            throws SQLException
    {

        Connection con = null;
        try {
            Class.forName(dbDriverClassName);
            if (!EMPTY_STRING.equals(userName)) {
                con = DriverManager.getConnection(dbUrlStr, userName, password);
            } else if (!EMPTY_STRING.equals(propertiesString)) {
                Properties props = parseProperties(propertiesString);
                if (props == null) {
                    throw new ControlException("Invalid properties annotation value: " + propertiesString);
                }
                con = DriverManager.getConnection(dbUrlStr, props);
            } else {
                con = DriverManager.getConnection(dbUrlStr);
            }
        } catch (ClassNotFoundException e) {
            throw new ControlException("Database driver class not found!", e);
        }
        return con;
    }

    /**
     * Get the Vector of Statements which we need to keep open.
     * @return Vector of PreparedStatement
     */
    private Vector<PreparedStatement> getResources() {
        if (_resources == null) {
            _resources = new Vector<PreparedStatement>();
        }
        return _resources;
    }

    /**
     * Parse the propertiesString into a Properties object.  The string must have the format of:
     * propertyName=propertyValue;propertyName=propertyValue;...
     *
     * @param propertiesString
     * @return A Properties instance or null if parse fails
     */
    private Properties parseProperties(String propertiesString) {
        Properties properties = null;
        String[] propPairs = propertiesString.split(";");
        if (propPairs.length > 0) {
            properties = new Properties();
            for (String propPair : propPairs) {
                int eq = propPair.indexOf('=');
                assert eq > -1 : "Invalid properties syntax: " + propertiesString;
                properties.put(propPair.substring(0, eq), propPair.substring(eq + 1, propPair.length()));
            }
        }
        return properties;
    }

    /**
     * Set any custom type mappers specifed in the annotation for the connection.  Used for mapping SQL UDTs to
     * java classes.
     *
     * @param typeMappers An array of TypeMapper.
     */
    private void setTypeMappers(TypeMapper[] typeMappers) throws SQLException {

        if (typeMappers.length > 0) {
            Map<String, Class<?>> mappers = _connection.getTypeMap();
            for (TypeMapper t : typeMappers) {
                mappers.put(t.UDTName(), t.mapperClass());
            }
            _connection.setTypeMap(mappers);
        }
    }
}
