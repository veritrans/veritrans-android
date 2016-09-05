package id.co.veritrans.sdk.coreflow.core;

import java.util.ArrayList;

import id.co.veritrans.sdk.coreflow.callback.CardRegistrationCallback;
import id.co.veritrans.sdk.coreflow.callback.CheckoutCallback;
import id.co.veritrans.sdk.coreflow.callback.GetCardCallback;
import id.co.veritrans.sdk.coreflow.callback.GetCardTokenCallback;
import id.co.veritrans.sdk.coreflow.callback.PaymentOptionCallback;
import id.co.veritrans.sdk.coreflow.callback.SaveCardCallback;
import id.co.veritrans.sdk.coreflow.callback.TransactionCallback;
import id.co.veritrans.sdk.coreflow.models.CardRegistrationResponse;
import id.co.veritrans.sdk.coreflow.models.CardTokenRequest;
import id.co.veritrans.sdk.coreflow.models.SaveCardRequest;
import id.co.veritrans.sdk.coreflow.models.SaveCardResponse;
import id.co.veritrans.sdk.coreflow.models.TokenDetailsResponse;
import id.co.veritrans.sdk.coreflow.models.TokenRequestModel;
import id.co.veritrans.sdk.coreflow.models.TransactionResponse;
import id.co.veritrans.sdk.coreflow.models.snap.Token;
import id.co.veritrans.sdk.coreflow.models.snap.Transaction;
import id.co.veritrans.sdk.coreflow.models.snap.payment.BankTransferPaymentRequest;
import id.co.veritrans.sdk.coreflow.models.snap.payment.BasePaymentRequest;
import id.co.veritrans.sdk.coreflow.models.snap.payment.CreditCardPaymentRequest;
import id.co.veritrans.sdk.coreflow.models.snap.payment.IndosatDompetkuPaymentRequest;
import id.co.veritrans.sdk.coreflow.models.snap.payment.KlikBCAPaymentRequest;
import id.co.veritrans.sdk.coreflow.models.snap.payment.MandiriClickPayPaymentRequest;
import id.co.veritrans.sdk.coreflow.models.snap.payment.TelkomselEcashPaymentRequest;

/**
 * Created by ziahaqi on 9/5/16.
 */
