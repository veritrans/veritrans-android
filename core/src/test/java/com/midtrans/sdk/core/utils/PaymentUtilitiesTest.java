package com.midtrans.sdk.core.utils;

import com.midtrans.sdk.core.Environment;
import com.midtrans.sdk.core.environment.ProductionEnvironment;
import com.midtrans.sdk.core.environment.SandboxEnvironment;
import com.midtrans.sdk.core.environment.StagingEnvironment;
import com.midtrans.sdk.core.models.snap.SnapCustomerDetails;
import com.midtrans.sdk.core.models.snap.bank.bca.BcaBankTransferPaymentRequest;
import com.midtrans.sdk.core.models.snap.bank.mandiri.MandiriBankTransferPaymentRequest;
import com.midtrans.sdk.core.models.snap.bank.other.OtherBankTransferPaymentRequest;
import com.midtrans.sdk.core.models.snap.bank.permata.PermataBankTransferPaymentRequest;
import com.midtrans.sdk.core.models.snap.card.CreditCardPaymentParams;
import com.midtrans.sdk.core.models.snap.card.CreditCardPaymentRequest;
import com.midtrans.sdk.core.models.snap.conveniencestore.indomaret.IndomaretPaymentRequest;
import com.midtrans.sdk.core.models.snap.conveniencestore.kioson.KiosonPaymentRequest;
import com.midtrans.sdk.core.models.snap.ebanking.bcaklikpay.BcaKlikpayPaymentRequest;
import com.midtrans.sdk.core.models.snap.ebanking.cimbclicks.CimbClicksPaymentRequest;
import com.midtrans.sdk.core.models.snap.ebanking.epaybri.EpayBriPaymentRequest;
import com.midtrans.sdk.core.models.snap.ebanking.klikbca.KlikBcaPaymentRequest;
import com.midtrans.sdk.core.models.snap.ebanking.mandiriclickpay.MandiriClickpayPaymentParams;
import com.midtrans.sdk.core.models.snap.ebanking.mandiriclickpay.MandiriClickpayPaymentRequest;
import com.midtrans.sdk.core.models.snap.ebanking.mandiriecash.MandiriECashPaymentRequest;
import com.midtrans.sdk.core.models.snap.ewallet.indosatdompetku.IndosatDompetkuPaymentRequest;
import com.midtrans.sdk.core.models.snap.ewallet.tcash.TelkomselCashPaymentRequest;
import com.midtrans.sdk.core.models.snap.ewallet.xltunai.XlTunaiPaymentRequest;
import com.midtrans.sdk.core.models.snap.gci.GiftCardPaymentRequest;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by rakawm on 2/14/17.
 */
public class PaymentUtilitiesTest {
    @Test
    public void buildCreditCardPaymentRequest() throws Exception {
        CreditCardPaymentParams cardPaymentParams = CreditCardPaymentParams.newBasicPaymentParams("card-token");
        CreditCardPaymentRequest cardPaymentRequest = PaymentUtilities.buildCreditCardPaymentRequest(cardPaymentParams, null);
        Assert.assertNotNull(cardPaymentRequest);
        Assert.assertEquals(CreditCardPaymentRequest.TYPE, cardPaymentRequest.paymentType);
        Assert.assertEquals(cardPaymentParams.getCardToken(), cardPaymentRequest.paymentParams.getCardToken());
    }

    @Test
    public void buildCreditCardPaymentRequestOnInstallmentPaymentParams() throws Exception {
        CreditCardPaymentParams cardPaymentParams = CreditCardPaymentParams.newInstallmentPaymentParams("card-token", "bank-6");
        CreditCardPaymentRequest cardPaymentRequest = PaymentUtilities.buildCreditCardPaymentRequest(cardPaymentParams, null);
        Assert.assertNotNull(cardPaymentRequest);
        Assert.assertEquals(CreditCardPaymentRequest.TYPE, cardPaymentRequest.paymentType);
        Assert.assertEquals(cardPaymentParams.getCardToken(), cardPaymentRequest.paymentParams.getCardToken());
        Assert.assertEquals(cardPaymentParams.getInstallment(), cardPaymentRequest.paymentParams.getInstallment());
    }

