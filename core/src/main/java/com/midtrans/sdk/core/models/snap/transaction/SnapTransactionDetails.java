package com.midtrans.sdk.core.models.snap.transaction;

/**
 * Created by rakawm on 10/20/16.
 */

public class SnapTransactionDetails {
    public final String orderId;
    public final int grossAmount;

    public SnapTransactionDetails(String orderId, int grossAmount) {
        this.orderId = orderId;
        this.grossAmount = grossAmount;
    }
}
