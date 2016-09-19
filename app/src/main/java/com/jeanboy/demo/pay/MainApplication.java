package com.jeanboy.demo.pay;

import android.app.Application;

/**
 * Created by jeanboy on 2016/9/13.
 */
public class MainApplication extends Application {
    private static MainApplication ourInstance = new MainApplication();

    public static MainApplication getInstance() {
        return ourInstance;
    }

    private MainApplication() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
