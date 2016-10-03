package com.midtrans.sdk.corekit.models;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;

/**
 * Created by ziahaqi on 7/19/16.
 */

public class CardCustomerDetailRequestTest {
    CardPaymentDetails details;
    private String bank = "bank";
    private String tokenId = "tokenId";
    private boolean savedTokenId = false;
    private String instalment = "instalment";
    private ArrayList<String> binsArray = new ArrayList<>();
    private String datasample = "sample";

    @Before
    public void setup() {
        details = new CardPaymentDetails(bank, tokenId, savedTokenId);
    }

    @Test
    public void constructorTest() {
        details = new CardPaymentDetails(bank, tokenId, savedTokenId);
        Assert.assertEquals(bank, details.getBank());
        Assert.assertEquals(tokenId, details.getTokenId());
        Assert.assertEquals(savedTokenId, details.isSaveTokenId());
    }

    @Test
    public void constructor3ArgsTest() {
        details = new CardPaymentDetails(bank, tokenId, savedTokenId, instalment, binsArray);
        Assert.assertEquals(bank, details.getBank());
        Assert.assertEquals(tokenId, details.getTokenId());
        Assert.assertEquals(savedTokenId, details.isSaveTokenId());
        Assert.assertEquals(instalment, details.getInstalmentTerm());
        Assert.assertEquals(binsArray, details.getBinsArray());
    }

    @Test
    public void bankTest() {
        details.setBank(datasample);
        Assert.assertEquals(datasample, details.getBank());
    }

    @Test
    public void tokenIdTest() {
        details.setTokenId(datasample);
        Assert.assertEquals(datasample, details.getTokenId());
    }

    @Test
    public void saveTokenIdTest() {
        details.setSaveTokenId(true);
        Assert.assertEquals(true, details.getSaveTokenId());
        Assert.assertEquals(true, details.isSaveTokenId());
    }

    @Test
    public void instalmentTest() {
        details.setInstalmentTerm(datasample);
        Assert.assertEquals(datasample, details.getInstalmentTerm());
    }

    @Test
    public void binTest() {
        details.setBinsArray(binsArray);
        Assert.assertEquals(binsArray, details.getBinsArray());
    }

    @Test
    public void recuringTest() {
        details.setRecurring(true);
        Assert.assertEquals(true, details.isRecurring());
    }

}
