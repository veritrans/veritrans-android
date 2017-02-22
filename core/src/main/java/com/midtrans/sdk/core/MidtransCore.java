package com.midtrans.sdk.core;

import com.midtrans.sdk.core.api.RestAdapter;
import com.midtrans.sdk.core.api.merchant.MerchantApiManager;
import com.midtrans.sdk.core.api.papi.MidtransApiManager;
import com.midtrans.sdk.core.api.snap.SnapApiManager;
import com.midtrans.sdk.core.models.merchant.CheckoutTokenRequest;
import com.midtrans.sdk.core.models.merchant.CheckoutTokenResponse;
import com.midtrans.sdk.core.models.papi.CardTokenRequest;
import com.midtrans.sdk.core.models.papi.CardTokenResponse;
import com.midtrans.sdk.core.models.snap.SnapCustomerDetails;
import com.midtrans.sdk.core.models.snap.bank.bca.BcaBankTransferPaymentResponse;
import com.midtrans.sdk.core.models.snap.bank.mandiri.MandiriBankTransferPaymentResponse;
import com.midtrans.sdk.core.models.snap.bank.other.OtherBankTransferPaymentResponse;
import com.midtrans.sdk.core.models.snap.bank.permata.PermataBankTransferPaymentResponse;
import com.midtrans.sdk.core.models.snap.bins.BankBinsResponse;
import com.midtrans.sdk.core.models.snap.card.CreditCardPaymentParams;
import com.midtrans.sdk.core.models.snap.card.CreditCardPaymentResponse;
import com.midtrans.sdk.core.models.snap.conveniencestore.indomaret.IndomaretPaymentResponse;
import com.midtrans.sdk.core.models.snap.conveniencestore.kioson.KiosonPaymentResponse;
import com.midtrans.sdk.core.models.snap.ebanking.bcaklikpay.BcaKlikpayPaymentResponse;
import com.midtrans.sdk.core.models.snap.ebanking.cimbclicks.CimbClicksPaymentResponse;
import com.midtrans.sdk.core.models.snap.ebanking.epaybri.EpayBriPaymentResponse;
import com.midtrans.sdk.core.models.snap.ebanking.klikbca.KlikBcaPaymentResponse;
import com.midtrans.sdk.core.models.snap.ebanking.mandiriclickpay.MandiriClickpayPaymentParams;
import com.midtrans.sdk.core.models.snap.ebanking.mandiriclickpay.MandiriClickpayPaymentResponse;
import com.midtrans.sdk.core.models.snap.ebanking.mandiriecash.MandiriECashPaymentResponse;
import com.midtrans.sdk.core.models.snap.ewallet.indosatdompetku.IndosatDompetkuPaymentResponse;
import com.midtrans.sdk.core.models.snap.ewallet.tcash.TelkomselCashPaymentResponse;
import com.midtrans.sdk.core.models.snap.ewallet.xltunai.XlTunaiPaymentResponse;
import com.midtrans.sdk.core.models.snap.gci.GiftCardPaymentResponse;
import com.midtrans.sdk.core.models.snap.transaction.SnapTransaction;
import com.midtrans.sdk.core.utils.Logger;
import com.midtrans.sdk.core.utils.PaymentUtilities;

import java.util.List;

/**
 * Midtrans SDK core class.
 */
public class MidtransCore {
    private static MidtransCore midtransCore;
    private Environment environment;
    private String clientKey;

    private MidtransApiManager midtransApiManager;
    private SnapApiManager snapApiManager;

    public MidtransCore(Builder builder) {
        this.clientKey = builder.clientKey;
        Logger.enabled = builder.enableLog;
        this.environment = builder.environment;

        // Initialize API manager
        this.midtransApiManager = initialisePaymentApiManager();
        this.snapApiManager = initialiseSnapApiManager();

        // Set static singleton instance
        Logger.debug("Midtrans core SDK initialized.");
        midtransCore = this;
    }

    /**
     * Return SDK singleton instance.
     *
     * @return SDK intance.
     */
    public synchronized static MidtransCore getInstance() {
        if (midtransCore == null) {
            throw new RuntimeException("SDK is not initialized. Please initialize it using Midtrans.Builder().");
        }

        return midtransCore;
    }

    private MidtransApiManager initialisePaymentApiManager() {
        return new MidtransApiManager(new RestAdapter(environment).getMidtransApiClient());
    }

    private SnapApiManager initialiseSnapApiManager() {
        return new SnapApiManager(new RestAdapter(environment).getSnapApiClient());
    }

    private MerchantApiManager initialiseMerchantApiManager(String checkoutUrl) {
        return new MerchantApiManager(new RestAdapter(environment).getMerchantApiClient(checkoutUrl));
    }

    /**
     * Get client key from SDK instance.
     *
     * @return client key.
     */
    public String getClientKey() {
        return clientKey;
    }

