package id.co.veritrans.sdk.core;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;

/**
 * Created by shivam on 10/19/15.
 */
public class VeritransSDK {

    private static final String FONTS_OPEN_SANS_BOLD_TTF = "fonts/open_sans_bold.ttf";
    private static final String FONTS_OPEN_SANS_REGULAR_TTF = "fonts/open_sans_regular.ttf";
    private static final String FONTS_OPEN_SANS_SEMI_BOLD_TTF = "fonts/open_sans_semibold.ttf";
    private static Context sContext = null;

    private static Typeface typefaceOpenSansRegular = null;
    private static Typeface typefaceOpenSansSemiBold = null;
    private static Typeface typefaceOpenSansBold = null;

    private static VeritransSDK sVeritransSDK = new VeritransSDK();
    private static boolean sIsLogEnabled = true;

    private static String sOrderId = null;
    private static double sAmount = 0.0;
    private static int sPaymentMethod = Constants.PAYMENT_METHOD_NOT_SELECTED;
    private static boolean isRunning = false;

    private VeritransSDK() {
    }


    protected static VeritransSDK getInstance(VeritransBuilder veritransBuilder) {

        if (veritransBuilder != null) {
            sContext = veritransBuilder.context;
            sOrderId = veritransBuilder.orderId;
            sAmount = veritransBuilder.amount;
            sPaymentMethod = veritransBuilder.paymentMethod;
            sIsLogEnabled = veritransBuilder.enableLog;
            initializeFonts();
            return sVeritransSDK;
        } else {
            return null;
        }
    }

    private static void initializeFonts() {
        AssetManager assets = sContext.getAssets();
        typefaceOpenSansBold = Typeface.createFromAsset(assets, FONTS_OPEN_SANS_BOLD_TTF);
        typefaceOpenSansRegular = Typeface.createFromAsset(assets, FONTS_OPEN_SANS_REGULAR_TTF);
        typefaceOpenSansSemiBold = Typeface.createFromAsset(assets,
                FONTS_OPEN_SANS_SEMI_BOLD_TTF);
    }


    public static Typeface getTypefaceOpenSansRegular() {
        return typefaceOpenSansRegular;
    }

    public static Typeface getTypefaceOpenSansSemiBold() {
        return typefaceOpenSansSemiBold;
    }

    public static Typeface getTypefaceOpenSansBold() {
        return typefaceOpenSansBold;
    }

    public static VeritransSDK getVeritransSDK() {
        return sVeritransSDK;
    }

    public static boolean isRunning() {
        return isRunning;
    }

    protected static void setIsRunning(boolean isRunning) {
        VeritransSDK.isRunning = isRunning;
    }


    public boolean isLogEnabled() {
        return sIsLogEnabled;
    }

    public void enableLog(boolean enableLog) {
        this.sIsLogEnabled = enableLog;
    }
}
