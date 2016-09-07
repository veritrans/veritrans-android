package id.co.veritrans.sdk.core;

import android.content.Context;

/**
 * Created by shivam on 10/20/15.
 * <p/>
 * helper class to create object of midtrans sdk.
 * </p>
 * Call its constructor with activity , client_key and sever_key.
 *</p>
 * You can also enable or disable using {@link #enableLog(boolean)}
 */
public class VeritransBuilder {

    /*protected String serverKey = null;*/
    protected String clientKey = null;
    protected Context context = null;
    protected boolean enableLog = true;
    protected String merchantServerUrl = null;
    protected String colorTheme = null;
    protected ExternalScanner scanner = null;
    protected String merchantName = null;

    /**
     * It  will initialize an data required to sdk.
     *
     * @param context   application context

     */
    public VeritransBuilder(Context context, String clientKey, String merchantServerUrl) {
            this.context = context.getApplicationContext();
            this.clientKey = clientKey;
            this.merchantServerUrl = merchantServerUrl;
    }


    /**
     * controls the log of sdk. Log can help you to debug application.
     * set false to disable log of sdk, by default logs are on.
     *
     * @param enableLog     is log enabled
     * @return object of VeritransBuilder
     */
    public VeritransBuilder enableLog(boolean enableLog) {
        this.enableLog = enableLog;
        return this;
    }

    /**
     * Set color theme using formatted hex
     *
     * @param colorTheme formatted hex color code
     * @return VeritransBuilder instance.
     */
    public VeritransBuilder setColorTheme(String colorTheme) {
        this.colorTheme = colorTheme;
        return this;
    }

    /**
     * Set external scanner.
     * @param externalScanner   external scanner
     * @return VeritransBuilder instance
     */
    public VeritransBuilder setExternalScanner(ExternalScanner externalScanner) {
        this.scanner = externalScanner;
        return this;
    }

    /**
     * Set merchant name.
     *
     * @param merchantName merchant name
     * @return VeritransBuilder instance
     */
    public VeritransBuilder setMerchantName(String merchantName) {
        this.merchantName = merchantName;
        return this;
    }

    /**
     * This method will start payment flow if you have set useUi field to true.
     *
     * @return it returns fully initialized object of midtrans sdk.
     */
    public VeritransSDK buildSDK() {

        if (VeritransSDK.getVeritransSDK() == null && isValidData()) {
            VeritransSDK veritransSDK = VeritransSDK.getInstance(this);
            return veritransSDK;

        } else {
            Logger.e("already performing an transaction");
        }
        return null;
    }

    public boolean isValidData() {

        if(merchantServerUrl == null || clientKey == null || context == null){
            Logger.e("invalid data supplied to sdk");
            return false;
        }

        return true;
    }
}