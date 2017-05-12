package com.midtrans.sdk.ui.constants;

/**
 * Created by ziahaqi on 2/27/17.
 */

public class Constants {

    public static final int MAX_ATTEMPT = 3;
    public static final long FADE_IN_FORM_TIME = 300;
    public static final long CARD_ANIMATION_TIME = 400;

    public static final int MONTH_COUNT = 12;

    public static final String EMAIL_PATTERN = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

    public static final int PHONE_NUMBER_LENGTH = 9;
    public static final int PHONE_NUMBER_MAX_LENGTH = 15;

    /**
     * web view params
     */
    public static final String WEB_VIEW_CALLBACK_STRING = "/token/callback/";
    public static final String WEB_VIEW_URL = "weburl";
    public static final String WEB_VIEW_PARAM_TYPE = "type";

    /**
     * card click mode
     */
    public static final String CREDIT_CARD_ONE_CLICK = "one_click";
    public static final String CREDIT_CARD_TWO_CLICKS = "two_clicks";

    public static final String PAYMENT_RESULT = "payment_result";

    public static final int INTENT_CODE_PAYMENT = 800;
    public static final int INTENT_CODE_WEB_PAYMENT = 100;
    public static final int INTENT_CODE_SCAN_REQUEST = 788;
}
