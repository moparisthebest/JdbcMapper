package com.moparisthebest.jdbc.codegen;

import java.sql.Connection;

/**
 * Created by mopar on 5/24/17.
 */
public abstract class JdbcMapperFactory {

	static final String SUFFIX = "Bean";

	@SuppressWarnings("unchecked")
	public static <T> T create(final Class<T> jdbcMapper, final Connection connection) {
		try {
			return (T) Class.forName(jdbcMapper.getName() + SUFFIX).getConstructor(Connection.class).newInstance(connection);
		} catch (Throwable e) {
			throw new RuntimeException("could not create JdbcMapper, did the processor run at compile time?", e);
		}
	}

	public static <T> T create(final Class<T> jdbcMapper) {
		return create(jdbcMapper, null);
	}
}
