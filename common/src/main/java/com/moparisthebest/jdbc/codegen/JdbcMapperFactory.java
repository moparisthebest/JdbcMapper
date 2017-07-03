package com.moparisthebest.jdbc.codegen;

import com.moparisthebest.jdbc.Factory;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.sql.Connection;
import java.sql.SQLException;

import static com.moparisthebest.jdbc.TryClose.tryClose;

/**
 * Created by mopar on 5/24/17.
 */
public class JdbcMapperFactory<T> implements Factory<T> {

	static final String SUFFIX = "Bean";

	static {
		try{
			final Class<?> ensureContext = Class.forName(System.getProperty("QueryMapper.ensureContext.class", System.getProperty("JdbcMapper.ensureContext.class", "com.gcl.containerless.EnsureContext")));
			final Method method = ensureContext.getMethod(System.getProperty("QueryMapper.ensureContext.method", System.getProperty("JdbcMapper.ensureContext.method", "setup")));
			method.invoke(null);
		}catch(Throwable e){
			// ignore
			//e.printStackTrace();
		}
	}

	@SuppressWarnings("unchecked")
	public static <T> Class<? extends T> getImplementationClass(final Class<T> jdbcMapper) throws ClassNotFoundException {
		if(jdbcMapper.isInterface() || Modifier.isAbstract(jdbcMapper.getModifiers()))
			return (Class<? extends T>) Class.forName(jdbcMapper.getName() + SUFFIX);
		else
			return jdbcMapper;
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

	public static <T> T create(final Class<T> jdbcMapper, final Factory<Connection> connectionFactory) {
		try {
			return getFactoryConstructor(jdbcMapper).newInstance(connectionFactory);
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

	public static Factory<Connection> connectionFactory(final DataSource dataSource) {
		if (dataSource == null)
			throw new NullPointerException("dataSource must be non-null");
		return
				//IFJAVA8_START
				dataSource::getConnection
				//IFJAVA8_END
				/*IFJAVA6_START
				new Factory<Connection>() {
					@Override
					public Connection create() throws SQLException {
						return dataSource.getConnection();
					}
				}
				IFJAVA6_END*/
				;
	}

	public static Factory<Connection> connectionFactorySingleLookup(final String jndiName) {
		if (jndiName == null)
			throw new NullPointerException("jndiName must be non-null");
		Context context = null;
		try {
			context = new InitialContext();
			return connectionFactory((DataSource) context.lookup(jndiName));
		} catch (NamingException e) {
			throw new RuntimeException("JNDI lookup failed to create connection", e);
		} finally {
			tryClose(context);
		}
	}

	public static Factory<Connection> connectionFactory(final String jndiName) {
		if (jndiName == null)
			throw new NullPointerException("jndiName must be non-null");
		return
		/*IFJAVA6_START
			new Factory<Connection>() {
				@Override
				public Connection create() throws SQLException
		IFJAVA6_END*/
		//IFJAVA8_START
			() ->
		//IFJAVA8_END
				{
					Context context = null;
					try {
						context = new InitialContext();
						final DataSource ds = (DataSource) context.lookup(jndiName);
						return ds.getConnection();
					} catch (NamingException e) {
						throw new RuntimeException("JNDI lookup failed to create connection", e);
					} finally {
						tryClose(context);
					}
				}
		/*IFJAVA6_START
			}
		IFJAVA6_END*/
		;
	}

	public static <T> Factory<T> of(final Class<T> jdbcMapper) {
		return new JdbcMapperFactory<T>(jdbcMapper);
	}

	public static <T> Factory<T> of(final Class<T> jdbcMapper, final Factory<Connection> connectionFactory) {
		return new JdbcMapperFactory<T>(jdbcMapper, connectionFactory);
	}

	public static <T> Factory<T> of(final Class<T> jdbcMapper, final String jndiName) {
		return of(jdbcMapper, connectionFactory(jndiName));
	}

	private final Constructor<? extends T> constructor;
	private final Object[] args;

	private JdbcMapperFactory(final Class<T> jdbcMapper) {
		try {
			this.constructor = getDefaultConstructor(jdbcMapper);
			this.args = null;
		} catch (Throwable e) {
			throw new RuntimeException("could not create JdbcMapper, did the processor run at compile time?", e);
		}
	}

	private JdbcMapperFactory(final Class<T> jdbcMapper, final Factory<Connection> connectionFactory) {
		if (jdbcMapper == null)
			throw new NullPointerException("jdbcMapper must be non-null");
		if (connectionFactory == null)
			throw new NullPointerException("connectionFactory must be non-null");
		try {
			this.constructor = getFactoryConstructor(jdbcMapper);
		} catch (Throwable e) {
			throw new RuntimeException("jdbcMapper must have a constructor that takes Factory<Connection>", e);
		}
		this.args = new Object[]{connectionFactory};
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
