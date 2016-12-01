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

    @Override
    public void runCreditCard(Context context) {
        MidtransSDK sdk = MidtransSDK.getInstance();
        if (sdk != null) {
            Intent intent = new Intent(context, UserDetailsActivity.class);
            intent.putExtra(UserDetailsActivity.CREDIT_CARD_ONLY, true);
            context.startActivity(intent);
        }
    }

    @Override
    public void runBankTransfer(Context context) {
        MidtransSDK sdk = MidtransSDK.getInstance();
        if (sdk != null) {
            Intent intent = new Intent(context, UserDetailsActivity.class);
            intent.putExtra(UserDetailsActivity.BANK_TRANSFER_ONLY, true);
            context.startActivity(intent);
        }
    }

    @Override
    public void runPermataBankTransfer(Context context) {
        MidtransSDK sdk = MidtransSDK.getInstance();
        if (sdk != null) {
            Intent intent = new Intent(context, UserDetailsActivity.class);
            intent.putExtra(UserDetailsActivity.BANK_TRANSFER_ONLY, true);
            intent.putExtra(UserDetailsActivity.BANK_TRANSFER_PERMATA, true);
            context.startActivity(intent);
        }
    }

    @Override
    public void runMandiriBankTransfer(Context context) {
        MidtransSDK sdk = MidtransSDK.getInstance();
        if (sdk != null) {
            Intent intent = new Intent(context, UserDetailsActivity.class);
            intent.putExtra(UserDetailsActivity.BANK_TRANSFER_ONLY, true);
            intent.putExtra(UserDetailsActivity.BANK_TRANSFER_MANDIRI, true);
            context.startActivity(intent);
        }
    }

    @Override
    public void runBCABankTransfer(Context context) {
        MidtransSDK sdk = MidtransSDK.getInstance();
        if (sdk != null) {
            Intent intent = new Intent(context, UserDetailsActivity.class);
            intent.putExtra(UserDetailsActivity.BANK_TRANSFER_ONLY, true);
            intent.putExtra(UserDetailsActivity.BANK_TRANSFER_BCA, true);
            context.startActivity(intent);
        }
    }

    @Override
    public void runOtherBankTransfer(Context context) {
        MidtransSDK sdk = MidtransSDK.getInstance();
        if (sdk != null) {
            Intent intent = new Intent(context, UserDetailsActivity.class);
            intent.putExtra(UserDetailsActivity.BANK_TRANSFER_ONLY, true);
            intent.putExtra(UserDetailsActivity.BANK_TRANSFER_OTHER, true);
            context.startActivity(intent);
        }
    }
}
