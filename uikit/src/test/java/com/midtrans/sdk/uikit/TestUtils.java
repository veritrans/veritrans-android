package com.midtrans.sdk.uikit;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

/**
 * Created by ziahaqi on 1/19/17.
 */

public class TestUtils {

    public class JsonIntegerStringConversion implements JsonSerializer {

        @Override
        public JsonElement serialize(Object src, Type typeOfSrc, JsonSerializationContext context) {
            return null;
        }
    }

    public static <T> T getJsonDataFromFile(ClassLoader loader, Class<T> tClass, String path) throws Exception {
        final InputStream stream = loader.getResourceAsStream(path);
        String ret = convertStreamToString(stream);
        stream.close();
        Gson gson = new Gson();
        T data = gson.fromJson(ret, tClass);
        return data;
    }

    public static <T> T getJsonDataFromMapFile(ClassLoader loader, Type jsonType, String path) throws Exception {
        final InputStream stream = loader.getResourceAsStream(path);
        String ret = convertStreamToString(stream);
        stream.close();
        Gson gson = new Gson();
        T data = gson.fromJson(ret, jsonType);
        return data;
    }

    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }
}
