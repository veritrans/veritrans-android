package com.midtrans.sdk.core;

import com.midtrans.sdk.core.api.papi.MidtransApiManager;
import com.midtrans.sdk.core.api.snap.SnapApiManager;
import com.midtrans.sdk.core.models.papi.CardTokenRequest;
import com.midtrans.sdk.core.models.papi.CardTokenResponse;
import com.midtrans.sdk.core.models.snap.bank.bca.BcaBankTransferPaymentRequest;
import com.midtrans.sdk.core.models.snap.bank.bca.BcaBankTransferPaymentResponse;
import com.midtrans.sdk.core.models.snap.bank.mandiri.MandiriBankTransferPaymentRequest;
import com.midtrans.sdk.core.models.snap.bank.mandiri.MandiriBankTransferPaymentResponse;
import com.midtrans.sdk.core.models.snap.bank.other.OtherBankTransferPaymentRequest;
import com.midtrans.sdk.core.models.snap.bank.other.OtherBankTransferPaymentResponse;
import com.midtrans.sdk.core.models.snap.bank.permata.PermataBankTransferPaymentRequest;
import com.midtrans.sdk.core.models.snap.bank.permata.PermataBankTransferPaymentResponse;
import com.midtrans.sdk.core.models.snap.bins.BankBinsResponse;
import com.midtrans.sdk.core.models.snap.card.CreditCardPaymentParams;
import com.midtrans.sdk.core.models.snap.card.CreditCardPaymentRequest;
import com.midtrans.sdk.core.models.snap.card.CreditCardPaymentResponse;
import com.midtrans.sdk.core.models.snap.conveniencestore.indomaret.IndomaretPaymentRequest;
import com.midtrans.sdk.core.models.snap.conveniencestore.indomaret.IndomaretPaymentResponse;
import com.midtrans.sdk.core.models.snap.conveniencestore.kioson.KiosonPaymentRequest;
import com.midtrans.sdk.core.models.snap.conveniencestore.kioson.KiosonPaymentResponse;
import com.midtrans.sdk.core.models.snap.ebanking.bcaklikpay.BcaKlikpayPaymentRequest;
import com.midtrans.sdk.core.models.snap.ebanking.bcaklikpay.BcaKlikpayPaymentResponse;
import com.midtrans.sdk.core.models.snap.ebanking.cimbclicks.CimbClicksPaymentRequest;
import com.midtrans.sdk.core.models.snap.ebanking.cimbclicks.CimbClicksPaymentResponse;
import com.midtrans.sdk.core.models.snap.ebanking.epaybri.EpayBriPaymentRequest;
import com.midtrans.sdk.core.models.snap.ebanking.epaybri.EpayBriPaymentResponse;
import com.midtrans.sdk.core.models.snap.ebanking.klikbca.KlikBcaPaymentRequest;
import com.midtrans.sdk.core.models.snap.ebanking.klikbca.KlikBcaPaymentResponse;
import com.midtrans.sdk.core.models.snap.ebanking.mandiriclickpay.MandiriClickpayPaymentParams;
import com.midtrans.sdk.core.models.snap.ebanking.mandiriclickpay.MandiriClickpayPaymentRequest;
import com.midtrans.sdk.core.models.snap.ebanking.mandiriclickpay.MandiriClickpayPaymentResponse;
import com.midtrans.sdk.core.models.snap.ebanking.mandiriecash.MandiriECashPaymentRequest;
import com.midtrans.sdk.core.models.snap.ebanking.mandiriecash.MandiriECashPaymentResponse;
import com.midtrans.sdk.core.models.snap.ewallet.indosatdompetku.IndosatDompetkuPaymentRequest;
import com.midtrans.sdk.core.models.snap.ewallet.indosatdompetku.IndosatDompetkuPaymentResponse;
import com.midtrans.sdk.core.models.snap.ewallet.tcash.TelkomselCashPaymentRequest;
import com.midtrans.sdk.core.models.snap.ewallet.tcash.TelkomselCashPaymentResponse;
import com.midtrans.sdk.core.models.snap.ewallet.xltunai.XlTunaiPaymentRequest;
import com.midtrans.sdk.core.models.snap.ewallet.xltunai.XlTunaiPaymentResponse;
import com.midtrans.sdk.core.models.snap.gci.GiftCardPaymentRequest;
import com.midtrans.sdk.core.models.snap.gci.GiftCardPaymentResponse;

