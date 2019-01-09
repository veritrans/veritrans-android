package com.midtrans.sdk.corekit.models.merchant.checkout.request.specific.banktransfer;

import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.specific.banktransfer.BankTransferRequestModel;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class BankTransferRequestModelTest {

    private BankTransferRequestModel bankTransferRequestModel;
    private String exampleTextPositive, exampleTextNegative;

    @Before
    public void test_setup() {
        this.bankTransferRequestModel = new BankTransferRequestModel();
        this.exampleTextPositive = "exampleTextPositive";
        this.exampleTextNegative = "exampleTextNegative";
    }

    @Test
    public void test_SetVaNumber_positive() {
        bankTransferRequestModel.setVaNumber(exampleTextPositive);
        assertEquals(bankTransferRequestModel.getVaNumber(), exampleTextPositive);
    }

    @Test
    public void test_SetVaNumber_negative() {
        bankTransferRequestModel.setVaNumber(exampleTextPositive);
        assertNotEquals(bankTransferRequestModel.getVaNumber(), exampleTextNegative);
    }

}