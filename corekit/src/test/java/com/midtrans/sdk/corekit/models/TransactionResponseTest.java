package com.midtrans.sdk.corekit.models;

import android.text.TextUtils;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.List;

/**
 * Created by ziahaqi on 7/14/16.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({TextUtils.class})
public class TransactionResponseTest {
    TransactionResponse transactionResponse;
    private String grossAmount = "20.0";
    private String statusCode = "200";
    private String statusMessage = "message";
    private String transId = "transId";
    private String orderId = "orderId";
    private String paymentType = "paymentype";
    private String transTime = "transtime";
    private String transStatus = "transstatus";
    @Mock
    private List<BCAVANumber> accountNumberMock;
    private String paymentCodeIndomaretMock = "codeindomaret";
    private String redirectUrl = "redirecturl";
    private String companyCode = "companycode";
    private String paymentcode = "paymentcode";
    private String vanumber = "vanumber";

    @Before
    public void setup() {

        PowerMockito.mockStatic(TextUtils.class);
        Mockito.when(TextUtils.equals(Mockito.any(CharSequence.class), Mockito.any(CharSequence.class))).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocation) throws Throwable {
                CharSequence a = (CharSequence) invocation.getArguments()[0];
                CharSequence b = (CharSequence) invocation.getArguments()[1];
                if (a == b) return true;
                int length;
                if (a != null && b != null && (length = a.length()) == b.length()) {
                    if (a instanceof String && b instanceof String) {
                        return a.equals(b);
                    } else {
                        for (int i = 0; i < length; i++) {
                            if (a.charAt(i) != b.charAt(i)) return false;
                        }
                        return true;
                    }
                }
                return false;
            }
        });
        transactionResponse = new TransactionResponse(statusCode, statusMessage,
                transId, orderId, grossAmount, paymentType, transTime, transStatus);
    }

    @Test
    public void constructorTest() {

        Assert.assertEquals(statusCode, transactionResponse.getStatusCode());
        Assert.assertEquals(statusMessage, transactionResponse.getStatusMessage());
        Assert.assertEquals(transId, transactionResponse.getTransactionId());
        Assert.assertEquals(orderId, transactionResponse.getOrderId());
        Assert.assertEquals(grossAmount, transactionResponse.getGrossAmount());
        Assert.assertEquals(paymentType, transactionResponse.getPaymentType());
        Assert.assertEquals(transTime, transactionResponse.getTransactionTime());
        Assert.assertEquals(transStatus, transactionResponse.getTransactionStatus());
    }


    @Test
    public void accountNumberTest() {
        transactionResponse.setAccountNumbers(accountNumberMock);
        Assert.assertEquals(accountNumberMock, transactionResponse.getAccountNumbers());
    }

    @Test
    public void paymentCodeIndomaretTest() {
        transactionResponse.setPaymentCodeResponse(paymentCodeIndomaretMock);
        Assert.assertEquals(paymentCodeIndomaretMock, transactionResponse.getPaymentCodeResponse());
    }

    @Test
    public void directUrlTest() {
        transactionResponse.setRedirectUrl(redirectUrl);
        Assert.assertEquals(redirectUrl, transactionResponse.getRedirectUrl());
    }

    @Test
    public void companyCodeTest() {
        transactionResponse.setCompanyCode(companyCode);
        Assert.assertEquals(companyCode, transactionResponse.getCompanyCode());
    }

    @Test
    public void paymentCodeTest() {
        transactionResponse.setPaymentCode(paymentcode);
        Assert.assertEquals(paymentcode, transactionResponse.getPaymentCode());
    }

    @Test
    public void eciTest() {
        transactionResponse.setEci("eci");
        Assert.assertEquals("eci", transactionResponse.getEci());
    }

    @Test
    public void bankTest() {
        transactionResponse.setBank("bank");
        Assert.assertEquals("bank", transactionResponse.getBank());
    }

    @Test
    public void scuretokenTest() {
        transactionResponse.setSecureToken(true);
        Assert.assertEquals(true, transactionResponse.isSecureToken());
    }

    @Test
    public void approvalCode() {
        transactionResponse.setApprovalCode(vanumber);
        Assert.assertEquals(vanumber, transactionResponse.getApprovalCode());
    }

    @Test
    public void tokenExpiredAt() {
        transactionResponse.setSavedTokenIdExpiredAt("token");
        Assert.assertEquals("token", transactionResponse.getSavedTokenIdExpiredAt());
    }

    @Test
    public void fraudStatusTest() {
        transactionResponse.setFraudStatus("fraud");
        Assert.assertEquals("fraud", transactionResponse.getFraudStatus());
    }

    @Test
    public void transactionStatus() {
        transactionResponse.setTransactionStatus("res");
        Assert.assertEquals("res", transactionResponse.getTransactionStatus());

        transactionResponse.setTransactionStatus("");
        Assert.assertEquals("", transactionResponse.getTransactionStatus());
    }

    @Test
    public void statusCodeTest() {
        transactionResponse.setStatusCode("statusCode");
        Assert.assertEquals("statusCode", transactionResponse.getStatusCode());

        transactionResponse.setStatusCode("");
        Assert.assertEquals("", transactionResponse.getStatusCode());
    }

    @Test
    public void statusMessageTest() {
        transactionResponse.setStatusMessage("message");
        Assert.assertEquals("message", transactionResponse.getStatusMessage());

        transactionResponse.setStatusMessage("");
        Assert.assertEquals("", transactionResponse.getStatusMessage());
    }

    @Test
    public void transactionIdTest() {
        transactionResponse.setTransactionId("com");
        Assert.assertEquals("com", transactionResponse.getTransactionId());
    }

    @Test
    public void savedToken() {
        transactionResponse.setSavedTokenId("token");
        Assert.assertEquals("token", transactionResponse.getSavedTokenId());

        transactionResponse.setSavedTokenId("");
        Assert.assertEquals("", transactionResponse.getSavedTokenId());
    }

    @Test
    public void maskcard() {
        transactionResponse.setMaskedCard("mask");
        Assert.assertEquals("mask", transactionResponse.getMaskedCard());
    }


    @Test
    public void orderIdTest() {
        transactionResponse.setOrderId("com");
        Assert.assertEquals("com", transactionResponse.getOrderId());

    }

    @Test
    public void grossAmountTest() {
        transactionResponse.setGrossAmount(grossAmount);
        Assert.assertEquals(grossAmount, transactionResponse.getGrossAmount());

    }

    @Test
    public void setPaymentTypeTest() {
        transactionResponse.setPaymentType(paymentType);
        Assert.assertEquals(paymentType, transactionResponse.getPaymentType());

    }

    @Test
    public void transactionTimeTest() {
        transactionResponse.setTransactionTime("time");
        Assert.assertEquals("time", transactionResponse.getTransactionTime());
    }
}
