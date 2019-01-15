package com.midtrans.sdk.corekit.models.merchant.checkout.response;

import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.response.CheckoutWithTransactionResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(PowerMockRunner.class)
public class CheckoutWithTransactionResponseTest {
    private CheckoutWithTransactionResponse response;
    private String exampleTextPositive;
    private String exampleTextNegative;

    @Before
    public void test_setup() {
        this.response = new CheckoutWithTransactionResponse();
        this.exampleTextPositive = "exampleTextPositive";
        this.exampleTextNegative = "exampleTextNegative";
    }

    @Test
    public void test_SetSnapToken_positive() {
        response.setToken(exampleTextPositive);
        assertEquals(response.getToken(), exampleTextPositive);
    }

    @Test
    public void test_SetSnapToken_negative() {
        response.setToken(exampleTextPositive);
        assertNotEquals(response.getToken(), exampleTextNegative);
    }
}
