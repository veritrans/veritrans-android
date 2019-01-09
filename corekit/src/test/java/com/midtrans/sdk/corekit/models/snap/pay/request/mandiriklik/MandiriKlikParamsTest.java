package com.midtrans.sdk.corekit.models.snap.pay.request.mandiriklik;

import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.mandiriclick.MandiriClickpayParams;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(PowerMockRunner.class)
public class MandiriKlikParamsTest {
    private MandiriClickpayParams response;
    private String exampleTextPositive;
    private String exampleTextNegative;

    @Before
    public void test_setup() {
        this.exampleTextPositive = "exampleTextPositive";
        this.exampleTextNegative = "exampleTextNegative";
        this.response = new MandiriClickpayParams(exampleTextPositive, exampleTextPositive, exampleTextPositive);
    }

    @Test
    public void test_setInput3_positive() {
        assertEquals(response.getInput3(), exampleTextPositive);
    }

    @Test
    public void test_setInput3_negative() {
        assertNotEquals(response.getInput3(), exampleTextNegative);
    }

    @Test
    public void test_setMandiriCardNumber_positive() {
        assertEquals(response.getMandiriCardNumber(), exampleTextPositive);
    }

    @Test
    public void test_setMandiriCardNumber_negative() {
        assertNotEquals(response.getMandiriCardNumber(), exampleTextNegative);
    }

    @Test
    public void test_setTokenResponse_positive() {
        assertEquals(response.getTokenResponse(), exampleTextPositive);
    }

    @Test
    public void test_setTokenResponse_negative() {
        assertNotEquals(response.getTokenResponse(), exampleTextNegative);
    }

}