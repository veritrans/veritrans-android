package id.co.veritrans.sdk.core;

import android.content.Context;

import java.util.ArrayList;

import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.models.PaymentMethodsModel;

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

    public static final PaymentMethodsModel OFFERS = new PaymentMethodsModel(METHOD_OFFERS, R.drawable.ic_offers, Constants.PAYMENT_METHOD_NOT_SELECTED);
    public static final PaymentMethodsModel CREDIT_CARD = new PaymentMethodsModel(METHOD_CREDIT_CARD, R.drawable.ic_credit, Constants.PAYMENT_METHOD_NOT_SELECTED);
    public static final PaymentMethodsModel MANDIRI_CLICKPAY = new PaymentMethodsModel(METHOD_MANDIRI_CLICKPAY, R.drawable.ic_mandiri2, Constants.PAYMENT_METHOD_NOT_SELECTED);
    public static final PaymentMethodsModel CIMB_CLICKS = new PaymentMethodsModel(METHOD_CIMB_CLICKS, R.drawable.ic_cimb, Constants.PAYMENT_METHOD_NOT_SELECTED);
    public static final PaymentMethodsModel EPAY_BRI = new PaymentMethodsModel(METHOD_EPAY_BRI, R.drawable.ic_epay, Constants.PAYMENT_METHOD_NOT_SELECTED);
    public static final PaymentMethodsModel BBM_MONEY = new PaymentMethodsModel(METHOD_BBM_MONEY, R.drawable.ic_bbm, Constants.PAYMENT_METHOD_NOT_SELECTED);
    public static final PaymentMethodsModel INDOSAT_DOMPETKU = new PaymentMethodsModel(METHOD_INDOSAT_DOMPETKU, R.drawable.ic_indosat, Constants.PAYMENT_METHOD_NOT_SELECTED);
    public static final PaymentMethodsModel MANDIRI_ECASH = new PaymentMethodsModel(METHOD_MANDIRI_ECASH, R.drawable.ic_mandiri_e_cash, Constants.PAYMENT_METHOD_NOT_SELECTED);
    public static final PaymentMethodsModel BANK_TRANSFER = new PaymentMethodsModel(METHOD_BANK_TRANSFER, R.drawable.ic_atm, Constants.PAYMENT_METHOD_NOT_SELECTED);
    public static final PaymentMethodsModel MANDIRI_BILL = new PaymentMethodsModel(METHOD_MANDIRI_BILL, R.drawable.ic_mandiri_bill_payment2, Constants.PAYMENT_METHOD_NOT_SELECTED);
    public static final PaymentMethodsModel INDOMARET = new PaymentMethodsModel(METHOD_INDOMARET, R.drawable.ic_indomaret, Constants.PAYMENT_METHOD_NOT_SELECTED);
    public static final PaymentMethodsModel BCA_KLIKPAY = new PaymentMethodsModel(METHOD_BCA_KLIKPAY, R.drawable.ic_bca, Constants.PAYMENT_METHOD_NOT_SELECTED);

    /**
     * Get all supported payment methods by default.
     */
    public static ArrayList<PaymentMethodsModel> getAllPaymentMethods(Context context) {
        ArrayList<PaymentMethodsModel> selectedPaymentMethods = new ArrayList<>();

        int[] paymentImageList = getImageList();
        String[] nameList = getStringList();

        for (int i = 0; i < nameList.length; i++) {
            PaymentMethodsModel model = new PaymentMethodsModel(nameList[i], paymentImageList[i],
                    id.co.veritrans.sdk.core.Constants.PAYMENT_METHOD_NOT_SELECTED);
            model.setIsSelected(true);
            selectedPaymentMethods.add(model);
        }
        return selectedPaymentMethods;

    }

    private static String[] getStringList() {
        String[] paymentNameList = new String[12];

        paymentNameList[0] = METHOD_OFFERS;
        paymentNameList[1] = METHOD_CREDIT_CARD;
        paymentNameList[2] = METHOD_MANDIRI_CLICKPAY;
        paymentNameList[3] = METHOD_CIMB_CLICKS;
        paymentNameList[4] = METHOD_EPAY_BRI;
        paymentNameList[5] = METHOD_BBM_MONEY;
        paymentNameList[6] = METHOD_INDOSAT_DOMPETKU;
        paymentNameList[7] = METHOD_MANDIRI_ECASH;
        paymentNameList[8] = METHOD_BANK_TRANSFER;
        paymentNameList[9] = METHOD_MANDIRI_BILL;
        paymentNameList[10] = METHOD_INDOMARET;
        paymentNameList[11] = METHOD_BCA_KLIKPAY;

        return paymentNameList;
    }

    private static int[] getImageList() {

        int[] paymentImageList = new int[12];

        paymentImageList[0] = id.co.veritrans.sdk.R.drawable.ic_offers;
        paymentImageList[1] = id.co.veritrans.sdk.R.drawable.ic_credit;
        paymentImageList[2] = id.co.veritrans.sdk.R.drawable.ic_mandiri2;
        paymentImageList[3] = id.co.veritrans.sdk.R.drawable.ic_cimb;
        paymentImageList[4] = id.co.veritrans.sdk.R.drawable.ic_epay;
        paymentImageList[5] = id.co.veritrans.sdk.R.drawable.ic_bbm;
        paymentImageList[6] = id.co.veritrans.sdk.R.drawable.ic_indosat;
        paymentImageList[7] = id.co.veritrans.sdk.R.drawable.ic_mandiri_e_cash; // mandiri e-Cash
        paymentImageList[8] = id.co.veritrans.sdk.R.drawable.ic_atm;
        paymentImageList[9] = id.co.veritrans.sdk.R.drawable.ic_mandiri_bill_payment2;
        paymentImageList[10] = id.co.veritrans.sdk.R.drawable.ic_indomaret;
        paymentImageList[11] = id.co.veritrans.sdk.R.drawable.ic_bca;

        return paymentImageList;
    }
}
