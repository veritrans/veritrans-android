package com.midtrans.sdk.corekit.core.merchant.model.checkout.request.specific.banktransfer;

import java.io.Serializable;
import java.util.List;

public class BcaBankFreeText implements Serializable {
    private List<BcaBankFreeTextLanguage> inquiry;
    private List<BcaBankFreeTextLanguage> payment;

    public BcaBankFreeText(List<BcaBankFreeTextLanguage> inquiry,
                           List<BcaBankFreeTextLanguage> payment) {
        this.inquiry = inquiry;
        this.payment = payment;
    }

    public List<BcaBankFreeTextLanguage> getInquiry() {
        return inquiry;
    }

    public void setInquiry(List<BcaBankFreeTextLanguage> inquiry) {
        this.inquiry = inquiry;
    }

    public List<BcaBankFreeTextLanguage> getPayment() {
        return payment;
    }

    public void setPayment(List<BcaBankFreeTextLanguage> payment) {
        this.payment = payment;
    }
}
