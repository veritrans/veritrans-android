package com.midtrans.sdk.ui.utils;

import android.content.Context;

import com.midtrans.sdk.ui.R;
import com.midtrans.sdk.ui.models.PaymentMethodModel;

import static com.midtrans.sdk.ui.constants.PaymentType.BANK_TRANSFER;
import static com.midtrans.sdk.ui.constants.PaymentType.BCA_KLIKPAY;
import static com.midtrans.sdk.ui.constants.PaymentType.BCA_VA;
import static com.midtrans.sdk.ui.constants.PaymentType.BRI_EPAY;
import static com.midtrans.sdk.ui.constants.PaymentType.CIMB_CLICKS;
import static com.midtrans.sdk.ui.constants.PaymentType.CREDIT_CARD;
import static com.midtrans.sdk.ui.constants.PaymentType.E_CHANNEL;
import static com.midtrans.sdk.ui.constants.PaymentType.GCI;
import static com.midtrans.sdk.ui.constants.PaymentType.INDOMARET;
import static com.midtrans.sdk.ui.constants.PaymentType.INDOSAT_DOMPETKU;
import static com.midtrans.sdk.ui.constants.PaymentType.KIOSON;
import static com.midtrans.sdk.ui.constants.PaymentType.KLIK_BCA;
import static com.midtrans.sdk.ui.constants.PaymentType.MANDIRI_CLICKPAY;
import static com.midtrans.sdk.ui.constants.PaymentType.MANDIRI_ECASH;
import static com.midtrans.sdk.ui.constants.PaymentType.OTHER_VA;
import static com.midtrans.sdk.ui.constants.PaymentType.PERMATA_VA;
import static com.midtrans.sdk.ui.constants.PaymentType.TELKOMSEL_CASH;
import static com.midtrans.sdk.ui.constants.PaymentType.XL_TUNAI;

/**
 * Created by ziahaqi on 2/21/17.
 */

public class PaymentMethodHelper {


    public static PaymentMethodModel createPaymentMethodModel(Context context, String paymentType) {
        PaymentMethodModel paymentMethodModel = null;
        switch (paymentType) {
            case CREDIT_CARD:
                paymentMethodModel = new PaymentMethodModel(context.getString(R.string.payment_method_credit_card), context.getString(R.string.payment_method_description_credit_card), paymentType, R.mipmap.ic_credit_card);
                break;
            case BANK_TRANSFER:
                paymentMethodModel = new PaymentMethodModel(context.getString(R.string.payment_method_bank_transfer), context.getString(R.string.payment_method_description_bank_transfer), paymentType, R.mipmap.ic_atm);
                break;
            case BCA_KLIKPAY:
                paymentMethodModel = new PaymentMethodModel(context.getString(R.string.payment_method_bca_klikpay), context.getString(R.string.payment_method_description_bca_klikpay), paymentType, R.mipmap.ic_klikpay);
                break;
            case KLIK_BCA:
                paymentMethodModel = new PaymentMethodModel(context.getString(R.string.payment_method_klik_bca), context.getString(R.string.payment_method_description_klik_bca), paymentType, R.mipmap.ic_klikpay);
                break;
            case BRI_EPAY:
                paymentMethodModel = new PaymentMethodModel(context.getString(R.string.payment_method_bri_epay), context.getString(R.string.payment_method_description_epay_bri), paymentType, R.mipmap.ic_epay);
                break;
            case CIMB_CLICKS:
                paymentMethodModel = new PaymentMethodModel(context.getString(R.string.payment_method_cimb_clicks), context.getString(R.string.payment_method_description_cimb_clicks), paymentType, R.mipmap.ic_cimb);
                break;
            case MANDIRI_CLICKPAY:
                paymentMethodModel = new PaymentMethodModel(context.getString(R.string.payment_method_mandiri_clickpay), context.getString(R.string.payment_method_description_mandiri_clickpay), paymentType, R.mipmap.ic_mandiri_clickpay);
                break;
            case INDOMARET:
                paymentMethodModel = new PaymentMethodModel(context.getString(R.string.payment_method_indomaret), context.getString(R.string.payment_method_description_indomaret), paymentType, R.mipmap.ic_indomaret);
                break;
            case KIOSON:
                paymentMethodModel = new PaymentMethodModel(context.getString(R.string.payment_method_kioson), context.getString(R.string.payment_method_description_kioson), paymentType, R.mipmap.ic_kioson);
                break;
            case TELKOMSEL_CASH:
                paymentMethodModel = new PaymentMethodModel(context.getString(R.string.payment_method_telkomsel_cash), context.getString(R.string.payment_method_description_telkomsel_cash), paymentType, R.mipmap.ic_tcash);
                break;
            case MANDIRI_ECASH:
                paymentMethodModel = new PaymentMethodModel(context.getString(R.string.payment_method_mandiri_ecash), context.getString(R.string.payment_method_description_mandiri_ecash), paymentType, R.mipmap.ic_mandiri_ecash);
                break;
            case INDOSAT_DOMPETKU:
                paymentMethodModel = new PaymentMethodModel(context.getString(R.string.payment_method_indosat_dompetku), context.getString(R.string.payment_method_description_indosat_dompetku), paymentType, R.mipmap.ic_indosat_dompetku);
                break;
            case XL_TUNAI:
                paymentMethodModel = new PaymentMethodModel(context.getString(R.string.payment_method_xl_tunai), context.getString(R.string.payment_method_description_xl_tunai), paymentType, R.mipmap.ic_xl_tunai);
                break;
            case GCI:
                paymentMethodModel = new PaymentMethodModel(context.getString(R.string.payment_method_gci), context.getString(R.string.payment_method_description_gci), paymentType, R.mipmap.ic_gci);
                break;
        }

        return paymentMethodModel;
    }

    public static PaymentMethodModel createBankTransferPaymentMethod(Context context, String type) {
        PaymentMethodModel paymentMethodModel = null;
        switch (type) {
            case PERMATA_VA:
                break;
            case BCA_VA:

                break;
            case E_CHANNEL:

                break;
            case OTHER_VA:

                break;
        }
        return null;
    }
}
