package com.midtrans.sdk.uikit.utilities;

/**
 * Created by ziahaqi on 7/23/17.
 */

public class UiKitConstants {

    public static final int MAX_ATTEMPT = 2;

    // intent request code
    public static final int INTENT_CARD_DETAILS = 501;
    public static final int INTENT_RESULT_DELETE_CARD = 503;
    public static final int INTENT_BANK_POINT = 504;
    public static final int INTENT_CODE_3DS_PAYMENT = 100;
    public static final int INTENT_CODE_RBA_AUTHENTICATION = 102;
    public static final int INTENT_REQUEST_SCAN_CARD = 101;


    public static final String KEY_USER_DETAILS = "user_details";


    //environment
    public static final String ENVIRONMENT_DEVELOPMENT = "development";
    public static final String ENVIRONMENT_PRODUCTION = "production";

    // payment status
    public static final String STATUS_SUCCESS = "success";
    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_FAILED = "failed";
    public static final String STATUS_INVALID = "invalid";
    public static final String STATUS_ERROR = "error";
    public static final String STATUS_SETTLEMENT = "settlement";
    public static final String STATUS_CHALLENGE = "challenge";
    public static final String STATUS_DENY = "deny";

    public static final String STATUS_CODE_500 = "500";


    // webview url callback pattern
    public static final String CALLBACK_PATTERN_RBA = "/token/rba/callback/";
    public static final String CALLBACK_PATTERN_3DS = "/token/callback/";
}
