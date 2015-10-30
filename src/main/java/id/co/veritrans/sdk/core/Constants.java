package id.co.veritrans.sdk.core;

/**
 * Created by chetan on 16/10/15.
 */
public class Constants {

    public static final int PAYMENT_METHOD_NOT_SELECTED = -1;

    public static final int PAYMENT_METHOD_OFFERS = 0;
    public static final int PAYMENT_METHOD_CREDIT_OR_DEBIT = 1;
    public static final int PAYMENT_METHOD_MANDIRI_CLICK_PAY = 2;

    public static final int PAYMENT_METHOD_CIMB_CLICKS = 3;
    public static final int PAYMENT_METHOD_EPAY_BRI = 4;
    public static final int PAYMENT_METHOD_BBM_MONEY = 5;

    public static final int PAYMENT_METHOD_INDOSAT_DOMPETKU = 6;
    public static final int PAYMENT_METHOD_MANDIRI_ECASH = 7;

    public static final int PAYMENT_METHOD_PERMATA_VA_BANK_TRANSFER = 8;
    public static final int PAYMENT_METHOD_MANDIRI_BILL_PAYMENT = 9;
    public static final int PAYMENT_METHOD_INDOMARET = 10;


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


    public static final float CARD_ASPECT_RATIO = 0.555f;


    public static final String BASE_URL_FOR_DEBUG = "http://api.sandbox.veritrans.co.id/v2/";
    public static final String BASE_URL_FOR_RELEASE = "http://api.sandbox.veritrans.co.id/v2/";


    public static final String DIR_APP = "VeritransData";
    public static final String CURRENCY_PREFIX = "Rp";

    public static final String ERROR_EMPTY_RESPONSE = "failed to retrieve response from server.";
    public static final String ERROR_SDK_IS_NOT_INITIALIZED = "sdk is not initialized.";
    public static final String ERROR_INVALID_DATA_SUPPLIED = "Invalid data supplied to sdk.";

    public static final int MONTH_COUNT = 12;
    public static final CharSequence RETROFIT_NETWORK_MESSAGE = "Unable to resolve host";
    public static final CharSequence CALLBACK_STRING = "/token/callback/";
    public static final String WEBURL = "weburl";
    public static final String BANK_NAME = "bni";
}
