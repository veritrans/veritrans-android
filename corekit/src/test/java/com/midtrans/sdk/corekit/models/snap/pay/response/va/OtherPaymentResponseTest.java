package com.midtrans.sdk.corekit.models.snap.pay.response.va;

import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.BankTransferVaOtherPaymentResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(PowerMockRunner.class)
public class OtherPaymentResponseTest {
    private BankTransferVaOtherPaymentResponse response;
    private String exampleTextPositive;
    private String exampleTextNegative;

    @Before
    public void test_setup() {
        this.exampleTextPositive = "exampleTextPositive";
        this.exampleTextNegative = "exampleTextNegative";
        this.response = new BankTransferVaOtherPaymentResponse();
    }

    @Test
    public void test_setOtherExpiration_positive() {
        this.response.setBniExpiration(exampleTextPositive);
        assertEquals(response.getBniExpiration(), exampleTextPositive);
    }

    @Test
    public void test_setOtherExpiration_negative() {
        this.response.setBniExpiration(exampleTextPositive);
        assertNotEquals(response.getBniExpiration(), exampleTextNegative);
    }

    @Test
    public void test_setVaNumber_positive() {
        this.response.setBniVaNumber(exampleTextPositive);
        assertEquals(response.getBniVaNumber(), exampleTextPositive);
    }

    @Test
    public void test_setVaNumber_negative() {
        this.response.setBniVaNumber(exampleTextPositive);
        assertNotEquals(response.getBniVaNumber(), exampleTextNegative);
    }

}