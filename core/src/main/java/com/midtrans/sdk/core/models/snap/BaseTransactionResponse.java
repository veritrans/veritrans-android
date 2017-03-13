package com.midtrans.sdk.core.models.snap;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rakawm on 10/19/16.
 */

public class BaseTransactionResponse {
    public final String statusCode;
    public final String statusMessage;
    public final String transactionId;
    public final String orderId;
    public final String grossAmount;
    public final String paymentType;
    public final String transactionTime;
    public final String transactionStatus;
    public final String fraudStatus;
    public final String finishRedirectUrl;
    public List<String> validationMessages;

    public BaseTransactionResponse(String statusCode,
                                   String statusMessage,
                                   String transactionId,
                                   String orderId,
                                   String grossAmount,
                                   String paymentType,
                                   String transactionTime,
                                   String transactionStatus,
                                   String fraudStatus,
                                   String finishRedirectUrl) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.transactionId = transactionId;
        this.orderId = orderId;
        this.grossAmount = grossAmount;
        this.paymentType = paymentType;
        this.transactionTime = transactionTime;
        this.transactionStatus = transactionStatus;
        this.fraudStatus = fraudStatus;
        this.finishRedirectUrl = finishRedirectUrl;
    }
}
