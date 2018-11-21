package com.midtrans.sdk.corekit.models.merchant.checkout.request.optional;

import com.midtrans.sdk.corekit.core.merchant.model.checkout.request.optional.ItemDetails;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ItemDetailsTest {

    private ItemDetails itemDetails;
    private String exampleTextPositive, exampleTextNegative;
    private int exampleNumber;
    private double examplePrice;

    @Before
    public void test_setup() {
        this.itemDetails = new ItemDetails();
        this.exampleTextPositive = "exampleTextPositive";
        this.exampleTextNegative = "exampleTextNegative";
        this.examplePrice = 10;
        this.exampleNumber = 1;
    }

    @Test
    public void test_SetName_positive() {
        itemDetails.setName(exampleTextPositive);
        assertEquals(itemDetails.getName(), exampleTextPositive);
    }

    @Test
    public void test_SetName_negative() {
        itemDetails.setName(exampleTextPositive);
        assertNotEquals(itemDetails.getName(), exampleTextNegative);
    }

    @Test
    public void test_SetPrice_positive() {
        itemDetails.setPrice(examplePrice);
        assertEquals(Double.valueOf(itemDetails.getPrice()), Double.valueOf(itemDetails.getPrice()));
    }

    @Test
    public void test_SetPrice_negative() {
        itemDetails.setPrice(examplePrice);
        assertEquals(Double.valueOf(itemDetails.getPrice()), Double.valueOf(itemDetails.getPrice()));
    }

    @Test
    public void test_SetQuantity_positive() {
        itemDetails.setQuantity(exampleNumber);
        assertEquals(itemDetails.getQuantity(), exampleNumber);
    }
}