package com.midtrans.sdk.corekit.models.snap.transaction.enablepayment;

import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.enablepayment.EnabledPayment;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class EnabledPaymentTest {
    private EnabledPayment enabledPayment;
    private String exampleTextPositive, exampleTextNegative;

    @Before
    public void setUp() throws Exception {
        this.exampleTextPositive = "examplePositive";
        this.exampleTextNegative = "exampleNegative";
        this.enabledPayment = new EnabledPayment(exampleTextPositive, exampleTextPositive);
    }

    @Test
    public void test_setStatus_positive() throws Exception {
        enabledPayment.setStatus(exampleTextPositive);
        assertEquals(enabledPayment.getStatus(), exampleTextPositive);
    }

    @Test
    public void test_setStatus_negative() throws Exception {
        enabledPayment.setStatus(exampleTextPositive);
        assertNotEquals(enabledPayment.getStatus(), exampleTextNegative);
    }

    @Test
    public void test_setCategory_positive() throws Exception {
        enabledPayment.setCategory(exampleTextPositive);
        assertEquals(enabledPayment.getCategory(), exampleTextPositive);
    }

    @Test
    public void test_setCategory_negative() throws Exception {
        enabledPayment.setCategory(exampleTextPositive);
        assertNotEquals(enabledPayment.getCategory(), exampleTextNegative);
    }

    @Test
    public void test_setType_positive() throws Exception {
        enabledPayment.setType(exampleTextPositive);
        assertEquals(enabledPayment.getType(), exampleTextPositive);
    }

    @Test
    public void test_setType_negative() throws Exception {
        enabledPayment.setType(exampleTextPositive);
        assertNotEquals(enabledPayment.getType(), exampleTextNegative);
    }
}