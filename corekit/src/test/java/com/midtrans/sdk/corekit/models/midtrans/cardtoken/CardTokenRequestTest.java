package com.midtrans.sdk.corekit.models.midtrans.cardtoken;

import com.midtrans.sdk.corekit.core.api.midtrans.model.cardtoken.CardTokenRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
public class CardTokenRequestTest {
    private CardTokenRequest response;
    private String exampleTextPositive;
    private String exampleTextNegative;

    @Before
    public void test_setup() {
        this.response = new CardTokenRequest();
        this.exampleTextPositive = "exampleTextPositive";
        this.exampleTextNegative = "exampleTextNegative";
    }

    @Test
    public void test_setBank_positive() {
        response.setBank(exampleTextPositive);
        assertEquals(response.getBank(), exampleTextPositive);
    }

    @Test
    public void test_setBank_negative() {
        response.setBank(exampleTextPositive);
        assertNotEquals(response.getBank(), exampleTextNegative);
    }

    @Test
    public void test_setCardCvv_positive() {
        response.setCardCVV(exampleTextPositive);
        assertEquals(response.getCardCVV(), exampleTextPositive);
    }

    @Test
    public void test_setCardCvv_negative() {
        response.setCardCVV(exampleTextPositive);
        assertNotEquals(response.getCardCVV(), exampleTextNegative);
    }

    @Test
    public void test_setCardExpiryMonth_positive() {
        response.setCardExpiryMonth(exampleTextPositive);
        assertEquals(response.getCardExpiryMonth(), exampleTextPositive);
    }

    @Test
    public void test_setCardExpiryMonth_negative() {
        response.setCardExpiryMonth(exampleTextPositive);
        assertNotEquals(response.getCardExpiryMonth(), exampleTextNegative);
    }

    @Test
    public void test_setCardExpiryYear_positive() {
        response.setCardExpiryYear(exampleTextPositive);
        assertEquals(response.getCardExpiryYear(), exampleTextPositive);
    }

    @Test
    public void test_setCardExpiryYear_negative() {
        response.setCardExpiryYear(exampleTextPositive);
        assertNotEquals(response.getCardExpiryYear(), exampleTextNegative);
    }

    @Test
    public void test_setCardNumber_positive() {
        response.setCardNumber(exampleTextPositive);
        assertEquals(response.getCardNumber(), exampleTextPositive);
    }

    @Test
    public void test_setCardNumber_negative() {
        response.setCardNumber(exampleTextPositive);
        assertNotEquals(response.getCardNumber(), exampleTextNegative);
    }

    @Test
    public void test_setCardType_positive() {
        response.setCardType(exampleTextPositive);
        assertEquals(response.getCardType(), exampleTextPositive);
    }

    @Test
    public void test_setCardType_negative() {
        response.setCardType(exampleTextPositive);
        assertNotEquals(response.getCardNumber(), exampleTextNegative);
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
    public void test_setSecure_positive() {
        response.setSecure(true);
        assertTrue(response.isSecure());
    }

    @Test
    public void test_setSecure_negative() {
        response.setSecure(true);
        assertNotEquals(response.isSecure(), false);
    }

    @Test
    public void test_setTwoClick_positive() {
        response.setTwoClick(true);
        assertTrue(response.isTwoClick());
    }

    @Test
    public void test_setTwoClick_negative() {
        response.setTwoClick(true);
        assertNotEquals(response.isTwoClick(), false);
    }

    @Test
    public void test_setSaved_positive() {
        response.setSaved(true);
        assertTrue(response.isSaved());
    }

    @Test
    public void test_setSaved_negative() {
        response.setSaved(true);
        assertNotEquals(response.isSaved(), false);
    }
}