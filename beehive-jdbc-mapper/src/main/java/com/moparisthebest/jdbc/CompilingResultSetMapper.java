package com.moparisthebest.jdbc;

import com.moparisthebest.classgen.Compiler;

import java.sql.ResultSet;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * This generally follows the contract of ResultSetMapper, with the differences specified in CompilingRowToObjectMapper.
 * <p>
 * This does cache compiled code based on column name/order and DTO being mapped to, so the (rather heavy)
 * code generation/compilation/instantiation only happens once for each query/dto, and is very fast on subsequent calls.
 *
 * @see CompilingRowToObjectMapper
 */
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
