package com.moparisthebest.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface PreparedStatementFactory {
    PreparedStatement prepareStatement(Connection conn, String sql) throws SQLException;
}
