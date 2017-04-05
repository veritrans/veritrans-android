package com.midtrans.sdk.core.api.snap;

import com.midtrans.sdk.core.MidtransCoreCallback;
import com.midtrans.sdk.core.api.snap.utils.MockSnapRetrofit;
import com.midtrans.sdk.core.models.snap.CreditCard;
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
import com.midtrans.sdk.core.models.snap.ebanking.klikbca.KlikBcaPaymentParams;
import com.midtrans.sdk.core.models.snap.ebanking.klikbca.KlikBcaPaymentRequest;
import com.midtrans.sdk.core.models.snap.ebanking.klikbca.KlikBcaPaymentResponse;
import com.midtrans.sdk.core.models.snap.ebanking.mandiriclickpay.MandiriClickpayPaymentParams;
import com.midtrans.sdk.core.models.snap.ebanking.mandiriclickpay.MandiriClickpayPaymentRequest;
import com.midtrans.sdk.core.models.snap.ebanking.mandiriclickpay.MandiriClickpayPaymentResponse;
import com.midtrans.sdk.core.models.snap.ebanking.mandiriecash.MandiriECashPaymentRequest;
import com.midtrans.sdk.core.models.snap.ebanking.mandiriecash.MandiriECashPaymentResponse;
import com.midtrans.sdk.core.models.snap.ewallet.indosatdompetku.IndosatDompetkuPaymentParams;
import com.midtrans.sdk.core.models.snap.ewallet.indosatdompetku.IndosatDompetkuPaymentRequest;
import com.midtrans.sdk.core.models.snap.ewallet.indosatdompetku.IndosatDompetkuPaymentResponse;
import com.midtrans.sdk.core.models.snap.ewallet.tcash.TelkomselCashPaymentParams;
import com.midtrans.sdk.core.models.snap.ewallet.tcash.TelkomselCashPaymentRequest;
import com.midtrans.sdk.core.models.snap.ewallet.tcash.TelkomselCashPaymentResponse;
import com.midtrans.sdk.core.models.snap.ewallet.xltunai.XlTunaiPaymentRequest;
import com.midtrans.sdk.core.models.snap.ewallet.xltunai.XlTunaiPaymentResponse;
import com.midtrans.sdk.core.models.snap.gci.GiftCardPaymentParams;
import com.midtrans.sdk.core.models.snap.gci.GiftCardPaymentRequest;
import com.midtrans.sdk.core.models.snap.gci.GiftCardPaymentResponse;
import com.midtrans.sdk.core.models.snap.transaction.SnapEnabledPayment;
import com.midtrans.sdk.core.models.snap.transaction.SnapMerchantData;
import com.midtrans.sdk.core.models.snap.transaction.SnapMerchantPreference;
import com.midtrans.sdk.core.models.snap.transaction.SnapTransaction;
import com.midtrans.sdk.core.models.snap.transaction.SnapTransactionDetails;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import retrofit2.mock.BehaviorDelegate;
import retrofit2.mock.MockRetrofit;

/**
 * Created by rakawm on 2/14/17.
 */
public class SnapApiManagerTest {
    BehaviorDelegate<SnapApi> snapApi;
    SnapApiManager snapApiManager;

    @Before
    public void setUp() throws Exception {
    }

    @Test
    public void getTransactionDetails() throws Exception {
        // Enabled payments
        List<SnapEnabledPayment> enabledPayments = new ArrayList<>();
        enabledPayments.add(new SnapEnabledPayment("credit_card", null));

        List<String> enabledPrinciples = new ArrayList<>();
        enabledPrinciples.add("visa");
        enabledPrinciples.add("mastercard");

        SnapTransaction snapTransaction = new SnapTransaction(
                "token",
                new SnapTransactionDetails("order_id", 1000),
                null,
                null,
                null,
                enabledPayments,
                new CreditCard(false, false, null, null, null, null, null),
                new SnapMerchantData(
                        new SnapMerchantPreference("test-merchant", null, null, null, null, null, null, null),
                        "clientkey",
                        enabledPrinciples
                )
        );
        MockRetrofit mockRetrofit = MockSnapRetrofit.getSnapMockRetrofit();
        snapApi = mockRetrofit.create(SnapApi.class);
        SnapApi api = snapApi.returningResponse(snapTransaction);
        snapApiManager = new SnapApiManager(api);
        snapApiManager.getTransactionDetails("token", new MidtransCoreCallback<SnapTransaction>() {
            @Override
            public void onSuccess(SnapTransaction object) {
                Assert.assertNotNull(object);
                Assert.assertEquals("token", object.token);
                Assert.assertEquals("order_id", object.transactionDetails.orderId);
                Assert.assertEquals(1000, object.transactionDetails.grossAmount);
            }

            @Override
            public void onFailure(SnapTransaction object) {
                // Do nothing
            }

            @Override
            public void onError(Throwable throwable) {
                // Do nothing
            }
        });
    }

