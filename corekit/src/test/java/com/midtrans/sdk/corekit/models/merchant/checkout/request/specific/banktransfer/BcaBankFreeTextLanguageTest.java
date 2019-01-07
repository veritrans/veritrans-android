package com.midtrans.sdk.corekit.models.merchant.checkout.request.specific.banktransfer;

import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.specific.banktransfer.BcaBankFreeTextLanguage;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class BcaBankFreeTextLanguageTest {

    private BcaBankFreeTextLanguage bcaBcankFreeTextLanguage;
    private String exampleTextPositive, exampleTextNegative;

    @Before
    public void test_setup() {
        this.exampleTextPositive = "exampleTextPositive";
        this.exampleTextNegative = "exampleTextNegative";
        this.bcaBcankFreeTextLanguage = new BcaBankFreeTextLanguage(exampleTextPositive, exampleTextPositive);
    }

    @Test
    public void test_GetId_positive() {
        assertEquals(bcaBcankFreeTextLanguage.getId(), exampleTextPositive);
    }

    @Test
    public void test_GetId_negative() {
        assertNotEquals(bcaBcankFreeTextLanguage.getId(), exampleTextNegative);
    }

    @Test
    public void test_GetEn_positive() {
        assertEquals(bcaBcankFreeTextLanguage.getEn(), exampleTextPositive);
    }

    @Test
    public void test_GetEn_negative() {
        assertNotEquals(bcaBcankFreeTextLanguage.getEn(), exampleTextNegative);
    }

}