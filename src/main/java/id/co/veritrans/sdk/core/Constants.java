package id.co.veritrans.sdk.core;

/**
 * Created by chetan on 16/10/15.
 */
public class Constants {

    public static final int PAYMENT_METHOD_NOT_SELECTED = -1;

    public static final int PAYMENT_METHOD_OFFERS = 1;
    public static final int PAYMENT_METHOD_CREDIT_OR_DEBIT = 2;
    public static final int PAYMENT_METHOD_MANDIRI_CLICK_PAY = 3;

    public static final int PAYMENT_METHOD_CIMB_CLICKS = 4;
    public static final int PAYMENT_METHOD_EPAY_BRI = 5;
    public static final int PAYMENT_METHOD_BBM_MONEY = 6;

    public static final int PAYMENT_METHOD_INDOSAT_DOMPETKU = 7;
    public static final int PAYMENT_METHOD_MANDIRI_ECASH = 8;

    public static final int PAYMENT_METHOD_MANDIRI_BILL_PAYMENT = 9;
    public static final int PAYMENT_METHOD_PERMATA_VA_BANK_TRANSFER = 10;

    public static final String TAG = "VeritransSDK";

    public static final int PHONE_NUMBER_LENGTH = 10;
    public static final String USER_DETAILS = "user_details";
    public static final int ZIPCODE_LENGTH = 6;
    public static final String USER_ADDRESS_DETAILS = "user_address_details";
    public static final int ADDRESS_TYPE_BILLING = 1;
    public static final int ADDRESS_TYPE_SHIPPING = 2;
    public static final int ADDRESS_TYPE_BOTH = 3;
    public static final String EMAIL_PATTERN ="^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                    + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

}
