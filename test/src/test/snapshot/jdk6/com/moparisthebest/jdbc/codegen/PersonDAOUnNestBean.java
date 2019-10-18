package com.moparisthebest.jdbc.codegen;

import com.moparisthebest.jdbc.util.SqlBuilder;
import com.moparisthebest.jdbc.Factory;

import java.sql.*;

import static com.moparisthebest.jdbc.util.ResultSetUtil.*;
import static com.moparisthebest.jdbc.TryClose.tryClose;

public class PersonDAOUnNestBean implements PersonDAO {

	private final Connection conn;
	private final boolean closeConn;

	private static final Factory<Connection> _conFactory = com.moparisthebest.jdbc.codegen.JdbcMapperFactory.connectionFactory("bob");

	public PersonDAOUnNestBean() throws SQLException {
		this(_conFactory);
	}

	private PersonDAOUnNestBean(final Connection conn, final boolean closeConn) {
		this.conn = conn;
		this.closeConn = closeConn;
		if (this.conn == null)
			throw new NullPointerException("Connection needs to be non-null for JdbcMapper...");
	}

	public PersonDAOUnNestBean(Connection conn) {
		this(conn, false);
	}

	public Connection getConnection() {
		return this.conn;
	}

	@Override
	public void createTablePerson() {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("CREATE TABLE person (person_no NUMERIC, first_name VARCHAR(40), last_name VARCHAR(40), birth_date TIMESTAMP)");
			ps.executeUpdate();
		} catch(SQLException e) {
			throw new RuntimeException(e);
		} finally {
			tryClose(ps);
		}
	}

	@Override
	public int insertPerson(final long personNo, final java.util.Date birthDate, final java.lang.String firstName, final java.lang.String lastName) {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("INSERT INTO person (person_no, birth_date, last_name, first_name) VALUES (?, ?, ?, ?)");
			ps.setObject(1, personNo);
			ps.setObject(2, birthDate == null ? null : new java.sql.Timestamp(birthDate.getTime()));
			ps.setObject(3, firstName);
			ps.setObject(4, lastName);
			return ps.executeUpdate();
		} catch(SQLException e) {
			throw new RuntimeException(e);
		} finally {
			tryClose(ps);
		}
	}

	@Override
	public long insertPersonGeneratedKey(final long personNo, final java.util.Date birthDate, final java.lang.String firstName, final java.lang.String lastName) {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("INSERT INTO person (person_no, birth_date, last_name, first_name) VALUES (?, ?, ?, ?)", java.sql.Statement.RETURN_GENERATED_KEYS);
			ps.setObject(1, personNo);
			ps.setObject(2, birthDate == null ? null : new java.sql.Timestamp(birthDate.getTime()));
			ps.setObject(3, firstName);
			ps.setObject(4, lastName);
			ps.executeUpdate();
			ResultSet rs = null;
			try {
				rs = ps.getGeneratedKeys();
				return rs.next() ? rs.getLong(1) : 0;
			} finally {
				tryClose(rs);
			}
		} catch(SQLException e) {
			throw new RuntimeException(e);
		} finally {
			tryClose(ps);
		}
	}

	@Override
	public java.lang.Long insertPersonGeneratedKeyLong(final long personNo, final java.util.Date birthDate, final java.lang.String firstName, final java.lang.String lastName) {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("INSERT INTO person (person_no, birth_date, last_name, first_name) VALUES (?, ?, ?, ?)", java.sql.Statement.RETURN_GENERATED_KEYS);
			ps.setObject(1, personNo);
			ps.setObject(2, birthDate == null ? null : new java.sql.Timestamp(birthDate.getTime()));
			ps.setObject(3, firstName);
			ps.setObject(4, lastName);
			ps.executeUpdate();
			ResultSet rs = null;
			try {
				rs = ps.getGeneratedKeys();
				return rs.next() ? com.moparisthebest.jdbc.util.ResultSetUtil.getObjectLong(rs, 1) : null;
			} finally {
				tryClose(rs);
			}
		} catch(SQLException e) {
			throw new RuntimeException(e);
		} finally {
			tryClose(ps);
		}
	}

	@Override
	public int setFirstName(final java.lang.String firstName, final java.lang.String lastName) {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("UPDATE person SET first_name = ? WHERE last_name = ?");
			ps.setObject(1, firstName);
			ps.setObject(2, lastName);
			return ps.executeUpdate();
		} catch(SQLException e) {
			throw new RuntimeException(e);
		} finally {
			tryClose(ps);
		}
	}

	@Override
	public void setFirstName(final java.lang.String firstName, final long personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("UPDATE person SET first_name = ? WHERE person_no = ?");
			ps.setObject(1, firstName);
			ps.setObject(2, personNo);
			ps.executeUpdate();
		} finally {
			tryClose(ps);
		}
	}

	@Override
	public void setFirstNameBlob(final byte[] firstName, final long personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("UPDATE person SET first_name = ? WHERE person_no = ?");
			ps.setBlob(1, firstName == null ? null : new java.io.ByteArrayInputStream(firstName));
			ps.setObject(2, personNo);
			ps.executeUpdate();
		} finally {
			tryClose(ps);
		}
	}

	@Override
	public void setFirstNameBlob(final java.lang.String firstName, final long personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("UPDATE person SET first_name = ? WHERE person_no = ?");
			try {
				ps.setBlob(1, firstName == null ? null : new java.io.ByteArrayInputStream(firstName.getBytes("UTF-8")));
			} catch (java.io.UnsupportedEncodingException e) {
				throw new SQLException("String to Blob UnsupportedEncodingException", e);
			}
			ps.setObject(2, personNo);
			ps.executeUpdate();
		} finally {
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

	@Override
	public long getPersonNoUnusedParam(final java.lang.String lastName, final java.lang.String _bla) throws java.sql.SQLException {
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

	@Override
	public java.sql.ResultSet getPeopleResultSet(final java.lang.String lastName) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT first_name, last_name FROM person WHERE last_name = ?");
			ps.setObject(1, lastName);
			rs = ps.executeQuery();
			return new com.moparisthebest.jdbc.StatementClosingResultSet(rs, ps);
		} catch(Throwable e) {
			tryClose(rs);
			tryClose(ps);
			if(e instanceof SQLException)
				throw (SQLException)e;
			if(e instanceof RuntimeException)
				throw (RuntimeException)e;
			throw new RuntimeException(e);
		}
	}

	@Override
	public java.sql.ResultSet getPeopleResultSetCached(final java.lang.String lastName) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.prepareStatement(0, "SELECT first_name, last_name FROM person WHERE last_name = ?");
			ps.setObject(1, lastName);
			rs = ps.executeQuery();
			return rs;
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
	public java.lang.String getFirstNameColumnNames(final long personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT first_name FROM person WHERE person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final java.lang.String ret = rs.getString(1);
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
	public java.lang.String getFirstName(final long personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT first_name FROM person WHERE person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final java.lang.String ret = rs.getString(1);
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
	public int getFirstNameUpdate(final long personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		try {
			ps = conn.prepareStatement("SELECT first_name FROM person WHERE person_no = ?");
			ps.setObject(1, personNo);
			return ps.executeUpdate();
		} finally {
			tryClose(ps);
		}
	}

	@Override
	public com.moparisthebest.jdbc.dto.FieldPerson getPersonColumnNames(final long personNo) throws java.sql.SQLException {
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
	public com.moparisthebest.jdbc.dto.BuilderPerson getBuilderPerson(final long personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT person_no, first_name, last_name, birth_date FROM person WHERE person_no = ?");
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
			tryClose(ps);
		}
	}

	@Override
	public com.moparisthebest.jdbc.dto.FieldPerson getPerson(final long personNo, final java.util.Calendar cal) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT first_name, last_name, birth_date FROM person WHERE person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final com.moparisthebest.jdbc.dto.FieldPerson ret = new com.moparisthebest.jdbc.dto.FieldPerson();
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[0], ret, rs.getString(1));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[1], ret, rs.getString(2));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[2], ret, com.moparisthebest.jdbc.util.ResultSetUtil.getUtilDate(rs, 3, cal));
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
	public com.moparisthebest.jdbc.dto.TypeUsePerson getTypeUsePerson(final long personNo, final java.util.Calendar cal) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT person_no, first_name, last_name, birth_date FROM person WHERE person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final com.moparisthebest.jdbc.dto.TypeUsePerson ret = new com.moparisthebest.jdbc.dto.TypeUsePerson();