    @Test
    public void buildBcaBankTransferPaymentRequest() throws Exception {
        BcaBankTransferPaymentRequest bcaBankTransferPaymentRequest = PaymentUtilities.buildBcaBankTransferPaymentRequest();
        Assert.assertNotNull(bcaBankTransferPaymentRequest);
        Assert.assertEquals(BcaBankTransferPaymentRequest.TYPE, bcaBankTransferPaymentRequest.paymentType);
    }

    @Test
    public void buildBcaBankTransferPaymentRequestWithCustomerDetails() throws Exception {
        SnapCustomerDetails snapCustomerDetails = new SnapCustomerDetails("name", "email", "phone");
        BcaBankTransferPaymentRequest bcaBankTransferPaymentRequest = PaymentUtilities.buildBcaBankTransferPaymentRequest(snapCustomerDetails);
        Assert.assertNotNull(bcaBankTransferPaymentRequest);
        Assert.assertEquals(BcaBankTransferPaymentRequest.TYPE, bcaBankTransferPaymentRequest.paymentType);
        Assert.assertEquals("name", bcaBankTransferPaymentRequest.customerDetails.fullName);
        Assert.assertEquals("email", bcaBankTransferPaymentRequest.customerDetails.email);
        Assert.assertEquals("phone", bcaBankTransferPaymentRequest.customerDetails.phone);
    }

    @Test
    public void buildPermataBankTransferPaymentRequest() throws Exception {
        PermataBankTransferPaymentRequest permataBankTransferPaymentRequest = PaymentUtilities.buildPermataBankTransferPaymentRequest();
        Assert.assertNotNull(permataBankTransferPaymentRequest);
        Assert.assertEquals(PermataBankTransferPaymentRequest.TYPE, permataBankTransferPaymentRequest.paymentType);
    }

    @Test
    public void buildPermataBankTransferPaymentRequestWithCustomerDetails() throws Exception {
        SnapCustomerDetails snapCustomerDetails = new SnapCustomerDetails("name", "email", "phone");
        PermataBankTransferPaymentRequest permataBankTransferPaymentRequest = PaymentUtilities.buildPermataBankTransferPaymentRequest(snapCustomerDetails);
        Assert.assertNotNull(permataBankTransferPaymentRequest);
        Assert.assertEquals(PermataBankTransferPaymentRequest.TYPE, permataBankTransferPaymentRequest.paymentType);
        Assert.assertEquals("name", permataBankTransferPaymentRequest.customerDetails.fullName);
        Assert.assertEquals("email", permataBankTransferPaymentRequest.customerDetails.email);
        Assert.assertEquals("phone", permataBankTransferPaymentRequest.customerDetails.phone);
    }

    @Test
    public void buildMandiriBankTransferPaymentRequest() throws Exception {
        MandiriBankTransferPaymentRequest mandiriBankTransferPaymentRequest = PaymentUtilities.buildMandiriBankTransferPaymentRequest();
        Assert.assertNotNull(mandiriBankTransferPaymentRequest);
        Assert.assertEquals(MandiriBankTransferPaymentRequest.TYPE, mandiriBankTransferPaymentRequest.paymentType);
    }

    @Test
    public void buildMandiriBankTransferPaymentRequestWithCustomerDetails() throws Exception {
        SnapCustomerDetails snapCustomerDetails = new SnapCustomerDetails("name", "email", "phone");
        MandiriBankTransferPaymentRequest mandiriBankTransferPaymentRequest = PaymentUtilities.buildMandiriBankTransferPaymentRequest(snapCustomerDetails);
        Assert.assertNotNull(mandiriBankTransferPaymentRequest);
        Assert.assertEquals(MandiriBankTransferPaymentRequest.TYPE, mandiriBankTransferPaymentRequest.paymentType);
        Assert.assertEquals("name", mandiriBankTransferPaymentRequest.customerDetails.fullName);
        Assert.assertEquals("email", mandiriBankTransferPaymentRequest.customerDetails.email);
        Assert.assertEquals("phone", mandiriBankTransferPaymentRequest.customerDetails.phone);
    }

