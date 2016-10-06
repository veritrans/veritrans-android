package com.midtrans.sdk.uikit;

import android.content.Context;

import com.midtrans.sdk.corekit.core.Constants;
import com.midtrans.sdk.corekit.models.BankTransferModel;
import com.midtrans.sdk.corekit.models.PaymentMethodsModel;

import java.util.ArrayList;
import java.util.List;

/**
 * Payment method list.
 *
 * @author rakawm
 */
public class PaymentMethods {

    public static PaymentMethodsModel getMethods(Context context, String name) {
        if (name.equals(context.getString(R.string.payment_credit_debit))) {
            return getMethodCreditCards(context, 1);
        } else if (name.equals(context.getString(R.string.payment_bank_transfer))) {
            return getMethodBankTransfer(context, 2);
        } else if (name.equals(context.getString(R.string.payment_bca_click))) {
            return getMethodBCAKlikpay(context, 3);
        } else if (name.equals(context.getString(R.string.payment_klik_bca))) {
            return getMethodKlikBCA(context, 4);
        } else if (name.equals(context.getString(R.string.payment_epay_bri))) {
            return getMethodEpayBRI(context, 5);
        } else if (name.equals(context.getString(R.string.payment_cimb_clicks))) {
            return getMethodCIMBClicks(context, 6);
        } else if (name.equals(context.getString(R.string.payment_mandiri_clickpay))) {
            return getMethodMandiriClickpay(context, 7);
        } else if (name.equals(context.getString(R.string.payment_indomaret))) {
            return getMethodIndomaret(context, 8);
        } else if (name.equals(context.getString(R.string.payment_kioson))) {
            return getMethodKiosan(context, 9);
        } else if (name.equals(context.getString(R.string.payment_telkomsel_cash))) {
            return getMethodTelkomselCash(context, 10);
        } else if (name.equals(context.getString(R.string.payment_mandiri_ecash))) {
            return getMethodMandiriECash(context, 11);
        } else if (name.equals(context.getString(R.string.payment_indosat_dompetku))) {
            return getMethodIndosatDompetku(context, 12);
        } else if (name.equals(context.getString(R.string.payment_xl_tunai))) {
            return getMethodXLTunai(context, 13);
        }  else {
            return null;
        }
    }


