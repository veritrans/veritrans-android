package com.midtrans.sdk.uikit;

import android.content.Context;
import android.content.Intent;

import com.midtrans.sdk.corekit.core.ISdkFlow;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.uikit.activities.SaveCreditCardActivity;
import com.midtrans.sdk.uikit.activities.UserDetailsActivity;

/**
 * Created by ziahaqi on 15/06/2016.
 */
public class UIFlow implements ISdkFlow {

    @Override
    public void runUIFlow(Context context) {
        MidtransSDK sdk = MidtransSDK.getInstance();
        if (sdk != null) {
            context.startActivity(new Intent(context,
                    UserDetailsActivity.class));
        }
    }

    @Override
    public void runRegisterCard(Context context) {
        Intent cardRegister = new Intent(context,
                SaveCreditCardActivity.class);
        context.startActivity(cardRegister);
    }
}
