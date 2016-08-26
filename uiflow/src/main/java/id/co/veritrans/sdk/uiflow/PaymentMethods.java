package id.co.veritrans.sdk.uiflow;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import id.co.veritrans.sdk.coreflow.core.Constants;
import id.co.veritrans.sdk.coreflow.models.BankTransferModel;
import id.co.veritrans.sdk.coreflow.models.PaymentMethodsModel;

/**
 * Payment method list.
 *
 * @author rakawm
 */
public class PaymentMethods {

    public static PaymentMethodsModel getMethods(Context context, String name) {
        if (name.equals(context.getString(R.string.payment_credit_debit))) {
            return getMethodCreditCards(context);
        } else if (name.equals(context.getString(R.string.payment_bank_transfer))) {
            return getMethodBankTransfer(context);
        } else if (name.equals(context.getString(R.string.payment_klik_bca))) {
            return getMethodKlikBCA(context);
        } else if (name.equals(context.getString(R.string.payment_mandiri_clickpay))) {
            return getMethodMandiriClickpay(context);
        } else if (name.equals(context.getString(R.string.payment_bca_click))) {
            return getMethodBCAKlikpay(context);
        } else if (name.equals(context.getString(R.string.payment_cimb_clicks))) {
            return getMethodCIMBClicks(context);
        } else if (name.equals(context.getString(R.string.payment_epay_bri))) {
            return getMethodEpayBRI(context);
        } else if (name.equals(context.getString(R.string.payment_indomaret))) {
            return getMethodIndomaret(context);
        } else if (name.equals(context.getString(R.string.payment_mandiri_ecash))) {
            return getMethodMandiriECash(context);
        } else if (name.equals(context.getString(R.string.payment_indosat_dompetku))) {
            return getMethodIndosatDompetku(context);
        } else if (name.equals(context.getString(R.string.payment_telkomsel_cash))) {
            return getMethodTelkomselCash(context);
        } else if (name.equals(context.getString(R.string.payment_xl_tunai))) {
            return getMethodXLTunai(context);
        } else if (name.equals(context.getString(R.string.payment_kioson))){
            return getMethodKiosan(context);
        } else{
            return null;
        }
    }

