package com.penopllast.loader;

import android.support.annotation.Nullable;

final class Result<T> {
    private final @Nullable Object value;
    private @Nullable Throwable throwable;
    private boolean isSuccess;

    private Result(@Nullable Object o) {
        this.value = o;
    }

    private Result(@Nullable Object o, boolean isSuccess) {
        this.isSuccess = isSuccess;
        this.value = o;
    }

    private Result(@Nullable Object o, Throwable throwable) {
        this.throwable = throwable;
        this.value = o;
    }

    boolean isSuccess() {
        return isSuccess;
    }

    static <T> Result<T> success(T value) {
        return new Result<T>(value, true);
    }

    static <T> Result<T> failure(Throwable ex) {
        return new Result<T>(createFailure(ex), ex);
    }

    private static Object createFailure(Throwable ex) {
        return new Failure(ex);
    }

    private static class Failure {
        private final Throwable exception;

        Failure(Throwable t) {
            this.exception = t;
        }
    }

    @Nullable
    public T getValue() throws ClassCastException {
        return isSuccess ? (T) value : null;
    }

    public String getErrorMessage() {
        return throwable != null ? throwable.getMessage() : "";
    }
}
