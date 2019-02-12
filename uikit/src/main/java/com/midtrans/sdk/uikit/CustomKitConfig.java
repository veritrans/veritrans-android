package com.midtrans.sdk.uikit;

public class CustomKitConfig {

    private String defaultText;
    private String boldText;
    private String semiBoldText;
    private boolean showPaymentStatus;
    private boolean saveCardChecked;
    private boolean enabledAnimation;
    private boolean enableAutoReadSms;
    private boolean skipCustomerDetailsPages;
    private boolean showEmailInCcForm;

    CustomKitConfig(
            String defaultText,
            String boldText,
            String semiBoldText,
            boolean showPaymentStatus,
            boolean saveCardChecked,
            boolean enabledAnimation,
            boolean enableAutoReadSms,
            boolean skipCustomerDetailsPages,
            boolean showEmailInCcForm
    ) {
        this.defaultText = defaultText;
        this.boldText = boldText;
        this.semiBoldText = semiBoldText;
        this.showPaymentStatus = showPaymentStatus;
        this.saveCardChecked = saveCardChecked;
        this.enabledAnimation = enabledAnimation;
        this.enableAutoReadSms = enableAutoReadSms;
        this.skipCustomerDetailsPages = skipCustomerDetailsPages;
        this.showEmailInCcForm = showEmailInCcForm;
    }

    public String getDefaultText() {
        return defaultText;
    }

    public String getBoldText() {
        return boldText;
    }

    public String getSemiBoldText() {
        return semiBoldText;
    }

    public boolean isShowPaymentStatus() {
        return showPaymentStatus;
    }

    public boolean isSaveCardChecked() {
        return saveCardChecked;
    }

    public boolean isEnabledAnimation() {
        return enabledAnimation;
    }

    public boolean isEnableAutoReadSms() {
        return enableAutoReadSms;
    }

    public boolean isSkipCustomerDetailsPages() {
        return skipCustomerDetailsPages;
    }

    public boolean isShowEmailInCcForm() {
        return showEmailInCcForm;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static final class Builder {
        private String defaultText;
        private String boldText;
        private String semiBoldText;
        private boolean showPaymentStatus = true;
        private boolean saveCardChecked = false;
        private boolean enabledAnimation = true;
        private boolean enableAutoReadSms = false;
        private boolean skipCustomerDetailsPages = false;
        private boolean showEmailInCcForm = false;

        public Builder setDefaultText(String defaultText) {
            this.defaultText = defaultText;
            return this;
        }

        public Builder setBoldText(String boldText) {
            this.boldText = boldText;
            return this;
        }

        public Builder setSemiBoldText(String semiBoldText) {
            this.semiBoldText = semiBoldText;
            return this;
        }

        public Builder setShowPaymentStatus(boolean showPaymentStatus) {
            this.showPaymentStatus = showPaymentStatus;
            return this;
        }

        public Builder setSaveCardChecked(boolean saveCardChecked) {
            this.saveCardChecked = saveCardChecked;
            return this;
        }

        public Builder setEnabledAnimation(boolean enabledAnimation) {
            this.enabledAnimation = enabledAnimation;
            return this;
        }

        public Builder setEnableAutoReadSms(boolean enableAutoReadSms) {
            this.enableAutoReadSms = enableAutoReadSms;
            return this;
        }

        public Builder setSkipCustomerDetailsPages(boolean skipCustomerDetailsPages) {
            this.skipCustomerDetailsPages = skipCustomerDetailsPages;
            return this;
        }

        public Builder setShowEmailInCcForm(boolean showEmailInCcForm) {
            this.showEmailInCcForm = showEmailInCcForm;
            return this;
        }

        public CustomKitConfig build() {
            return new CustomKitConfig(
                    this.defaultText,
                    this.boldText,
                    this.semiBoldText,
                    this.showPaymentStatus,
                    this.saveCardChecked,
                    this.enabledAnimation,
                    this.enableAutoReadSms,
                    this.skipCustomerDetailsPages,
                    this.showEmailInCcForm
            );
        }
    }
}
