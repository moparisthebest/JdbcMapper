package com.moparisthebest.jdbc.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
//IFJAVA8_START
import java.time.*;
//IFJAVA8_END

/**
 * Created by mopar on 5/16/17.
 */
public class ResultSetUtil {

	public static final String YES = System.getProperty("UpdateableDTO.YES", "Y");
	public static final String NO = System.getProperty("UpdateableDTO.NO", "N");

	public static Integer getObjectInt(final ResultSet rs, final int index) throws SQLException {
		final int ret = rs.getInt(index);
		return rs.wasNull() ? null : ret;
	}

	public static Long getObjectLong(final ResultSet rs, final int index) throws SQLException {
		final long ret = rs.getLong(index);
		return rs.wasNull() ? null : ret;
	}

	public static Float getObjectFloat(final ResultSet rs, final int index) throws SQLException {
		final float ret = rs.getFloat(index);
		return rs.wasNull() ? null : ret;
	}

	public static Double getObjectDouble(final ResultSet rs, final int index) throws SQLException {
		final double ret = rs.getDouble(index);
		return rs.wasNull() ? null : ret;
	}

	public static Byte getObjectByte(final ResultSet rs, final int index) throws SQLException {
		final byte ret = rs.getByte(index);
		return rs.wasNull() ? null : ret;
	}

	public static Short getObjectShort(final ResultSet rs, final int index) throws SQLException {
		final short ret = rs.getShort(index);
		return rs.wasNull() ? null : ret;
	}

	public static Boolean getObjectBoolean(final ResultSet rs, final int index) throws SQLException {
		final boolean ret = rs.getBoolean(index);
		return rs.wasNull() ? null : (ret ? Boolean.TRUE : Boolean.FALSE);
	}

	public static Boolean getObjectBooleanYN(final ResultSet rs, final int index) throws SQLException {
		try {
			return getObjectBoolean(rs, index);
		} catch (SQLException e) {
			// if we are here, it wasn't a boolean or null, so try to grab a string instead
			final String bool = rs.getString(index);//.toUpperCase(); // do we want it case-insensitive?
			final boolean ret = YES.equals(bool);
			if (!ret && !NO.equals(bool))
				throw new SQLException(String.format("Implicit conversion of database string to boolean failed on column '%d'. Returned string needs to be '%s' or '%s' and was instead '%s'.", index, YES, NO, bool));
			return ret;
		}
	}

	public static boolean getBooleanYN(final ResultSet rs, final int index) throws SQLException {
		final Boolean ret = getObjectBooleanYN(rs, index);
		if(ret == null)
			throw new SQLException(String.format("Implicit conversion of database string to boolean failed on column '%d'. Returned string needs to be '%s' or '%s' and was instead 'null'. If you want to accept null values, make it an object Boolean instead of primitive boolean.", index, YES, NO));
		return ret;
	}

	public static java.util.Date getUtilDate(final ResultSet _resultSet, final int index) throws SQLException {
		// convert explicity to java.util.Date
		// 12918 |  knex does not return java.sql.Date properly from web service
		java.sql.Timestamp ts = _resultSet.getTimestamp(index);
		if (null == ts)
			return null;
		return new java.util.Date(ts.getTime());
	}

	public static java.util.Date getUtilDate(final ResultSet _resultSet, final int index, final Calendar _cal) throws SQLException {
		if(_cal == null)
			return getUtilDate(_resultSet, index);
		// convert explicity to java.util.Date
		// 12918 |  knex does not return java.sql.Date properly from web service
		java.sql.Timestamp ts = _resultSet.getTimestamp(index, _cal);
		if (null == ts)
			return null;
		return new java.util.Date(ts.getTime());
	}

	public static Calendar getCalendar(final ResultSet _resultSet, final int index) throws SQLException {
		java.sql.Timestamp ts = _resultSet.getTimestamp(index);
		if (null == ts)
			return null;
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(ts.getTime());
		return c;
	}

	public static Calendar getCalendar(final ResultSet _resultSet, final int index, final Calendar _cal) throws SQLException {
		if(_cal == null)
			return getCalendar(_resultSet, index);
		java.sql.Timestamp ts = _resultSet.getTimestamp(index, _cal);
		if (null == ts)
			return null;
		Calendar c = (Calendar) _cal.clone();
		c.setTimeInMillis(ts.getTime());
		return c;
	}

	//IFJAVA8_START

