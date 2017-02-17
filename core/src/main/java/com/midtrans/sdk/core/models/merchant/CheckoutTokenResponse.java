package com.midtrans.sdk.core.models.merchant;

import java.util.List;

/**
 * Created by rakawm on 10/19/16.
 */

public class CheckoutTokenResponse {
    public final String token;
    public final List<String> errorMessages;

    public CheckoutTokenResponse(String token, List<String> errorMessages) {
        this.token = token;
        this.errorMessages = errorMessages;
    }
}
