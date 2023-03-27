package com.example.bluehearts;

import android.app.Application;

import com.parse.Parse;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("J7aFl7VUSGK4P61MFlDbyoHdfSpdTGzfYmfYuEG0")
                // if defined
                .clientKey("zltLIQtBpppkm8LpI046ByfGC5r72oy1ByZkpGxE")
                .server("http://localhost:1337/parse/")
                .build()
        );
    }
}
