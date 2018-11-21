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
        this.billingAddress = new BillingAddress();
        this.exampleTextPositive = "exampleTextPositive";
        this.exampleTextNegative = "exampleTextNegative";
    }

    @Test
    public void test_SetPhone_positive() {
        billingAddress.setPhone(exampleTextPositive);
        assertEquals(billingAddress.getPhone(), exampleTextPositive);
    }

    @Test
    public void test_SetPhone_negative() {
        billingAddress.setPhone(exampleTextPositive);
        assertNotEquals(billingAddress.getPhone(), exampleTextNegative);
    }

    @Test
    public void test_SetAddress_positive() {
        billingAddress.setAddress(exampleTextPositive);
        assertEquals(billingAddress.getAddress(), exampleTextPositive);
    }

    @Test
    public void test_SetAddress_negative() {
        billingAddress.setAddress(exampleTextPositive);
        assertNotEquals(billingAddress.getAddress(), exampleTextNegative);
    }

    @Test
    public void test_SetFirstName_positive() {
        billingAddress.setFirstName(exampleTextPositive);
        assertEquals(billingAddress.getFirstName(), exampleTextPositive);
    }

    @Test
    public void test_SetFirstName_negative() {
        billingAddress.setFirstName(exampleTextPositive);
        assertNotEquals(billingAddress.getFirstName(), exampleTextNegative);
    }

    @Test
    public void test_SetLastName_positive() {
        billingAddress.setLastName(exampleTextPositive);
        assertEquals(billingAddress.getLastName(), exampleTextPositive);
    }

    @Test
    public void test_SetLastName_negative() {
        billingAddress.setLastName(exampleTextPositive);
        assertNotEquals(billingAddress.getLastName(), exampleTextNegative);
    }

    @Test
    public void test_SetCountryCode_positive() {
        billingAddress.setCountryCode(exampleTextPositive);
        assertEquals(billingAddress.getCountryCode(), exampleTextPositive);
    }

    @Test
    public void test_SetCountryCode_negative() {
        billingAddress.setCountryCode(exampleTextPositive);
        assertNotEquals(billingAddress.getCountryCode(), exampleTextNegative);
    }

    @Test
    public void test_SetPostalCode_positive() {
        billingAddress.setPostalCode(exampleTextPositive);
        assertEquals(billingAddress.getPostalCode(), exampleTextPositive);
    }

    @Test
    public void test_SetPostalCode_negative() {
        billingAddress.setPostalCode(exampleTextPositive);
        assertNotEquals(billingAddress.getPostalCode(), exampleTextNegative);
    }

    @Test
    public void test_SetCity_positive() {
        billingAddress.setCity(exampleTextPositive);
        assertEquals(billingAddress.getCity(), exampleTextPositive);
    }

    @Test
    public void test_SetCity_negative() {
        billingAddress.setCity(exampleTextPositive);
        assertNotEquals(billingAddress.getCity(), exampleTextNegative);
    }
}