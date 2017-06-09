package com.moparisthebest.jdbc.util;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;

/**
 * Created by mopar on 6/9/17.
 */
public interface ResultSetToObject<T> {
	T toObject(final ResultSet rs, final Calendar cal) throws SQLException;
}
