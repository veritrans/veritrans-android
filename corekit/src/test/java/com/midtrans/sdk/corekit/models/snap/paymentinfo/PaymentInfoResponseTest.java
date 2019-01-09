package com.midtrans.sdk.corekit.models.snap.paymentinfo;

import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.PaymentInfoResponse;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class PaymentInfoResponseTest {

    private PaymentInfoResponse response;
    private String exampleTextPositive, exampleTextNegative;
    private ArrayList<String> exampleListPositive, exampleListNegative;

    @Before
    public void test_setup() throws Exception {
        this.response = new PaymentInfoResponse();
        this.exampleTextPositive = "examplePositive";
        this.exampleTextNegative = "exampleNegative";
        this.exampleListNegative = new ArrayList<>();
        this.exampleListPositive = new ArrayList<>();
        this.exampleListPositive.add(exampleTextPositive);
        this.exampleListNegative.add(exampleTextNegative);
    }

    @Test
    public void test_setToken_positive() throws Exception {
        response.setToken(exampleTextPositive);
        assertEquals(response.getToken(), exampleTextPositive);
    }

    @Test
    public void test_setToken_negative() throws Exception {
        response.setToken(exampleTextPositive);
        assertNotEquals(response.getToken(), exampleTextNegative);
    }
}