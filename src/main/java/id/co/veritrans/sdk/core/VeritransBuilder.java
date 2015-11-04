package id.co.veritrans.sdk.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import id.co.veritrans.sdk.activities.UserDetailsActivity;

/**
 * Created by shivam on 10/20/15.
 * <p/>
 * helper class to create object of veritrans sdk class.
 */
public class VeritransBuilder {

    protected String orderId;
    protected double amount;
    protected int paymentMethod = Constants.PAYMENT_METHOD_NOT_SELECTED;
    protected String serverKey = null;
    protected String clientKey = null;
    protected Context context;
    protected boolean useUi = true;
    protected Activity mActivity = null;
    protected boolean enableLog = true;
    protected String cardClickType = Constants.CARD_CLICK_TYPE_NONE;
    protected boolean isSecureCard = true;

    /**
     * It  will initialize an data required to sdk.
     *
     * @param activity
     * @param orderId
     * @param amount
     * @param clientKey client key retrieved from veritrans server.
     * @param serverKey server key retrieved from veritrans server.
     */
    public VeritransBuilder(Activity activity, String orderId, String clientKey,
                            String serverKey, double amount) {

        if (activity != null && orderId != null && amount > 0.0
                && clientKey != null && serverKey != null) {

            this.mActivity = activity;
            this.context = activity.getApplicationContext();
            this.orderId = orderId;
            this.amount = amount;
            this.clientKey = clientKey;
            this.serverKey = serverKey;

        } else {
            throw new IllegalArgumentException("Invalid data supplied to sdk.");
        }
    }

    /**
     * set payment using which user wants to perform transaction.
     * use payment methods from {@link id.co.veritrans.sdk.core.Constants}
     *
     * @param paymentMethod
     * @return object of VeritransBuilder
     */
    public VeritransBuilder setPaymentMethod(int paymentMethod) {
        this.paymentMethod = paymentMethod;
        return this;
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
     * It will help to enable/disable default ui provided by sdk.
     * By default it is true, set it to false to use your own ui to show transaction.
     *
     * @param enableUi
     */
    public void enableUi(boolean enableUi) {
        this.useUi = enableUi;
    }


    /**
     * This method will start payment flow if you have set useUi field to true.
     *
     * @return it returns fully initialized object of veritrans sdk.
     */
    public VeritransSDK buildSDK() {

        if (!VeritransSDK.isRunning()) {

            VeritransSDK veritransSDK = VeritransSDK.getInstance(this);

            if (veritransSDK != null) {
                veritransSDK.setIsRunning(true);

                if (useUi) {
                    mActivity.startActivity(new Intent(mActivity, UserDetailsActivity.class));
                }
                return veritransSDK;
            }
        } else {
            Logger.e("already performing an transaction");
        }
        return null;
    }

    public void setCardPaymentInfo(String clickType,boolean isSecureCard) {
        Logger.i("clicktype:"+clickType+",isSecured:"+isSecureCard);
        this.cardClickType = clickType;
        this.isSecureCard = isSecureCard;
    }
}