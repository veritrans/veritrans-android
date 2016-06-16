package id.co.veritrans.sdk.uiflow;

import android.app.Activity;
import android.content.Intent;

import id.co.veritrans.sdk.coreflow.core.VeritransSDK;
import id.co.veritrans.sdk.uiflow.activities.SaveCreditCardActivity;
import id.co.veritrans.sdk.uiflow.activities.UserDetailsActivity;

/**
 * Created by HQ on 15/06/2016.
 */
public class UIFlowSetup {

    public static boolean runUIFlowPayment(Activity activity){
        VeritransSDK sdk = VeritransSDK.getVeritransSDK();
        if(sdk != null){
            activity.startActivity(new Intent(activity,
                    UserDetailsActivity.class));
        }
        return false;
    }


    public static void initUIFlowCardRegister(Activity activity) {
        Intent  cardRegister = new Intent(activity,
                SaveCreditCardActivity.class);
        activity.startActivity(cardRegister);
    }
}
