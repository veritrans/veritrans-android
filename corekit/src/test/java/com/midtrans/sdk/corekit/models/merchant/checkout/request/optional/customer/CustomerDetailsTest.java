package com.midtrans.sdk.corekit.models.merchant.checkout.request.optional.customer;

import com.midtrans.sdk.corekit.core.merchant.model.checkout.request.optional.customer.CustomerDetails;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class CustomerDetailsTest {
    private CustomerDetails customerDetails;
    private String exampleTextPositive,exampleTextNegative;

    @Before
    public void test_setup() {
        this.customerDetails = new CustomerDetails();
        this.exampleTextPositive = "exampleTextPositive";
        this.exampleTextNegative = "exampleTextNegative";
    }

    @Test
    public void test_SetPhone_positive() {
        customerDetails.setPhone(exampleTextPositive);
        assertEquals(customerDetails.getPhone(), exampleTextPositive);
    }

    @Test
    public void test_SetPhone_negative() {
        customerDetails.setPhone(exampleTextPositive);
        assertNotEquals(customerDetails.getPhone(), exampleTextNegative);
    }

    @Test
    public void test_SetEmail_positive() {
        customerDetails.setEmail(exampleTextPositive);
        assertEquals(customerDetails.getEmail(), exampleTextPositive);
    }

    @Test
    public void test_SetEmail_negative() {
        customerDetails.setEmail(exampleTextPositive);
        assertNotEquals(customerDetails.getEmail(), exampleTextNegative);
    }

    @Test
    public void test_SetFirstName_positive() {
        customerDetails.setFirstName(exampleTextPositive);
        assertEquals(customerDetails.getFirstName(), exampleTextPositive);
    }

    @Test
    public void test_SetFirstName_negative() {
        customerDetails.setFirstName(exampleTextPositive);
        assertNotEquals(customerDetails.getFirstName(), exampleTextNegative);
    }

    @Test
    public void test_SetLastName_positive() {
        customerDetails.setLastName(exampleTextPositive);
        assertEquals(customerDetails.getLastName(), exampleTextPositive);
    }

    @Test
    public void test_SetLastName_negative() {
        customerDetails.setLastName(exampleTextPositive);
        assertNotEquals(customerDetails.getLastName(), exampleTextNegative);
    }

}