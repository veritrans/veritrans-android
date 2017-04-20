package com.midtrans.sdk.uikit.utilities;

import android.content.Context;
import android.text.TextUtils;

import com.midtrans.sdk.uikit.BuildConfig;
import com.midtrans.sdk.uikit.R;

import java.util.ArrayList;

/**
 * Created by ziahaqi on 4/19/17.
 */

public class MessageUtil {

    private static final String PAID_ORDER_ID = "has been paid";
    private static final String GROSS_AMOUNT_NOT_EQUAL = "is not equal to the sum";
    private static final String GROSS_AMOUNT_REQUIRED = "amount is required";
    private static final String ORDER_ID_REQUIRED = "order_id is required";
    private static final String FLAVOR_DEVELOPMENT = "development";


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
                if (statusMessage.contains(PAID_ORDER_ID)) {
                    message = context.getString(R.string.error_paid_orderid);
                } else if (statusMessage.contains(GROSS_AMOUNT_NOT_EQUAL)) {
                    message = context.getString(R.string.error_gross_amount_not_equal);
                } else if (statusMessage.contains(GROSS_AMOUNT_REQUIRED)) {
                    message = context.getString(R.string.error_gross_amount_required);
                } else if (statusMessage.contains(ORDER_ID_REQUIRED)) {
                    message = context.getString(R.string.error_order_id_required);
                } else if (statusMessage.contains(ORDER_ID_REQUIRED)) {
                    message = context.getString(R.string.error_order_id_required);
                } else {
                    message = context.getString(R.string.txt_error_snap_token);
                }
            }
        }

        return message;
    }

    public static String createMessageWhenCheckoutError(Context context, String statusMessage) {
        String message;

        if (BuildConfig.FLAVOR.equals(FLAVOR_DEVELOPMENT)) {
            if (TextUtils.isEmpty(statusMessage)) {
                message = context.getString(R.string.checkout_error_empty_response);
            } else {
                message = statusMessage;
            }
        } else {
            message = context.getString(R.string.error_unable_to_connect);
        }
        return message;
    }
}
