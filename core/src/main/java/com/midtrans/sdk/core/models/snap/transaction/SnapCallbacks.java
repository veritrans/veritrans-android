package com.midtrans.sdk.core.models.snap.transaction;

/**
 * Created by rakawm on 10/20/16.
 */

public class SnapCallbacks {
    public final String error;
    public final String finish;

    public SnapCallbacks(String error, String finish) {
        this.error = error;
        this.finish = finish;
    }
}
