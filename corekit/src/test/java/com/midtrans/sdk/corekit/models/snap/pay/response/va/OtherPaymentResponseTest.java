package com.midtrans.sdk.corekit.models.snap.pay.response.va;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(PowerMockRunner.class)
public class OtherPaymentResponseTest {
    private OtherPaymentResponse response;
    private String exampleTextPositive;
    private String exampleTextNegative;

    @Before
    public void test_setup() {
        this.exampleTextPositive = "exampleTextPositive";
        this.exampleTextNegative = "exampleTextNegative";
        this.response = new OtherPaymentResponse();
    }

    @Test
    public void test_setOtherExpiration_positive() {
        this.response.setOtherExpiration(exampleTextPositive);
        assertEquals(response.getOtherExpiration(), exampleTextPositive);
    }

    @Test
    public void test_setOtherExpiration_negative() {
        this.response.setOtherExpiration(exampleTextPositive);
        assertNotEquals(response.getOtherExpiration(), exampleTextNegative);
    }

    @Test
    public void test_setVaNumber_positive() {
        this.response.setOtherVaNumber(exampleTextPositive);
        assertEquals(response.getOtherVaNumber(), exampleTextPositive);
    }

    @Test
    public void test_setVaNumber_negative() {
        this.response.setOtherVaNumber(exampleTextPositive);
        assertNotEquals(response.getOtherVaNumber(), exampleTextNegative);
    }

}