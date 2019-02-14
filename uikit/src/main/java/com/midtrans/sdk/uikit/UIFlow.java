package com.midtrans.sdk.uikit;

import android.content.Context;
import android.content.Intent;
import com.midtrans.sdk.corekit.callback.CardRegistrationCallback;
import com.midtrans.sdk.corekit.core.ISdkFlow;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.uikit.activities.UserDetailsActivity;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;
import com.midtrans.sdk.uikit.views.creditcard.register.CardRegistrationActivity;

/**
 * Created by ziahaqi on 15/06/2016.
 */
public class UIFlow implements ISdkFlow {

    @Override
    public void runUIFlow(Context context, String snapToken) {
        MidtransSDK sdk = MidtransSDK.getInstance();
        if (sdk != null) {
            Intent intent = new Intent(context, UserDetailsActivity.class);
            intent.putExtra(UiKitConstants.EXTRA_SNAP_TOKEN, snapToken);
            context.startActivity(intent);
        }
    }

    @Override
    public void runCreditCard(Context context, String snapToken) {
        MidtransSDK sdk = MidtransSDK.getInstance();
        if (sdk != null) {
            Intent intent = new Intent(context, UserDetailsActivity.class);
            intent.putExtra(UserDetailsActivity.CREDIT_CARD_ONLY, true);
            intent.putExtra(UiKitConstants.EXTRA_SNAP_TOKEN, snapToken);
            context.startActivity(intent);
        }
    }

    @Override
    public void runBankTransfer(Context context, String snapToken) {
        MidtransSDK sdk = MidtransSDK.getInstance();
        if (sdk != null) {
            Intent intent = new Intent(context, UserDetailsActivity.class);
            intent.putExtra(UserDetailsActivity.BANK_TRANSFER_ONLY, true);
            intent.putExtra(UiKitConstants.EXTRA_SNAP_TOKEN, snapToken);
            context.startActivity(intent);
        }
    }

    @Override
    public void runPermataBankTransfer(Context context, String snapToken) {
        MidtransSDK sdk = MidtransSDK.getInstance();
        if (sdk != null) {
            Intent intent = new Intent(context, UserDetailsActivity.class);
            intent.putExtra(UserDetailsActivity.BANK_TRANSFER_ONLY, true);
            intent.putExtra(UserDetailsActivity.BANK_TRANSFER_PERMATA, true);
            intent.putExtra(UiKitConstants.EXTRA_SNAP_TOKEN, snapToken);
            context.startActivity(intent);
        }
    }

    @Override
    public void runMandiriBankTransfer(Context context, String snapToken) {
        MidtransSDK sdk = MidtransSDK.getInstance();
        if (sdk != null) {
            Intent intent = new Intent(context, UserDetailsActivity.class);
            intent.putExtra(UserDetailsActivity.BANK_TRANSFER_ONLY, true);
            intent.putExtra(UserDetailsActivity.BANK_TRANSFER_MANDIRI, true);
            intent.putExtra(UiKitConstants.EXTRA_SNAP_TOKEN, snapToken);
            context.startActivity(intent);
        }
    }

    @Override
    public void runBniBankTransfer(Context context, String snapToken) {
        MidtransSDK sdk = MidtransSDK.getInstance();
        if (sdk != null) {
            Intent intent = new Intent(context, UserDetailsActivity.class);
            intent.putExtra(UserDetailsActivity.BANK_TRANSFER_ONLY, true);
            intent.putExtra(UserDetailsActivity.BANK_TRANSFER_BNI, true);
            intent.putExtra(UiKitConstants.EXTRA_SNAP_TOKEN, snapToken);
            context.startActivity(intent);
        }
    }

    @Override
    public void runBCABankTransfer(Context context, String snapToken) {
        MidtransSDK sdk = MidtransSDK.getInstance();
        if (sdk != null) {
            Intent intent = new Intent(context, UserDetailsActivity.class);
            intent.putExtra(UserDetailsActivity.BANK_TRANSFER_ONLY, true);
            intent.putExtra(UserDetailsActivity.BANK_TRANSFER_BCA, true);
            intent.putExtra(UiKitConstants.EXTRA_SNAP_TOKEN, snapToken);
            context.startActivity(intent);
        }
    }

