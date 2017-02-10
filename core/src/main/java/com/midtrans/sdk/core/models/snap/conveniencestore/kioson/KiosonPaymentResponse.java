package com.midtrans.sdk.core.models.snap.conveniencestore.kioson;

import com.midtrans.sdk.core.models.snap.conveniencestore.ConvenienceStorePaymentResponse;

/**
 * Created by rakawm on 1/26/17.
 */

public class KiosonPaymentResponse extends ConvenienceStorePaymentResponse {
    public final String kiosonExpireTime;

    public KiosonPaymentResponse(String statusCode,
                                 String statusMessage,
                                 String transactionId,
                                 String orderId,
                                 String grossAmount,
                                 String paymentType,
                                 String transactionTime,
                                 String transactionStatus,
                                 String fraudStatus,
                                 String finishRedirectUrl,
                                 String pdfUrl,
                                 String paymentCode,
                                 String store,
                                 String kiosonExpireTime) {
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
                pdfUrl,
                paymentCode,
                store);
        this.kiosonExpireTime = kiosonExpireTime;
    }
}
