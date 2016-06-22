package id.co.veritrans.sdk.uiflow;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import id.co.veritrans.sdk.coreflow.core.ISdkFlow;
import id.co.veritrans.sdk.coreflow.core.VeritransSDK;
import id.co.veritrans.sdk.uiflow.activities.SaveCreditCardActivity;
import id.co.veritrans.sdk.uiflow.activities.UserDetailsActivity;

/**
 * Created by ziahaqi on 15/06/2016.
 */
public class UIFlow implements ISdkFlow {

    @Override
    public void runUIFlow(Context context) {
        VeritransSDK sdk = VeritransSDK.getVeritransSDK();
        if(sdk != null){
            context.startActivity(new Intent(context,
                    UserDetailsActivity.class));
        }
    }

    @Override
    public void runRegisterCard(Context context) {
        Intent  cardRegister = new Intent(context,
                SaveCreditCardActivity.class);
        context.startActivity(cardRegister);
    }
}