    /**
     * Start checkout request via merchant server.
     *
     * @param checkoutTokenRequest checkout token request.
     * @param callback             callback to be called after checkout was finished.
     */
    public void checkout(String checkoutUrl, CheckoutTokenRequest checkoutTokenRequest, MidtransCoreCallback<CheckoutTokenResponse> callback) {
        MerchantApiManager merchantApiManager = initialiseMerchantApiManager(checkoutUrl.endsWith("/") ? checkoutUrl : checkoutUrl + "/");
        merchantApiManager.checkout(checkoutUrl, checkoutTokenRequest, callback);
    }

    public void getTransactionDetails(String checkoutToken, MidtransCoreCallback<SnapTransaction> callback){
        snapApiManager.getTransactionDetails(checkoutToken, callback);
    }
    /**
     * Get card token from Midtrans Payment API.
     */
    public void getCardToken(CardTokenRequest cardTokenRequest, MidtransCoreCallback<CardTokenResponse> callback) {
        midtransApiManager.getCardToken(cardTokenRequest, callback);
    }

    /**
     * Start payment for transaction for specific checkout token using credit card.
     *
     * @param checkoutToken   checkout token.
     * @param paymentParams   credit card payment params.
     * @param customerDetails customer details.
     * @param callback        callback to be called after transaction was done.
     */
    public void paymentUsingCreditCard(String checkoutToken,
                                       CreditCardPaymentParams paymentParams,
                                       SnapCustomerDetails customerDetails,
                                       MidtransCoreCallback<CreditCardPaymentResponse> callback) {
        snapApiManager.paymentUsingCreditCard(checkoutToken, PaymentUtilities.buildCreditCardPaymentRequest(paymentParams, customerDetails), callback);
    }

    /**
     * Start payment for transaction for specific checkout token using BCA bank trasfer.
     *
     * @param checkoutToken checkout token.
     * @param callback      callback to be called after transaction was done.
     */
    public void paymentUsingBcaBankTransfer(String checkoutToken,
                                            MidtransCoreCallback<BcaBankTransferPaymentResponse> callback) {
        snapApiManager.paymentUsingBcaBankTransfer(checkoutToken, PaymentUtilities.buildBcaBankTransferPaymentRequest(), callback);
    }

    /**
     * Start payment for transaction for specific checkout token using BCA bank trasfer.
     *
     * @param checkoutToken   checkout token.
     * @param customerDetails customer details.
     * @param callback        callback to be called after transaction was done.
     */
    public void paymentUsingBcaBankTransfer(String checkoutToken,
                                            SnapCustomerDetails customerDetails,
                                            MidtransCoreCallback<BcaBankTransferPaymentResponse> callback) {
        snapApiManager.paymentUsingBcaBankTransfer(checkoutToken, PaymentUtilities.buildBcaBankTransferPaymentRequest(customerDetails), callback);
    }

    /**
     * Start payment for transaction for specific checkout token using Permata bank transfer.
     *
     * @param checkoutToken checkout token.
     * @param callback      callback to be called after transaction was done.
     */
    public void paymentUsingPermataBankTransfer(String checkoutToken,
                                                MidtransCoreCallback<PermataBankTransferPaymentResponse> callback) {
        snapApiManager.paymentUsingPermataBankTransfer(checkoutToken, PaymentUtilities.buildPermataBankTransferPaymentRequest(), callback);
    }

    /**
     * Start payment for transaction for specific checkout token using Permata bank transfer.
     *
     * @param checkoutToken   checkout token.
     * @param customerDetails customer details.
     * @param callback        callback to be called after transaction was done.
     */
    public void paymentUsingPermataBankTransfer(String checkoutToken,
                                                SnapCustomerDetails customerDetails,
                                                MidtransCoreCallback<PermataBankTransferPaymentResponse> callback) {
        snapApiManager.paymentUsingPermataBankTransfer(checkoutToken, PaymentUtilities.buildPermataBankTransferPaymentRequest(customerDetails), callback);
    }

    /**
     * Start payment for transaction for specific checkout token using Mandiri bill pay.
     *
     * @param checkoutToken checkout token.
     * @param callback      callback to be called after transaction was done.
     */
    public void paymentUsingMandiriBankTransfer(String checkoutToken,
                                                MidtransCoreCallback<MandiriBankTransferPaymentResponse> callback) {
        snapApiManager.paymentUsingMandiriBankTransfer(checkoutToken, PaymentUtilities.buildMandiriBankTransferPaymentRequest(), callback);
    }

