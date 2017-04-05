package com.midtrans.sdk.core.models.snap.transaction;

import java.util.List;

/**
 * Created by rakawm on 10/21/16.
 */

public class SnapMerchantData {
    public final SnapMerchantPreference preference;
    public final String clientKey;
    public final List<String> enabledPrinciples;
    public final List<String> pointBanks;

    public SnapMerchantData(SnapMerchantPreference preference, String clientKey, List<String> enabledPrinciples, List<String> pointBanks) {
        this.preference = preference;
        this.clientKey = clientKey;
        this.enabledPrinciples = enabledPrinciples;
        this.pointBanks = pointBanks;
    }
}
