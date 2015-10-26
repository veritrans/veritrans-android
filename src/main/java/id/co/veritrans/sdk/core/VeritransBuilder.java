package id.co.veritrans.sdk.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import id.co.veritrans.sdk.activities.PaymentMethodsActivity;

/**
 * Created by shivam on 10/20/15.
 * <p/>
 * helper class to create object of veritrans sdk class.
 */
public class VeritransBuilder {

    protected String orderId;
    protected double amount;
    protected int paymentMethod = Constants.PAYMENT_METHOD_NOT_SELECTED;
    protected Context context;
    protected boolean useUi = false;
    protected Activity mActivity = null;
    protected boolean enableLog = true;

    /**
     * It  will
     *
     * @param activity
     * @param orderId
     * @param amount
     * @param useUi
     */
    public VeritransBuilder(Activity activity, String orderId, double amount, boolean useUi) {


        if (activity != null && orderId != null && amount > 0.0) {
            this.mActivity = activity;
            this.context = activity.getApplicationContext();
            this.orderId = orderId;
            this.amount = amount;
            this.useUi = useUi;
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
     * This method will start payment flow if you have set useUi field to true.
     *
     * @return it returns fully initialized object of veritrans sdk.
     */
    public VeritransSDK buildSDK() {

        if ( !VeritransSDK.isRunning() )  {

            VeritransSDK veritransSDK = VeritransSDK.getInstance(this);

            if (veritransSDK != null) {
                veritransSDK.setIsRunning(true);

                if (useUi) {
                    mActivity.startActivity(new Intent(mActivity, PaymentMethodsActivity.class));
                }
                return veritransSDK;
            }
        }else {
            Logger.e("already performing an transaction");
        }
            return null;

    }
}