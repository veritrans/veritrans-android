package com.midtrans.sdk.core.models.merchant;

import java.io.Serializable;

/**
 * Created by rakawm on 10/19/16.
 */

public class CheckoutOrderDetails implements Serializable {
    public final String orderId;
    public final int grossAmount;

    public CheckoutOrderDetails(String orderId, int grossAmount) {
        this.orderId = orderId;
        this.grossAmount = grossAmount;
    }
}
