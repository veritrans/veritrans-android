package com.midtrans.sdk.uikit.utilities;

import android.content.Context;

import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.PaymentException;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.BuildConfig;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.models.MessageInfo;

import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import retrofit2.HttpException;

/**
 * Created by ziahaqi on 4/19/17.
 */

public class MessageUtil {
    private static String TAG = MessageUtil.class.getSimpleName();

    private static final String PAID_ORDER_ID = "has been paid";
    public static final String PROCESSED_ORDER_ID = "transaction has been processed";
    private static final String GROSS_AMOUNT_NOT_EQUAL = "is not equal to the sum";
    private static final String GROSS_AMOUNT_REQUIRED = "amount is required";
    private static final String ORDER_ID_REQUIRED = "order_id is required";
    public static final String TIME_OUT = "timeout";
    public static final String TIMED_OUT = "timed out";
    private static final String FLAVOR_DEVELOPMENT = "development";
    public static final String TIMEOUT = "timeout";
    public static final String STATUS_UNSUCCESSFUL = "payment has not been made";
    public static final String PROMO_UNAVAILABLE = "promo is not available";
    public static final String CURRENCY_NOT_INCLUDED = "Currency is not included";


    public static String createMessageWhenCheckoutFailed(Context context, ArrayList<String> statusMessage) {
        String message = context.getString(R.string.error_message_status_code_400);

        if (statusMessage != null && !statusMessage.isEmpty()) {
            if (BuildConfig.FLAVOR.equals(FLAVOR_DEVELOPMENT)) {
                message = statusMessage.get(0);
            } else {
                if (statusMessage.contains(PAID_ORDER_ID) || statusMessage.contains(PROCESSED_ORDER_ID)) {
                    message = context.getString(R.string.error_message_status_code_406);
                } else if (statusMessage.contains(GROSS_AMOUNT_NOT_EQUAL)) {
                    message = context.getString(R.string.error_gross_amount_not_equal);
                } else if (statusMessage.contains(GROSS_AMOUNT_REQUIRED)) {
                    message = context.getString(R.string.error_gross_amount_required);
                } else if (statusMessage.contains(ORDER_ID_REQUIRED)) {
                    message = context.getString(R.string.error_order_id_required);
                } else if (isTimeOut(statusMessage.get(0))) {
                    message = context.getString(R.string.timeout_message);
                } else if (statusMessage.contains(CURRENCY_NOT_INCLUDED)) {
                    message = context.getString(R.string.currency_invalid);
                }
            }
        }

        return message;
    }

    public static MessageInfo createPaymentFailedMessage(Context context, TransactionResponse response) {
        PaymentException exception = new PaymentException(
                response.getStatusCode(),
                response.getStatusMessage(),
                new Throwable(response.getStatusMessage()));

        return createMessageOnError(exception, context);
    }

    public static MessageInfo createMessageOnError(Throwable throwable, Context context) {
        String errorMessageDetails = context.getString(R.string.error_message_others);

        try {
            if (BuildConfig.FLAVOR.equals(FLAVOR_DEVELOPMENT)) {
                errorMessageDetails = throwable.getMessage();
            } else {
                if (throwable instanceof HttpException) {
                    int statusCode = ((HttpException) throwable).code();
                    String message = ((HttpException) throwable).message();

                    errorMessageDetails = getErrorMessage(String.valueOf(statusCode), message, context);
                } else if (throwable instanceof PaymentException) {
                    String statusCode = ((PaymentException) throwable).statusCode;
                    String message = throwable.getMessage();

                    errorMessageDetails = getErrorMessage(statusCode, message, context);
                } else if (throwable instanceof TimeoutException) {
                    errorMessageDetails = context.getString(R.string.timeout_message);
                }
            }
        } catch (RuntimeException e) {
            Logger.e(TAG, e.getMessage());
        }

        return new MessageInfo(
                context.getString(R.string.failed_title),
                errorMessageDetails
        );
    }

    private static String getErrorMessage(String statusCode, String defaultErrorMessage, Context context) {
        String errorMessageDetails;

        switch (String.valueOf(statusCode)) {
            case UiKitConstants.STATUS_CODE_400:
                errorMessageDetails = context.getString(R.string.error_message_status_code_400);
                break;
            case UiKitConstants.STATUS_CODE_411:
                if (isTimeOut(defaultErrorMessage)) {
                    errorMessageDetails = context.getString(R.string.timeout_message);
                } else {
                    errorMessageDetails = context.getString(R.string.details_message_invalid);
                }
                break;
            case UiKitConstants.STATUS_CODE_406:
                errorMessageDetails = context.getString(R.string.error_message_status_code_406);
                break;
            case UiKitConstants.STATUS_CODE_407:
                errorMessageDetails = context.getString(R.string.error_message_status_code_407);
                break;
            case UiKitConstants.STATUS_CODE_500:
                errorMessageDetails = context.getString(R.string.error_message_status_code_500);
                break;
            case UiKitConstants.STATUS_CODE_502:
                errorMessageDetails = context.getString(R.string.error_message_status_code_502);
                break;
            default:
                errorMessageDetails = context.getString(R.string.error_message_others);
                break;
        }

        return errorMessageDetails;
    }

    private static boolean isTimeOut(String errorMessage) {
        return errorMessage.contains(TIMED_OUT) ||
                errorMessage.contains(TIMEOUT) ||
                errorMessage.equals(TIME_OUT);
    }
}
