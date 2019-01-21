package com.midtrans.sdk.corekit.models.merchant.checkout.request.optional.customer;

import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.optional.customer.Address;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.optional.customer.ShippingAddress;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class AddressTest {
    private Address shippingAddress;
    private String exampleTextPositive, exampleTextNegative;

    @Before
    public void test_setup() {
        this.shippingAddress = new ShippingAddress();
        this.exampleTextPositive = "exampleTextPositive";
        this.exampleTextNegative = "exampleTextNegative";
    }

    @Test
    public void test_SetPhone_positive() {
        shippingAddress.setPhone(exampleTextPositive);
        assertEquals(shippingAddress.getPhone(), exampleTextPositive);
    }

    @Test
    public void test_SetPhone_negative() {
        shippingAddress.setPhone(exampleTextPositive);
        assertNotEquals(shippingAddress.getPhone(), exampleTextNegative);
    }

    @Test
    public void test_SetAddress_positive() {
        shippingAddress.setAddress(exampleTextPositive);
        assertEquals(shippingAddress.getAddress(), exampleTextPositive);
    }

    @Test
    public void test_SetAddress_negative() {
        shippingAddress.setAddress(exampleTextPositive);
        assertNotEquals(shippingAddress.getAddress(), exampleTextNegative);
    }

    @Test
    public void test_SetFirstName_positive() {
        shippingAddress.setFirstName(exampleTextPositive);
        assertEquals(shippingAddress.getFirstName(), exampleTextPositive);
    }

    @Test
    public void test_SetFirstName_negative() {
        shippingAddress.setFirstName(exampleTextPositive);
        assertNotEquals(shippingAddress.getFirstName(), exampleTextNegative);
    }

    @Test
    public void test_SetLastName_positive() {
        shippingAddress.setLastName(exampleTextPositive);
        assertEquals(shippingAddress.getLastName(), exampleTextPositive);
    }

    @Test
    public void test_SetLastName_negative() {
        shippingAddress.setLastName(exampleTextPositive);
        assertNotEquals(shippingAddress.getLastName(), exampleTextNegative);
    }

    @Test
    public void test_SetCountryCode_positive() {
        shippingAddress.setCountryCode(exampleTextPositive);
        assertEquals(shippingAddress.getCountryCode(), exampleTextPositive);
    }

    @Test
    public void test_SetCountryCode_negative() {
        shippingAddress.setCountryCode(exampleTextPositive);
        assertNotEquals(shippingAddress.getCountryCode(), exampleTextNegative);
    }

    @Test
    public void test_SetPostalCode_positive() {
        shippingAddress.setPostalCode(exampleTextPositive);
        assertEquals(shippingAddress.getPostalCode(), exampleTextPositive);
    }

    @Test
    public void test_SetPostalCode_negative() {
        shippingAddress.setPostalCode(exampleTextPositive);
        assertNotEquals(shippingAddress.getPostalCode(), exampleTextNegative);
    }

    @Test
    public void test_SetCity_positive() {
        shippingAddress.setCity(exampleTextPositive);
        assertEquals(shippingAddress.getCity(), exampleTextPositive);
    }

    @Test
    public void test_SetCity_negative() {
        shippingAddress.setCity(exampleTextPositive);
        assertNotEquals(shippingAddress.getCity(), exampleTextNegative);
    }
}