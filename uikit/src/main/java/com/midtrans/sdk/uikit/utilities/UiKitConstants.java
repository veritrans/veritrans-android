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
    public static final int INTENT_CODE_PAYMENT_STATUS = 210;
    public static final int INTENT_CODE_PAYMENT = 108;
    public static final int INTENT_VERIFICATION = 110;
    public static final int INTENT_WEBVIEW_PAYMENT = 111;


    public static final String KEY_USER_DETAILS = "user_details";


    //environment
    public static final String ENVIRONMENT_DEVELOPMENT = "development";
    public static final String ENVIRONMENT_PRODUCTION = "production";

    // response status code
    public static final String STATUS_CODE_200 = "200";
    public static final String STATUS_CODE_201 = "201";
    public static final String STATUS_CODE_400 = "400";
    public static final String STATUS_CODE_404 = "404";
    public static final String STATUS_CODE_500 = "500";
    public static final String STATUS_CODE_503 = "503";
    public static final String STATUS_CODE_504 = "504";


    // payment status
    public static final String STATUS_SUCCESS = "success";
    public static final String STATUS_PENDING = "pending";
    public static final String STATUS_FAILED = "failed";
    public static final String STATUS_INVALID = "invalid";
    public static final String STATUS_ERROR = "error";
    public static final String STATUS_SETTLEMENT = "settlement";
    public static final String STATUS_CHALLENGE = "challenge";
    public static final String STATUS_DENY = "deny";


    // webview url callback pattern
    public static final String CALLBACK_PATTERN_RBA = "/token/rba/callback/";
    public static final String CALLBACK_PATTERN_3DS = "/token/callback/";
    public static final String CALLBACK_DANAMON_ONLINE = "/callback?signature=";
    public static final String CALLBACK_CIMB_CLICKS = "cimb-clicks/response";
    public static final String CALLBACK_BRI_EPAY = "briPayment?tid=";
    public static final String CALLBACK_MANDIRI_ECAH = "notify?id=";
    public static final String CALLBACK_BCA_KLIKPAY = "?id=";

    public static final int MONTH_COUNT = 12;

    // atm network
    public static final int ATM_BERSAMA = 0;
    public static final int PRIMA = 1;
    public static final int ALTO = 2;

    public static final int RESULT_SDK_NOT_AVAILABLE = -999;

}
