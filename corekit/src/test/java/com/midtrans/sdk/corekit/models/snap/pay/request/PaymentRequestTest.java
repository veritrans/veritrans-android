package com.midtrans.sdk.corekit.models.snap.pay.request;

import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.CustomerDetailPayRequest;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.PaymentRequest;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(PowerMockRunner.class)
public class PaymentRequestTest {
    private PaymentRequest response;
    private CustomerDetailPayRequest customerDetailPayRequest;
    private String exampleTextPositive;
    private String exampleTextNegative;

    @Before
    public void test_setup() {
        this.exampleTextPositive = "exampleTextPositive";
        this.exampleTextNegative = "exampleTextNegative";
        this.customerDetailPayRequest = new CustomerDetailPayRequest(exampleTextPositive,exampleTextPositive,exampleTextPositive);
        this.response = new PaymentRequest(exampleTextPositive,customerDetailPayRequest);
    }

    @Test
    public void test_setCustomerDetail_positive() {
        assertEquals(response.getCustomerDetails(), customerDetailPayRequest);
    }

    @Test
    public void test_setCustomerDetail_negative() {
        assertNotEquals(response.getCustomerDetails(), new CustomerDetailPayRequest());
    }

}