import org.junit.After;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.Matchers;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.powermock.modules.agent.PowerMockAgent;
import org.powermock.modules.junit4.rule.PowerMockRule;
import org.powermock.reflect.Whitebox;

import java.util.List;

/**
 * Created by rakawm on 2/20/17.
 */
@RunWith(MockitoJUnitRunner.class)
public class MidtransCoreTest {

    static {
        PowerMockAgent.initializeIfNeeded();
    }

    @Rule
    public PowerMockRule rule = new PowerMockRule();

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
    public void getCardToken() throws Exception {
        MidtransCoreCallback<CardTokenResponse> callback = new MidtransCoreCallback<CardTokenResponse>() {
            @Override
            public void onSuccess(CardTokenResponse object) {

            }

            @Override
            public void onFailure(CardTokenResponse object) {

            }

            @Override
            public void onError(Throwable throwable) {

            }
        };
        setUp();
        MidtransApiManager midtransApiManager = Mockito.mock(MidtransApiManager.class);
        Mockito.doNothing().when(midtransApiManager).getCardToken(Matchers.any(CardTokenRequest.class), Matchers.eq(callback));
        Whitebox.setInternalState(midtransCore, "midtransApiManager", midtransApiManager);
        midtransCore.getCardToken(CardTokenRequest.newNormalCard("cardNumber", "cardCvv", "cardExpiryMonth", "cardExpiryYear"), callback);
        Mockito.verify(midtransApiManager).getCardToken(Matchers.any(CardTokenRequest.class), Matchers.eq(callback));
    }

    @Test
    public void paymentUsingCreditCard() throws Exception {
        MidtransCoreCallback<CreditCardPaymentResponse> callback = new MidtransCoreCallback<CreditCardPaymentResponse>() {
            @Override
            public void onSuccess(CreditCardPaymentResponse object) {

            }

            @Override
            public void onFailure(CreditCardPaymentResponse object) {

            }

            @Override
            public void onError(Throwable throwable) {

            }
        };
        setUp();
        SnapApiManager snapApiManager = Mockito.mock(SnapApiManager.class);
        Mockito.doNothing().when(snapApiManager).paymentUsingCreditCard(Matchers.anyString(), Matchers.any(CreditCardPaymentRequest.class), Matchers.eq(callback));
        Whitebox.setInternalState(midtransCore, "snapApiManager", snapApiManager);
        midtransCore.paymentUsingCreditCard("checkoutToken", CreditCardPaymentParams.newBasicPaymentParams("cardToken"), null, callback);
        Mockito.verify(snapApiManager).paymentUsingCreditCard(Matchers.anyString(), Matchers.any(CreditCardPaymentRequest.class), Matchers.eq(callback));

    }

    @Test
    public void paymentUsingBcaBankTransfer() throws Exception {
        MidtransCoreCallback<BcaBankTransferPaymentResponse> callback = new MidtransCoreCallback<BcaBankTransferPaymentResponse>() {
            @Override
            public void onSuccess(BcaBankTransferPaymentResponse object) {

            }

            @Override
            public void onFailure(BcaBankTransferPaymentResponse object) {

            }

            @Override
            public void onError(Throwable throwable) {

            }
        };
        setUp();
        SnapApiManager snapApiManager = Mockito.mock(SnapApiManager.class);
        Mockito.doNothing().when(snapApiManager).paymentUsingBcaBankTransfer(Matchers.anyString(), Matchers.any(BcaBankTransferPaymentRequest.class), Matchers.eq(callback));
        Whitebox.setInternalState(midtransCore, "snapApiManager", snapApiManager);
        midtransCore.paymentUsingBcaBankTransfer("checkoutToken", callback);
        Mockito.verify(snapApiManager).paymentUsingBcaBankTransfer(Matchers.anyString(), Matchers.any(BcaBankTransferPaymentRequest.class), Matchers.eq(callback));
    }

