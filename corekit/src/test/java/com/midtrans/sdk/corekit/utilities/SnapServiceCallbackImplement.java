package com.midtrans.sdk.corekit.utilities;

import com.midtrans.sdk.corekit.callback.BankBinsCallback;
import com.midtrans.sdk.corekit.callback.BanksPointCallback;
import com.midtrans.sdk.corekit.callback.CheckoutCallback;
import com.midtrans.sdk.corekit.callback.DeleteCardCallback;
import com.midtrans.sdk.corekit.callback.GetTransactionStatusCallback;
import com.midtrans.sdk.corekit.callback.TransactionCallback;
import com.midtrans.sdk.corekit.callback.TransactionOptionsCallback;
import com.midtrans.sdk.corekit.core.SnapServiceManager;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.models.snap.BankBinsResponse;
import com.midtrans.sdk.corekit.models.snap.BanksPointResponse;
import com.midtrans.sdk.corekit.models.snap.Token;
import com.midtrans.sdk.corekit.models.snap.Transaction;
import com.midtrans.sdk.corekit.models.snap.TransactionStatusResponse;
import com.midtrans.sdk.corekit.models.snap.payment.BankTransferPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.BasePaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.CreditCardPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.DanamonOnlinePaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.GCIPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.GoPayPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.IndosatDompetkuPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.KlikBCAPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.NewMandiriClickPayPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.TelkomselEcashPaymentRequest;

import java.util.ArrayList;

/**
 * Created by ziahaqi on 4/2/18.
 */

public class SnapServiceCallbackImplement implements TransactionCallback, CheckoutCallback,
        TransactionOptionsCallback, DeleteCardCallback, BankBinsCallback, BanksPointCallback,
        GetTransactionStatusCallback {
    CallbackCollaborator callbackCollaborator;
    SnapServiceManager serviceManager;

    public SnapServiceCallbackImplement(SnapServiceManager serviceManager, CallbackCollaborator callbackCollaborator) {
        this.callbackCollaborator = callbackCollaborator;
        this.serviceManager = serviceManager;
    }


    @Override
    public void onError(Throwable error) {
        callbackCollaborator.onError();
    }

    @Override
    public void onSuccess(Token token) {

    }

    @Override
    public void onFailure(Token token, String reason) {

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
    public void onSuccess(Void object) {
        callbackCollaborator.onDeleteCardSuccess();
    }

    @Override
    public void onFailure(Void object) {
        callbackCollaborator.onDeleteCardFailure();
    }

    @Override
    public void onSuccess(ArrayList<BankBinsResponse> response) {
        callbackCollaborator.onGetBankBinSuccess();
    }

    @Override
    public void onSuccess(BanksPointResponse response) {
        callbackCollaborator.onGetbanksPointSuccess();
    }

    @Override
    public void onFailure(String reason) {
        callbackCollaborator.onGetBankBinFailure();
    }

    @Override
    public void onSuccess(TransactionStatusResponse response) {
        this.callbackCollaborator.onGetTransactionStatatusSuccess();
    }

    @Override
    public void onFailure(TransactionStatusResponse response, String reason) {
        this.callbackCollaborator.onGetTransactionStatatusFailure();
    }

    public void getTransactionOptions(String tokenId) {
        this.serviceManager.getTransactionOptions(tokenId, this);
    }

    public void paymentUsingCreditCard(String snapToken, CreditCardPaymentRequest creditCardRequest) {
        this.serviceManager.paymentUsingCreditCard(snapToken, creditCardRequest, this);
    }

    public void paymentUsingVa(String snapToken, BankTransferPaymentRequest request) {
        this.serviceManager.paymentUsingVa(snapToken, request, this);
    }

    public void paymentBaseMethod(String snapToken, BasePaymentRequest request) {
        this.serviceManager.paymentUsingBaseMethod(snapToken, request, this);
    }

    public void paymentUsingMandiriClickPay(String snapToken, NewMandiriClickPayPaymentRequest request) {
        this.serviceManager.paymentUsingMandiriClickPay(snapToken, request, this);
    }

    public void paymentUsingTCash(String snapToken, TelkomselEcashPaymentRequest request) {
        this.serviceManager.paymentUsingTelkomselCash(snapToken, request, this);
    }

    public void paymentUsingIndosatDomputku(String snapToken, IndosatDompetkuPaymentRequest request) {
        this.serviceManager.paymentUsingIndosatDompetku(snapToken, request, this);
    }

    public void paymentUsingGci(String snapToken, GCIPaymentRequest request) {
        this.serviceManager.paymentUsingGci(snapToken, request, this);
    }

    public void paymentUsingKlikBca(String snapToken, KlikBCAPaymentRequest request) {
        this.serviceManager.paymentUsingKlikBca(snapToken, request, this);
    }

    public void paymentUsingGopay(String snapToken, GoPayPaymentRequest request) {
        this.serviceManager.paymentUsingGoPay(snapToken, request, this);
    }

    public void paymentUsingDanamonOnline(String snapToken, DanamonOnlinePaymentRequest request) {
        this.serviceManager.paymentUsingDanamonOnline(snapToken, request, this);
    }

    public void deleteSavedCard(String snapToken, String maskedCardNumber) {
        this.serviceManager.deleteCard(snapToken, maskedCardNumber, this);
    }

    public void getBankBins() {
        this.serviceManager.getBankBins(this);
    }

    public void getBankPoints(String snapToken, String cardToken) {
        this.serviceManager.getBanksPoint(snapToken, cardToken, this);
    }

    public void getTransactionStatus(String snapToken) {
        this.serviceManager.getTransactionStatus(snapToken, this);
    }

    public TransactionCallback getTransactionCallback(){
        return this;
    }

    public TransactionOptionsCallback getTransactionOptionsCallback(){
        return this;
    }

    public BankBinsCallback getBankBinCallback() {
        return this;
    }

    public BanksPointCallback getbankPointCallback() {
        return this;
    }

    public DeleteCardCallback getDeleteCardCallback() {
        return this;
    }
}
