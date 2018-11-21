package com.midtrans.sdk.corekit.models.merchant.checkout.request.optional;

import com.midtrans.sdk.corekit.core.merchant.model.checkout.request.optional.GopayDeeplink;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class GopayDeeplinkTest {

    private GopayDeeplink gopayDeeplink;
    private String exampleTextPositive, exampleTextNegative;

    @Before
    public void test_setup() {
        this.gopayDeeplink = new GopayDeeplink();
        this.exampleTextPositive = "exampleTextPositive";
        this.exampleTextNegative = "exampleTextNegative";
    }

    @Test
    public void test_SetMerchantGopayDeepLink_positive() {
        gopayDeeplink.setMerchantGopayDeeplink(exampleTextPositive);
        assertEquals(gopayDeeplink.getMerchantGopayDeeplink(), exampleTextPositive);
    }

    @Test
    public void test_SetMerchantGopayDeepLink_negative() {
        gopayDeeplink.setMerchantGopayDeeplink(exampleTextPositive);
        assertNotEquals(gopayDeeplink.getMerchantGopayDeeplink(), exampleTextNegative);
    }

}