    @Test
    public void paymentUsingBcaBankTransferWithCustomerDetails() throws Exception {
        MidtransCoreCallback<BcaBankTransferPaymentResponse> callback = new MidtransCoreCallback<BcaBankTransferPaymentResponse>() {
            @Override
            public void onSuccess(BcaBankTransferPaymentResponse object) {

            }

            @Override
            public void onFailure(BcaBankTransferPaymentResponse object) {

            }

            @Override
            public void onError(Throwable throwable) {

            }
        };
        setUp();
        SnapApiManager snapApiManager = Mockito.mock(SnapApiManager.class);
        Mockito.doNothing().when(snapApiManager).paymentUsingBcaBankTransfer(Matchers.anyString(), Matchers.any(BcaBankTransferPaymentRequest.class), Matchers.eq(callback));
        Whitebox.setInternalState(midtransCore, "snapApiManager", snapApiManager);
        midtransCore.paymentUsingBcaBankTransfer("checkoutToken", null, callback);
        Mockito.verify(snapApiManager).paymentUsingBcaBankTransfer(Matchers.anyString(), Matchers.any(BcaBankTransferPaymentRequest.class), Matchers.eq(callback));
    }

    @Test
    public void paymentUsingPermataBankTransfer() throws Exception {
        MidtransCoreCallback<PermataBankTransferPaymentResponse> callback = new MidtransCoreCallback<PermataBankTransferPaymentResponse>() {
            @Override
            public void onSuccess(PermataBankTransferPaymentResponse object) {

            }

            @Override
            public void onFailure(PermataBankTransferPaymentResponse object) {

            }

            @Override
            public void onError(Throwable throwable) {

            }
        };
        setUp();
        SnapApiManager snapApiManager = Mockito.mock(SnapApiManager.class);
        Mockito.doNothing().when(snapApiManager).paymentUsingPermataBankTransfer(Matchers.anyString(), Matchers.any(PermataBankTransferPaymentRequest.class), Matchers.eq(callback));
        Whitebox.setInternalState(midtransCore, "snapApiManager", snapApiManager);
        midtransCore.paymentUsingPermataBankTransfer("checkoutToken", callback);
        Mockito.verify(snapApiManager).paymentUsingPermataBankTransfer(Matchers.anyString(), Matchers.any(PermataBankTransferPaymentRequest.class), Matchers.eq(callback));
    }

    @Test
    public void paymentUsingPermataBankTransferWithCustomerDetails() throws Exception {
        MidtransCoreCallback<PermataBankTransferPaymentResponse> callback = new MidtransCoreCallback<PermataBankTransferPaymentResponse>() {
            @Override
            public void onSuccess(PermataBankTransferPaymentResponse object) {

            }

            @Override
            public void onFailure(PermataBankTransferPaymentResponse object) {

            }

            @Override
            public void onError(Throwable throwable) {

            }
        };
        setUp();
        SnapApiManager snapApiManager = Mockito.mock(SnapApiManager.class);
        Mockito.doNothing().when(snapApiManager).paymentUsingPermataBankTransfer(Matchers.anyString(), Matchers.any(PermataBankTransferPaymentRequest.class), Matchers.eq(callback));
        Whitebox.setInternalState(midtransCore, "snapApiManager", snapApiManager);
        midtransCore.paymentUsingPermataBankTransfer("checkoutToken", null, callback);
        Mockito.verify(snapApiManager).paymentUsingPermataBankTransfer(Matchers.anyString(), Matchers.any(PermataBankTransferPaymentRequest.class), Matchers.eq(callback));
    }

    @Test
    public void paymentUsingMandiriBankTransfer() throws Exception {
        MidtransCoreCallback<MandiriBankTransferPaymentResponse> callback = new MidtransCoreCallback<MandiriBankTransferPaymentResponse>() {
            @Override
            public void onSuccess(MandiriBankTransferPaymentResponse object) {

            }

            @Override
            public void onFailure(MandiriBankTransferPaymentResponse object) {

            }

            @Override
            public void onError(Throwable throwable) {

            }
        };
        setUp();
        SnapApiManager snapApiManager = Mockito.mock(SnapApiManager.class);
        Mockito.doNothing().when(snapApiManager).paymentUsingMandiriBankTransfer(Matchers.anyString(), Matchers.any(MandiriBankTransferPaymentRequest.class), Matchers.eq(callback));
        Whitebox.setInternalState(midtransCore, "snapApiManager", snapApiManager);
        midtransCore.paymentUsingMandiriBankTransfer("checkoutToken", callback);
        Mockito.verify(snapApiManager).paymentUsingMandiriBankTransfer(Matchers.anyString(), Matchers.any(MandiriBankTransferPaymentRequest.class), Matchers.eq(callback));
    }

