# Overview

Mandiri Bill payment is like Bank Transfer transaction. It will create a virtual account for the payment.

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

To start the payment use `snapPaymentUsingMandiriBillPay` from `VeritransSDK` instance.

```Java
VeritransSDK.getVeritransSDK().snapPaymentUsingMandiriBillPay(CHECKOUT_TOKEN, CUSTOMER_EMAIL);
```

## Capture Transaction Information

Merchant needs to show the payment code and company code. They are provided in `TransactionSuccessEvent`.

```Java
String paymentCode = transactionSuccessEvent.getResponse().getPaymentCode();
String companyCode = transactionSuccessEvent.getResponse().getCompanyCode();
```