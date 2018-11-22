package com.midtrans.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.midtrans.sdk.corekit.base.enums.Environment;
import com.midtrans.sdk.corekit.base.model.BankType;
import com.midtrans.sdk.corekit.core.MidtransSdk;
import com.midtrans.sdk.corekit.core.merchant.model.checkout.CheckoutCallback;
import com.midtrans.sdk.corekit.core.merchant.model.checkout.request.TransactionRequest;
import com.midtrans.sdk.corekit.core.merchant.model.checkout.request.optional.customer.BillingAddress;
import com.midtrans.sdk.corekit.core.merchant.model.checkout.request.optional.customer.CustomerDetails;
import com.midtrans.sdk.corekit.core.merchant.model.checkout.request.optional.customer.ShippingAddress;
import com.midtrans.sdk.corekit.core.merchant.model.checkout.request.specific.creditcard.CreditCard;
import com.midtrans.sdk.corekit.core.merchant.model.checkout.response.TokenResponse;
import com.midtrans.sdk.corekit.core.snap.model.transaction.TransactionOptionsCallback;
import com.midtrans.sdk.corekit.core.snap.model.transaction.response.TransactionOptionsResponse;
import com.midtrans.sdk.corekit.utilities.Currency;
import com.midtrans.sdk.corekit.utilities.Logger;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MidtransSdk
                .builder(this,
                        BuildConfig.CLIENT_KEY,
                        BuildConfig.BASE_URL)
                .setLogEnabled(true)
                .setEnvironment(Environment.SANDBOX)
                .build();

        TransactionRequest trxRequest = TransactionRequest
                .builder("sample_sdk_test_core_00005", 20000.0)
                .setCurrency(Currency.IDR)
                .setGopayCallbackDeepLink("demo://midtrans")
                .setCreditCard(CreditCard
                        .normalClickBuilder(false, CreditCard.AUTHENTICATION_TYPE_NONE)
                        .setTokenId("")
                        .setSaveCard(true)
                        .setBank(BankType.BCA)
                        .setInstallment(false, null)
                        .setBlackListBins(null)
                        .setWhiteListBins(null)
                        .setSavedTokens(null)
                        .setChannel(CreditCard.MIGS)
                        .build())
                .setCustomerDetails(CustomerDetails
                        .builder("FirstName",
                                "LastName",
                                "email@mail.com",
                                "6281234567890")
                        .setBillingAddress(new BillingAddress("Firstname",
                                "LastName",
                                "mail@mail.com",
                                "Bogor",
                                "16710",
                                "62877",
                                "id"))
                        .setShippingAddress(new ShippingAddress("Firstname",
                                "LastName",
                                "mail@mail.com",
                                "Bogor",
                                "16710",
                                "62877",
                                "id"))
                        .build())
                .build();

        TransactionRequest trxInstance = TransactionRequest.getInstance();

        MidtransSdk.getInstance().setTransactionRequest(trxRequest);
        MidtransSdk.getInstance().checkout(new CheckoutCallback() {
            @Override
            public void onSuccess(TokenResponse token) {
                Logger.debug("MIDTRANS SDK NEW RETURN SUCCESS >>> " + token.getSnapToken());
                getTransactionOptions(token.getSnapToken());
            }

            @Override
            public void onFailure(TokenResponse token, String reason) {
                Logger.debug("MIDTRANS SDK NEW RETURN FAILURE >>> " + reason);
            }

            @Override
            public void onError(Throwable error) {
                Logger.debug("MIDTRANS SDK NEW RETURN ERROR >>> " + error.getMessage());
            }
        });
    }

    private void getTransactionOptions(String snapToken) {
        MidtransSdk.getInstance().getPaymentInfo(snapToken, new TransactionOptionsCallback() {
            @Override
            public void onSuccess(TransactionOptionsResponse transaction) {
                Logger.debug("MIDTRANS SDK NEW RETURN SUCCESS >>> " + transaction.getToken());

            }

            @Override
            public void onFailure(TransactionOptionsResponse transaction, String reason) {
                Logger.debug("MIDTRANS SDK NEW RETURN FAILURE >>> " + reason);

            }

            @Override
            public void onError(Throwable error) {
                Logger.debug("MIDTRANS SDK NEW RETURN ERROR >>> " + error.getMessage());

            }
        });
    }
}