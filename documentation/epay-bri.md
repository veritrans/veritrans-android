# Overview

Epay BRI is direct debit using BRI. It uses Webview to complete the payment.

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

To start payment use `paymentUsingEpayBRI ` on `MidtransSDK` instance.

```Java
MidtransSDK.getInstance().paymentUsingEpayBRI(AUTHENTICATION_TOKEN, new TransactionCallback() {
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

Then, load `redirect` into Webview to complete the payment.

```Java
webview.loadUrl(redirect);
```