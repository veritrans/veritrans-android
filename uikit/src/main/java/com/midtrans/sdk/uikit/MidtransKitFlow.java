package com.midtrans.sdk.uikit;

import android.app.Activity;
import android.content.Intent;

import com.midtrans.sdk.corekit.base.enums.PaymentType;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.CheckoutTransaction;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.PaymentResponse;
import com.midtrans.sdk.uikit.base.callback.PaymentResult;
import com.midtrans.sdk.uikit.utilities.MidtransKitHelper;
import com.midtrans.sdk.uikit.view.PaymentListActivity;

public class MidtransKitFlow {

    public static final String INTENT_EXTRA_CALLBACK = "intent.extra.callback";
    public static final String INTENT_EXTRA_TOKEN = "intent.extra.token";
    public static final String INTENT_EXTRA_TRANSACTION = "intent.extra.transaction";
    public static final String INTENT_EXTRA_DIRECT = "intent.extra.direct";
    public static final String INTENT_EXTRA_ALL_PAYMENT = "intent.extra.all_payment";
    public static final String INTENT_EXTRA_CREDIT_CARD_ONLY = "intent.extra.cconly";
    public static final String INTENT_EXTRA_BANK_TRANSFER_ONLY = "intent.extra.btonly";
    public static final String INTENT_EXTRA_BANK_TRANSFER_MANDIRI = "intent.extra.bt_mandiri";
    public static final String INTENT_EXTRA_BANK_TRANSFER_BCA = "intent.extra.bt_bca";
    public static final String INTENT_EXTRA_BANK_TRANSFER_PERMATA = "intent.extra.bt_permata";
    public static final String INTENT_EXTRA_BANK_TRANSFER_BNI = "intent.extra.bt_bni";
    public static final String INTENT_EXTRA_BANK_TRANSFER_OTHER = "intent.extra.bt_other";
    public static final String INTENT_EXTRA_GOPAY = "intent.extra.gopay";
    public static final String INTENT_EXTRA_BCA_KLIKPAY = "intent.extra.bcaklikpay";
    public static final String INTENT_EXTRA_KLIK_BCA = "intent.extra.klikbca";
    public static final String INTENT_EXTRA_MANDIRI_CLICKPAY = "intent.extra.mandiriclickpay";
    public static final String INTENT_EXTRA_MANDIRI_ECASH = "intent.extra.mandiriecash";
    public static final String INTENT_EXTRA_CIMB_CLICKS = "intent.extra.cimbclicks";
    public static final String INTENT_EXTRA_BRI_EPAY = "intent.extra.briepay";
    public static final String INTENT_EXTRA_TELKOMSEL_CASH = "intent.extra.tcash";
    public static final String INTENT_EXTRA_INDOSAT_DOMPETKU = "intent.extra.indosatdompetku";
    public static final String INTENT_EXTRA_INDOMARET = "intent.extra.indomaret";
    public static final String INTENT_EXTRA_DANAMON_ONLINE = "intent.extra.danamon_online";
    public static final String INTENT_EXTRA_AKULAKU = "intent.extra.akulaku";
    public static final String INTENT_EXTRA_ALFAMART = "intent.extra.alfamart";

    static void paymentWithTransactionFlow(
            Activity context,
            CheckoutTransaction checkoutTransaction,
            PaymentResult<PaymentResponse> callback
    ) {
        if (MidtransKitHelper.isValidForStartMidtransKit(context, checkoutTransaction, callback)) {
            Intent intent = new Intent(context, PaymentListActivity.class);
            intent.putExtra(INTENT_EXTRA_CALLBACK, callback);
            intent.putExtra(INTENT_EXTRA_TRANSACTION, checkoutTransaction);
            intent.putExtra(INTENT_EXTRA_ALL_PAYMENT, true);
            context.startActivity(intent);
        }
    }

    static void paymentWithTokenFlow(
            Activity context,
            String token,
            PaymentResult<PaymentResponse> callback
    ) {
        if (MidtransKitHelper.isValidForStartMidtransKit(context, token, callback)) {
            Intent intent = new Intent(context, PaymentListActivity.class);
            intent.putExtra(INTENT_EXTRA_CALLBACK, callback);
            intent.putExtra(INTENT_EXTRA_TOKEN, token);
            intent.putExtra(INTENT_EXTRA_ALL_PAYMENT, true);
            context.startActivity(intent);
        }
    }

