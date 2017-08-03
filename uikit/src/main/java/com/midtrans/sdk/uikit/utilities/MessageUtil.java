package com.midtrans.sdk.uikit.utilities;

import android.content.Context;
import android.text.TextUtils;

import com.midtrans.sdk.uikit.BuildConfig;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.activities.MandiriClickPayActivity;

import java.util.ArrayList;

/**
 * Created by ziahaqi on 4/19/17.
 */

public class MessageUtil {

    private static final String PAID_ORDER_ID = "has been paid";
    private static final String PROCESSED_ORDER_ID = "transaction has been processed";
    private static final String TOKEN_NOT_FOUND = "token not found";
    private static final String GROSS_AMOUNT_NOT_EQUAL = "is not equal to the sum";
    private static final String GROSS_AMOUNT_REQUIRED = "amount is required";
    private static final String ORDER_ID_REQUIRED = "order_id is required";
    public static final String TIME_OUT = "timeout";
    public static final String TIMED_OUT = "timed out";
    private static final String FLAVOR_DEVELOPMENT = "development";
    public static final String TIMEOUT = "timeout";
    public static final String MAINTENANCE = "maintenance";
    public static final String RETROFIT_TIMEOUT = "timed out";
    public static final String STATUS_UNSUCCESSFUL = "Payment has not been made";



    public static String createMessageWhenCheckoutFailed(Context context, ArrayList<String> statusMessage) {
        String message;

        if (BuildConfig.FLAVOR.equals(FLAVOR_DEVELOPMENT)) {
            if (statusMessage == null || statusMessage.isEmpty()) {
                message = context.getString(R.string.checkout_error_empty_response);
            } else {
                message = statusMessage.get(0);
            }
        } else {
            if (statusMessage == null || statusMessage.isEmpty()) {
                message = context.getString(R.string.txt_error_snap_token);
            } else {
                if (statusMessage.contains(PAID_ORDER_ID) || statusMessage.contains(PROCESSED_ORDER_ID)) {
                    message = context.getString(R.string.error_paid_orderid);
                } else if (statusMessage.contains(GROSS_AMOUNT_NOT_EQUAL)) {
                    message = context.getString(R.string.error_gross_amount_not_equal);
                } else if (statusMessage.contains(GROSS_AMOUNT_REQUIRED)) {
                    message = context.getString(R.string.error_gross_amount_required);
                } else if (statusMessage.contains(ORDER_ID_REQUIRED)) {
                    message = context.getString(R.string.error_order_id_required);
                } else if (statusMessage.contains(ORDER_ID_REQUIRED)) {
                    message = context.getString(R.string.error_order_id_required);
                } else if (statusMessage.contains(TIMEOUT) || statusMessage.contains(RETROFIT_TIMEOUT)) {
                    message = context.getString(R.string.timeout_message);
                } else {
                    message = context.getString(R.string.txt_error_snap_token);
                }
            }
        }

        return message;
    }

    public static String createMessageWhenCheckoutError(Context context, String statusMessage, String defaultMessage) {
        String message;

        if (BuildConfig.FLAVOR.equals(FLAVOR_DEVELOPMENT)) {
            if (TextUtils.isEmpty(statusMessage)) {
                message = context.getString(R.string.checkout_error_empty_response);
            } else {
                message = statusMessage;
            }
        } else {
            if (!TextUtils.isEmpty(statusMessage) && statusMessage.contains(TIMEOUT)) {
                message = context.getString(R.string.timeout_message);
            } else {
                message = defaultMessage;
            }
        }
        return message;
    }

    public static String createPaymentErrorMessage(Context context, String errorMessage, String defaultMessage) {
        String message;
        if (!TextUtils.isEmpty(defaultMessage)) {
            message = defaultMessage;
        } else {
            message = context.getString(R.string.payment_failed);
        }

        if (errorMessage == null) {
            message = context.getString(R.string.error_empty_response);
        } else {
            if (errorMessage.contains(TIME_OUT) || errorMessage.contains(TIMED_OUT)) {
                message = context.getString(R.string.timeout_message);
            }
        }

        return message;
    }

    public static String createpaymentFailedMessage(Context context, String statusCode, String errorMessage, String defaultMessage) {
        String message;
        if (!TextUtils.isEmpty(defaultMessage)) {
            message = defaultMessage;
        } else {
            message = context.getString(R.string.payment_failed);
        }

        if (errorMessage == null) {
            message = context.getString(R.string.error_empty_response);
        } else if(!TextUtils.isEmpty(statusCode) && statusCode.equals(UiKitConstants.STATUS_CODE_500)){
            message = context.getString(R.string.message_error_internal_server);
        }
        else {
            if (errorMessage.contains(TIME_OUT) || errorMessage.contains(TIMED_OUT)) {
                message = context.getString(R.string.timeout_message);
            }
        }

        return message;
    }
}
