# Overview

This payment methods require user to input token APPLI response from Mandiri.

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

To start the payment, use `paymentUsingMandiriClickPay ` function from `MidtransSDK` instance with  `AUTHENTICATION_TOKEN`, `CARD_NUMBER`, `CHALLENGE_TOKEN`, and `INPUT3`.

```Java
MidtransSDK.getInstance().paymentUsingMandiriClickPay(AUTHENTICATION_TOKEN,
CARD_NUMBER, INPUT3, new TransactionCallback() {
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
## Capture Transaction Information

It is provided in `TransactionResponse`.