    /**
     * Start payment for transaction for specific checkout token using Mandiri bill pay.
     *
     * @param checkoutToken   checkout token.
     * @param customerDetails customer details.
     * @param callback        callback to be called after transaction was done.
     */
    public void paymentUsingMandiriBankTransfer(String checkoutToken,
                                                SnapCustomerDetails customerDetails,
                                                MidtransCoreCallback<MandiriBankTransferPaymentResponse> callback) {
        snapApiManager.paymentUsingMandiriBankTransfer(checkoutToken, PaymentUtilities.buildMandiriBankTransferPaymentRequest(customerDetails), callback);
    }

    /**
     * Start payment for transaction for specific checkout token using other bank transfer.
     *
     * @param checkoutToken checkout token.
     * @param callback      callback to be called after transaction was done.
     */
    public void paymentUsingOtherBankTransfer(String checkoutToken,
                                              MidtransCoreCallback<OtherBankTransferPaymentResponse> callback) {
        snapApiManager.paymentUsingOtherBankTransfer(checkoutToken, PaymentUtilities.buildOtherBankTransferPaymentRequest(), callback);
    }

    /**
     * Start payment for transaction for specific checkout token using other bank transfer.
     *
     * @param checkoutToken   checkout token.
     * @param customerDetails customer details.
     * @param callback        callback to be called after transaction was done.
     */
    public void paymentUsingOtherBankTransfer(String checkoutToken,
                                              SnapCustomerDetails customerDetails,
                                              MidtransCoreCallback<OtherBankTransferPaymentResponse> callback) {
        snapApiManager.paymentUsingOtherBankTransfer(checkoutToken, PaymentUtilities.buildOtherBankTransferPaymentRequest(customerDetails), callback);
    }

    /**
     * Start payment for transaction for specific checkout token using BCA KlikPay.
     *
     * @param checkoutToken checkout token.
     * @param callback      callback to be called after transaction was done.
     */
    public void paymentUsingBcaKlikpay(String checkoutToken,
                                       MidtransCoreCallback<BcaKlikpayPaymentResponse> callback) {
        snapApiManager.paymentUsingBcaKlikpay(checkoutToken, PaymentUtilities.buildBcaKlikpayPaymentRequest(), callback);
    }

    /**
     * Start payment for transaction for specific checkout token using KlikBCA.
     *
     * @param checkoutToken checkout token.
     * @param klikBcaId     user ID of KlikBCA account.
     * @param callback      callback to be called after transaction was done.
     */
    public void paymentUsingKlikBca(String checkoutToken,
                                    String klikBcaId,
                                    MidtransCoreCallback<KlikBcaPaymentResponse> callback) {
        snapApiManager.paymentUsingKlikBca(checkoutToken, PaymentUtilities.buildKlikBcaPaymentRequest(klikBcaId), callback);
    }

    /**
     * Start payment for transaction for specific checkout token using Epay BRI.
     *
     * @param checkoutToken checkout token.
     * @param callback      callback to be called after transaction was done.
     */
    public void paymentUsingEpayBri(String checkoutToken,
                                    MidtransCoreCallback<EpayBriPaymentResponse> callback) {
        snapApiManager.paymentUsingEpayBri(checkoutToken, PaymentUtilities.buildEpayBriPaymentRequest(), callback);
    }

    /**
     * Start payment for transaction for specific checkout token using CIMB Clicks.
     *
     * @param checkoutToken checkout token
     * @param callback      callback to be called after transaction was done.
     */
    public void paymentUsingCimbClicks(String checkoutToken,
                                       MidtransCoreCallback<CimbClicksPaymentResponse> callback) {
        snapApiManager.paymentUsingCimbClicks(checkoutToken, PaymentUtilities.buildCimbClicksPaymentRequest(), callback);
    }

    /**
     * Start payment for transaction for specific checkout token using Mandiri Clickpay.
     *
     * @param checkoutToken checkout token.
     * @param paymentParams payment parameters.
     * @param callback      callback to be called after transaction was done.
     */
    public void paymentUsingMandiriClickpay(String checkoutToken,
                                            MandiriClickpayPaymentParams paymentParams,
                                            MidtransCoreCallback<MandiriClickpayPaymentResponse> callback) {
        snapApiManager.paymentUsingMandiriClickpay(checkoutToken, PaymentUtilities.buildMandiriClickpayPaymentRequest(paymentParams), callback);
    }

    /**
     * Start payment for transaction for specific checkout token using Mandiri E Cash.
     *
     * @param checkoutToken checkout token.
     * @param callback      callback to be called after transaction was done.
     */
    public void paymentUsingMandiriECash(String checkoutToken,
                                         MidtransCoreCallback<MandiriECashPaymentResponse> callback) {
        snapApiManager.paymentUsingMandiriECash(checkoutToken, PaymentUtilities.buildMandiriECashPaymentRequest(), callback);
    }

