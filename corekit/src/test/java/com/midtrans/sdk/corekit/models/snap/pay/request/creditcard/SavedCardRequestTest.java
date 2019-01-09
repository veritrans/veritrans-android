package com.midtrans.sdk.corekit.models.snap.pay.request.creditcard;

import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.creditcard.SaveCardRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(PowerMockRunner.class)
public class SavedCardRequestTest {
    private SaveCardRequest response;
    private String exampleTextPositive;
    private String exampleTextNegative;

    @Before
    public void test_setup() {
        this.exampleTextPositive = "exampleTextPositive";
        this.exampleTextNegative = "exampleTextNegative";
        this.response = new SaveCardRequest(exampleTextPositive,
                exampleTextPositive,
                exampleTextPositive);
    }

    @Test
    public void test_setSavedTokenId_positive() {
        assertEquals(response.getSavedTokenId(), exampleTextPositive);
    }

    @Test
    public void test_setSavedTokenId_negative() {
        assertNotEquals(response.getSavedTokenId(), exampleTextNegative);
    }

    @Test
    public void test_setMaskedCard_positive() {
        assertEquals(response.getMaskedCard(), exampleTextPositive);
    }

    @Test
    public void test_setMaskedCard_negative() {
        assertNotEquals(response.getMaskedCard(), exampleTextNegative);
    }

    @Test
    public void test_setType_positive() {
        assertEquals(response.getType(), exampleTextPositive);
    }

}