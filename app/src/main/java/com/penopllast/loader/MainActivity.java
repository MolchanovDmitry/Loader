package com.penopllast.loader;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import dalvik.system.InMemoryDexClassLoader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ILoader loader = new Loader();
        loader.load("https://www.youtube.com");
    }
}
