package com.midtrans.sdk.corekit.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

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

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class, Logger.class})
public class NetworkHelperTest {

    @Mock
    Context contextMock;
    @Mock
    private ConnectivityManager conManagerMock;
    @Mock
    private android.net.NetworkInfo networkInfoMock;

    @Before
    public void setup() {
        Mockito.when(contextMock.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(conManagerMock);
        PowerMockito.mockStatic(Log.class);
        PowerMockito.mockStatic(Logger.class);
    }

    @Test
    public void test_isNetworkAvailable_positive() {
        Mockito.when(conManagerMock.getActiveNetworkInfo()).thenReturn(networkInfoMock);
        Mockito.when(networkInfoMock.isAvailable()).thenReturn(true);
        Mockito.when(networkInfoMock.isConnected()).thenReturn(true);
        Assert.assertTrue(NetworkHelper.isNetworkAvailable(contextMock));
    }

    @Test
    public void test_isNetworkAvailable_negative_exceptionError() {
        Mockito.when(contextMock.getSystemService(Context.CONNECTIVITY_SERVICE)).thenReturn(null);
        Mockito.when(conManagerMock.getActiveNetworkInfo()).thenReturn(null);
        NetworkHelper.isNetworkAvailable(null);
        PowerMockito.verifyStatic(Mockito.times(1));
        Logger.error(Matchers.anyString());
    }

    @Test
    public void test_isNetworkAvailable_negative_exception() {
        Mockito.when(conManagerMock.getActiveNetworkInfo()).thenReturn(null);
        Mockito.when(networkInfoMock.isAvailable()).thenReturn(true);
        Mockito.when(networkInfoMock.isConnected()).thenReturn(true);

        Assert.assertFalse(NetworkHelper.isNetworkAvailable(contextMock));
    }

    @Test
    public void test_isNetworkAvailable_negative_notConnected() {
        Mockito.when(conManagerMock.getActiveNetworkInfo()).thenReturn(null);
        Mockito.when(networkInfoMock.isAvailable()).thenReturn(false);
        Mockito.when(networkInfoMock.isConnected()).thenReturn(true);

        Assert.assertFalse(NetworkHelper.isNetworkAvailable(contextMock));
    }

    @Test
    public void test_isNetworkAvailable_negative_notAvailable() {
        Mockito.when(conManagerMock.getActiveNetworkInfo()).thenReturn(null);
        Mockito.when(networkInfoMock.isAvailable()).thenReturn(true);
        Mockito.when(networkInfoMock.isConnected()).thenReturn(false);

        Assert.assertFalse(NetworkHelper.isNetworkAvailable(contextMock));
    }

}