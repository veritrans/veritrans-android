package com.midtrans.sdk.corekit.models.snap.pay.request.gopay;

import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.gopay.GopayPaymentRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(PowerMockRunner.class)
public class GopayPaymentRequestTest {

    private GopayPaymentRequest response;
    private String exampleTextPositive;
    private String exampleTextNegative;

    @Before
    public void test_setup() {
        this.exampleTextPositive = "exampleTextPositive";
        this.exampleTextNegative = "exampleTextNegative";
        this.response = new GopayPaymentRequest(exampleTextPositive, exampleTextPositive);
    }

    @Test
    public void test_setAccountNumber_positive() {
        assertEquals(response.getGopayAccountNumber(), exampleTextPositive);
    }

    @Test
    public void test_setAccountNumber_negative() {
        assertNotEquals(response.getGopayAccountNumber(), exampleTextNegative);
    }

}