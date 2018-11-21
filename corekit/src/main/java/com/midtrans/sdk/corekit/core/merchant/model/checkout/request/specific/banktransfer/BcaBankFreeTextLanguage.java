package com.midtrans.sdk.corekit.core.merchant.model.checkout.request.specific.banktransfer;

public class BcaBankFreeTextLanguage {
    private String id;
    private String en;

    public BcaBankFreeTextLanguage(String id, String en) {
        this.id = id;
        this.en = en;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEn() {
        return en;
    }

    public void setEn(String en) {
        this.en = en;
    }
}