    @Test
    public void paymentUsingCreditCard() throws Exception {
        CreditCardPaymentResponse creditCardPaymentResponse = new CreditCardPaymentResponse(
                "200",
                "success",
                "transaction-id",
                "order-id",
                "1000",
                "credit_card",
                "time",
                "capture",
                "accept",
                null,
                null,
                null,
                null,
                "approval",
                "bni");
        MockRetrofit mockRetrofit = MockSnapRetrofit.getSnapMockRetrofit();
        snapApi = mockRetrofit.create(SnapApi.class);
        SnapApi api = snapApi.returningResponse(creditCardPaymentResponse);
        snapApiManager = new SnapApiManager(api);
        snapApiManager.paymentUsingCreditCard("token", new CreditCardPaymentRequest(CreditCardPaymentParams.newBasicPaymentParams("card_token"), null), new MidtransCoreCallback<CreditCardPaymentResponse>() {
            @Override
            public void onSuccess(CreditCardPaymentResponse object) {
                Assert.assertEquals("200", object.statusCode);
                Assert.assertEquals("success", object.statusMessage);
                Assert.assertEquals("capture", object.transactionStatus);
            }

            @Override
            public void onFailure(CreditCardPaymentResponse object) {
                // Do nothing
            }

            @Override
            public void onError(Throwable throwable) {
                // Do nothing
            }
        });
    }

    @Test
    public void paymentUsingBcaBankTransfer() throws Exception {
        BcaBankTransferPaymentResponse bcaBankTransferPaymentResponse = new BcaBankTransferPaymentResponse(
                "201",
                "pending",
                "transaction-id",
                "order-id",
                "1000",
                "bca_va",
                "time",
                "pending",
                "accept",
                null,
                "bca-va",
                "bca-exp",
                null,
                "pdf-url"
        );

        MockRetrofit mockRetrofit = MockSnapRetrofit.getSnapMockRetrofit();
        snapApi = mockRetrofit.create(SnapApi.class);
        SnapApi api = snapApi.returningResponse(bcaBankTransferPaymentResponse);
        snapApiManager = new SnapApiManager(api);
        snapApiManager.paymentUsingBcaBankTransfer("token", new BcaBankTransferPaymentRequest(null), new MidtransCoreCallback<BcaBankTransferPaymentResponse>() {
            @Override
            public void onSuccess(BcaBankTransferPaymentResponse object) {
                Assert.assertEquals("201", object.statusCode);
                Assert.assertEquals("pending", object.statusMessage);
                Assert.assertEquals("bca-va", object.bcaVaNumber);
                Assert.assertEquals("bca-exp", object.bcaExpiration);
            }

            @Override
            public void onFailure(BcaBankTransferPaymentResponse object) {
                // Do nothing
            }

            @Override
            public void onError(Throwable throwable) {
                // Do nothing
            }
        });
    }

