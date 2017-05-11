package com.midtrans.sdk.ui.utils;

import android.content.Context;

import com.midtrans.sdk.core.models.CreditCardType;
import com.midtrans.sdk.ui.R;
import com.midtrans.sdk.ui.constants.PaymentType;
import com.midtrans.sdk.ui.models.PaymentMethodModel;

import java.util.ArrayList;
import java.util.List;

import static com.midtrans.sdk.ui.constants.PaymentType.BANK_TRANSFER;
import static com.midtrans.sdk.ui.constants.PaymentType.BCA_KLIKPAY;
import static com.midtrans.sdk.ui.constants.PaymentType.BCA_VA;
import static com.midtrans.sdk.ui.constants.PaymentType.BNI_VA;
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

public class PaymentMethodUtils {

    public static PaymentMethodModel createPaymentMethodModel(Context context, String paymentType) {
        PaymentMethodModel paymentMethodModel = null;
        switch (paymentType) {
            case CREDIT_CARD:
                int creditCardSupportType = UiUtils.getCreditCardIconType();
                switch (creditCardSupportType) {
                    case CreditCardType.TYPE_MASTER_VISA_JCB_AMEX:
                        paymentMethodModel = new PaymentMethodModel(context.getString(R.string.payment_method_credit_card), context.getString(R.string.payment_method_description_credit_card), paymentType, R.drawable.ic_credit);
                        break;
                    case CreditCardType.TYPE_MASTER_VISA_JCB:
                        paymentMethodModel = new PaymentMethodModel(context.getString(R.string.payment_method_credit_card), context.getString(R.string.payment_method_description_credit_card_3), paymentType, R.drawable.ic_credit_3);
                        break;
                    case CreditCardType.TYPE_MASTER_VISA_AMEX:
                        paymentMethodModel = new PaymentMethodModel(context.getString(R.string.payment_method_credit_card), context.getString(R.string.payment_method_description_credit_card_4), paymentType, R.drawable.ic_credit_4);
                        break;
                    default:
                        paymentMethodModel = new PaymentMethodModel(context.getString(R.string.payment_method_credit_card), context.getString(R.string.payment_method_description_credit_card_2), paymentType, R.drawable.ic_credit_2);
                        break;
                }
                break;
            case BANK_TRANSFER:
                paymentMethodModel = new PaymentMethodModel(context.getString(R.string.payment_method_bank_transfer), context.getString(R.string.payment_method_description_bank_transfer), paymentType, R.drawable.ic_atm);
                break;
            case BCA_KLIKPAY:
                paymentMethodModel = new PaymentMethodModel(context.getString(R.string.payment_method_bca_klikpay), context.getString(R.string.payment_method_description_bca_klikpay), paymentType, R.drawable.ic_klikpay);
                break;
            case KLIK_BCA:
                paymentMethodModel = new PaymentMethodModel(context.getString(R.string.payment_method_klik_bca), context.getString(R.string.payment_method_description_klik_bca), paymentType, R.drawable.ic_klikbca);
                break;
            case BRI_EPAY:
                paymentMethodModel = new PaymentMethodModel(context.getString(R.string.payment_method_bri_epay), context.getString(R.string.payment_method_description_epay_bri), paymentType, R.drawable.ic_epay);
                break;
            case CIMB_CLICKS:
                paymentMethodModel = new PaymentMethodModel(context.getString(R.string.payment_method_cimb_clicks), context.getString(R.string.payment_method_description_cimb_clicks), paymentType, R.drawable.ic_cimb);
                break;
            case MANDIRI_CLICKPAY:
                paymentMethodModel = new PaymentMethodModel(context.getString(R.string.payment_method_mandiri_clickpay), context.getString(R.string.payment_method_description_mandiri_clickpay), paymentType, R.drawable.ic_mandiri2);
                break;
            case INDOMARET:
                paymentMethodModel = new PaymentMethodModel(context.getString(R.string.payment_method_indomaret), context.getString(R.string.payment_method_description_indomaret), paymentType, R.drawable.ic_indomaret);
                break;
            case KIOSON:
                paymentMethodModel = new PaymentMethodModel(context.getString(R.string.payment_method_kioson), context.getString(R.string.payment_method_description_kioson), paymentType, R.drawable.ic_kioson);
                break;
            case TELKOMSEL_CASH:
                paymentMethodModel = new PaymentMethodModel(context.getString(R.string.payment_method_telkomsel_cash), context.getString(R.string.payment_method_description_telkomsel_cash), paymentType, R.drawable.ic_telkomsel);
                break;
            case MANDIRI_ECASH:
                paymentMethodModel = new PaymentMethodModel(context.getString(R.string.payment_method_mandiri_ecash), context.getString(R.string.payment_method_description_mandiri_ecash), paymentType, R.drawable.ic_mandiri_e_cash);
                break;
            case INDOSAT_DOMPETKU:
                paymentMethodModel = new PaymentMethodModel(context.getString(R.string.payment_method_indosat_dompetku), context.getString(R.string.payment_method_description_indosat_dompetku), paymentType, R.drawable.ic_indosat);
                break;
            case XL_TUNAI:
                paymentMethodModel = new PaymentMethodModel(context.getString(R.string.payment_method_xl_tunai), context.getString(R.string.payment_method_description_xl_tunai), paymentType, R.drawable.ic_xl);
                break;
            case GCI:
                paymentMethodModel = new PaymentMethodModel(context.getString(R.string.payment_method_gci), context.getString(R.string.payment_method_description_gci), paymentType, R.drawable.ic_gci);
                break;
        }

        return paymentMethodModel;
    }

