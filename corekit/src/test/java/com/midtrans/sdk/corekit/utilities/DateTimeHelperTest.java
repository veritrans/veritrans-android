package com.midtrans.sdk.corekit.utilities;

import android.util.Log;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Log.class, Logger.class})
public class DateTimeHelperTest {

    @Before
    public void setup() {
        PowerMockito.mockStatic(Log.class);
        PowerMockito.mockStatic(Logger.class);
    }

    public void test_getFormattedDate_positive() {
        Assert.assertEquals("1970-01-01 07:00:10 +0700", DateTimeHelper.getFormattedTime(10000));
    }

    @Test
    public void test_getMonth_positive() {
        Assert.assertEquals("January", DateTimeHelper.getMonth(1));
        Assert.assertEquals("February", DateTimeHelper.getMonth(2));
        Assert.assertEquals("March", DateTimeHelper.getMonth(3));
        Assert.assertEquals("April", DateTimeHelper.getMonth(4));
        Assert.assertEquals("May", DateTimeHelper.getMonth(5));
        Assert.assertEquals("June", DateTimeHelper.getMonth(6));
        Assert.assertEquals("July", DateTimeHelper.getMonth(7));
        Assert.assertEquals("August", DateTimeHelper.getMonth(8));
        Assert.assertEquals("September", DateTimeHelper.getMonth(9));
        Assert.assertEquals("October", DateTimeHelper.getMonth(10));
        Assert.assertEquals("November", DateTimeHelper.getMonth(11));
        Assert.assertEquals("December", DateTimeHelper.getMonth(12));
    }

    @Test
    public void test_getMonth_negative_invalid() {
        Assert.assertEquals("Invalid Month", DateTimeHelper.getMonth(123));
    }

}