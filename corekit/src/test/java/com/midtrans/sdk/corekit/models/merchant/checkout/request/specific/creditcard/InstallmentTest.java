package com.midtrans.sdk.corekit.models.merchant.checkout.request.specific.creditcard;

import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.specific.creditcard.SavedToken;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class InstallmentTest {

    private SavedToken savedToken;
    private String exampleTextPositive, exampleTextNegative;

    @Before
    public void test_setup() {
        this.exampleTextPositive = "exampleTextPositive";
        this.exampleTextNegative = "exampleTextNegative";
        this.savedToken = new SavedToken();
    }

    @Test
    public void test_SetSavedToken_positive() {
        savedToken.setExpiresAt(exampleTextPositive);
        assertEquals(savedToken.getExpiresAt(), exampleTextPositive);
    }

    @Test
    public void test_SetSavedToken_negative() {
        savedToken.setExpiresAt(exampleTextPositive);
        assertNotEquals(savedToken.getExpiresAt(), exampleTextNegative);
    }

    @Test
    public void test_SetMaskedCard_positive() {
        savedToken.setMaskedCard(exampleTextPositive);
        assertEquals(savedToken.getMaskedCard(), exampleTextPositive);
    }

    @Test
    public void test_SetMaskedCard_negative() {
        savedToken.setMaskedCard(exampleTextPositive);
        assertNotEquals(savedToken.getMaskedCard(), exampleTextNegative);
    }

    @Test
    public void test_SetToken_positive() {
        savedToken.setToken(exampleTextPositive);
        assertEquals(savedToken.getToken(), exampleTextPositive);
    }

    @Test
    public void test_SeToken_negative() {
        savedToken.setToken(exampleTextPositive);
        assertNotEquals(savedToken.getToken(), exampleTextNegative);
    }

    @Test
    public void test_SetTokenType_positive() {
        savedToken.setTokenType(exampleTextPositive);
        assertEquals(savedToken.getTokenType(), exampleTextPositive);
    }

    @Test
    public void test_SetTokenType_negative() {
        savedToken.setTokenType(exampleTextPositive);
        assertNotEquals(savedToken.getTokenType(), exampleTextNegative);
    }

}