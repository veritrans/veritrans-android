package com.midtrans.sdk.corekit.utilities;

import android.util.Log;

import com.midtrans.sdk.corekit.base.model.Currency;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.HashMap;

import static com.midtrans.sdk.corekit.utilities.ValidationHelper.isNotEmpty;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class, Logger.class, ValidationHelper.class})
public class ValidationHelperTest {

    @Before
    public void setup() {
        PowerMockito.mockStatic(Log.class);
        PowerMockito.mockStatic(Logger.class);
    }


    @Test
    public void test_isNotEmpty_positive() {
        Assert.assertTrue(isNotEmpty(new HashMap<>()));
        Assert.assertTrue(isNotEmpty(new Object()));
        Assert.assertTrue(isNotEmpty(new String()));
        Assert.assertTrue(isNotEmpty(new ArrayList<>()));
    }


}