    @Test
    public void buildOtherBankTransferPaymentRequest() throws Exception {
        OtherBankTransferPaymentRequest otherBankTransferPaymentRequest = PaymentUtilities.buildOtherBankTransferPaymentRequest();
        Assert.assertNotNull(otherBankTransferPaymentRequest);
        Assert.assertEquals(OtherBankTransferPaymentRequest.TYPE, otherBankTransferPaymentRequest.paymentType);
    }

    @Test
    public void buildOtherBankTransferPaymentRequestWithCustomerDetails() throws Exception {
        SnapCustomerDetails snapCustomerDetails = new SnapCustomerDetails("name", "email", "phone");
        OtherBankTransferPaymentRequest otherBankTransferPaymentRequest = PaymentUtilities.buildOtherBankTransferPaymentRequest(snapCustomerDetails);
        Assert.assertNotNull(otherBankTransferPaymentRequest);
        Assert.assertEquals(OtherBankTransferPaymentRequest.TYPE, otherBankTransferPaymentRequest.paymentType);
        Assert.assertEquals("name", otherBankTransferPaymentRequest.customerDetails.fullName);
        Assert.assertEquals("email", otherBankTransferPaymentRequest.customerDetails.email);
        Assert.assertEquals("phone", otherBankTransferPaymentRequest.customerDetails.phone);
    }

    @Test
    public void buildBcaKlikpayPaymentRequest() throws Exception {
        BcaKlikpayPaymentRequest bcaKlikpayPaymentRequest = PaymentUtilities.buildBcaKlikpayPaymentRequest();
        Assert.assertNotNull(bcaKlikpayPaymentRequest);
        Assert.assertEquals(BcaKlikpayPaymentRequest.TYPE, bcaKlikpayPaymentRequest.paymentType);
    }

    @Test
    public void buildKlikBcaPaymentRequest() throws Exception {
        KlikBcaPaymentRequest klikBcaPaymentRequest = PaymentUtilities.buildKlikBcaPaymentRequest("user-id");
        Assert.assertNotNull(klikBcaPaymentRequest);
        Assert.assertEquals(KlikBcaPaymentRequest.TYPE, klikBcaPaymentRequest.paymentType);
        Assert.assertEquals("user-id", klikBcaPaymentRequest.paymentParams.userId);
    }

    @Test
    public void buildEpayBriPaymentRequest() throws Exception {
        EpayBriPaymentRequest epayBriPaymentRequest = PaymentUtilities.buildEpayBriPaymentRequest();
        Assert.assertNotNull(epayBriPaymentRequest);
        Assert.assertEquals(EpayBriPaymentRequest.TYPE, epayBriPaymentRequest.paymentType);
    }

    @Test
    public void buildCimbClicksPaymentRequest() throws Exception {
        CimbClicksPaymentRequest cimbClicksPaymentRequest = PaymentUtilities.buildCimbClicksPaymentRequest();
        Assert.assertNotNull(cimbClicksPaymentRequest);
        Assert.assertEquals(CimbClicksPaymentRequest.TYPE, cimbClicksPaymentRequest.paymentType);
    }

    @Test
    public void buildMandiriClickpayPaymentRequest() throws Exception {
        MandiriClickpayPaymentParams mandiriClickpayPaymentParams = new MandiriClickpayPaymentParams("mandiri-card", "input3", "token-response");
        MandiriClickpayPaymentRequest mandiriClickpayPaymentRequest = PaymentUtilities.buildMandiriClickpayPaymentRequest(mandiriClickpayPaymentParams);
        Assert.assertNotNull(mandiriClickpayPaymentRequest);
        Assert.assertEquals("mandiri-card", mandiriClickpayPaymentRequest.paymentParams.mandiriCardNo);
        Assert.assertEquals("input3", mandiriClickpayPaymentRequest.paymentParams.input3);
        Assert.assertEquals("token-response", mandiriClickpayPaymentRequest.paymentParams.tokenResponse);
    }

    @Test
    public void buildMandiriECashPaymentRequest() throws Exception {
        MandiriECashPaymentRequest mandiriECashPaymentRequest = PaymentUtilities.buildMandiriECashPaymentRequest();
        Assert.assertNotNull(mandiriECashPaymentRequest);
        Assert.assertEquals(MandiriECashPaymentRequest.TYPE, mandiriECashPaymentRequest.paymentType);
    }

