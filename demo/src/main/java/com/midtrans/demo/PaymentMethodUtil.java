package com.midtrans.demo;

import android.content.Context;

import com.midtrans.sdk.ui.constants.PaymentType;
import com.midtrans.sdk.ui.models.PaymentMethodModel;

import java.util.ArrayList;
import java.util.List;

import static com.midtrans.sdk.ui.constants.PaymentType.BANK_TRANSFER;
import static com.midtrans.sdk.ui.constants.PaymentType.BCA_KLIKPAY;
import static com.midtrans.sdk.ui.constants.PaymentType.BRI_EPAY;
import static com.midtrans.sdk.ui.constants.PaymentType.CIMB_CLICKS;
import static com.midtrans.sdk.ui.constants.PaymentType.CREDIT_CARD;
import static com.midtrans.sdk.ui.constants.PaymentType.GCI;
import static com.midtrans.sdk.ui.constants.PaymentType.INDOMARET;
import static com.midtrans.sdk.ui.constants.PaymentType.INDOSAT_DOMPETKU;
import static com.midtrans.sdk.ui.constants.PaymentType.KIOSON;
import static com.midtrans.sdk.ui.constants.PaymentType.KLIK_BCA;
import static com.midtrans.sdk.ui.constants.PaymentType.MANDIRI_CLICKPAY;
import static com.midtrans.sdk.ui.constants.PaymentType.MANDIRI_ECASH;
import static com.midtrans.sdk.ui.constants.PaymentType.TELKOMSEL_CASH;
import static com.midtrans.sdk.ui.constants.PaymentType.XL_TUNAI;

/**
 * Created by ziahaqi on 6/2/17.
 */

public class PaymentMethodUtil {


    public static List<PaymentMethodModel> getDefaultPaymentMethods(Context context) {
        List<PaymentMethodModel> list = new ArrayList<>();
        for (String s : getDefaultPayments()) {
            PaymentMethodModel model = createPaymentMethodModel(context, s);
            if (model != null) {
                list.add(model);
            }
        }
        return list;
    }


    public static List<String> getDefaultPayments() {
        List<String> list = new ArrayList<>();
        list.add(PaymentType.CREDIT_CARD);
        list.add(PaymentType.PERMATA_VA);
        list.add(PaymentType.BNI_VA);
        list.add(PaymentType.OTHER_VA);
        list.add(PaymentType.KLIK_BCA);
        list.add(PaymentType.BCA_KLIKPAY);
        list.add(PaymentType.CIMB_CLICKS);
        list.add(PaymentType.MANDIRI_CLICKPAY);
        list.add(PaymentType.BRI_EPAY);
        list.add(PaymentType.TELKOMSEL_CASH);
        list.add(PaymentType.XL_TUNAI);
        list.add(PaymentType.BBM_MONEY);
        list.add(PaymentType.MANDIRI_ECASH);
        list.add(PaymentType.E_CHANNEL);
        list.add(PaymentType.INDOMARET);
        list.add(PaymentType.INDOSAT_DOMPETKU);
        list.add(PaymentType.KIOSON);
        list.add(PaymentType.GCI);

        return list;
    }

    public static PaymentMethodModel createPaymentMethodModel(Context context, String paymentType) {
        PaymentMethodModel paymentMethodModel = null;
        switch (paymentType) {
            case CREDIT_CARD:
                paymentMethodModel = new PaymentMethodModel(context.getString(com.midtrans.sdk.ui.R.string.payment_method_credit_card), context.getString(com.midtrans.sdk.ui.R.string.payment_method_description_credit_card_2), paymentType, 0);
                break;
            case BANK_TRANSFER:
                paymentMethodModel = new PaymentMethodModel(context.getString(com.midtrans.sdk.ui.R.string.payment_method_bank_transfer), context.getString(com.midtrans.sdk.ui.R.string.payment_method_description_bank_transfer), paymentType, 0);
                break;
            case BCA_KLIKPAY:
                paymentMethodModel = new PaymentMethodModel(context.getString(com.midtrans.sdk.ui.R.string.payment_method_bca_klikpay), context.getString(com.midtrans.sdk.ui.R.string.payment_method_description_bca_klikpay), paymentType, 0);
                break;
            case KLIK_BCA:
                paymentMethodModel = new PaymentMethodModel(context.getString(com.midtrans.sdk.ui.R.string.payment_method_klik_bca), context.getString(com.midtrans.sdk.ui.R.string.payment_method_description_klik_bca), paymentType, 0);
                break;
            case BRI_EPAY:
                paymentMethodModel = new PaymentMethodModel(context.getString(com.midtrans.sdk.ui.R.string.payment_method_bri_epay), context.getString(com.midtrans.sdk.ui.R.string.payment_method_description_epay_bri), paymentType, 0);
                break;
            case CIMB_CLICKS:
                paymentMethodModel = new PaymentMethodModel(context.getString(com.midtrans.sdk.ui.R.string.payment_method_cimb_clicks), context.getString(com.midtrans.sdk.ui.R.string.payment_method_description_cimb_clicks), paymentType, 0);
                break;
            case MANDIRI_CLICKPAY:
                paymentMethodModel = new PaymentMethodModel(context.getString(com.midtrans.sdk.ui.R.string.payment_method_mandiri_clickpay), context.getString(com.midtrans.sdk.ui.R.string.payment_method_description_mandiri_clickpay), paymentType, 0);
                break;
            case INDOMARET:
                paymentMethodModel = new PaymentMethodModel(context.getString(com.midtrans.sdk.ui.R.string.payment_method_indomaret), context.getString(com.midtrans.sdk.ui.R.string.payment_method_description_indomaret), paymentType, 0);
                break;
            case KIOSON:
                paymentMethodModel = new PaymentMethodModel(context.getString(com.midtrans.sdk.ui.R.string.payment_method_kioson), context.getString(com.midtrans.sdk.ui.R.string.payment_method_description_kioson), paymentType, 0);
                break;
            case TELKOMSEL_CASH:
                paymentMethodModel = new PaymentMethodModel(context.getString(com.midtrans.sdk.ui.R.string.payment_method_telkomsel_cash), context.getString(com.midtrans.sdk.ui.R.string.payment_method_description_telkomsel_cash), paymentType, 0);
                break;
            case MANDIRI_ECASH:
                paymentMethodModel = new PaymentMethodModel(context.getString(com.midtrans.sdk.ui.R.string.payment_method_mandiri_ecash), context.getString(com.midtrans.sdk.ui.R.string.payment_method_description_mandiri_ecash), paymentType, 0);
                break;
            case INDOSAT_DOMPETKU:
                paymentMethodModel = new PaymentMethodModel(context.getString(com.midtrans.sdk.ui.R.string.payment_method_indosat_dompetku), context.getString(com.midtrans.sdk.ui.R.string.payment_method_description_indosat_dompetku), paymentType, 0);
                break;
            case XL_TUNAI:
                paymentMethodModel = new PaymentMethodModel(context.getString(com.midtrans.sdk.ui.R.string.payment_method_xl_tunai), context.getString(com.midtrans.sdk.ui.R.string.payment_method_description_xl_tunai), paymentType, 0);
                break;
            case GCI:
                paymentMethodModel = new PaymentMethodModel(context.getString(com.midtrans.sdk.ui.R.string.payment_method_gci), context.getString(com.midtrans.sdk.ui.R.string.payment_method_description_gci), paymentType, 0);
                break;
        }

        return paymentMethodModel;
    }
}
