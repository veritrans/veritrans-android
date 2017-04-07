package com.midtrans.sdk.core.models.snap.ebanking.klikbca;

import com.midtrans.sdk.core.models.snap.ebanking.EbankingPaymentResponse;

/**
 * Created by rakawm on 1/24/17.
 */

public class KlikBcaPaymentResponse extends EbankingPaymentResponse {

    public final String approvalCode;
    public final String bcaKlikbcaExpireTime;

    public KlikBcaPaymentResponse(String statusCode,
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
                                  String approvalCode,
                                  String bcaKlikbcaExpireTime) {
        super(statusCode,
                statusMessage,
                transactionId,
                orderId, grossAmount,
                paymentType,
                transactionTime,
                transactionStatus,
                fraudStatus,
                finishRedirectUrl,
                redirectUrl);
        this.approvalCode = approvalCode;
        this.bcaKlikbcaExpireTime = bcaKlikbcaExpireTime;
    }
}
