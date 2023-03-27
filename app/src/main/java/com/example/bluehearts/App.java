package com.example.bluehearts;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;
import com.parse.ParseUser;

import org.w3c.dom.Comment;

import models.Report;
import models.Token;
import models.User;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //register all models BEFORE initializing!
        ParseObject.registerSubclass(Token.class);
        ParseObject.registerSubclass(Report.class);
        ParseUser.registerSubclass(User.class);

        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId("J7aFl7VUSGK4P61MFlDbyoHdfSpdTGzfYmfYuEG0")
                // if defined
                .clientKey("zltLIQtBpppkm8LpI046ByfGC5r72oy1ByZkpGxE")
                .server("https://parseapi.back4app.com")
                .build()
        );
    }
}
