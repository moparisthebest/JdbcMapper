package com.moparisthebest.jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

public interface Finishable {
	public void finish(ResultSet rs) throws SQLException;
}
