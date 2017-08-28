package com.midtrans.sdk.corekit.core;

import android.content.SharedPreferences;

import com.google.gson.Gson;

/**
 * @author rakawm
 */
public class LocalDataHandler {

    private static String convertObjectIntoJson(Object object) {
        Gson gson = new Gson();
        return gson.toJson(object);
    }

    private static <T> T convertBackToObject(String json, Class<T> classType) {
        Gson gson = new Gson();
        return gson.fromJson(json, classType);
    }

    /**
     * Save specified string into shared preferences.
     *
     * @param key   file name.
     * @param value string to store.
     */
    public static void saveString(String key, String value) {
        SharedPreferences preferences = MidtransSDK.getmPreferences();
        if (preferences != null) {
            preferences.edit().putString(key, value).apply();
        }
    }

    /**
     * Get specified string object from shared preferences.
     *
     * @param key file name.
     * @return string value.
     */
    public static String readString(String key) {
        SharedPreferences preferences = MidtransSDK.getmPreferences();
        return preferences == null ? null : preferences.getString(key, "");
    }

    /**
     * Save specified object into shared preferences. Object will be converted into json string
     * first before saved.
     *
     * @param key    file name.
     * @param object object to store.
     */
    public static void saveObject(String key, Object object) {
        SharedPreferences preferences = MidtransSDK.getmPreferences();
        if (preferences != null) {
            preferences.edit().putString(key, convertObjectIntoJson(object)).apply();
        }
    }

    /**
     * Get specified string object from shared preferences.
     *
     * @param key file name.
     * @return json string converted object.
     */
    public static <T> T readObject(String key, Class<T> classType) {
        SharedPreferences preferences = MidtransSDK.getmPreferences();
        return preferences == null ? null : convertBackToObject(preferences.getString(key, ""), classType);
    }

}