    /**
     * Start payment for transaction for specific checkout token using Telkomsel Cash.
     *
     * @param checkoutToken checkout token.
     * @param customer      customer credential.
     * @param callback      callback to be called after transaction was done.
     */
    public void paymentUsingTelkomselCash(String checkoutToken,
                                          String customer,
                                          MidtransCoreCallback<TelkomselCashPaymentResponse> callback) {
        snapApiManager.paymentUsingTelkomselCash(checkoutToken, PaymentUtilities.buildTelkomselCashPaymentRequest(customer), callback);
    }

    /**
     * Start payment for transaction for specific checkout token using Indosat Dompetku.
     *
     * @param checkoutToken checkout token.
     * @param msisdn        customer phone number.
     * @param callback      callback to be called after transaction was done.
     */
    public void paymentUsingIndosatDompetku(String checkoutToken,
                                            String msisdn,
                                            MidtransCoreCallback<IndosatDompetkuPaymentResponse> callback) {
        snapApiManager.paymentUsingIndosatDompetku(checkoutToken, PaymentUtilities.buildIndosatDompetkuPaymentRequest(msisdn), callback);
    }

    /**
     * Start payment for transaction for specific checkout token using XL Tunai.
     *
     * @param checkoutToken checkout token.
     * @param callback      callback to be called after transaction was done.
     */
    public void paymentUsingXlTunai(String checkoutToken,
                                    MidtransCoreCallback<XlTunaiPaymentResponse> callback) {
        snapApiManager.paymentUsingXlTunai(checkoutToken, PaymentUtilities.buildXlTunaiPaymentRequest(), callback);
    }

    /**
     * Start payment for transaction for specific checkout token using Indomaret.
     *
     * @param checkoutToken checkout token.
     * @param callback      callback to be called after transaction was done.
     */
    public void paymentUsingIndomaret(String checkoutToken,
                                      MidtransCoreCallback<IndomaretPaymentResponse> callback) {
        snapApiManager.paymentUsingIndomaret(checkoutToken, PaymentUtilities.buildIndomaretPaymentRequest(), callback);
    }

    /**
     * Start payment for transaction for specific checkout token using Kioson.
     *
     * @param checkoutToken checkout token.
     * @param callback      callback to be called after transaction was done.
     */
    public void paymentUsingKioson(String checkoutToken,
                                   MidtransCoreCallback<KiosonPaymentResponse> callback) {
        snapApiManager.paymentUsingKioson(checkoutToken, PaymentUtilities.buildKiosonPaymentRequest(), callback);
    }

    /**
     * Start payment for transaction for specific checkout token using Gift Card.
     *
     * @param checkoutToken checkout token.
     * @param cardNumber    card number.
     * @param pin           pin number.
     * @param callback      callback to be called after transaction was done.
     */
    public void paymentUsingGiftCard(String checkoutToken,
                                     String cardNumber,
                                     String pin,
                                     MidtransCoreCallback<GiftCardPaymentResponse> callback) {
        snapApiManager.paymentUsingGiftCard(checkoutToken, PaymentUtilities.buildGiftCardPaymentRequest(cardNumber, pin), callback);
    }

    /**
     * Start getting bank bins from Snap.
     *
     * @param callback callback to be called after API call was finished.
     */
    public void getBankBins(MidtransCoreCallback<List<BankBinsResponse>> callback) {
        snapApiManager.getBankBins(callback);
    }

    public Environment getEnvironment() {
        return environment;
    }

    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    /**
     * Midtrans Core SDK builder class.
     */
    public static class Builder {
        private boolean enableLog = true;
        private String clientKey;
        private Environment environment;

        /**
         * Controls the log of SDK. Log can help you to debug application. Set false to disable log
         * of SDK, by default logs are on.
         *
         * @param enableLog flag to enable log.
         * @return {@link Builder} instance.
         */
        public Builder enableLog(boolean enableLog) {
            this.enableLog = enableLog;
            return this;
        }

        /**
         * Set the client key at SDK builder instance.
         *
         * @param clientKey client key.
         * @return {@link Builder} instance.
         */
        public Builder setClientKey(String clientKey) {
            this.clientKey = clientKey;
            return this;
        }

        /**
         * Set environment.
         *
         * @param environment environment.
         * @return {@link Builder} instance.
         */
        public Builder setEnvironment(Environment environment) {
            this.environment = environment;
            return this;
        }

        /**
         * Build Midtrans Core SDK.
         */
        public MidtransCore build() {
            if (clientKey == null || clientKey.equalsIgnoreCase("")) {
                String message = "Client key cannot be null or empty. Please pass the client key using setClientKey()";
                RuntimeException runtimeException = new RuntimeException(message);
                Logger.error(message, runtimeException);
                throw runtimeException;
            }

            if (environment == null) {
                String message = "You must set an environment. Please use setEnvironment(Environment.$ENV)";
                RuntimeException runtimeException = new RuntimeException(message);
                Logger.error(message, runtimeException);
                throw runtimeException;
            }

            return new MidtransCore(this);
        }
    }
}
