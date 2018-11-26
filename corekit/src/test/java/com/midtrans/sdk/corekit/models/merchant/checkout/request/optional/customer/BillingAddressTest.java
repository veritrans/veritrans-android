package com.midtrans.sdk.corekit.models.merchant.checkout.request.optional.customer;

import com.midtrans.sdk.corekit.core.merchant.model.checkout.request.optional.customer.BillingAddress;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class BillingAddressTest {
    private BillingAddress billingAddress;
    private String exampleTextPositive, exampleTextNegative;

    @Before
    public void test_setup() {
        this.billingAddress = new BillingAddress(exampleTextPositive,
                exampleTextPositive,
                exampleTextPositive,
                exampleTextPositive,
                exampleTextPositive,
                exampleTextPositive,
                exampleTextPositive);
        this.exampleTextPositive = "exampleTextPositive";
        this.exampleTextNegative = "exampleTextNegative";
    }

    @Test
    public void test_SetPhone_positive() {
        assertEquals(billingAddress.getPhone(), exampleTextPositive);
    }

    @Test
    public void test_SetPhone_negative() {
        assertNotEquals(billingAddress.getPhone(), exampleTextNegative);
    }

    @Test
    public void test_SetAddress_positive() {
        assertEquals(billingAddress.getAddress(), exampleTextPositive);
    }

    @Test
    public void test_SetAddress_negative() {
        assertNotEquals(billingAddress.getAddress(), exampleTextNegative);
    }

    @Test
    public void test_SetFirstName_positive() {
        assertEquals(billingAddress.getFirstName(), exampleTextPositive);
    }

    @Test
    public void test_SetFirstName_negative() {
        assertNotEquals(billingAddress.getFirstName(), exampleTextNegative);
    }

    @Test
    public void test_SetLastName_positive() {
        assertEquals(billingAddress.getLastName(), exampleTextPositive);
    }

    @Test
    public void test_SetLastName_negative() {
        assertNotEquals(billingAddress.getLastName(), exampleTextNegative);
    }

    @Test
    public void test_SetCountryCode_positive() {
        assertEquals(billingAddress.getCountryCode(), exampleTextPositive);
    }

    @Test
    public void test_SetCountryCode_negative() {
        assertNotEquals(billingAddress.getCountryCode(), exampleTextNegative);
    }

    @Test
    public void test_SetPostalCode_positive() {
        assertEquals(billingAddress.getPostalCode(), exampleTextPositive);
    }

    @Test
    public void test_SetPostalCode_negative() {
        assertNotEquals(billingAddress.getPostalCode(), exampleTextNegative);
    }

    @Test
    public void test_SetCity_positive() {
        assertEquals(billingAddress.getCity(), exampleTextPositive);
    }

    @Test
    public void test_SetCity_negative() {
        assertNotEquals(billingAddress.getCity(), exampleTextNegative);
    }
}