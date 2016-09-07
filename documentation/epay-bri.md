# Overview

Epay BRI is direct debit using BRI. It uses Webview to complete the payment.

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

To start payment use `snapPaymentUsingEpayBRI ` on `VeritransSDK` instance.

```Java
VeritransSDK.getVeritransSDK().snapPaymentUsingEpayBRI(CHECKOUT_TOKEN);
```

## Capture Redirect URL and handle the Webview

Redirect URL is provided in `TransactionSuccessEvent`.

```Java
String redirect = transactionSuccessEvent.getResponse().getRedirectUrl();
```

Then, load `redirect` into Webview to complete the payment.

```Java
webview.loadUrl(redirect);
```