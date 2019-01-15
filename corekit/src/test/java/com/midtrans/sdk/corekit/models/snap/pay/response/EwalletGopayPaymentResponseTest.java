package com.midtrans.sdk.corekit.models.snap.pay.response;

import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.EwalletGopayPaymentResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(PowerMockRunner.class)
public class EwalletGopayPaymentResponseTest {
    private EwalletGopayPaymentResponse response;
    private String exampleTextPositive;
    private String exampleTextNegative;

    @Before
    public void test_setup() {
        this.exampleTextPositive = "exampleTextPositive";
        this.exampleTextNegative = "exampleTextNegative";
        this.response = new EwalletGopayPaymentResponse();
    }

    @Test
    public void test_setStatusMessage_positive() {
        this.response.setStatusMessage(exampleTextPositive);
        assertEquals(response.getStatusMessage(), exampleTextPositive);
    }

    @Test
    public void test_setStatusMessage_negative() {
        this.response.setStatusMessage(exampleTextPositive);
        assertNotEquals(response.getStatusMessage(), exampleTextNegative);
    }

    @Test
    public void test_setStatusCode_positive() {
        this.response.setStatusCode(exampleTextPositive);
        assertEquals(response.getStatusCode(), exampleTextPositive);
    }

    @Test
    public void test_setStatusCode_negative() {
        this.response.setStatusCode(exampleTextPositive);
        assertNotEquals(response.getStatusCode(), exampleTextNegative);
    }

    @Test
    public void test_setTransactionId_positive() {
        this.response.setTransactionId(exampleTextPositive);
        assertEquals(response.getTransactionId(), exampleTextPositive);
    }

    @Test
    public void test_setTransactionId_negative() {
        this.response.setTransactionId(exampleTextPositive);
        assertNotEquals(response.getTransactionId(), exampleTextNegative);
    }

    @Test
    public void test_setTransactionStatus_positive() {
        this.response.setTransactionStatus(exampleTextPositive);
        assertEquals(response.getTransactionStatus(), exampleTextPositive);
    }

    @Test
    public void test_setTransactionStatus_negative() {
        this.response.setTransactionStatus(exampleTextPositive);
        assertNotEquals(response.getTransactionTime(), exampleTextNegative);
    }

    @Test
    public void test_setTransactionTime_positive() {
        this.response.setTransactionTime(exampleTextPositive);
        assertEquals(response.getTransactionTime(), exampleTextPositive);
    }

    @Test
    public void test_setTransactionTime_negative() {
        this.response.setTransactionTime(exampleTextPositive);
        assertNotEquals(response.getTransactionTime(), exampleTextNegative);
    }

    @Test
    public void test_setFinishRedirectUrl_positive() {
        this.response.setFinishRedirectUrl(exampleTextPositive);
        assertEquals(response.getFinishRedirectUrl(), exampleTextPositive);
    }

    @Test
    public void test_setFInishRedirectUrl_negative() {
        this.response.setFinishRedirectUrl(exampleTextPositive);
        assertNotEquals(response.getFinishRedirectUrl(), exampleTextNegative);
    }

}