# Overview

This payment methods require user to input token APPLI response from Mandiri.

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

Start the payment using this code.

```Java
VeritransSDK.getVeritransSDK().snapPaymentUsingMandiriClickPay(CHECKOUT_TOKEN, CARD_NUMBER, CHALLENGE_TOKEN, INPUT3);
```

## Capture Transaction Information

It is provided in `TransactionSuccessEvent`.

```Java
TransactionResponse response = transactionSuccessEvent.getResponse();
```