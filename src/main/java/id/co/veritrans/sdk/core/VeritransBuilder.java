package id.co.veritrans.sdk.core;

import android.content.Context;

/**
 * Created by shivam on 10/20/15.
 * <p/>
 * helper class to create object of veritrans sdk.
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

    /**
     * It  will initialize an data required to sdk.
     *
     * @param context

     */
    public VeritransBuilder(Context context,String clientKey) {
            this.context = context.getApplicationContext();
            this.clientKey = clientKey;
    }


    /**
     * controls the log of sdk. Log can help you to debug application.
     * set false to disable log of sdk, by default logs are on.
     *
     * @param enableLog
     * @return object of VeritransBuilder
     */
    public VeritransBuilder enableLog(boolean enableLog) {
        this.enableLog = enableLog;
        return this;
    }


    /**
     * This method will start payment flow if you have set useUi field to true.
     *
     * @return it returns fully initialized object of veritrans sdk.
     */
    public VeritransSDK buildSDK() {

        if (VeritransSDK.getVeritransSDK() == null) {

            VeritransSDK veritransSDK = VeritransSDK.getInstance(this);
            return veritransSDK;

        } else {
            Logger.e("already performing an transaction");
        }
        return null;
    }

}