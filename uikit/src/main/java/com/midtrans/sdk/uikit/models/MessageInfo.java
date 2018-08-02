package com.midtrans.sdk.uikit.models;

import android.support.annotation.NonNull;

/**
 * Created by ziahaqi on 8/7/17.
 */

public class MessageInfo {
    public String titleMessage;
    public String detailsMessage;

    public MessageInfo(@NonNull String titleMessage, @NonNull String detailsMessage) {
        this.titleMessage = titleMessage;
        this.detailsMessage = detailsMessage;
    }


}
