package com.midtrans.sdk.corekit.core.merchant.model.checkout.request.optional;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SnapPromo implements Serializable {
    private boolean enabled;
    @SerializedName("allowed_promo_codes")
    private List<String> allowedPromoCodes;

    public List<String> getAllowedPromoCodes() {
        return allowedPromoCodes;
    }

    public void setAllowedPromoCodes(List<String> allowedPromoCodes) {
        this.allowedPromoCodes = allowedPromoCodes;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
