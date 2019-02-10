package com.moparisthebest.jdbc.codegen;

import com.moparisthebest.jdbc.Factory;

import java.sql.*;

import static com.moparisthebest.jdbc.util.ResultSetUtil.*;
import static com.moparisthebest.jdbc.TryClose.tryClose;

public class AbstractDaoUnNestBean extends AbstractDao {

	private final Connection conn;
	private final boolean closeConn;


	private AbstractDaoUnNestBean(final Connection conn, final boolean closeConn) {
		this.conn = conn;
		this.closeConn = closeConn;
		if (this.conn == null)
			throw new NullPointerException("Connection needs to be non-null for JdbcMapper...");
	}

	public AbstractDaoUnNestBean(Connection conn) {
		this(conn, false);
	}

	public Connection getConnection() {
		return this.conn;
	}

	@Override
	public com.moparisthebest.jdbc.dto.FieldPerson getPerson(final long personNo) throws java.sql.SQLException {
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
				return ret;
			} else {
				return null;
			}
		} finally {
			tryClose(rs);
			tryClose(ps);
		}
	}

	@Override
	public long getPersonNo(final java.lang.String lastName) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT person_no FROM person WHERE last_name = ?");
			ps.setObject(1, lastName);
			rs = ps.executeQuery();
			if(rs.next()) {
final long ret = rs.getLong(1);
				return ret;
			} else {
				return 0;
			}
		} finally {
			tryClose(rs);
			tryClose(ps);
		}
	}
}
