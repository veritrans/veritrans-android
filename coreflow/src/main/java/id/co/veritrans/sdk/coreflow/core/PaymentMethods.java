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

    public static final String METHOD_OFFERS = "Offers";
    public static final String METHOD_CREDIT_CARD = "Credit/Debit Card";
    public static final String METHOD_MANDIRI_CLICKPAY = "Mandiri Clickpay";
    public static final String METHOD_CIMB_CLICKS = "CIMB Clicks";
    public static final String METHOD_EPAY_BRI = "ePay BRI";
    public static final String METHOD_BBM_MONEY = "BBM Money";
    public static final String METHOD_INDOSAT_DOMPETKU = "Indosat Dompetku";
    public static final String METHOD_MANDIRI_ECASH = "Mandiri e-Cash";
    public static final String METHOD_BANK_TRANSFER = "Bank Transfer";
    public static final String METHOD_MANDIRI_BILL = "Mandiri Bill Payment";
    public static final String METHOD_INDOMARET = "Indomaret";
    public static final String METHOD_BCA_KLIKPAY = "BCA KlikPay";

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

    /**
     * Get all supported payment methods by default.
     */
    public static ArrayList<PaymentMethodsModel> getAllPaymentMethods(Context context) {
        ArrayList<PaymentMethodsModel> selectedPaymentMethods = new ArrayList<>();

        int[] paymentImageList = getImageList();
        String[] nameList = getStringList(context);

        for (int i = 0; i < nameList.length; i++) {
            PaymentMethodsModel model = new PaymentMethodsModel(nameList[i], paymentImageList[i],
                    id.co.veritrans.sdk.coreflow.core.Constants.PAYMENT_METHOD_NOT_SELECTED);
            model.setIsSelected(true);
            selectedPaymentMethods.add(model);
        }

        return selectedPaymentMethods;
    }

    private static String[] getStringList(Context context) {
        String[] paymentNameList = new String[12];

        paymentNameList[0] = context.getString(R.string.payment_method_offers);
        paymentNameList[1] = context.getString(R.string.payment_method_credit_card);
        paymentNameList[2] = context.getString(R.string.payment_method_mandiri_clickpay);
        paymentNameList[3] = context.getString(R.string.payment_method_cimb_clicks);
        paymentNameList[4] = context.getString(R.string.payment_method_bri_epay);
        paymentNameList[5] = context.getString(R.string.payment_method_bbm_money);
        paymentNameList[6] = context.getString(R.string.payment_method_indosat_dompetku);
        paymentNameList[7] = context.getString(R.string.payment_method_mandiri_ecash);
        paymentNameList[8] = context.getString(R.string.payment_method_bank_transfer);
        paymentNameList[9] = context.getString(R.string.payment_method_mandiri_bill);
        paymentNameList[10] = context.getString(R.string.payment_method_indomaret);
        paymentNameList[11] = context.getString(R.string.payment_method_bca_klikpay);

        return paymentNameList;
    }

    private static int[] getImageList() {

        int[] paymentImageList = new int[12];

        paymentImageList[0] = id.co.veritrans.sdk.coreflow.R.drawable.ic_offers;
        paymentImageList[1] = id.co.veritrans.sdk.coreflow.R.drawable.ic_credit;
        paymentImageList[2] = id.co.veritrans.sdk.coreflow.R.drawable.ic_mandiri2;
        paymentImageList[3] = id.co.veritrans.sdk.coreflow.R.drawable.ic_cimb;
        paymentImageList[4] = id.co.veritrans.sdk.coreflow.R.drawable.ic_epay;
        paymentImageList[5] = id.co.veritrans.sdk.coreflow.R.drawable.ic_bbm;
        paymentImageList[6] = id.co.veritrans.sdk.coreflow.R.drawable.ic_indosat;
        paymentImageList[7] = id.co.veritrans.sdk.coreflow.R.drawable.ic_mandiri_e_cash; // mandiri e-Cash
        paymentImageList[8] = id.co.veritrans.sdk.coreflow.R.drawable.ic_atm;
        paymentImageList[9] = id.co.veritrans.sdk.coreflow.R.drawable.ic_mandiri_bill_payment2;
        paymentImageList[10] = id.co.veritrans.sdk.coreflow.R.drawable.ic_indomaret;
        paymentImageList[11] = id.co.veritrans.sdk.coreflow.R.drawable.ic_bca;

        return paymentImageList;
    }
}
