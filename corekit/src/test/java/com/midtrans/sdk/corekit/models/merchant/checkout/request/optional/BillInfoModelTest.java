package com.midtrans.sdk.corekit.models.merchant.checkout.request.optional;

import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.optional.BillInfoModel;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class BillInfoModelTest {

    private BillInfoModel billInfoModel;
    private String exampleTextPositive, exampleTextNegative;

    @Before
    public void test_setup() {
        this.exampleTextPositive = "exampleTextPositive";
        this.exampleTextNegative = "exampleTextNegative";
        this.billInfoModel = new BillInfoModel(exampleTextPositive, exampleTextPositive);
    }

    @Test
    public void test_SetBillInfo1_positive() {
        billInfoModel.setBillInfo1(exampleTextPositive);
        assertEquals(billInfoModel.getBillInfo1(), exampleTextPositive);
    }

    @Test
    public void test_SetBillInfo1_negative() {
        billInfoModel.setBillInfo1(exampleTextPositive);
        assertNotEquals(billInfoModel.getBillInfo1(), exampleTextNegative);
    }

    @Test
    public void test_SetBillInfo2_positive() {
        billInfoModel.setBillInfo2(exampleTextPositive);
        assertEquals(billInfoModel.getBillInfo2(), exampleTextPositive);
    }

    @Test
    public void test_SetBillInfo2_negative() {
        billInfoModel.setBillInfo2(exampleTextPositive);
        assertNotEquals(billInfoModel.getBillInfo2(), exampleTextNegative);
    }

    @Test
    public void test_SetBillInfo3_positive() {
        billInfoModel.setBillInfo3(exampleTextPositive);
        assertEquals(billInfoModel.getBillInfo3(), exampleTextPositive);
    }

    @Test
    public void test_SetBillInfo3_negative() {
        billInfoModel.setBillInfo3(exampleTextPositive);
        assertNotEquals(billInfoModel.getBillInfo3(), exampleTextNegative);
    }

    @Test
    public void test_SetBillInfo4_positive() {
        billInfoModel.setBillInfo4(exampleTextPositive);
        assertEquals(billInfoModel.getBillInfo4(), exampleTextPositive);
    }

    @Test
    public void test_SetBillInfo4_negative() {
        billInfoModel.setBillInfo4(exampleTextPositive);
        assertNotEquals(billInfoModel.getBillInfo4(), exampleTextNegative);
    }

    @Test
    public void test_SetBillInfo5_positive() {
        billInfoModel.setBillInfo5(exampleTextPositive);
        assertEquals(billInfoModel.getBillInfo5(), exampleTextPositive);
    }

    @Test
    public void test_SetBillInfo5_negative() {
        billInfoModel.setBillInfo5(exampleTextPositive);
        assertNotEquals(billInfoModel.getBillInfo5(), exampleTextNegative);
    }

    @Test
    public void test_SetBillInfo6_positive() {
        billInfoModel.setBillInfo6(exampleTextPositive);
        assertEquals(billInfoModel.getBillInfo6(), exampleTextPositive);
    }

    @Test
    public void test_SetBillInfo6_negative() {
        billInfoModel.setBillInfo6(exampleTextPositive);
        assertNotEquals(billInfoModel.getBillInfo6(), exampleTextNegative);
    }

    @Test
    public void test_SetBillInfo7_positive() {
        billInfoModel.setBillInfo7(exampleTextPositive);
        assertEquals(billInfoModel.getBillInfo7(), exampleTextPositive);
    }

    @Test
    public void test_SetBillInfo7_negative() {
        billInfoModel.setBillInfo7(exampleTextPositive);
        assertNotEquals(billInfoModel.getBillInfo7(), exampleTextNegative);
    }

    @Test
    public void test_SetBillInfo8_positive() {
        billInfoModel.setBillInfo8(exampleTextPositive);
        assertEquals(billInfoModel.getBillInfo8(), exampleTextPositive);
    }

    @Test
    public void test_SetBillInfo8_negative() {
        billInfoModel.setBillInfo8(exampleTextPositive);
        assertNotEquals(billInfoModel.getBillInfo8(), exampleTextNegative);
    }

}