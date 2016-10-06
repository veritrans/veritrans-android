# Overview

Telkomsel Cash is payment method by using direct debit to Telkomsel phone credits.
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

## Start the Payment

To start the payment use `paymentUsingTelkomselEcash ` method from `MidtransSDK` instance and pass the checkout token and the the telkomsel token from SMS.

```Java
MidtransSDK.getInstance().paymentUsingTelkomselEcash(AUTHENTICATION_TOKEN,
TELKOMSEL_TOKEN, new TransactionCallback() {
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

## Capture Transaction Response

You can get the transaction response on `TransactionResponse` object

