# Overview

XL Tunai is like Bank Transfer transaction. It will create a virtual account for the payment.

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

To start the payment use `paymentUsingXLTunai ` from `MidtansSDK` instance using checkout token as the parameter.

```Java
MidtransSDK.getInstance().paymentUsingXLTunai(AUTHENTICATION_TOKEN, new TransactionCallback() {
        @Override
        public void onSuccess(TransactionResponse response) {
        //action when transaction success
        }

        @Override
        public void onFailure(TransactionResponse response, String reason) {
        //action when transaction failed
        }

        @Override
        public void onError(Throwable error) {
        //action when error
        }
    }
);
```

## Capture Transaction Information

Merchant needs to show the order ID, merchant ID and expiration time. They are provided in `TransactionResponse`.

```Java
String expiration = response.getXlTunaiExpiration();
String orderId = response.getXlTunaiOrderId();
String merchantId = response.getXlTunaiMerchantId();
```