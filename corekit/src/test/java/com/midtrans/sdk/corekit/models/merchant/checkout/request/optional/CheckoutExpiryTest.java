package com.midtrans.sdk.corekit.models.merchant.checkout.request.optional;

import com.midtrans.sdk.corekit.base.enums.ExpiryTimeUnit;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.optional.CheckoutExpiry;

import org.junit.Before;
import org.junit.Test;

import static com.midtrans.sdk.corekit.base.enums.ExpiryTimeUnit.DAY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class CheckoutExpiryTest {

    private CheckoutExpiry checkoutExpiry;
    private String exampleTextPositive, exampleTextNegative;
    private int exampleIntPositive, exampleIntNegative;

    @Before
    public void test_setup() {
        this.checkoutExpiry = new CheckoutExpiry("", DAY, 1);
        this.exampleTextPositive = "exampleTextPositive";
        this.exampleTextNegative = "exampleTextNegative";
        this.exampleIntNegative = 0;
        this.exampleIntPositive = 1;
    }

    @Test
    public void test_SetDuration_positive() {
        checkoutExpiry.setDuration(exampleIntPositive);
        assertEquals(checkoutExpiry.getDuration(), exampleIntPositive);
    }

    @Test
    public void test_SetStartTime_positive() {
        checkoutExpiry.setStartTime(exampleTextPositive);
        assertEquals(checkoutExpiry.getStartTime(), exampleTextPositive);
    }

    @Test
    public void test_SetStartTime_negative() {
        checkoutExpiry.setStartTime(exampleTextPositive);
        assertNotEquals(checkoutExpiry.getStartTime(), exampleTextNegative);
    }

    @Test
    public void test_SetUnit_positive() {
        checkoutExpiry.setUnit(DAY);
        assertEquals(checkoutExpiry.getUnit(), DAY);
    }

    @Test
    public void test_SetUnit_negative() {
        checkoutExpiry.setUnit(ExpiryTimeUnit.HOUR);
        assertNotEquals(checkoutExpiry.getUnit(), exampleTextNegative);
    }

}