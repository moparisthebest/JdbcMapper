package com.moparisthebest.jdbc.cache;

import java.util.function.Function;
import java.util.function.Supplier;

@FunctionalInterface
public interface FunctionSupplier<T, F> extends Supplier<T>, Function<F, T> {

    default T get(final F f) {
        return get();
    }

    @Override
    default T apply(final F f) {
        return get(f);
    }
}
