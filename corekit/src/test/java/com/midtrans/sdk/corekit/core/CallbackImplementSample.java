package com.midtrans.sdk.corekit.core;

import com.midtrans.sdk.corekit.callback.CardRegistrationCallback;
import com.midtrans.sdk.corekit.callback.CardTokenCallback;
import com.midtrans.sdk.corekit.callback.CheckoutCallback;
import com.midtrans.sdk.corekit.callback.GetCardCallback;
import com.midtrans.sdk.corekit.callback.SaveCardCallback;
import com.midtrans.sdk.corekit.callback.TransactionCallback;
import com.midtrans.sdk.corekit.callback.TransactionOptionsCallback;
import com.midtrans.sdk.corekit.models.CardRegistrationResponse;
import com.midtrans.sdk.corekit.models.CardTokenRequest;
import com.midtrans.sdk.corekit.models.SaveCardRequest;
import com.midtrans.sdk.corekit.models.SaveCardResponse;
import com.midtrans.sdk.corekit.models.TokenDetailsResponse;
import com.midtrans.sdk.corekit.models.TokenRequestModel;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.models.snap.Token;
import com.midtrans.sdk.corekit.models.snap.Transaction;
import com.midtrans.sdk.corekit.models.snap.payment.BankTransferPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.BasePaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.CreditCardPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.IndosatDompetkuPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.KlikBCAPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.MandiriClickPayPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.TelkomselEcashPaymentRequest;

import java.util.ArrayList;

/**
 * Created by ziahaqi on 9/5/16.
 */
