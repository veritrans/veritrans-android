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

    public void paymentUsingCreditCard(CreditCardPaymentRequest request) {
        snapTransactionManager.paymentUsingCreditCard(request, this);
    }

    public void paymentUsingSnapBankTransferBCA(BankTransferPaymentRequest request) {
        snapTransactionManager.paymentUsingBankTransferBCA(request, this);
    }

    public void paymentUsingSnapBankTransferPermata(BankTransferPaymentRequest request) {
        snapTransactionManager.paymentUsingBankTransferPermata(request, this);
    }

    public void paymentUsingKlikBCA(KlikBCAPaymentRequest request) {
        snapTransactionManager.paymentUsingKlikBCA(request, this);
    }

    public void paymentUsingBCAKlikpay(BasePaymentRequest request) {
        snapTransactionManager.paymentUsingBCAKlikpay(request, this);
    }

    public void paymentUsingSnapMandiriBillpay(BankTransferPaymentRequest request) {
        snapTransactionManager.paymentUsingMandiriBillPay(request, this);
    }

    public void paymentUsingSnapMandiriClickPay(MandiriClickPayPaymentRequest request) {
        snapTransactionManager.paymentUsingMandiriClickPay(request, this);
    }

    public void paymentUsingSnapCIMBClick(BasePaymentRequest request) {
        snapTransactionManager.paymentUsingCIMBClick(request, this);
    }

    public void paymentUsingSnapBRIEpay(BasePaymentRequest request) {
        snapTransactionManager.paymentUsingBRIEpay(request, this);
    }

    public void paymentUsingSnapMandirEcash(BasePaymentRequest request) {
        snapTransactionManager.paymentUsingMandiriEcash(request, this);
    }

    public void paymentUsingSnapTelkomselEcash(TelkomselEcashPaymentRequest request) {
        snapTransactionManager.paymentUsingTelkomselCash(request, this);
    }

    public void paymentUsingSnapXLTunai(BasePaymentRequest request) {
        snapTransactionManager.paymentUsingXLTunai(request, this);
    }

    public void paymentUsingSnapIndomaret(BasePaymentRequest request) {
        snapTransactionManager.paymentUsingIndomaret(request, this);
    }

    public void paymentUsingSnapIndosatDompetku(IndosatDompetkuPaymentRequest request) {
        snapTransactionManager.paymentUsingIndosatDompetku(request, this);
    }

    public void paymentUsingSnapKiosan(BasePaymentRequest request) {
        snapTransactionManager.paymentUsingKiosan(request, this);
    }

    public void paymentUsingSnapBankTransferAllBank(BankTransferPaymentRequest request) {
        snapTransactionManager.paymentUsingBankTransferAllBank(request, this);
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
