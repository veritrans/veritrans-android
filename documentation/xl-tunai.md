# Overview

XL Tunai is like Bank Transfer transaction. It will create a virtual account for the paymenr.

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

To start the payment use `snapPaymentUsingXLTunai ` from `VeritransSDK` instance using checkout token as the parameter.

```Java
VeritransSDK.getVeritransSDK().snapPaymentUsingXLTunai(CHECKOUT_TOKEN);
```

## Capture Transaction Information

Merchant needs to show the order ID, merchant ID and expiration time. They are provided in `TransactionSuccessEvent`.

```Java
String expiration = transactionSuccessEvent.getResponse().getXlTunaiExpiration();
String orderId = transactionSuccessEvent.getResponse().getXlTunaiOrderId();
String merchantId = transactionSuccessEvent.getResponse().getXlTunaiMerchantId();
```