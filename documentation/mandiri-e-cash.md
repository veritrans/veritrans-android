# Overview 

This payment method needs webview to handle payment from Mandiri E-Cash. 

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

To start the payment use `snapPaymentUsingMandiriEcash` at `VeritransSDK` instance.

```Java
VeritransSDK.getVeritransSDK().snapPaymentUsingMandiriEcash(CHECKOUT_TOKEN);
```

## Capture Redirect URL and Handle the Webview

Redirect URL provided in `TransactionSuccessEvent`.

```Java
String redirect = transactionSuccessEvent.getResponse().getRedirectUrl();
```

Then load `redirect` into your webview.

```Java
webview.loadUrl(redirect);
```