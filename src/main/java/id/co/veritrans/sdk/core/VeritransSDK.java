package id.co.veritrans.sdk.core;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;

/**
 * Created by shivam on 10/19/15.
 */
 public class VeritransSDK {

    private static final String FONTS_ROBOTO_BOLD_TTF = "fonts/roboto_bold.ttf";
    private static final String FONTS_ROBOTO_REGULAR_TTF = "fonts/roboto_regular.ttf";
    private static final String FONTS_ROBOTO_LIGHT_TTF = "fonts/roboto_light.ttf";
    private static Context sContext = null;

    private static Typeface typefaceRobotoRegular = null;
    private static Typeface typefaceRobotoLight = null;
    private static Typeface typefaceRobotoBold = null;
    private static VeritransSDK sVeritransSDK = new VeritransSDK();
    private boolean isLogEnabled = true;

    private static String sOrderId = null;
    private static double sAmount = 0.0;
    private static int sPaymentMethod = Constants.PAYMENT_METHOD_NOT_SELECTED;
    private static boolean isRunning = false;

    private VeritransSDK(){
    }


    protected static VeritransSDK getInstance(VeritransBuilder veritransBuilder){

        if(veritransBuilder != null){
            sContext = veritransBuilder.context;
            sOrderId = veritransBuilder.orderId;
            sAmount = veritransBuilder.amount;
            sPaymentMethod = veritransBuilder.paymentMethod;

            initializeFonts();
            return sVeritransSDK;
        }else{
            return null;
        }
    }

    private static void initializeFonts(){
        AssetManager assets = sContext.getAssets();
        typefaceRobotoBold = Typeface.createFromAsset(assets, FONTS_ROBOTO_BOLD_TTF);
        typefaceRobotoRegular = Typeface.createFromAsset(assets, FONTS_ROBOTO_REGULAR_TTF);
        typefaceRobotoLight = Typeface.createFromAsset(assets, FONTS_ROBOTO_LIGHT_TTF);
    }

    public Typeface getTypefaceRobotoRegular() {
        return typefaceRobotoRegular;
    }

    public Typeface getTypefaceRobotoLight() {
        return typefaceRobotoLight;
    }

    public Typeface getTypefaceRobotoBold() {
        return typefaceRobotoBold;
    }

    public static VeritransSDK getVeritransSDK(){
        return sVeritransSDK;
    }

    public static boolean isRunning() {
        return isRunning;
    }

    protected static void setIsRunning(boolean isRunning) {
        VeritransSDK.isRunning = isRunning;
    }


    public boolean isLogEnabled() {
        return isLogEnabled;
    }

    public void enableLog(boolean enableLog) {
        this.isLogEnabled = enableLog;
    }
}
