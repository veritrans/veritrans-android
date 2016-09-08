# Overview 

This payment method needs webview to handle payment from Mandiri E-Cash. 

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

To start the payment use `paymentUsingMandiriEcash` at `MidtransSDK` instance.

```Java
MidtransSDK.getInstance().paymentUsingMandiriEcash(AUTHENTICATION_TOKEN, new TransactionCallback() {
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

## Capture Redirect URL and Handle the Webview

Redirect URL provided in `TransactionResponse`.

```Java
String redirect = response.getRedirectUrl();
```

Then load `redirect` into your webview.

```Java
webview.loadUrl(redirect);
```