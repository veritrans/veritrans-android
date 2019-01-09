package com.midtrans.sdk.corekit.utilities;

import java.util.Collection;
import java.util.Map;

public class ValidationHelper {

    private static final String TAG = "ValidationHelper";

    /**
     * This method returns true if the collection is null or is empty.
     *
     * @return true | false
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        if (collection != null || !collection.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * This method returns true of the map is null or is empty.
     *
     * @return true | false
     */
    public static boolean isNotEmpty(Map<?, ?> map) {
        if (map != null || !map.isEmpty()) {
            return true;
        }
        return false;
    }

    /**
     * This method returns true if the objet is null.
     *
     * @return true | false
     */
    public static boolean isNotEmpty(Object object) {
        return object != null;
    }

    /**
     * This method returns true if the input array is null or its length is zero.
     *
     * @return true | false
     */
    public static boolean isNotEmpty(Object[] array) {
        if (array != null || array.length != 0) {
            return true;
        }
        return false;
    }

    /**
     * This method returns true if the input string is null or its length is zero.
     *
     * @return true | false
     */
    public static boolean isNotEmpty(String string) {
        if (string != null || string.trim().length() != 0) {
            return true;
        }
        return false;
    }
}