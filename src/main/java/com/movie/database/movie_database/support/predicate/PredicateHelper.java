package com.movie.database.movie_database.support.predicate;

import java.util.function.Predicate;
import java.util.function.Supplier;

public class PredicateHelper<T> {

    private final Predicate<T> predicate;

    private PredicateHelper(Predicate<T> predicate) {
        this.predicate = predicate;
    }

    public static <T> PredicateHelper<T> of(Predicate<T> predicate) {
        return new PredicateHelper<>(predicate);
    }

    public PredicateHelper<T> doIfTrue(T object, Runnable runnable) {
        if (predicate.test(object)) {
            runnable.run();
        }
        return this;
    }

    public PredicateHelper<T> doIfFalse(T object, Runnable runnable) {
        if (predicate.negate().test(object)) {
            runnable.run();
        }
        return this;
    }

    public PredicateHelper<T> doIfTrueOrElse(T object, Runnable trueRunnable, Runnable falseRunnable) {
        if (predicate.test(object)) {
            trueRunnable.run();
        } else {
            falseRunnable.run();
        }
        return this;
    }

    public PredicateHelper<T> throwIfTrue(T object, Supplier<? extends RuntimeException> exceptionSupplier) {
        if (predicate.test(object)) {
            throw exceptionSupplier.get();
        }
        return this;
    }

    public PredicateHelper<T> throwIfFalse(T object, Supplier<? extends RuntimeException> exceptionSupplier) {
        if (predicate.negate().test(object)) {
            throw exceptionSupplier.get();
        }
        return this;
    }
}
