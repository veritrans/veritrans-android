# Overview

BCA Klikpay used webview to redirect users to the payment page provided by BCA.

# Implementation

## Event Bus Setup

Before doing the charging, you must setup the event bus subscriber to your calling class.

We provide interface to make this easier. You just need to implement `TransactionBusCallback` in your class.

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

To start the payment, use `snapPaymentUsingBCAKlikpay ` function from `VeritransSDK` instance with a `checkout_token`.

```Java
VeritransSDK.getVeritransSDK().snapPaymentUsingBCAKlikpay(CHECKOUT_TOKEN);
```

## Capture Redirect URL and handle the Webview

Redirect URL is got from `TransactionSuccessEvent`.

```Java
String redirect = transactionSuccessEvent.getResponse().getRedirectUrl();
```

Then you must load `redirect` into your webview.

```Java
webview.loadUrl(redirect);
```