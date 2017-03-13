package com.midtrans.sdk.ui.constants;

/**
 * Created by ziahaqi on 2/27/17.
 */

public class Constants {

    public static final int MAX_ATTEMPT = 3;
    public static final long FADE_IN_FORM_TIME = 300;
    public static final long CARD_ANIMATION_TIME = 400;

    public static final int MONTH_COUNT = 12;

    public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

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
