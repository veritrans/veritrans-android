package com.midtrans.sdk.core.utils;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

import android.support.annotation.NonNull;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;

/**
 * Created by rakawm on 10/19/16.
 */

public class Utilities {

    /**
     * Build custom Gson instance. The field naming policy is set to LOWER_CASE_WITH_UNDERSCORES.
     *
     * @return Gson instance
     */
    public static Gson buildGson() {
        return new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
    }

    /**
     * Create {@link JsonObject} object for custom key-values map.
     *
     * @param map custom key-values map.
     * @return {@link JsonObject} object of key-values pairs.
     */
    public static Object createCustomMapObject(@NonNull Map<String, String> map) {
        JsonObject object = new JsonObject();
        for (Map.Entry<String, String> entry : map.entrySet()) {
            object.addProperty(entry.getKey(), entry.getValue());
        }
        return object;
    }

    /**
     * Get ISO 8601 formatted time.
     *
     * @param time epoch time in milliseconds.
     * @return ISO 8601 formatted time.
     */
    public static String getFormattedTime(long time) {
        // Quoted "Z" to indicate UTC, no timezone offset
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
        df.setTimeZone(TimeZone.getTimeZone("Asia/Jakarta"));
        return df.format(new Date(time));
    }

    /**
     * It will generate random 5 digit number. It is used as input3 in mandiri click pay.
     *
     * @return random number
     */
    public static int generateRandomNumber() {
        int number;
        int high = 99999;
        int low = 10000;
        byte[] randomBytes = new byte[128];

        try {
            SecureRandom secureRandomGenerator = SecureRandom.getInstance("SHA1PRNG");
            secureRandomGenerator.nextBytes(randomBytes);
            number = secureRandomGenerator.nextInt(high - low) + low;
        } catch (NoSuchAlgorithmException e) {
            Random random = new Random();
            number = random.nextInt(high - low) + low;
        }
        return number;
    }
}
