package com.moparisthebest.jdbc.util;

import java.util.Iterator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

public interface AutoCloseableUtil {

    static <E extends AutoCloseable, T, R> R apply(final Supplier<E> supplier, final Function<? super E, R> action) {
        try (final E stream = supplier.get()) {
            return action.apply(stream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static <E extends AutoCloseable, T> void accept(final Supplier<E> supplier, final Consumer<? super E> action) {
        try (final E stream = supplier.get()) {
            action.accept(stream);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    static <E extends Stream<T>, T> void forEach(final Supplier<E> supplier, final Consumer<? super T> action) {
        accept(supplier, s -> s.forEach(action));
    }

    static <E extends AutoCloseable & Iterable<T>, T> void forEachIt(final Supplier<E> supplier, final Consumer<? super T> action) {
        accept(supplier, s -> {
            for (final T val : s) {
                action.accept(val);
            }
        });
    }

    static <T> void forEachRemove(final Iterator<T> iterator, final Function<? super T, Boolean> action) {
        while (iterator.hasNext()) {
            if (action.apply(iterator.next())) {
                iterator.remove();
            }
        }
    }

}
