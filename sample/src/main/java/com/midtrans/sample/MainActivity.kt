package com.midtrans.sample

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.midtrans.sdk.corekit.MidtransSdk
import com.midtrans.sdk.corekit.base.callback.MidtransCallback
import com.midtrans.sdk.corekit.base.enums.AcquiringBankType
import com.midtrans.sdk.corekit.base.enums.Authentication
import com.midtrans.sdk.corekit.base.enums.Currency
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.CheckoutTransaction
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.optional.Item
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.optional.customer.Address
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.optional.customer.CustomerDetails
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.specific.creditcard.CreditCard
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.response.CheckoutWithTransactionResponse
import com.midtrans.sdk.corekit.core.api.midtrans.model.registration.CreditCardTokenizeResponse
import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.PaymentInfoResponse
import com.midtrans.sdk.corekit.core.payment.CreditCardCharge
import com.midtrans.sdk.corekit.utilities.Logger
import com.midtrans.sdk.uikit.MidtransKit
import com.midtrans.sdk.uikit.base.callback.PaymentResult
import com.midtrans.sdk.uikit.base.callback.Result
import com.midtrans.sdk.uikit.base.model.PaymentResponse
import com.midtrans.sdk.uikit.utilities.Helper
import kotlinx.android.synthetic.main.activity_main.*
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        text_view_test.setOnClickListener {
            val checkoutTransaction = CheckoutTransaction
                .builder(
                    getRandomString(5),
                    20000.0
                )
                .setUserId("midtrans.android2@yopmail.com")
                .setCurrency(Currency.IDR)
                .setGopayCallbackDeepLink("demo://midtrans")
                .setCreditCard(
                    CreditCard
                        .builder()
                        .setAcquiringBank(AcquiringBankType.BCA)
                        .setInstallment(false, HashMap<String, MutableList<Int>>())
                        .setBlackListBins(mutableListOf())
                        .setWhiteListBins(mutableListOf())
                        .setSavedTokens(mutableListOf())
                        .setSaveCard(true)
                        .setAuthentication(Authentication.AUTH_3DS)
                        .build())
                .setCustomerDetails(
                    CustomerDetails
                        .builder()
                        .setFirstName("Budi")
                        .setLastName("Utomo")
                        .setEmail("midtrans.android2@yopmail.com")
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
                                .build()
                        )
                        .build()
                )
                .setCheckoutItems(mutableListOf(Item("1", 20000.0, 1, "sabun")))
                .build()

            MidtransKit
                .getInstance()
                .startPaymentUiWithTransaction(
                    this,
                    checkoutTransaction,
                    object : PaymentResult {
                        override fun onPaymentFinished(result: Result?, response: PaymentResponse?) {
                            Logger.debug("RESULT IS >>> ${result?.paymentType} AND ${result?.paymentStatus} >>> ${response?.bcaVaNumber} || ${response?.bniVaNumber} || ${response?.permataVaNumber} || ${response?.billKey}")
                        }

                        override fun onFailed(throwable: Throwable?) {
                            Logger.debug("ERROR IS >>> ${throwable?.message}")
                        }
                    }
                )
        }
    }

    private fun checkout(checkoutTransaction: CheckoutTransaction) {
        MidtransSdk.getInstance().checkoutWithTransaction(checkoutTransaction,
            object : MidtransCallback<CheckoutWithTransactionResponse> {
                override fun onSuccess(data: CheckoutWithTransactionResponse) {
                    Logger.debug("Success return snapToken ${data.token}")
                }

                override fun onFailed(throwable: Throwable) {
                    Logger.debug("Failed return error >>> ${throwable.message}")
                }
            })
    }

    fun getRandomString(length: Int): String {
        val formatter = SimpleDateFormat("mmss")
        val date = Date()
        val allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXTZabcdefghiklmnopqrstuvwxyz"
        val alpha = (1..length)
            .map { allowedChars.random() }
            .joinToString("")
        val randomNumber = (Math.random() * 5250 + 152).toInt()
        return "SAM-" + alpha + Helper.generateRandomNumber() + "" + randomNumber + "" + formatter.format(date)
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
        CreditCardCharge.tokenizeCard(
            "4105058689481467",
            "123",
            "12",
            "2019",
            object : MidtransCallback<CreditCardTokenizeResponse> {
                override fun onSuccess(data: CreditCardTokenizeResponse) {

                }

                override fun onFailed(throwable: Throwable) {

                }
            }
        )
    }

}