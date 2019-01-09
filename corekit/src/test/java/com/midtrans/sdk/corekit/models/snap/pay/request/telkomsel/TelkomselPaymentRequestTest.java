package com.midtrans.sdk.corekit.models.snap.pay.request.telkomsel;

import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.telkomsel.TelkomselCashPaymentRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(PowerMockRunner.class)
public class TelkomselPaymentRequestTest {
    private TelkomselCashPaymentRequest response;
    private String exampleTextPositive;
    private String exampleTextNegative;

    @Before
    public void test_setup() {
        this.exampleTextPositive = "exampleTextPositive";
        this.exampleTextNegative = "exampleTextNegative";
        this.response = new TelkomselCashPaymentRequest(exampleTextPositive, exampleTextPositive);
    }

    @Test
    public void test_setCustomerNumber_positive() {
        assertEquals(response.getCustomerNumber(), exampleTextPositive);
    }

    @Test
    public void test_setCustomberNumber_negative() {
        assertNotEquals(response.getCustomerNumber(), exampleTextNegative);
    }

}