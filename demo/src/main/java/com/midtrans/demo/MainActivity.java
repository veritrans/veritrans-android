package com.midtrans.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.midtrans.sdk.corekit.MidtransSdk;
import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.base.enums.Authentication;
import com.midtrans.sdk.corekit.base.enums.BankType;
import com.midtrans.sdk.corekit.base.enums.Environment;
import com.midtrans.sdk.corekit.base.enums.ExpiryModelUnit;
import com.midtrans.sdk.corekit.base.model.Currency;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.CheckoutTransaction;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.optional.BillInfoModel;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.optional.ExpiryModel;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.optional.Item;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.optional.customer.Address;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.optional.customer.CustomerDetails;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.specific.banktransfer.BcaBankFreeText;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.specific.banktransfer.BcaBankFreeTextLanguage;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.specific.banktransfer.BcaBankTransferRequestModel;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.specific.creditcard.CreditCard;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.response.CheckoutWithTransactionResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.EwalletGopayPaymentResponse;
import com.midtrans.sdk.corekit.core.payment.EWalletCharge;
import com.midtrans.sdk.corekit.utilities.Logger;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    String snapToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Environment environment;
        if (BuildConfig.DEBUG) {
            environment = Environment.SANDBOX;
        } else {
            environment = Environment.PRODUCTION;
        }

        MidtransSdk
                .builder(this,
                        BuildConfig.CLIENT_KEY,
                        BuildConfig.BASE_URL)
                .setLogEnabled(true)
                .setEnvironment(environment)
                .setApiRequestTimeOut(60)
                .build();

        CheckoutTransaction trxRequest = CheckoutTransaction
                .builder("sample_sdk_test_core_" + System.currentTimeMillis(), 20000.0)
                .setCurrency(Currency.IDR)
                .setGopayCallbackDeepLink("demo://midtrans")
                .setCreditCard(CreditCard
                        .twoClickBuilder(false)
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
                        new Address("Firstname",
                                "LastName",
                                "mail@mail.com",
                                "Bogor",
                                "16710",
                                "62877",
                                "idn"),
                        new Address("Firstname",
                                "LastName",
                                "mail@mail.com",
                                "Bogor",
                                "16710",
                                "62877",
                                "idn")))
                .setBillInfoModel(new BillInfoModel("1", "2"))
                .setEnabledPayments(new ArrayList<String>())
                .setExpiry(new ExpiryModel("", ExpiryModelUnit.EXPIRY_UNIT_DAY, 1))
                .setCheckoutItems(new ArrayList<Item>())
                .setBcaVa(new BcaBankTransferRequestModel("",
                        new BcaBankFreeText(new ArrayList<BcaBankFreeTextLanguage>(),
                                new ArrayList<BcaBankFreeTextLanguage>()),
                        ""))
                .setCustomField1("Custom Field 1")
                .setCustomField2("Custom Field 2")
                .setCustomField3("Custom Field 3")
                .build();

        EWalletCharge.paymentUsingGopay("snapToken",
                "08123456789",
                new MidtransCallback<EwalletGopayPaymentResponse>() {
                    @Override
                    public void onSuccess(EwalletGopayPaymentResponse data) {

                    }

                    @Override
                    public void onFailed(Throwable throwable) {

                    }
                });

        CheckoutTransaction trxInstance = CheckoutTransaction.getInstance();
        MidtransSdk.getInstance().setCheckoutTransaction(trxInstance);
        MidtransSdk.getInstance().checkoutWithTransaction(new MidtransCallback<CheckoutWithTransactionResponse>() {
            @Override
            public void onFailed(Throwable throwable) {
                Logger.debug("MIDTRANS SDK NEW RETURN ERROR >>> " + throwable.getMessage());
            }

            @Override
            public void onSuccess(CheckoutWithTransactionResponse data) {
                Logger.debug("RESULT TOKEN CHECKOUT " + data.getToken());
                snapToken = data.getToken();
            }
        });
    }

}