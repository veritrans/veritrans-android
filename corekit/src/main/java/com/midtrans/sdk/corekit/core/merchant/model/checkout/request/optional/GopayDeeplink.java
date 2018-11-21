package com.midtrans.sdk.corekit.core.merchant.model.checkout.request.optional;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GopayDeeplink implements Serializable {

    @SerializedName("enable_callback")
    private boolean enableCallback = false;
    @SerializedName("callback_url")
    private String merchantGopayDeeplink;

    public GopayDeeplink() {
    }

    public GopayDeeplink(String merchantGopayDeeplink) {
        this.merchantGopayDeeplink = merchantGopayDeeplink;
        this.enableCallback = true;
    }

    public String getMerchantGopayDeeplink() {
        return merchantGopayDeeplink;
    }

    public void setMerchantGopayDeeplink(String merchantGopayDeeplink) {
        this.merchantGopayDeeplink = merchantGopayDeeplink;
        this.enableCallback = true;
    }
}