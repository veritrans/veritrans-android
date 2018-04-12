package com.midtrans.sdk.corekit.core;

/**
 * It contains all constant and sdk related static information. Created by chetan on 16/10/15.
 */
public class Constants {

    // sdk preference migration settings

    public static final int PREFERENCES_VERSION = 1;


    public static final int PAYMENT_METHOD_NOT_SELECTED = -1;

    public static final int PAYMENT_METHOD_CREDIT_OR_DEBIT = 1;
    public static final int PAYMENT_METHOD_MANDIRI_CLICK_PAY = 2;

    public static final int PAYMENT_METHOD_INDOSAT_DOMPETKU = 6;
    public static final int PAYMENT_METHOD_MANDIRI_ECASH = 7;
    public static final int PAYMENT_METHOD_PERMATA_VA_BANK_TRANSFER = 8;

    public static final int PAYMENT_METHOD_MANDIRI_BILL_PAYMENT = 9;
    public static final int PAYMENT_METHOD_INDOMARET = 10;
    public static final int PAYMENT_METHOD_KLIKBCA = 11;
    public static final int PAYMENT_METHOD_TELKOMSEL_CASH = 12;
    public static final int PAYMENT_METHOD_XL_TUNAI = 13;
    public static final int PAYMENT_METHOD_BANK_TRANSFER_ALL_BANK = 14;
    public static final int PAYMENT_METHOD_KIOSON = 15;
    public static final int PAYMENT_METHOD_GCI = 16;

    public static final int BANK_TRANSFER_BCA = 1001;
    public static final int BANK_TRANSFER_PERMATA = 1003;
    public static final int BANK_TRANSFER_BNI = 1004;

    public static final String TAG = "MidtransSDK";

    public static final int PHONE_NUMBER_LENGTH = 9;
    public static final int PHONE_NUMBER_MAX_LENGTH = 15;
    public static final int ZIPCODE_LENGTH = 5;


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
     * regex for email id.
     */
    public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    /**
     * If trying to create api request before initializing sdk.
     */
    public static final String ERROR_SDK_IS_NOT_INITIALIZED = "sdk is not initialized.";


    public static final String WEBURL = "weburl";
    public static final String TYPE = "type";

    /**
     * result code used for payment transfer activities
     */
    public static final int RESULT_CODE_PAYMENT_TRANSFER = 5102;
    //payment types to send in rerquest
    public static final String PAYMENT_EPAY_BRI = "bri_epay";
    public static final String PAYMENT_CIMB_CLICKS = "cimb_clicks";

    public static final String PAYMENT_CREDIT_DEBIT = "credit_card";
    public static final long FADE_IN_FORM_TIME = 300;

    public static final String AUTH_TOKEN = "authentication.token";
    public static final String WEBVIEW_REDIRECT_URL = "redirect_url";

    // status code
    public static final String STATUS_CODE_200 = "200";
    public static final String STATUS_CODE_201 = "201";
    public static final String STATUS_CODE_400 = "400";

    // messages
    public static final String MESSAGE_ERROR_EMPTY_RESPONSE = "Failed to retrieve response from server";
    public static final String MESSAGE_ERROR_EMPTY_MERCHANT_URL = "Merchant base url is empty. Please set merchant base url on SDK";
    public static final String MESSAGE_ERROR_INVALID_DATA_SUPPLIED = "Invalid data supplied to SDK.";
    public static final String MESSAGE_ERROR_CALLBACK_UNIMPLEMENTED = "Callback Unimplemented";
    public static final String MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER = "Failed to connect to server.";
    public static final String MESSAGE_ERROR_ALREADY_RUNNING = "Failed to connect to server.";

    public static final String KEY_PREFERENCES_VERSION = "preferences.version";
    public static final String USER_DETAILS = "user_details";
}