    private static PaymentMethodsModel getMethodKiosan(Context context) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_kioson), R.drawable.ic_kioson, Constants.PAYMENT_METHOD_NOT_SELECTED);
    }

    public static PaymentMethodsModel getMethodOffers(Context context) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_offers), R.drawable.ic_offers, Constants.PAYMENT_METHOD_NOT_SELECTED);
    }

    public static PaymentMethodsModel getMethodCreditCards(Context context) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_credit_card), R.drawable.ic_credit, Constants.PAYMENT_METHOD_NOT_SELECTED);
    }

    public static PaymentMethodsModel getMethodMandiriClickpay(Context context) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_mandiri_clickpay), R.drawable.ic_mandiri2, Constants.PAYMENT_METHOD_NOT_SELECTED);
    }

    public static PaymentMethodsModel getMethodCIMBClicks(Context context) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_cimb_clicks), R.drawable.ic_cimb, Constants.PAYMENT_METHOD_NOT_SELECTED);
    }

    public static PaymentMethodsModel getMethodEpayBRI(Context context) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_bri_epay), R.drawable.ic_epay, Constants.PAYMENT_METHOD_NOT_SELECTED);
    }

    public static PaymentMethodsModel getMethodIndosatDompetku(Context context) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_indosat_dompetku), R.drawable.ic_indosat, Constants.PAYMENT_METHOD_NOT_SELECTED);
    }

    public static PaymentMethodsModel getMethodMandiriECash(Context context) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_mandiri_ecash), R.drawable.ic_mandiri_e_cash, Constants.PAYMENT_METHOD_NOT_SELECTED);
    }

    public static PaymentMethodsModel getMethodBankTransfer(Context context) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_bank_transfer), R.drawable.ic_atm, Constants.PAYMENT_METHOD_NOT_SELECTED);
    }

    public static PaymentMethodsModel getMethodMandiriBill(Context context) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_mandiri_bill), R.drawable.ic_mandiri_bill_payment2, Constants.PAYMENT_METHOD_NOT_SELECTED);
    }

    public static PaymentMethodsModel getMethodIndomaret(Context context) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_indomaret), R.drawable.ic_indomaret, Constants.PAYMENT_METHOD_NOT_SELECTED);
    }

    public static PaymentMethodsModel getMethodBCAKlikpay(Context context) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_bca_klikpay), R.drawable.ic_bca, Constants.PAYMENT_METHOD_NOT_SELECTED);
    }

    public static PaymentMethodsModel getMethodKlikBCA(Context context) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_klik_bca), R.drawable.ic_klikbca, Constants.PAYMENT_METHOD_NOT_SELECTED);
    }

    public static PaymentMethodsModel getMethodTelkomselCash(Context context) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_telkomsel_cash), R.drawable.ic_telkomsel, Constants.PAYMENT_METHOD_NOT_SELECTED);
    }

    public static PaymentMethodsModel getMethodXLTunai(Context context) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_xl_tunai), R.drawable.ic_xl, Constants.PAYMENT_METHOD_NOT_SELECTED);
    }

    /**
     * Get all supported payment methods by default.
     */
    public static ArrayList<PaymentMethodsModel> getAllPaymentMethods(Context context) {
        ArrayList<PaymentMethodsModel> selectedPaymentMethods = new ArrayList<>();

        int[] paymentImageList = getImageList();
        String[] nameList = getStringList(context);

        for (int i = 0; i < nameList.length; i++) {
            PaymentMethodsModel model = new PaymentMethodsModel(nameList[i], paymentImageList[i],
                     Constants.PAYMENT_METHOD_NOT_SELECTED);
            model.setIsSelected(true);
            selectedPaymentMethods.add(model);
        }

        return selectedPaymentMethods;
    }

    private static String[] getStringList(Context context) {
        String[] paymentNameList = new String[14];
        paymentNameList[0] = context.getString(R.string.payment_method_credit_card);
        paymentNameList[1] = context.getString(R.string.payment_method_bank_transfer);
        paymentNameList[2] = context.getString(R.string.payment_method_klik_bca);
        paymentNameList[3] = context.getString(R.string.payment_method_bca_klikpay);
        paymentNameList[4] = context.getString(R.string.payment_method_mandiri_clickpay);
        paymentNameList[5] = context.getString(R.string.payment_method_mandiri_ecash);
        paymentNameList[7] = context.getString(R.string.payment_method_bri_epay);
        paymentNameList[8] = context.getString(R.string.payment_method_cimb_clicks);
        paymentNameList[9] = context.getString(R.string.payment_method_offers);
        paymentNameList[10] = context.getString(R.string.payment_method_indosat_dompetku);
        paymentNameList[11] = context.getString(R.string.payment_method_indomaret);
        paymentNameList[12] = context.getString(R.string.payment_method_telkomsel_cash);
        paymentNameList[13] = context.getString(R.string.payment_method_xl_tunai);
        return paymentNameList;
    }

    private static int[] getImageList() {

        int[] paymentImageList = new int[14];
        paymentImageList[0] = R.drawable.ic_credit;
        paymentImageList[1] = R.drawable.ic_atm;
        paymentImageList[2] = R.drawable.ic_klikbca;
        paymentImageList[3] = R.drawable.ic_bca;
        paymentImageList[4] = R.drawable.ic_mandiri2;
        paymentImageList[5] = R.drawable.ic_mandiri_e_cash;
        paymentImageList[7] = R.drawable.ic_epay;
        paymentImageList[8] = R.drawable.ic_cimb;
        paymentImageList[9] = R.drawable.ic_offers;
        paymentImageList[10] = R.drawable.ic_indosat;
        paymentImageList[11] = R.drawable.ic_indomaret;
        paymentImageList[12] = R.drawable.ic_telkomsel;
        paymentImageList[13] = R.drawable.ic_xl;

        return paymentImageList;
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
        if (name.equals(context.getString(R.string.bank_transfer_bca))) {
            return new BankTransferModel(context.getString(R.string.bca_bank_transfer), R.drawable.ic_bca, true);
        } else if (name.equals(context.getString(R.string.bank_transfer_permata))) {
            return new BankTransferModel(context.getString(R.string.permata_bank_transfer), R.drawable.ic_permata, true);
        } else if (name.equals(context.getString(R.string.bank_transfer_all_bank))) {
            return new BankTransferModel(context.getString(R.string.all_bank_transfer), R.drawable.ic_other_bank, true);
        } else if (name.equals(context.getString(R.string.bank_transfer_mandiri))) {
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
