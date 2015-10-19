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


    private VeritransSDK(){
    }


    public static VeritransSDK getInstance(Context context){

        if(context != null){
            sContext = context;
            initializeFonts();
            return sVeritransSDK;
        }else{
            throw new IllegalArgumentException("Context cann't be null.");
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
}
