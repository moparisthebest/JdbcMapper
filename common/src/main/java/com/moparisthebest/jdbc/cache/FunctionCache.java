package com.moparisthebest.jdbc.cache;

import com.moparisthebest.jdbc.Factory;

import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.moparisthebest.jdbc.TryClose.tryClose;

/**
 * This implements a simple cache, meant to be accessed concurrently from multiple threads, that is refreshed on a user
 * defined interval.
 *
 * Most often instances of this class will be `static final` so the entire application can benefit from the cache
 *
 * This adds a method on top of Cache that allows the caller to send in a parameter for the refreshing function to use,
 * this will most often be used to send in an already-open java.sql.Connection or JdbcMapper instance
 *
 * @param <T> type of cached item
 */
public class FunctionCache<T, F> extends Cache<T> implements FunctionSupplier<T, F> {

    public static <T, F> FunctionSupplier<T, F> ofNotCloseable(final Duration refreshInterval, final Function<F, T> function, final Supplier<F> factory) {
        return of(refreshInterval, function, () -> function.apply(factory.get()));
    }

    public static <T, F extends AutoCloseable> FunctionSupplier<T, F> ofSupplier(final Duration refreshInterval, final Function<F, T> function, final Supplier<F> factory) {
        return ofFactory(refreshInterval, function, factory::get);
    }

    public static <T, F extends AutoCloseable> FunctionSupplier<T, F> ofFactory(final Duration refreshInterval, final Function<F, T> function, final Factory<F> factory) {
        return of(refreshInterval, function, () -> {
            F f = null;
            try {
                try {
                    f = factory.create();
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                return function.apply(f);
            } finally {
                tryClose(f);
            }
        });
    }

    public static <T, F> FunctionSupplier<T, F> of(final Duration refreshInterval, final Function<F, T> function, final Supplier<T> supplier) {
        return new FunctionCache<>(refreshInterval, function, supplier);
    }

    public static <T, F> Function<F, T> of(final Duration refreshInterval, final Function<F, T> function) {
        return of(refreshInterval, function, null);
    }

    protected final Function<F, T> function;

    protected FunctionCache(final Duration refreshInterval, final Function<F, T> function, final Supplier<T> supplier) {
        super(refreshInterval, supplier);
        this.function = function;
    }

    @Override
    public T get(final F f) {
        if (shouldRefresh()) {
            // we want to refresh
            synchronized (this) {
                if (shouldRefresh()) {
                    // maybe another thread beat us in here and already refreshed
                    this.item = function.apply(f);
                    markRefreshed();
                }
            }
        }
        return item;
    }
}
