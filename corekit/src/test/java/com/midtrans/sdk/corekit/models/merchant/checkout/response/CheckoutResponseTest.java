package com.midtrans.sdk.corekit.models.merchant.checkout.response;

import com.midtrans.sdk.corekit.core.merchant.model.checkout.response.CheckoutResponse;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class CheckoutResponseTest {
    private CheckoutResponse response;
    private String exampleTextPositive;
    private String exampleTextNegative;

    @Before
    public void test_setup() {
        this.response = new CheckoutResponse();
        this.exampleTextPositive = "exampleTextPositive";
        this.exampleTextNegative = "exampleTextNegative";
    }

    @Test
    public void test_SetSnapToken_positive() {
        response.setSnapToken(exampleTextPositive);
        assertEquals(response.getSnapToken(), exampleTextPositive);
    }

    @Test
    public void test_SetSnapToken_negative() {
        response.setSnapToken(exampleTextPositive);
        assertNotEquals(response.getSnapToken(), exampleTextNegative);
    }
}
