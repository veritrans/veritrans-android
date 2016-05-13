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

    public static final PaymentMethodsModel OFFERS = new PaymentMethodsModel("Offers", R.drawable.ic_offers, Constants.PAYMENT_METHOD_NOT_SELECTED);
    public static final PaymentMethodsModel CREDIT_CARD = new PaymentMethodsModel("Credit/Debit Card", R.drawable.ic_credit, Constants.PAYMENT_METHOD_NOT_SELECTED);
    public static final PaymentMethodsModel MANDIRI_CLICKPAY = new PaymentMethodsModel("Mandiri Clickpay", R.drawable.ic_mandiri2, Constants.PAYMENT_METHOD_NOT_SELECTED);
    public static final PaymentMethodsModel CIMB_CLICKS = new PaymentMethodsModel("CIMB Clicks", R.drawable.ic_cimb, Constants.PAYMENT_METHOD_NOT_SELECTED);
    public static final PaymentMethodsModel EPAY_BRI = new PaymentMethodsModel("ePay BRI", R.drawable.ic_epay, Constants.PAYMENT_METHOD_NOT_SELECTED);
    public static final PaymentMethodsModel BBM_MONEY = new PaymentMethodsModel("BBM Money", R.drawable.ic_bbm, Constants.PAYMENT_METHOD_NOT_SELECTED);
    public static final PaymentMethodsModel INDOSAT_DOMPETKU = new PaymentMethodsModel("Indosat Dompetku", R.drawable.ic_indosat, Constants.PAYMENT_METHOD_NOT_SELECTED);
    public static final PaymentMethodsModel MANDIRI_ECASH = new PaymentMethodsModel("Mandiri e-Cash", R.drawable.ic_mandiri_e_cash, Constants.PAYMENT_METHOD_NOT_SELECTED);
    public static final PaymentMethodsModel BANK_TRANSFER = new PaymentMethodsModel("Bank Transfer", R.drawable.ic_atm, Constants.PAYMENT_METHOD_NOT_SELECTED);
    public static final PaymentMethodsModel MANDIRI_BILL = new PaymentMethodsModel("Mandiri Bill Payment", R.drawable.ic_mandiri_bill_payment2, Constants.PAYMENT_METHOD_NOT_SELECTED);
    public static final PaymentMethodsModel INDOMARET = new PaymentMethodsModel("Indomaret", R.drawable.ic_indomaret, Constants.PAYMENT_METHOD_NOT_SELECTED);

    /**
     * Get all supported payment methods by default.
     */
    public static ArrayList<PaymentMethodsModel> getAllPaymentMethods(Context context) {
        ArrayList<PaymentMethodsModel> selectedPaymentMethods = new ArrayList<>();
        String[] names = context.getResources().getStringArray(id.co.veritrans.sdk.R.array.payment_methods);
        Logger.d("there are total " + names.length + " payment methods available.");

        int[] paymentImageList = getImageList();

        for (int i = 0; i < names.length; i++) {
            PaymentMethodsModel model = new PaymentMethodsModel(names[i], paymentImageList[i],
                    id.co.veritrans.sdk.core.Constants.PAYMENT_METHOD_NOT_SELECTED);
            model.setIsSelected(true);
            selectedPaymentMethods.add(model);
        }
        return selectedPaymentMethods;

    }

    private static int[] getImageList() {

        int[] paymentImageList = new int[11];

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

        return paymentImageList;
    }
}
