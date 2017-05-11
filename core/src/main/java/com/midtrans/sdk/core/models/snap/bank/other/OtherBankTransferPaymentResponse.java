package com.midtrans.sdk.core.models.snap.bank.other;

import com.midtrans.sdk.core.models.snap.bank.VaNumber;
import com.midtrans.sdk.core.models.snap.bank.permata.PermataBankTransferPaymentResponse;

import java.util.List;

/**
 * Created by rakawm on 1/24/17.
 */

public class OtherBankTransferPaymentResponse extends PermataBankTransferPaymentResponse {

    public OtherBankTransferPaymentResponse(String statusCode,
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
                                            List<VaNumber> vaNumbers,
                                            String permataVaNumber,
                                            String permataExpiration) {
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
                vaNumbers,
                permataVaNumber,
                permataExpiration);
    }
}
