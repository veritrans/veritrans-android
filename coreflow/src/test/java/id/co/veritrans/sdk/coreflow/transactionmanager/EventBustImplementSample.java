package id.co.veritrans.sdk.coreflow.transactionmanager;

import org.greenrobot.eventbus.Subscribe;

import id.co.veritrans.sdk.coreflow.core.MerchantRestAPI;
import id.co.veritrans.sdk.coreflow.core.TransactionManager;
import id.co.veritrans.sdk.coreflow.core.VeritransRestAPI;
import id.co.veritrans.sdk.coreflow.eventbus.bus.VeritransBus;
import id.co.veritrans.sdk.coreflow.eventbus.bus.VeritransBusProvider;
import id.co.veritrans.sdk.coreflow.eventbus.callback.CardRegistrationBusCallback;
import id.co.veritrans.sdk.coreflow.eventbus.callback.DeleteCardBusCallback;
import id.co.veritrans.sdk.coreflow.eventbus.callback.GetAuthenticationBusCallback;
import id.co.veritrans.sdk.coreflow.eventbus.callback.GetCardBusCallback;
import id.co.veritrans.sdk.coreflow.eventbus.callback.GetOfferBusCallback;
import id.co.veritrans.sdk.coreflow.eventbus.callback.HttpErrorCallback;
import id.co.veritrans.sdk.coreflow.eventbus.callback.SaveCardBusCallback;
import id.co.veritrans.sdk.coreflow.eventbus.callback.TokenBusCallback;
import id.co.veritrans.sdk.coreflow.eventbus.callback.TransactionBusCallback;
import id.co.veritrans.sdk.coreflow.eventbus.events.AuthenticationEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.CardRegistrationFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.CardRegistrationSuccessEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.DeleteCardFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.DeleteCardSuccessEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.GeneralErrorEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.GetCardFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.GetCardsSuccessEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.GetOfferFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.GetOfferSuccessEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.GetTokenFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.GetTokenSuccessEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.NetworkUnavailableEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.SSLErrorEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.SaveCardFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.SaveCardSuccessEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.TransactionFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.TransactionSuccessEvent;
import id.co.veritrans.sdk.coreflow.models.BBMMoneyRequestModel;
import id.co.veritrans.sdk.coreflow.models.BCABankTransfer;
import id.co.veritrans.sdk.coreflow.models.BCAKlikPayModel;
import id.co.veritrans.sdk.coreflow.models.CIMBClickPayModel;
import id.co.veritrans.sdk.coreflow.models.CardTokenRequest;
import id.co.veritrans.sdk.coreflow.models.CardTransfer;
import id.co.veritrans.sdk.coreflow.models.EpayBriTransfer;
import id.co.veritrans.sdk.coreflow.models.GetOffersResponseModel;
import id.co.veritrans.sdk.coreflow.models.IndomaretRequestModel;
import id.co.veritrans.sdk.coreflow.models.IndosatDompetku;
import id.co.veritrans.sdk.coreflow.models.IndosatDompetkuRequest;
import id.co.veritrans.sdk.coreflow.models.KlikBCAModel;
import id.co.veritrans.sdk.coreflow.models.MandiriBillPayTransferModel;
import id.co.veritrans.sdk.coreflow.models.MandiriClickPayRequestModel;
import id.co.veritrans.sdk.coreflow.models.MandiriECashModel;
import id.co.veritrans.sdk.coreflow.models.PermataBankTransfer;
import id.co.veritrans.sdk.coreflow.models.SaveCardRequest;

/**
 * Created by ziahaqi on 25/06/2016.
 */
