# Overview

Telkomsel Cash is payment method by using direct debit to Telkomsel phone credits.

# Implementation

## Setup Event Bus

Before doing the charging, you must setup the event bus subscriber to your calling class.

We provide interface to make this easier. You just need to implement `TransactionBusCallback` in your class.

It contains four implemented methods `onSuccess`, `onFailure` and two general callback methods

```Java
@Subscribe
@Override
public void onEvent(TransactionSuccessEvent event) {
    // Success Event
}

@Subscribe
@Override
public void onEvent(TransactionFailedEvent event) {
    // Failed Event
    String errorMessage = event.getMessage();
}
```

## Start the Payment

To start the payment use `snapPaymentUsingTelkomselEcash ` method from `VeritransSDK` instance and pass the checkout token and the the telkomsel token from SMS.

```Java
VeritransSDK.getVeritransSDK().snapPaymentUsingTelkomselEcash(CHECKOUT_TOKEN, TELKOMSEL_TOKEN);
```

## Capture Transaction Response

You can get the transaction response on `TransactionSuccessEvent` subscriber method.

```Java
TransactionResponse  response = transactionSuccessEvent.getResponse();
```