    @Test
    public void paymentUsingPermataBankTransfer() throws Exception {
        PermataBankTransferPaymentResponse permataBankTransferPaymentResponse = new PermataBankTransferPaymentResponse(
                "201",
                "pending",
                "transaction-id",
                "order-id",
                "1000",
                "permata_va",
                "time",
                "pending",
                "accept",
                null,
                "pdf-url",
                "permata-va",
                "permata-exp"
        );

        MockRetrofit mockRetrofit = MockSnapRetrofit.getSnapMockRetrofit();
        snapApi = mockRetrofit.create(SnapApi.class);
        SnapApi api = snapApi.returningResponse(permataBankTransferPaymentResponse);
        snapApiManager = new SnapApiManager(api);
        snapApiManager.paymentUsingPermataBankTransfer("token", new PermataBankTransferPaymentRequest(null), new MidtransCoreCallback<PermataBankTransferPaymentResponse>() {
            @Override
            public void onSuccess(PermataBankTransferPaymentResponse object) {
                Assert.assertEquals("201", object.statusCode);
                Assert.assertEquals("pending", object.statusMessage);
                Assert.assertEquals("permata-va", object.permataVaNumber);
                Assert.assertEquals("permata-exp", object.permataExpiration);
            }

            @Override
            public void onFailure(PermataBankTransferPaymentResponse object) {
                // Do nothing
            }

            @Override
            public void onError(Throwable throwable) {
                // Do nothing
            }
        });
    }

    @Test
    public void paymentUsingMandiriBankTransfer() throws Exception {
        MandiriBankTransferPaymentResponse mandiriBankTransferPaymentResponse = new MandiriBankTransferPaymentResponse(
                "201",
                "pending",
                "transaction-id",
                "order-id",
                "1000",
                "echannel",
                "time",
                "pending",
                "accept",
                null,
                "pdf-url",
                "mandiri-bill-key",
                "mandiri-biller",
                "mandiri-bill-exp"
        );

        MockRetrofit mockRetrofit = MockSnapRetrofit.getSnapMockRetrofit();
        snapApi = mockRetrofit.create(SnapApi.class);
        SnapApi api = snapApi.returningResponse(mandiriBankTransferPaymentResponse);
        snapApiManager = new SnapApiManager(api);
        snapApiManager.paymentUsingMandiriBankTransfer("token", new MandiriBankTransferPaymentRequest(null), new MidtransCoreCallback<MandiriBankTransferPaymentResponse>() {
            @Override
            public void onSuccess(MandiriBankTransferPaymentResponse object) {
                Assert.assertEquals("201", object.statusCode);
                Assert.assertEquals("pending", object.statusMessage);
                Assert.assertEquals("mandiri-bill-key", object.billKey);
                Assert.assertEquals("mandiri-biller", object.billerCode);
                Assert.assertEquals("mandiri-bill-exp", object.billpaymentExpiration);
            }

            @Override
            public void onFailure(MandiriBankTransferPaymentResponse object) {
                // Do nothing
            }

            @Override
            public void onError(Throwable throwable) {
                // Do nothing
            }
        });
    }

    @Test
    public void paymentUsingOtherBankTransfer() throws Exception {
        OtherBankTransferPaymentResponse otherBankTransferPaymentResponse = new OtherBankTransferPaymentResponse(
                "201",
                "pending",
                "transaction-id",
                "order-id",
                "1000",
                "other_va",
                "time",
                "pending",
                "accept",
                null,
                "pdf-url",
                "permata-va",
                "permata-exp"
        );

        MockRetrofit mockRetrofit = MockSnapRetrofit.getSnapMockRetrofit();
        snapApi = mockRetrofit.create(SnapApi.class);
        SnapApi api = snapApi.returningResponse(otherBankTransferPaymentResponse);
        snapApiManager = new SnapApiManager(api);
        snapApiManager.paymentUsingOtherBankTransfer("token", new OtherBankTransferPaymentRequest(null), new MidtransCoreCallback<OtherBankTransferPaymentResponse>() {
            @Override
            public void onSuccess(OtherBankTransferPaymentResponse object) {
                Assert.assertEquals("201", object.statusCode);
                Assert.assertEquals("pending", object.statusMessage);
                Assert.assertEquals("permata-va", object.permataVaNumber);
                Assert.assertEquals("permata-exp", object.permataExpiration);
            }

            @Override
            public void onFailure(OtherBankTransferPaymentResponse object) {
                // Do nothing
            }

            @Override
            public void onError(Throwable throwable) {
                // Do nothing
            }
        });
    }

