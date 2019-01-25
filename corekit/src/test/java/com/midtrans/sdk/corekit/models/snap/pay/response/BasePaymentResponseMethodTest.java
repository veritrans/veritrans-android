package com.midtrans.sdk.corekit.models.snap.pay.response;

import com.midtrans.sdk.corekit.base.enums.Currency;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.BasePaymentResponseMethod;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(PowerMockRunner.class)
public class BasePaymentResponseMethodTest {
    private BasePaymentResponseMethod response;
    private String exampleTextPositive;
    private String exampleTextNegative;

    @Before
    public void test_setup() {
        this.exampleTextPositive = "exampleTextPositive";
        this.exampleTextNegative = "exampleTextNegative";
        this.response = new BasePaymentResponseMethod();
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
    public void test_setCurrency_positive() {
        this.response.setCurrency(Currency.IDR);
        assertEquals(Currency.IDR, response.getCurrency());
    }

    @Test
    public void test_setCurrency_negative() {
        this.response.setCurrency(Currency.IDR);
        assertNotEquals("SGD", response.getCurrency());
    }

}