package com.midtrans.veritransandroid;

import android.app.Application;

import com.midtrans.sdk.core.Environment;
import com.midtrans.sdk.core.MidtransCore;

/**
 * Created by rakawm on 10/19/16.
 */

public class MyApp extends Application {
    public static final String CHECKOUT_URL = "https://rakawm-snap.herokuapp.com/charge";
    private static final String CLIENT_KEY = "VT-client-E4f1bsi1LpL1p5cF";

    @Override
    public void onCreate() {
        super.onCreate();
        initMidtransSDK();
    }

    private void initMidtransSDK() {
        new MidtransCore.Builder()
                .enableLog(true)
                .setEnvironment(Environment.SANDBOX)
                .setClientKey(CLIENT_KEY)
                .build();
    }
}
