# Overview

Veritrans SDK supports payment using KlikBCA.

KlikBCA payment flow is rather simple. 

1. User input account's username from their KlikBCA
2. Veritrans will add their payment to the respective KlikBCA account

# Implementation

There are three steps to implement this payment.

## Event Bus Setup

Before doing the charging, you must setup the event bus subscriber to your calling class.

We provide interface to make this easier. You just need to implement TransactionBusCallback in your class.

It contains four implemented methods onSuccess, onFailure and two general callback methods

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

## Start The Payment

To start payment, call `snapPaymentUsingKlikBCA ` function from `VeritransSDK` instance with `checkout_token` and `user_id`.

```Java
VeritransSDK.getVeritransSDK().snapPaymentUsingKlikBCA(CHECKOUT_TOKEN, KLIK_BCA_USER_ID);
```

## Get Payment Information

It is provided in `TransactionSuccessEvent`.

```Java
TransactionResponse response = transactionSuccessEvent.getResponse();
```

