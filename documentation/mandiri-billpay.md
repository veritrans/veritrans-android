# Overview

Mandiri Bill payment is like Bank Transfer transaction. It will create a virtual account for the payment.

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

To start the payment use `PaymentUsingMandiriBillPay` from `MidtansSDK` instance.

```Java
MidtransSDK.getInstance().paymentUsingBCAKlikpay(AUTHENTICATION_TOKEN, COSTUMER_EMAL, new TransactionCallback() {
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

Merchant needs to show the payment code and company code. They are provided in `TransactionResponse`.

```Java
String paymentCode = response.getPaymentCode();
String companyCode = response.getCompanyCode();
```