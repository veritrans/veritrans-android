package com.midtrans.sdk.corekit.models.snap.pay.response.bcaklikpay;

import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.bcaklikpay.BcaKlikPayDataParams;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(PowerMockRunner.class)
public class BcaKlikPayDataParamsTest {
    private BcaKlikPayDataParams response;
    private String exampleTextPositive;
    private String exampleTextNegative;

    @Before
    public void test_setup() {
        this.exampleTextPositive = "exampleTextPositive";
        this.exampleTextNegative = "exampleTextNegative";
        this.response = new BcaKlikPayDataParams();
    }

    @Test
    public void test_setCallback_positive() {
        this.response.setCallback(exampleTextPositive);
        assertEquals(response.getCallback(), exampleTextPositive);
    }

    @Test
    public void test_setCallback_negative() {
        this.response.setCallback(exampleTextPositive);
        assertNotEquals(response.getCallback(), exampleTextNegative);
    }

    @Test
    public void test_setCurrency_positive() {
        this.response.setCurrency(exampleTextPositive);
        assertEquals(response.getCurrency(), exampleTextPositive);
    }

    @Test
    public void test_setCurrency_negative() {
        this.response.setCurrency(exampleTextPositive);
        assertNotEquals(response.getCurrency(), exampleTextNegative);
    }

    @Test
    public void test_setDescp_positive() {
        this.response.setDescp(exampleTextPositive);
        assertEquals(response.getDescp(), exampleTextPositive);
    }

    @Test
    public void test_setDescp_negative() {
        this.response.setDescp(exampleTextPositive);
        assertNotEquals(response.getDescp(), exampleTextNegative);
    }

    @Test
    public void test_setKlikPayCode_positive() {
        this.response.setKlikPayCode(exampleTextPositive);
        assertEquals(response.getKlikPayCode(), exampleTextPositive);
    }

    @Test
    public void test_setKlikPayCOde_negative() {
        this.response.setKlikPayCode(exampleTextPositive);
        assertNotEquals(response.getKlikPayCode(), exampleTextNegative);
    }

    @Test
    public void test_setMiscFee_positive() {
        this.response.setMiscFee(exampleTextPositive);
        assertEquals(response.getMiscFee(), exampleTextPositive);
    }

    @Test
    public void test_setMiscFee_negative() {
        this.response.setMiscFee(exampleTextPositive);
        assertNotEquals(response.getMiscFee(), exampleTextNegative);
    }

    @Test
    public void test_setPayType_positive() {
        this.response.setPayType(exampleTextPositive);
        assertEquals(response.getPayType(), exampleTextPositive);
    }

    @Test
    public void test_setPayType_negative() {
        this.response.setPayType(exampleTextPositive);
        assertNotEquals(response.getPayType(), exampleTextNegative);
    }

    @Test
    public void test_setSignature_positive() {
        this.response.setSignature(exampleTextPositive);
        assertEquals(response.getSignature(), exampleTextPositive);
    }

    @Test
    public void test_setSignature_negative() {
        this.response.setSignature(exampleTextPositive);
        assertNotEquals(response.getSignature(), exampleTextNegative);
    }

    @Test
    public void test_setTotalAmount_positive() {
        this.response.setTotalAmount(exampleTextPositive);
        assertEquals(response.getTotalAmount(), exampleTextPositive);
    }

    @Test
    public void test_setTotalAmount_negative() {
        this.response.setTotalAmount(exampleTextPositive);
        assertNotEquals(response.getTotalAmount(), exampleTextNegative);
    }

    @Test
    public void test_setTransactionDate_positive() {
        this.response.setTransactionDate(exampleTextPositive);
        assertEquals(response.getTransactionDate(), exampleTextPositive);
    }

    @Test
    public void test_setTransactionDate_negative() {
        this.response.setTransactionDate(exampleTextPositive);
        assertNotEquals(response.getTransactionDate(), exampleTextNegative);
    }

    @Test
    public void test_setTransactionNo_positive() {
        this.response.setTransactionNo(exampleTextPositive);
        assertEquals(response.getTransactionNo(), exampleTextPositive);
    }

    @Test
    public void test_setTransactionNo_negative() {
        this.response.setTransactionNo(exampleTextPositive);
        assertNotEquals(response.getTransactionNo(), exampleTextNegative);
    }

}