    static <T> void directPaymentWithTransactionFlow(
            Activity context,
            CheckoutTransaction checkoutTransaction,
            @PaymentType String paymentType,
            PaymentResult<T> callback
    ) {
        if (MidtransKitHelper.isValidForStartMidtransKit(context, checkoutTransaction, callback)) {
            Intent intent = new Intent(context, PaymentListActivity.class);
            intent.putExtra(INTENT_EXTRA_CALLBACK, callback);
            intent.putExtra(INTENT_EXTRA_TRANSACTION, checkoutTransaction);
            context.startActivity(intentDirectWithMapping(intent, paymentType));
        }
    }

    static <T> void directPaymentWithTokenFlow(
            Activity context,
            String token,
            @PaymentType String paymentType,
            PaymentResult<T> callback
    ) {
        if (MidtransKitHelper.isValidForStartMidtransKit(context, token, callback)) {
            Intent intent = new Intent(context, PaymentListActivity.class);
            intent.putExtra(INTENT_EXTRA_CALLBACK, callback);
            intent.putExtra(INTENT_EXTRA_TOKEN, token);
            context.startActivity(intentDirectWithMapping(intent, paymentType));
        }
    }

    private static Intent intentDirectWithMapping(Intent intent, @PaymentType String paymentType) {
        intent.putExtra(INTENT_EXTRA_DIRECT, paymentType);
        switch (paymentType) {
            case PaymentType.CREDIT_CARD:
                intent.putExtra(INTENT_EXTRA_CREDIT_CARD_ONLY, true);
                break;
            case PaymentType.BCA_VA:
                intent.putExtra(INTENT_EXTRA_BANK_TRANSFER_ONLY, true);
                intent.putExtra(INTENT_EXTRA_BANK_TRANSFER_BCA, true);
                break;
            case PaymentType.BNI_VA:
                intent.putExtra(INTENT_EXTRA_BANK_TRANSFER_ONLY, true);
                intent.putExtra(INTENT_EXTRA_BANK_TRANSFER_BNI, true);
                break;
            case PaymentType.PERMATA_VA:
                intent.putExtra(INTENT_EXTRA_BANK_TRANSFER_ONLY, true);
                intent.putExtra(INTENT_EXTRA_BANK_TRANSFER_PERMATA, true);
                break;
            case PaymentType.OTHER_VA:
                intent.putExtra(INTENT_EXTRA_BANK_TRANSFER_ONLY, true);
                intent.putExtra(INTENT_EXTRA_BANK_TRANSFER_OTHER, true);
                break;
            case PaymentType.GOPAY:
                intent.putExtra(INTENT_EXTRA_GOPAY, true);
                break;
            case PaymentType.BCA_KLIKPAY:
                intent.putExtra(INTENT_EXTRA_BCA_KLIKPAY, true);
                break;
            case PaymentType.KLIK_BCA:
                intent.putExtra(INTENT_EXTRA_KLIK_BCA, true);
                break;
            case PaymentType.MANDIRI_CLICKPAY:
                intent.putExtra(INTENT_EXTRA_MANDIRI_CLICKPAY, true);
                break;
            case PaymentType.MANDIRI_ECASH:
                intent.putExtra(INTENT_EXTRA_MANDIRI_ECASH, true);
                break;
            case PaymentType.CIMB_CLICKS:
                intent.putExtra(INTENT_EXTRA_CIMB_CLICKS, true);
                break;
            case PaymentType.BRI_EPAY:
                intent.putExtra(INTENT_EXTRA_BRI_EPAY, true);
                break;
            case PaymentType.TELKOMSEL_CASH:
                intent.putExtra(INTENT_EXTRA_TELKOMSEL_CASH, true);
                break;
            case PaymentType.INDOMARET:
                intent.putExtra(INTENT_EXTRA_INDOMARET, true);
                break;
            case PaymentType.DANAMON_ONLINE:
                intent.putExtra(INTENT_EXTRA_DANAMON_ONLINE, true);
                break;
            case PaymentType.AKULAKU:
                intent.putExtra(INTENT_EXTRA_AKULAKU, true);
                break;
            case PaymentType.ALFAMART:
                intent.putExtra(INTENT_EXTRA_ALFAMART, true);
                break;
        }
        return intent;
    }

}