package com.midtrans.sdk.corekit.models.merchant.checkout.request.optional.customer;

import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.optional.customer.CustomerDetails;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class CustomerDetailsTest {
    private CustomerDetails customerDetails;
    private String exampleTextPositive, exampleTextNegative;

    @Before
    public void test_setup() {
        this.exampleTextPositive = "exampleTextPositive";
        this.exampleTextNegative = "exampleTextNegative";
        this.customerDetails = new CustomerDetails(exampleTextPositive,
                exampleTextPositive,
                exampleTextPositive,
                exampleTextPositive);
    }

    @Test
    public void test_SetPhone_positive() {
        assertEquals(customerDetails.getPhone(), exampleTextPositive);
    }

    @Test
    public void test_SetPhone_negative() {
        assertNotEquals(customerDetails.getPhone(), exampleTextNegative);
    }

    @Test
    public void test_SetEmail_positive() {
        assertEquals(customerDetails.getEmail(), exampleTextPositive);
    }

    @Test
    public void test_SetEmail_negative() {
        assertNotEquals(customerDetails.getEmail(), exampleTextNegative);
    }

    @Test
    public void test_SetFirstName_positive() {
        assertEquals(customerDetails.getFirstName(), exampleTextPositive);
    }

    @Test
    public void test_SetFirstName_negative() {
        assertNotEquals(customerDetails.getFirstName(), exampleTextNegative);
    }

    @Test
    public void test_SetLastName_positive() {
        assertEquals(customerDetails.getLastName(), exampleTextPositive);
    }

    @Test
    public void test_SetLastName_negative() {
        assertNotEquals(customerDetails.getLastName(), exampleTextNegative);
    }

}