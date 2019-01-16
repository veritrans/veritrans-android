package com.midtrans.sdk.corekit.utilities;

import android.util.Log;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class, Logger.class})
public class HelperTest {

    private String ccNumberSample = "4811111111111114";
    private String ccFormatedCCNumberSample = "4811 1111 1111 1114 ";

    @Before
    public void setup() {
        PowerMockito.mockStatic(Log.class);
        PowerMockito.mockStatic(Logger.class);
    }

    @Test
    public void test_formatCC_positive() {
        String formattedCC = Helper.getFormattedCreditCardNumber(ccNumberSample);
        Assert.assertEquals(ccFormatedCCNumberSample, formattedCC);
    }

    @Test
    public void test_formatCC_negative() {
        Assert.assertNotEquals(ccFormatedCCNumberSample, null);
    }

    @Test
    public void test_getCardType_positive_whenCardShort() {
        Assert.assertEquals("", Helper.getCardType("1"));
    }

    @Test
    public void test_getCardType_positive_whenCardNull() {
        Assert.assertEquals("", Helper.getCardType(""));
    }

    @Test
    public void test_getCardType_positive_whenVisa() {
        Assert.assertEquals(Helper.CARD_TYPE_VISA, Helper.getCardType("4811"));
    }

    @Test
    public void test_getCardType_positive_whenMasterCard() {
        Assert.assertEquals(Helper.CARD_TYPE_MASTERCARD, Helper.getCardType("5111"));
        Assert.assertEquals(Helper.CARD_TYPE_MASTERCARD, Helper.getCardType("5211"));
        Assert.assertEquals(Helper.CARD_TYPE_MASTERCARD, Helper.getCardType("5311"));
        Assert.assertEquals(Helper.CARD_TYPE_MASTERCARD, Helper.getCardType("5411"));
        Assert.assertEquals(Helper.CARD_TYPE_MASTERCARD, Helper.getCardType("5511"));
    }

    @Test
    public void test_getCard_positive_whenAmex() {
        Assert.assertEquals(Helper.CARD_TYPE_AMEX, Helper.getCardType("3488"));
        Assert.assertEquals(Helper.CARD_TYPE_AMEX, Helper.getCardType("3788"));
    }

    @Test
    public void test_getCard_whenInvalid() {
        Assert.assertEquals("", Helper.getCardType("9999"));
    }


    @Test
    public void test_getValidityTime_whenNull() {
        Assert.assertEquals(null, DateTimeHelper.getValidityTime(null));
    }

    @Test
    public void test_getValidityTime_whenNotNull() {
        Assert.assertNotNull(DateTimeHelper.getValidityTime("2015-10-30 20:32:51"));
    }

    @Test
    public void test_getValidtyTime_whenParseExcetion() {
        String sampleTime = "string error 23423 234";
        Assert.assertEquals(sampleTime, DateTimeHelper.getValidityTime(sampleTime));
        PowerMockito.verifyStatic(Mockito.times(1));
        Logger.error(Matchers.anyString());
    }

}