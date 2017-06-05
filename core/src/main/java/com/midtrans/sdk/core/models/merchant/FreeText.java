package com.midtrans.sdk.core.models.merchant;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rakawm on 5/11/17.
 */

public class FreeText implements Serializable {
    private List<FreeTextItem> inquiry;
    private List<FreeTextItem> payment;

    public FreeText(List<FreeTextItem> inquiry, List<FreeTextItem> payment) {
        this.inquiry = inquiry;
        this.payment = payment;
    }

    public List<FreeTextItem> getInquiry() {
        return inquiry;
    }

    public void setInquiry(List<FreeTextItem> inquiry) {
        this.inquiry = inquiry;
    }

    public List<FreeTextItem> getPayment() {
        return payment;
    }

    public void setPayment(List<FreeTextItem> payment) {
        this.payment = payment;
    }
}