    @Test
    public void paymentUsingBcaKlikpay() throws Exception {
        BcaKlikpayPaymentResponse bcaKlikpayPaymentResponse = new BcaKlikpayPaymentResponse(
                "201",
                "pending",
                "transaction-id",
                "order-id",
                "1000",
                "bca_klikbca",
                "time",
                "pending",
                "accept",
                null,
                "bca-url",
                null
        );

        MockRetrofit mockRetrofit = MockSnapRetrofit.getSnapMockRetrofit();
        snapApi = mockRetrofit.create(SnapApi.class);
        SnapApi api = snapApi.returningResponse(bcaKlikpayPaymentResponse);
        snapApiManager = new SnapApiManager(api);
        snapApiManager.paymentUsingBcaKlikpay("token", new BcaKlikpayPaymentRequest(), new MidtransCoreCallback<BcaKlikpayPaymentResponse>() {
            @Override
            public void onSuccess(BcaKlikpayPaymentResponse object) {
                Assert.assertEquals("201", object.statusCode);
                Assert.assertEquals("pending", object.statusMessage);
                Assert.assertEquals("bca-url", object.redirectUrl);
            }

            @Override
            public void onFailure(BcaKlikpayPaymentResponse object) {
                // Do nothing
            }

            @Override
            public void onError(Throwable throwable) {
                // Do nothing
            }
        });
    }

    @Test
    public void paymentUsingKlikBca() throws Exception {
        KlikBcaPaymentResponse klikBcaPaymentResponse = new KlikBcaPaymentResponse(
                "201",
                "pending",
                "transaction-id",
                "order-id",
                "1000",
                "bca_klik",
                "time",
                "pending",
                "accept",
                null,
                "klik-bca-url",
                "approval",
                "bca-exp"
        );

        MockRetrofit mockRetrofit = MockSnapRetrofit.getSnapMockRetrofit();
        snapApi = mockRetrofit.create(SnapApi.class);
        SnapApi api = snapApi.returningResponse(klikBcaPaymentResponse);
        snapApiManager = new SnapApiManager(api);
        snapApiManager.paymentUsingKlikBca("token", new KlikBcaPaymentRequest(new KlikBcaPaymentParams("user")), new MidtransCoreCallback<KlikBcaPaymentResponse>() {
            @Override
            public void onSuccess(KlikBcaPaymentResponse object) {
                Assert.assertEquals("201", object.statusCode);
                Assert.assertEquals("pending", object.statusMessage);
                Assert.assertEquals("klik-bca-url", object.redirectUrl);
            }

            @Override
            public void onFailure(KlikBcaPaymentResponse object) {
                // Do nothing
            }

            @Override
            public void onError(Throwable throwable) {
                // Do nothing
            }
        });
    }

    @Test
    public void paymentUsingEpayBri() throws Exception {
        EpayBriPaymentResponse epayBriPaymentResponse = new EpayBriPaymentResponse(
                "201",
                "pending",
                "transaction-id",
                "order-id",
                "1000",
                "bri_epay",
                "time",
                "pending",
                "accept",
                null,
                "bri-url"
        );

        MockRetrofit mockRetrofit = MockSnapRetrofit.getSnapMockRetrofit();
        snapApi = mockRetrofit.create(SnapApi.class);
        SnapApi api = snapApi.returningResponse(epayBriPaymentResponse);
        snapApiManager = new SnapApiManager(api);
        snapApiManager.paymentUsingEpayBri("token", new EpayBriPaymentRequest(), new MidtransCoreCallback<EpayBriPaymentResponse>() {
            @Override
            public void onSuccess(EpayBriPaymentResponse object) {
                Assert.assertEquals("201", object.statusCode);
                Assert.assertEquals("pending", object.statusMessage);
                Assert.assertEquals("bri-url", object.redirectUrl);
            }

            @Override
            public void onFailure(EpayBriPaymentResponse object) {
                // Do nothing
            }

            @Override
            public void onError(Throwable throwable) {
                // Do nothing
            }
        });
    }

