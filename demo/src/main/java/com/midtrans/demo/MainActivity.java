package com.midtrans.demo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.base.enums.Environment;
import com.midtrans.sdk.corekit.base.model.BankType;
import com.midtrans.sdk.corekit.core.MidtransSdk;
import com.midtrans.sdk.corekit.core.merchant.model.checkout.request.TransactionRequest;
import com.midtrans.sdk.corekit.core.merchant.model.checkout.request.optional.customer.BillingAddress;
import com.midtrans.sdk.corekit.core.merchant.model.checkout.request.optional.customer.CustomerDetails;
import com.midtrans.sdk.corekit.core.merchant.model.checkout.request.optional.customer.ShippingAddress;
import com.midtrans.sdk.corekit.core.merchant.model.checkout.request.specific.creditcard.CreditCard;
import com.midtrans.sdk.corekit.core.merchant.model.checkout.response.CheckoutResponse;
import com.midtrans.sdk.corekit.core.snap.model.pay.request.va.CustomerDetailRequest;
import com.midtrans.sdk.corekit.core.snap.model.pay.response.va.BcaPaymentResponse;
import com.midtrans.sdk.corekit.core.snap.model.transaction.response.PaymentInfoResponse;
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
                .builder("sample_sdk_test_core_" + System.currentTimeMillis(), 20000.0)
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
        MidtransSdk.getInstance().checkout(new MidtransCallback<CheckoutResponse>() {
            @Override
            public void onSuccess(CheckoutResponse data) {
                Logger.debug("RESULT TOKEN CHECKOUT " + data.getSnapToken());
                getTransactionOptions(data.getSnapToken());
            }

            @Override
            public void onFailed(Throwable throwable) {
                Logger.debug("RESULT ERROR CHECKOUT " + throwable.getMessage());
            }
        });
    }

    private void getTransactionOptions(final String snapToken) {
        MidtransSdk.getInstance().getPaymentInfo(snapToken, new MidtransCallback<PaymentInfoResponse>() {
            @Override
            public void onSuccess(PaymentInfoResponse data) {
                startPayment(snapToken);
                Logger.debug("RESULT SUCCESS PAYMENT INFO " + data.getToken());
            }

            @Override
            public void onFailed(Throwable throwable) {
                Logger.debug("RESULT ERROR PAYMENT INFO " + throwable.getMessage());
            }
        });
    }

    private void startPayment(String snapToken) {
        MidtransSdk.getInstance().paymentUsingBankTransferVaBca(snapToken,
                new CustomerDetailRequest("FirstName",
                        "mail@test.com",
                        "08123456789"),
                new MidtransCallback<BcaPaymentResponse>() {
                    @Override
                    public void onSuccess(BcaPaymentResponse data) {
                        Logger.debug("RESULT SUCCESS PAYMENT " + data.getBcaVaNumber());
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                        Logger.debug("RESULT ERROR PAYMENT " + throwable.getMessage());
                    }
                });
    }
}