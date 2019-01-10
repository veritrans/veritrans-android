package com.midtrans.sdk.corekit.models.snap.pay.request.mandiriklik;

import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.mandiriclick.MandiriClickpayParams;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.mandiriclick.MandiriClickpayPaymentRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(PowerMockRunner.class)
public class MandiriKlikPaymentRequestTest {
    private MandiriClickpayPaymentRequest response;
    private String exampleTextPositive;
    private String exampleTextNegative;
    private MandiriClickpayParams mandiriClickpayParams;

    @Before
    public void test_setup() {
        this.exampleTextPositive = "exampleTextPositive";
        this.exampleTextNegative = "exampleTextNegative";
        this.mandiriClickpayParams = new MandiriClickpayParams(exampleTextPositive, exampleTextPositive, exampleTextPositive);
        this.response = new MandiriClickpayPaymentRequest(exampleTextPositive, mandiriClickpayParams);
    }

    @Test
    public void test_setKlikBcaUserId_positive() {
        assertEquals(response.getPaymentParams(), this.mandiriClickpayParams);
    }

    @Test
    public void test_setKlikBcaUserId_negative() {
        assertNotEquals(response.getPaymentParams(), new MandiriClickpayPaymentRequest(null, null));
    }

}