package com.midtrans.sdk.corekit.models.snap.pay.response;

import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.PaymentResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(PowerMockRunner.class)
public class PaymentResponseTest {
    private PaymentResponse response;
    private String exampleTextPositive;
    private String exampleTextNegative;

    @Before
    public void test_setup() {
        this.exampleTextPositive = "exampleTextPositive";
        this.exampleTextNegative = "exampleTextNegative";
        this.response = new PaymentResponse();
    }

    @Test
    public void test_setApprovalCode_positive() {
        this.response.setApprovalCode(exampleTextPositive);
        assertEquals(response.getApprovalCode(), exampleTextPositive);
    }

    @Test
    public void test_setApprovalCode_negative() {
        this.response.setApprovalCode(exampleTextPositive);
        assertNotEquals(response.getApprovalCode(), exampleTextNegative);
    }

    @Test
    public void test_setBank_positive() {
        this.response.setBank(exampleTextPositive);
        assertEquals(response.getBank(), exampleTextPositive);
    }

    @Test
    public void test_setBank_negative() {
        this.response.setBank(exampleTextPositive);
        assertNotEquals(response.getBank(), exampleTextNegative);
    }

    @Test
    public void test_setBcaExpiration_positive() {
        this.response.setBcaExpiration(exampleTextPositive);
        assertEquals(response.getBcaExpiration(), exampleTextPositive);
    }

    @Test
    public void test_setBcaExpiration_negative() {
        this.response.setBcaExpiration(exampleTextPositive);
        assertNotEquals(response.getBcaExpiration(), exampleTextNegative);
    }

    @Test
    public void test_setBcaVaNumber_positive() {
        this.response.setBcaVaNumber(exampleTextPositive);
        assertEquals(response.getBcaVaNumber(), exampleTextPositive);
    }

    @Test
    public void test_setBcaVaNumber_negative() {
        this.response.setBcaVaNumber(exampleTextPositive);
        assertNotEquals(response.getBcaVaNumber(), exampleTextNegative);
    }

    @Test
    public void test_setBniVaNumber_positive() {
        this.response.setBniVaNumber(exampleTextPositive);
        assertEquals(response.getBniVaNumber(), exampleTextPositive);
    }

    @Test
    public void test_setBniVaNumber_negative() {
        this.response.setBniVaNumber(exampleTextPositive);
        assertNotEquals(response.getBniVaNumber(), exampleTextNegative);
    }

    @Test
    public void test_setBniExpiration_positive() {
        this.response.setBniExpiration(exampleTextPositive);
        assertEquals(response.getBniExpiration(), exampleTextPositive);
    }

    @Test
    public void test_setBniExpiration_negative() {
        this.response.setBniExpiration(exampleTextPositive);
        assertNotEquals(response.getBniExpiration(), exampleTextNegative);
    }

    @Test
    public void test_setCompanyCode_positive() {
        this.response.setCompanyCode(exampleTextPositive);
        assertEquals(response.getCompanyCode(), exampleTextPositive);
    }

    @Test
    public void test_setCompanyCode_negative() {
        this.response.setCompanyCode(exampleTextPositive);
        assertNotEquals(response.getCompanyCode(), exampleTextNegative);
    }

    @Test
    public void test_setCurrency_positive() {
        this.response.setCurrency(exampleTextPositive);
        assertEquals(response.getCurrency(), exampleTextPositive);
    }

    @Test
    public void test_setCurrency_negative() {
        this.response.setCurrency(exampleTextPositive);
        assertNotEquals(response.getCurrency(), exampleTextNegative);
    }

    @Test
    public void test_setDeeplink_positive() {
        this.response.setDeeplinkUrl(exampleTextPositive);
        assertEquals(response.getDeeplinkUrl(), exampleTextPositive);
    }

    @Test
    public void test_setDeeplink_negative() {
        this.response.setDeeplinkUrl(exampleTextPositive);
        assertNotEquals(response.getDeeplinkUrl(), exampleTextNegative);
    }

    @Test
    public void test_setEci_positive() {
        this.response.setEci(exampleTextPositive);
        assertEquals(response.getEci(), exampleTextPositive);
    }

    @Test
    public void test_setEci_negative() {
        this.response.setEci(exampleTextPositive);
        assertNotEquals(response.getEci(), exampleTextNegative);
    }

}