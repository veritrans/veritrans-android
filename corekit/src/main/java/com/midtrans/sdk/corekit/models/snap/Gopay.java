package com.midtrans.sdk.corekit.models.snap;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ziahaqi on 10/13/16.
 */

public class Gopay implements Serializable{

    @SerializedName("enable_callback")
    private boolean enableCallback = false;
    @SerializedName("callback_url")
    private String merchantGopayDeeplink;

    public Gopay(String merchantGopayDeeplink) {
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
