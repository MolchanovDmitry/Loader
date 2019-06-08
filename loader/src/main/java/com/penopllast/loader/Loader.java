package com.penopllast.loader;

import java.util.concurrent.ExecutionException;

public class Loader implements ILoader {

    private final Async async = new Async();

    @Override
    public void load(String url) {
        Promise<String> promise = async.submit(() -> {
            String text = "test";
            long n = 1500;
            System.out.println("sleeping " + n);
            Thread.sleep(n);
            return text;
        });
        Promise<Integer> promise2 = promise.bind(string -> Promise.pure(string.length()));
        try {
            System.out.println("length = " + promise2.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