    @Test
    public void paymentUsingMandiriBankTransferWithCustomerDetails() throws Exception {
        MidtransCoreCallback<MandiriBankTransferPaymentResponse> callback = new MidtransCoreCallback<MandiriBankTransferPaymentResponse>() {
            @Override
            public void onSuccess(MandiriBankTransferPaymentResponse object) {

            }

            @Override
            public void onFailure(MandiriBankTransferPaymentResponse object) {

            }

            @Override
            public void onError(Throwable throwable) {

            }
        };
        setUp();
        SnapApiManager snapApiManager = Mockito.mock(SnapApiManager.class);
        Mockito.doNothing().when(snapApiManager).paymentUsingMandiriBankTransfer(Matchers.anyString(), Matchers.any(MandiriBankTransferPaymentRequest.class), Matchers.eq(callback));
        Whitebox.setInternalState(midtransCore, "snapApiManager", snapApiManager);
        midtransCore.paymentUsingMandiriBankTransfer("checkoutToken", null, callback);
        Mockito.verify(snapApiManager).paymentUsingMandiriBankTransfer(Matchers.anyString(), Matchers.any(MandiriBankTransferPaymentRequest.class), Matchers.eq(callback));
    }

    @Test
    public void paymentUsingOtherBankTransfer() throws Exception {
        MidtransCoreCallback<OtherBankTransferPaymentResponse> callback = new MidtransCoreCallback<OtherBankTransferPaymentResponse>() {
            @Override
            public void onSuccess(OtherBankTransferPaymentResponse object) {

            }

            @Override
            public void onFailure(OtherBankTransferPaymentResponse object) {

            }

            @Override
            public void onError(Throwable throwable) {

            }
        };
        setUp();
        SnapApiManager snapApiManager = Mockito.mock(SnapApiManager.class);
        Mockito.doNothing().when(snapApiManager).paymentUsingOtherBankTransfer(Matchers.anyString(), Matchers.any(OtherBankTransferPaymentRequest.class), Matchers.eq(callback));
        Whitebox.setInternalState(midtransCore, "snapApiManager", snapApiManager);
        midtransCore.paymentUsingOtherBankTransfer("checkoutToken", callback);
        Mockito.verify(snapApiManager).paymentUsingOtherBankTransfer(Matchers.anyString(), Matchers.any(OtherBankTransferPaymentRequest.class), Matchers.eq(callback));
    }

    @Test
    public void paymentUsingOtherBankTransferWithCustomerDetails() throws Exception {
        MidtransCoreCallback<OtherBankTransferPaymentResponse> callback = new MidtransCoreCallback<OtherBankTransferPaymentResponse>() {
            @Override
            public void onSuccess(OtherBankTransferPaymentResponse object) {

            }

            @Override
            public void onFailure(OtherBankTransferPaymentResponse object) {

            }

            @Override
            public void onError(Throwable throwable) {

            }
        };
        setUp();
        SnapApiManager snapApiManager = Mockito.mock(SnapApiManager.class);
        Mockito.doNothing().when(snapApiManager).paymentUsingOtherBankTransfer(Matchers.anyString(), Matchers.any(OtherBankTransferPaymentRequest.class), Matchers.eq(callback));
        Whitebox.setInternalState(midtransCore, "snapApiManager", snapApiManager);
        midtransCore.paymentUsingOtherBankTransfer("checkoutToken", null, callback);
        Mockito.verify(snapApiManager).paymentUsingOtherBankTransfer(Matchers.anyString(), Matchers.any(OtherBankTransferPaymentRequest.class), Matchers.eq(callback));
    }

    @Test
    public void paymentUsingBcaKlikpay() throws Exception {
        MidtransCoreCallback<BcaKlikpayPaymentResponse> callback = new MidtransCoreCallback<BcaKlikpayPaymentResponse>() {
            @Override
            public void onSuccess(BcaKlikpayPaymentResponse object) {

            }

            @Override
            public void onFailure(BcaKlikpayPaymentResponse object) {

            }

            @Override
            public void onError(Throwable throwable) {

            }
        };
        setUp();
        SnapApiManager snapApiManager = Mockito.mock(SnapApiManager.class);
        Mockito.doNothing().when(snapApiManager).paymentUsingBcaKlikpay(Matchers.anyString(), Matchers.any(BcaKlikpayPaymentRequest.class), Matchers.eq(callback));
        Whitebox.setInternalState(midtransCore, "snapApiManager", snapApiManager);
        midtransCore.paymentUsingBcaKlikpay("checkoutToken", callback);
        Mockito.verify(snapApiManager).paymentUsingBcaKlikpay(Matchers.anyString(), Matchers.any(BcaKlikpayPaymentRequest.class), Matchers.eq(callback));
    }

