package com.midtrans.sdk.uikit;

import android.content.Context;
import android.text.TextUtils;

import com.midtrans.sdk.corekit.core.Constants;
import com.midtrans.sdk.corekit.core.PaymentType;
import com.midtrans.sdk.corekit.models.BankTransferModel;
import com.midtrans.sdk.corekit.models.PaymentMethodsModel;
import com.midtrans.sdk.corekit.models.snap.EnabledPayment;
import com.midtrans.sdk.uikit.models.BankTransfer;
import com.midtrans.sdk.uikit.models.CreditCardType;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Payment method list.
 *
 * @author rakawm
 */
public class PaymentMethods {

    public static PaymentMethodsModel getMethods(Context context, String name, String status) {
        if (name.equals(context.getString(R.string.payment_credit_debit))) {
            return getMethodCreditCards(context, 1, status);
        } else if (name.equals(context.getString(R.string.payment_bank_transfer))) {
            return getMethodBankTransfer(context, 2, status);
        } else if (name.equals(context.getString(R.string.payment_bca_click))) {
            return getMethodBCAKlikpay(context, 3, status);
        } else if (name.equals(context.getString(R.string.payment_klik_bca))) {
            return getMethodKlikBCA(context, 4, status);
        } else if (name.equals(context.getString(R.string.payment_epay_bri))) {
            return getMethodEpayBRI(context, 5, status);
        } else if (name.equals(context.getString(R.string.payment_cimb_clicks))) {
            return getMethodCIMBClicks(context, 6, status);
        } else if (name.equals(context.getString(R.string.payment_mandiri_clickpay))) {
            return getMethodMandiriClickpay(context, 7, status);
        } else if (name.equals(context.getString(R.string.payment_indomaret))) {
            return getMethodIndomaret(context, 8, status);
        } else if (name.equals(context.getString(R.string.payment_kioson))) {
            return getMethodKiosan(context, 9, status);
        } else if (name.equals(context.getString(R.string.payment_telkomsel_cash))) {
            return getMethodTelkomselCash(context, 10, status);
        } else if (name.equals(context.getString(R.string.payment_mandiri_ecash))) {
            return getMethodMandiriECash(context, 11, status);
        } else if (name.equals(context.getString(R.string.payment_indosat_dompetku))) {
            return getMethodIndosatDompetku(context, 12, status);
        } else if (name.equals(context.getString(R.string.payment_xl_tunai))) {
            return getMethodXLTunai(context, 13, status);
        } else if (name.equals(context.getString(R.string.payment_gci))) {
            return getMethodGCI(context, 14, status);
        } else if (name.equals(context.getString(R.string.payment_gopay))) {
            return getMethodGopay(context, 15, status);
        } else if (name.equals(context.getString(R.string.payment_danamon_online))) {
            return getDanamonOnline(context, 16, status);
        } else {
            return null;
        }
    }

    private static PaymentMethodsModel getMethodCreditCards(Context context, int priority, String status) {
        int creditCardSupportType = SdkUIFlowUtil.getCreditCardIconType();
        switch (creditCardSupportType) {
            case CreditCardType.TYPE_MASTER_VISA_JCB_AMEX:
                return new PaymentMethodsModel(context.getString(R.string.payment_method_credit_card), context.getString(R.string.payment_method_description_credit_card), R.drawable.ic_credit, Constants.PAYMENT_METHOD_NOT_SELECTED, priority, status);
            case CreditCardType.TYPE_MASTER_VISA_JCB:
                return new PaymentMethodsModel(context.getString(R.string.payment_method_credit_card), context.getString(R.string.payment_method_description_credit_card_3), R.drawable.ic_credit_3, Constants.PAYMENT_METHOD_NOT_SELECTED, priority, status);
            case CreditCardType.TYPE_MASTER_VISA_AMEX:
                return new PaymentMethodsModel(context.getString(R.string.payment_method_credit_card), context.getString(R.string.payment_method_description_credit_card_4), R.drawable.ic_credit_4, Constants.PAYMENT_METHOD_NOT_SELECTED, priority, status);
            default:
                return new PaymentMethodsModel(context.getString(R.string.payment_method_credit_card), context.getString(R.string.payment_method_description_credit_card_2), R.drawable.ic_credit_2, Constants.PAYMENT_METHOD_NOT_SELECTED, priority, status);
        }
    }

