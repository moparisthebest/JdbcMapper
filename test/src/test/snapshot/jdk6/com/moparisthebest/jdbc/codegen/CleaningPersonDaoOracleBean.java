package com.moparisthebest.jdbc.codegen;

import com.moparisthebest.jdbc.Factory;

import java.sql.*;

import static com.moparisthebest.jdbc.util.ResultSetUtil.*;
import static com.moparisthebest.jdbc.TryClose.tryClose;

public class CleaningPersonDaoOracleBean implements CleaningPersonDao {

	private final Connection conn;
	private final boolean closeConn;


	private CleaningPersonDaoOracleBean(final Connection conn, final boolean closeConn) {
		this.conn = conn;
		this.closeConn = closeConn;
		if (this.conn == null)
			throw new NullPointerException("Connection needs to be non-null for JdbcMapper...");
	}

	public CleaningPersonDaoOracleBean(Connection conn) {
		this(conn, false);
	}

	public Connection getConnection() {
		return this.conn;
	}

	@Override
	public com.moparisthebest.jdbc.dto.FieldPerson getPerson(final long personNo, final com.moparisthebest.jdbc.Cleaner<com.moparisthebest.jdbc.dto.FieldPerson> personCleaner) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT person_no, first_name, last_name, birth_date FROM person WHERE person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final com.moparisthebest.jdbc.dto.FieldPerson ret = new com.moparisthebest.jdbc.dto.FieldPerson(
rs.getLong(1),
com.moparisthebest.jdbc.util.ResultSetUtil.getUtilDate(rs, 4),
rs.getString(2),
rs.getString(3));
				return personCleaner == null ? ret : personCleaner.clean(ret);
			} else {
				return null;
			}
		} finally {
			tryClose(rs);
			tryClose(ps);
		}
	}
}
