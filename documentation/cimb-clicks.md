# Overview

CIMB Clicks is payment using direct debit on CIMB. It uses Webview to handle the payment.

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

First, create `DescriptionModel` instance.

```Java
DescriptionModel cimbDescription = new DescriptionModel(PAYMENT_DESCRIPTION);
```

To start the payment use `snapPaymentUsingCIMBClick` on `VeritransSDK` instance.

```Java
VeritransSDK.getVeritransSDK().snapPaymentUsingCIMBClick(CHECKOUT_TOKEN);
```

## Capture Redirect URL and Handle the Webview