package com.penopllast.loader;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


final class Async<V> {

    private final ExecutorService executor;

    Async() {
        this(1);
    }

    Async(int threadCount) {
        executor = Executors.newFixedThreadPool(threadCount);
    }

    void shutdown() {
        executor.shutdownNow();
    }

    Promise<V> submit(final Callable<V> callable) {
        Promise<V> promise = new Promise<>();
        Callable<V> smarterCallable = () -> {
            try {
                V result = callable.call();
                promise.invoke(result);
                return result;
            } catch (Throwable e) {
                promise.invokeWithException(e);
                return null;
            }
        };

        executor.submit(smarterCallable);
        return promise;
    }
}
