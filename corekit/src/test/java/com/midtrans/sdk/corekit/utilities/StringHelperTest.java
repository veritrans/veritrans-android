package com.midtrans.sdk.corekit.utilities;

import android.util.Log;

import com.midtrans.sdk.corekit.base.enums.Currency;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class, Logger.class})
public class StringHelperTest {

    @Before
    public void setup() {
        PowerMockito.mockStatic(Log.class);
        PowerMockito.mockStatic(Logger.class);
    }

    @Test
    public void test_checkCurrency_positive() {
        Assert.assertEquals(Currency.IDR, StringHelper.checkCurrency("unknown"));
        Assert.assertEquals(Currency.IDR, StringHelper.checkCurrency("IDR"));
        Assert.assertEquals(Currency.SGD, StringHelper.checkCurrency("SGD"));
    }

    @Test
    public void test_checkCurrency_negative() {
        Assert.assertNotEquals(Currency.SGD, StringHelper.checkCurrency("IDR"));
        Assert.assertNotEquals(Currency.SGD, StringHelper.checkCurrency("unknown"));
        Assert.assertNotEquals(Currency.SGD, StringHelper.checkCurrency("IDR"));
        Assert.assertNotEquals(Currency.IDR, StringHelper.checkCurrency("SGD"));
    }

    @Test
    public void test_isValidURL_positive() {
        Assert.assertTrue(StringHelper.isValidURL("https://www.google.com"));
    }

    @Test
    public void test_isValidURL_negative() {
        Assert.assertFalse(StringHelper.isValidURL("unknown"));
    }

    @Test
    public void test_formattedAmount_positive() {
        Assert.assertEquals("20,000", StringHelper.getFormattedAmount(20000));
        Assert.assertEquals("1,000", StringHelper.getFormattedAmount(1000.0));
    }

}