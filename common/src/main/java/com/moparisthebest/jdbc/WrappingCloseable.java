package com.moparisthebest.jdbc;

import java.io.Closeable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.ResultSet;

import static com.moparisthebest.jdbc.TryClose.tryClose;

/**
 * This wraps a class implementing closeable and also closes a supplied closeable after this class is closed
 * <p>
 * An example use is for holding a Connection open while a returned ResultSet is still held open, and closing the
 * Connection when the ResultSet is closed
 */
public class WrappingCloseable implements InvocationHandler {

    //IFJAVA8_START

    @SuppressWarnings("unchecked")
    public static <T extends AutoCloseable, C extends AutoCloseable> T wrap(final T delegate, final Class<T> iface, final C closeable) {
        return (T) Proxy.newProxyInstance(iface.getClassLoader(),
                new Class<?>[]{iface}, new WrappingCloseable(delegate, closeable)
        );
    }

    protected final Object delegate;
    protected final AutoCloseable closeable;

    protected WrappingCloseable(final Object delegate, final AutoCloseable closeable) {
        this.delegate = delegate;
        this.closeable = closeable;
    }

    //IFJAVA8_END

    /*IFJAVA6_START

    @SuppressWarnings("unchecked")
    public static <T extends Closeable, C extends Closeable> T wrap(final T delegate, final Class<T> iface, final C closeable) {
        return (T) Proxy.newProxyInstance(iface.getClassLoader(),
                new Class<?>[]{iface}, new WrappingCloseable(delegate, closeable)
        );
    }

    @SuppressWarnings("unchecked")
    public static <T extends ResultSet, C extends Closeable> T wrap(final T delegate, final Class<T> iface, final C closeable) {
        return (T) Proxy.newProxyInstance(iface.getClassLoader(),
                new Class<?>[]{iface}, new WrappingCloseable(delegate, closeable)
        );
    }

    protected final Object delegate;
    protected final Closeable closeable;

    protected WrappingCloseable(final Object delegate, final Closeable closeable) {
        this.delegate = delegate;
        this.closeable = closeable;
    }

    IFJAVA6_END*/

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        try {
            return method.invoke(delegate, args);
        } finally {
            // todo: is there a better way of determining close method?
            if ((args == null || args.length == 0) && "close".equals(method.getName())) {
                tryClose(closeable);
            }
        }
    }

}
