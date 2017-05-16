package com.moparisthebest.jdbc.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;

/**
 * Created by mopar on 5/16/17.
 */
public class ResultSetUtil {

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

	public static Timestamp getTimestamp(final ResultSet _resultSet, final Calendar _cal, final int index) throws SQLException {
		if (null == _cal)
			return _resultSet.getTimestamp(index);
		else
			return _resultSet.getTimestamp(index, _cal);
	}

	public static Time getTime(final ResultSet _resultSet, final Calendar _cal, final int index) throws SQLException {
		if (null == _cal)
			return _resultSet.getTime(index);
		else
			return _resultSet.getTime(index, _cal);
	}

	public static java.sql.Date getSqlDate(final ResultSet _resultSet, final Calendar _cal, final int index) throws SQLException {
		if (null == _cal)
			return _resultSet.getDate(index);
		else
			return _resultSet.getDate(index, _cal);
	}

	public static java.util.Date getUtilDate(final ResultSet _resultSet, final Calendar _cal, final int index) throws SQLException {
		// convert explicity to java.util.Date
		// 12918 |  knex does not return java.sql.Date properly from web service
		java.sql.Timestamp ts = (null == _cal) ? _resultSet.getTimestamp(index) : _resultSet.getTimestamp(index, _cal);
		if (null == ts)
			return null;
		return new java.util.Date(ts.getTime());
	}

	public static Calendar getCalendar(final ResultSet _resultSet, final Calendar _cal, final int index) throws SQLException {
		java.sql.Timestamp ts = (null == _cal) ? _resultSet.getTimestamp(index) : _resultSet.getTimestamp(index, _cal);
		if (null == ts)
			return null;
		Calendar c = (null == _cal) ? Calendar.getInstance() : (Calendar) _cal.clone();
		c.setTimeInMillis(ts.getTime());
		return c;
	}

}
