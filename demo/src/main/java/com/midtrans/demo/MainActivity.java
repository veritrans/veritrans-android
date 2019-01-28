package com.midtrans.demo;

import android.os.Bundle;

import com.midtrans.sdk.corekit.MidtransSdk;
import com.midtrans.sdk.corekit.base.callback.MidtransCallback;
import com.midtrans.sdk.corekit.base.enums.AcquiringBankType;
import com.midtrans.sdk.corekit.base.enums.AcquiringChannel;
import com.midtrans.sdk.corekit.base.enums.Authentication;
import com.midtrans.sdk.corekit.base.enums.CreditCardTransactionType;
import com.midtrans.sdk.corekit.base.enums.Currency;
import com.midtrans.sdk.corekit.base.enums.Environment;
import com.midtrans.sdk.corekit.base.enums.ExpiryTimeUnit;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.CheckoutTransaction;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.optional.BillInfoModel;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.optional.CheckoutExpiry;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.optional.Item;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.optional.customer.Address;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.optional.customer.CustomerDetails;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.specific.banktransfer.BcaBankFreeText;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.specific.banktransfer.BcaBankFreeTextLanguage;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.specific.banktransfer.BcaBankTransferRequestModel;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.specific.creditcard.CreditCard;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.response.CheckoutWithTransactionResponse;
import com.midtrans.sdk.corekit.utilities.Logger;

import java.util.ArrayList;

import androidx.appcompat.app.AppCompatActivity;

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

        CheckoutTransaction checkoutTransaction = CheckoutTransaction
                .builder("sample_sdk_test_core_" + System.currentTimeMillis(), 20000.0)
                .setCurrency(Currency.IDR)
                .setGopayCallbackDeepLink("demo://midtrans")
                .setCreditCard(CreditCard
                        .builder()
                        //.setTokenId("")
                        .setSaveCard(true)
                        .setSecure(Authentication.AUTH_3DS)
                        .setType(CreditCardTransactionType.AUTHORIZE)
                        .setAcquiringBank(AcquiringBankType.BCA)
                        .setAcquiringChannel(AcquiringChannel.MIGS)
                        .build())
                .setCustomerDetails(
                        CustomerDetails
                                .builder()
                                .setFirstName("FirstName")
                                .setLastName("LastName")
                                .setEmail("mail@mailbox.com")
                                .setPhone("08123456789")
                                .setBillingAddress(
                                        Address
                                                .builder()
                                                .setFirstName("FirstName")
                                                .setLastName("LastName")
                                                .setAddress("address")
                                                .setCity("City")
                                                .setPostalCode("12345")
                                                .setPhone("08123456789")
                                                .setCountryCode("IDR")
                                                .build()
                                )
                                .setShippingAddress(
                                        Address
                                                .builder()
                                                .setFirstName("FirstName")
                                                .setLastName("LastName")
                                                .setAddress("address")
                                                .setCity("City")
                                                .setPostalCode("12345")
                                                .setPhone("08123456789")
                                                .setCountryCode("IDR")
                                                .build()
                                )
                                .build()
                )
                .setBillInfoModel(new BillInfoModel("1", "2"))
                .setEnabledPayments(new ArrayList<String>())
                .setCheckoutExpiry(new CheckoutExpiry("", ExpiryTimeUnit.DAY, 1))
                .setCheckoutItems(new ArrayList<Item>())
                .setBcaVa(new BcaBankTransferRequestModel("",
                        new BcaBankFreeText(new ArrayList<BcaBankFreeTextLanguage>(),
                                new ArrayList<BcaBankFreeTextLanguage>()),
                        ""))
                .setCustomField1("Custom Field 1")
                .setCustomField2("Custom Field 2")
                .setCustomField3("Custom Field 3")
                .build();

        MidtransSdk.getInstance().checkoutWithTransaction(checkoutTransaction, new MidtransCallback<CheckoutWithTransactionResponse>() {
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