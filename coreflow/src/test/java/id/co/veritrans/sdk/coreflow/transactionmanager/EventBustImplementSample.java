package id.co.veritrans.sdk.coreflow.transactionmanager;

import org.greenrobot.eventbus.Subscribe;

import id.co.veritrans.sdk.coreflow.core.PaymentAPI;
import id.co.veritrans.sdk.coreflow.core.TransactionManager;
import id.co.veritrans.sdk.coreflow.core.VeritransRestAPI;
import id.co.veritrans.sdk.coreflow.eventbus.bus.VeritransBus;
import id.co.veritrans.sdk.coreflow.eventbus.bus.VeritransBusProvider;
import id.co.veritrans.sdk.coreflow.eventbus.callback.CardRegistrationBusCallback;
import id.co.veritrans.sdk.coreflow.eventbus.events.CardRegistrationFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.CardRegistrationSuccessEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.GeneralErrorEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.NetworkUnavailableEvent;

/**
 * Created by ziahaqi on 25/06/2016.
 */
public class EventBustImplementSample implements CardRegistrationBusCallback {
    BusCollaborator busCollaborator;
    public String onsuccessStatusCode;
    private TransactionManager transactionManager;
    private VeritransBus bus;

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

    public void registerBus(VeritransBus veritransBus) {
        VeritransBus.setBus(veritransBus);
        VeritransBusProvider.getInstance().register(this);
    }

    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void regCard(VeritransRestAPI registered, String cardNumber, String cardCvv, String cardExpMonth, String cardExpYear) throws InterruptedException {

        transactionManager.setVeritransPaymentAPI(registered);
        transactionManager.cardRegistration(cardNumber,
                cardCvv,
                cardExpMonth,
                cardExpYear);
    }

    public VeritransBus getBus(){
        return this.bus;
    }
}
