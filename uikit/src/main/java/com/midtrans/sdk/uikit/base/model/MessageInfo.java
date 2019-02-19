package com.midtrans.sdk.uikit.base.model;

public class MessageInfo {
    private String titleMessage;
    private String detailsMessage;

    public MessageInfo(String titleMessage, String detailsMessage) {
        this.titleMessage = titleMessage;
        this.detailsMessage = detailsMessage;
    }

    public String getTitleMessage() {
        return titleMessage;
    }

    public void setTitleMessage(String titleMessage) {
        this.titleMessage = titleMessage;
    }

    public String getDetailsMessage() {
        return detailsMessage;
    }

    public void setDetailsMessage(String detailsMessage) {
        this.detailsMessage = detailsMessage;
    }
}
