package com.penopllast.loader;

public interface ILoader {
    Promise<Result<String>> load(String url);

    void cancel();
}
