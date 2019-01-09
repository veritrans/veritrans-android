package com.midtrans.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.base.enums.Environment;
import com.midtrans.sdk.corekit.base.enums.ExpiryModelUnit;
import com.midtrans.sdk.corekit.base.model.BankType;
import com.midtrans.sdk.corekit.base.model.Currency;
import com.midtrans.sdk.corekit.MidtransSdk;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.CheckoutTransaction;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.optional.BillInfoModel;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.optional.ExpiryModel;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.optional.ItemDetails;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.optional.customer.BillingAddress;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.optional.customer.CustomerDetails;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.optional.customer.ShippingAddress;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.specific.banktransfer.BcaBankFreeText;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.specific.banktransfer.BcaBankFreeTextLanguage;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.specific.banktransfer.BcaBankTransferRequestModel;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.specific.creditcard.CreditCard;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.response.CheckoutWithTransactionResponse;
import com.midtrans.sdk.corekit.core.api.midtrans.model.cardregistration.CardRegistrationResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.CustomerDetailPayRequest;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.creditcard.CreditCardPaymentParams;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.BasePaymentResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.PaymentInfoResponse;
import com.midtrans.sdk.corekit.core.payment.CreditCardCharge;
import com.midtrans.sdk.corekit.utilities.Logger;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    String snapToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MidtransSdk.getInstance().checkoutWithTransaction(new MidtransCallback<CheckoutWithTransactionResponse>() {
            @Override
            public void onSuccess(CheckoutWithTransactionResponse data) {

            }

            @Override
            public void onFailed(Throwable throwable) {

            }
        });

        MidtransSdk
                .builder(this,
                        BuildConfig.CLIENT_KEY,
                        BuildConfig.BASE_URL)
                .setLogEnabled(true)
                .setEnvironment(Environment.SANDBOX)
                .setApiRequestTimeOut(60)
                .build();

        CheckoutTransaction trxRequest = CheckoutTransaction
                .builder("sample_sdk_test_core_" + System.currentTimeMillis(), 20000.0)
                .setCurrency(Currency.IDR)
                .setGopayCallbackDeepLink("demo://midtrans")
                .setCreditCard(CreditCard
                        .normalClickBuilder(false, CreditCard.AUTHENTICATION_TYPE_NONE)
                        //.setTokenId("")
                        .setSaveCard(true)
                        .setBank(BankType.BNI)
                        //.setInstallment(false, new HashMap<String, ArrayList<Integer>>())
                        //.setBlackListBins(new ArrayList<String>())
                        //.setWhiteListBins(new ArrayList<String>())
                        //.setSavedTokens(new ArrayList<SavedToken>())
                        //.setChannel(CreditCard.MIGS)
                        .build())
                .setCustomerDetails(new CustomerDetails("FirstName",
                        "LastName",
                        "email@mail.com",
                        "6281234567890",
                        new ShippingAddress("Firstname",
                                "LastName",
                                "mail@mail.com",
                                "Bogor",
                                "16710",
                                "62877",
                                "idn"),
                        new BillingAddress("Firstname",
                                "LastName",
                                "mail@mail.com",
                                "Bogor",
                                "16710",
                                "62877",
                                "idn")))
                .setBillInfoModel(new BillInfoModel("1", "2"))
                .setEnabledPayments(new ArrayList<String>())
                .setExpiry(new ExpiryModel("", ExpiryModelUnit.EXPIRY_UNIT_DAY, 1))
                .setItemDetails(new ArrayList<ItemDetails>())
                .setBcaVa(new BcaBankTransferRequestModel("",
                        new BcaBankFreeText(new ArrayList<BcaBankFreeTextLanguage>(),
                                new ArrayList<BcaBankFreeTextLanguage>()),
                        ""))
                .setCustomField1("Custom Field 1")
                .setCustomField2("Custom Field 2")
                .setCustomField3("Custom Field 3")
                .build();

        CheckoutTransaction trxInstance = CheckoutTransaction.getInstance();
        MidtransSdk.getInstance().setCheckoutTransaction(trxInstance);
        MidtransSdk.getInstance().checkoutWithTransaction(new MidtransCallback<CheckoutWithTransactionResponse>() {
            @Override
            public void onFailed(Throwable throwable) {
                Logger.debug("MIDTRANS SDK NEW RETURN ERROR >>> " + throwable.getMessage());
            }

            @Override
            public void onSuccess(CheckoutWithTransactionResponse data) {
                Logger.debug("RESULT TOKEN CHECKOUT " + data.getSnapToken());
                getTransactionOptions(data.getSnapToken());
                snapToken = data.getSnapToken();
                registerCard();
            }
        });
    }


    private void getTransactionOptions(final String snapToken) {
        MidtransSdk.getInstance().getPaymentInfo(snapToken, new MidtransCallback<PaymentInfoResponse>() {
            @Override
            public void onSuccess(PaymentInfoResponse data) {
                //startPayment(snapToken);
                Logger.debug("RESULT SUCCESS PAYMENT INFO " + data.getToken());
            }

            @Override
            public void onFailed(Throwable throwable) {
                Logger.debug("MIDTRANS SDK NEW RETURN ERROR >>> " + throwable.getMessage());

            }
        });
    }

    private void registerCard() {
        CreditCardCharge.cardRegistration(
                "4105058689481467",
                "123",
                "12",
                "2019",
                new MidtransCallback<CardRegistrationResponse>() {
                    @Override
                    public void onSuccess(CardRegistrationResponse data) {

                    }

                    @Override
                    public void onFailed(Throwable throwable) {

                    }
                }
        );
    }

    private void startCCPayment(final CardRegistrationResponse cc) {
        CreditCardCharge.paymentUsingCard(
                snapToken,
                new CreditCardPaymentParams(cc.getSavedTokenId(), true, cc.getMaskedCard()),
                new CustomerDetailPayRequest("Budi Utomo", "budi@utomo.comin", "081808466410"),
                new MidtransCallback<BasePaymentResponse>() {
                    @Override
                    public void onSuccess(BasePaymentResponse data) {

                    }

                    @Override
                    public void onFailed(Throwable throwable) {

                    }
                }
        );

    }
}