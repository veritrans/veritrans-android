package com.midtrans.demo;

import android.app.Application;

import com.crashlytics.android.Crashlytics;

import io.fabric.sdk.android.Fabric;

/**
 * Created by rakawm on 3/15/17.
 */

public class DemoApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        initCrashlytics();
    }

    private void initCrashlytics() {
        Fabric.with(this, new Crashlytics());
    }
}