ret.personNo = rs.getLong(1);
ret.firstName = rs.getString(2);
ret.lastName = rs.getString(3);
ret.birthDate = com.moparisthebest.jdbc.util.ResultSetUtil.getUtilDate(rs, 4, cal);
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
	public java.util.List<com.moparisthebest.jdbc.dto.FieldPerson> getPeople(final java.lang.String lastName) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT first_name, last_name FROM person WHERE last_name = ?");
			ps.setObject(1, lastName);
			rs = ps.executeQuery();
			final java.util.List<com.moparisthebest.jdbc.dto.FieldPerson> _colret = new java.util.ArrayList<com.moparisthebest.jdbc.dto.FieldPerson>();
			while(rs.next()) {
final com.moparisthebest.jdbc.dto.FieldPerson ret = new com.moparisthebest.jdbc.dto.FieldPerson();
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[0], ret, rs.getString(1));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[1], ret, rs.getString(2));
				_colret.add(ret);
			}
			return _colret;
		} finally {
			tryClose(rs);
			tryClose(ps);
		}
	}

	@Override
	public java.util.List<com.moparisthebest.jdbc.dto.FieldPerson> getPeople(final java.lang.String[] lastName) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		final Array[] _bindArrays = new Array[1];
		try {
			_bindArrays[0] = conn.createArrayOf("TEXT", lastName);
			ps = conn.prepareStatement("SELECT first_name, last_name FROM person WHERE (NOT(last_name = ANY(?)))");
			ps.setArray(1, _bindArrays[0]);
			rs = ps.executeQuery();
			final java.util.List<com.moparisthebest.jdbc.dto.FieldPerson> _colret = new java.util.ArrayList<com.moparisthebest.jdbc.dto.FieldPerson>();
			while(rs.next()) {
final com.moparisthebest.jdbc.dto.FieldPerson ret = new com.moparisthebest.jdbc.dto.FieldPerson();
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[0], ret, rs.getString(1));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[1], ret, rs.getString(2));
				_colret.add(ret);
			}
			return _colret;
		} finally {
			for(final Array _bindArray : _bindArrays)
				tryClose(_bindArray);
			tryClose(rs);
			tryClose(ps);
		}
	}

	@Override
	public java.util.List<com.moparisthebest.jdbc.dto.FieldPerson> getPeople(final java.util.List<java.lang.String> lastName) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		final Array[] _bindArrays = new Array[1];
		try {
			_bindArrays[0] = conn.createArrayOf("TEXT", lastName.toArray(new java.lang.String[lastName.size()]));
			ps = conn.prepareStatement("SELECT first_name, last_name FROM person WHERE (last_name = ANY(?))");
			ps.setArray(1, _bindArrays[0]);
			rs = ps.executeQuery();
			final java.util.List<com.moparisthebest.jdbc.dto.FieldPerson> _colret = new java.util.ArrayList<com.moparisthebest.jdbc.dto.FieldPerson>();
			while(rs.next()) {
final com.moparisthebest.jdbc.dto.FieldPerson ret = new com.moparisthebest.jdbc.dto.FieldPerson();
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[0], ret, rs.getString(1));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[1], ret, rs.getString(2));
				_colret.add(ret);
			}
			return _colret;
		} finally {
			for(final Array _bindArray : _bindArrays)
				tryClose(_bindArray);
			tryClose(rs);
			tryClose(ps);
		}
	}

	@Override
	public com.moparisthebest.jdbc.dto.FieldPerson[] getPeopleArray(final java.lang.String lastName) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT first_name, last_name FROM person WHERE last_name = ?");
			ps.setObject(1, lastName);
			rs = ps.executeQuery();
			final java.util.List<com.moparisthebest.jdbc.dto.FieldPerson> _colret = new java.util.ArrayList<com.moparisthebest.jdbc.dto.FieldPerson>();
			while(rs.next()) {
final com.moparisthebest.jdbc.dto.FieldPerson ret = new com.moparisthebest.jdbc.dto.FieldPerson();
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[0], ret, rs.getString(1));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[1], ret, rs.getString(2));
				_colret.add(ret);
			}
			return _colret.toArray(new com.moparisthebest.jdbc.dto.FieldPerson[_colret.size()]);
		} finally {
			tryClose(rs);
			tryClose(ps);
		}
	}

	@Override
	public java.util.Iterator<com.moparisthebest.jdbc.dto.FieldPerson> getPeopleIterator(final java.lang.String lastName) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT first_name, last_name FROM person WHERE last_name = ?");
			ps.setObject(1, lastName);
			rs = ps.executeQuery();
			final java.util.List<com.moparisthebest.jdbc.dto.FieldPerson> _colret = new java.util.ArrayList<com.moparisthebest.jdbc.dto.FieldPerson>();
			while(rs.next()) {
final com.moparisthebest.jdbc.dto.FieldPerson ret = new com.moparisthebest.jdbc.dto.FieldPerson();
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[0], ret, rs.getString(1));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[1], ret, rs.getString(2));
				_colret.add(ret);
			}
			return _colret.iterator();
		} finally {
			tryClose(rs);
			tryClose(ps);
		}
	}

	@Override
	public java.util.ListIterator<com.moparisthebest.jdbc.dto.FieldPerson> getPeopleListIterator(final java.lang.String lastName) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT first_name, last_name FROM person WHERE last_name = ?");
			ps.setObject(1, lastName);
			rs = ps.executeQuery();
			final java.util.List<com.moparisthebest.jdbc.dto.FieldPerson> _colret = new java.util.ArrayList<com.moparisthebest.jdbc.dto.FieldPerson>();
			while(rs.next()) {
final com.moparisthebest.jdbc.dto.FieldPerson ret = new com.moparisthebest.jdbc.dto.FieldPerson();
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[0], ret, rs.getString(1));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[1], ret, rs.getString(2));
				_colret.add(ret);
			}
			return _colret.listIterator();
		} finally {
			tryClose(rs);
			tryClose(ps);
		}
	}

	@Override
	public java.util.Map<java.lang.String,com.moparisthebest.jdbc.dto.FieldPerson> getPersonMap(final java.lang.String lastName) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT first_name, last_name, person_no FROM person WHERE last_name = ?");
			ps.setObject(1, lastName);
			rs = ps.executeQuery();
			final java.util.Map<java.lang.String,com.moparisthebest.jdbc.dto.FieldPerson> _colret = new java.util.HashMap<java.lang.String,com.moparisthebest.jdbc.dto.FieldPerson>();
			while(rs.next()) {
final com.moparisthebest.jdbc.dto.FieldPerson ret = new com.moparisthebest.jdbc.dto.FieldPerson();
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[0], ret, rs.getString(1));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[1], ret, rs.getString(2));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[3], ret, rs.getLong(3));
				_colret.put(rs.getString(1), ret);
			}
			return _colret;
		} finally {
			tryClose(rs);
			tryClose(ps);
		}
	}

	@Override
	public java.util.Map<java.lang.String,java.util.List<com.moparisthebest.jdbc.dto.FieldPerson>> getPersonMapList(final java.lang.String lastName) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT first_name, last_name, person_no FROM person WHERE last_name = ?");
			ps.setObject(1, lastName);
			rs = ps.executeQuery();
			final java.util.Map<java.lang.String,java.util.List<com.moparisthebest.jdbc.dto.FieldPerson>> _colret = new java.util.HashMap<java.lang.String,java.util.List<com.moparisthebest.jdbc.dto.FieldPerson>>();
			while(rs.next()) {
final com.moparisthebest.jdbc.dto.FieldPerson ret = new com.moparisthebest.jdbc.dto.FieldPerson();
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[0], ret, rs.getString(1));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[1], ret, rs.getString(2));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[3], ret, rs.getLong(3));
				final java.lang.String _colkey = rs.getString(1);
				java.util.List<com.moparisthebest.jdbc.dto.FieldPerson> _collist = _colret.get(_colkey);
				if(_collist == null) {
					_collist = new java.util.ArrayList<com.moparisthebest.jdbc.dto.FieldPerson>();
					_colret.put(_colkey, _collist);
				}
				_collist.add(ret);
			}
			return _colret;
		} finally {
			tryClose(rs);
			tryClose(ps);
		}
	}

	@Override
	public java.lang.String getFirstName(final long personNo, final java.lang.String lastName) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT first_name FROM person WHERE person_no = ? and last_name = ?");
			ps.setObject(1, personNo);
			ps.setObject(2, lastName);
			rs = ps.executeQuery();
			if(rs.next()) {
final java.lang.String ret = rs.getString(1);
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
	public java.util.Calendar getBirthDateCalendar(final long personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT birth_date FROM person WHERE person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final java.util.Calendar ret = com.moparisthebest.jdbc.util.ResultSetUtil.getCalendar(rs, 1);
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
	public java.util.Calendar getBirthDateCalendar(final long personNo, final java.util.Calendar mycal) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT birth_date FROM person WHERE person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final java.util.Calendar ret = com.moparisthebest.jdbc.util.ResultSetUtil.getCalendar(rs, 1, mycal);
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
	public java.sql.Timestamp getBirthDateTimestamp(final long personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT birth_date FROM person WHERE person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final java.sql.Timestamp ret = rs.getTimestamp(1);
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
	public java.sql.Timestamp getBirthDateTimestamp(final long personNo, final java.util.Calendar mycal) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT birth_date FROM person WHERE person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final java.sql.Timestamp ret = rs.getTimestamp(1, mycal);
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
	public java.sql.Time getBirthDateTime(final long personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT birth_date FROM person WHERE person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final java.sql.Time ret = rs.getTime(1);
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
	public java.sql.Time getBirthDateTime(final long personNo, final java.util.Calendar mycal) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT birth_date FROM person WHERE person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final java.sql.Time ret = rs.getTime(1, mycal);
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
	public java.sql.Date getBirthDateSqlDate(final long personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT birth_date FROM person WHERE person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final java.sql.Date ret = rs.getDate(1);
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
	public java.sql.Date getBirthDateSqlDate(final long personNo, final java.util.Calendar mycal) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT birth_date FROM person WHERE person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final java.sql.Date ret = rs.getDate(1, mycal);
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
	public java.util.Date getBirthDateUtilDate(final long personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT birth_date FROM person WHERE person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final java.util.Date ret = com.moparisthebest.jdbc.util.ResultSetUtil.getUtilDate(rs, 1);
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
	public java.util.Date getBirthDateUtilDate(final long personNo, final java.util.Calendar mycal) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT birth_date FROM person WHERE person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final java.util.Date ret = com.moparisthebest.jdbc.util.ResultSetUtil.getUtilDate(rs, 1, mycal);
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
	public com.moparisthebest.jdbc.dto.FieldPerson getPersonCleanFieldPerson(final long personNo, final com.moparisthebest.jdbc.Cleaner<com.moparisthebest.jdbc.dto.FieldPerson> clean) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT first_name, last_name, birth_date FROM person WHERE person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final com.moparisthebest.jdbc.dto.FieldPerson ret = new com.moparisthebest.jdbc.dto.FieldPerson();
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[0], ret, rs.getString(1));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[1], ret, rs.getString(2));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[2], ret, com.moparisthebest.jdbc.util.ResultSetUtil.getUtilDate(rs, 3));
				return clean == null ? ret : clean.clean(ret);
			} else {
				return null;
			}
		} finally {
			tryClose(rs);
			tryClose(ps);
		}
	}

	@Override
	public com.moparisthebest.jdbc.dto.FieldPerson getPersonCleanPerson(final long personNo, final com.moparisthebest.jdbc.Cleaner<com.moparisthebest.jdbc.dto.Person> clean) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT first_name, last_name, birth_date FROM person WHERE person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final com.moparisthebest.jdbc.dto.FieldPerson ret = new com.moparisthebest.jdbc.dto.FieldPerson();
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[0], ret, rs.getString(1));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[1], ret, rs.getString(2));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[2], ret, com.moparisthebest.jdbc.util.ResultSetUtil.getUtilDate(rs, 3));
				return clean == null ? ret : clean.clean(ret);
			} else {
				return null;
			}
		} finally {
			tryClose(rs);
			tryClose(ps);
		}
	}

	@Override
	public com.moparisthebest.jdbc.dto.FieldPerson getPersonCleanObject(final long personNo, final com.moparisthebest.jdbc.Cleaner<java.lang.Object> clean) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT first_name, last_name, birth_date FROM person WHERE person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final com.moparisthebest.jdbc.dto.FieldPerson ret = new com.moparisthebest.jdbc.dto.FieldPerson();
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[0], ret, rs.getString(1));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[1], ret, rs.getString(2));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[2], ret, com.moparisthebest.jdbc.util.ResultSetUtil.getUtilDate(rs, 3));
				return clean == null ? ret : clean.clean(ret);
			} else {
				return null;
			}
		} finally {
			tryClose(rs);
			tryClose(ps);
		}
	}

	@Override
	public java.util.List<com.moparisthebest.jdbc.dto.FieldPerson> getPersonCleanPersonList(final long personNo, final com.moparisthebest.jdbc.Cleaner<com.moparisthebest.jdbc.dto.Person> clean) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT first_name, last_name, birth_date FROM person WHERE person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			final java.util.List<com.moparisthebest.jdbc.dto.FieldPerson> _colret = new java.util.ArrayList<com.moparisthebest.jdbc.dto.FieldPerson>();
			while(rs.next()) {
final com.moparisthebest.jdbc.dto.FieldPerson ret = new com.moparisthebest.jdbc.dto.FieldPerson();
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[0], ret, rs.getString(1));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[1], ret, rs.getString(2));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[2], ret, com.moparisthebest.jdbc.util.ResultSetUtil.getUtilDate(rs, 3));
				_colret.add(clean == null ? ret : clean.clean(ret));
			}
			return _colret;
		} finally {
			tryClose(rs);
			tryClose(ps);
		}
	}

	@Override
	public java.util.Map<java.lang.String,com.moparisthebest.jdbc.dto.FieldPerson> getPersonCleanPersonMap(final long personNo, final com.moparisthebest.jdbc.Cleaner<com.moparisthebest.jdbc.dto.Person> clean) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT first_name, last_name, birth_date FROM person WHERE person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			final java.util.Map<java.lang.String,com.moparisthebest.jdbc.dto.FieldPerson> _colret = new java.util.HashMap<java.lang.String,com.moparisthebest.jdbc.dto.FieldPerson>();
			while(rs.next()) {
final com.moparisthebest.jdbc.dto.FieldPerson ret = new com.moparisthebest.jdbc.dto.FieldPerson();
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[0], ret, rs.getString(1));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[1], ret, rs.getString(2));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[2], ret, com.moparisthebest.jdbc.util.ResultSetUtil.getUtilDate(rs, 3));
				_colret.put(rs.getString(1), clean == null ? ret : clean.clean(ret));
			}
			return _colret;
		} finally {
			tryClose(rs);
			tryClose(ps);
		}
	}

	@Override
	public java.util.Map<java.lang.String,java.util.List<com.moparisthebest.jdbc.dto.FieldPerson>> getPersonCleanPersonMapList(final long personNo, final com.moparisthebest.jdbc.Cleaner<com.moparisthebest.jdbc.dto.Person> clean) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT first_name, last_name, birth_date FROM person WHERE person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			final java.util.Map<java.lang.String,java.util.List<com.moparisthebest.jdbc.dto.FieldPerson>> _colret = new java.util.HashMap<java.lang.String,java.util.List<com.moparisthebest.jdbc.dto.FieldPerson>>();
			while(rs.next()) {
final com.moparisthebest.jdbc.dto.FieldPerson ret = new com.moparisthebest.jdbc.dto.FieldPerson();
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[0], ret, rs.getString(1));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[1], ret, rs.getString(2));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[2], ret, com.moparisthebest.jdbc.util.ResultSetUtil.getUtilDate(rs, 3));
				final java.lang.String _colkey = rs.getString(1);
				java.util.List<com.moparisthebest.jdbc.dto.FieldPerson> _collist = _colret.get(_colkey);
				if(_collist == null) {
					_collist = new java.util.ArrayList<com.moparisthebest.jdbc.dto.FieldPerson>();
					_colret.put(_colkey, _collist);
				}
				_collist.add(clean == null ? ret : clean.clean(ret));
			}
			return _colret;
		} finally {
			tryClose(rs);
			tryClose(ps);
		}
	}

	@Override
	public java.util.Map<java.lang.String,com.moparisthebest.jdbc.dto.FieldPerson> getPersonDynamicLimit(final long personNo, final byte maxRows) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT first_name, last_name, birth_date FROM person WHERE person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			byte _rowCount = 0;
			final java.util.Map<java.lang.String,com.moparisthebest.jdbc.dto.FieldPerson> _colret = new java.util.HashMap<java.lang.String,com.moparisthebest.jdbc.dto.FieldPerson>();
			while(rs.next()) {
final com.moparisthebest.jdbc.dto.FieldPerson ret = new com.moparisthebest.jdbc.dto.FieldPerson();
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[0], ret, rs.getString(1));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[1], ret, rs.getString(2));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[2], ret, com.moparisthebest.jdbc.util.ResultSetUtil.getUtilDate(rs, 3));
				_colret.put(rs.getString(1), ret);
				if(maxRows > 0 && ++_rowCount == maxRows)
					break;
			}
			return _colret;
		} finally {
			tryClose(rs);
			tryClose(ps);
		}
	}

	@Override
	public java.util.Map<java.lang.String,com.moparisthebest.jdbc.dto.FieldPerson> getPersonDynamicLimit(final long personNo, final short arrayMaxLength) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT first_name, last_name, birth_date FROM person WHERE person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			short _rowCount = 0;
			final java.util.Map<java.lang.String,com.moparisthebest.jdbc.dto.FieldPerson> _colret = new java.util.HashMap<java.lang.String,com.moparisthebest.jdbc.dto.FieldPerson>();
			while(rs.next()) {
final com.moparisthebest.jdbc.dto.FieldPerson ret = new com.moparisthebest.jdbc.dto.FieldPerson();
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[0], ret, rs.getString(1));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[1], ret, rs.getString(2));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[2], ret, com.moparisthebest.jdbc.util.ResultSetUtil.getUtilDate(rs, 3));
				_colret.put(rs.getString(1), ret);
				if(arrayMaxLength > 0 && ++_rowCount == arrayMaxLength)
					break;
			}
			return _colret;
		} finally {
			tryClose(rs);
			tryClose(ps);
		}
	}

	@Override
	public java.util.List<com.moparisthebest.jdbc.dto.FieldPerson> getPersonDynamicLimit(final long personNo, final int maxRows) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT first_name, last_name, birth_date FROM person WHERE person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			int _rowCount = 0;
			final java.util.List<com.moparisthebest.jdbc.dto.FieldPerson> _colret = new java.util.ArrayList<com.moparisthebest.jdbc.dto.FieldPerson>();
			while(rs.next()) {
final com.moparisthebest.jdbc.dto.FieldPerson ret = new com.moparisthebest.jdbc.dto.FieldPerson();
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[0], ret, rs.getString(1));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[1], ret, rs.getString(2));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[2], ret, com.moparisthebest.jdbc.util.ResultSetUtil.getUtilDate(rs, 3));
				_colret.add(ret);
				if(maxRows > 0 && ++_rowCount == maxRows)
					break;
			}
			return _colret;
		} finally {
			tryClose(rs);
			tryClose(ps);
		}
	}

	@Override
	public java.util.Map<java.lang.String,java.util.List<com.moparisthebest.jdbc.dto.FieldPerson>> getPersonDynamicLimit(final long personNo, final long rowLimit) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT first_name, last_name, birth_date FROM person WHERE person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			long _rowCount = 0;
			final java.util.Map<java.lang.String,java.util.List<com.moparisthebest.jdbc.dto.FieldPerson>> _colret = new java.util.HashMap<java.lang.String,java.util.List<com.moparisthebest.jdbc.dto.FieldPerson>>();
			while(rs.next()) {
final com.moparisthebest.jdbc.dto.FieldPerson ret = new com.moparisthebest.jdbc.dto.FieldPerson();
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[0], ret, rs.getString(1));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[1], ret, rs.getString(2));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[2], ret, com.moparisthebest.jdbc.util.ResultSetUtil.getUtilDate(rs, 3));
				final java.lang.String _colkey = rs.getString(1);
				java.util.List<com.moparisthebest.jdbc.dto.FieldPerson> _collist = _colret.get(_colkey);
				if(_collist == null) {
					_collist = new java.util.ArrayList<com.moparisthebest.jdbc.dto.FieldPerson>();
					_colret.put(_colkey, _collist);
				}
				_collist.add(ret);
				if(rowLimit > 0 && ++_rowCount == rowLimit)
					break;
			}
			return _colret;
		} finally {
			tryClose(rs);
			tryClose(ps);
		}
	}

	@Override
	public java.util.List<com.moparisthebest.jdbc.dto.FieldPerson> getPersonStaticLimitListByte(final long personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT first_name, last_name, birth_date FROM person WHERE person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			byte _rowCount = 0;
			final java.util.List<com.moparisthebest.jdbc.dto.FieldPerson> _colret = new java.util.ArrayList<com.moparisthebest.jdbc.dto.FieldPerson>();
			while(rs.next()) {
final com.moparisthebest.jdbc.dto.FieldPerson ret = new com.moparisthebest.jdbc.dto.FieldPerson();
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[0], ret, rs.getString(1));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[1], ret, rs.getString(2));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[2], ret, com.moparisthebest.jdbc.util.ResultSetUtil.getUtilDate(rs, 3));
				_colret.add(ret);
				if(++_rowCount == 127)
					break;
			}
			return _colret;
		} finally {
			tryClose(rs);
			tryClose(ps);
		}
	}

	@Override
	public java.util.List<com.moparisthebest.jdbc.dto.FieldPerson> getPersonStaticLimitList(final long personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT first_name, last_name, birth_date FROM person WHERE person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			short _rowCount = 0;
			final java.util.List<com.moparisthebest.jdbc.dto.FieldPerson> _colret = new java.util.ArrayList<com.moparisthebest.jdbc.dto.FieldPerson>();
			while(rs.next()) {
final com.moparisthebest.jdbc.dto.FieldPerson ret = new com.moparisthebest.jdbc.dto.FieldPerson();
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[0], ret, rs.getString(1));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[1], ret, rs.getString(2));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[2], ret, com.moparisthebest.jdbc.util.ResultSetUtil.getUtilDate(rs, 3));
				_colret.add(ret);
				if(++_rowCount == 32767)
					break;
			}
			return _colret;
		} finally {
			tryClose(rs);
			tryClose(ps);
		}
	}

	@Override
	public java.util.Map<java.lang.String,com.moparisthebest.jdbc.dto.FieldPerson> getPersonStaticLimitMap(final long personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT first_name, last_name, birth_date FROM person WHERE person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			int _rowCount = 0;
			final java.util.Map<java.lang.String,com.moparisthebest.jdbc.dto.FieldPerson> _colret = new java.util.HashMap<java.lang.String,com.moparisthebest.jdbc.dto.FieldPerson>();
			while(rs.next()) {
final com.moparisthebest.jdbc.dto.FieldPerson ret = new com.moparisthebest.jdbc.dto.FieldPerson();
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[0], ret, rs.getString(1));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[1], ret, rs.getString(2));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[2], ret, com.moparisthebest.jdbc.util.ResultSetUtil.getUtilDate(rs, 3));
				_colret.put(rs.getString(1), ret);
				if(++_rowCount == 2147483647)
					break;
			}
			return _colret;
		} finally {
			tryClose(rs);
			tryClose(ps);
		}
	}

	@Override
	public java.util.Map<java.lang.String,java.util.List<com.moparisthebest.jdbc.dto.FieldPerson>> getPersonStaticLimitMapList(final long personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT first_name, last_name, birth_date FROM person WHERE person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			long _rowCount = 0;
			final java.util.Map<java.lang.String,java.util.List<com.moparisthebest.jdbc.dto.FieldPerson>> _colret = new java.util.HashMap<java.lang.String,java.util.List<com.moparisthebest.jdbc.dto.FieldPerson>>();
			while(rs.next()) {
final com.moparisthebest.jdbc.dto.FieldPerson ret = new com.moparisthebest.jdbc.dto.FieldPerson();
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[0], ret, rs.getString(1));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[1], ret, rs.getString(2));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[2], ret, com.moparisthebest.jdbc.util.ResultSetUtil.getUtilDate(rs, 3));
				final java.lang.String _colkey = rs.getString(1);
				java.util.List<com.moparisthebest.jdbc.dto.FieldPerson> _collist = _colret.get(_colkey);
				if(_collist == null) {
					_collist = new java.util.ArrayList<com.moparisthebest.jdbc.dto.FieldPerson>();
					_colret.put(_colkey, _collist);
				}
				_collist.add(ret);
				if(++_rowCount == 9223372036854775807L)
					break;
			}
			return _colret;
		} finally {
			tryClose(rs);
			tryClose(ps);
		}
	}

	@Override
	public java.util.List<com.moparisthebest.jdbc.dto.FieldPerson> getPeopleList(final long personNo1, final long personNo2, final long personNo3) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT person_no, birth_date, last_name, first_name from person WHERE person_no IN (?,?,?) ORDER BY person_no");
			ps.setObject(1, personNo1);
			ps.setObject(2, personNo2);
			ps.setObject(3, personNo3);
			rs = ps.executeQuery();
			final java.util.List<com.moparisthebest.jdbc.dto.FieldPerson> _colret = new java.util.ArrayList<com.moparisthebest.jdbc.dto.FieldPerson>();
			while(rs.next()) {
final com.moparisthebest.jdbc.dto.FieldPerson ret = new com.moparisthebest.jdbc.dto.FieldPerson(
rs.getLong(1),
com.moparisthebest.jdbc.util.ResultSetUtil.getUtilDate(rs, 2),
rs.getString(4),
rs.getString(3));
				_colret.add(ret);
			}
			return _colret;
		} finally {
			tryClose(rs);
			tryClose(ps);
		}
	}

	@Override
	public com.moparisthebest.jdbc.util.ResultSetIterable<com.moparisthebest.jdbc.dto.FieldPerson> getPeopleResultSetIterable(final long personNo1, final long personNo2, final long personNo3) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT person_no, birth_date, last_name, first_name from person WHERE person_no IN (?,?,?) ORDER BY person_no");
			ps.setObject(1, personNo1);
			ps.setObject(2, personNo2);
			ps.setObject(3, personNo3);
			rs = ps.executeQuery();
			return com.moparisthebest.jdbc.util.ResultSetIterable.getResultSetIterable(rs,
					rs.next() ? new com.moparisthebest.jdbc.util.ResultSetToObject<com.moparisthebest.jdbc.dto.FieldPerson>() {
					public com.moparisthebest.jdbc.dto.FieldPerson toObject(final ResultSet _rs, final java.util.Calendar _cal) throws SQLException {
final com.moparisthebest.jdbc.dto.FieldPerson ret = new com.moparisthebest.jdbc.dto.FieldPerson(
_rs.getLong(1),
com.moparisthebest.jdbc.util.ResultSetUtil.getUtilDate(_rs, 2),
_rs.getString(4),
_rs.getString(3));
						return ret;
					}
					}
				: null, null).setPreparedStatementToClose(ps);
		} catch(Throwable e) {
			tryClose(rs);
			tryClose(ps);
			if(e instanceof SQLException)
				throw (SQLException)e;
			if(e instanceof RuntimeException)
				throw (RuntimeException)e;
			throw new RuntimeException(e);
		}
	}

	@Override
	public com.moparisthebest.jdbc.util.ResultSetIterable<com.moparisthebest.jdbc.dto.FieldPerson> getPeopleResultSetIterableCachedPreparedStatement(final long personNo1, final long personNo2, final long personNo3) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = this.prepareStatement(1, "SELECT person_no, birth_date, last_name, first_name from person WHERE person_no IN (?,?,?) ORDER BY person_no");
			ps.setObject(1, personNo1);
			ps.setObject(2, personNo2);
			ps.setObject(3, personNo3);
			rs = ps.executeQuery();
			return com.moparisthebest.jdbc.util.ResultSetIterable.getResultSetIterable(rs,
					rs.next() ? new com.moparisthebest.jdbc.util.ResultSetToObject<com.moparisthebest.jdbc.dto.FieldPerson>() {
					public com.moparisthebest.jdbc.dto.FieldPerson toObject(final ResultSet _rs, final java.util.Calendar _cal) throws SQLException {
final com.moparisthebest.jdbc.dto.FieldPerson ret = new com.moparisthebest.jdbc.dto.FieldPerson(
_rs.getLong(1),
com.moparisthebest.jdbc.util.ResultSetUtil.getUtilDate(_rs, 2),
_rs.getString(4),
_rs.getString(3));
						return ret;
					}
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
			ps = conn.prepareStatement("SELECT first_name, last_name FROM person WHERE person_no = ?");
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
			tryClose(ps);
		}
	}

	@Override
	public com.moparisthebest.jdbc.dto.EnumPerson getEnumPersonConstructor(final long personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT first_name FROM person WHERE person_no = ?");
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
			tryClose(ps);
		}
	}

	@Override
	public com.moparisthebest.jdbc.dto.FirstName getFirstNameEnum(final long personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT first_name FROM person WHERE person_no = ?");
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
			tryClose(ps);
		}
	}

	@Override
	public com.moparisthebest.jdbc.dto.EnumPerson getEnumPersonNull() throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT str_val as first_name, str_val as last_name FROM val WHERE val_no = 4");
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
			tryClose(ps);
		}
	}

	@Override
	public com.moparisthebest.jdbc.dto.FirstName getEnumNull() throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT str_val FROM val WHERE val_no = 4");
			rs = ps.executeQuery();
			if(rs.next()) {
final com.moparisthebest.jdbc.dto.FirstName ret = com.moparisthebest.jdbc.util.ResultSetUtil.getEnum(rs, 1, com.moparisthebest.jdbc.dto.FirstName.class);
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
	public com.moparisthebest.jdbc.dto.CaseSensitivePerson getCaseSensitivePerson(final long personNo) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT first_name AS M_PERSON_FIRST_NAME FROM person WHERE person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final com.moparisthebest.jdbc.dto.CaseSensitivePerson ret = new com.moparisthebest.jdbc.dto.CaseSensitivePerson();
ret.setmPersonFirstName(rs.getString(1));
				return ret;
			} else {
				return null;
			}
		} catch(SQLException e) {
			throw new RuntimeException(e);
		} finally {
			tryClose(rs);
			tryClose(ps);
		}
	}

	@Override
	public com.moparisthebest.jdbc.codegen.SameClassPathEnumPerson getSameClassPathEnumPerson(final long personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT first_name, last_name FROM person WHERE person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final com.moparisthebest.jdbc.codegen.SameClassPathEnumPerson ret = new com.moparisthebest.jdbc.codegen.SameClassPathEnumPerson();
ret.setFirstName(com.moparisthebest.jdbc.util.ResultSetUtil.getEnum(rs, 1, com.moparisthebest.jdbc.codegen.SameClassPathEnumPerson.FirstName.class));
ret.setLastName(rs.getString(2));
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
	public com.moparisthebest.jdbc.codegen.SameClassPathEnumPerson getSameClassPathEnumPersonConstructor(final long personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT first_name FROM person WHERE person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final com.moparisthebest.jdbc.codegen.SameClassPathEnumPerson ret = new com.moparisthebest.jdbc.codegen.SameClassPathEnumPerson(
com.moparisthebest.jdbc.util.ResultSetUtil.getEnum(rs, 1, com.moparisthebest.jdbc.codegen.SameClassPathEnumPerson.FirstName.class));
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
	public com.moparisthebest.jdbc.codegen.SameClassPathEnumPerson.FirstName getSameClassPathFirstNameEnum(final long personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT first_name FROM person WHERE person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final com.moparisthebest.jdbc.codegen.SameClassPathEnumPerson.FirstName ret = com.moparisthebest.jdbc.util.ResultSetUtil.getEnum(rs, 1, com.moparisthebest.jdbc.codegen.SameClassPathEnumPerson.FirstName.class);
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
	public com.moparisthebest.jdbc.codegen.SameClassPathEnumPerson getSameClassPathEnumPersonNull() throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT str_val as first_name, str_val as last_name FROM val WHERE val_no = 4");
			rs = ps.executeQuery();
			if(rs.next()) {
final com.moparisthebest.jdbc.codegen.SameClassPathEnumPerson ret = new com.moparisthebest.jdbc.codegen.SameClassPathEnumPerson();
ret.setFirstName(com.moparisthebest.jdbc.util.ResultSetUtil.getEnum(rs, 1, com.moparisthebest.jdbc.codegen.SameClassPathEnumPerson.FirstName.class));
ret.setLastName(rs.getString(2));
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
	public com.moparisthebest.jdbc.codegen.SameClassPathEnumPerson.FirstName getSameClassPathEnumNull() throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT str_val FROM val WHERE val_no = 4");
			rs = ps.executeQuery();
			if(rs.next()) {
final com.moparisthebest.jdbc.codegen.SameClassPathEnumPerson.FirstName ret = com.moparisthebest.jdbc.util.ResultSetUtil.getEnum(rs, 1, com.moparisthebest.jdbc.codegen.SameClassPathEnumPerson.FirstName.class);
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
	public java.util.List<com.moparisthebest.jdbc.dto.FieldPerson> getPeopleInListArray(final java.lang.Long[] personNos) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		final Array[] _bindArrays = new Array[1];
		try {
			_bindArrays[0] = conn.createArrayOf("NUMERIC", personNos);
			ps = conn.prepareStatement("SELECT first_name, last_name FROM person WHERE (person_no = ANY(?))");
			ps.setArray(1, _bindArrays[0]);
			rs = ps.executeQuery();
			final java.util.List<com.moparisthebest.jdbc.dto.FieldPerson> _colret = new java.util.ArrayList<com.moparisthebest.jdbc.dto.FieldPerson>();
			while(rs.next()) {
final com.moparisthebest.jdbc.dto.FieldPerson ret = new com.moparisthebest.jdbc.dto.FieldPerson();
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[0], ret, rs.getString(1));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[1], ret, rs.getString(2));
				_colret.add(ret);
			}
			return _colret;
		} catch(SQLException e) {
			throw new RuntimeException(e);
		} finally {
			for(final Array _bindArray : _bindArrays)
				tryClose(_bindArray);
			tryClose(rs);
			tryClose(ps);
		}
	}

	@Override
	public java.util.List<com.moparisthebest.jdbc.dto.FieldPerson> getPeopleInListCollection(final java.util.Collection<java.lang.Long> personNos) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		final Array[] _bindArrays = new Array[1];
		try {
			_bindArrays[0] = conn.createArrayOf("NUMERIC", personNos.toArray(new java.lang.Long[personNos.size()]));
			ps = conn.prepareStatement("SELECT first_name, last_name FROM person WHERE (person_no = ANY(?))");
			ps.setArray(1, _bindArrays[0]);
			rs = ps.executeQuery();
			final java.util.List<com.moparisthebest.jdbc.dto.FieldPerson> _colret = new java.util.ArrayList<com.moparisthebest.jdbc.dto.FieldPerson>();
			while(rs.next()) {
final com.moparisthebest.jdbc.dto.FieldPerson ret = new com.moparisthebest.jdbc.dto.FieldPerson();
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[0], ret, rs.getString(1));
com.moparisthebest.jdbc.util.ReflectionUtil.setValue(_fields[1], ret, rs.getString(2));
				_colret.add(ret);
			}
			return _colret;
		} catch(SQLException e) {
			throw new RuntimeException(e);
		} finally {
			for(final Array _bindArray : _bindArrays)
				tryClose(_bindArray);
			tryClose(rs);
			tryClose(ps);
		}
	}

	@Override
	public byte[] getBlob(final long valNo) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT some_blob FROM val WHERE val_no = ?");
			ps.setObject(1, valNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final byte[] ret = rs.getBytes(1);
				return ret;
			} else {
				return null;
			}
		} catch(SQLException e) {
			throw new RuntimeException(e);
		} finally {
			tryClose(rs);
			tryClose(ps);
		}
	}

	@Override
	public java.lang.String[] getSinglePersonNameArray(final long personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT first_name, last_name FROM person WHERE person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final java.lang.String[] ret = new java.lang.String[2];
ret[0] = rs.getString(1);
ret[1] = rs.getString(2);
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
	public java.util.Map<java.lang.String,java.lang.String> getSinglePersonNameMap(final long personNo) throws java.sql.SQLException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement("SELECT first_name, last_name FROM person WHERE person_no = ?");
			ps.setObject(1, personNo);
			rs = ps.executeQuery();
			if(rs.next()) {
final java.util.Map<java.lang.String,java.lang.String> ret = new java.util.HashMap<java.lang.String,java.lang.String>();
ret.put("first_name", rs.getString(1));
ret.put("last_name", rs.getString(2));
				return ret;
			} else {
				return null;
			}
		} finally {
			tryClose(rs);
			tryClose(ps);
		}
	}

	private static final java.lang.reflect.Field[] _fields = new java.lang.reflect.Field[] {
		com.moparisthebest.jdbc.util.ReflectionUtil.getAccessibleField(com.moparisthebest.jdbc.dto.FieldPerson.class, "firstName"),
		com.moparisthebest.jdbc.util.ReflectionUtil.getAccessibleField(com.moparisthebest.jdbc.dto.FieldPerson.class, "lastName"),
		com.moparisthebest.jdbc.util.ReflectionUtil.getAccessibleField(com.moparisthebest.jdbc.dto.FieldPerson.class, "birthDate"),
		com.moparisthebest.jdbc.util.ReflectionUtil.getAccessibleField(com.moparisthebest.jdbc.dto.FieldPerson.class, "personNo"),
	};

	private final PreparedStatement[] psCache = new PreparedStatement[2];

	private PreparedStatement prepareStatement(final int index, final String sql) throws SQLException {
		final PreparedStatement ps = psCache[index];
		return ps == null ? (psCache[index] = conn.prepareStatement(sql)) : ps;
	}

	@Override
	public void close() {
		for(final PreparedStatement ps : psCache)
			tryClose(ps);
		if(closeConn)
			tryClose(conn);
	}

	public PersonDAOUnNestBean(final Factory<Connection> connectionFactory) throws SQLException {
		this(connectionFactory.create(), true);
	}

	public PersonDAOUnNestBean(final String jndiName) throws SQLException {
		this(com.moparisthebest.jdbc.codegen.JdbcMapperFactory.connectionFactory(jndiName));
	}

	@Override
	public SqlBuilder sqlBuilder() {
		return SqlBuilder.of(conn, com.moparisthebest.jdbc.ArrayInList.instance());
	}
}
