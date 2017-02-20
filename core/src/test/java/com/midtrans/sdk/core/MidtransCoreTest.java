package com.midtrans.sdk.core;

import org.junit.After;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 * Created by rakawm on 2/20/17.
 */
public class MidtransCoreTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private String clientKey = "client-key";
    private MidtransCore midtransCore;

    @After
    public void cleanUp() throws Exception {

    }

    public void setUp() throws Exception {
        midtransCore = new MidtransCore.Builder()
                .setEnvironment(Environment.SANDBOX)
                .setClientKey(clientKey)
                .build();
    }

    @Test
    public void getInstance() throws Exception {
        setUp();
        Assert.assertNotNull(MidtransCore.getInstance());
        Assert.assertEquals(midtransCore, MidtransCore.getInstance());
    }

    @Test
    public void getInstanceOnNullClientKey() throws Exception {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Client key cannot be null or empty. Please pass the client key using setClientKey()");
        midtransCore = new MidtransCore.Builder()
                .setEnvironment(Environment.SANDBOX)
                .build();
    }

    @Test
    public void getInstanceOnNullEnvironment() throws Exception {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("You must set an environment. Please use setEnvironment(Environment.$ENV)");
        midtransCore = new MidtransCore.Builder()
                .setClientKey(clientKey)
                .build();
    }

    @Test
    public void getInstanceOnEmptyClientKey() throws Exception {
        thrown.expect(RuntimeException.class);
        thrown.expectMessage("Client key cannot be null or empty. Please pass the client key using setClientKey()");
        midtransCore = new MidtransCore.Builder()
                .setEnvironment(Environment.SANDBOX)
                .setClientKey("")
                .build();
    }

    @Test
    public void getClientKey() throws Exception {
        setUp();
        Assert.assertEquals(clientKey, midtransCore.getClientKey());
    }

    @Test
    public void checkout() throws Exception {
        setUp();

    }

    @Test
    public void getCardToken() throws Exception {

    }

    @Test
    public void paymentUsingCreditCard() throws Exception {

    }

    @Test
    public void paymentUsingBcaBankTransfer() throws Exception {

    }

    @Test
    public void paymentUsingBcaBankTransfer1() throws Exception {

    }

    @Test
    public void paymentUsingPermataBankTransfer() throws Exception {

    }

    @Test
    public void paymentUsingPermataBankTransfer1() throws Exception {

    }

    @Test
    public void paymentUsingMandiriBankTransfer() throws Exception {

    }

    @Test
    public void paymentUsingMandiriBankTransfer1() throws Exception {

    }

    @Test
    public void paymentUsingOtherBankTransfer() throws Exception {

    }

    @Test
    public void paymentUsingOtherBankTransfer1() throws Exception {

    }

    @Test
    public void paymentUsingBcaKlikpay() throws Exception {

    }

    @Test
    public void paymentUsingKlikBca() throws Exception {

    }

    @Test
    public void paymentUsingEpayBri() throws Exception {

    }

    @Test
    public void paymentUsingCimbClicks() throws Exception {

    }

    @Test
    public void paymentUsingMandiriClickpay() throws Exception {

    }

    @Test
    public void paymentUsingMandiriECash() throws Exception {

    }

    @Test
    public void paymentUsingTelkomselCash() throws Exception {

    }

    @Test
    public void paymentUsingIndosatDompetku() throws Exception {

    }

    @Test
    public void paymentUsingXlTunai() throws Exception {

    }

    @Test
    public void paymentUsingIndomaret() throws Exception {

    }

    @Test
    public void paymentUsingKioson() throws Exception {

    }

    @Test
    public void paymentUsingGiftCard() throws Exception {

    }

    @Test
    public void getBankBins() throws Exception {

    }

    @Test
    public void getEnvironment() throws Exception {
        setUp();
        Assert.assertEquals(Environment.SANDBOX, midtransCore.getEnvironment());
    }

    @Test
    public void setEnvironment() throws Exception {
        setUp();
        midtransCore.setEnvironment(Environment.PRODUCTION);
        Assert.assertEquals(Environment.PRODUCTION, midtransCore.getEnvironment());
    }

}