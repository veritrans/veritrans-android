package com.midtrans.sdk.corekit.models.snap.paymentinfo.merchantdata;

import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.merchantdata.MerchantData;
import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.merchantdata.MerchantPreferences;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class MerchantDataTest {
    private MerchantData merchantData;
    private String exampleTextPositive, exampleTextNegative;
    private ArrayList<String> exampleListPositive, exampleListNegative;
    private MerchantPreferences preferences;

    @Before
    public void setUp() throws Exception {
        this.merchantData = new MerchantData();
        this.preferences = new MerchantPreferences();
        this.exampleTextPositive = "examplePositive";
        this.exampleTextNegative = "exampleNegative";
        this.exampleListNegative = new ArrayList<>();
        this.exampleListPositive = new ArrayList<>();
        this.exampleListPositive.add(exampleTextPositive);
        this.exampleListNegative.add(exampleTextNegative);
    }

    @Test
    public void test_setStatus_positive() throws Exception {
        merchantData.setAcquiringBanks(exampleListPositive);
        assertEquals(merchantData.getAcquiringBanks(), exampleListPositive);
    }

    @Test
    public void test_setStatus_negative() throws Exception {
        merchantData.setAcquiringBanks(exampleListNegative);
        assertNotEquals(merchantData.getAcquiringBanks(), exampleListPositive);
    }

    @Test
    public void test_setClientKey_positive() throws Exception {
        merchantData.setClientKey(exampleTextPositive);
        assertEquals(merchantData.getClientKey(), exampleTextPositive);
    }

    @Test
    public void test_setClientKey_negative() throws Exception {
        merchantData.setClientKey(exampleTextPositive);
        assertNotEquals(merchantData.getClientKey(), exampleTextNegative);
    }

    @Test
    public void test_setMerchantId_positive() throws Exception {
        merchantData.setMerchantId(exampleTextPositive);
        assertEquals(merchantData.getMerchantId(), exampleTextPositive);
    }

    @Test
    public void test_setMerchantId_negative() throws Exception {
        merchantData.setMerchantId(exampleTextPositive);
        assertNotEquals(merchantData.getMerchantId(), exampleTextNegative);
    }

    @Test
    public void test_setEnabledPrinciples_positive() throws Exception {
        merchantData.setEnabledPrinciples(exampleListPositive);
        assertEquals(merchantData.getEnabledPrinciples(), exampleListPositive);
    }

    @Test
    public void test_setEnabledPrinciples_negative() throws Exception {
        merchantData.setEnabledPrinciples(exampleListPositive);
        assertNotEquals(merchantData.getEnabledPrinciples(), exampleListNegative);
    }

    @Test
    public void test_setPointBanks_positive() throws Exception {
        merchantData.setPointBanks(exampleListPositive);
        assertEquals(merchantData.getPointBanks(), exampleListPositive);
    }

    @Test
    public void test_setPointBanks_negative() throws Exception {
        merchantData.setPointBanks(exampleListPositive);
        assertNotEquals(merchantData.getPointBanks(), exampleListNegative);
    }

    @Test
    public void test_setPreferences_positive() throws Exception {
        merchantData.setPreference(preferences);
        assertEquals(merchantData.getPreference(), preferences);
    }

    @Test
    public void test_setPreferences_negative() throws Exception {
        merchantData.setPreference(preferences);
        assertNotEquals(merchantData.getPreference(), merchantData);
    }
}