public class EventBustImplementSample implements GetAuthenticationBusCallback, DeleteCardBusCallback,
        CardRegistrationBusCallback, SaveCardBusCallback, HttpErrorCallback,
        TokenBusCallback, TransactionBusCallback, GetCardBusCallback,
        GetOfferBusCallback {
    BusCollaborator busCollaborator;
    public String onsuccessStatusCode;
    private TransactionManager transactionManager;
    private VeritransBus bus;


    public void registerBus(VeritransBus veritransBus) {
        VeritransBus.setBus(veritransBus);
        VeritransBusProvider.getInstance().register(this);
    }

    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void cardRegistration(VeritransRestAPI registered, String cardNumber, String cardCvv, String cardExpMonth, String cardExpYear, String authtoken) throws InterruptedException {

        transactionManager.setVeritransPaymentAPI(registered);
        transactionManager.cardRegistration(cardNumber,
                cardCvv,
                cardExpMonth,
                cardExpYear, authtoken);
    }

    public VeritransBus getBus(){
        return this.bus;
    }

    public void getToken(VeritransRestAPI veritransRestAPI, CardTokenRequest cardTokenRequest) {
        transactionManager.setVeritransPaymentAPI(veritransRestAPI);
        transactionManager.getToken(cardTokenRequest);
    }

    public void paymentUsingPermataBank(MerchantRestAPI merchantRestAPI, PermataBankTransfer transfer, String token) {
        transactionManager.setMerchantPaymentAPI(merchantRestAPI);
        transactionManager.paymentUsingPermataBank(transfer, token);
    }


    public void paymentUsingPermataBCA(MerchantRestAPI merchantRestAPI, BCABankTransfer transfer, String mToken) {
        transactionManager.setMerchantPaymentAPI(merchantRestAPI);
        transactionManager.paymentUsingBCATransfer(transfer, mToken);
    }

    public void paymentUsingCard(MerchantRestAPI merchantRestAPIMock, String xAuth, CardTransfer transfer, String mToken) {
        transactionManager.setMerchantPaymentAPI(merchantRestAPIMock);
        transactionManager.paymentUsingCard(transfer, mToken);
    }

    public void paymentUsingMandiriClickPay(MerchantRestAPI merchantRestAPIMock, String xAuth, MandiriClickPayRequestModel requestModel, String mToken) {
        transactionManager.setMerchantPaymentAPI(merchantRestAPIMock);
        transactionManager.paymentUsingMandiriClickPay(requestModel, xAuth);
    }

    public void paymentUsingBCAClickPay(MerchantRestAPI merchantRestAPIMock, String xAuth, BCAKlikPayModel requestModel, String mToken) {
        transactionManager.setMerchantPaymentAPI(merchantRestAPIMock);
        transactionManager.paymentUsingBCAKlikPay(requestModel, xAuth);
    }

    public void paymentUsingMandiriBillpay(MerchantRestAPI merchantRestAPIMock, String xAuth, MandiriBillPayTransferModel requestModel, String mToken) {
        transactionManager.setMerchantPaymentAPI(merchantRestAPIMock);
        transactionManager.paymentUsingMandiriBillPay(requestModel, xAuth);
    }

    public void paymentUsingCIMBPay(MerchantRestAPI merchantRestAPIMock, String xAuth, CIMBClickPayModel requestModel, String mToken) {
        transactionManager.setMerchantPaymentAPI(merchantRestAPIMock);
        transactionManager.paymentUsingCIMBPay(requestModel, xAuth);
    }



    public void paymentUsingIndosatDompetku(MerchantRestAPI merchantRestAPIMock, IndosatDompetkuRequest requestModel, String mToken) {
        transactionManager.setMerchantPaymentAPI(merchantRestAPIMock);
        transactionManager.paymentUsingIndosatDompetku(requestModel, mToken);
    }

    public void paymentUsingIndomaret(MerchantRestAPI merchantRestAPIMock, IndomaretRequestModel requestModel, String mToken) {
        transactionManager.setMerchantPaymentAPI(merchantRestAPIMock);
        transactionManager.paymentUsingIndomaret(requestModel, mToken);
    }

    public void paymentUsingBBMMoney(MerchantRestAPI merchantRestAPIMock, BBMMoneyRequestModel requestModel, String mToken) {
        transactionManager.setMerchantPaymentAPI(merchantRestAPIMock);
        transactionManager.paymentUsingBBMMoney(requestModel, mToken);
    }

    public void paymentUsingClickBCAModel(MerchantRestAPI merchantRestAPIMock, KlikBCAModel requestModel, String mToken) {
        transactionManager.setMerchantPaymentAPI(merchantRestAPIMock);
        transactionManager.paymentUsingKlikBCA( requestModel);
    }

    public void getAuthenticationToken(MerchantRestAPI veritransRestAPIMock) {
        transactionManager.setMerchantPaymentAPI(veritransRestAPIMock);
        transactionManager.getAuthenticationToken();
    }

    public void paymentUsingMandiriEcashPay(MerchantRestAPI merchantRestAPIMock, String xAuth, MandiriECashModel requestModel, String mToken) {
        transactionManager.setMerchantPaymentAPI(merchantRestAPIMock);
        transactionManager.paymentUsingMandiriECash(requestModel, xAuth);
    }

    public void paymentUsingBriEpay(MerchantRestAPI merchantRestAPIMock, String xAuth, EpayBriTransfer requestModel, String mToken) {
        transactionManager.setMerchantPaymentAPI(merchantRestAPIMock);
        transactionManager.paymentUsingEpayBri(requestModel, xAuth);
    }


    /*
     * Card Registration Callback stuff
     */

    @Override
    @Subscribe
    public void onEvent(CardRegistrationSuccessEvent event) {
        busCollaborator.onCardRegistrationSuccess();
        onsuccessStatusCode = event.getResponse().getStatusCode();
    }

    @Subscribe
    @Override
    public void onEvent(CardRegistrationFailedEvent event) {
        busCollaborator.onCardRegistrationFailed();
    }

    @Override
    @Subscribe
    public void onEvent(NetworkUnavailableEvent event) {
        busCollaborator.onNetworkUnAvailable();
    }

    @Override
    @Subscribe
    public void onEvent(GeneralErrorEvent event) {
        busCollaborator.onGeneralErrorEvent();
    }

    /*
     * token bus callback stuff
     */
    @Subscribe
    @Override
    public void onEvent(GetTokenSuccessEvent event) {
        busCollaborator.onGetTokenSuccessEvent();
    }

    @Subscribe
    @Override
    public void onEvent(GetTokenFailedEvent event) {
        busCollaborator.onGetTokenFailedEvent();
    }


    /*
     * transaction bus callbaack
     */
    @Subscribe
    @Override
    public void onEvent(TransactionSuccessEvent event) {
        busCollaborator.onTransactionSuccessEvent();
    }

    @Subscribe
    @Override
    public void onEvent(TransactionFailedEvent event) {
        busCollaborator.onTransactionFailedEvent();
    }


    @Subscribe
    @Override
    public void onEvent(AuthenticationEvent event) {
        busCollaborator.onAuthenticationEvent();
    }

    public void getOffers(MerchantRestAPI merchantRestAPIMock,  String mToken) {
        transactionManager.setMerchantPaymentAPI(merchantRestAPIMock);
        transactionManager.getOffers(mToken);
    }

    @Override
    @Subscribe
    public void onEvent(GetOfferSuccessEvent event) {
        busCollaborator.onGetOfferSuccesEvent();

    }

    @Override
    @Subscribe
    public void onEvent(GetOfferFailedEvent event) {
        busCollaborator.onGetOfferFailedEvent();
    }

    public void deleteCard(MerchantRestAPI merchantRestAPIMock, SaveCardRequest requestModel, String mToken) {
        transactionManager.setMerchantPaymentAPI(merchantRestAPIMock);
        transactionManager.deleteCard(requestModel, mToken);
    }

    @Override
    @Subscribe
    public void onEvent(DeleteCardSuccessEvent event) {
        busCollaborator.onDeleteCardSuccessEvent();
    }

    @Override
    @Subscribe
    public void onEvent(DeleteCardFailedEvent event) {
        busCollaborator.onDeleteCardFailedEvent();
    }

    public void getCards(MerchantRestAPI merchantRestAPIMock, String mToken) {
        transactionManager.setMerchantPaymentAPI(merchantRestAPIMock);
        transactionManager.getCards(mToken);
    }

    @Subscribe
    @Override
    public void onEvent(GetCardsSuccessEvent event) {
        busCollaborator.onGetCardSuccess();
    }

    @Subscribe
    @Override
    public void onEvent(GetCardFailedEvent event) {
        busCollaborator.onGetCardFailed();
    }

    public void saveCard(MerchantRestAPI merchantRestAPIMock, SaveCardRequest request, String mToken) {
        transactionManager.setMerchantPaymentAPI(merchantRestAPIMock);
        transactionManager.saveCards(request, mToken);
    }

    @Override
    @Subscribe
    public void onEvent(SaveCardSuccessEvent event) {
        busCollaborator.onSaveCardSuccessEvent();
    }

    @Override
    @Subscribe
    public void onEvent(SaveCardFailedEvent event) {
        busCollaborator.onsaveCardFailedEvent();
    }

    @Override
    @Subscribe
    public void onEvent(SSLErrorEvent errorEvent) {
        busCollaborator.onSSLErrorEvent();
    }
}
