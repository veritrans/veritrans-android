package com.midtrans.sdk.core.models.papi;

/**
 * Created by rakawm on 10/19/16.
 */

public class CardTokenResponse {
    public final String statusMessage;
    public final String bank;
    public final String statusCode;
    public final String tokenId;
    public final String redirectUrl;

    public CardTokenResponse(String statusMessage,
                             String bank,
                             String statusCode,
                             String tokenId,
                             String redirectUrl) {
        this.statusMessage = statusMessage;
        this.bank = bank;
        this.statusCode = statusCode;
        this.tokenId = tokenId;
        this.redirectUrl = redirectUrl;
    }
}
