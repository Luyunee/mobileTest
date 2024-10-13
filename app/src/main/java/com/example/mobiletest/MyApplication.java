package com.example.mobiletest;

import android.app.Application;

import com.example.mobiletest.utils.AppUtils;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AppUtils.INSTANCE.app(this);
    }
}
