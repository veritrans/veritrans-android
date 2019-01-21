package com.midtrans.sdk.corekit.utilities;

import android.content.Context;
import android.util.Log;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class, Logger.class})
public class InstallationHelperTest {

    @Mock
    private Context contextMock;

    @Before
    public void setup() {
        PowerMockito.mockStatic(Log.class);
        PowerMockito.mockStatic(Logger.class);
    }

    @Test
    public void test_generatedRandomID_negative() {
        Assert.assertNull(InstallationHelper.generatedRandomID(null));
    }


    @Test
    public void test_generatedRandomID_positive() {
        Assert.assertNotNull(InstallationHelper.generatedRandomID(contextMock));
    }

}