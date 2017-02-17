package com.midtrans.sdk.core.models.snap.conveniencestore.indomaret;

import com.midtrans.sdk.core.models.snap.conveniencestore.ConvenienceStorePaymentResponse;

/**
 * Created by rakawm on 1/26/17.
 */

public class IndomaretPaymentResponse extends ConvenienceStorePaymentResponse {
    public final String indomaretExpireTime;

    public IndomaretPaymentResponse(String statusCode,
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
                                    String indomaretExpireTime) {
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
        this.indomaretExpireTime = indomaretExpireTime;
    }
}
