package com.midtrans.sdk.corekit.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.midtrans.sdk.corekit.core.Logger;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 * Created by ziahaqi on 7/14/16.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class, Logger.class})
public class UtilsTest {

    @Mock
    Context contextMock;
    @Mock
    private ConnectivityManager conManagerMock;
    @Mock
    private android.net.NetworkInfo networkInfoMock;
    @Mock
    private View viewMock;
    @Mock
    private InputMethodManager inputMethodMock;
    @Mock
    private IBinder binderMock;
    private String ccNumberSample = "4811111111111114";
    private String ccFormatedCCNumberSample = "4811 1111 1111 1114 ";

    @Before
    public void setup() {
        Mockito.when(contextMock.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(conManagerMock);
        PowerMockito.mockStatic(Log.class);
        PowerMockito.mockStatic(Logger.class);
    }

    @Test
    public void isNetworkAvailableTest() {
        Mockito.when(conManagerMock.getActiveNetworkInfo()).thenReturn(networkInfoMock);
        Mockito.when(networkInfoMock.isAvailable()).thenReturn(true);
        Mockito.when(networkInfoMock.isConnected()).thenReturn(true);
        Assert.assertTrue(Utils.isNetworkAvailable(contextMock));
    }

    @Test
    public void isNetworkAvailableTest_excptionError() {
        Mockito.when(contextMock.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(null);
        Mockito.when(conManagerMock.getActiveNetworkInfo()).thenReturn(null);
        Utils.isNetworkAvailable(null);
        PowerMockito.verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString());
    }

    @Test
    public void isNetworkAvailableTest_excption() {
        Mockito.when(conManagerMock.getActiveNetworkInfo()).thenReturn(null);
        Mockito.when(networkInfoMock.isAvailable()).thenReturn(true);
        Mockito.when(networkInfoMock.isConnected()).thenReturn(true);

        Assert.assertFalse(Utils.isNetworkAvailable(contextMock));
    }

    @Test
    public void isNetworkAvailableTest_notconnected() {
        Mockito.when(conManagerMock.getActiveNetworkInfo()).thenReturn(null);
        Mockito.when(networkInfoMock.isAvailable()).thenReturn(false);
        Mockito.when(networkInfoMock.isConnected()).thenReturn(true);

        Assert.assertFalse(Utils.isNetworkAvailable(contextMock));
    }

    @Test
    public void isNetworkAvailableTest_notAvailable() {
        Mockito.when(conManagerMock.getActiveNetworkInfo()).thenReturn(null);
        Mockito.when(networkInfoMock.isAvailable()).thenReturn(true);
        Mockito.when(networkInfoMock.isConnected()).thenReturn(false);

        Assert.assertFalse(Utils.isNetworkAvailable(contextMock));
    }

    @Test
    public void hideKeyboard() {
        Mockito.when(contextMock.getSystemService(Context.INPUT_METHOD_SERVICE)).thenReturn(inputMethodMock);
        Mockito.when(viewMock.getWindowToken()).thenReturn(binderMock);
        Mockito.when(inputMethodMock.hideSoftInputFromWindow(binderMock, 0)).thenReturn(true);
        Utils.hideKeyboard(contextMock, viewMock);

        Mockito.verify(inputMethodMock).hideSoftInputFromWindow(binderMock, 0);
    }

    @Test
    public void hideKeyboard_exception() {
        Mockito.when(contextMock.getSystemService(Context.INPUT_METHOD_SERVICE)).thenReturn(null);
        Mockito.when(viewMock.getWindowToken()).thenReturn(null);
        Mockito.when(inputMethodMock.hideSoftInputFromWindow(binderMock, 0)).thenReturn(true);
        Utils.hideKeyboard(contextMock, viewMock);
        PowerMockito.verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString());

    }

    @Test
    public void getMonthTest() {
        Assert.assertEquals("January", Utils.getMonth(1));
        Assert.assertEquals("February", Utils.getMonth(2));
        Assert.assertEquals("March", Utils.getMonth(3));
        Assert.assertEquals("April", Utils.getMonth(4));
        Assert.assertEquals("May", Utils.getMonth(5));
        Assert.assertEquals("June", Utils.getMonth(6));
        Assert.assertEquals("July", Utils.getMonth(7));
        Assert.assertEquals("August", Utils.getMonth(8));
        Assert.assertEquals("September", Utils.getMonth(9));
        Assert.assertEquals("October", Utils.getMonth(10));
        Assert.assertEquals("November", Utils.getMonth(11));
        Assert.assertEquals("December", Utils.getMonth(12));
    }

    @Test
    public void getMonthTest_invalid() {
        Assert.assertEquals("Invalid Month", Utils.getMonth(123));
    }

    @Test
    public void formatCCTest() {
        String formatedCC = Utils.getFormattedCreditCardNumber(ccNumberSample);
        Assert.assertEquals(ccFormatedCCNumberSample, formatedCC);
    }

    @Test
    public void getCardTypeTest_whenCardShort() {
        Assert.assertEquals("", Utils.getCardType("1"));
    }

    @Test
    public void getCardTypeTest_whenCardNull() {
        Assert.assertEquals("", Utils.getCardType(""));
    }

    @Test
    public void getCardTypeTest_whenVisa() {
        Assert.assertEquals(Utils.CARD_TYPE_VISA, Utils.getCardType("4811"));
    }

    @Test
    public void getCardTypeTest_whenMasterCard() {
        Assert.assertEquals(Utils.CARD_TYPE_MASTERCARD, Utils.getCardType("5111"));
        Assert.assertEquals(Utils.CARD_TYPE_MASTERCARD, Utils.getCardType("5211"));
        Assert.assertEquals(Utils.CARD_TYPE_MASTERCARD, Utils.getCardType("5311"));
        Assert.assertEquals(Utils.CARD_TYPE_MASTERCARD, Utils.getCardType("5411"));
        Assert.assertEquals(Utils.CARD_TYPE_MASTERCARD, Utils.getCardType("5511"));
    }

    @Test
    public void getCardType_whenAmex() {
        Assert.assertEquals(Utils.CARD_TYPE_AMEX, Utils.getCardType("3488"));
        Assert.assertEquals(Utils.CARD_TYPE_AMEX, Utils.getCardType("3788"));
    }

    @Test
    public void getCardType_whenInvalid() {
        Assert.assertEquals("", Utils.getCardType("9999"));
    }


    @Test
    public void getValidityTime_whenNull() {
        Assert.assertEquals(null, Utils.getValidityTime(null));
    }

    @Test
    public void getValidityTime_whenNotNull() {
        Assert.assertNotNull(Utils.getValidityTime("2015-10-30 20:32:51"));
    }

    @Test
    public void getValidtyTime_whenParseExcetion() {
        String sampleTime = "string error 23423 234";
        Assert.assertEquals(sampleTime, Utils.getValidityTime(sampleTime));
        PowerMockito.verifyStatic(Mockito.times(1));
        Logger.e(Matchers.anyString());
    }

    @Test
    public void getFormattedAmount() {
        Assert.assertEquals("1,000", Utils.getFormattedAmount(1000.0));
    }
}