public class CallbackImplementSample implements TransactionCallback, CheckoutCallback, PaymentOptionCallback,
        SaveCardCallback, GetCardCallback, CardRegistrationCallback, GetCardTokenCallback{
        public String onsuccessStatusCode;
    /**
     * Created by ziahaqi on 25/06/2016.
     */
    CallbackCollaborator callbackCollaborator;
    private SnapTransactionManager snapTransactionManager;
    private SnapRestAPI snapRestAPI;
    private VeritransRestAPI veritransRestAPI;
    private MerchantRestAPI merchantRestAPI;

    public void setTransactionManager(SnapTransactionManager transactionManager, MerchantRestAPI merchantRestAPI,
                                      VeritransRestAPI veritransRestAPI, SnapRestAPI snapRestAPI) {
        this.snapRestAPI = snapRestAPI;
        this.merchantRestAPI = merchantRestAPI;
        this.veritransRestAPI = veritransRestAPI;
        this.snapTransactionManager = transactionManager;
        this.snapTransactionManager.setRestApi(snapRestAPI);
        this.snapTransactionManager.setMerchantPaymentAPI(merchantRestAPI);
        this.snapTransactionManager.setVeritransPaymentAPI(veritransRestAPI);
    }

//
//
//        /*
//         * Card Registration Callback stuff
//         */
//
//    @Override
//    @Subscribe
//    public void onEvent(CardRegistrationSuccessEvent event) {
//        callbackCollaborator.onCardRegistrationSuccess();
//        onsuccessStatusCode = event.getResponse().getStatusCode();
//    }
//
//    @Subscribe
//    @Override
//    public void onEvent(CardRegistrationFailedEvent event) {
//        callbackCollaborator.onCardRegistrationFailed();
//    }
//
//    @Override
//    @Subscribe
//    public void onEvent(NetworkUnavailableEvent event) {
//        callbackCollaborator.onNetworkUnAvailable();
//    }
//
//    @Override
//    @Subscribe
//    public void onEvent(GeneralErrorEvent event) {
//        callbackCollaborator.onGeneralErrorEvent();
//    }
//
//    /*
//     * token bus callback stuff
//     */
//    @Subscribe
//    @Override
//    public void onEvent(GetTokenSuccessEvent event) {
//        callbackCollaborator.onGetTokenSuccessEvent();
//    }
//
//    @Subscribe
//    @Override
//    public void onEvent(GetTokenFailedEvent event) {
//        callbackCollaborator.onGetTokenFailedEvent();
//    }
//
//
//    /*
//     * transaction bus callbaack
//     */
//    @Subscribe
//    @Override
//    public void onEvent(TransactionSuccessEvent event) {
//        callbackCollaborator.onTransactionSuccessEvent();
//    }
//
//    @Subscribe
//    @Override
//    public void onEvent(TransactionFailedEvent event) {
//        callbackCollaborator.onTransactionFailedEvent();
//    }
//
//
//    @Subscribe
//    @Override
//    public void onEvent(AuthenticationEvent event) {
//        callbackCollaborator.onAuthenticationEvent();
//    }
//
//    public void getOffers(MerchantRestAPI merchantRestAPIMock, String mToken) {
//        transactionManager.setMerchantPaymentAPI(merchantRestAPIMock);
//        transactionManager.getOffers(mToken);
//    }
//
//    @Override
//    @Subscribe
//    public void onEvent(GetOfferSuccessEvent event) {
//        callbackCollaborator.onGetOfferSuccesEvent();
//
//    }
//
//    @Override
//    @Subscribe
//    public void onEvent(GetOfferFailedEvent event) {
//        callbackCollaborator.onGetOfferFailedEvent();
//    }
//
//    public void deleteCard(MerchantRestAPI merchantRestAPIMock, SaveCardRequest requestModel, String mToken) {
//        transactionManager.setMerchantPaymentAPI(merchantRestAPIMock);
//        transactionManager.deleteCard(requestModel, mToken);
//    }
//
//    @Override
//    @Subscribe
//    public void onEvent(DeleteCardSuccessEvent event) {
//        callbackCollaborator.onDeleteCardSuccessEvent();
//    }
//
//    @Override
//    @Subscribe
//    public void onEvent(DeleteCardFailedEvent event) {
//        callbackCollaborator.onDeleteCardFailedEvent();
//    }
//
//    public void getCards(MerchantRestAPI merchantRestAPIMock, String mToken) {
//        transactionManager.setMerchantPaymentAPI(merchantRestAPIMock);
//        transactionManager.getCards(mToken);
//    }
//
//    @Subscribe
//    @Override
//    public void onEvent(GetCardsSuccessEvent event) {
//        callbackCollaborator.onGetCardSuccess();
//    }
//
//    @Subscribe
//    @Override
//    public void onEvent(GetCardFailedEvent event) {
//        callbackCollaborator.onGetCardFailed();
//    }
//
//    public void saveCard(MerchantRestAPI merchantRestAPIMock, SaveCardRequest request, String mToken) {
//        transactionManager.setMerchantPaymentAPI(merchantRestAPIMock);
//        transactionManager.saveCards(request, mToken);
//    }
//
//    @Override
//    @Subscribe
//    public void onEvent(SaveCardSuccessEvent event) {
//        callbackCollaborator.onSaveCardSuccessEvent();
//    }
//
//    @Override
//    @Subscribe
//    public void onEvent(SaveCardFailedEvent event) {
//        callbackCollaborator.onsaveCardFailedEvent();
//    }
//
//    @Override
//    @Subscribe
//    public void onEvent(SSLErrorEvent errorEvent) {
//        callbackCollaborator.onSSLErrorEvent();
//    }
//
//
//
//    @Subscribe
//    @Override
//    public void onEvent(GetSnapTokenSuccessEvent event) {
//        callbackCollaborator.onGetSnapTokenSuccess();
//    }
//
//    @Subscribe
//    @Override
//    public void onEvent(GetSnapTokenFailedEvent event) {
//        callbackCollaborator.onGetSnapTokenFailed();
//    }
//
//    @Subscribe
//    @Override
//    public void onEvent(GetSnapTransactionSuccessEvent event) {
//        callbackCollaborator.onGetSnapTransactionSuccess();
//    }
//
//    @Subscribe
//    @Override
//    public void onEvent(GetSnapTransactionFailedEvent event) {
//        callbackCollaborator.onGetSnapTransactionFailed();
//    }
//    public void getSnapToken(MerchantRestAPI merchantRestAPI, TokenRequestModel model) {
//
//        snapTransactionManager.setMerchantPaymentAPI(merchantRestAPI);
//        snapTransactionManager.checkout(model);
//
//    }
//
//
//    @Subscribe
//    @Override
//    public void onEvent(id.co.veritrans.sdk.coreflow.eventbus.events.snap.GetCardsSuccessEvent event) {
//        callbackCollaborator.onGetCardsSnapSuccess();
//
//    }
//
//    @Subscribe
//    @Override
//    public void onEvent(GetCardsFailedEvent event) {
//        callbackCollaborator.onGetCardsSnapFailed();
//    }

    // snap

//    public void getPaymentType(SnapRestAPI restAPI, String snapToken) {
//        snapTransactionManager.setRestApi(restAPI);
//        snapTransactionManager.getTransactionOptions(snapToken);
//    }
//
//    public void paymentUsingSnapCreditCard(SnapRestAPI snapAPI, CreditCardPaymentRequest request) {
//        snapTransactionManager.setRestApi(snapAPI);
//        snapTransactionManager.paymentUsingCreditCard(request);
//    }
//
//    public void paymentUsingSnapBankTransferBCA(SnapRestAPI snapAPI, BankTransferPaymentRequest request) {
//        snapTransactionManager.setRestApi(snapAPI);
//        snapTransactionManager.paymentUsingBankTransferBCA(request);
//    }
//
//    public void paymentUsingSnapBankTransferPermata(SnapRestAPI snapAPI, BankTransferPaymentRequest request) {
//        snapTransactionManager.setRestApi(snapAPI);
//        snapTransactionManager.paymentUsingBankTransferPermata(request);
//    }
//
//    public void paymentUsingKlikBCA(SnapRestAPI snapAPI, KlikBCAPaymentRequest request) {
//        snapTransactionManager.setRestApi(snapAPI);
//        snapTransactionManager.paymentUsingKlikBCA(request);
//    }
//
//    public void paymentUsingBCAKlikpay(SnapRestAPI snapAPI, BasePaymentRequest request) {
//        snapTransactionManager.setRestApi(snapAPI);
//        snapTransactionManager.paymentUsingBCAKlikpay(request);
//    }
//
//    public void paymentUsingSnapMandiriBillpay(SnapRestAPI snapAPI, BankTransferPaymentRequest request) {
//        snapTransactionManager.setRestApi(snapAPI);
//        snapTransactionManager.paymentUsingMandiriBillPay(request);
//    }
//
//    public void paymentUsingSnapMandiriClickPay(SnapRestAPI snapAPI, MandiriClickPayPaymentRequest request) {
//        snapTransactionManager.setRestApi(snapAPI);
//        snapTransactionManager.paymentUsingMandiriClickPay(request);
//    }
//
//    public void paymentUsingSnapCIMBClick(SnapRestAPI snapAPI, BasePaymentRequest request) {
//        snapTransactionManager.setRestApi(snapAPI);
//        snapTransactionManager.paymentUsingCIMBClick(request);
//    }
//
//    public void paymentUsingSnapBRIEpay(SnapRestAPI snapAPI, BasePaymentRequest request) {
//        snapTransactionManager.setRestApi(snapAPI);
//        snapTransactionManager.paymentUsingBRIEpay(request);
//    }
//
//    public void paymentUsingSnapMandirEcash(SnapRestAPI snapAPI, BasePaymentRequest request) {
//        snapTransactionManager.setRestApi(snapAPI);
//        snapTransactionManager.paymentUsingMandiriEcash(request);
//    }
//
//    public void paymentUsingSnapTelkomselEcash(SnapRestAPI snapAPI, TelkomselEcashPaymentRequest request) {
//        snapTransactionManager.setRestApi(snapAPI);
//        snapTransactionManager.paymentUsingTelkomselCash(request);
//    }
//
//    public void paymentUsingSnapXLTunai(SnapRestAPI snapAPI, BasePaymentRequest request) {
//        snapTransactionManager.setRestApi(snapAPI);
//        snapTransactionManager.paymentUsingXLTunai(request);
//    }
//
//    public void paymentUsingSnapIndomaret(SnapRestAPI snapAPI, BasePaymentRequest request) {
//        snapTransactionManager.setRestApi(snapAPI);
//        snapTransactionManager.paymentUsingIndomaret(request);
//    }
//
//    public void paymentUsingSnapIndosatDompetku(SnapRestAPI snapAPI, IndosatDompetkuPaymentRequest request) {
//        snapTransactionManager.setRestApi(snapAPI);
//        snapTransactionManager.paymentUsingIndosatDompetku(request);
//    }
//
//    public void paymentUsingSnapKiosan(SnapRestAPI snapAPI, BasePaymentRequest request) {
//        snapTransactionManager.setRestApi(snapAPI);
//        snapTransactionManager.paymentUsingKiosan(request);
//    }
//
//    public void paymentUsingSnapBankTransferAllBank(SnapRestAPI snapAPI, BankTransferPaymentRequest request) {
//        snapTransactionManager.setRestApi(snapAPI);
//        snapTransactionManager.paymentUsingBankTransferAllBank(request);
//    }
//
//    public void snapSaveCard(MerchantRestAPI restAPI, String userId, ArrayList<SaveCardRequest> requests) {
//        snapTransactionManager.setMerchantPaymentAPI(restAPI);
//        snapTransactionManager.saveCards(userId, requests);
//    }
//
//    public void snapGetCards(MerchantRestAPI merchantApi, String sampleUserId) {
//        snapTransactionManager.setMerchantPaymentAPI(merchantApi);
//        snapTransactionManager.getCards(sampleUserId);
//    }

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
}
