package id.co.veritrans.sdk.core;

import android.content.Context;

/**
 * Created by shivam on 10/20/15.
 * <p/>
 * helper class to create object of veritrans sdk class.
 */
public class VeritransBuilder {

    protected String serverKey = null;
    protected String clientKey = null;
    protected Context context = null;
    protected boolean enableLog = true;

    /**
     * It  will initialize an data required to sdk.
     *
     * @param context
     * @param clientKey client key retrieved from veritrans server.
     * @param serverKey server key retrieved from veritrans server.
     */
    public VeritransBuilder(Context context, String clientKey,
                            String serverKey) {

        if (context != null
                && clientKey != null && serverKey != null) {

            this.context = context.getApplicationContext();
            this.clientKey = clientKey;
            this.serverKey = serverKey;

        } else {
            throw new IllegalArgumentException("Invalid data supplied to sdk.");
        }
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