    @Test
    public void buildTelkomselCashPaymentRequest() throws Exception {
        TelkomselCashPaymentRequest telkomselCashPaymentRequest = PaymentUtilities.buildTelkomselCashPaymentRequest("customer");
        Assert.assertNotNull(telkomselCashPaymentRequest);
        Assert.assertEquals(TelkomselCashPaymentRequest.TYPE, telkomselCashPaymentRequest.paymentType);
        Assert.assertEquals("customer", telkomselCashPaymentRequest.paymentParams.customer);
    }

    @Test
    public void buildIndosatDompetkuPaymentRequest() throws Exception {
        IndosatDompetkuPaymentRequest indosatDompetkuPaymentRequest = PaymentUtilities.buildIndosatDompetkuPaymentRequest("msisdn");
        Assert.assertNotNull(indosatDompetkuPaymentRequest);
        Assert.assertEquals(IndosatDompetkuPaymentRequest.TYPE, indosatDompetkuPaymentRequest.paymentType);
        Assert.assertEquals("msisdn", indosatDompetkuPaymentRequest.paymentParams.msisdn);
    }

    @Test
    public void buildXlTunaiPaymentRequest() throws Exception {
        XlTunaiPaymentRequest xlTunaiPaymentReques = PaymentUtilities.buildXlTunaiPaymentRequest();
        Assert.assertNotNull(xlTunaiPaymentReques);
        Assert.assertEquals(XlTunaiPaymentRequest.TYPE, xlTunaiPaymentReques.paymentType);
    }

    @Test
    public void buildIndomaretPaymentRequest() throws Exception {
        IndomaretPaymentRequest indomaretPaymentRequest = PaymentUtilities.buildIndomaretPaymentRequest();
        Assert.assertNotNull(indomaretPaymentRequest);
        Assert.assertEquals(IndomaretPaymentRequest.TYPE, indomaretPaymentRequest.paymentType);
    }

    @Test
    public void buildKiosonPaymentRequest() throws Exception {
        KiosonPaymentRequest kiosonPaymentRequest = PaymentUtilities.buildKiosonPaymentRequest();
        Assert.assertNotNull(kiosonPaymentRequest);
        Assert.assertEquals(KiosonPaymentRequest.TYPE, kiosonPaymentRequest.paymentType);
    }

    @Test
    public void buildGiftCardPaymentRequest() throws Exception {
        GiftCardPaymentRequest giftCardPaymentRequest = PaymentUtilities.buildGiftCardPaymentRequest("card-number", "pin");
        Assert.assertNotNull(giftCardPaymentRequest);
        Assert.assertEquals(GiftCardPaymentRequest.TYPE, giftCardPaymentRequest.paymentType);
        Assert.assertEquals("card-number", giftCardPaymentRequest.paymentParams.cardNumber);
        Assert.assertEquals("pin", giftCardPaymentRequest.paymentParams.pin);
    }

    @Test
    public void getPaymentBaseUrl() throws Exception {
        String stagingUrl = PaymentUtilities.getPaymentBaseUrl(Environment.STAGING);
        Assert.assertEquals(StagingEnvironment.PAYMENT_API_URL, stagingUrl);

        String productionUrl = PaymentUtilities.getPaymentBaseUrl(Environment.PRODUCTION);
        Assert.assertEquals(ProductionEnvironment.PAYMENT_API_URL, productionUrl);

        String sandboxUrl = PaymentUtilities.getPaymentBaseUrl(Environment.SANDBOX);
        Assert.assertEquals(SandboxEnvironment.PAYMENT_API_URL, sandboxUrl);
    }

    @Test
    public void getSnapBaseUrl() throws Exception {
        String stagingUrl = PaymentUtilities.getSnapBaseUrl(Environment.STAGING);
        Assert.assertEquals(StagingEnvironment.SNAP_API_URL, stagingUrl);

        String productionUrl = PaymentUtilities.getSnapBaseUrl(Environment.PRODUCTION);
        Assert.assertEquals(ProductionEnvironment.SNAP_API_URL, productionUrl);

        String sandboxUrl = PaymentUtilities.getSnapBaseUrl(Environment.SANDBOX);
        Assert.assertEquals(SandboxEnvironment.SNAP_API_URL, sandboxUrl);
    }

}