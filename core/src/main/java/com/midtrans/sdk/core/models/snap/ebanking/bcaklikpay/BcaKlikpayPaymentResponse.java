package com.midtrans.sdk.core.models.snap.ebanking.bcaklikpay;

import com.midtrans.sdk.core.models.snap.ebanking.EbankingPaymentResponse;

import java.io.Serializable;

/**
 * Created by rakawm on 1/24/17.
 */

public class BcaKlikpayPaymentResponse extends EbankingPaymentResponse implements Serializable {

    public final BcaKlikpayRedirectData redirectData;

    public BcaKlikpayPaymentResponse(String statusCode,
                                     String statusMessage,
                                     String transactionId,
                                     String orderId,
                                     String grossAmount,
                                     String paymentType,
                                     String transactionTime,
                                     String transactionStatus,
                                     String fraudStatus,
                                     String finishRedirectUrl,
                                     String redirectUrl,
                                     BcaKlikpayRedirectData redirectData) {
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
        this.redirectData = redirectData;
    }
}
