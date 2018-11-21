package com.midtrans.sdk.corekit.models.snap.transaction.promo;

import com.midtrans.sdk.corekit.core.snap.model.transaction.response.promo.PromoResponse;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class PromoResponseTest {
    private PromoResponse promoResponse;
    private String exampleTextPositive, exampleTextNegative;
    private int exampleIntPositive, exampleIntNegative;
    private ArrayList<String> exampleListPositive, exampleListNegative;

    @Before
    public void setUp() throws Exception {
        this.promoResponse = new PromoResponse();
        this.exampleTextPositive = "examplePositive";
        this.exampleTextNegative = "exampleNegative";
        this.exampleIntNegative = 1;
        this.exampleIntPositive = 0;
        this.exampleListPositive = new ArrayList<>();
        this.exampleListNegative = new ArrayList<>();
        this.exampleListPositive.add(exampleTextPositive);
        this.exampleListNegative.add(exampleTextNegative);
    }

    @Test
    public void test_setBins_positive() throws Exception {
        promoResponse.setBins(exampleListPositive);
        assertEquals(promoResponse.getBins(), exampleListPositive);
    }

    @Test
    public void test_setBins_negative() throws Exception {
        promoResponse.setBins(exampleListNegative);
        assertNotEquals(promoResponse.getBins(), exampleListPositive);
    }

    @Test
    public void test_setDiscountAmount_positive() throws Exception {
        promoResponse.setDiscountAmount(exampleIntPositive);
        assertEquals(promoResponse.getDiscountAmount(), exampleIntPositive);
    }

    @Test
    public void test_setDiscountAmount_negative() throws Exception {
        promoResponse.setDiscountAmount(exampleIntPositive);
        assertNotEquals(promoResponse.getDiscountAmount(), exampleIntNegative);
    }

    @Test
    public void test_setDiscountType_positive() throws Exception {
        promoResponse.setDiscountType(exampleTextPositive);
        assertEquals(promoResponse.getDiscountType(), exampleTextPositive);
    }

    @Test
    public void test_setDiscountType_negative() throws Exception {
        promoResponse.setDiscountType(exampleTextPositive);
        assertNotEquals(promoResponse.getDiscountType(), exampleTextNegative);
    }

    @Test
    public void test_setEndDate_positive() throws Exception {
        promoResponse.setEndDate(exampleTextPositive);
        assertEquals(promoResponse.getEndDate(), exampleTextPositive);
    }

    @Test
    public void test_setEndDate_negative() throws Exception {
        promoResponse.setEndDate(exampleTextPositive);
        assertNotEquals(promoResponse.getEndDate(), exampleTextNegative);
    }

    @Test
    public void test_setId_positive() throws Exception {
        promoResponse.setId(exampleIntPositive);
        assertEquals(promoResponse.getId(), exampleIntPositive);
    }

    @Test
    public void test_setId_negative() throws Exception {
        promoResponse.setId(exampleIntPositive);
        assertNotEquals(promoResponse.getId(), exampleIntNegative);
    }

    @Test
    public void test_setPreferences_positive() throws Exception {
        promoResponse.setPromoCode(exampleTextPositive);
        assertEquals(promoResponse.getPromoCode(), exampleTextPositive);
    }

    @Test
    public void test_setPreferences_negative() throws Exception {
        promoResponse.setPromoCode(exampleTextPositive);
        assertNotEquals(promoResponse.getPromoCode(), exampleTextNegative);
    }

    @Test
    public void test_setStartDate_positive() throws Exception {
        promoResponse.setStartDate(exampleTextPositive);
        assertEquals(promoResponse.getStartDate(), exampleTextPositive);
    }

    @Test
    public void test_setStartDate_negative() throws Exception {
        promoResponse.setStartDate(exampleTextPositive);
        assertNotEquals(promoResponse.getStartDate(), exampleTextNegative);
    }

    @Test
    public void test_setSponsorMessageEn_positive() throws Exception {
        promoResponse.setSponsorMessageEn(exampleTextPositive);
        assertEquals(promoResponse.getSponsorMessageEn(), exampleTextPositive);
    }

    @Test
    public void test_setSponsorMessageEn_negative() throws Exception {
        promoResponse.setSponsorMessageEn(exampleTextPositive);
        assertNotEquals(promoResponse.getSponsorMessageEn(), exampleTextNegative);
    }

    @Test
    public void test_setSponsorMessageId_positive() throws Exception {
        promoResponse.setSponsorMessageId(exampleTextPositive);
        assertEquals(promoResponse.getSponsorMessageId(), exampleTextPositive);
    }

    @Test
    public void test_setSponsorMessageId_negative() throws Exception {
        promoResponse.setSponsorMessageId(exampleTextPositive);
        assertNotEquals(promoResponse.getSponsorMessageId(), exampleTextNegative);
    }

    @Test
    public void test_setSponsorName_positive() throws Exception {
        promoResponse.setSponsorName(exampleTextPositive);
        assertEquals(promoResponse.getSponsorName(), exampleTextPositive);
    }

    @Test
    public void test_setSponsorName_negative() throws Exception {
        promoResponse.setSponsorName(exampleTextPositive);
        assertNotEquals(promoResponse.getSponsorName(), exampleTextNegative);
    }
}