    private static PaymentMethodModel createBankTransferPaymentMethod(Context context, String type) {
        PaymentMethodModel paymentMethodModel = null;
        switch (type) {
            case PERMATA_VA:
                paymentMethodModel = new PaymentMethodModel(context.getString(R.string.permata_bank_transfer), context.getString(R.string.payment_bank_description_permata), type, R.drawable.ic_permata);
                break;
            case BCA_VA:
                paymentMethodModel = new PaymentMethodModel(context.getString(R.string.bca_bank_transfer), context.getString(R.string.payment_bank_description_bca), type, R.drawable.ic_bca);
                break;
            case E_CHANNEL:
                paymentMethodModel = new PaymentMethodModel(context.getString(R.string.mandiri_bank_transfer), context.getString(R.string.payment_bank_description_mandiri), type, R.drawable.ic_mandiri_bill);
                break;
            case OTHER_VA:
                paymentMethodModel = new PaymentMethodModel(context.getString(R.string.other_bank_transfer), context.getString(R.string.payment_bank_description_other), type, R.drawable.ic_atm);
                break;
            case BNI_VA:
                paymentMethodModel = new PaymentMethodModel(context.getString(R.string.bni_bank_transfer), context.getString(R.string.payment_bank_description_bni), type, R.drawable.ic_bni);
                break;
        }
        return paymentMethodModel;
    }

    public static boolean isCreditCardExist(List<PaymentMethodModel> paymentMethods) {
        for (PaymentMethodModel paymentMethodModel : paymentMethods) {
            if (paymentMethodModel.getPaymentType().equals(PaymentType.CREDIT_CARD)) {
                return true;
            }
        }
        return false;
    }

    /**
     * create bank payment method models by payment type
     * @param context
     * @param bankList banks type
     * @return bank payment method models
     */
    public static List<PaymentMethodModel> createBankPaymentMethods(Context context, List<String> bankList) {
        List<PaymentMethodModel> paymentMethodList = new ArrayList<>();
        if (bankList != null && !bankList.isEmpty()) {
            for (String paymentType : bankList) {
                paymentMethodList.add(createBankTransferPaymentMethod(context, paymentType));
            }
        }
        return paymentMethodList;
    }
}
