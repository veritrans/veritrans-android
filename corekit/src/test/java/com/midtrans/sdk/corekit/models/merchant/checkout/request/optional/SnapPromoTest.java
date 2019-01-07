package com.midtrans.sdk.corekit.models.merchant.checkout.request.optional;

import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.optional.SnapPromo;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class SnapPromoTest {

    private SnapPromo snapPromo;
    private String exampleTextPositive, exampleTextNegative;
    private List<String> exampleListPositive, exampleListNegative;

    @Before
    public void test_setup() {
        this.snapPromo = new SnapPromo();
        this.exampleTextPositive = "exampleTextPositive";
        this.exampleTextNegative = "exampleTextNegative";
        this.exampleListPositive = new ArrayList<>();
        this.exampleListNegative = new ArrayList<>();
        this.exampleListPositive.add(exampleTextPositive);
        this.exampleListNegative.add(exampleTextNegative);
    }

    @Test
    public void test_SetEnabled_positive() {
        snapPromo.setEnabled(true);
        assertTrue(snapPromo.isEnabled());
    }

    @Test
    public void test_SetEnabled_negative() {
        snapPromo.setEnabled(false);
        assertFalse(false);
    }

    @Test
    public void test_SetAllowedPromoCodes_positive() {
        snapPromo.setAllowedPromoCodes(exampleListPositive);
        assertEquals(snapPromo.getAllowedPromoCodes(), exampleListPositive);
    }

    @Test
    public void test_SetAllowedPromoCodes_negative() {
        snapPromo.setAllowedPromoCodes(exampleListPositive);
        assertNotEquals(snapPromo.getAllowedPromoCodes(), exampleListNegative);
    }

}