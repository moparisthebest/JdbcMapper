package com.moparisthebest.jdbc;

import com.moparisthebest.classgen.Compiler;

import java.sql.ResultSet;
import java.util.Calendar;

/**
 * This generally follows the contract of ResultSetMapper, with the differences specified in CompilingRowToObjectMapper.
 * <p>
 * This does cache compiled code based on column name/order and DTO being mapped to, so the (rather heavy)
 * code generation/compilation/instantiation only happens once for each query/dto, and is very fast on subsequent calls.
 * <p>
 * By default this uses a plain HashMap for the cache, unbounded, and not thread-safe. Use CacheUtil to get Maps for your
 * preferred use case. You cansend in your own custom Map implementation, CompilingResultSetMapper guarantees null will
 * never be used for key or value.
 *
 * @see CompilingRowToObjectMapper
 */
public class CompilingResultSetMapper extends ResultSetMapper {

	protected final Compiler compiler = new Compiler();
	protected final CompilingRowToObjectMapper.Cache cache;

	public CompilingResultSetMapper(final Calendar cal, final int arrayMaxLength, final CompilingRowToObjectMapper.Cache cache) {
		super(cal, arrayMaxLength);
		this.cache = cache == null ? new CompilingRowToObjectMapper.Cache() : cache;
	}

	public CompilingResultSetMapper(final CompilingRowToObjectMapper.Cache cache) {
		this.cache = cache == null ? new CompilingRowToObjectMapper.Cache() : cache;
	}

	public CompilingResultSetMapper(final int arrayMaxLength, final CompilingRowToObjectMapper.Cache cache) {
		super(arrayMaxLength);
		this.cache = cache == null ? new CompilingRowToObjectMapper.Cache() : cache;
	}

	public CompilingResultSetMapper() {
		this.cache = new CompilingRowToObjectMapper.Cache();
	}

	@Override
	public <K, T> RowMapper<K, T> getRowMapper(ResultSet resultSet, Class<T> returnTypeClass, Calendar cal, Class<?> mapValType, Class<K> mapKeyType) {
		return new CompilingRowToObjectMapper<K, T>(compiler, cache, resultSet, returnTypeClass, cal, mapValType, mapKeyType);
	}
}
