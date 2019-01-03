package com.midtrans.sdk.corekit.utilities;

import android.content.Context;

import com.midtrans.sdk.corekit.base.callback.MidtransCallback;

import java.util.Collection;
import java.util.Map;

public class Validation {

    private static final String TAG = "Validation";

    public static <T> Boolean isValidForNetworkCall(Context context, MidtransCallback<T> callback) {
        if (context == null) {
            Logger.error(TAG, Constants.MESSAGE_ERROR_MISSING_CONTEXT);
        }

        if (callback == null) {
            Logger.error(TAG, Constants.MESSAGE_ERROR_CALLBACK_UNIMPLEMENTED);
            return false;
        }

        if (!isNetworkAvailable(context)) {
            callback.onFailed(new Throwable(Constants.MESSAGE_ERROR_FAILED_TO_CONNECT_TO_SERVER));
            return false;
        } else {
            return true;
        }
    }

    /**
     * Open utils for checking network status
     *
     * @return boolean based on network status
     */
    public static boolean isNetworkAvailable(Context context) {
        return NetworkHelper.isNetworkAvailable(context);
    }

    /**
     * This method returns true if the collection is null or is empty.
     *
     * @param collection
     * @return true | false
     */
    public static boolean isEmpty(Collection<?> collection) {
        if (collection == null || collection.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * This method returns true of the map is null or is empty.
     *
     * @param map
     * @return true | false
     */
    public static boolean isEmpty(Map<?, ?> map) {
        if (map == null || map.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * This method returns true if the objet is null.
     *
     * @param object
     * @return true | false
     */
    public static boolean isEmpty(Object object) {
        if (object == null) {
            return true;
        }
        return false;
    }

    /**
     * This method returns true if the input array is null or its length is zero.
     *
     * @param array
     * @return true | false
     */
    public static boolean isEmpty(Object[] array) {
        if (array == null || array.length == 0) {
            return true;
        }
        return false;
    }

    /**
     * This method returns true if the input string is null or its length is zero.
     *
     * @param string
     * @return true | false
     */
    public static boolean isEmpty(String string) {
        if (string == null || string.trim().length() == 0) {
            return true;
        }
        return false;
    }
}
