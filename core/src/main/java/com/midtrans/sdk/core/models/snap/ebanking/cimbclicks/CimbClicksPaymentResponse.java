package com.midtrans.sdk.core.models.snap.ebanking.cimbclicks;

import com.midtrans.sdk.core.models.snap.ebanking.EbankingPaymentResponse;

/**
 * Created by rakawm on 1/25/17.
 */

public class CimbClicksPaymentResponse extends EbankingPaymentResponse {

    public CimbClicksPaymentResponse(String statusCode,
                                     String statusMessage,
                                     String transactionId,
                                     String orderId,
                                     String grossAmount,
                                     String paymentType,
                                     String transactionTime,
                                     String transactionStatus,
                                     String fraudStatus,
                                     String finishRedirectUrl,
                                     String redirectUrl) {
        super(statusCode,
                statusMessage,
                transactionId,
                orderId,
                grossAmount,
                paymentType,
                transactionTime,
                transactionStatus,
                fraudStatus,
                finishRedirectUrl,
                redirectUrl);
    }
}
