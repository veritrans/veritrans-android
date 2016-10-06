package com.midtrans.sdk.corekit.models;

import android.text.TextUtils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.core.classloader.annotations.PrepareForTest;

/**
 * Created by ziahaqi on 7/14/16.
 */
@RunWith(MockitoJUnitRunner.class)
@PrepareForTest({TextUtils.class})
public class TransactionStatusResponseTest {

    private TransactionStatusResponse transactoinStatus;
    private String stringSample = "sample";

    @Before
    public void setup() {
        this.transactoinStatus = new TransactionStatusResponse();
    }

    @Test
    public void statusMessageTest() {
        transactoinStatus.setStatusMessage(stringSample);
        Assert.assertEquals(stringSample, transactoinStatus.getStatusMessage());
    }

    @Test
    public void transactionidTest() {
        transactoinStatus.setTransactionId(stringSample);
        Assert.assertEquals(stringSample, transactoinStatus.getTransactionId());
    }

    @Test
    public void fraudStatusTest() {
        transactoinStatus.setFraudStatus(stringSample);
        Assert.assertEquals(stringSample, transactoinStatus.getFraudStatus());
    }

    @Test
    public void approveCodeTest() {
        transactoinStatus.setApprovaCode(stringSample);
        Assert.assertEquals(stringSample, transactoinStatus.getApprovaCode());
    }

    @Test
    public void transactionStatusTest() {
        transactoinStatus.setTransactionStatus(stringSample);
        Assert.assertEquals(stringSample, transactoinStatus.getTransactionStatus());
    }

    @Test
    public void statusCode() {
        transactoinStatus.setStatusCode(stringSample);
        Assert.assertEquals(stringSample, transactoinStatus.getStatusCode());
    }

    @Test
    public void signatureKey() {
        transactoinStatus.setSignatureKey(stringSample);
        Assert.assertEquals(stringSample, transactoinStatus.getSignatureKey());
    }

    @Test
    public void grossAmountTest() {
        transactoinStatus.setGrossAmount(stringSample);
        Assert.assertEquals(stringSample, transactoinStatus.getGrossAmount());
    }

    @Test
    public void paymentTypeTest() {
        transactoinStatus.setPaymentType(stringSample);
        Assert.assertEquals(stringSample, transactoinStatus.getPaymentType());
    }

    @Test
    public void bankTest() {
        transactoinStatus.setBank(stringSample);
        Assert.assertEquals(stringSample, transactoinStatus.getBank());
    }

    @Test
    public void maskCard() {
        transactoinStatus.setMaskedCard(stringSample);
        Assert.assertEquals(stringSample, transactoinStatus.getMaskedCard());
    }

    @Test
    public void transTime() {
        transactoinStatus.setTransactionTime(stringSample);
        Assert.assertEquals(stringSample, transactoinStatus.getTransactionTime());
    }

    @Test
    public void orderIdTest() {
        transactoinStatus.setOrderId(stringSample);
        Assert.assertEquals(stringSample, transactoinStatus.getOrderId());
    }

}
