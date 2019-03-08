package com.moparisthebest.jdbc.codegen;

import com.moparisthebest.jdbc.util.SqlBuilder;
import com.moparisthebest.jdbc.Factory;

import java.sql.*;

import static com.moparisthebest.jdbc.util.ResultSetUtil.*;
import static com.moparisthebest.jdbc.TryClose.tryClose;

public class QmDaoUnNestBean implements QmDao {

	private final Connection conn;
	private final boolean closeConn;


	private QmDaoUnNestBean(final Connection conn, final boolean closeConn) {
		this.conn = conn;
		this.closeConn = closeConn;
		if (this.conn == null)
			throw new NullPointerException("Connection needs to be non-null for JdbcMapper...");
	}

	public QmDaoUnNestBean(Connection conn) {
		this(conn, false);
	}

	public Connection getConnection() {
		return this.conn;
	}

	@Override
	public com.moparisthebest.jdbc.dto.FieldPerson getFieldRegularPerson(final long personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.prepareStatement(0, "SELECT person_no, first_name, last_name, birth_date FROM person WHERE person_no = ?");
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
		}
	}

	@Override
	public com.moparisthebest.jdbc.dto.BuilderPerson getBuilderPerson(final long personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.prepareStatement(1, "SELECT person_no, first_name, last_name, birth_date FROM person WHERE person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final com.moparisthebest.jdbc.dto.BuilderPerson ret = new com.moparisthebest.jdbc.dto.BuilderPerson();
ret.setPersonNo(rs.getLong(1));
ret.setFirstName(rs.getString(2));
ret.setLastName(rs.getString(3));
ret.setBirthDate(com.moparisthebest.jdbc.util.ResultSetUtil.getUtilDate(rs, 4));
				return ret;
			} else {
				return null;
			}
		} finally {
			tryClose(rs);
		}
	}

	@Override
	public com.moparisthebest.jdbc.dto.FieldBoss getFieldRegularAndUnderscore(final long personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.prepareStatement(2, "SELECT p.person_no, p.first_name AS firstName, p.last_name, p.birth_date, b.department, p.first_name FROM person p JOIN boss b ON p.person_no = b.person_no WHERE p.person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final com.moparisthebest.jdbc.dto.FieldBoss ret = new com.moparisthebest.jdbc.dto.FieldBoss(
rs.getLong(1),
com.moparisthebest.jdbc.util.ResultSetUtil.getUtilDate(rs, 4),
rs.getString(6),
rs.getString(3),
rs.getString(5),
rs.getString(6));
				return ret;
			} else {
				return null;
			}
		} finally {
			tryClose(rs);
		}
	}

	@Override
	public com.moparisthebest.jdbc.dto.FieldBoss getFieldRegularAndUnderscoreReverse(final long personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.prepareStatement(3, "SELECT p.person_no, p.first_name, p.last_name, p.birth_date, b.department, p.first_name AS firstName FROM person p JOIN boss b ON p.person_no = b.person_no WHERE p.person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final com.moparisthebest.jdbc.dto.FieldBoss ret = new com.moparisthebest.jdbc.dto.FieldBoss(
rs.getLong(1),
com.moparisthebest.jdbc.util.ResultSetUtil.getUtilDate(rs, 4),
rs.getString(6),
rs.getString(3),
rs.getString(5),
rs.getString(2));
				return ret;
			} else {
				return null;
			}
		} finally {
			tryClose(rs);
		}
	}

	@Override
	public com.moparisthebest.jdbc.dto.FieldBoss getFieldRegularAndUnderscoreNoConstructor(final long personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.prepareStatement(4, "SELECT p.person_no, p.first_name AS firstName, p.last_name, p.birth_date, b.department, p.first_name, p.first_name AS dummy FROM person p JOIN boss b ON p.person_no = b.person_no WHERE p.person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final com.moparisthebest.jdbc.dto.FieldBoss ret = new com.moparisthebest.jdbc.dto.FieldBoss();
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[0], ret, rs.getLong(1));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[1], ret, rs.getString(2));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[2], ret, rs.getString(3));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[3], ret, com.moparisthebest.jdbc.util.ResultSetUtil.getUtilDate(rs, 4));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[4], ret, rs.getString(5));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[5], ret, rs.getString(6));
ret.setDummy(rs.getString(7));
				return ret;
			} else {
				return null;
			}
		} finally {
			tryClose(rs);
		}
	}

	@Override
	public com.moparisthebest.jdbc.dto.FieldBoss getFieldRegularAndUnderscoreReverseNoConstructor(final long personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.prepareStatement(5, "SELECT p.person_no, p.first_name, p.last_name, p.birth_date, b.department, p.first_name AS firstName, p.first_name AS dummy FROM person p JOIN boss b ON p.person_no = b.person_no WHERE p.person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final com.moparisthebest.jdbc.dto.FieldBoss ret = new com.moparisthebest.jdbc.dto.FieldBoss();
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[0], ret, rs.getLong(1));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[5], ret, rs.getString(2));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[2], ret, rs.getString(3));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[3], ret, com.moparisthebest.jdbc.util.ResultSetUtil.getUtilDate(rs, 4));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[4], ret, rs.getString(5));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[1], ret, rs.getString(6));
ret.setDummy(rs.getString(7));
				return ret;
			} else {
				return null;
			}
		} finally {
			tryClose(rs);
		}
	}

	@Override
	public com.moparisthebest.jdbc.dto.FieldBoss getFieldRegular(final long personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.prepareStatement(6, "SELECT p.person_no, p.first_name AS firstName, p.last_name, p.birth_date, b.department FROM person p JOIN boss b ON p.person_no = b.person_no WHERE p.person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final com.moparisthebest.jdbc.dto.FieldBoss ret = new com.moparisthebest.jdbc.dto.FieldBoss();
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[0], ret, rs.getLong(1));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[1], ret, rs.getString(2));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[2], ret, rs.getString(3));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[3], ret, com.moparisthebest.jdbc.util.ResultSetUtil.getUtilDate(rs, 4));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[4], ret, rs.getString(5));
				return ret;
			} else {
				return null;
			}
		} finally {
			tryClose(rs);
		}
	}

	@Override
	public com.moparisthebest.jdbc.dto.FieldBoss getFieldUnderscore(final long personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.prepareStatement(7, "SELECT p.person_no, p.first_name, p.last_name, p.birth_date, b.department FROM person p JOIN boss b ON p.person_no = b.person_no WHERE p.person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final com.moparisthebest.jdbc.dto.FieldBoss ret = new com.moparisthebest.jdbc.dto.FieldBoss();
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[0], ret, rs.getLong(1));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[5], ret, rs.getString(2));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[2], ret, rs.getString(3));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[3], ret, com.moparisthebest.jdbc.util.ResultSetUtil.getUtilDate(rs, 4));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[4], ret, rs.getString(5));
				return ret;
			} else {
				return null;
			}
		} finally {
			tryClose(rs);
		}
	}

	@Override
	public com.moparisthebest.jdbc.dto.SetPerson getSetRegularPerson(final long personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.prepareStatement(8, "SELECT person_no, first_name, last_name, birth_date FROM person WHERE person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final com.moparisthebest.jdbc.dto.SetPerson ret = new com.moparisthebest.jdbc.dto.SetPerson(
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
		}
	}

	@Override
	public com.moparisthebest.jdbc.dto.SetPerson getSetRegularPersonNoConstructor(final long personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.prepareStatement(9, "SELECT person_no, first_name, last_name, birth_date, first_name AS dummy FROM person WHERE person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final com.moparisthebest.jdbc.dto.SetPerson ret = new com.moparisthebest.jdbc.dto.SetPerson();
ret.setPersonNo(rs.getLong(1));
ret.setFirstName(rs.getString(2));
ret.setLastName(rs.getString(3));
ret.setBirthDate(com.moparisthebest.jdbc.util.ResultSetUtil.getUtilDate(rs, 4));
ret.setDummy(rs.getString(5));
				return ret;
			} else {
				return null;
			}
		} finally {
			tryClose(rs);
		}
	}

	@Override
	public com.moparisthebest.jdbc.dto.SetBoss getSetRegularAndUnderscore(final long personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.prepareStatement(10, "SELECT p.person_no, p.first_name AS firstName, p.last_name, p.birth_date, b.department, p.first_name FROM person p JOIN boss b ON p.person_no = b.person_no WHERE p.person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final com.moparisthebest.jdbc.dto.SetBoss ret = new com.moparisthebest.jdbc.dto.SetBoss(
rs.getLong(1),
com.moparisthebest.jdbc.util.ResultSetUtil.getUtilDate(rs, 4),
rs.getString(6),
rs.getString(3),
rs.getString(5),
rs.getString(6));
				return ret;
			} else {
				return null;
			}
		} finally {
			tryClose(rs);
		}
	}

	@Override
	public com.moparisthebest.jdbc.dto.SetBoss getSetRegularAndUnderscoreReverse(final long personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.prepareStatement(11, "SELECT p.person_no, p.first_name, p.last_name, p.birth_date, b.department, p.first_name AS firstName FROM person p JOIN boss b ON p.person_no = b.person_no WHERE p.person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final com.moparisthebest.jdbc.dto.SetBoss ret = new com.moparisthebest.jdbc.dto.SetBoss(
rs.getLong(1),
com.moparisthebest.jdbc.util.ResultSetUtil.getUtilDate(rs, 4),
rs.getString(6),
rs.getString(3),
rs.getString(5),
rs.getString(2));
				return ret;
			} else {
				return null;
			}
		} finally {
			tryClose(rs);
		}
	}

	@Override
	public com.moparisthebest.jdbc.dto.SetBoss getSetRegular(final long personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.prepareStatement(12, "SELECT p.person_no, p.first_name AS firstName, p.last_name, p.birth_date, b.department FROM person p JOIN boss b ON p.person_no = b.person_no WHERE p.person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final com.moparisthebest.jdbc.dto.SetBoss ret = new com.moparisthebest.jdbc.dto.SetBoss();
ret.setPersonNo(rs.getLong(1));
ret.setFirstName(rs.getString(2));
ret.setLastName(rs.getString(3));
ret.setBirthDate(com.moparisthebest.jdbc.util.ResultSetUtil.getUtilDate(rs, 4));
ret.setDepartment(rs.getString(5));
				return ret;
			} else {
				return null;
			}
		} finally {
			tryClose(rs);
		}
	}

	@Override
	public com.moparisthebest.jdbc.dto.SetBoss getSetUnderscore(final long personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.prepareStatement(13, "SELECT p.person_no, p.first_name, p.last_name, p.birth_date, b.department FROM person p JOIN boss b ON p.person_no = b.person_no WHERE p.person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final com.moparisthebest.jdbc.dto.SetBoss ret = new com.moparisthebest.jdbc.dto.SetBoss();
ret.setPersonNo(rs.getLong(1));
ret.setFirst_name(rs.getString(2));
ret.setLastName(rs.getString(3));
ret.setBirthDate(com.moparisthebest.jdbc.util.ResultSetUtil.getUtilDate(rs, 4));
ret.setDepartment(rs.getString(5));
				return ret;
			} else {
				return null;
			}
		} finally {
			tryClose(rs);
		}
	}

	@Override
	public com.moparisthebest.jdbc.dto.ReverseFieldPerson getReverseFieldRegularPerson(final long personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.prepareStatement(14, "SELECT person_no, first_name, last_name, birth_date FROM person WHERE person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final com.moparisthebest.jdbc.dto.ReverseFieldPerson ret = new com.moparisthebest.jdbc.dto.ReverseFieldPerson(
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
		}
	}

	@Override
	public com.moparisthebest.jdbc.dto.ReverseFieldBoss getReverseFieldRegularAndUnderscore(final long personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.prepareStatement(15, "SELECT p.person_no, p.first_name AS firstName, p.last_name, p.birth_date, b.department, p.first_name FROM person p JOIN boss b ON p.person_no = b.person_no WHERE p.person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final com.moparisthebest.jdbc.dto.ReverseFieldBoss ret = new com.moparisthebest.jdbc.dto.ReverseFieldBoss(
rs.getLong(1),
com.moparisthebest.jdbc.util.ResultSetUtil.getUtilDate(rs, 4),
rs.getString(6),
rs.getString(3),
rs.getString(5),
rs.getString(6));
				return ret;
			} else {
				return null;
			}
		} finally {
			tryClose(rs);
		}
	}

	@Override
	public com.moparisthebest.jdbc.dto.ReverseFieldBoss getReverseFieldRegularAndUnderscoreReverse(final long personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.prepareStatement(16, "SELECT p.person_no, p.first_name, p.last_name, p.birth_date, b.department, p.first_name AS firstName FROM person p JOIN boss b ON p.person_no = b.person_no WHERE p.person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final com.moparisthebest.jdbc.dto.ReverseFieldBoss ret = new com.moparisthebest.jdbc.dto.ReverseFieldBoss(
rs.getLong(1),
com.moparisthebest.jdbc.util.ResultSetUtil.getUtilDate(rs, 4),
rs.getString(6),
rs.getString(3),
rs.getString(5),
rs.getString(2));
				return ret;
			} else {
				return null;
			}
		} finally {
			tryClose(rs);
		}
	}

	@Override
	public com.moparisthebest.jdbc.dto.ReverseFieldBoss getReverseFieldRegular(final long personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.prepareStatement(17, "SELECT p.person_no, p.first_name AS firstName, p.last_name, p.birth_date, b.department FROM person p JOIN boss b ON p.person_no = b.person_no WHERE p.person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final com.moparisthebest.jdbc.dto.ReverseFieldBoss ret = new com.moparisthebest.jdbc.dto.ReverseFieldBoss();
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[6], ret, rs.getLong(1));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[7], ret, rs.getString(2));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[8], ret, rs.getString(3));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[9], ret, com.moparisthebest.jdbc.util.ResultSetUtil.getUtilDate(rs, 4));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[10], ret, rs.getString(5));
				return ret;
			} else {
				return null;
			}
		} finally {
			tryClose(rs);
		}
	}

	@Override
	public com.moparisthebest.jdbc.dto.ReverseFieldBoss getReverseFieldUnderscore(final long personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.prepareStatement(18, "SELECT p.person_no, p.first_name, p.last_name, p.birth_date, b.department FROM person p JOIN boss b ON p.person_no = b.person_no WHERE p.person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final com.moparisthebest.jdbc.dto.ReverseFieldBoss ret = new com.moparisthebest.jdbc.dto.ReverseFieldBoss();
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[6], ret, rs.getLong(1));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[11], ret, rs.getString(2));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[8], ret, rs.getString(3));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[9], ret, com.moparisthebest.jdbc.util.ResultSetUtil.getUtilDate(rs, 4));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[10], ret, rs.getString(5));
				return ret;
			} else {
				return null;
			}
		} finally {
			tryClose(rs);
		}
	}

	@Override
	public com.moparisthebest.jdbc.dto.ReverseSetPerson getReverseSetRegularPerson(final long personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.prepareStatement(19, "SELECT person_no, first_name, last_name, birth_date FROM person WHERE person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final com.moparisthebest.jdbc.dto.ReverseSetPerson ret = new com.moparisthebest.jdbc.dto.ReverseSetPerson(
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
		}
	}

	@Override
	public com.moparisthebest.jdbc.dto.ReverseSetBoss getReverseSetRegularAndUnderscore(final long personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.prepareStatement(20, "SELECT p.person_no, p.first_name AS firstName, p.last_name, p.birth_date, b.department, p.first_name FROM person p JOIN boss b ON p.person_no = b.person_no WHERE p.person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final com.moparisthebest.jdbc.dto.ReverseSetBoss ret = new com.moparisthebest.jdbc.dto.ReverseSetBoss(
rs.getLong(1),
com.moparisthebest.jdbc.util.ResultSetUtil.getUtilDate(rs, 4),
rs.getString(6),
rs.getString(3),
rs.getString(5),
rs.getString(6));
				return ret;
			} else {
				return null;
			}
		} finally {
			tryClose(rs);
		}
	}

	@Override
	public com.moparisthebest.jdbc.dto.ReverseSetBoss getReverseSetRegularAndUnderscoreReverse(final long personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.prepareStatement(21, "SELECT p.person_no, p.first_name, p.last_name, p.birth_date, b.department, p.first_name AS firstName FROM person p JOIN boss b ON p.person_no = b.person_no WHERE p.person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final com.moparisthebest.jdbc.dto.ReverseSetBoss ret = new com.moparisthebest.jdbc.dto.ReverseSetBoss(
rs.getLong(1),
com.moparisthebest.jdbc.util.ResultSetUtil.getUtilDate(rs, 4),
rs.getString(6),
rs.getString(3),
rs.getString(5),
rs.getString(2));
				return ret;
			} else {
				return null;
			}
		} finally {
			tryClose(rs);
		}
	}

	@Override
	public com.moparisthebest.jdbc.dto.ReverseSetBoss getReverseSetRegular(final long personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.prepareStatement(22, "SELECT p.person_no, p.first_name AS firstName, p.last_name, p.birth_date, b.department FROM person p JOIN boss b ON p.person_no = b.person_no WHERE p.person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final com.moparisthebest.jdbc.dto.ReverseSetBoss ret = new com.moparisthebest.jdbc.dto.ReverseSetBoss();
ret.setPersonNo(rs.getLong(1));
ret.setFirstName(rs.getString(2));
ret.setLastName(rs.getString(3));
ret.setBirthDate(com.moparisthebest.jdbc.util.ResultSetUtil.getUtilDate(rs, 4));
ret.setDepartment(rs.getString(5));
				return ret;
			} else {
				return null;
			}
		} finally {
			tryClose(rs);
		}
	}

	@Override
	public com.moparisthebest.jdbc.dto.ReverseSetBoss getReverseSetUnderscore(final long personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.prepareStatement(23, "SELECT p.person_no, p.first_name, p.last_name, p.birth_date, b.department FROM person p JOIN boss b ON p.person_no = b.person_no WHERE p.person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final com.moparisthebest.jdbc.dto.ReverseSetBoss ret = new com.moparisthebest.jdbc.dto.ReverseSetBoss();
ret.setPersonNo(rs.getLong(1));
ret.setFirst_name(rs.getString(2));
ret.setLastName(rs.getString(3));
ret.setBirthDate(com.moparisthebest.jdbc.util.ResultSetUtil.getUtilDate(rs, 4));
ret.setDepartment(rs.getString(5));
				return ret;
			} else {
				return null;
			}
		} finally {
			tryClose(rs);
		}
	}

	@Override
	public java.lang.Long getPersonNo(final long personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.prepareStatement(24, "SELECT person_no FROM person WHERE person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final java.lang.Long ret = com.moparisthebest.jdbc.util.ResultSetUtil.getObjectLong(rs, 1);
				return ret;
			} else {
				return null;
			}
		} finally {
			tryClose(rs);
		}
	}

	@Override
	public long getPersonNoPrimitive(final long personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.prepareStatement(25, "SELECT person_no FROM person WHERE person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final long ret = rs.getLong(1);
				return ret;
			} else {
				return 0;
			}
		} finally {
			tryClose(rs);
		}
	}

	@Override
	public int getPersonNoPrimitiveInt(final int personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.prepareStatement(26, "SELECT person_no FROM person WHERE person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final int ret = rs.getInt(1);
				return ret;
			} else {
				return 0;
			}
		} finally {
			tryClose(rs);
		}
	}

	@Override
	public java.lang.Long[] getPersonNoObjectArray(final java.lang.Long personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.prepareStatement(27, "SELECT person_no FROM person WHERE person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			final java.util.List<java.lang.Long> _colret = new java.util.ArrayList<java.lang.Long>();
			while(rs.next()) {
final java.lang.Long ret = com.moparisthebest.jdbc.util.ResultSetUtil.getObjectLong(rs, 1);
				_colret.add(ret);
			}
			return _colret.toArray(new java.lang.Long[_colret.size()]);
		} finally {
			tryClose(rs);
		}
	}

	@Override
	public java.util.List<java.util.Map<java.lang.String,java.lang.String>> getAllNames() throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.prepareStatement(28, "SELECT first_name, last_name FROM person WHERE person_no < 4");
			rs = ps.executeQuery();
			final java.util.List<java.util.Map<java.lang.String,java.lang.String>> _colret = new java.util.ArrayList<java.util.Map<java.lang.String,java.lang.String>>();
			while(rs.next()) {
final java.util.Map<java.lang.String,java.lang.String> ret = new java.util.HashMap<java.lang.String,java.lang.String>();
ret.put("first_name", rs.getString(1));
ret.put("last_name", rs.getString(2));
				_colret.add(ret);
			}
			return _colret;
		} finally {
			tryClose(rs);
		}
	}

	@Override
	public java.util.Map<java.lang.String,java.lang.String>[] getAllNamesArray() throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.prepareStatement(29, "SELECT first_name, last_name FROM person WHERE person_no < 4");
			rs = ps.executeQuery();
			final java.util.List<java.util.Map<java.lang.String,java.lang.String>> _colret = new java.util.ArrayList<java.util.Map<java.lang.String,java.lang.String>>();
			while(rs.next()) {
final java.util.Map<java.lang.String,java.lang.String> ret = new java.util.HashMap<java.lang.String,java.lang.String>();
ret.put("first_name", rs.getString(1));
ret.put("last_name", rs.getString(2));
				_colret.add(ret);
			}
			@SuppressWarnings("unchecked")
			final java.util.Map<java.lang.String,java.lang.String>[] _warnret = _colret.toArray(new java.util.Map[_colret.size()]);
			return _warnret;
		} finally {
			tryClose(rs);
		}
	}

	@Override
	public java.util.Map<java.lang.String,java.lang.String> getAllNameMap() throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.prepareStatement(30, "SELECT first_name, last_name FROM person WHERE person_no < 4");
			rs = ps.executeQuery();
			final java.util.Map<java.lang.String,java.lang.String> _colret = new java.util.HashMap<java.lang.String,java.lang.String>();
			while(rs.next()) {
final java.lang.String ret = rs.getString(2);
				_colret.put(rs.getString(1), ret);
			}
			return _colret;
		} finally {
			tryClose(rs);
		}
	}

	@Override
	public java.util.Map<java.lang.Long,com.moparisthebest.jdbc.dto.FieldBoss> getMapLongPerson() throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.prepareStatement(31, "SELECT p.person_no, p.first_name AS firstName, p.last_name, p.birth_date, b.department FROM person p JOIN boss b ON p.person_no = b.person_no WHERE p.person_no in (2,3,4)");
			rs = ps.executeQuery();
			final java.util.Map<java.lang.Long,com.moparisthebest.jdbc.dto.FieldBoss> _colret = new java.util.HashMap<java.lang.Long,com.moparisthebest.jdbc.dto.FieldBoss>();
			while(rs.next()) {
final com.moparisthebest.jdbc.dto.FieldBoss ret = new com.moparisthebest.jdbc.dto.FieldBoss();
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[0], ret, rs.getLong(1));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[1], ret, rs.getString(2));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[2], ret, rs.getString(3));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[3], ret, com.moparisthebest.jdbc.util.ResultSetUtil.getUtilDate(rs, 4));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[4], ret, rs.getString(5));
				_colret.put(com.moparisthebest.jdbc.util.ResultSetUtil.getObjectLong(rs, 1), ret);
			}
			return _colret;
		} finally {
			tryClose(rs);
		}
	}

	@Override
	public java.util.Map<java.lang.Long,java.lang.Long> getMapLongLong() throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.prepareStatement(32, "SELECT person_no AS first_no, person_no AS last_no FROM person WHERE person_no < 4");
			rs = ps.executeQuery();
			final java.util.Map<java.lang.Long,java.lang.Long> _colret = new java.util.HashMap<java.lang.Long,java.lang.Long>();
			while(rs.next()) {
final java.lang.Long ret = com.moparisthebest.jdbc.util.ResultSetUtil.getObjectLong(rs, 2);
				_colret.put(com.moparisthebest.jdbc.util.ResultSetUtil.getObjectLong(rs, 1), ret);
			}
			return _colret;
		} finally {
			tryClose(rs);
		}
	}

	@Override
	public java.lang.Long[] getLongObjectArray() throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.prepareStatement(33, "SELECT 1, 2, 3 FROM person WHERE person_no = 1");
			rs = ps.executeQuery();
			if(rs.next()) {
final java.lang.Long[] ret = new java.lang.Long[3];
ret[0] = com.moparisthebest.jdbc.util.ResultSetUtil.getObjectLong(rs, 1);
ret[1] = com.moparisthebest.jdbc.util.ResultSetUtil.getObjectLong(rs, 2);
ret[2] = com.moparisthebest.jdbc.util.ResultSetUtil.getObjectLong(rs, 3);
				return ret;
			} else {
				return null;
			}
		} finally {
			tryClose(rs);
		}
	}

	@Override
	public long[] getLongPrimitiveArray() throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.prepareStatement(34, "SELECT 1, 2, 3 FROM person WHERE person_no = 1");
			rs = ps.executeQuery();
			if(rs.next()) {
final long[] ret = new long[3];
ret[0] = rs.getLong(1);
ret[1] = rs.getLong(2);
ret[2] = rs.getLong(3);
				return ret;
			} else {
				return null;
			}
		} finally {
			tryClose(rs);
		}
	}

	@Override
	public java.util.List<java.util.Map<java.lang.String,java.lang.String>> getBobTomMap() throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.prepareStatement(35, "SELECT 'bob' as bob, 'tom' as tom FROM person WHERE person_no = 1");
			rs = ps.executeQuery();
			final java.util.List<java.util.Map<java.lang.String,java.lang.String>> _colret = new java.util.ArrayList<java.util.Map<java.lang.String,java.lang.String>>();
			while(rs.next()) {
final java.util.Map<java.lang.String,java.lang.String> ret = new java.util.HashMap<java.lang.String,java.lang.String>();
ret.put("bob", rs.getString(1));
ret.put("tom", rs.getString(2));
				_colret.add(ret);
			}
			return _colret;
		} finally {
			tryClose(rs);
		}
	}

	@Override
	public java.util.List<com.moparisthebest.jdbc.util.CaseInsensitiveHashMap<java.lang.String,java.lang.String>> getBobTomMapCaseInsensitive() throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.prepareStatement(36, "SELECT 'bob' as bob, 'tom' as tom FROM person WHERE person_no = 1");
			rs = ps.executeQuery();
			final java.util.List<com.moparisthebest.jdbc.util.CaseInsensitiveHashMap<java.lang.String,java.lang.String>> _colret = new java.util.ArrayList<com.moparisthebest.jdbc.util.CaseInsensitiveHashMap<java.lang.String,java.lang.String>>();
			while(rs.next()) {
final com.moparisthebest.jdbc.util.CaseInsensitiveHashMap<java.lang.String,java.lang.String> ret = new com.moparisthebest.jdbc.util.CaseInsensitiveHashMap<java.lang.String,java.lang.String>();
ret.put("bob", rs.getString(1));
ret.put("tom", rs.getString(2));
				_colret.add(ret);
			}
			return _colret;
		} finally {
			tryClose(rs);
		}
	}

	@Override
	public java.util.List<com.moparisthebest.jdbc.dto.FieldPerson> getThreePeople(final long personNo1, final long personNo2, final long personNo3) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.prepareStatement(37, "SELECT person_no, first_name, last_name, birth_date from person WHERE person_no IN (?, ?, ?) ORDER BY person_no");
			ps.setObject(1, personNo1);
			ps.setObject(2, personNo2);
			ps.setObject(3, personNo3);
			rs = ps.executeQuery();
			final java.util.List<com.moparisthebest.jdbc.dto.FieldPerson> _colret = new java.util.ArrayList<com.moparisthebest.jdbc.dto.FieldPerson>();
			while(rs.next()) {
final com.moparisthebest.jdbc.dto.FieldPerson ret = new com.moparisthebest.jdbc.dto.FieldPerson(
rs.getLong(1),
com.moparisthebest.jdbc.util.ResultSetUtil.getUtilDate(rs, 4),
rs.getString(2),
rs.getString(3));
				_colret.add(ret);
			}
			return _colret;
		} finally {
			tryClose(rs);
		}
	}

	@Override
	public com.moparisthebest.jdbc.util.ResultSetIterable<com.moparisthebest.jdbc.dto.FieldPerson> getThreePeopleResultSetIterable(final long personNo1, final long personNo2, final long personNo3) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.prepareStatement(38, "SELECT person_no, first_name, last_name, birth_date from person WHERE person_no IN (?, ?, ?) ORDER BY person_no");
			ps.setObject(1, personNo1);
			ps.setObject(2, personNo2);
			ps.setObject(3, personNo3);
			rs = ps.executeQuery();
			return com.moparisthebest.jdbc.util.ResultSetIterable.getResultSetIterable(rs,
					rs.next() ? (_rs, _cal) -> {
final com.moparisthebest.jdbc.dto.FieldPerson ret = new com.moparisthebest.jdbc.dto.FieldPerson(
_rs.getLong(1),
com.moparisthebest.jdbc.util.ResultSetUtil.getUtilDate(_rs, 4),
_rs.getString(2),
_rs.getString(3));
						return ret;
					}
				: null, null);
		} catch(Throwable e) {
			tryClose(rs);
			if(e instanceof SQLException)
				throw (SQLException)e;
			if(e instanceof RuntimeException)
				throw (RuntimeException)e;
			throw new RuntimeException(e);
		}
	}

	@Override
	public java.util.stream.Stream<com.moparisthebest.jdbc.dto.FieldPerson> getThreePeopleStream(final long personNo1, final long personNo2, final long personNo3) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.prepareStatement(39, "SELECT person_no, first_name, last_name, birth_date from person WHERE person_no IN (?, ?, ?) ORDER BY person_no");
			ps.setObject(1, personNo1);
			ps.setObject(2, personNo2);
			ps.setObject(3, personNo3);
			rs = ps.executeQuery();
			return com.moparisthebest.jdbc.util.ResultSetIterable.getStream(rs,
					rs.next() ? (_rs, _cal) -> {
final com.moparisthebest.jdbc.dto.FieldPerson ret = new com.moparisthebest.jdbc.dto.FieldPerson(
_rs.getLong(1),
com.moparisthebest.jdbc.util.ResultSetUtil.getUtilDate(_rs, 4),
_rs.getString(2),
_rs.getString(3));
						return ret;
					}
				: null, null);
		} catch(Throwable e) {
			tryClose(rs);
			if(e instanceof SQLException)
				throw (SQLException)e;
			if(e instanceof RuntimeException)
				throw (RuntimeException)e;
			throw new RuntimeException(e);
		}
	}

	@Override
	public com.moparisthebest.jdbc.dto.EnumPerson getEnumPerson(final long personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.prepareStatement(40, "SELECT first_name, last_name FROM person WHERE person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final com.moparisthebest.jdbc.dto.EnumPerson ret = new com.moparisthebest.jdbc.dto.EnumPerson();
ret.setFirstName(com.moparisthebest.jdbc.util.ResultSetUtil.getEnum(rs, 1, com.moparisthebest.jdbc.dto.FirstName.class));
ret.setLastName(rs.getString(2));
				return ret;
			} else {
				return null;
			}
		} finally {
			tryClose(rs);
		}
	}

	@Override
	public com.moparisthebest.jdbc.dto.EnumPerson getEnumPersonConstructor(final long personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.prepareStatement(41, "SELECT first_name FROM person WHERE person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final com.moparisthebest.jdbc.dto.EnumPerson ret = new com.moparisthebest.jdbc.dto.EnumPerson(
com.moparisthebest.jdbc.util.ResultSetUtil.getEnum(rs, 1, com.moparisthebest.jdbc.dto.FirstName.class));
				return ret;
			} else {
				return null;
			}
		} finally {
			tryClose(rs);
		}
	}

	@Override
	public com.moparisthebest.jdbc.dto.FirstName getFirstName(final long personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.prepareStatement(42, "SELECT first_name FROM person WHERE person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final com.moparisthebest.jdbc.dto.FirstName ret = com.moparisthebest.jdbc.util.ResultSetUtil.getEnum(rs, 1, com.moparisthebest.jdbc.dto.FirstName.class);
				return ret;
			} else {
				return null;
			}
		} finally {
			tryClose(rs);
		}
	}

	@Override
	public com.moparisthebest.jdbc.dto.EnumPerson getEnumPersonNull() throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.prepareStatement(43, "SELECT str_val as first_name, str_val as last_name FROM val WHERE val_no = 4");
			rs = ps.executeQuery();
			if(rs.next()) {
final com.moparisthebest.jdbc.dto.EnumPerson ret = new com.moparisthebest.jdbc.dto.EnumPerson();
ret.setFirstName(com.moparisthebest.jdbc.util.ResultSetUtil.getEnum(rs, 1, com.moparisthebest.jdbc.dto.FirstName.class));
ret.setLastName(rs.getString(2));
				return ret;
			} else {
				return null;
			}
		} finally {
			tryClose(rs);
		}
	}

	@Override
	public com.moparisthebest.jdbc.dto.FirstName getFirstNameNull() throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.prepareStatement(44, "SELECT str_val FROM val WHERE val_no = 4");
			rs = ps.executeQuery();
			if(rs.next()) {
final com.moparisthebest.jdbc.dto.FirstName ret = com.moparisthebest.jdbc.util.ResultSetUtil.getEnum(rs, 1, com.moparisthebest.jdbc.dto.FirstName.class);
				return ret;
			} else {
				return null;
			}
		} finally {
			tryClose(rs);
		}
	}

	@Override
	public com.moparisthebest.jdbc.dto.CaseSensitivePerson getCaseSensitivePerson(final long personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.prepareStatement(45, "SELECT first_name AS M_PERSON_FIRST_NAME FROM person WHERE person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final com.moparisthebest.jdbc.dto.CaseSensitivePerson ret = new com.moparisthebest.jdbc.dto.CaseSensitivePerson();
ret.setmPersonFirstName(rs.getString(1));
				return ret;
			} else {
				return null;
			}
		} finally {
			tryClose(rs);
		}
	}

	@Override
	public java.time.Instant getBirthdateInstant(final long personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.prepareStatement(46, "SELECT birth_date FROM person WHERE person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final java.time.Instant ret = com.moparisthebest.jdbc.util.ResultSetUtil.getInstant(rs, 1);
				return ret;
			} else {
				return null;
			}
		} finally {
			tryClose(rs);
		}
	}

	@Override
	public java.time.LocalDateTime getBirthdateLocalDateTime(final long personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.prepareStatement(47, "SELECT birth_date FROM person WHERE person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final java.time.LocalDateTime ret = com.moparisthebest.jdbc.util.ResultSetUtil.getLocalDateTime(rs, 1);
				return ret;
			} else {
				return null;
			}
		} finally {
			tryClose(rs);
		}
	}

	@Override
	public java.time.LocalDate getBirthdateLocalDate(final long personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.prepareStatement(48, "SELECT birth_date FROM person WHERE person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final java.time.LocalDate ret = com.moparisthebest.jdbc.util.ResultSetUtil.getLocalDate(rs, 1);
				return ret;
			} else {
				return null;
			}
		} finally {
			tryClose(rs);
		}
	}

	@Override
	public java.time.LocalTime getBirthdateLocalTime(final long personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.prepareStatement(49, "SELECT birth_date FROM person WHERE person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final java.time.LocalTime ret = com.moparisthebest.jdbc.util.ResultSetUtil.getLocalTime(rs, 1);
				return ret;
			} else {
				return null;
			}
		} finally {
			tryClose(rs);
		}
	}

	@Override
	public java.time.ZonedDateTime getBirthdateZonedDateTime(final long personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.prepareStatement(50, "SELECT birth_date FROM person WHERE person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final java.time.ZonedDateTime ret = com.moparisthebest.jdbc.util.ResultSetUtil.getZonedDateTime(rs, 1);
				return ret;
			} else {
				return null;
			}
		} finally {
			tryClose(rs);
		}
	}

	@Override
	public java.time.OffsetDateTime getBirthdateOffsetDateTime(final long personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.prepareStatement(51, "SELECT birth_date FROM person WHERE person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final java.time.OffsetDateTime ret = com.moparisthebest.jdbc.util.ResultSetUtil.getOffsetDateTime(rs, 1);
				return ret;
			} else {
				return null;
			}
		} finally {
			tryClose(rs);
		}
	}

	@Override
	public java.time.OffsetTime getBirthdateOffsetTime(final long personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.prepareStatement(52, "SELECT birth_date FROM person WHERE person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final java.time.OffsetTime ret = com.moparisthebest.jdbc.util.ResultSetUtil.getOffsetTime(rs, 1);
				return ret;
			} else {
				return null;
			}
		} finally {
			tryClose(rs);
		}
	}

	@Override
	public java.time.Year getYearInt(final long valNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.prepareStatement(53, "SELECT num_val FROM val WHERE val_no = ?");
			ps.setObject(1, valNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final java.time.Year ret = com.moparisthebest.jdbc.util.ResultSetUtil.getYear(rs, 1);
				return ret;
			} else {
				return null;
			}
		} finally {
			tryClose(rs);
		}
	}

	@Override
	public java.time.Year getYearString(final long valNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.prepareStatement(54, "SELECT str_val FROM val WHERE val_no = ?");
			ps.setObject(1, valNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final java.time.Year ret = com.moparisthebest.jdbc.util.ResultSetUtil.getYear(rs, 1);
				return ret;
			} else {
				return null;
			}
		} finally {
			tryClose(rs);
		}
	}

	@Override
	public java.time.ZoneId getZoneId(final long valNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.prepareStatement(55, "SELECT str_val FROM val WHERE val_no = ?");
			ps.setObject(1, valNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final java.time.ZoneId ret = com.moparisthebest.jdbc.util.ResultSetUtil.getZoneId(rs, 1);
				return ret;
			} else {
				return null;
			}
		} finally {
			tryClose(rs);
		}
	}

	@Override
	public java.time.ZoneOffset getZoneOffsetInt(final long valNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.prepareStatement(56, "SELECT str_val FROM val WHERE val_no = ?");
			ps.setObject(1, valNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final java.time.ZoneOffset ret = com.moparisthebest.jdbc.util.ResultSetUtil.getZoneOffset(rs, 1);
				return ret;
			} else {
				return null;
			}
		} finally {
			tryClose(rs);
		}
	}

	@Override
	public java.time.ZoneOffset getZoneOffsetString(final long valNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.prepareStatement(57, "SELECT str_val FROM val WHERE val_no = ?");
			ps.setObject(1, valNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final java.time.ZoneOffset ret = com.moparisthebest.jdbc.util.ResultSetUtil.getZoneOffset(rs, 1);
				return ret;
			} else {
				return null;
			}
		} finally {
			tryClose(rs);
		}
	}

	@Override
	public java.util.List<com.moparisthebest.jdbc.dto.FieldPerson> getFieldPeopleStream(final java.util.stream.Stream<java.lang.Long> personNos) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		final Array[] _bindArrays = new Array[1];
		try {
			_bindArrays[0] = conn.createArrayOf("NUMERIC", personNos.toArray(java.lang.Long[]::new));
			ps = this.prepareStatement(58, "SELECT person_no, first_name, last_name, birth_date from person WHERE (person_no IN(UNNEST(?))) ORDER BY person_no");
			ps.setArray(1, _bindArrays[0]);
			rs = ps.executeQuery();
			final java.util.List<com.moparisthebest.jdbc.dto.FieldPerson> _colret = new java.util.ArrayList<com.moparisthebest.jdbc.dto.FieldPerson>();
			while(rs.next()) {
final com.moparisthebest.jdbc.dto.FieldPerson ret = new com.moparisthebest.jdbc.dto.FieldPerson(
rs.getLong(1),
com.moparisthebest.jdbc.util.ResultSetUtil.getUtilDate(rs, 4),
rs.getString(2),
rs.getString(3));
				_colret.add(ret);
			}
			return _colret;
		} finally {
			for(final Array _bindArray : _bindArrays)
				tryClose(_bindArray);
			tryClose(rs);
		}
	}

	@Override
	public java.util.List<com.moparisthebest.jdbc.dto.FieldPerson> getFieldPeople(final java.util.List<java.lang.Long> personNos) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		final Array[] _bindArrays = new Array[1];
		try {
			_bindArrays[0] = conn.createArrayOf("NUMERIC", personNos.toArray(new java.lang.Long[personNos.size()]));
			ps = this.prepareStatement(59, "SELECT person_no, first_name, last_name, birth_date from person WHERE (person_no IN(UNNEST(?))) ORDER BY person_no");
			ps.setArray(1, _bindArrays[0]);
			rs = ps.executeQuery();
			final java.util.List<com.moparisthebest.jdbc.dto.FieldPerson> _colret = new java.util.ArrayList<com.moparisthebest.jdbc.dto.FieldPerson>();
			while(rs.next()) {
final com.moparisthebest.jdbc.dto.FieldPerson ret = new com.moparisthebest.jdbc.dto.FieldPerson(
rs.getLong(1),
com.moparisthebest.jdbc.util.ResultSetUtil.getUtilDate(rs, 4),
rs.getString(2),
rs.getString(3));
				_colret.add(ret);
			}
			return _colret;
		} finally {
			for(final Array _bindArray : _bindArrays)
				tryClose(_bindArray);
			tryClose(rs);
		}
	}

	@Override
	public java.util.List<com.moparisthebest.jdbc.dto.FieldPerson> getFieldPeopleByName(final java.util.List<java.lang.Long> personNos, final java.util.List<java.lang.String> names) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		final Array[] _bindArrays = new Array[2];
		try {
			_bindArrays[0] = conn.createArrayOf("NUMERIC", personNos.toArray(new java.lang.Long[personNos.size()]));
			_bindArrays[1] = conn.createArrayOf("VARCHAR", names.toArray(new java.lang.String[names.size()]));
			ps = this.prepareStatement(60, "SELECT person_no, first_name, last_name, birth_date from person WHERE (person_no IN(UNNEST(?))) AND ((first_name IN(UNNEST(?))) OR (last_name IN(UNNEST(?)))) ORDER BY person_no");
			ps.setArray(1, _bindArrays[0]);
			ps.setArray(2, _bindArrays[1]);
			ps.setArray(3, _bindArrays[1]);
			rs = ps.executeQuery();
			final java.util.List<com.moparisthebest.jdbc.dto.FieldPerson> _colret = new java.util.ArrayList<com.moparisthebest.jdbc.dto.FieldPerson>();
			while(rs.next()) {
final com.moparisthebest.jdbc.dto.FieldPerson ret = new com.moparisthebest.jdbc.dto.FieldPerson(
rs.getLong(1),
com.moparisthebest.jdbc.util.ResultSetUtil.getUtilDate(rs, 4),
rs.getString(2),
rs.getString(3));
				_colret.add(ret);
			}
			return _colret;
		} finally {
			for(final Array _bindArray : _bindArrays)
				tryClose(_bindArray);
			tryClose(rs);
		}
	}

	@Override
	public java.util.List<com.moparisthebest.jdbc.dto.FieldPerson> getFieldPeopleNotIn(final java.util.List<java.lang.Long> personNos) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		final Array[] _bindArrays = new Array[1];
		try {
			_bindArrays[0] = conn.createArrayOf("NUMERIC", personNos.toArray(new java.lang.Long[personNos.size()]));
			ps = this.prepareStatement(61, "SELECT person_no, first_name, last_name, birth_date from person WHERE (person_no NOT IN(UNNEST(?))) ORDER BY person_no");
			ps.setArray(1, _bindArrays[0]);
			rs = ps.executeQuery();
			final java.util.List<com.moparisthebest.jdbc.dto.FieldPerson> _colret = new java.util.ArrayList<com.moparisthebest.jdbc.dto.FieldPerson>();
			while(rs.next()) {
final com.moparisthebest.jdbc.dto.FieldPerson ret = new com.moparisthebest.jdbc.dto.FieldPerson(
rs.getLong(1),
com.moparisthebest.jdbc.util.ResultSetUtil.getUtilDate(rs, 4),
rs.getString(2),
rs.getString(3));
				_colret.add(ret);
			}
			return _colret;
		} finally {
			for(final Array _bindArray : _bindArrays)
				tryClose(_bindArray);
			tryClose(rs);
		}
	}

	@Override
	public long insertGetGeneratedKeyOracle(final long value) throws java.sql.SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("INSERT INTO a_thaoeu_table (a_thaoeu_table_no, a_thaoeu_table_val) VALUES (a_thaoeu_table_seq.nextval, ?)", java.sql.Statement.RETURN_GENERATED_KEYS);
			ps.setObject(1, value);
			ps.executeUpdate();
			ResultSet rs = null;
			try {
				rs = ps.getGeneratedKeys();
				return rs.next() ? rs.getLong(1) : 0;
			} finally {
				tryClose(rs);
			}
		} finally {
			tryClose(ps);
		}
	}

	@Override
	public long insertGetGeneratedKey(final long value) throws java.sql.SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("INSERT INTO a_thaoeu_table (a_thaoeu_table_val) VALUES (?)", java.sql.Statement.RETURN_GENERATED_KEYS);
			ps.setObject(1, value);
			ps.executeUpdate();
			ResultSet rs = null;
			try {
				rs = ps.getGeneratedKeys();
				return rs.next() ? rs.getLong(1) : 0;
			} finally {
				tryClose(rs);
			}
		} finally {
			tryClose(ps);
		}
	}

	@Override
	public java.util.List<java.lang.Long> selectRandomSql(final java.lang.String sql) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT person_no FROM person WHERE " + sql + "");
			rs = ps.executeQuery();
			final java.util.List<java.lang.Long> _colret = new java.util.ArrayList<java.lang.Long>();
			while(rs.next()) {
final java.lang.Long ret = com.moparisthebest.jdbc.util.ResultSetUtil.getObjectLong(rs, 1);
				_colret.add(ret);
			}
			return _colret;
		} finally {
			tryClose(rs);
			tryClose(ps);
		}
	}

	@Override
	public java.util.List<java.lang.Long> selectRandomSqlBuilder(final com.moparisthebest.jdbc.util.SqlBuilder sql) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT person_no FROM person WHERE " + sql + "");
			int psParamCount = 0;
			psParamCount = com.moparisthebest.jdbc.util.PreparedStatementUtil.recursiveBindIndex(ps, psParamCount, sql);
			rs = ps.executeQuery();
			final java.util.List<java.lang.Long> _colret = new java.util.ArrayList<java.lang.Long>();
			while(rs.next()) {
final java.lang.Long ret = com.moparisthebest.jdbc.util.ResultSetUtil.getObjectLong(rs, 1);
				_colret.add(ret);
			}
			return _colret;
		} finally {
			tryClose(rs);
			tryClose(ps);
		}
	}

	@Override
	public java.util.List<java.lang.Long> selectRandomSql(final long personNo1, final java.lang.String sql, final java.lang.String firstName) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT person_no FROM person WHERE person_no = ? " + sql + " OR first_name = ?");
			ps.setObject(1, personNo1);
			ps.setObject(2, firstName);
			rs = ps.executeQuery();
			final java.util.List<java.lang.Long> _colret = new java.util.ArrayList<java.lang.Long>();
			while(rs.next()) {
final java.lang.Long ret = com.moparisthebest.jdbc.util.ResultSetUtil.getObjectLong(rs, 1);
				_colret.add(ret);
			}
			return _colret;
		} finally {
			tryClose(rs);
			tryClose(ps);
		}
	}

	@Override
	public java.util.List<java.lang.Long> selectRandomSqlBuilder(final long personNo1, final com.moparisthebest.jdbc.util.Bindable sql, final java.lang.String firstName) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT person_no FROM person WHERE person_no = ? " + sql + " OR first_name = ?");
			int psParamCount = 0;
			ps.setObject(++psParamCount, personNo1);
			psParamCount = com.moparisthebest.jdbc.util.PreparedStatementUtil.recursiveBindIndex(ps, psParamCount, sql.getBindObject());
			ps.setObject(++psParamCount, firstName);
			rs = ps.executeQuery();
			final java.util.List<java.lang.Long> _colret = new java.util.ArrayList<java.lang.Long>();
			while(rs.next()) {
final java.lang.Long ret = com.moparisthebest.jdbc.util.ResultSetUtil.getObjectLong(rs, 1);
				_colret.add(ret);
			}
			return _colret;
		} finally {
			tryClose(rs);
			tryClose(ps);
		}
	}

	@Override
	public void insertRandomSqlCollection(final java.util.Collection<java.lang.Long> sql) throws java.sql.SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("INSERT " + sql + "");
			int psParamCount = 0;
			psParamCount = com.moparisthebest.jdbc.util.PreparedStatementUtil.recursiveBindIndex(ps, psParamCount, sql);
			ps.executeUpdate();
		} finally {
			tryClose(ps);
		}
	}

	@Override
	public void insertRandomSqlIterable(final java.lang.Iterable<java.lang.Long> sql) throws java.sql.SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("INSERT " + sql + "");
			int psParamCount = 0;
			psParamCount = com.moparisthebest.jdbc.util.PreparedStatementUtil.recursiveBindIndex(ps, psParamCount, sql);
			ps.executeUpdate();
		} finally {
			tryClose(ps);
		}
	}

	@Override
	public java.sql.ResultSet getFieldPeopleResultSet(final java.util.List<java.lang.Long> personNos) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		final Array[] _bindArrays = new Array[1];
		try {
			_bindArrays[0] = conn.createArrayOf("NUMERIC", personNos.toArray(new java.lang.Long[personNos.size()]));
			ps = this.prepareStatement(62, "SELECT person_no, first_name, last_name, birth_date from person WHERE (person_no IN(UNNEST(?))) ORDER BY person_no");
			ps.setArray(1, _bindArrays[0]);
			rs = ps.executeQuery();
			return rs;
		} catch(Throwable e) {
			tryClose(rs);
			if(e instanceof SQLException)
				throw (SQLException)e;
			if(e instanceof RuntimeException)
				throw (RuntimeException)e;
			throw new RuntimeException(e);
		} finally {
			for(final Array _bindArray : _bindArrays)
				tryClose(_bindArray);
		}
	}

	private static final java.lang.reflect.Field[] _fields = new java.lang.reflect.Field[] {
		com.moparisthebest.jdbc.util.ReflectionUtil.getAccessibleField(com.moparisthebest.jdbc.dto.FieldPerson.class, "personNo"),
		com.moparisthebest.jdbc.util.ReflectionUtil.getAccessibleField(com.moparisthebest.jdbc.dto.FieldPerson.class, "firstName"),
		com.moparisthebest.jdbc.util.ReflectionUtil.getAccessibleField(com.moparisthebest.jdbc.dto.FieldPerson.class, "lastName"),
		com.moparisthebest.jdbc.util.ReflectionUtil.getAccessibleField(com.moparisthebest.jdbc.dto.FieldPerson.class, "birthDate"),
		com.moparisthebest.jdbc.util.ReflectionUtil.getAccessibleField(com.moparisthebest.jdbc.dto.FieldBoss.class, "department"),
		com.moparisthebest.jdbc.util.ReflectionUtil.getAccessibleField(com.moparisthebest.jdbc.dto.FieldBoss.class, "first_name"),
		com.moparisthebest.jdbc.util.ReflectionUtil.getAccessibleField(com.moparisthebest.jdbc.dto.ReverseFieldPerson.class, "personNo"),
		com.moparisthebest.jdbc.util.ReflectionUtil.getAccessibleField(com.moparisthebest.jdbc.dto.ReverseFieldBoss.class, "firstName"),
		com.moparisthebest.jdbc.util.ReflectionUtil.getAccessibleField(com.moparisthebest.jdbc.dto.ReverseFieldPerson.class, "lastName"),
		com.moparisthebest.jdbc.util.ReflectionUtil.getAccessibleField(com.moparisthebest.jdbc.dto.ReverseFieldPerson.class, "birthDate"),
		com.moparisthebest.jdbc.util.ReflectionUtil.getAccessibleField(com.moparisthebest.jdbc.dto.ReverseFieldBoss.class, "department"),
		com.moparisthebest.jdbc.util.ReflectionUtil.getAccessibleField(com.moparisthebest.jdbc.dto.ReverseFieldPerson.class, "first_name"),
	};

	private final PreparedStatement[] psCache = new PreparedStatement[63];

	private PreparedStatement prepareStatement(final int index, final String sql) throws SQLException {
		final PreparedStatement ps = psCache[index];
		return ps == null ? (psCache[index] = conn.prepareStatement(sql)) : ps;
	}

	@Override
	public void close() {
		for(final PreparedStatement ps : psCache)
			tryClose(ps);
	}

	public QmDaoUnNestBean(final Factory<Connection> connectionFactory) throws SQLException {
		this(connectionFactory.create(), true);
	}

	public QmDaoUnNestBean(final String jndiName) throws SQLException {
		this(com.moparisthebest.jdbc.codegen.JdbcMapperFactory.connectionFactory(jndiName));
	}

	@Override
	public SqlBuilder sqlBuilder() {
		return SqlBuilder.of(conn, com.moparisthebest.jdbc.UnNestArrayInList.instance());
	}
}
