package com.moparisthebest.jdbc.cache;

import java.util.function.Function;
import java.util.function.Supplier;

public interface FunctionSupplier<T, F> extends Supplier<T>, Function<F, T> {

    T get(F f);

    @Override
    default T apply(final F f) {
        return get(f);
    }
}
