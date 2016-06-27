package id.co.veritrans.sdk.coreflow.transactionmanager;

import org.greenrobot.eventbus.Subscribe;

import id.co.veritrans.sdk.coreflow.core.PaymentAPI;
import id.co.veritrans.sdk.coreflow.core.TransactionManager;
import id.co.veritrans.sdk.coreflow.eventbus.bus.VeritransBus;
import id.co.veritrans.sdk.coreflow.eventbus.bus.VeritransBusProvider;
import id.co.veritrans.sdk.coreflow.eventbus.callback.CardRegistrationBusCallback;
import id.co.veritrans.sdk.coreflow.eventbus.events.CardRegistrationFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.CardRegistrationSuccessEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.GeneralErrorEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.NetworkUnavailableEvent;
import id.co.veritrans.sdk.coreflow.veritransandroid.VeritransAndroidSDKTest;

/**
 * Created by ziahaqi on 25/06/2016.
 */
public class EventBustImplementSample implements CardRegistrationBusCallback {
    SampleClass sampleClass;
    public String onsuccessStatusCode;
    private TransactionManager transactionManager;
    private VeritransBus bus;

    @Override
    @Subscribe
    public void onEvent(CardRegistrationSuccessEvent event) {
        sampleClass.callMethod(event);
       onsuccessStatusCode = event.getResponse().getStatusCode();
    }

    @Subscribe
    @Override
    public void onEvent(CardRegistrationFailedEvent event) {

    }

    @Override
    @Subscribe
    public void onEvent(NetworkUnavailableEvent event) {

    }

    @Override
    @Subscribe
    public void onEvent(GeneralErrorEvent event) {

    }

    public void setTransactionManager(TransactionManager transactionManager) {
        this.transactionManager = transactionManager;
    }

    public void regCard(PaymentAPI registered, String cardNumber, String cardCvv, String cardExpMonth, String cardExpYear) {
        transactionManager.setVeritransPaymentAPI(registered);
        transactionManager.cardRegistration(cardNumber,
                cardCvv,
                cardExpMonth,
                cardExpYear);
    }

    public void registerBus(VeritransBus veritransBus) {
        VeritransBus.setBus(veritransBus);
        VeritransBusProvider.getInstance().register(this);
    }
    public VeritransBus getBus(){
        return this.bus;
    }
}
