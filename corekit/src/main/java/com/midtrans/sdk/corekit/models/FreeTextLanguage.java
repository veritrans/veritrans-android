package com.midtrans.sdk.corekit.models;

/**
 * Created by ziahaqi on 5/29/17.
 */

public class FreeTextLanguage {
    private String id;
    private String en;

    public FreeTextLanguage(String id, String en) {
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