    @Test
    public void paymentUsingKlikBca() throws Exception {
        MidtransCoreCallback<KlikBcaPaymentResponse> callback = new MidtransCoreCallback<KlikBcaPaymentResponse>() {
            @Override
            public void onSuccess(KlikBcaPaymentResponse object) {

            }

            @Override
            public void onFailure(KlikBcaPaymentResponse object) {

            }

            @Override
            public void onError(Throwable throwable) {

            }
        };
        setUp();
        SnapApiManager snapApiManager = Mockito.mock(SnapApiManager.class);
        Mockito.doNothing().when(snapApiManager).paymentUsingKlikBca(Matchers.anyString(), Matchers.any(KlikBcaPaymentRequest.class), Matchers.eq(callback));
        Whitebox.setInternalState(midtransCore, "snapApiManager", snapApiManager);
        midtransCore.paymentUsingKlikBca("checkoutToken", "user", callback);
        Mockito.verify(snapApiManager).paymentUsingKlikBca(Matchers.anyString(), Matchers.any(KlikBcaPaymentRequest.class), Matchers.eq(callback));
    }

    @Test
    public void paymentUsingEpayBri() throws Exception {
        MidtransCoreCallback<EpayBriPaymentResponse> callback = new MidtransCoreCallback<EpayBriPaymentResponse>() {
            @Override
            public void onSuccess(EpayBriPaymentResponse object) {

            }

            @Override
            public void onFailure(EpayBriPaymentResponse object) {

            }

            @Override
            public void onError(Throwable throwable) {

            }
        };
        setUp();
        SnapApiManager snapApiManager = Mockito.mock(SnapApiManager.class);
        Mockito.doNothing().when(snapApiManager).paymentUsingEpayBri(Matchers.anyString(), Matchers.any(EpayBriPaymentRequest.class), Matchers.eq(callback));
        Whitebox.setInternalState(midtransCore, "snapApiManager", snapApiManager);
        midtransCore.paymentUsingEpayBri("checkoutToken", callback);
        Mockito.verify(snapApiManager).paymentUsingEpayBri(Matchers.anyString(), Matchers.any(EpayBriPaymentRequest.class), Matchers.eq(callback));
    }

    @Test
    public void paymentUsingCimbClicks() throws Exception {
        MidtransCoreCallback<CimbClicksPaymentResponse> callback = new MidtransCoreCallback<CimbClicksPaymentResponse>() {
            @Override
            public void onSuccess(CimbClicksPaymentResponse object) {

            }

            @Override
            public void onFailure(CimbClicksPaymentResponse object) {

            }

            @Override
            public void onError(Throwable throwable) {

            }
        };
        setUp();
        SnapApiManager snapApiManager = Mockito.mock(SnapApiManager.class);
        Mockito.doNothing().when(snapApiManager).paymentUsingCimbClicks(Matchers.anyString(), Matchers.any(CimbClicksPaymentRequest.class), Matchers.eq(callback));
        Whitebox.setInternalState(midtransCore, "snapApiManager", snapApiManager);
        midtransCore.paymentUsingCimbClicks("checkoutToken", callback);
        Mockito.verify(snapApiManager).paymentUsingCimbClicks(Matchers.anyString(), Matchers.any(CimbClicksPaymentRequest.class), Matchers.eq(callback));
    }

    @Test
    public void paymentUsingMandiriClickpay() throws Exception {
        MidtransCoreCallback<MandiriClickpayPaymentResponse> callback = new MidtransCoreCallback<MandiriClickpayPaymentResponse>() {
            @Override
            public void onSuccess(MandiriClickpayPaymentResponse object) {

            }

            @Override
            public void onFailure(MandiriClickpayPaymentResponse object) {

            }

            @Override
            public void onError(Throwable throwable) {

            }
        };
        setUp();
        SnapApiManager snapApiManager = Mockito.mock(SnapApiManager.class);
        Mockito.doNothing().when(snapApiManager).paymentUsingMandiriClickpay(Matchers.anyString(), Matchers.any(MandiriClickpayPaymentRequest.class), Matchers.eq(callback));
        Whitebox.setInternalState(midtransCore, "snapApiManager", snapApiManager);
        midtransCore.paymentUsingMandiriClickpay("checkoutToken", new MandiriClickpayPaymentParams("card", "input3", "token"), callback);
        Mockito.verify(snapApiManager).paymentUsingMandiriClickpay(Matchers.anyString(), Matchers.any(MandiriClickpayPaymentRequest.class), Matchers.eq(callback));
    }

