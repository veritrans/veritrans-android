package com.midtrans.sdk.corekit.models.merchant.checkout.request.specific.banktransfer;

import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.specific.banktransfer.BcaBankFreeText;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.specific.banktransfer.BcaBankFreeTextLanguage;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class BcaBankFreeTextTest {

    private BcaBankFreeText bcaBcankFreeText;
    private String exampleTextPositive, exampleTextNegative;
    private List<BcaBankFreeTextLanguage> exampleListPositive, exampleListNegative;

    @Before
    public void test_setup() {
        this.exampleTextPositive = "exampleTextPositive";
        this.exampleTextNegative = "exampleTextNegative";
        this.exampleListPositive = new ArrayList<>();
        this.exampleListNegative = new ArrayList<>();
        this.exampleListPositive.add(new BcaBankFreeTextLanguage(exampleTextPositive, exampleTextPositive));
        this.exampleListNegative.add(new BcaBankFreeTextLanguage(exampleTextNegative, exampleTextNegative));
        this.bcaBcankFreeText = new BcaBankFreeText(exampleListPositive, exampleListPositive);
    }

    @Test
    public void test_GetPayment_positive() {
        assertEquals(bcaBcankFreeText.getPayment(), exampleListPositive);
    }

    @Test
    public void test_GetPayment_negative() {
        assertNotEquals(bcaBcankFreeText.getPayment(), exampleListNegative);
    }

    @Test
    public void test_GetInquiry_positive() {
        assertEquals(bcaBcankFreeText.getInquiry(), exampleListPositive);
    }

    @Test
    public void test_GetInquiry_negative() {
        assertNotEquals(bcaBcankFreeText.getInquiry(), exampleListNegative);
    }

}