	public static Instant getInstant(final ResultSet _resultSet, final int index) throws SQLException {
		final java.sql.Timestamp ts = _resultSet.getTimestamp(index);
		return ts == null ? null : ts.toInstant();
	}

	public static Instant getInstant(final ResultSet _resultSet, final int index, final Calendar _cal) throws SQLException {
		if(_cal == null)
			return getInstant(_resultSet, index);
		final java.sql.Timestamp ts = _resultSet.getTimestamp(index, _cal);
		return ts == null ? null : ts.toInstant();
	}

	public static LocalDateTime getLocalDateTime(final ResultSet _resultSet, final int index) throws SQLException {
		final java.sql.Timestamp ts = _resultSet.getTimestamp(index);
		return ts == null ? null : ts.toLocalDateTime();
	}

	public static LocalDateTime getLocalDateTime(final ResultSet _resultSet, final int index, final Calendar _cal) throws SQLException {
		if(_cal == null)
			return getLocalDateTime(_resultSet, index);
		final java.sql.Timestamp ts = _resultSet.getTimestamp(index, _cal);
		return ts == null ? null : ts.toLocalDateTime();
	}

	public static LocalDate getLocalDate(final ResultSet _resultSet, final int index) throws SQLException {
		final java.sql.Date ts =  _resultSet.getDate(index);
		return ts == null ? null : ts.toLocalDate();
	}

	public static LocalDate getLocalDate(final ResultSet _resultSet, final int index, final Calendar _cal) throws SQLException {
		if(_cal == null)
			return getLocalDate(_resultSet, index);
		final java.sql.Date ts =  _resultSet.getDate(index, _cal);
		return ts == null ? null : ts.toLocalDate();
	}

	public static LocalTime getLocalTime(final ResultSet _resultSet, final int index) throws SQLException {
		final java.sql.Time ts = _resultSet.getTime(index);
		return ts == null ? null : ts.toLocalTime();
	}

	public static LocalTime getLocalTime(final ResultSet _resultSet, final int index, final Calendar _cal) throws SQLException {
		if(_cal == null)
			return getLocalTime(_resultSet, index);
		final java.sql.Time ts = _resultSet.getTime(index, _cal);
		return ts == null ? null : ts.toLocalTime();
	}

	public static ZonedDateTime getZonedDateTime(final ResultSet _resultSet, final int index) throws SQLException {
		final java.sql.Timestamp ts = _resultSet.getTimestamp(index);
		return ts == null ? null : ZonedDateTime.ofInstant(ts.toInstant(), ZoneId.systemDefault());
	}

	public static ZonedDateTime getZonedDateTime(final ResultSet _resultSet, final int index, final Calendar _cal) throws SQLException {
		if(_cal == null)
			return getZonedDateTime(_resultSet, index);
		final java.sql.Timestamp ts = _resultSet.getTimestamp(index, _cal);
		return ts == null ? null : ZonedDateTime.ofInstant(ts.toInstant(), _cal.getTimeZone().toZoneId());
	}

	public static OffsetDateTime getOffsetDateTime(final ResultSet _resultSet, final int index) throws SQLException {
		final java.sql.Timestamp ts = _resultSet.getTimestamp(index);
		return ts == null ? null : OffsetDateTime.ofInstant(ts.toInstant(), ZoneId.systemDefault());
	}

	public static OffsetDateTime getOffsetDateTime(final ResultSet _resultSet, final int index, final Calendar _cal) throws SQLException {
		if(_cal == null)
			return getOffsetDateTime(_resultSet, index);
		final java.sql.Timestamp ts = _resultSet.getTimestamp(index, _cal);
		return ts == null ? null : OffsetDateTime.ofInstant(ts.toInstant(), _cal.getTimeZone().toZoneId());
	}

	public static OffsetTime getOffsetTime(final ResultSet _resultSet, final int index) throws SQLException {
		final java.sql.Timestamp ts = _resultSet.getTimestamp(index);
		return ts == null ? null : OffsetTime.ofInstant(ts.toInstant(), ZoneId.systemDefault());
	}

	public static OffsetTime getOffsetTime(final ResultSet _resultSet, final int index, final Calendar _cal) throws SQLException {
		if(_cal == null)
			return getOffsetTime(_resultSet, index);
		final java.sql.Timestamp ts = _resultSet.getTimestamp(index, _cal);
		return ts == null ? null : OffsetTime.ofInstant(ts.toInstant(), _cal.getTimeZone().toZoneId());
	}

	//IFJAVA8_END

}
