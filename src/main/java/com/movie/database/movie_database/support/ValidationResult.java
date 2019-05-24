package com.movie.database.movie_database.support;

import com.movie.database.movie_database.support.exceptions.ErrorInfo;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class ValidationResult<T> {

    private final boolean isValid;
    private final T validatedObject;
    private final List<ErrorInfo> validationErrors;

    public ValidationResult(boolean isValid, T validatedObject, List<ErrorInfo> validationErrors) {
        this.isValid = isValid;
        this.validatedObject = validatedObject;
        this.validationErrors = validationErrors;
    }

    public static <T> ValidationResult<T> from(T object, Predicate<T> predicate) {
        if (predicate.test(object)) {
            return valid(object);
        }
        return invalid(object);
    }

    public ValidationResult<T> and(Function<T, Boolean> otherValidation) {
        if (isValid && otherValidation.apply(validatedObject)) {
            return this;
        }
        return invalid(validatedObject, validationErrors);
    }

    public ValidationResult<T> or(ValidationResult<T> otherResult) {
        if (isValid) {
            return this;
        }
        if (otherResult.isValid) {
            return otherResult;
        }
        return invalid(validatedObject, validationErrors);
    }

    public boolean isValid() {
        return isValid;
    }

    public T throwIfInvalid(Supplier<? extends RuntimeException> exceptionSupplier) {
        if (isValid) {
            return validatedObject;
        }

        throw exceptionSupplier.get();
    }

    private static <T> ValidationResult<T> valid(T validatedObject) {
        return new ValidationResult<>(true, validatedObject, Collections.emptyList());
    }

    private static <T> ValidationResult<T> invalid(T validatedObject) {
        return new ValidationResult<>(false, validatedObject, Collections.emptyList());
    }

    private static <T> ValidationResult<T> invalid(T validatedObject, List<ErrorInfo> validationErrors) {
        return new ValidationResult<>(false, validatedObject, validationErrors);
    }
}
