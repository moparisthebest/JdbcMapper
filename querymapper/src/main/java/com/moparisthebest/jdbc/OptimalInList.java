package com.moparisthebest.jdbc;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

public class OptimalInList implements InList {

    private static final InList instance;

    public static InList instance() {
        return instance;
    }

    public static final Class<?> hsqlConnection, oracleConnection, postgreConnection, h2Connection;

    static {
        hsqlConnection = classForName("org.hsqldb.jdbc.JDBCConnection");
        oracleConnection = classForName("oracle.jdbc.OracleConnection");
        postgreConnection = classForName("org.postgresql.PGConnection");
        h2Connection = classForName("org.h2.jdbc.JdbcConnection");
        instance = new OptimalInList();
    }

    public static Class<?> classForName(final String className) {
        try {
            return Class.forName(className);
        } catch (Exception e) {
            return null;
        }
    }

    public static boolean isWrapperFor(final Connection conn, final Class<?> connectionClass) {
        if (connectionClass == null)
            return false;
        try {
            return conn.isWrapperFor(connectionClass);
        } catch (Exception e) {
            return false;
        }
    }

    private final InList any, oracle, unnest, bind;

    public OptimalInList(final InList any, final InList oracle, final InList unnest, final InList bind) {
        this.any = any;
        this.oracle = oracle;
        this.unnest = unnest;
        this.bind = bind;
    }

    protected OptimalInList() {
        this(ArrayInList.instance(), OracleArrayInList.instance(), UnNestArrayInList.instance(), BindInList.instance());
    }

    @Override
    public InList instance(Connection conn) {
        if (isWrapperFor(conn, postgreConnection)
                // java6 version of h2 doesn't support this
                //IFJAVA8_START
                || isWrapperFor(conn, h2Connection)
            //IFJAVA8_END
                )
            return any;
        if (isWrapperFor(conn, oracleConnection))
            return oracle;
        if (isWrapperFor(conn, hsqlConnection))
            return unnest;
        // works for everything
        return bind;
    }

    /**
     * Don't call this which inspect connection type each time, get an InList once with .instance(Connection) and use it forever
     *
     * @param conn
     * @param columnName Column name for query
     * @param values     values for in list
     * @param <T>
     * @return
     * @throws SQLException
     */
    @Override
    public <T> InListObject inList(Connection conn, String columnName, Collection<T> values) throws SQLException {
        return this.instance(conn).inList(conn, columnName, values);
    }

    /**
     * Don't call this which inspect connection type each time, get an InList once with .instance(Connection) and use it forever
     *
     * @param conn
     * @param columnName Column name for query
     * @param values     values for in list
     * @param <T>
     * @return
     * @throws SQLException
     */
    @Override
    public <T> InListObject notInList(Connection conn, String columnName, Collection<T> values) throws SQLException {
        return this.instance(conn).notInList(conn, columnName, values);
    }
}
