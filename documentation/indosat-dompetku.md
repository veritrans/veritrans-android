# Overview

Indosat Dompetku is payment method by using direct debit to Indosat phone credits.

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

To start the payment use `paymentUsingIndosatDompetku` method from `MidtransSDK` instance and pass the phone number as a parameter.

```Java
MidtransSDK.getInstance().paymentUsingBCAKlikpay(AUTHENTICATION_TOKEN, PHONE_NUMBER, new TransactionCallback() {
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

## Capture Redirect URL and handle the Webview

Redirect URL is provided in `TransactionResponse`.

```Java
String redirect = response.getRedirectUrl();
```

Then load `redirect` into Webview.

```Java
webview.loadUrl(redirect);
```