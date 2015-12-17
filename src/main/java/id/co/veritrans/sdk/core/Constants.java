package id.co.veritrans.sdk.core;

/**
 * It contains all constant and sdk related static information.
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
    public static final String POSITION = "position";


    public static final int PHONE_NUMBER_LENGTH = 10;
    public static final int PHONE_NUMBER_MAX_LENGTH = 12;
    public static final String USER_DETAILS = "user_details";
    public static final int ZIPCODE_LENGTH = 5;
    public static final String USER_ADDRESS_DETAILS = "user_address_details";


    /**
     * constant to indicate billing address
     */
    public static final int ADDRESS_TYPE_BILLING = 1;
    /**
     * constant to indicate shipping address
     */
    public static final int ADDRESS_TYPE_SHIPPING = 2;

    /**
     * constant to indicate that this address will be used for both billing and shipping purpose.
     */
    public static final int ADDRESS_TYPE_BOTH = 3;



    /**
     *  regex for email id.
     */
    public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";


    public static final float CARD_ASPECT_RATIO = 0.555f;


    /**
     * server end point when application is running in debug mode.
     */
    public static final String BASE_URL_FOR_DEBUG = "https://api.sandbox.veritrans.co.id/v2/";

    /**
     * server end point when application is running in release version.
     */
    public static final String BASE_URL_FOR_RELEASE = "https://api.sandbox.veritrans.co.id/v2/";


    /**
     * name of application directory
     */
    public static final String DIR_APP = "VeritransData";

    /**
     * indonesian rupiah currency symbol
     */
    public static final String CURRENCY_PREFIX = "Rp";

    /**
     * When server returns empty response body for any api request.
     */
    public static final String ERROR_EMPTY_RESPONSE = "failed to retrieve response from server.";

    /**
     * If trying to create api request before initializing sdk.
     */
    public static final String ERROR_SDK_IS_NOT_INITIALIZED = "sdk is not initialized.";

    /**
     * If passed unexpected parameter to any method.
     */
    public static final String ERROR_INVALID_DATA_SUPPLIED = "Invalid data supplied to sdk.";


    public static final int MONTH_COUNT = 12;

    /**
     * network error message
     */
    public static final CharSequence RETROFIT_NETWORK_MESSAGE = "Unable to resolve host";

    public static final CharSequence CALLBACK_STRING = "/token/callback/";

    public static final CharSequence CALLBACK_URL ="https://hangout.betas.in/veritrans/api/paymentstatus";

    public static final String WEBURL = "weburl";

    //public static final String BANK_NAME = "bni";

    /**
     * When failed to create api request, probably because of no network connection.
     */
    public static final String ERROR_UNABLE_TO_CONNECT = "failed to connect to server.";

    /**
     * error message for invalid email id.
     */
    public static final String ERROR_INVALID_EMAIL_ID = "Invalid email Id.";
    public static final String ERROR_INVALID_PHONE_NUMBER = "Invalid Phone Number.";

    public static final String ERROR_SOMETHING_WENT_WRONG = "Please try later, Something went " +
            "wrong!";

    /**
     * error message to show if trying to execute multiple payment/charge request simultaneously.
     */
    public static final String ERROR_ALREADY_RUNNING = "Already Running!";

    /**
     * http success code 200
     */
    public static final String SUCCESS_CODE_200 = "200";

    /**
     * http success code 201
     */
    public static final String SUCCESS_CODE_201 = "201";

    public static final String CARD_CLICK_TYPE_NONE = "normal";
    public static final String CARD_CLICK_TYPE_ONE_CLICK = "one_click";
    public static final String CARD_CLICK_TYPE_TWO_CLICK = "two_click";
    public static final String USERS_SAVED_CARD = "users_saved_card";

    public static final String BANK_DETAILS = "bank_details";
    public static final String VISA_REGEX= "^4[0-9]{12}(?:[0-9]{3})?$";
    public static final String MASTERCARD_REGEX= "^5[1-5][0-9]{14}$";
    public static final String AMEX_REGEX="^3[47][0-9]{13}$";
    public static final String CARD_TYPE_VISA = "VISA";
    public static final String CARD_TYPE_MASTERCARD = "MASTERCARD";
    public static final String SUCCESS = "SUCCESS";

    /**
     * result code used for payment transfer activities
     */
    public static final int RESULT_CODE_PAYMENT_TRANSFER = 5102;
    public static final String TRANSACTION_RESPONSE = "transaction_response";
    public static final String TRANSACTION_ERROR_MESSAGE = "transaction_error";
    public static final String EVENT_TRANSACTION_COMPLETE = "veritranse_transaction_complete";
    public static final String PAYMENT_STATUS = "payment_status";
    public static final String VERITRANS_RESPONSE = "Android";
    public static final String SETTLEMENT = "settlement";
    //payment types to send in rerquest
    public static final String PAYMENT_EPAY_BRI = "bri_epay";
    public static final String PAYMENT_BANK_TRANSFER = "bank_transfer";
    public static final String PAYMENT_MANDIRI_BILL_PAYMENT = "echannel";
    public static final String PAYMENT_MANDIRI_CLICKPAY="mandiri_clickpay";
    public static final String PAYMENT_CIMB_CLICKS="cimb_clicks";
    public static final String PAYMENT_BCA_CLICK="bca_klikpay";
    public static final String PAYMENT_BBM_MONEY="bbm_money";
    public static final String PAYMENT_INDOSAT_DOMPETKU = "indosat_dompetku";
    public static final String PAYMENT_MANDIRI_ECASH = "mandiri_ecash";
    public static final String PAYMENT_INDOMARET = "cstore";

    public static final String PENDING = "pending";

    //BBM App Installed or not
    public static final String BBM_MONEY_PACKAGE = "com.monitise.client.android.bbmmoney";
    public static final String MARKET_URL = "market://details?id=";
    public static final String PLAY_STORE_URL = "https://play.google.com/store/apps/details?id=";

    //BBM CallBack Urls
    public static final String BBM_PREFIX_URL = "bbmmoney://api/payment/imp?data=";


    public static final String PAYMENT_RESPONSE = "payment_response";

    public static final String PAYMENT_CREDIT_DEBIT = "credit_card";
    public static final String PAYMENT_PERMATA = "permata";
    public static final String DENY = "deny";
    public static final String ERROR_DESCRIPTION_REQUIRED = "add description model";

    public static final String CHALLENGE = "challenge";
    public static final String SUCCESS_CODE_202 = "202";
}