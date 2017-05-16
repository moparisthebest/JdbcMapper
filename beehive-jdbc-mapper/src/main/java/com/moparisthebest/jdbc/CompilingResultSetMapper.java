package com.moparisthebest.jdbc;

import com.moparisthebest.classgen.Compiler;

import java.sql.ResultSet;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CompilingResultSetMapper extends ResultSetMapper {

	protected final Compiler compiler = new Compiler();
	protected final Map<CachingRowToObjectMapper.ResultSetKey, CompilingRowToObjectMapper.ResultSetToObject<?>> cache = new HashMap<CachingRowToObjectMapper.ResultSetKey, CompilingRowToObjectMapper.ResultSetToObject<?>>();

	public CompilingResultSetMapper(Calendar cal, int arrayMaxLength) {
		super(cal, arrayMaxLength);
	}

	public CompilingResultSetMapper() {
		super();
	}

	@Override
	protected <T> RowToObjectMapper<T> getRowMapper(ResultSet resultSet, Class<T> returnTypeClass, Calendar cal, Class<?> mapValType) {
		return new CompilingRowToObjectMapper<T>(compiler, cache, resultSet, returnTypeClass, cal, mapValType);
	}
}
