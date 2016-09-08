# Overview

Veritrans SDK supports payment using KlikBCA.

KlikBCA payment flow is rather simple. 

1. User input account's username from their KlikBCA
2. Veritrans will add their payment to the respective KlikBCA account

# Implementation
We provide interface for transaction callback. You just need to implement TransactionCallback when make a transaction to get transaction response.
It contains three implemented methods `onSuccess`, `onFailure` and `onError`.

```Java
public interface TransactionCallback {
    //transaction response when success
    public void onSuccess(TransactionResponse response);

    //response when transaction failed
    public void onFailure(TransactionResponse response, String reason);

    //general error
    public void onError(Throwable error);
}
```

## Start The Payment

To start payment, call `paymentUsingKlikBCA ` function from `MidtransSDK` instance with `checkout_token` and `user_id`.

```Java
MidtransSDK.getInstance().paymentUsingBCAKlikpay(AUTHENTICATION_TOKEN, USER_ID,
new TransactionCallback() {
        @Override
        public void onSuccess(TransactionResponse response) {
        //action when transaction success
        }

        @Override
        public void onFailure(TransactionResponse response, String reason) {
        //action when transaction failure
        }

        @Override
        public void onError(Throwable error) {
        //action when error
        }
    }
);
```

## Get Payment Information
It is provided in `TransactionResponse`.
