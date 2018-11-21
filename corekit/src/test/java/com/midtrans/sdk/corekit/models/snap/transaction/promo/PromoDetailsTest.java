package com.midtrans.sdk.corekit.models.snap.transaction.promo;

import com.midtrans.sdk.corekit.core.snap.model.transaction.response.promo.Promo;
import com.midtrans.sdk.corekit.core.snap.model.transaction.response.promo.PromoDetails;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class PromoDetailsTest {
    private PromoDetails promo;
    private ArrayList<Promo> exampleListPositive, exampleListNegative;

    @Before
    public void test_setup() throws Exception {
        this.promo = new PromoDetails();
        this.exampleListNegative = new ArrayList<>();
        this.exampleListPositive = new ArrayList<>();
        this.exampleListPositive.add(new Promo());
        this.exampleListNegative.add(null);
    }

    @Test
    public void test_setPromos_positive() throws Exception {
        promo.setPromos(exampleListPositive);
        assertEquals(promo.getPromos(), exampleListPositive);
    }

    @Test
    public void test_setPromos_negative() throws Exception {
        promo.setPromos(exampleListNegative);
        assertNotEquals(promo.getPromos(), exampleListPositive);
    }
}