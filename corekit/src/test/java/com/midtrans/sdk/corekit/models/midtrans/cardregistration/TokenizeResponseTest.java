package com.midtrans.sdk.corekit.models.midtrans.cardregistration;

import com.midtrans.sdk.corekit.core.api.midtrans.model.registration.TokenizeResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(PowerMockRunner.class)
public class TokenizeResponseTest {
    private TokenizeResponse response;
    private String exampleTextPositive;
    private String exampleTextNegative;

    @Before
    public void test_setup() {
        this.response = new TokenizeResponse();
        this.exampleTextPositive = "exampleTextPositive";
        this.exampleTextNegative = "exampleTextNegative";
    }

    @Test
    public void test_setMaskedCard_positive() {
        response.setMaskedCard(exampleTextPositive);
        assertEquals(response.getMaskedCard(), exampleTextPositive);
    }

    @Test
    public void test_setMaskedCard_negative() {
        response.setMaskedCard(exampleTextPositive);
        assertNotEquals(response.getMaskedCard(), exampleTextNegative);
    }

    @Test
    public void test_setSavedTokenId_positive() {
        response.setSavedTokenId(exampleTextPositive);
        assertEquals(response.getSavedTokenId(), exampleTextPositive);
    }

    @Test
    public void test_setSavedTokenId_negative() {
        response.setSavedTokenId(exampleTextPositive);
        assertNotEquals(response.getSavedTokenId(), exampleTextNegative);
    }

    @Test
    public void test_setStatusMessage_positive() {
        response.setStatusMessage(exampleTextPositive);
        assertEquals(response.getStatusMessage(), exampleTextPositive);
    }

    @Test
    public void test_setStatusMessage_negative() {
        response.setStatusMessage(exampleTextPositive);
        assertNotEquals(response.getStatusMessage(), exampleTextNegative);
    }

    @Test
    public void test_setStatusCode_positive() {
        response.setStatusCode(exampleTextPositive);
        assertEquals(response.getStatusCode(), exampleTextPositive);
    }

    @Test
    public void test_setStatusCode_negative() {
        response.setStatusCode(exampleTextPositive);
        assertNotEquals(response.getStatusMessage(), exampleTextNegative);
    }

    @Test
    public void test_setTransactionId_positive() {
        response.setTransactionId(exampleTextPositive);
        assertEquals(response.getTransactionId(), exampleTextPositive);
    }

    @Test
    public void test_setTransactionId_negative() {
        response.setTransactionId(exampleTextPositive);
        assertNotEquals(response.getTransactionId(), exampleTextNegative);
    }
}