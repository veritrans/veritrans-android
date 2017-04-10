package com.midtrans.sdk.core.models.snap.ebanking.bcaklikpay;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by rakawm on 1/24/17.
 */

public class BcaKlikpayRedirectParams implements Serializable {

    // Some fields is not using underscore :(
    @Expose
    public final String klikPayCode;
    @Expose
    public final String transactionNo;
    @Expose
    public final String totalAmount;
    public final String currency;
    @Expose
    public final String payType;
    public final String callback;
    @Expose
    public final String transactionType;
    public final String descp;
    @Expose
    public final String miscFee;
    public final String signature;

    public BcaKlikpayRedirectParams(String klikPayCode,
                                    String transactionNo,
                                    String totalAmount,
                                    String currency,
                                    String payType,
                                    String callback,
                                    String transactionType,
                                    String descp,
                                    String miscFee,
                                    String signature) {
        this.klikPayCode = klikPayCode;
        this.transactionNo = transactionNo;
        this.totalAmount = totalAmount;
        this.currency = currency;
        this.payType = payType;
        this.callback = callback;
        this.transactionType = transactionType;
        this.descp = descp;
        this.miscFee = miscFee;
        this.signature = signature;
    }
}