    @Test
    public void paymentUsingCimbClicks() throws Exception {
        CimbClicksPaymentResponse cimbClicksPaymentResponse = new CimbClicksPaymentResponse(
                "201",
                "pending",
                "transaction-id",
                "order-id",
                "1000",
                "cimb_clicks",
                "time",
                "pending",
                "accept",
                null,
                "cimb-url"
        );

        MockRetrofit mockRetrofit = MockSnapRetrofit.getSnapMockRetrofit();
        snapApi = mockRetrofit.create(SnapApi.class);
        SnapApi api = snapApi.returningResponse(cimbClicksPaymentResponse);
        snapApiManager = new SnapApiManager(api);
        snapApiManager.paymentUsingCimbClicks("token", new CimbClicksPaymentRequest(), new MidtransCoreCallback<CimbClicksPaymentResponse>() {
            @Override
            public void onSuccess(CimbClicksPaymentResponse object) {
                Assert.assertEquals("201", object.statusCode);
                Assert.assertEquals("pending", object.statusMessage);
                Assert.assertEquals("cimb-url", object.redirectUrl);
            }

            @Override
            public void onFailure(CimbClicksPaymentResponse object) {
                // Do nothing
            }

            @Override
            public void onError(Throwable throwable) {
                // Do nothing
            }
        });
    }

    @Test
    public void paymentUsingMandiriClickpay() throws Exception {
        MandiriClickpayPaymentResponse mandiriClickpayPaymentResponse = new MandiriClickpayPaymentResponse(
                "200",
                "success",
                "transaction-id",
                "order-id",
                "1000",
                "mandiri_clickpay",
                "time",
                "success",
                "accept",
                null,
                "approval-code",
                "masked-card"
        );

        MockRetrofit mockRetrofit = MockSnapRetrofit.getSnapMockRetrofit();
        snapApi = mockRetrofit.create(SnapApi.class);
        SnapApi api = snapApi.returningResponse(mandiriClickpayPaymentResponse);
        snapApiManager = new SnapApiManager(api);
        snapApiManager.paymentUsingMandiriClickpay("token", new MandiriClickpayPaymentRequest(new MandiriClickpayPaymentParams("card-no", "input3", "tokenresponse")), new MidtransCoreCallback<MandiriClickpayPaymentResponse>() {
            @Override
            public void onSuccess(MandiriClickpayPaymentResponse object) {
                Assert.assertEquals("200", object.statusCode);
                Assert.assertEquals("success", object.statusMessage);
            }

            @Override
            public void onFailure(MandiriClickpayPaymentResponse object) {
                // Do nothing
            }

            @Override
            public void onError(Throwable throwable) {
                // Do nothing
            }
        });
    }

    @Test
    public void paymentUsingMandiriECash() throws Exception {
        MandiriECashPaymentResponse mandiriECashPaymentResponse = new MandiriECashPaymentResponse(
                "200",
                "success",
                "transaction-id",
                "order-id",
                "1000",
                "mandiri_ecash",
                "time",
                "success",
                "accept",
                null,
                "mandiri-ecash-url"
        );

        MockRetrofit mockRetrofit = MockSnapRetrofit.getSnapMockRetrofit();
        snapApi = mockRetrofit.create(SnapApi.class);
        SnapApi api = snapApi.returningResponse(mandiriECashPaymentResponse);
        snapApiManager = new SnapApiManager(api);
        snapApiManager.paymentUsingMandiriECash("token", new MandiriECashPaymentRequest(), new MidtransCoreCallback<MandiriECashPaymentResponse>() {
            @Override
            public void onSuccess(MandiriECashPaymentResponse object) {
                Assert.assertEquals("200", object.statusCode);
                Assert.assertEquals("success", object.statusMessage);
                Assert.assertEquals("mandiri-ecash-url", object.redirectUrl);
            }

            @Override
            public void onFailure(MandiriECashPaymentResponse object) {
                // Do nothing
            }

            @Override
            public void onError(Throwable throwable) {
                // Do nothing
            }
        });
    }

    @Test
    public void paymentUsingTelkomselCash() throws Exception {
        TelkomselCashPaymentResponse telkomselCashPaymentResponse = new TelkomselCashPaymentResponse(
                "200",
                "success",
                "transaction-id",
                "order-id",
                "1000",
                "t-cash",
                "time",
                "success",
                "accept",
                null
        );

        MockRetrofit mockRetrofit = MockSnapRetrofit.getSnapMockRetrofit();
        snapApi = mockRetrofit.create(SnapApi.class);
        SnapApi api = snapApi.returningResponse(telkomselCashPaymentResponse);
        snapApiManager = new SnapApiManager(api);
        snapApiManager.paymentUsingTelkomselCash("token", new TelkomselCashPaymentRequest(new TelkomselCashPaymentParams("customer")), new MidtransCoreCallback<TelkomselCashPaymentResponse>() {
            @Override
            public void onSuccess(TelkomselCashPaymentResponse object) {
                Assert.assertEquals("200", object.statusCode);
                Assert.assertEquals("success", object.statusMessage);
            }

            @Override
            public void onFailure(TelkomselCashPaymentResponse object) {
                // Do nothing
            }

            @Override
            public void onError(Throwable throwable) {
                // Do nothing
            }
        });
    }