    @Test
    public void paymentUsingMandiriECash() throws Exception {
        MidtransCoreCallback<MandiriECashPaymentResponse> callback = new MidtransCoreCallback<MandiriECashPaymentResponse>() {
            @Override
            public void onSuccess(MandiriECashPaymentResponse object) {

            }

            @Override
            public void onFailure(MandiriECashPaymentResponse object) {

            }

            @Override
            public void onError(Throwable throwable) {

            }
        };
        setUp();
        SnapApiManager snapApiManager = Mockito.mock(SnapApiManager.class);
        Mockito.doNothing().when(snapApiManager).paymentUsingMandiriECash(Matchers.anyString(), Matchers.any(MandiriECashPaymentRequest.class), Matchers.eq(callback));
        Whitebox.setInternalState(midtransCore, "snapApiManager", snapApiManager);
        midtransCore.paymentUsingMandiriECash("checkoutToken", callback);
        Mockito.verify(snapApiManager).paymentUsingMandiriECash(Matchers.anyString(), Matchers.any(MandiriECashPaymentRequest.class), Matchers.eq(callback));
    }

    @Test
    public void paymentUsingTelkomselCash() throws Exception {
        MidtransCoreCallback<TelkomselCashPaymentResponse> callback = new MidtransCoreCallback<TelkomselCashPaymentResponse>() {
            @Override
            public void onSuccess(TelkomselCashPaymentResponse object) {

            }

            @Override
            public void onFailure(TelkomselCashPaymentResponse object) {

            }

            @Override
            public void onError(Throwable throwable) {

            }
        };
        setUp();
        SnapApiManager snapApiManager = Mockito.mock(SnapApiManager.class);
        Mockito.doNothing().when(snapApiManager).paymentUsingTelkomselCash(Matchers.anyString(), Matchers.any(TelkomselCashPaymentRequest.class), Matchers.eq(callback));
        Whitebox.setInternalState(midtransCore, "snapApiManager", snapApiManager);
        midtransCore.paymentUsingTelkomselCash("checkoutToken", "customer", callback);
        Mockito.verify(snapApiManager).paymentUsingTelkomselCash(Matchers.anyString(), Matchers.any(TelkomselCashPaymentRequest.class), Matchers.eq(callback));
    }

    @Test
    public void paymentUsingIndosatDompetku() throws Exception {
        MidtransCoreCallback<IndosatDompetkuPaymentResponse> callback = new MidtransCoreCallback<IndosatDompetkuPaymentResponse>() {
            @Override
            public void onSuccess(IndosatDompetkuPaymentResponse object) {

            }

            @Override
            public void onFailure(IndosatDompetkuPaymentResponse object) {

            }

            @Override
            public void onError(Throwable throwable) {

            }
        };
        setUp();
        SnapApiManager snapApiManager = Mockito.mock(SnapApiManager.class);
        Mockito.doNothing().when(snapApiManager).paymentUsingIndosatDompetku(Matchers.anyString(), Matchers.any(IndosatDompetkuPaymentRequest.class), Matchers.eq(callback));
        Whitebox.setInternalState(midtransCore, "snapApiManager", snapApiManager);
        midtransCore.paymentUsingIndosatDompetku("checkoutToken", "msisdn", callback);
        Mockito.verify(snapApiManager).paymentUsingIndosatDompetku(Matchers.anyString(), Matchers.any(IndosatDompetkuPaymentRequest.class), Matchers.eq(callback));
    }

    @Test
    public void paymentUsingXlTunai() throws Exception {
        MidtransCoreCallback<XlTunaiPaymentResponse> callback = new MidtransCoreCallback<XlTunaiPaymentResponse>() {
            @Override
            public void onSuccess(XlTunaiPaymentResponse object) {

            }

            @Override
            public void onFailure(XlTunaiPaymentResponse object) {

            }

            @Override
            public void onError(Throwable throwable) {

            }
        };
        setUp();
        SnapApiManager snapApiManager = Mockito.mock(SnapApiManager.class);
        Mockito.doNothing().when(snapApiManager).paymentUsingXlTunai(Matchers.anyString(), Matchers.any(XlTunaiPaymentRequest.class), Matchers.eq(callback));
        Whitebox.setInternalState(midtransCore, "snapApiManager", snapApiManager);
        midtransCore.paymentUsingXlTunai("checkoutToken", callback);
        Mockito.verify(snapApiManager).paymentUsingXlTunai(Matchers.anyString(), Matchers.any(XlTunaiPaymentRequest.class), Matchers.eq(callback));
    }

