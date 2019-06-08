package com.penopllast.loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import javax.net.ssl.HttpsURLConnection;

public class Loader implements ILoader {

    private final Async async = new Async();

    @Override
    public void load(String url) {
        Promise<String> promise = async.submit(new LoadCallable(url));
        //Promise<Integer> promise2 = promise.bind(string -> Promise.pure(string.length()));
        try {
            System.out.println("data = " + promise.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    private static final class LoadCallable implements Callable<String> {
        private final String url;

        LoadCallable(String url) {
            this.url = url;
        }

        @Override
        public String call() throws Exception {
            return getContent(url);
        }

        private String getContent(String path) throws IOException {
            BufferedReader reader=null;
            try {
                URL url=new URL(path);
                HttpsURLConnection c=(HttpsURLConnection)url.openConnection();
                c.setRequestMethod("GET");
                c.setReadTimeout(10000);
                c.connect();
                reader= new BufferedReader(new InputStreamReader(c.getInputStream()));
                StringBuilder buf=new StringBuilder();
                String line=null;
                while ((line=reader.readLine()) != null) {
                    buf.append(line).append("\n");
                }
                return(buf.toString());
            }
            finally {
                if (reader != null) {
                    reader.close();
                }
            }
        }


    }
}
