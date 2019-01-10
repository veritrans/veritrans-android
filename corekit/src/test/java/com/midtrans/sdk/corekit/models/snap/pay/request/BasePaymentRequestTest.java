package com.midtrans.sdk.corekit.models.snap.pay.request;

import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.BasePaymentRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(PowerMockRunner.class)
public class BasePaymentRequestTest {
    private BasePaymentRequest response;
    private String exampleTextPositive;
    private String exampleTextNegative;

    @Before
    public void test_setup() {
        this.exampleTextPositive = "exampleTextPositive";
        this.exampleTextNegative = "exampleTextNegative";
        this.response = new BasePaymentRequest(exampleTextPositive);
    }

    @Test
    public void test_setPaymentType_positive() {
        assertEquals(response.getPaymentType(), exampleTextPositive);
    }

    @Test
    public void test_setPaymentType_negative() {
        assertNotEquals(response.getPaymentType(), exampleTextNegative);
    }

}