    @Test
    public void paymentUsingIndomaret() throws Exception {
        MidtransCoreCallback<IndomaretPaymentResponse> callback = new MidtransCoreCallback<IndomaretPaymentResponse>() {
            @Override
            public void onSuccess(IndomaretPaymentResponse object) {

            }

            @Override
            public void onFailure(IndomaretPaymentResponse object) {

            }

            @Override
            public void onError(Throwable throwable) {

            }
        };
        setUp();
        SnapApiManager snapApiManager = Mockito.mock(SnapApiManager.class);
        Mockito.doNothing().when(snapApiManager).paymentUsingIndomaret(Matchers.anyString(), Matchers.any(IndomaretPaymentRequest.class), Matchers.eq(callback));
        Whitebox.setInternalState(midtransCore, "snapApiManager", snapApiManager);
        midtransCore.paymentUsingIndomaret("checkoutToken", callback);
        Mockito.verify(snapApiManager).paymentUsingIndomaret(Matchers.anyString(), Matchers.any(IndomaretPaymentRequest.class), Matchers.eq(callback));
    }

    @Test
    public void paymentUsingKioson() throws Exception {
        MidtransCoreCallback<KiosonPaymentResponse> callback = new MidtransCoreCallback<KiosonPaymentResponse>() {
            @Override
            public void onSuccess(KiosonPaymentResponse object) {

            }

            @Override
            public void onFailure(KiosonPaymentResponse object) {

            }

            @Override
            public void onError(Throwable throwable) {

            }
        };
        setUp();
        SnapApiManager snapApiManager = Mockito.mock(SnapApiManager.class);
        Mockito.doNothing().when(snapApiManager).paymentUsingKioson(Matchers.anyString(), Matchers.any(KiosonPaymentRequest.class), Matchers.eq(callback));
        Whitebox.setInternalState(midtransCore, "snapApiManager", snapApiManager);
        midtransCore.paymentUsingKioson("checkoutToken", callback);
        Mockito.verify(snapApiManager).paymentUsingKioson(Matchers.anyString(), Matchers.any(KiosonPaymentRequest.class), Matchers.eq(callback));
    }

    @Test
    public void paymentUsingGiftCard() throws Exception {
        MidtransCoreCallback<GiftCardPaymentResponse> callback = new MidtransCoreCallback<GiftCardPaymentResponse>() {
            @Override
            public void onSuccess(GiftCardPaymentResponse object) {

            }

            @Override
            public void onFailure(GiftCardPaymentResponse object) {

            }

            @Override
            public void onError(Throwable throwable) {

            }
        };
        setUp();
        SnapApiManager snapApiManager = Mockito.mock(SnapApiManager.class);
        Mockito.doNothing().when(snapApiManager).paymentUsingGiftCard(Matchers.anyString(), Matchers.any(GiftCardPaymentRequest.class), Matchers.eq(callback));
        Whitebox.setInternalState(midtransCore, "snapApiManager", snapApiManager);
        midtransCore.paymentUsingGiftCard("checkoutToken", "card_number", "card_pin", callback);
        Mockito.verify(snapApiManager).paymentUsingGiftCard(Matchers.anyString(), Matchers.any(GiftCardPaymentRequest.class), Matchers.eq(callback));
    }

    @Test
    public void getBankBins() throws Exception {
        MidtransCoreCallback<List<BankBinsResponse>> callback = new MidtransCoreCallback<List<BankBinsResponse>>() {
            @Override
            public void onSuccess(List<BankBinsResponse> object) {

            }

            @Override
            public void onFailure(List<BankBinsResponse> object) {

            }

            @Override
            public void onError(Throwable throwable) {

            }
        };
        setUp();
        SnapApiManager snapApiManager = Mockito.mock(SnapApiManager.class);
        Mockito.doNothing().when(snapApiManager).getBankBins(callback);
        Whitebox.setInternalState(midtransCore, "snapApiManager", snapApiManager);
        midtransCore.getBankBins(callback);
        Mockito.verify(snapApiManager).getBankBins(Matchers.eq(callback));
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