    @Override
    public void runOtherBankTransfer(Context context, String snapToken) {
        MidtransSDK sdk = MidtransSDK.getInstance();
        if (sdk != null) {
            Intent intent = new Intent(context, UserDetailsActivity.class);
            intent.putExtra(UserDetailsActivity.BANK_TRANSFER_ONLY, true);
            intent.putExtra(UserDetailsActivity.BANK_TRANSFER_OTHER, true);
            intent.putExtra(UiKitConstants.EXTRA_SNAP_TOKEN, snapToken);
            context.startActivity(intent);
        }
    }

    @Override
    public void runGoPay(Context context, String snapToken) {
        MidtransSDK sdk = MidtransSDK.getInstance();
        if (sdk != null) {
            Intent intent = new Intent(context, UserDetailsActivity.class);
            intent.putExtra(UserDetailsActivity.GO_PAY, true);
            intent.putExtra(UiKitConstants.EXTRA_SNAP_TOKEN, snapToken);
            context.startActivity(intent);
        }
    }

    @Override
    public void runBCAKlikPay(Context context, String snapToken) {
        MidtransSDK sdk = MidtransSDK.getInstance();
        if (sdk != null) {
            Intent intent = new Intent(context, UserDetailsActivity.class);
            intent.putExtra(UserDetailsActivity.BCA_KLIKPAY, true);
            intent.putExtra(UiKitConstants.EXTRA_SNAP_TOKEN, snapToken);
            context.startActivity(intent);
        }
    }

    @Override
    public void runKlikBCA(Context context, String snapToken) {
        MidtransSDK sdk = MidtransSDK.getInstance();
        if (sdk != null) {
            Intent intent = new Intent(context, UserDetailsActivity.class);
            intent.putExtra(UserDetailsActivity.KLIK_BCA, true);
            intent.putExtra(UiKitConstants.EXTRA_SNAP_TOKEN, snapToken);
            context.startActivity(intent);
        }
    }

    @Override
    public void runMandiriClickpay(Context context, String snapToken) {
        MidtransSDK sdk = MidtransSDK.getInstance();
        if (sdk != null) {
            Intent intent = new Intent(context, UserDetailsActivity.class);
            intent.putExtra(UserDetailsActivity.MANDIRI_CLICKPAY, true);
            intent.putExtra(UiKitConstants.EXTRA_SNAP_TOKEN, snapToken);
            context.startActivity(intent);
        }
    }

    @Override
    public void runMandiriECash(Context context, String snapToken) {
        MidtransSDK sdk = MidtransSDK.getInstance();
        if (sdk != null) {
            Intent intent = new Intent(context, UserDetailsActivity.class);
            intent.putExtra(UserDetailsActivity.MANDIRI_ECASH, true);
            intent.putExtra(UiKitConstants.EXTRA_SNAP_TOKEN, snapToken);
            context.startActivity(intent);
        }
    }

    @Override
    public void runCIMBClicks(Context context, String snapToken) {
        MidtransSDK sdk = MidtransSDK.getInstance();
        if (sdk != null) {
            Intent intent = new Intent(context, UserDetailsActivity.class);
            intent.putExtra(UserDetailsActivity.CIMB_CLICKS, true);
            intent.putExtra(UiKitConstants.EXTRA_SNAP_TOKEN, snapToken);
            context.startActivity(intent);
        }
    }

    @Override
    public void runBRIEpay(Context context, String snapToken) {
        MidtransSDK sdk = MidtransSDK.getInstance();
        if (sdk != null) {
            Intent intent = new Intent(context, UserDetailsActivity.class);
            intent.putExtra(UserDetailsActivity.BRI_EPAY, true);
            intent.putExtra(UiKitConstants.EXTRA_SNAP_TOKEN, snapToken);
            context.startActivity(intent);
        }
    }

