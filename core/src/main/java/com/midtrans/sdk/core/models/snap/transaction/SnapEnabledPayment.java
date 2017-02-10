package com.midtrans.sdk.core.models.snap.transaction;

/**
 * Created by rakawm on 10/20/16.
 */

public class SnapEnabledPayment {
    public final String type;
    public final String category;

    public SnapEnabledPayment(String type, String category) {
        this.type = type;
        this.category = category;
    }
}
