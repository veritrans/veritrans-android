package com.midtrans.sdk.corekit.models.snap.pay.request.klickbca;

import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.klikbca.KlikBcaPaymentRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(PowerMockRunner.class)
public class KlikBcaPaymentRequestTest {
    private KlikBcaPaymentRequest response;
    private String exampleTextPositive;
    private String exampleTextNegative;

    @Before
    public void test_setup() {
        this.exampleTextPositive = "exampleTextPositive";
        this.exampleTextNegative = "exampleTextNegative";
        this.response = new KlikBcaPaymentRequest(exampleTextPositive, exampleTextPositive);
    }

    @Test
    public void test_setKlikBcaUserId_positive() {
        assertEquals(response.getKlikBcaUserId(), exampleTextPositive);
    }

    @Test
    public void test_setKlikBcaUserId_negative() {
        assertNotEquals(response.getKlikBcaUserId(), exampleTextNegative);
    }

}