package com.midtrans.sdk.corekit.models.merchant.checkout.request.optional;

import com.midtrans.sdk.corekit.base.enums.ExpiryModelUnit;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.optional.ExpiryModel;

import org.junit.Before;
import org.junit.Test;

import static com.midtrans.sdk.corekit.base.enums.ExpiryModelUnit.EXPIRY_UNIT_DAY;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ExpiryModelTest {

    private ExpiryModel expiryModel;
    private String exampleTextPositive, exampleTextNegative;
    private int exampleIntPositive, exampleIntNegative;

    @Before
    public void test_setup() {
        this.expiryModel = new ExpiryModel("", EXPIRY_UNIT_DAY, 1);
        this.exampleTextPositive = "exampleTextPositive";
        this.exampleTextNegative = "exampleTextNegative";
        this.exampleIntNegative = 0;
        this.exampleIntPositive = 1;
    }

    @Test
    public void test_SetDuration_positive() {
        expiryModel.setDuration(exampleIntPositive);
        assertEquals(expiryModel.getDuration(), exampleIntPositive);
    }

    @Test
    public void test_SetStartTime_positive() {
        expiryModel.setStartTime(exampleTextPositive);
        assertEquals(expiryModel.getStartTime(), exampleTextPositive);
    }

    @Test
    public void test_SetStartTime_negative() {
        expiryModel.setStartTime(exampleTextPositive);
        assertNotEquals(expiryModel.getStartTime(), exampleTextNegative);
    }

    @Test
    public void test_SetUnit_positive() {
        expiryModel.setUnit(EXPIRY_UNIT_DAY);
        assertEquals(expiryModel.getUnit(), EXPIRY_UNIT_DAY);
    }

    @Test
    public void test_SetUnit_negative() {
        expiryModel.setUnit(ExpiryModelUnit.EXPIRY_UNIT_HOUR);
        assertNotEquals(expiryModel.getUnit(), exampleTextNegative);
    }

}