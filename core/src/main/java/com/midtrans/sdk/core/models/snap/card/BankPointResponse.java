package com.midtrans.sdk.core.models.snap.card;

import java.util.List;

/**
 * Created by rakawm on 4/5/17.
 */

public class BankPointResponse {
    public final String statusCode;
    public final String statusMessage;
    public final List<String> validationMessages;
    public final Long pointBalance;
    public final String pointBalanceAmount;
    public final String transactionTime;

    public BankPointResponse(String statusCode,
                             String statusMessage,
                             List<String> validationMessages,
                             Long pointBalance,
                             String pointBalanceAmount,
                             String transactionTime) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.validationMessages = validationMessages;
        this.pointBalance = pointBalance;
        this.pointBalanceAmount = pointBalanceAmount;
        this.transactionTime = transactionTime;
    }
}
