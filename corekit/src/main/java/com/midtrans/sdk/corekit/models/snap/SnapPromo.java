package com.midtrans.sdk.corekit.models.snap;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by rakawm on 1/2/17.
 */

public class SnapPromo {
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