    @Test
    public void paymentUsingIndosatDompetku() throws Exception {
        IndosatDompetkuPaymentResponse indosatDompetkuPaymentResponse = new IndosatDompetkuPaymentResponse(
                "200",
                "success",
                "transaction-id",
                "order-id",
                "1000",
                "indosat_dompetku",
                "time",
                "success",
                "accept",
                null
        );

        MockRetrofit mockRetrofit = MockSnapRetrofit.getSnapMockRetrofit();
        snapApi = mockRetrofit.create(SnapApi.class);
        SnapApi api = snapApi.returningResponse(indosatDompetkuPaymentResponse);
        snapApiManager = new SnapApiManager(api);
        snapApiManager.paymentUsingIndosatDompetku("token", new IndosatDompetkuPaymentRequest(new IndosatDompetkuPaymentParams("msisdn")), new MidtransCoreCallback<IndosatDompetkuPaymentResponse>() {
            @Override
            public void onSuccess(IndosatDompetkuPaymentResponse object) {
                Assert.assertEquals("200", object.statusCode);
                Assert.assertEquals("success", object.statusMessage);
            }

            @Override
            public void onFailure(IndosatDompetkuPaymentResponse object) {
                // Do nothing
            }

            @Override
            public void onError(Throwable throwable) {
                // Do nothing
            }
        });
    }

    @Test
    public void paymentUsingXlTunai() throws Exception {
        XlTunaiPaymentResponse xlTunaiPaymentResponse = new XlTunaiPaymentResponse(
                "201",
                "pending",
                "transaction-id",
                "order-id",
                "1000",
                "xl_tunai",
                "time",
                "pending",
                "accept",
                null,
                "xl-order-id",
                "xl-merchant-id",
                "xl-exp"
        );

        MockRetrofit mockRetrofit = MockSnapRetrofit.getSnapMockRetrofit();
        snapApi = mockRetrofit.create(SnapApi.class);
        SnapApi api = snapApi.returningResponse(xlTunaiPaymentResponse);
        snapApiManager = new SnapApiManager(api);
        snapApiManager.paymentUsingXlTunai("token", new XlTunaiPaymentRequest(), new MidtransCoreCallback<XlTunaiPaymentResponse>() {
            @Override
            public void onSuccess(XlTunaiPaymentResponse object) {
                Assert.assertEquals("201", object.statusCode);
                Assert.assertEquals("pending", object.statusMessage);
                Assert.assertEquals("xl-order-id", object.xlTunaiOrderId);
                Assert.assertEquals("xl-merchant-id", object.xlTunaiMerchantId);
                Assert.assertEquals("xl-exp", object.xlExpiration);
            }

            @Override
            public void onFailure(XlTunaiPaymentResponse object) {
                // Do nothing
            }

            @Override
            public void onError(Throwable throwable) {
                // Do nothing
            }
        });
    }

    @Test
    public void paymentUsingIndomaret() throws Exception {
        IndomaretPaymentResponse indomaretPaymentResponse = new IndomaretPaymentResponse(
                "201",
                "pending",
                "transaction-id",
                "order-id",
                "1000",
                "cstore",
                "time",
                "pending",
                "accept",
                null,
                "pdf-url",
                "indomaret-code",
                "indomaret",
                "indomaret-exp"
        );

        MockRetrofit mockRetrofit = MockSnapRetrofit.getSnapMockRetrofit();
        snapApi = mockRetrofit.create(SnapApi.class);
        SnapApi api = snapApi.returningResponse(indomaretPaymentResponse);
        snapApiManager = new SnapApiManager(api);
        snapApiManager.paymentUsingIndomaret("token", new IndomaretPaymentRequest(), new MidtransCoreCallback<IndomaretPaymentResponse>() {
            @Override
            public void onSuccess(IndomaretPaymentResponse object) {
                Assert.assertEquals("201", object.statusCode);
                Assert.assertEquals("pending", object.statusMessage);
                Assert.assertEquals("indomaret-code", object.paymentCode);
                Assert.assertEquals("indomaret-exp", object.indomaretExpireTime);
            }

            @Override
            public void onFailure(IndomaretPaymentResponse object) {
                // Do nothing
            }

            @Override
            public void onError(Throwable throwable) {
                // Do nothing
            }
        });
    }

