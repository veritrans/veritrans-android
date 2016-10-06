# Overview

BCA Klikpay used webview to redirect users to the payment page provided by BCA.

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

To start the payment, use `paymentUsingBCAKlikpay ` function from `MidtransSDK` instance with a `checkout_token`.

```Java
MidtransSDK.getInstance().paymentUsingBCAKlikpay(AUTHENTICATION_TOKEN, new TransactionCallback() {
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

Redirect URL is got from `TransactionResponse`.

```Java
String redirect = response.getRedirectUrl();
```

Then you must load `redirect` into your webview.

```Java
webview.loadUrl(redirect);
```
