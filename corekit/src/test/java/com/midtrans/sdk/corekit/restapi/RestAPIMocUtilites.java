package com.midtrans.sdk.corekit.restapi;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by ziahaqi on 23/06/2016.
 */
public class RestAPIMocUtilites {

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

    public static String getStringFromFile(ClassLoader loader, String filePath) throws Exception {
        final InputStream stream = loader.getResourceAsStream(filePath);
        String ret = convertStreamToString(stream);
        stream.close();
        return ret;
    }

    public static RetrofitMockClient getClient(ClassLoader loader, final int httpStatusCode, String reason, String responseFileName) throws Exception {
        return new RetrofitMockClient(httpStatusCode, reason, getStringFromFile(loader, responseFileName));
    }

    public static <T> T getSampleDataFromFile(ClassLoader loader, Class<T> tClass, String path) throws Exception {
        final InputStream stream = loader.getResourceAsStream(path);
        String ret = convertStreamToString(stream);
        stream.close();
        Gson gson = new Gson();
        T data = gson.fromJson(ret, tClass);
        return data;
    }
}
