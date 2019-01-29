package com.midtrans.sdk.corekit.models.snap.pay.response.bcaklikpay;

import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.bcaklikpay.BcaKlikPayData;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(PowerMockRunner.class)
public class BcaKlikPayDataTest {
    private BcaKlikPayData response;
    private String exampleTextPositive;
    private String exampleTextNegative;

    @Before
    public void test_setup() {
        this.exampleTextPositive = "exampleTextPositive";
        this.exampleTextNegative = "exampleTextNegative";
        this.response = new BcaKlikPayData();
    }

    @Test
    public void test_setMethod_positive() {
        this.response.setMethod(exampleTextPositive);
        assertEquals(response.getMethod(), exampleTextPositive);
    }

    @Test
    public void test_setMethod_negative() {
        this.response.setMethod(exampleTextPositive);
        assertNotEquals(response.getMethod(), exampleTextNegative);
    }

    @Test
    public void test_setUrl_positive() {
        this.response.setUrl(exampleTextPositive);
        assertEquals(response.getUrl(), exampleTextPositive);
    }

    @Test
    public void test_setUrl_negative() {
        this.response.setUrl(exampleTextPositive);
        assertNotEquals(response.getUrl(), exampleTextNegative);
    }

}