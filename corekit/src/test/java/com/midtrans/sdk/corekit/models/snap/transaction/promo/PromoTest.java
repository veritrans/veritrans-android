package com.midtrans.sdk.corekit.models.snap.transaction.promo;

import com.midtrans.sdk.corekit.core.snap.model.transaction.response.promo.Promo;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class PromoTest {
    private Promo promo;

    @Before
    public void setUp() throws Exception {
        this.promo = new Promo();
    }

    @Test
    public void test_setSelected_positive() throws Exception {
        promo.setSelected(true);
        assertEquals(promo.isSelected(), true);
    }

    @Test
    public void test_setSelected_negative() throws Exception {
        promo.setSelected(false);
        assertNotEquals(promo.isSelected(), true);
    }
}