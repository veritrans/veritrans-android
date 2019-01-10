package com.midtrans.sdk.corekit.models.snap.pay.request.creditcard;

import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.creditcard.CreditCardPaymentParams;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
public class CreditCardPaymentParamsTest {
    private CreditCardPaymentParams response;
    private String exampleTextPositive;
    private String exampleTextNegative;

    @Before
    public void test_setup() {
        this.exampleTextPositive = "exampleTextPositive";
        this.exampleTextNegative = "exampleTextNegative";
        this.response = new CreditCardPaymentParams(exampleTextPositive,
                true,
                exampleTextPositive);
    }

    @Test
    public void test_setCardToken_positive() {
        assertEquals(response.getCardToken(), exampleTextPositive);
    }

    @Test
    public void test_setCardToken_negative() {
        assertNotEquals(response.getCardToken(), exampleTextNegative);
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
    public void test_setSaveCard_positive() {
        assertTrue(response.isSaveCard());
    }

    @Test
    public void test_setSaveCard_negative() {
        assertNotEquals(response.isSaveCard(), false);
    }

}