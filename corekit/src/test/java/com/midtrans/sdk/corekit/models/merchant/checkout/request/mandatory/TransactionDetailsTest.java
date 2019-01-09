package com.midtrans.sdk.corekit.models.merchant.checkout.request.mandatory;

import android.text.TextUtils;

import com.midtrans.sdk.corekit.base.model.Currency;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.mandatory.TransactionDetails;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(PowerMockRunner.class)
@PrepareForTest({TextUtils.class})
public class TransactionDetailsTest {

    private TransactionDetails transactionDetails;
    private String exampleTextPositive, exampleTextNegative;
    private double examplePricePositive, examplePriceNegative;

    @Before
    public void testSetup() {
        PowerMockito.mockStatic(TextUtils.class);
        this.exampleTextPositive = "exampleTextPositive";
        this.exampleTextNegative = "exampleTextNegative";
        this.examplePricePositive = 10;
        this.examplePriceNegative = 1;
        this.transactionDetails = new TransactionDetails(exampleTextPositive, examplePricePositive);
    }

    @Test
    public void test_SetCurrent_positive() {
        this.transactionDetails.setCurrency(Currency.IDR);
        assertEquals(transactionDetails.getCurrency(), Currency.IDR);
    }

    @Test
    public void test_SetCurrency_negative() {
        this.transactionDetails.setCurrency(exampleTextPositive);
        assertNotEquals(transactionDetails.getCurrency(), exampleTextNegative);
    }

    @Test
    public void test_GetOrderId_positive() {
        assertEquals(transactionDetails.getOrderId(), exampleTextPositive);
    }

    @Test
    public void test_GetOrderId_negative() {
        assertNotEquals(transactionDetails.getOrderId(), exampleTextNegative);
    }

    @Test
    public void test_GetAmount_negative() {
        assertNotEquals(transactionDetails.getGrossAmount(), examplePriceNegative);
    }
}