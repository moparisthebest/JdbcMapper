package com.moparisthebest.jdbc;

import java.sql.SQLException;

/**
 * Created by mopar on 7/1/17.
 */
public interface Factory<T> {
	T create() throws SQLException;
}
