package com.midtrans.sdk.ui.constants;

/**
 * Created by ziahaqi on 2/27/17.
 */

public class Constants {

    public static final int MAX_ATTEMPT = 3;

    public static class Animation {
        public static final long FADE_IN_FORM_TIME = 300;

        public static final long CARD_ANIMATION_TIME = 400;
    }

    public static class DateTime {
        public static final int MONTH_COUNT = 12;

    }

    public static class IntentCode {
        public static final int PAYMENT = 800;
        public static final int PAYMENT_WEB_INTENT = 100;
        public static final int SCAN_REQUEST_CODE = 788;
    }


    public static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    /**
     *
     */
    public static class PhoneNumber {
        public static final int PHONE_NUMBER_LENGTH = 9;
        public static final int PHONE_NUMBER_MAX_LENGTH = 15;
    }

    public static class WebView {
        public static final String WEB_URL = "weburl";
        public static final String TYPE = "type";
        public static final String CALLBACK_STRING = "/token/callback/";

        public static final String TYPE_CREDIT_CARD = "credit";
        public static final String TYPE_BCA_KLIKPAY = "bca_klikpay";
        public static final String TYPE_MANDIRI_ECASH = "mandiri_ecash";
        public static final String TYPE_CIMB_CLICK = "cimb_click";
        public static final String TYPE_EPAY_BRI = "epay_bri";

        private static final String URL_PARAM = "url_param";
        private static final String TYPE_PARAM = "type_param";
    }

    public class Theme{
        public static final String PRIMARY_COLOR= "primary_color";
        public static final String PRIMARY_DARK_COLOR= "primary_color";
        public static final String SECONDARY_COLOR= "primary_color";
    }
}