    @Override
    public void runTelkomselCash(Context context, String snapToken) {
        MidtransSDK sdk = MidtransSDK.getInstance();
        if (sdk != null) {
            Intent intent = new Intent(context, UserDetailsActivity.class);
            intent.putExtra(UserDetailsActivity.TELKOMSEL_CASH, true);
            intent.putExtra(UiKitConstants.EXTRA_SNAP_TOKEN, snapToken);
            context.startActivity(intent);
        }
    }

    @Override
    public void runIndosatDompetku(Context context, String snapToken) {
        MidtransSDK sdk = MidtransSDK.getInstance();
        if (sdk != null) {
            Intent intent = new Intent(context, UserDetailsActivity.class);
            intent.putExtra(UserDetailsActivity.INDOSAT_DOMPETKU, true);
            intent.putExtra(UiKitConstants.EXTRA_SNAP_TOKEN, snapToken);
            context.startActivity(intent);
        }
    }

    @Override
    public void runXlTunai(Context context, String snapToken) {
        MidtransSDK sdk = MidtransSDK.getInstance();
        if (sdk != null) {
            Intent intent = new Intent(context, UserDetailsActivity.class);
            intent.putExtra(UserDetailsActivity.XL_TUNAI, true);
            intent.putExtra(UiKitConstants.EXTRA_SNAP_TOKEN, snapToken);
            context.startActivity(intent);
        }
    }

    @Override
    public void runIndomaret(Context context, String snapToken) {
        MidtransSDK sdk = MidtransSDK.getInstance();
        if (sdk != null) {
            Intent intent = new Intent(context, UserDetailsActivity.class);
            intent.putExtra(UserDetailsActivity.INDOMARET, true);
            intent.putExtra(UiKitConstants.EXTRA_SNAP_TOKEN, snapToken);
            context.startActivity(intent);
        }
    }

    @Override
    public void runKioson(Context context, String snapToken) {
        MidtransSDK sdk = MidtransSDK.getInstance();
        if (sdk != null) {
            Intent intent = new Intent(context, UserDetailsActivity.class);
            intent.putExtra(UserDetailsActivity.KIOSON, true);
            intent.putExtra(UiKitConstants.EXTRA_SNAP_TOKEN, snapToken);
            context.startActivity(intent);
        }
    }

    @Override
    public void runGci(Context context, String snapToken) {
        MidtransSDK sdk = MidtransSDK.getInstance();
        if (sdk != null) {
            Intent intent = new Intent(context, UserDetailsActivity.class);
            intent.putExtra(UserDetailsActivity.GIFT_CARD, true);
            intent.putExtra(UiKitConstants.EXTRA_SNAP_TOKEN, snapToken);
            context.startActivity(intent);
        }
    }

    @Override
    public void runDanamonOnline(Context context, String tokenToken) {
        MidtransSDK sdk = MidtransSDK.getInstance();
        if (sdk != null) {
            Intent intent = new Intent(context, UserDetailsActivity.class);
            intent.putExtra(UserDetailsActivity.DANAMON_ONLINE, true);
            intent.putExtra(UiKitConstants.EXTRA_SNAP_TOKEN, tokenToken);
            context.startActivity(intent);
        }
    }

    @Override
    public void runAkulaku(Context context, String snapToken) {
        MidtransSDK sdk = MidtransSDK.getInstance();
        if (sdk != null) {
            Intent intent = new Intent(context, UserDetailsActivity.class);
            intent.putExtra(UserDetailsActivity.AKULAKU, true);
            intent.putExtra(UiKitConstants.EXTRA_SNAP_TOKEN, snapToken);
            context.startActivity(intent);
        }
    }

    @Override
    public void runAlfamart(Context context, String snapToken) {
        MidtransSDK sdk = MidtransSDK.getInstance();
        if (sdk != null) {
            Intent intent = new Intent(context, UserDetailsActivity.class);
            intent.putExtra(UserDetailsActivity.ALFAMART, true);
            intent.putExtra(UiKitConstants.EXTRA_SNAP_TOKEN, snapToken);
            context.startActivity(intent);
        }
    }

    @Override
    public void runCardRegistration(Context context, CardRegistrationCallback callback) {
        Intent intent = new Intent(context, CardRegistrationActivity.class);
        context.startActivity(intent);
    }
}
