package com.midtrans.sdk.corekit.models.merchant.checkout.request.optional;

import com.midtrans.sdk.corekit.core.merchant.model.checkout.request.optional.GopayDeepLink;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class GopayDeepLinkTest {

    private GopayDeepLink gopayDeepLink;
    private String exampleTextPositive, exampleTextNegative;

    @Before
    public void test_setup() {
        this.exampleTextPositive = "exampleTextPositive";
        this.exampleTextNegative = "exampleTextNegative";
        this.gopayDeepLink = new GopayDeepLink(exampleTextPositive);
    }

    @Test
    public void test_SetMerchantGopayDeepLink_positive() {
        gopayDeepLink.setMerchantGopayDeeplink(exampleTextPositive);
        assertEquals(gopayDeepLink.getMerchantGopayDeeplink(), exampleTextPositive);
    }

    @Test
    public void test_SetMerchantGopayDeepLink_negative() {
        gopayDeepLink.setMerchantGopayDeeplink(exampleTextPositive);
        assertNotEquals(gopayDeepLink.getMerchantGopayDeeplink(), exampleTextNegative);
    }

}