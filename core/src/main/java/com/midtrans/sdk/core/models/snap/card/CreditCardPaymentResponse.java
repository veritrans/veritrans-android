package com.midtrans.sdk.core.models.snap.card;

import com.midtrans.sdk.core.models.snap.BaseTransactionResponse;

/**
 * Created by rakawm on 10/19/16.
 */

public class CreditCardPaymentResponse extends BaseTransactionResponse {
    public final String savedTokenId;
    public final String maskedCard;
    public final String savedTokenIdExpiredAt;
    public final String approvalCode;
    public final String bank;

    public CreditCardPaymentResponse(String statusCode,
                                     String statusMessage,
                                     String transactionId,
                                     String orderId,
                                     String grossAmount,
                                     String paymentType,
                                     String transactionTime,
                                     String transactionStatus,
                                     String fraudStatus,
                                     String finishRedirectUrl,
                                     String savedTokenId,
                                     String maskedCard,
                                     String savedTokenIdExpiredAt,
                                     String approvalCode,
                                     String bank) {
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
        this.savedTokenId = savedTokenId;
        this.maskedCard = maskedCard;
        this.savedTokenIdExpiredAt = savedTokenIdExpiredAt;
        this.approvalCode = approvalCode;
        this.bank = bank;
    }
}
