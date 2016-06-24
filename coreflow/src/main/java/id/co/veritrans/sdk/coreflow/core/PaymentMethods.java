package id.co.veritrans.sdk.coreflow.core;

import android.content.Context;

import java.util.ArrayList;

import id.co.veritrans.sdk.coreflow.R;
import id.co.veritrans.sdk.coreflow.models.PaymentMethodsModel;

/**
 * Payment method list.
 *
 * @author rakawm
 */
public class PaymentMethods {

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

    public static PaymentMethodsModel getMethodBBMMoney(Context context) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_bbm_money), R.drawable.ic_bbm, Constants.PAYMENT_METHOD_NOT_SELECTED);
    }

    public static PaymentMethodsModel getMethodIndosatDompetku(Context context) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_indosat_dompetku), R.drawable.ic_indosat, Constants.PAYMENT_METHOD_NOT_SELECTED);
    }

    public static PaymentMethodsModel geMethodtMandiriECash(Context context) {
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
        String[] paymentNameList = new String[13];
        paymentNameList[0] = context.getString(R.string.payment_method_credit_card);
        paymentNameList[1] = context.getString(R.string.payment_method_bank_transfer);
        paymentNameList[2] = context.getString(R.string.payment_method_klik_bca);
        paymentNameList[3] = context.getString(R.string.payment_method_bca_klikpay);
        paymentNameList[4] = context.getString(R.string.payment_method_mandiri_clickpay);
        paymentNameList[5] = context.getString(R.string.payment_method_mandiri_ecash);
        paymentNameList[6] = context.getString(R.string.payment_method_mandiri_bill);
        paymentNameList[7] = context.getString(R.string.payment_method_bri_epay);
        paymentNameList[8] = context.getString(R.string.payment_method_cimb_clicks);
        paymentNameList[9] = context.getString(R.string.payment_method_offers);
        paymentNameList[10] = context.getString(R.string.payment_method_indosat_dompetku);
        paymentNameList[11] = context.getString(R.string.payment_method_indomaret);
        paymentNameList[12] = context.getString(R.string.payment_method_bbm_money);

        return paymentNameList;
    }

    private static int[] getImageList() {

        int[] paymentImageList = new int[13];
        paymentImageList[0] = id.co.veritrans.sdk.coreflow.R.drawable.ic_credit;
        paymentImageList[1] = id.co.veritrans.sdk.coreflow.R.drawable.ic_atm;
        paymentImageList[2] = id.co.veritrans.sdk.coreflow.R.drawable.ic_klikbca;
        paymentImageList[3] = id.co.veritrans.sdk.coreflow.R.drawable.ic_bca;
        paymentImageList[4] = id.co.veritrans.sdk.coreflow.R.drawable.ic_mandiri2;
        paymentImageList[5] = id.co.veritrans.sdk.coreflow.R.drawable.ic_mandiri_e_cash; // mandiri e-Cash
        paymentImageList[6] = id.co.veritrans.sdk.coreflow.R.drawable.ic_mandiri_bill_payment2;
        paymentImageList[7] = id.co.veritrans.sdk.coreflow.R.drawable.ic_epay;
        paymentImageList[8] = id.co.veritrans.sdk.coreflow.R.drawable.ic_cimb;
        paymentImageList[9] = id.co.veritrans.sdk.coreflow.R.drawable.ic_offers;
        paymentImageList[10] = id.co.veritrans.sdk.coreflow.R.drawable.ic_indosat;
        paymentImageList[11] = id.co.veritrans.sdk.coreflow.R.drawable.ic_indomaret;
        paymentImageList[12] = id.co.veritrans.sdk.coreflow.R.drawable.ic_bbm;

        return paymentImageList;
    }
}