    public static PaymentMethodsModel getMethodOffers(Context context) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_offers), R.drawable.ic_offers, Constants.PAYMENT_METHOD_NOT_SELECTED, 0);
    }

    public static PaymentMethodsModel getMethodCreditCards(Context context, int priority) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_credit_card), R.drawable.ic_credit, Constants.PAYMENT_METHOD_NOT_SELECTED , priority);
    }

    public static PaymentMethodsModel getMethodBankTransfer(Context context, int priority) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_bank_transfer), R.drawable.ic_atm, Constants.PAYMENT_METHOD_NOT_SELECTED , priority);
    }

    public static PaymentMethodsModel getMethodBCAKlikpay(Context context, int priority) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_bca_klikpay), R.drawable.ic_bca, Constants.PAYMENT_METHOD_NOT_SELECTED, priority);
    }

    public static PaymentMethodsModel getMethodKlikBCA(Context context, int priority) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_klik_bca), R.drawable.ic_klikbca, Constants.PAYMENT_METHOD_NOT_SELECTED, priority);
    }

    public static PaymentMethodsModel getMethodEpayBRI(Context context, int priority) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_bri_epay), R.drawable.ic_epay, Constants.PAYMENT_METHOD_NOT_SELECTED, priority);
    }

    public static PaymentMethodsModel getMethodCIMBClicks(Context context, int priority) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_cimb_clicks), R.drawable.ic_cimb, Constants.PAYMENT_METHOD_NOT_SELECTED, priority);
    }

    public static PaymentMethodsModel getMethodMandiriClickpay(Context context, int priority) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_mandiri_clickpay), R.drawable.ic_mandiri2, Constants.PAYMENT_METHOD_NOT_SELECTED, priority);
    }

    public static PaymentMethodsModel getMethodIndomaret(Context context, int priority) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_indomaret), R.drawable.ic_indomaret, Constants.PAYMENT_METHOD_NOT_SELECTED, priority);
    }

    private static PaymentMethodsModel getMethodKiosan(Context context, int priority) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_kioson), R.drawable.ic_kioson, Constants.PAYMENT_METHOD_NOT_SELECTED, priority);
    }

    public static PaymentMethodsModel getMethodTelkomselCash(Context context, int priority) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_telkomsel_cash), R.drawable.ic_telkomsel, Constants.PAYMENT_METHOD_NOT_SELECTED, priority);
    }

    public static PaymentMethodsModel getMethodMandiriECash(Context context, int priority) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_mandiri_ecash), R.drawable.ic_mandiri_e_cash, Constants.PAYMENT_METHOD_NOT_SELECTED, priority);
    }

    public static PaymentMethodsModel getMethodIndosatDompetku(Context context, int priority) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_indosat_dompetku), R.drawable.ic_indosat, Constants.PAYMENT_METHOD_NOT_SELECTED , priority);
    }

    public static PaymentMethodsModel getMethodXLTunai(Context context, int priority) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_xl_tunai), R.drawable.ic_xl, Constants.PAYMENT_METHOD_NOT_SELECTED, priority);
    }

    public static ArrayList<BankTransferModel> getBankTransferList(Context context) {
        ArrayList<BankTransferModel> models = new ArrayList<>();
        models.add(new BankTransferModel(context.getString(R.string.bca_bank_transfer), R.drawable.ic_bca, true));
        models.add(new BankTransferModel(context.getString(R.string.permata_bank_transfer), R.drawable.ic_permata, true));
        models.add(new BankTransferModel(context.getString(R.string.all_bank_transfer), R.drawable.ic_other_bank, true));
        models.add(new BankTransferModel(context.getString(R.string.mandiri_bank_transfer), R.drawable.ic_mandiri_bill_payment2, true));
        return models;
    }

    public static BankTransferModel getBankTransferModel(Context context, String name) {
        if (name.equals(context.getString(R.string.payment_bca_va))) {
            return new BankTransferModel(context.getString(R.string.bca_bank_transfer), R.drawable.ic_bca, true);
        } else if (name.equals(context.getString(R.string.payment_permata_va))) {
            return new BankTransferModel(context.getString(R.string.permata_bank_transfer), R.drawable.ic_permata, true);
        } else if (name.equals(context.getString(R.string.payment_all_va))) {
            return new BankTransferModel(context.getString(R.string.all_bank_transfer), R.drawable.ic_other_bank, true);
        } else if (name.equals(context.getString(R.string.payment_mandiri_bill_payment))) {
            return new BankTransferModel(context.getString(R.string.mandiri_bank_transfer), R.drawable.ic_mandiri_bill_payment2, true);
        } else {
            return null;
        }
    }

    public static List<String> getDefaultPaymentList(Context context) {
        List<String> paymentNameList = new ArrayList<>();
        paymentNameList.add(context.getString(R.string.payment_credit_debit));
        paymentNameList.add(context.getString(R.string.payment_bank_transfer));
        paymentNameList.add(context.getString(R.string.payment_klik_bca));
        paymentNameList.add(context.getString(R.string.payment_bca_click));
        paymentNameList.add(context.getString(R.string.payment_mandiri_clickpay));
        paymentNameList.add(context.getString(R.string.payment_mandiri_ecash));
        paymentNameList.add(context.getString(R.string.payment_epay_bri));
        paymentNameList.add(context.getString(R.string.payment_cimb_clicks));
        paymentNameList.add(context.getString(R.string.payment_indosat_dompetku));
        paymentNameList.add(context.getString(R.string.payment_indomaret));
        paymentNameList.add(context.getString(R.string.payment_telkomsel_cash));
        paymentNameList.add(context.getString(R.string.payment_xl_tunai));
        return paymentNameList;
    }
}
