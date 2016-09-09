package com.midtrans.sdk.sample;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * @author rakawm
 */
public class MyApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
    }
}
