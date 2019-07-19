package com.moparisthebest.jdbc;

import com.moparisthebest.jdbc.codegen.JdbcMapperFactory;

import java.io.Closeable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.ResultSet;

import static com.moparisthebest.jdbc.TryClose.tryClose;

/**
 * This provides implementations of Closeable/JdbcMapper interfaces that simply construct a new object,
 * execute the method, and close the object for each method call.  Implementations returned never need
 * to be closed and are as thread-safe as the Factory sent in, of which the provided impls are entirely thread-safe.
 * <p>
 * If any of the methods returns an *interface* that extends Closeable, we assume *this* Closeable should not be closed
 * until the returned value is, and it is therefore wrapped with WrappingCloseable before return
 */
public class SingletonCloseable implements InvocationHandler {

    //IFJAVA8_START

    public static <T extends AutoCloseable> T of(final Class<T> jdbcMapper) {
        return of(jdbcMapper, JdbcMapperFactory.of(jdbcMapper));
    }

    public static <T extends AutoCloseable> T of(final Class<T> jdbcMapper, final String jndiName) {
        return of(jdbcMapper, JdbcMapperFactory.of(jdbcMapper, jndiName));
    }

    @SuppressWarnings("unchecked")
    public static <T extends AutoCloseable> T of(final Class<T> jdbcMapper, final Factory<? extends T> factory) {
        return (T) Proxy.newProxyInstance(jdbcMapper.getClassLoader(),
                new Class<?>[]{jdbcMapper}, new SingletonCloseable(factory)
        );
    }

    protected final Factory<? extends AutoCloseable> factory;

    protected SingletonCloseable(final Factory<? extends AutoCloseable> factory) {
        this.factory = factory;
    }

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        AutoCloseable jdbcMapper = null;
        try {
            jdbcMapper = factory.create();
            Object ret = method.invoke(jdbcMapper, args);
            if (ret instanceof AutoCloseable) {
                // wrap jdbcMapper into ret so it gets closed when ret does, can only do this for Interfaces like ResultSet not concrete classes
                final Class<?> returnType = method.getReturnType();
                if (returnType.isInterface() && AutoCloseable.class.isAssignableFrom(returnType)) {
                    @SuppressWarnings("unchecked")
                    final Class<AutoCloseable> returnTypeCloseable = (Class<AutoCloseable>) returnType;
                    ret = WrappingCloseable.wrap((AutoCloseable) ret, returnTypeCloseable, jdbcMapper);
                    // now that jdbcMapper will be closed by above, don't let it be closed here
                    // if a ClassCastException might happen here we will leak jdbcMapper, but how could it???
                    jdbcMapper = null;
                }
            }
            return ret;
        } finally {
            tryClose(jdbcMapper);
        }
    }

    //IFJAVA8_END

    /*IFJAVA6_START

    public static <T extends Closeable> T of(final Class<T> jdbcMapper) {
        return of(jdbcMapper, JdbcMapperFactory.of(jdbcMapper));
    }

    public static <T extends Closeable> T of(final Class<T> jdbcMapper, final String jndiName) {
        return of(jdbcMapper, JdbcMapperFactory.of(jdbcMapper, jndiName));
    }

    @SuppressWarnings("unchecked")
    public static <T extends Closeable> T of(final Class<T> jdbcMapper, final Factory<? extends T> factory) {
        return (T) Proxy.newProxyInstance(jdbcMapper.getClassLoader(),
                new Class<?>[]{jdbcMapper}, new SingletonCloseable(factory)
        );
    }

    protected final Factory<? extends Closeable> factory;

    protected SingletonCloseable(final Factory<? extends Closeable> factory) {
        this.factory = factory;
    }

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        Closeable jdbcMapper = null;
        try {
            jdbcMapper = factory.create();
            Object ret = method.invoke(jdbcMapper, args);
            if (ret instanceof Closeable) {
                // wrap jdbcMapper into ret so it gets closed when ret does, can only do this for Interfaces like ResultSet not concrete classes
                final Class<?> returnType = method.getReturnType();
                if (returnType.isInterface() && Closeable.class.isAssignableFrom(returnType)) {
                    @SuppressWarnings("unchecked") final Class<Closeable> returnTypeCloseable = (Class<Closeable>) returnType;
                    ret = WrappingCloseable.wrap((Closeable) ret, returnTypeCloseable, jdbcMapper);
                    // now that jdbcMapper will be closed by above, don't let it be closed here
                    // if a ClassCastException might happen here we will leak jdbcMapper, but how could it???
                    jdbcMapper = null;
                }
            } else if (ret instanceof ResultSet) {
                // wrap jdbcMapper into ret so it gets closed when ret does, can only do this for Interfaces like ResultSet not concrete classes
                final Class<?> returnType = method.getReturnType();
                if (returnType.isInterface() && ResultSet.class.isAssignableFrom(returnType)) {
                    @SuppressWarnings("unchecked") final Class<ResultSet> returnTypeCloseable = (Class<ResultSet>) returnType;
                    ret = WrappingCloseable.wrap((ResultSet) ret, returnTypeCloseable, jdbcMapper);
                    // now that jdbcMapper will be closed by above, don't let it be closed here
                    // if a ClassCastException might happen here we will leak jdbcMapper, but how could it???
                    jdbcMapper = null;
                }
            }
            return ret;
        } finally {
            tryClose(jdbcMapper);
        }
    }

    IFJAVA6_END*/

}
