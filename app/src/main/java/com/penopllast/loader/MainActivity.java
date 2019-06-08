package com.penopllast.loader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String[] urls = new String[]{
                "https://yandex.ru",
                "https://yan111dex.ru",
                "h111ttps://yandex.ru",
                ""
        };
        for (String url : urls) {
            loadUrls(url);
        }
    }

    private void loadUrls(String url) {

        ILoader loader = new Loader();
        Promise<Result<String>> promise = loader.load(url);

        Result<String> a = null;
        String exceptionMessage = "";
        try {
            a = promise.get();
        } catch (InterruptedException | ExecutionException e) {
            exceptionMessage = e.getMessage();
        }

        String status = a == null
                ? exceptionMessage : a.isSuccess()
                ? "success" : a.getErrorMessage();
        addRow("url = " + url + "; status = " + status);
    }

    private void addRow(String text) {
        LinearLayout layout = findViewById(R.id.root);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        TextView textView = new TextView(this);
        textView.setText(text);
        layout.addView(textView, params);
    }
}