    @Test
    public void paymentUsingKioson() throws Exception {
        KiosonPaymentResponse kiosonPaymentResponse = new KiosonPaymentResponse(
                "201",
                "pending",
                "transaction-id",
                "order-id",
                "1000",
                "kioson",
                "time",
                "pending",
                "accept",
                null,
                "pdf-url",
                "kioson-code",
                "kioson",
                "kioson-exp"
        );

        MockRetrofit mockRetrofit = MockSnapRetrofit.getSnapMockRetrofit();
        snapApi = mockRetrofit.create(SnapApi.class);
        SnapApi api = snapApi.returningResponse(kiosonPaymentResponse);
        snapApiManager = new SnapApiManager(api);
        snapApiManager.paymentUsingKioson("token", new KiosonPaymentRequest(), new MidtransCoreCallback<KiosonPaymentResponse>() {
            @Override
            public void onSuccess(KiosonPaymentResponse object) {
                Assert.assertEquals("201", object.statusCode);
                Assert.assertEquals("pending", object.statusMessage);
                Assert.assertEquals("kioson-code", object.paymentCode);
                Assert.assertEquals("kioson-exp", object.kiosonExpireTime);
            }

            @Override
            public void onFailure(KiosonPaymentResponse object) {
                // Do nothing
            }

            @Override
            public void onError(Throwable throwable) {
                // Do nothing
            }
        });
    }

    @Test
    public void paymentUsingGiftCard() throws Exception {
        GiftCardPaymentResponse giftCardPaymentResponse = new GiftCardPaymentResponse(
                "200",
                "success",
                "transaction-id",
                "order-id",
                "1000",
                "gci",
                "time",
                "success",
                "accept",
                null,
                "approval-code"
        );

        MockRetrofit mockRetrofit = MockSnapRetrofit.getSnapMockRetrofit();
        snapApi = mockRetrofit.create(SnapApi.class);
        SnapApi api = snapApi.returningResponse(giftCardPaymentResponse);
        snapApiManager = new SnapApiManager(api);
        snapApiManager.paymentUsingGiftCard("token", new GiftCardPaymentRequest(new GiftCardPaymentParams("card-number", "pin")), new MidtransCoreCallback<GiftCardPaymentResponse>() {
            @Override
            public void onSuccess(GiftCardPaymentResponse object) {
                Assert.assertEquals("200", object.statusCode);
                Assert.assertEquals("success", object.statusMessage);
            }

            @Override
            public void onFailure(GiftCardPaymentResponse object) {
                // Do nothing
            }

            @Override
            public void onError(Throwable throwable) {
                // Do nothing
            }
        });
    }

    @Test
    public void getBankBins() throws Exception {
        List<BankBinsResponse> bankBinsResponses = new ArrayList<>();
        ArrayList<String> bins = new ArrayList<>();
        bins.add("481111");
        bins.add("521111");
        bankBinsResponses.add(new BankBinsResponse("bank", bins));

        MockRetrofit mockRetrofit = MockSnapRetrofit.getSnapMockRetrofit();
        snapApi = mockRetrofit.create(SnapApi.class);
        SnapApi api = snapApi.returningResponse(bankBinsResponses);
        snapApiManager = new SnapApiManager(api);
        snapApiManager.getBankBins(new MidtransCoreCallback<List<BankBinsResponse>>() {
            @Override
            public void onSuccess(List<BankBinsResponse> object) {
                Assert.assertEquals(1, object.size());
                Assert.assertEquals("bank", object.get(0).bank);
            }

            @Override
            public void onFailure(List<BankBinsResponse> object) {
                // Do nothing
            }

            @Override
            public void onError(Throwable throwable) {
                // Do nothing
            }
        });
    }

}