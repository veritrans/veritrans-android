package com.midtrans.sdk.uikit;

import android.content.Context;
import android.content.Intent;

import com.midtrans.sdk.corekit.core.ISdkFlow;
import com.midtrans.sdk.corekit.core.MidtransSDK;
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
    public void runBniBankTransfer(Context context) {
        MidtransSDK sdk = MidtransSDK.getInstance();
        if (sdk != null) {
            Intent intent = new Intent(context, UserDetailsActivity.class);
            intent.putExtra(UserDetailsActivity.BANK_TRANSFER_ONLY, true);
            intent.putExtra(UserDetailsActivity.BANK_TRANSFER_BNI, true);
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

    @Override
    public void runBCAKlikPay(Context context) {
        MidtransSDK sdk = MidtransSDK.getInstance();
        if (sdk != null) {
            Intent intent = new Intent(context, UserDetailsActivity.class);
            intent.putExtra(UserDetailsActivity.BCA_KLIKPAY, true);
            context.startActivity(intent);
        }
    }

    @Override
    public void runKlikBCA(Context context) {
        MidtransSDK sdk = MidtransSDK.getInstance();
        if (sdk != null) {
            Intent intent = new Intent(context, UserDetailsActivity.class);
            intent.putExtra(UserDetailsActivity.KLIK_BCA, true);
            context.startActivity(intent);
        }
    }

    @Override
    public void runMandiriClickpay(Context context) {
        MidtransSDK sdk = MidtransSDK.getInstance();
        if (sdk != null) {
            Intent intent = new Intent(context, UserDetailsActivity.class);
            intent.putExtra(UserDetailsActivity.MANDIRI_CLICKPAY, true);
            context.startActivity(intent);
        }
    }

    @Override
    public void runMandiriECash(Context context) {
        MidtransSDK sdk = MidtransSDK.getInstance();
        if (sdk != null) {
            Intent intent = new Intent(context, UserDetailsActivity.class);
            intent.putExtra(UserDetailsActivity.MANDIRI_ECASH, true);
            context.startActivity(intent);
        }
    }

    @Override
    public void runCIMBClicks(Context context) {
        MidtransSDK sdk = MidtransSDK.getInstance();
        if (sdk != null) {
            Intent intent = new Intent(context, UserDetailsActivity.class);
            intent.putExtra(UserDetailsActivity.CIMB_CLICKS, true);
            context.startActivity(intent);
        }
    }

    @Override
    public void runBRIEpay(Context context) {
        MidtransSDK sdk = MidtransSDK.getInstance();
        if (sdk != null) {
            Intent intent = new Intent(context, UserDetailsActivity.class);
            intent.putExtra(UserDetailsActivity.BRI_EPAY, true);
            context.startActivity(intent);
        }
    }

    @Override
    public void runTelkomselCash(Context context) {
        MidtransSDK sdk = MidtransSDK.getInstance();
        if (sdk != null) {
            Intent intent = new Intent(context, UserDetailsActivity.class);
            intent.putExtra(UserDetailsActivity.TELKOMSEL_CASH, true);
            context.startActivity(intent);
        }
    }

    @Override
    public void runIndosatDompetku(Context context) {
        MidtransSDK sdk = MidtransSDK.getInstance();
        if (sdk != null) {
            Intent intent = new Intent(context, UserDetailsActivity.class);
            intent.putExtra(UserDetailsActivity.INDOSAT_DOMPETKU, true);
            context.startActivity(intent);
        }
    }

    @Override
    public void runXlTunai(Context context) {
        MidtransSDK sdk = MidtransSDK.getInstance();
        if (sdk != null) {
            Intent intent = new Intent(context, UserDetailsActivity.class);
            intent.putExtra(UserDetailsActivity.XL_TUNAI, true);
            context.startActivity(intent);
        }
    }

    @Override
    public void runIndomaret(Context context) {
        MidtransSDK sdk = MidtransSDK.getInstance();
        if (sdk != null) {
            Intent intent = new Intent(context, UserDetailsActivity.class);
            intent.putExtra(UserDetailsActivity.INDOMARET, true);
            context.startActivity(intent);
        }
    }

    @Override
    public void runKioson(Context context) {
        MidtransSDK sdk = MidtransSDK.getInstance();
        if (sdk != null) {
            Intent intent = new Intent(context, UserDetailsActivity.class);
            intent.putExtra(UserDetailsActivity.KIOSON, true);
            context.startActivity(intent);
        }
    }

    @Override
    public void runGci(Context context) {
        MidtransSDK sdk = MidtransSDK.getInstance();
        if (sdk != null) {
            Intent intent = new Intent(context, UserDetailsActivity.class);
            intent.putExtra(UserDetailsActivity.GIFT_CARD, true);
            context.startActivity(intent);
        }
    }
}
