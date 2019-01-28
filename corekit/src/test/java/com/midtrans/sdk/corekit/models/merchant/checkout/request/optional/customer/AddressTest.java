package com.midtrans.sdk.corekit.models.merchant.checkout.request.optional.customer;

import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.optional.customer.Address;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class AddressTest {
    private Address shippingAddress;
    private String exampleTextPositive, exampleTextNegative;

    @Before
    public void test_setup() {
        this.exampleTextPositive = "exampleTextPositive";
        this.exampleTextNegative = "exampleTextNegative";
        this.shippingAddress = Address
                .builder()
                .setFirstName(exampleTextPositive)
                .setLastName(exampleTextPositive)
                .setAddress(exampleTextPositive)
                .setCity(exampleTextPositive)
                .setPostalCode(exampleTextPositive)
                .setPhone(exampleTextPositive)
                .setCountryCode(exampleTextPositive)
                .build();
    }

    @Test
    public void test_SetPhone_positive() {
        assertEquals(shippingAddress.getPhone(), exampleTextPositive);
    }

    @Test
    public void test_SetPhone_negative() {
        assertNotEquals(shippingAddress.getPhone(), exampleTextNegative);
    }

    @Test
    public void test_SetAddress_positive() {
        assertEquals(shippingAddress.getAddress(), exampleTextPositive);
    }

    @Test
    public void test_SetAddress_negative() {
        assertNotEquals(shippingAddress.getAddress(), exampleTextNegative);
    }

    @Test
    public void test_SetFirstName_positive() {
        assertEquals(shippingAddress.getFirstName(), exampleTextPositive);
    }

    @Test
    public void test_SetFirstName_negative() {
        assertNotEquals(shippingAddress.getFirstName(), exampleTextNegative);
    }

    @Test
    public void test_SetLastName_positive() {
        assertEquals(shippingAddress.getLastName(), exampleTextPositive);
    }

    @Test
    public void test_SetLastName_negative() {
        assertNotEquals(shippingAddress.getLastName(), exampleTextNegative);
    }

    @Test
    public void test_SetCountryCode_positive() {
        assertEquals(shippingAddress.getCountryCode(), exampleTextPositive);
    }

    @Test
    public void test_SetCountryCode_negative() {
        assertNotEquals(shippingAddress.getCountryCode(), exampleTextNegative);
    }

    @Test
    public void test_SetPostalCode_positive() {
        assertEquals(shippingAddress.getPostalCode(), exampleTextPositive);
    }

    @Test
    public void test_SetPostalCode_negative() {
        assertNotEquals(shippingAddress.getPostalCode(), exampleTextNegative);
    }

    @Test
    public void test_SetCity_positive() {
        assertEquals(shippingAddress.getCity(), exampleTextPositive);
    }

    @Test
    public void test_SetCity_negative() {
        assertNotEquals(shippingAddress.getCity(), exampleTextNegative);
    }
}