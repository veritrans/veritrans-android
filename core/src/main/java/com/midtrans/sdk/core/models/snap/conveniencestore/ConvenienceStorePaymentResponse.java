package com.midtrans.sdk.core.models.snap.conveniencestore;

import com.midtrans.sdk.core.models.snap.BaseTransactionResponse;

/**
 * Created by rakawm on 1/26/17.
 */

public class ConvenienceStorePaymentResponse extends BaseTransactionResponse {
    public final String pdfUrl;
    public final String paymentCode;
    public final String store;

    public ConvenienceStorePaymentResponse(String statusCode,
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
                                           String store) {
        super(statusCode,
                statusMessage,
                transactionId,
                orderId,
                grossAmount,
                paymentType,
                transactionTime,
                transactionStatus,
                fraudStatus,
                finishRedirectUrl);
        this.pdfUrl = pdfUrl;
        this.paymentCode = paymentCode;
        this.store = store;
    }
}
