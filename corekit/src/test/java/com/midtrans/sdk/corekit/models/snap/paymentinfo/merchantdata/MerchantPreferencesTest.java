package com.midtrans.sdk.corekit.models.snap.paymentinfo.merchantdata;

import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.merchantdata.MerchantPreferences;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class MerchantPreferencesTest {
    private MerchantPreferences merchantPreferences;
    private String exampleTextPositive, exampleTextNegative;

    @Before
    public void test_setup() throws Exception {
        this.merchantPreferences = new MerchantPreferences();
        this.exampleTextPositive = "examplePositive";
        this.exampleTextNegative = "exampleNegative";
    }

    @Test
    public void test_setColorScheme_positive() throws Exception {
        merchantPreferences.setColorScheme(exampleTextPositive);
        assertEquals(merchantPreferences.getColorScheme(), exampleTextPositive);
    }

    @Test
    public void test_setColorScheme_negative() throws Exception {
        merchantPreferences.setColorScheme(exampleTextPositive);
        assertNotEquals(merchantPreferences.getColorScheme(), exampleTextNegative);
    }

    @Test
    public void test_setColorSchemeUrl_positive() throws Exception {
        merchantPreferences.setColorSchemeUrl(exampleTextPositive);
        assertEquals(merchantPreferences.getColorSchemeUrl(), exampleTextPositive);
    }

    @Test
    public void test_setColorSchemeUrl_negative() throws Exception {
        merchantPreferences.setColorSchemeUrl(exampleTextPositive);
        assertNotEquals(merchantPreferences.getColorSchemeUrl(), exampleTextNegative);
    }

    @Test
    public void test_setDisplayName_positive() throws Exception {
        merchantPreferences.setDisplayName(exampleTextPositive);
        assertEquals(merchantPreferences.getDisplayName(), exampleTextPositive);
    }

    @Test
    public void test_setDisplayName_negative() throws Exception {
        merchantPreferences.setDisplayName(exampleTextPositive);
        assertNotEquals(merchantPreferences.getDisplayName(), exampleTextNegative);
    }

    @Test
    public void test_setErrorUrl_positive() throws Exception {
        merchantPreferences.setErrorUrl(exampleTextPositive);
        assertEquals(merchantPreferences.getErrorUrl(), exampleTextPositive);
    }

    @Test
    public void test_setErrorUrl_negative() throws Exception {
        merchantPreferences.setErrorUrl(exampleTextPositive);
        assertNotEquals(merchantPreferences.getErrorUrl(), exampleTextNegative);
    }

    @Test
    public void test_setFinishUrl_positive() throws Exception {
        merchantPreferences.setFinishUrl(exampleTextPositive);
        assertEquals(merchantPreferences.getFinishUrl(), exampleTextPositive);
    }

    @Test
    public void test_setFinishUrl_negative() throws Exception {
        merchantPreferences.setFinishUrl(exampleTextPositive);
        assertNotEquals(merchantPreferences.getFinishUrl(), exampleTextNegative);
    }

    @Test
    public void test_setLocale_positive() throws Exception {
        merchantPreferences.setLocale(exampleTextPositive);
        assertEquals(merchantPreferences.getLocale(), exampleTextPositive);
    }

    @Test
    public void test_setLocale_negative() throws Exception {
        merchantPreferences.setLocale(exampleTextPositive);
        assertNotEquals(merchantPreferences.getLocale(), exampleTextNegative);
    }

    @Test
    public void test_setLogoutUrl_positive() throws Exception {
        merchantPreferences.setLogoUrl(exampleTextPositive);
        assertEquals(merchantPreferences.getLogoUrl(), exampleTextPositive);
    }

    @Test
    public void test_setLogoUrl_negative() throws Exception {
        merchantPreferences.setLogoUrl(exampleTextPositive);
        assertNotEquals(merchantPreferences.getLogoUrl(), exampleTextNegative);
    }

    @Test
    public void test_setPendingUrl_positive() throws Exception {
        merchantPreferences.setPendingUrl(exampleTextPositive);
        assertEquals(merchantPreferences.getPendingUrl(), exampleTextPositive);
    }

    @Test
    public void test_setPendingUrl_negative() throws Exception {
        merchantPreferences.setPendingUrl(exampleTextPositive);
        assertNotEquals(merchantPreferences.getPendingUrl(), exampleTextNegative);
    }

    @Test
    public void test_setOtherVa_positive() throws Exception {
        merchantPreferences.setOtherVaProcessor(exampleTextPositive);
        assertEquals(merchantPreferences.getOtherVaProcessor(), exampleTextPositive);
    }

    @Test
    public void test_setOtherVa_negative() throws Exception {
        merchantPreferences.setOtherVaProcessor(exampleTextPositive);
        assertNotEquals(merchantPreferences.getOtherVaProcessor(), exampleTextNegative);
    }
}