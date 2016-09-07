# Overview

Indomaret payment will create payment code that will be paid on Indomaret Convenience Store.

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

To start the payment use `snapPaymentUsingIndomaret` from VeritransSDK instance.

```Java
VeritransSDK.getVeritransSDK().snapPaymentUsingIndomaret(CHECKOUT_TOKEN);
```

## Capture Transaction Information

Payment code is provided in `TransactionSuccessEvent`.

```Java
String indomaretCode = transactionSuccessEvent.getResponse().getPaymentCodeIndomaret();
```
