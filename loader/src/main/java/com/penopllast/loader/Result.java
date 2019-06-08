package com.penopllast.loader;

import android.support.annotation.NonNull;

final class Result<T> {
    private final Object value;
    private boolean isSuccess;

    public Result(Object o) {
        this.value = o;
    }

    public boolean isSuccess() {
        return !(value instanceof Failure);
    }

    public static <T> Result<T> success(T value) {
        return new Result<T>(value);
    }

    public static <T> Result<T> failure(Throwable ex) {
        return new Result<T>(createFailure(ex));
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

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
