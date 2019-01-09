package com.midtrans.sdk.corekit.models.merchant.savecard;

import com.midtrans.sdk.corekit.core.api.merchant.model.savecard.SaveCardResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(PowerMockRunner.class)
public class SavedCardResponseTest {
    private SaveCardResponse response;
    private String exampleTextPositive;
    private String exampleTextNegative;
    private int exampleIntPositive;
    private int exampleIntNegative;

    @Before
    public void test_setup() {
        this.response = new SaveCardResponse();
        this.exampleTextPositive = "exampleTextPositive";
        this.exampleTextNegative = "exampleTextNegative";
        this.exampleIntNegative = 0;
        this.exampleIntPositive = 1;
    }

    @Test
    public void test_setCode_positive() {
        response.setCode(exampleIntPositive);
        assertEquals(response.getCode(), exampleIntPositive);
    }

    @Test
    public void test_setCode_negative() {
        response.setCode(exampleIntPositive);
        assertNotEquals(response.getCode(), exampleIntNegative);
    }

    @Test
    public void test_setStatus_positive() {
        response.setStatus(exampleTextPositive);
        assertEquals(response.getStatus(), exampleTextPositive);
    }

    @Test
    public void test_setStatus_negative() {
        response.setStatus(exampleTextPositive);
        assertNotEquals(response.getStatus(), exampleTextNegative);
    }

    @Test
    public void test_setMessage_positive() {
        response.setMessage(exampleTextPositive);
        assertEquals(response.getMessage(), exampleTextPositive);
    }

    @Test
    public void test_setMessage_negative() {
        response.setMessage(exampleTextPositive);
        assertNotEquals(response.getMessage(), exampleTextNegative);
    }
}
