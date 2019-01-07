package com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.optional;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class GopayDeepLink implements Serializable {

    @SerializedName("enable_callback")
    private boolean enableCallback = false;
    @SerializedName("callback_url")
    private String merchantGopayDeeplink;

    public GopayDeepLink(String merchantGopayDeeplink) {
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