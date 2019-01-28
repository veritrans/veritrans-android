package com.midtrans.sdk.corekit.models.merchant.checkout.request.specific.banktransfer;

import com.midtrans.sdk.corekit.SDKConfigTest;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.specific.banktransfer.BcaBankFreeText;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.specific.banktransfer.BcaBankFreeTextLanguage;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.specific.banktransfer.BcaBankTransferRequestModel;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class BcaBankTransferRequestModelTest {

    private BcaBankTransferRequestModel bcaBankTransferRequestModel;
    private String exampleTextPositive, exampleTextNegative;

    @Before
    public void test_setup() {
        this.exampleTextPositive = "exampleTextPositive";
        this.exampleTextNegative = "exampleTextNegative";
        this.bcaBankTransferRequestModel = new BcaBankTransferRequestModel(SDKConfigTest.CARD_NUMBER,
                new BcaBankFreeText(new ArrayList<BcaBankFreeTextLanguage>(),
                        new ArrayList<BcaBankFreeTextLanguage>()),
                SDKConfigTest.BANK);
    }

    @Test
    public void test_SetSubCompany_positive() {
        assertEquals(bcaBankTransferRequestModel.getSubCompanyCode(), SDKConfigTest.BANK);
    }

    @Test
    public void test_SetSubCompany_negative() {
        assertNotEquals(bcaBankTransferRequestModel.getSubCompanyCode(), exampleTextNegative);
    }

    @Test
    public void test_SetVaNumber_positive() {
        bcaBankTransferRequestModel.setVaNumber(exampleTextPositive);
        assertEquals(bcaBankTransferRequestModel.getVaNumber(), exampleTextPositive);
    }

    @Test
    public void test_SetVaNumber_negative() {
        bcaBankTransferRequestModel.setVaNumber(exampleTextPositive);
        assertNotEquals(bcaBankTransferRequestModel.getVaNumber(), exampleTextNegative);
    }

}