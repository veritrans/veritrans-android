package com.midtrans.sdk.core.models.merchant;

/**
 * Created by rakawm on 10/19/16.
 */

public class CheckoutOrderDetails {
    public final String orderId;
    public final int grossAmount;

    public CheckoutOrderDetails(String orderId, int grossAmount) {
        this.orderId = orderId;
        this.grossAmount = grossAmount;
    }
}
