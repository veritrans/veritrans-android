package com.midtrans.sdk.ui.models;

import android.text.TextUtils;

import com.midtrans.sdk.core.models.snap.BaseTransactionResponse;
import com.midtrans.sdk.core.models.snap.card.CreditCardPaymentResponse;
import com.midtrans.sdk.ui.constants.PaymentStatus;

import java.io.Serializable;
import java.util.List;

/**
 * Created by ziahaqi on 2/20/17.
 */

public class PaymentResult<T extends BaseTransactionResponse> implements Serializable {
    private String installmentTerm;
    private String errorMessage;
    private String paymentStatus;

    /**
     * base & tcash & indosat
     */
    private String statusCode;
    private String statusMessage;
    private String transactionId;
    private String orderId;
    private String grossAmount;
    private String paymentType;
    private String transactionTime;
    private String transactionStatus;
    private String fraudStatus;
    private String finishRedirectUrl;

    // bank transfert & cstore
    private String pdfUrl;

    // cc and gci
    private String savedTokenId;
    private String maskedCard;
    private String savedTokenIdExpiredAt;
    private String approvalCode;
    private String bank;

    // cstore
    private String paymentCode;
    private String store;

    //e-banking
    private String redirectUrl;

    //xltunai
    private String xlTunaiOrderId;
    private String xlTunaiMerchantId;
    private String xlExpiration;
    private List<String> validationMessages;


    private void init(BaseTransactionResponse response) {
        this.statusCode = response.statusCode;
        this.statusMessage = response.statusMessage;
        this.transactionId = response.transactionId;
        this.orderId = response.orderId;
        this.grossAmount = response.grossAmount;
        this.paymentType = response.paymentType;
        this.transactionTime = response.transactionTime;
        this.transactionStatus = response.transactionStatus;
        this.fraudStatus = response.fraudStatus;
        this.finishRedirectUrl = response.finishRedirectUrl;
        this.validationMessages = response.validationMessages;
        initPaymentStatus();
    }

    private void initPaymentStatus() {
        if (statusCode.equals(PaymentStatus.CODE_200) ||
                transactionStatus.equalsIgnoreCase(PaymentStatus.SUCCESS) ||
                transactionStatus.equalsIgnoreCase(PaymentStatus.SETTLEMENT)) {
            paymentStatus = PaymentStatus.SUCCESS;
        } else if (statusCode.equals(PaymentStatus.PENDING) || transactionStatus.equalsIgnoreCase(PaymentStatus.PENDING)) {
            if (fraudStatus.equalsIgnoreCase(PaymentStatus.CHALLENGE)) {
                paymentStatus = PaymentStatus.CHALLENGE;
            } else {
                paymentStatus = PaymentStatus.PENDING;
            }
        } else if (TextUtils.isEmpty(errorMessage)) {
            this.paymentStatus = PaymentStatus.INVALID;
        } else {
            this.paymentStatus = PaymentStatus.FAILED;
        }
    }

    public PaymentResult(T paymentResponse) {
        init(paymentResponse);
        if (paymentResponse instanceof CreditCardPaymentResponse) {
            CreditCardPaymentResponse response = (CreditCardPaymentResponse) paymentResponse;
            this.savedTokenId = response.savedTokenId;
            this.maskedCard = response.maskedCard;
            this.savedTokenIdExpiredAt = response.savedTokenIdExpiredAt;
            this.approvalCode = response.approvalCode;
            this.bank = response.bank;
            this.installmentTerm = response.installmentTerm;
        }
    }

    public PaymentResult(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getGrossAmount() {
        return grossAmount;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public String getTransactionTime() {
        return transactionTime;
    }

    public String getTransactionStatus() {
        return transactionStatus;
    }

    public String getFraudStatus() {
        return fraudStatus;
    }

    public String getFinishRedirectUrl() {
        return finishRedirectUrl;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public String getSavedTokenId() {
        return savedTokenId;
    }

    public String getMaskedCard() {
        return maskedCard;
    }

    public String getSavedTokenIdExpiredAt() {
        return savedTokenIdExpiredAt;
    }

    public String getApprovalCode() {
        return approvalCode;
    }

    public String getBank() {
        return bank;
    }

    public String getPaymentCode() {
        return paymentCode;
    }

    public String getStore() {
        return store;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public String getXlTunaiOrderId() {
        return xlTunaiOrderId;
    }

    public String getXlTunaiMerchantId() {
        return xlTunaiMerchantId;
    }

    public String getXlExpiration() {
        return xlExpiration;
    }

    public String getPaymentStatus() {
        return paymentStatus;
    }

    public List<String> getValidationMessages() {
        return validationMessages;
    }

    public String getInstallmentTerm() {
        return installmentTerm;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

}
