package com.penopllast.loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.concurrent.Callable;

import javax.net.ssl.HttpsURLConnection;

public class Loader implements ILoader {

    private final Async async = new Async();

    @Override
    public Promise<Result<String>> load(String url) {
        return async.submit(new LoadCallable(url));
    }

    @Override
    public void cancel() {
        async.shutdown();
    }

    private static final class LoadCallable implements Callable<Result<String>> {
        private static final String GET = "GET";
        private static final int TIMEOUT = 10000;
        private final String url;

        LoadCallable(String url) {
            this.url = url;
        }

        @Override
        public Result<String> call() throws Exception {
            return getContent(url);
        }

        private Result<String> getContent(String path) throws IOException {
            BufferedReader reader = null;
            try {
                URL url = new URL(path);
                HttpsURLConnection c = (HttpsURLConnection) url.openConnection();
                c.setRequestMethod(GET);
                c.setReadTimeout(TIMEOUT);
                c.connect();
                reader = new BufferedReader(new InputStreamReader(c.getInputStream()));
                StringBuilder buf = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    buf.append(line).append("\n");
                }
                return Result.success(buf.toString());
            } catch (InterruptedIOException
                    | ConnectException
                    | UnknownHostException
                    | MalformedURLException e) {
                return Result.failure(e);
            } finally {
                if (reader != null) {
                    reader.close();
                }
            }
        }
    }
}
