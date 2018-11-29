package com.midtrans.sdk.corekit.base.network;

public abstract class BaseServiceManager {
    private static String TAG = BaseServiceManager.class.getSimpleName();
    Boolean isRunning;

    protected void releaseResources() {
        this.isRunning = false;
    }

}