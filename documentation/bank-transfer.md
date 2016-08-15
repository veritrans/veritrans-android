# Overview

There are two banks supported at this SDK.

- BCA
- Permata
- Other Banks (Using Permata)

# Implementation

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

## Start the payment

You need the checkout token and customer email before starting the payment.

Execute Transaction using following method to get response back.

```Java
// Using Permata
veritransSDK.snapPaymentUsingBankTransferPermata(CHECKOUT_TOKEN, CUSTOMER_EMAIL);
// Using BCA 
VeritransSDK.getVeritransSDK().snapPaymentUsingBankTransferBCA(CHECKOUT_TOKEN, CUSTOMER_EMAIL);
// Using Other Banks
VeritransSDK.getVeritransSDK().snapPaymentUsingBankTransferAllBank(CHECKOUT_TOKEN, CUSTOMER_EMAIL);
```

## Get Virtual Account Information

User need to get the virtual account number. It is provided in `TransactionSuccessEvent`.

BCA and Permata has different data structure so we must handle it in different way.

### BCA Virtual Account

BCA can provide more than one virtual account, so we get a list of `BCAVANumber` object.

```Java
List<BCAVANumber> virtualAccounts = transactionSuccessEvent.getResponse().getAccountNumbers();
```

### Permata and Other Bank Virtual Account

```Java
String virtualAccount = transactionSuccessEvent.getResponse().getPermataVANumber();
```