    private static PaymentMethodsModel getMethodBankTransfer(Context context, int priority, String status) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_bank_transfer), context.getString(R.string.payment_method_description_bank_transfer), R.drawable.ic_atm, Constants.PAYMENT_METHOD_NOT_SELECTED, priority, status);
    }

    private static PaymentMethodsModel getMethodBCAKlikpay(Context context, int priority, String status) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_bca_klikpay), context.getString(R.string.payment_method_description_bca_klikpay), R.drawable.ic_klikpay, Constants.PAYMENT_METHOD_NOT_SELECTED, priority, status);
    }

    private static PaymentMethodsModel getMethodKlikBCA(Context context, int priority, String status) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_klik_bca), context.getString(R.string.payment_method_description_klik_bca), R.drawable.ic_klikbca, Constants.PAYMENT_METHOD_NOT_SELECTED, priority, status);
    }

    private static PaymentMethodsModel getMethodEpayBRI(Context context, int priority, String status) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_bri_epay), context.getString(R.string.payment_method_description_epay_bri), R.drawable.ic_epay, Constants.PAYMENT_METHOD_NOT_SELECTED, priority, status);
    }

    private static PaymentMethodsModel getMethodCIMBClicks(Context context, int priority, String status) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_cimb_clicks), context.getString(R.string.payment_method_description_cimb_clicks), R.drawable.ic_cimb, Constants.PAYMENT_METHOD_NOT_SELECTED, priority, status);
    }

    private static PaymentMethodsModel getMethodMandiriClickpay(Context context, int priority, String status) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_mandiri_clickpay), context.getString(R.string.payment_method_description_mandiri_clickpay), R.drawable.ic_mandiri2, Constants.PAYMENT_METHOD_NOT_SELECTED, priority, status);
    }

    private static PaymentMethodsModel getMethodIndomaret(Context context, int priority, String status) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_indomaret), context.getString(R.string.payment_method_description_indomaret), R.drawable.ic_indomaret, Constants.PAYMENT_METHOD_NOT_SELECTED, priority, status);
    }

    private static PaymentMethodsModel getMethodKiosan(Context context, int priority, String status) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_kioson), context.getString(R.string.payment_method_description_kioson), R.drawable.ic_kioson, Constants.PAYMENT_METHOD_NOT_SELECTED, priority, status);
    }

    private static PaymentMethodsModel getMethodTelkomselCash(Context context, int priority, String status) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_telkomsel_cash), context.getString(R.string.payment_method_description_telkomsel_cash), R.drawable.ic_telkomsel, Constants.PAYMENT_METHOD_NOT_SELECTED, priority, status);
    }

    private static PaymentMethodsModel getMethodMandiriECash(Context context, int priority, String status) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_mandiri_ecash), context.getString(R.string.payment_method_description_mandiri_ecash), R.drawable.ic_mandiri_e_cash, Constants.PAYMENT_METHOD_NOT_SELECTED, priority, status);
    }

    private static PaymentMethodsModel getMethodIndosatDompetku(Context context, int priority, String status) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_indosat_dompetku), context.getString(R.string.payment_method_description_indosat_dompetku), R.drawable.ic_indosat, Constants.PAYMENT_METHOD_NOT_SELECTED, priority, status);
    }

    static PaymentMethodsModel getMethodXLTunai(Context context, int priority, String status) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_xl_tunai), context.getString(R.string.payment_method_description_xl_tunai), R.drawable.ic_xl, Constants.PAYMENT_METHOD_NOT_SELECTED, priority, status);
    }

    private static PaymentMethodsModel getMethodGCI(Context context, int priority, String status) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_gci), context.getString(R.string.payment_method_description_gci), R.drawable.ic_gci, Constants.PAYMENT_METHOD_NOT_SELECTED, priority, status);
    }

    private static PaymentMethodsModel getMethodGopay(Context context, int priority, String status) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_gopay), context.getString(R.string.payment_method_description_gopay), R.drawable.ic_gopay, Constants.PAYMENT_METHOD_NOT_SELECTED, priority, status);
    }

    private static PaymentMethodsModel getDanamonOnline(Context context, int priority, String status) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_danamon_online), context.getString(R.string.payment_method_description_danamon_online), R.drawable.ic_danamon_online, Constants.PAYMENT_METHOD_NOT_SELECTED, priority, status);
    }

    public static ArrayList<BankTransferModel> getBankTransferList(Context context) {
        ArrayList<BankTransferModel> models = new ArrayList<>();
        models.add(getBankTransferModel(context, context.getString(R.string.payment_bca_va), EnabledPayment.STATUS_DOWN));
        models.add(getBankTransferModel(context, context.getString(R.string.payment_mandiri_bill_payment), EnabledPayment.STATUS_DOWN));
        models.add(getBankTransferModel(context, context.getString(R.string.payment_permata_va), EnabledPayment.STATUS_DOWN));
        models.add(getBankTransferModel(context, context.getString(R.string.payment_all_va), EnabledPayment.STATUS_DOWN));
        return models;
    }

    /**
     * Get bank transfer model to be shown
     * It's deprecated now, please use {@link PaymentMethods#createBankTransferModel(Context, String, String)}
     *
     * @param context context
     * @param name    bank name
     * @param status  whether it is up or down
     * @return bank transfer model
     */
    @Deprecated
    public static BankTransferModel getBankTransferModel(Context context, String name, String status) {
        if (name.equals(context.getString(R.string.payment_bca_va))) {
            return new BankTransferModel(context.getString(R.string.bca_bank_transfer), R.drawable.ic_bca, true, 1, context.getString(R.string.payment_bank_description_bca), status);
        } else if (name.equals(context.getString(R.string.payment_permata_va))) {
            return new BankTransferModel(context.getString(R.string.permata_bank_transfer), R.drawable.ic_permata, true, 3, context.getString(R.string.payment_bank_description_permata), status);
        } else if (name.equals(context.getString(R.string.payment_all_va))) {
            return new BankTransferModel(context.getString(R.string.all_bank_transfer), R.drawable.ic_atm, true, 5, context.getString(R.string.payment_bank_description_other), status);
        } else if (name.equals(context.getString(R.string.payment_mandiri_bill_payment))) {
            return new BankTransferModel(context.getString(R.string.mandiri_bank_transfer), R.drawable.ic_mandiri_bill_payment2, true, 2, context.getString(R.string.payment_bank_description_mandiri), status);
        } else if (name.equals(context.getString(R.string.payment_bni_va))) {
            return new BankTransferModel(context.getString(R.string.bni_bank_transfer), R.drawable.ic_bni, true, 4, context.getString(R.string.payment_bank_description_bni), status);
        } else {
            return null;
        }
    }


    public static BankTransfer createBankTransferModel(Context context, String type, String status) {
        BankTransfer bankTransfer = null;
        if (!TextUtils.isEmpty(type)) {
            switch (type) {
                case PaymentType.BCA_VA:
                    bankTransfer = new BankTransfer(type, context.getString(R.string.bca_bank_transfer), R.drawable.ic_bca, 1, context.getString(R.string.payment_bank_description_bca), status);
                    break;

                case PaymentType.E_CHANNEL:
                    bankTransfer = new BankTransfer(type, context.getString(R.string.mandiri_bill), R.drawable.ic_mandiri_bill_payment2, 2, context.getString(R.string.payment_bank_description_mandiri), status);
                    break;

                case PaymentType.BNI_VA:
                    bankTransfer = new BankTransfer(type, context.getString(R.string.bni_bank_transfer), R.drawable.ic_bni, 4, context.getString(R.string.payment_bank_description_bni), status);
                    break;

                case PaymentType.PERMATA_VA:
                    bankTransfer = new BankTransfer(type, context.getString(R.string.permata_bank_transfer), R.drawable.ic_permata, 3, context.getString(R.string.payment_bank_description_permata), status);
                    break;

                case PaymentType.ALL_VA:
                    bankTransfer = new BankTransfer(type, context.getString(R.string.all_bank_transfer), R.drawable.ic_atm, 5, context.getString(R.string.payment_bank_description_other), status);
                    break;
            }

        }

        return bankTransfer;
    }
}
