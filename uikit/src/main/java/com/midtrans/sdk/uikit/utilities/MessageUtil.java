package com.midtrans.sdk.uikit.utilities;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.utilities.Utils;
import com.midtrans.sdk.uikit.BuildConfig;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.models.MessageInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ziahaqi on 4/19/17.
 */

public class MessageUtil {
    private static String TAG = MessageUtil.class.getSimpleName();

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
    public static final String RETROFIT_TIMEOUT = "timed out";
    public static final String MAINTENANCE = "maintenance";
    public static final String STATUS_UNSUCCESSFUL = "payment has not been made";
    private static final String NOT_ACCEPTABLE = "406 not acceptable";
    private static final CharSequence PAYMENT_EXIPIRED = "expired";
    private static final String NOT_FOUND = "404 not found";
    private static final String UNABLE_RESOLVE_HOST = "unable to resolve host";
    private static final String PAYMENT_NOT_ENABLED = "is not enabled";
    private static final String USER_ID_LESS = "less than or equal";
    private static final String USER_ID_INVALID = "user_id format is invalid";
    private static final String DENY = "deny";


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
        } else if (!TextUtils.isEmpty(statusCode) && statusCode.equals(UiKitConstants.STATUS_CODE_500)) {
            message = context.getString(R.string.message_error_internal_server);
        } else {
            if (errorMessage.contains(TIME_OUT) || errorMessage.contains(TIMED_OUT)) {
                message = context.getString(R.string.timeout_message);
            }
        }

        return message;
    }

    public static MessageInfo createpaymentFailedMessage(Context context, TransactionResponse response, String defaultMessage) {
        MessageInfo message = new MessageInfo(null, null, context.getString(R.string.payment_failed));

        if (!TextUtils.isEmpty(defaultMessage)) {
            message = new MessageInfo(null, null, defaultMessage);
        }

        try {
            if (response != null) {
                if (BuildConfig.FLAVOR.equals(FLAVOR_DEVELOPMENT)) {

                    if (response.getValidationMessages() != null && !response.getValidationMessages().isEmpty()) {

                        message = new MessageInfo(response.getStatusCode(), context.getString(R.string.status_message_invalid),
                                response.getValidationMessages().get(0));

                    } else if (!TextUtils.isEmpty(response.getStatusMessage())) {

                        message = new MessageInfo(response.getStatusCode(), context.getString(R.string.status_message_invalid),
                                response.getStatusMessage());
                    }

                } else {
                    String statusCode = response.getStatusCode();
                    if (!TextUtils.isEmpty(statusCode)) {
                        if (statusCode.contains(UiKitConstants.STATUS_CODE_400)) {

                            List<String> validationMessages = response.getValidationMessages();
                            if (validationMessages != null && !validationMessages.isEmpty()) {
                                String validationMessage = validationMessages.get(0);
                                if (!TextUtils.isEmpty(validationMessage) && validationMessage.toLowerCase().contains(PAYMENT_EXIPIRED)) {
                                    message = new MessageInfo(statusCode, context.getString(R.string.status_message_expired),
                                            context.getString(R.string.details_message_expired));
                                } else if (!TextUtils.isEmpty(validationMessage) && validationMessage.toLowerCase().contains(PAYMENT_NOT_ENABLED)) {
                                    message = new MessageInfo(statusCode, context.getString(R.string.status_message_not_enabled),
                                            context.getString(R.string.details_message_not_enabled));
                                } else if (!TextUtils.isEmpty(validationMessage) && (validationMessage.toLowerCase().contains(USER_ID_INVALID)
                                        || validationMessage.toLowerCase().contains(USER_ID_LESS))) {
                                    message = new MessageInfo(statusCode, context.getString(R.string.status_message_userid_invalid),
                                            context.getString(R.string.details_message_userid_invalid));
                                } else {
                                    message = new MessageInfo(statusCode, context.getString(R.string.status_message_invalid),
                                            context.getString(R.string.details_message_invalid));
                                }
                            } else {
                                message = new MessageInfo(statusCode, context.getString(R.string.status_message_invalid),
                                        context.getString(R.string.details_message_invalid));
                            }
                        } else if (!TextUtils.isEmpty(response.getTransactionStatus())) {
                            if (response.getTransactionStatus().equals(DENY)) {
                                message = new MessageInfo(statusCode, context.getString(R.string.deny),
                                        context.getString(R.string.message_payment_denied));
                            } else {
                                message = new MessageInfo(statusCode, context.getString(R.string.status_message_invalid),
                                        context.getString(R.string.details_message_invalid));
                            }
                        } else if (statusCode.contains(UiKitConstants.STATUS_CODE_503) || statusCode.contains(UiKitConstants.STATUS_CODE_504)) {
                            message = new MessageInfo(statusCode, context.getString(R.string.status_message_internal_error),
                                    context.getString(R.string.message_error_internal_server));
                        }
                    }
                }
            }

        } catch (RuntimeException e) {
            Log.e(TAG, "createpaymentFailedMessage():" + e.getMessage());
        }

        return message;
    }

    public static MessageInfo createMessageOnError(Context context, Throwable error, String defaultMessage) {
        MessageInfo message = new MessageInfo(null, context.getString(R.string.status_message_payment_error), context.getString(R.string.detail_message_payment_error));

        if (!TextUtils.isEmpty(defaultMessage)) {
            message = new MessageInfo(null, context.getString(R.string.status_message_payment_error), defaultMessage);
        }

        try {
            if (BuildConfig.FLAVOR.equals(FLAVOR_DEVELOPMENT)) {

                if (error != null && !TextUtils.isEmpty(error.getMessage())) {
                    message = new MessageInfo(null, context.getString(R.string.failed_title), error.getMessage());
                }

            } else {
                if (error != null) {
                    if (!TextUtils.isEmpty(error.getMessage())) {
                        String errorMessage = error.getMessage().toLowerCase();
                        if (errorMessage.contains(TOKEN_NOT_FOUND)) {

                            message = new MessageInfo(UiKitConstants.STATUS_CODE_404,
                                    context.getString(R.string.status_message_token_notfound),
                                    context.getString(R.string.detail_message_token_notfound));

                        } else if (!Utils.isNetworkAvailable(context)) {

                            message = new MessageInfo(null,
                                    context.getString(R.string.status_message_network_unavailable),
                                    context.getString(R.string.detail_message_network_unavailable));

                        } else if (errorMessage.contains(NOT_FOUND)) {

                            message = new MessageInfo(UiKitConstants.STATUS_CODE_404,
                                    context.getString(R.string.status_message_not_found),
                                    context.getString(R.string.detail_message_not_found));

                        } else if (errorMessage.contains(TIMEOUT) || errorMessage.contains(RETROFIT_TIMEOUT)) {

                            message = new MessageInfo(null,
                                    context.getString(R.string.failed_title), context.getString(R.string.timeout_message));

                        } else if (errorMessage.contains(UNABLE_RESOLVE_HOST)) {

                            message = new MessageInfo(null,
                                    context.getString(R.string.failed_title), context.getString(R.string.timeout_message));

                        }
                    }
                }
            }
        } catch (RuntimeException e) {
            Log.e(TAG, "createMessageOnError():" + e.getMessage());
        }

        return message;
    }
}
