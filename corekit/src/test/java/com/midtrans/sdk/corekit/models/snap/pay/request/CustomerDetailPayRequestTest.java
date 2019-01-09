package com.midtrans.sdk.corekit.models.snap.pay.request;

import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.CustomerDetailPayRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(PowerMockRunner.class)
public class CustomerDetailPayRequestTest {
    private CustomerDetailPayRequest response;
    private String exampleTextPositive;
    private String exampleTextNegative;

    @Before
    public void test_setup() {
        this.exampleTextPositive = "exampleTextPositive";
        this.exampleTextNegative = "exampleTextNegative";
    }

    @Test
    public void test_setEmail_positive() {
        this.response.setEmail(exampleTextPositive);
        assertEquals(response.getEmail(), exampleTextPositive);
    }

    @Test
    public void test_setEmail_negative() {
        this.response.setEmail(exampleTextPositive);
        assertNotEquals(response.getEmail(), exampleTextNegative);
    }

    @Test
    public void test_setFullname_positive() {
        this.response.setFullName(exampleTextPositive);
        assertEquals(response.getFullName(), exampleTextPositive);
    }

    @Test
    public void test_setFullName_negative() {
        this.response.setFullName(exampleTextPositive);
        assertNotEquals(response.getFullName(), exampleTextNegative);
    }

    @Test
    public void test_setPhone_positive() {
        this.response.setPhone(exampleTextPositive);
        assertEquals(response.getPhone(), exampleTextPositive);
    }

    @Test
    public void test_setPhone_negative() {
        this.response.setPhone(exampleTextPositive);
        assertNotEquals(response.getPhone(), exampleTextNegative);
    }

}