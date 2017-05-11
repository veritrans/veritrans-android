package com.midtrans.veritransandroid;

import android.app.Application;

import com.midtrans.sdk.core.Environment;
import com.midtrans.sdk.ui.MidtransUi;

/**
 * Created by rakawm on 10/19/16.
 */

public class MyApp extends Application {
    public static final String CHECKOUT_URL = "https://rakawm-snap.herokuapp.com/charge";
    private static final String CLIENT_KEY = "VT-client-E4f1bsi1LpL1p5cF";
    private static final String DEFAULT_FONT = "fonts/open_sans_regular.ttf";
    private static final String SEMI_BOLD_FONT = "fonts/open_sans_semibold.ttf";
    private static final String BOLD_FONT = "fonts/open_sans_bold.ttf";

    @Override
    public void onCreate() {
        super.onCreate();
        initMidtransSDK();
    }

    private void initMidtransSDK() {
        new MidtransUi.Builder()
                .setContext(this)
                .setClientKey(CLIENT_KEY)
                .setEnvironment(Environment.SANDBOX)
                .setDefaultFontPath(DEFAULT_FONT)
                .setBoldFontPath(BOLD_FONT)
                .setSemiBoldFontPath(SEMI_BOLD_FONT)
                .enableLog(true)
                .build();
    }
}
