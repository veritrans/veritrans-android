package com.midtrans.sdk.corekit.models;

import java.util.List;

/**
 * Created by ziahaqi on 5/29/17.
 */

public class FreeText {
    private List<FreeTextLanguage> inquiry;
    private List<FreeTextLanguage> payment;

    public FreeText(List<FreeTextLanguage> inquiry, List<FreeTextLanguage> payment) {
        this.inquiry = inquiry;
        this.payment = payment;
    }

    public void setInquiry(List<FreeTextLanguage> inquiry) {
        this.inquiry = inquiry;
    }

    public void setPayment(List<FreeTextLanguage> payment) {
        this.payment = payment;
    }

}
