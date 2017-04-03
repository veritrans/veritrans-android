package com.midtrans.sdk.ui;
/**
 * This class is used for custom MidtransUi settings,
 *
 * There are some UI that can be customized:
 * <li>
 *     <ul>Payment status</ul>
 *     <ul>Save card checked mode</ul>
 *     <ul>Color theme</ul>
 *     <ul>Font style</ul>
 * </li>
 *
 * @author ziahaqi
 */
public class CustomSetting {
    private boolean showPaymentStatus = true;
    private boolean saveCardChecked;
    private boolean animationEnabled = true;

    /**
     * Get payment status enabled mode.
     * If true, then payment status page will be shown.
     *
     * @return payment status enabled mode.
     */
    public boolean isShowPaymentStatus() {
        return showPaymentStatus;
    }

    /**
     * Set payment status enabled mode.
     *
     * @param showPaymentStatus payment status enabled mode.
     */
    public void setShowPaymentStatus(boolean showPaymentStatus) {
        this.showPaymentStatus = showPaymentStatus;
    }

    /**
     * Get save card checked mode.
     * If true, then save card checkbox will be checked by default.
     *
     * @return save card checked mode.
     */
    public boolean isSaveCardChecked() {
        return saveCardChecked;
    }

    /**
     * Set save card checked mode.
     *
     * @param saveCardChecked save card checked mode.
     */
    public void setSaveCardChecked(boolean saveCardChecked) {
        this.saveCardChecked = saveCardChecked;
    }

    public boolean isAnimationEnabled() {
        return animationEnabled;
    }

    public void setAnimationEnabled(boolean animationEnabled) {
        this.animationEnabled = animationEnabled;
    }
}
