package com.midtrans.sdk.corekit.core;

/**
 * Created by ziahaqi on 11/4/16.
 */

public class UIKitCustomSetting {

    private boolean showPaymentStatus = true;
    private boolean saveCardChecked = false;
    private boolean enabledAnimation = true;
    private boolean enableAutoReadSms = false;
    private boolean skipCustomerDetailsPages = false;
    private boolean showEmailInCcForm = false;

    public boolean isShowPaymentStatus() {
        return showPaymentStatus;
    }

    public void setShowPaymentStatus(boolean showPaymentStatus) {
        this.showPaymentStatus = showPaymentStatus;
    }

    public boolean isSaveCardChecked() {
        return saveCardChecked;
    }

    public void setSaveCardChecked(boolean saveCardChecked) {
        this.saveCardChecked = saveCardChecked;
    }

    public boolean isEnabledAnimation() {
        return enabledAnimation;
    }

    public void setEnabledAnimation(boolean enabledAnimation) {
        this.enabledAnimation = enabledAnimation;
    }

    public boolean isEnableAutoReadSms() {
        return enableAutoReadSms;
    }

    public void setEnableAutoReadSms(boolean enableAutoReadSms) {
        this.enableAutoReadSms = enableAutoReadSms;
    }

    public boolean isSkipCustomerDetailsPages() {
        return skipCustomerDetailsPages;
    }

    public void setSkipCustomerDetailsPages(boolean skipCustomerDetailsPages) {
        this.skipCustomerDetailsPages = skipCustomerDetailsPages;
    }

    public boolean isShowEmailInCcForm() {
        return showEmailInCcForm;
    }

    public void setShowEmailInCcForm(boolean showEmailInCcForm) {
        this.showEmailInCcForm = showEmailInCcForm;
    }
}
