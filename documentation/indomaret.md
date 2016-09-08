# Overview

Indomaret payment will create payment code that will be paid on Indomaret Convenience Store.

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

To start the payment use `paymentUsingIndomaret` from `MidtransSDK` instance.

```Java
MidtransSDK.getInstance().paymentUsingIndomaret(AUTHENTICATION_TOKEN, new TransactionCallback() {
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

## Capture Transaction Information

Payment code is provided in `TransactionResponse`.

```Java
String indomaretCode = response.getPaymentCodeIndomaret();
```
