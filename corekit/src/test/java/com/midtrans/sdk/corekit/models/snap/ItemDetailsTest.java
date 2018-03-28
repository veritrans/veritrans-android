package com.midtrans.sdk.corekit.models.snap;

import android.text.TextUtils;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;

import static org.junit.Assert.assertEquals;

/**
 * @author rakawm
 */

@RunWith(MockitoJUnitRunner.class)
@PrepareForTest({TextUtils.class})
public class ItemDetailsTest {

    private ItemDetails itemDetails;
    private String exampleText;
    private int exampleNumber;
    private long examplePrice;

    @Before
    public void setUp() throws Exception {
        itemDetails = new ItemDetails();
        exampleText = "example";
        examplePrice = 10;
        exampleNumber = 1;
    }

    @Test
    public void testSetName() throws Exception {
        itemDetails.setName(exampleText);
        assertEquals(itemDetails.getName(), exampleText);
    }

    @Test
    public void testSetPrice() throws Exception {
        itemDetails.setPrice(examplePrice);
        assertEquals(itemDetails.getPrice(), examplePrice);
    }

    @Test
    public void testSetQuantity() throws Exception {
        itemDetails.setQuantity(exampleNumber);
        assertEquals(itemDetails.getQuantity(), exampleNumber);
    }
}