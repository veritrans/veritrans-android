package com.midtrans.sdk.core.models.snap;

import com.midtrans.sdk.core.models.snap.bank.bca.BcaVaNumber;

import java.util.List;

/**
 * Created by rakawm on 10/19/16.
 */

public class TransactionResponse {
    // Base
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

    // Credit card
    public final String savedTokenId;
    public final String maskedCard;
    public final String savedTokenIdExpiredAt;
    public final String approvalCode;
    public final String bank;

    public final boolean secureToken;
    // Bank Transfer Permata
    public final String permataVaNumber;
    // Bank Transfer BCA
    public final List<BcaVaNumber> vaNumbers;
    // XL Tunai
    public final String xlTunaiOrderId;
    public final String xlTunaiMerchantId;
    public final String xlExpiration;
    // Redirect URL
    public final String redirectUrl;
    // PDF Url
    public final String pdfUrl;

    public final String eci;
    public final String billKey;
    public final String billerCode;
    public final String paymentCode;

    public final String kiosonExpireTime;

    public TransactionResponse(String statusCode,
                               String statusMessage,
                               String transactionId,
                               String savedTokenId,
                               String maskedCard,
                               String orderId,
                               String grossAmount,
                               String paymentType,
                               String transactionTime,
                               String transactionStatus,
                               String fraudStatus,
                               String savedTokenIdExpiredAt,
                               String approvalCode,
                               boolean secureToken,
                               String permataVaNumber,
                               List<BcaVaNumber> vaNumbers,
                               String xlTunaiOrderId,
                               String xlTunaiMerchantId,
                               String xlExpiration,
                               String redirectUrl,
                               String pdfUrl,
                               String bank,
                               String eci,
                               String billKey,
                               String billerCode,
                               String paymentCode,
                               String finishRedirectUrl,
                               String kiosonExpireTime) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.transactionId = transactionId;
        this.savedTokenId = savedTokenId;
        this.maskedCard = maskedCard;
        this.orderId = orderId;
        this.grossAmount = grossAmount;
        this.paymentType = paymentType;
        this.transactionTime = transactionTime;
        this.transactionStatus = transactionStatus;
        this.fraudStatus = fraudStatus;
        this.savedTokenIdExpiredAt = savedTokenIdExpiredAt;
        this.approvalCode = approvalCode;
        this.secureToken = secureToken;
        this.permataVaNumber = permataVaNumber;
        this.vaNumbers = vaNumbers;
        this.xlTunaiOrderId = xlTunaiOrderId;
        this.xlTunaiMerchantId = xlTunaiMerchantId;
        this.xlExpiration = xlExpiration;
        this.redirectUrl = redirectUrl;
        this.pdfUrl = pdfUrl;
        this.bank = bank;
        this.eci = eci;
        this.billKey = billKey;
        this.billerCode = billerCode;
        this.paymentCode = paymentCode;
        this.finishRedirectUrl = finishRedirectUrl;
        this.kiosonExpireTime = kiosonExpireTime;
    }
}
