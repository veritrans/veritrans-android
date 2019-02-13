package com.midtrans.sdk.uikit.utilities;

import android.content.Context;

import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.CheckoutTransaction;
import com.midtrans.sdk.corekit.utilities.Constants;
import com.midtrans.sdk.corekit.utilities.Logger;
import com.midtrans.sdk.uikit.base.callback.PaymentResult;

import static com.midtrans.sdk.corekit.utilities.Constants.MESSAGE_ERROR_INVALID_DATA_SUPPLIED;
import static com.midtrans.sdk.corekit.utilities.Constants.MESSAGE_ERROR_SNAP_TOKEN;

public class MidtransKitHelper {

    public static <T> Boolean isValidForStartMidtransKit(Context context, CheckoutTransaction checkoutTransaction, PaymentResult<T> callback) {
        if (checkoutTransaction == null) {
            callback.onFailed(new Throwable(MESSAGE_ERROR_INVALID_DATA_SUPPLIED));
            return false;
        } else {
            return isValidForStartMidtransKit(context, callback);
        }
    }

    public static <T> Boolean isValidForStartMidtransKit(Context context, String token, PaymentResult<T> callback) {
        if (token == null) {
            callback.onFailed(new Throwable(MESSAGE_ERROR_SNAP_TOKEN));
            return false;
        } else {
            return isValidForStartMidtransKit(context, callback);
        }
    }

    public static <T> Boolean isValidForStartMidtransKit(Context context, PaymentResult<T> callback) {
        if (isParamNotNull(context, callback)) {
            if (NetworkHelper.isNetworkAvailable(context))
                return true;
            else {
                Logger.error(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER);
                return false;
            }
        }
        return false;
    }

    public static <T> Boolean isParamNotNull(Context context, T callback) {
        if (context == null) {
            Logger.error(Constants.MESSAGE_ERROR_MISSING_CONTEXT);
            return false;
        }

        if (callback == null) {
            Logger.error(Constants.MESSAGE_ERROR_CALLBACK_UNIMPLEMENTED);
            return false;
        }
        return true;
    }

}