public class CallbackImplementSample implements TransactionCallback, CheckoutCallback, TransactionOptionsCallback,
        SaveCardCallback, GetCardCallback, CardRegistrationCallback, CardTokenCallback {

    /**
     * Created by ziahaqi on 25/06/2016.
     */
    CallbackCollaborator callbackCollaborator;
    private SnapTransactionManager snapTransactionManager;
    private SnapRestAPI snapRestAPI;
    private MidtransRestAPI midtransRestAPI;
    private MerchantRestAPI merchantRestAPI;

    public void setTransactionManager(SnapTransactionManager transactionManager, MerchantRestAPI merchantRestAPI,
                                      MidtransRestAPI midtransRestAPI, SnapRestAPI snapRestAPI) {
        this.snapRestAPI = snapRestAPI;
        this.merchantRestAPI = merchantRestAPI;
        this.midtransRestAPI = midtransRestAPI;
        this.snapTransactionManager = transactionManager;
        this.snapTransactionManager.setRestApi(snapRestAPI);
        this.snapTransactionManager.setMerchantPaymentAPI(merchantRestAPI);
        this.snapTransactionManager.setMidtransPaymentAPI(midtransRestAPI);
    }

    @Override
    public void onSuccess(Token token) {
        callbackCollaborator.onCheckoutSuccess();
    }

    @Override
    public void onFailure(Token token, String reason) {
        callbackCollaborator.onCheckoutFailure();
    }

    @Override
    public void onSuccess(TransactionResponse response) {

        callbackCollaborator.onTransactionSuccess();
    }

    @Override
    public void onFailure(TransactionResponse response, String reason) {
        callbackCollaborator.onTransactionFailure();
    }

    @Override
    public void onSuccess(Transaction transaction) {
        callbackCollaborator.onGetPaymentOptionSuccess();
    }

    @Override
    public void onFailure(Transaction transaction, String reason) {
        callbackCollaborator.onGetPaymentOptionFailure();
    }

    @Override
    public void onSuccess(SaveCardResponse response) {
        callbackCollaborator.onSaveCardsSuccess();
    }

    @Override
    public void onSuccess(ArrayList<SaveCardRequest> response) {
        callbackCollaborator.onGetCardsSuccess();
    }

    @Override
    public void onFailure(String reason) {
        callbackCollaborator.onSaveCardsFailure();
    }

    @Override
    public void onSuccess(CardRegistrationResponse response) {
        callbackCollaborator.onCardRegistrationSuccess();
    }

    @Override
    public void onFailure(CardRegistrationResponse response, String reason) {
        callbackCollaborator.onCardRegistrationFailed();
    }

    @Override
    public void onSuccess(TokenDetailsResponse response) {
        callbackCollaborator.onGetCardTokenSuccess();
    }

    @Override
    public void onFailure(TokenDetailsResponse response, String reason) {
        callbackCollaborator.onGetCardTokenFailed();
    }

    @Override
    public void onError(Throwable error) {
        callbackCollaborator.onError();
    }


    public void checkout(TokenRequestModel tokenRequestModel) {
        snapTransactionManager.checkout(tokenRequestModel, this);
    }

    public void getPaymentOption(String token) {
        snapTransactionManager.getTransactionOptions(token, this);
    }

    public void paymentUsingCreditCard(CreditCardPaymentRequest request, String token) {
        snapTransactionManager.paymentUsingCreditCard(token, request, this);
    }

    public void paymentUsingSnapBankTransferBCA(String tokenId, BankTransferPaymentRequest request) {
        snapTransactionManager.paymentUsingBankTransferBCA(tokenId, request, this);
    }

    public void paymentUsingSnapBankTransferPermata(String token, BankTransferPaymentRequest request) {
        snapTransactionManager.paymentUsingBankTransferPermata(token, request, this);
    }

    public void paymentUsingKlikBCA(String token, KlikBCAPaymentRequest request) {
        snapTransactionManager.paymentUsingKlikBCA(token, request, this);
    }

    public void paymentUsingBCAKlikpay(String token,BasePaymentRequest request) {
        snapTransactionManager.paymentUsingBCAKlikpay(token,request, this);
    }

    public void paymentUsingSnapMandiriBillpay(String token, BankTransferPaymentRequest request) {
        snapTransactionManager.paymentUsingMandiriBillPay(token, request, this);
    }

    public void paymentUsingSnapMandiriClickPay(String token, MandiriClickPayPaymentRequest request) {
        snapTransactionManager.paymentUsingMandiriClickPay(token, request, this);
    }

    public void paymentUsingSnapCIMBClick(String token,BasePaymentRequest request) {
        snapTransactionManager.paymentUsingCIMBClick(token, request, this);
    }

    public void paymentUsingSnapBRIEpay(String token, BasePaymentRequest request) {
        snapTransactionManager.paymentUsingBRIEpay(token, request, this);
    }

    public void paymentUsingSnapMandirEcash(String token, BasePaymentRequest request) {
        snapTransactionManager.paymentUsingMandiriEcash(token, request, this);
    }

    public void paymentUsingSnapTelkomselEcash(String token, TelkomselEcashPaymentRequest request) {
        snapTransactionManager.paymentUsingTelkomselCash(token, request, this);
    }

    public void paymentUsingSnapXLTunai(String token, BasePaymentRequest request) {
        snapTransactionManager.paymentUsingXLTunai(token, request, this);
    }

    public void paymentUsingSnapIndomaret(String token, BasePaymentRequest request) {
        snapTransactionManager.paymentUsingIndomaret(token, request, this);
    }

    public void paymentUsingSnapIndosatDompetku(String token, IndosatDompetkuPaymentRequest request) {
        snapTransactionManager.paymentUsingIndosatDompetku(token, request, this);
    }

    public void paymentUsingSnapKiosan(String token, BasePaymentRequest request) {
        snapTransactionManager.paymentUsingKiosan(token, request, this);
    }

    public void paymentUsingSnapBankTransferAllBank(String token, BankTransferPaymentRequest request) {
        snapTransactionManager.paymentUsingBankTransferAllBank(token, request, this);
    }

    public void saveCards(String userId, ArrayList<SaveCardRequest> requests) {
        snapTransactionManager.saveCards(userId, requests, this);
    }

    public void snapGetCards(String sampleUserId) {
        snapTransactionManager.getCards(sampleUserId, this);
    }

    public void cardRegistration(String cardNumber, String cardCvv, String cardExpMonth, String cardExpYear, String clientkey) {
        snapTransactionManager.cardRegistration(cardNumber, cardCvv, cardExpMonth, cardExpYear, clientkey, this);
    }

    public void getToken(CardTokenRequest cardTokenRequest) {
        snapTransactionManager.getToken(cardTokenRequest, this);
    }

    public CheckoutCallback getCheckoutCallback() {
        return this;
    }

    public TransactionOptionsCallback getTransactionOptionCallback() {
        return this;
    }

    public TransactionCallback getTransactionCallback() {
        return this;
    }

    public CardTokenCallback getCardTokenCallback() {
        return this;
    }
}
