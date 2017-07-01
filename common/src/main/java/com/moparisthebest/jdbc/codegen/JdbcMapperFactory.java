package com.moparisthebest.jdbc.codegen;

import com.moparisthebest.jdbc.Factory;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * Created by mopar on 5/24/17.
 */
public class JdbcMapperFactory<T> implements Factory<T> {

	static final String SUFFIX = "Bean";

	@SuppressWarnings("unchecked")
	public static <T> Class<? extends T> getImplementationClass(final Class<T> jdbcMapper) throws ClassNotFoundException {
		return (Class<? extends T>) Class.forName(jdbcMapper.getName() + SUFFIX);
	}

	public static <T> Constructor<? extends T> getConnectionConstructor(final Class<T> jdbcMapper) throws ClassNotFoundException, NoSuchMethodException {
		return getImplementationClass(jdbcMapper).getConstructor(Connection.class);
	}

	public static <T> Constructor<? extends T> getDefaultConstructor(final Class<T> jdbcMapper) throws ClassNotFoundException, NoSuchMethodException {
		return getImplementationClass(jdbcMapper).getConstructor();
	}

	public static <T> Constructor<? extends T> getFactoryConstructor(final Class<T> jdbcMapper) throws ClassNotFoundException, NoSuchMethodException {
		return getImplementationClass(jdbcMapper).getConstructor(Factory.class);
	}

	public static <T> T create(final Class<T> jdbcMapper, final Connection connection) {
		try {
			return getConnectionConstructor(jdbcMapper).newInstance(connection);
		} catch (Throwable e) {
			throw new RuntimeException("could not create JdbcMapper, did the processor run at compile time?", e);
		}
	}

	public static <T> T create(final Class<T> jdbcMapper) {
		try {
			return getDefaultConstructor(jdbcMapper).newInstance();
		} catch (Throwable e) {
			throw new RuntimeException("could not create JdbcMapper, did the processor run at compile time?", e);
		}
	}

	private final Constructor<? extends T> constructor;
	private final Object[] args;

	public JdbcMapperFactory(final Class<T> jdbcMapper) {
		try {
			this.constructor = getDefaultConstructor(jdbcMapper);
			this.args = null;
		} catch (Throwable e) {
			throw new RuntimeException("could not create JdbcMapper, did the processor run at compile time?", e);
		}
	}

	public JdbcMapperFactory(final Constructor<? extends T> constructor, final Object... args) {
		if(constructor == null)
			throw new NullPointerException("constructor must be non-null");
		this.constructor = constructor;
		this.args = args;
	}

	public JdbcMapperFactory(final Class<T> queryMapperClass, final Factory<Connection> connectionFactory) {
		if(queryMapperClass == null)
			throw new NullPointerException("queryMapperClass must be non-null");
		if(connectionFactory == null)
			throw new NullPointerException("connectionFactory must be non-null");
		try {
			this.constructor = queryMapperClass.getConstructor(Factory.class);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException("queryMapperClass must have a constructor that takes Factory<Connection>", e);
		}
		this.args = new Object[]{connectionFactory};
	}

	public JdbcMapperFactory(final Class<T> queryMapperClass, final String jndiName) {
		if(queryMapperClass == null)
			throw new NullPointerException("queryMapperClass must be non-null");
		if(jndiName == null)
			throw new NullPointerException("jndiName must be non-null");
		try {
			this.constructor = queryMapperClass.getConstructor(String.class);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException("queryMapperClass must have a constructor that takes String", e);
		}
		this.args = new Object[]{jndiName};
	}

	@Override
	public T create() throws SQLException {
		try {
			return this.constructor.newInstance(args);
		} catch (InstantiationException e) {
			throw new RuntimeException("could not create JdbcMapper, did the processor run at compile time?", e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException("could not create JdbcMapper, did the processor run at compile time?", e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException("could not create JdbcMapper, did the processor run at compile time?", e);
		}
	}
}
