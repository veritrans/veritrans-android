package com.midtrans.sample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.midtrans.sdk.corekit.MidtransSdk
import com.midtrans.sdk.corekit.base.callback.MidtransCallback
import com.midtrans.sdk.corekit.base.enums.Environment
import com.midtrans.sdk.corekit.base.enums.ExpiryModelUnit
import com.midtrans.sdk.corekit.base.model.BankType
import com.midtrans.sdk.corekit.base.model.Currency
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.CheckoutTransaction
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.optional.BillInfoModel
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.optional.ExpiryModel
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.optional.customer.BillingAddress
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.optional.customer.CustomerDetails
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.optional.customer.ShippingAddress
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.specific.banktransfer.BcaBankFreeText
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.specific.banktransfer.BcaBankTransferRequestModel
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.specific.creditcard.CreditCard
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.response.CheckoutWithTransactionResponse
import com.midtrans.sdk.corekit.core.api.midtrans.model.cardregistration.CardRegistrationResponse
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.BasePaymentResponse
import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.PaymentInfoResponse
import com.midtrans.sdk.corekit.core.payment.CreditCardCharge
import com.midtrans.sdk.corekit.core.payment.OnlineDebitCharge
import com.midtrans.sdk.corekit.utilities.InstallationHelper
import com.midtrans.sdk.corekit.utilities.Logger
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MidtransSdk
            .builder(this,
                BuildConfig.CLIENT_KEY,
                BuildConfig.BASE_URL)
            .setLogEnabled(true)
            .setEnvironment(if (BuildConfig.DEBUG) {
                Environment.SANDBOX
            } else {
                Environment.PRODUCTION
            })
            .setApiRequestTimeOut(60)
            .build()

        MidtransSdk
            .builder(this,
                BuildConfig.CLIENT_KEY,
                BuildConfig.BASE_URL)
            .setApiRequestTimeOut(40)
            .setEnvironment(Environment.SANDBOX)
            .setLogEnabled(true)
            .build()

        val instance = MidtransSdk.getInstance()

        val checkoutTransaction = CheckoutTransaction
            .builder("", 1.0)
            .build()

        MidtransSdk.getInstance().checkoutWithTransaction(checkoutTransaction,
            object : MidtransCallback<CheckoutWithTransactionResponse> {
                override fun onSuccess(data: CheckoutWithTransactionResponse) {
                    Logger.debug("Success return snapToken ${data.token}")
                }

                override fun onFailed(throwable: Throwable) {
                    Logger.debug("Failed return error >>> ${throwable.message}")
                }
            })

        MidtransSdk.getInstance().getPaymentInfo("",
            object : MidtransCallback<PaymentInfoResponse> {
                override fun onSuccess(data: PaymentInfoResponse) {
                    Logger.debug("RESULT SUCCESS PAYMENT INFO")
                }

                override fun onFailed(throwable: Throwable) {
                    Logger.debug("Failed return error >>> ${throwable.message}")
                }
            })

        val trxRequest = CheckoutTransaction
            .builder(InstallationHelper.generatedRandomID(this),
                20000.0)
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
            .setCustomerDetails(CustomerDetails("FirstName",
                "LastName",
                "email@mail.com",
                "6281234567890",
                ShippingAddress("Firstname",
                    "LastName",
                    "mail@mail.com",
                    "Bogor",
                    "16710",
                    "62877",
                    "idn"),
                BillingAddress("Firstname",
                    "LastName",
                    "mail@mail.com",
                    "Bogor",
                    "16710",
                    "62877",
                    "idn")))
            .setBillInfoModel(BillInfoModel("1", "2"))
            .setEnabledPayments(ArrayList())
            .setExpiry(ExpiryModel("", ExpiryModelUnit.EXPIRY_UNIT_DAY, 1))
            .setItemDetails(ArrayList())
            .setBcaVa(BcaBankTransferRequestModel("",
                BcaBankFreeText(ArrayList(),
                    ArrayList()),
                ""))
            .setCustomField1("Custom Field 1")
            .setCustomField2("Custom Field 2")
            .setCustomField3("Custom Field 3")
            .build()

        MidtransSdk.getInstance().checkoutTransaction = trxRequest
        MidtransSdk.getInstance().checkoutWithTransaction(object : MidtransCallback<CheckoutWithTransactionResponse> {
            override fun onFailed(throwable: Throwable) {
                Logger.debug("MIDTRANS SDK NEW RETURN ERROR >>> " + throwable.message)
            }

            override fun onSuccess(data: CheckoutWithTransactionResponse) {
                Logger.debug("RESULT TOKEN CHECKOUT " + data.token)
                getTransactionOptions(data.token)
                registerCard()
            }
        })
    }

    private fun getTransactionOptions(snapToken: String) {
        MidtransSdk.getInstance().getPaymentInfo(snapToken, object : MidtransCallback<PaymentInfoResponse> {
            override fun onSuccess(data: PaymentInfoResponse) {
                //startPayment(snapToken);
                Logger.debug("RESULT SUCCESS PAYMENT INFO " + data.token)
            }

            override fun onFailed(throwable: Throwable) {
                Logger.debug("MIDTRANS SDK NEW RETURN ERROR >>> " + throwable.message)

            }
        })
    }

    private fun registerCard() {
        CreditCardCharge.cardRegistration(
            "4105058689481467",
            "123",
            "12",
            "2019",
            object : MidtransCallback<CardRegistrationResponse> {
                override fun onSuccess(data: CardRegistrationResponse) {

                }

                override fun onFailed(throwable: Throwable) {

                }
            }
        )
    }

}