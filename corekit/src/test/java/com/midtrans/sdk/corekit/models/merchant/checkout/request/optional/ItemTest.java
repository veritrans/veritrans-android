package com.midtrans.sdk.corekit.models.merchant.checkout.request.optional;

import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.optional.Items;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ItemsTest {

    private Items items;
    private String exampleTextPositive, exampleTextNegative;
    private int exampleNumber;
    private double examplePrice;

    @Before
    public void test_setup() {
        this.items = new Items();
        this.exampleTextPositive = "exampleTextPositive";
        this.exampleTextNegative = "exampleTextNegative";
        this.examplePrice = 10;
        this.exampleNumber = 1;
    }

    @Test
    public void test_SetName_positive() {
        items.setName(exampleTextPositive);
        assertEquals(items.getName(), exampleTextPositive);
    }

    @Test
    public void test_SetName_negative() {
        items.setName(exampleTextPositive);
        assertNotEquals(items.getName(), exampleTextNegative);
    }

    @Test
    public void test_SetPrice_positive() {
        items.setPrice(examplePrice);
        assertEquals(Double.valueOf(items.getPrice()), Double.valueOf(items.getPrice()));
    }

    @Test
    public void test_SetPrice_negative() {
        items.setPrice(examplePrice);
        assertEquals(Double.valueOf(items.getPrice()), Double.valueOf(items.getPrice()));
    }

    @Test
    public void test_SetQuantity_positive() {
        items.setQuantity(exampleNumber);
        assertEquals(items.getQuantity(), exampleNumber);
    }
}