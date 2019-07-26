package com.moparisthebest.jdbc.cache;

import java.time.Duration;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * This implements a simple cache, meant to be accessed concurrently from multiple threads, that is refreshed on a user
 * defined interval.
 *
 * Most often instances of this class will be `static final` so the entire application can benefit from the cache
 *
 * @param <T> type of cached item
 */
public class Cache<T> implements Supplier<T> {

    public static <T> Supplier<T> of(final Duration refreshInterval, final Supplier<T> supplier) {
        return new Cache<>(refreshInterval, supplier);
    }

    private final long refreshInterval;
    private long nextRefresh;

    protected final Supplier<T> supplier;

    protected T item;

    protected Cache(final Duration refreshInterval, final Supplier<T> supplier) {
        Objects.requireNonNull(refreshInterval);
        // we are explicitly allowing supplier to be null, in which case get() will fail
        this.refreshInterval = refreshInterval.toMillis();
        this.supplier = supplier;
        // so refresh will be called immediately
        this.nextRefresh = Long.MIN_VALUE;
    }

    protected boolean shouldRefresh() {
        return System.currentTimeMillis() > nextRefresh;
    }

    protected void markRefreshed() {
        this.nextRefresh = System.currentTimeMillis() + refreshInterval;
    }

    @Override
    public T get() {
        if (shouldRefresh()) {
            // we want to refresh
            synchronized (this) {
                if (shouldRefresh()) {
                    // maybe another thread beat us in here and already refreshed
                    this.item = supplier.get();
                    markRefreshed();
                }
            }
        }
        return item;
    }
}
