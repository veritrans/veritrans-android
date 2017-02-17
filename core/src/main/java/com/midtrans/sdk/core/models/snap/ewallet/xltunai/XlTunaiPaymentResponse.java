package com.midtrans.sdk.core.models.snap.ewallet.xltunai;

import com.midtrans.sdk.core.models.snap.BaseTransactionResponse;

/**
 * Created by rakawm on 1/26/17.
 */

public class XlTunaiPaymentResponse extends BaseTransactionResponse {
    public final String xlTunaiOrderId;
    public final String xlTunaiMerchantId;
    public final String xlExpiration;

    public XlTunaiPaymentResponse(String statusCode,
                                  String statusMessage,
                                  String transactionId,
                                  String orderId,
                                  String grossAmount,
                                  String paymentType,
                                  String transactionTime,
                                  String transactionStatus,
                                  String fraudStatus,
                                  String finishRedirectUrl,
                                  String xlTunaiOrderId,
                                  String xlTunaiMerchantId,
                                  String xlExpiration) {
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
        this.xlTunaiOrderId = xlTunaiOrderId;
        this.xlTunaiMerchantId = xlTunaiMerchantId;
        this.xlExpiration = xlExpiration;
    }
}
