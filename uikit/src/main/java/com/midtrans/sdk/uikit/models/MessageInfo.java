package com.midtrans.sdk.uikit.models;

import android.support.annotation.NonNull;

/**
 * Created by ziahaqi on 8/7/17.
 */

public class MessageInfo {
    public String statusCode;
    public String statusMessage;
    public String detailsMessage;

    public MessageInfo(String statusCode, @NonNull String statusMessage, @NonNull String detailsMessage) {
        this.statusCode = statusCode;
        this.statusMessage = statusMessage;
        this.detailsMessage = detailsMessage;
    }


}
