package com.penopllast.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.function.Function;


final class Promise<V> {

    public interface Action<T> {
        void invoke(T result);
    }

    private final CountDownLatch taskLock = new CountDownLatch(1);
    private List<Action<Promise<V>>> callbacks = new ArrayList<>();

    private V result = null;
    private Throwable exception = null;

    private boolean invoked = false;

    V get() throws InterruptedException, ExecutionException {
        taskLock.await();
        if (exception != null) {
            throw new ExecutionException(exception);
        }
        return result;
    }

    void invoke(V result) {
        invokeWithResultOrException(result, null);
    }

    void invokeWithException(Throwable t) {
        invokeWithResultOrException(null, t);
    }

    private void invokeWithResultOrException(V result, Throwable t) {
        synchronized (this) {
            if (!invoked) {
                invoked = true;
                this.result = result;
                this.exception = t;
                taskLock.countDown();
            } else {
                return;
            }
        }
        for (Action<Promise<V>> callback : callbacks) {
            callback.invoke(this);
        }
    }

    private void onRedeem(Action<Promise<V>> callback) {
        synchronized (this) {
            if (!invoked) {
                callbacks.add(callback);
            }
        }
        if (invoked) {
            callback.invoke(this);
        }
    }

    static <V> Promise<V> pure(final V v) {
        Promise<V> p = new Promise<>();
        p.invoke(v);
        return p;
    }

    <R> Promise<R> bind(final Function<V, Promise<R>> function) {
        Promise<R> result = new Promise<>();

        this.onRedeem(callback -> {
            try {
                V v = callback.get();
                Promise<R> applicationResult = function.apply(v);
                applicationResult.onRedeem(applicationCallback -> {
                    try {
                        R r = applicationCallback.get();
                        result.invoke(r);
                    } catch (Throwable e) {
                        result.invokeWithException(e);
                    }
                });
            } catch (Throwable e) {
                result.invokeWithException(e);
            }
        });

        return result;
    }
}
