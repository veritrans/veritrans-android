package com.midtrans.sdk.corekit.models.midtrans.tokendetails;

import com.midtrans.sdk.corekit.core.api.midtrans.model.tokendetails.TokenDetailsResponse;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.modules.junit4.PowerMockRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

@RunWith(PowerMockRunner.class)
public class TokenDetailResponseTest {
    private TokenDetailsResponse response;
    private String exampleTextPositive;
    private String exampleTextNegative;

    @Before
    public void test_setup() {
        this.response = new TokenDetailsResponse();
        this.exampleTextPositive = "exampleTextPositive";
        this.exampleTextNegative = "exampleTextNegative";
    }

    @Test
    public void test_setBank_positive() {
        response.setBank(exampleTextPositive);
        assertEquals(response.getBank(), exampleTextPositive);
    }

    @Test
    public void test_setBank_negative() {
        response.setBank(exampleTextPositive);
        assertNotEquals(response.getBank(), exampleTextNegative);
    }

    @Test
    public void test_setRedirectUrl_positive() {
        response.setRedirectUrl(exampleTextPositive);
        assertEquals(response.getRedirectUrl(), exampleTextPositive);
    }

    @Test
    public void test_setRedirectUrl_negative() {
        response.setRedirectUrl(exampleTextPositive);
        assertNotEquals(response.getRedirectUrl(), exampleTextNegative);
    }

    @Test
    public void test_setStatusMessage_positive() {
        response.setStatusMessage(exampleTextPositive);
        assertEquals(response.getStatusMessage(), exampleTextPositive);
    }

    @Test
    public void test_setStatusMessage_negative() {
        response.setStatusMessage(exampleTextPositive);
        assertNotEquals(response.getStatusMessage(), exampleTextNegative);
    }

    @Test
    public void test_setStatusCode_positive() {
        response.setStatusCode(exampleTextPositive);
        assertEquals(response.getStatusCode(), exampleTextPositive);
    }

    @Test
    public void test_setStatusCode_negative() {
        response.setStatusCode(exampleTextPositive);
        assertNotEquals(response.getStatusMessage(), exampleTextNegative);
    }

    @Test
    public void test_setTokenId_positive() {
        response.setTokenId(exampleTextPositive);
        assertEquals(response.getTokenId(), exampleTextPositive);
    }

    @Test
    public void test_setTokenId_negative() {
        response.setTokenId(exampleTextPositive);
        assertNotEquals(response.getTokenId(), exampleTextNegative);
    }
}