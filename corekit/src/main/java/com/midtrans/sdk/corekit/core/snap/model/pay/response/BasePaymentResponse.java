package com.midtrans.sdk.corekit.core.snap.model.pay.response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.midtrans.sdk.corekit.core.snap.model.pay.response.bcaklikpay.BcaKlikPayDataResponse;

import java.io.Serializable;

public class BasePaymentResponse implements Serializable {

    @SerializedName("status_code")
    @Expose
    protected String statusCode;
    @SerializedName("status_message")
    @Expose
    protected String statusMessage;
    @SerializedName("redirect_url")
    @Expose
    protected String redirectUrl;
    @SerializedName("transaction_id")
    @Expose
    protected String transactionId;
    @SerializedName("order_id")
    @Expose
    protected String orderId;
    @SerializedName("gross_amount")
    @Expose
    protected String grossAmount;
    @SerializedName("currency")
    @Expose
    protected String currency;
    @SerializedName("payment_type")
    @Expose
    protected String paymentType;
    @SerializedName("transaction_time")
    @Expose
    protected String transactionTime;
    @SerializedName("transaction_status")
    @Expose
    protected String transactionStatus;
    @SerializedName("finish_redirect_url")
    @Expose
    protected String finishRedirectUrl;

    /**
     * KlikBCA
     */
    @SerializedName("approval_code")
    @Expose
    protected String approvalCode;

    /**
     * BcaKlikPay
     */
    @SerializedName("redirect_data")
    @Expose
    protected BcaKlikPayDataResponse dataResponse;

    /**
     * BRIEpay
     */
    @SerializedName("fraud_status")
    @Expose
    protected String fraudStatus;
}