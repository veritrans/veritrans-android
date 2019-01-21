package com.midtrans.sdk.corekit.models.merchant.checkout.request.optional;

import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.optional.Item;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class ItemTest {

    private Item item;
    private String exampleTextPositive, exampleTextNegative;
    private int exampleNumber;
    private double examplePrice;

    @Before
    public void test_setup() {
        this.item = new Item();
        this.exampleTextPositive = "exampleTextPositive";
        this.exampleTextNegative = "exampleTextNegative";
        this.examplePrice = 10;
        this.exampleNumber = 1;
    }

    @Test
    public void test_SetName_positive() {
        item.setName(exampleTextPositive);
        assertEquals(item.getName(), exampleTextPositive);
    }

    @Test
    public void test_SetName_negative() {
        item.setName(exampleTextPositive);
        assertNotEquals(item.getName(), exampleTextNegative);
    }

    @Test
    public void test_SetPrice_positive() {
        item.setPrice(examplePrice);
        assertEquals(Double.valueOf(item.getPrice()), Double.valueOf(item.getPrice()));
    }

    @Test
    public void test_SetPrice_negative() {
        item.setPrice(examplePrice);
        assertEquals(Double.valueOf(item.getPrice()), Double.valueOf(item.getPrice()));
    }

    @Test
    public void test_SetQuantity_positive() {
        item.setQuantity(exampleNumber);
        assertEquals(item.getQuantity(), exampleNumber);
    }
}