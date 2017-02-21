package com.midtrans.sdk.core.utils;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.junit.Assert;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rakawm on 2/20/17.
 */
public class UtilitiesTest {
    @Test
    public void buildGson() throws Exception {
        Gson gson = Utilities.buildGson();
        Assert.assertNotNull(gson);
        Assert.assertEquals(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES, gson.fieldNamingStrategy());
    }

    @Test
    public void createCustomMapObject() throws Exception {
        Map<String, String> maps = new HashMap<>();
        maps.put("key1", "value1");
        maps.put("key2", "value2");
        JsonObject custom = (JsonObject) Utilities.createCustomMapObject(maps);
        Assert.assertEquals("value1", custom.get("key1").getAsString());
        Assert.assertEquals("value2", custom.get("key2").getAsString());
    }

    @Test
    public void getFormattedTime() throws Exception {
        long time = 1487554274000L;
        String formattedTime = Utilities.getFormattedTime(time);
        Assert.assertEquals("2017-02-20 08:31:14 +0700", formattedTime);

    }

    @Test
    public void generateRandomNumber() throws Exception {
        int number1 = Utilities.generateRandomNumber();
        int number2 = Utilities.generateRandomNumber();
        int number3 = Utilities.generateRandomNumber();
        Assert.assertTrue(number1 < 1000000);
        Assert.assertTrue(number2 < 1000000);
        Assert.assertTrue(number3 < 1000000);
    }

}