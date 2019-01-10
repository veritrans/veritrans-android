package com.midtrans.sdk.corekit.models.snap.pay.response.va;

import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.va.VaNumber;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(PowerMockRunner.class)
public class VaNumberTest {
    private VaNumber response;
    private String exampleTextPositive;
    private String exampleTextNegative;

    @Before
    public void test_setup() {
        this.exampleTextPositive = "exampleTextPositive";
        this.exampleTextNegative = "exampleTextNegative";
        this.response = new VaNumber();
    }

    @Test
    public void test_setAccountNumber_positive() {
        this.response.setAccountNumber(exampleTextPositive);
        assertEquals(response.getAccountNumber(), exampleTextPositive);
    }

    @Test
    public void test_setAccountNumber_negative() {
        this.response.setAccountNumber(exampleTextPositive);
        assertNotEquals(response.getAccountNumber(), exampleTextNegative);
    }

    @Test
    public void test_setBank_positive() {
        this.response.setBank(exampleTextPositive);
        assertEquals(response.getBank(), exampleTextPositive);
    }

    @Test
    public void test_setBank_negative() {
        this.response.setBank(exampleTextPositive);
        assertNotEquals(response.getBank(), exampleTextNegative);
    }

}