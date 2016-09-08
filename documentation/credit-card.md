# Credit Card Payment

Credit card payment has several steps to do the charging.

1. _Tokenize_ - Wrap and encrypt your secure credit card information to one time token that will be used for charging.
2. _Charging_ - Use the token in step 1 to do the payment.

## Card Type details

There are **3 types** of flow for **credit card payments**.

1. _One click_ : Allows merchants to securely tokenize the credit card, and use it for transactions later, without users entering the card details again.
2. _Two click_ : Allows merchants to securely tokenize the credit card and use it for the later transactions, with the user entering the CVV.
3. _Normal_ : This is the simple case where user enters his credit card information every time a transaction is needed to be done.

**Notes**

* For 1) and 2) some API must be implemented on the Merchant server backend. See [Merchant Server Reference Implementation](https://github.com/veritrans/mobile-merchant-server) for more information.
* These features are available on special MID's from Banks and may not be available to all merchants. We will go in detail about these flows ahead.
* For **normal flow** we can do secure and Insecure way.
    * Secure : With 3d secure validation.
    * Insecure : Without 3d secure validation.
* **One click** and **two click** can only be done in **secure** flow only(ie. 3DS validation)

To set the card click type and the secure mode of the transaction use code below.

```Java
transactionRequest.setCardPaymentInfo(CARD_CLICK_TYPE, isSecure);

CARD_CLICK_TYPE - type of card use these resource strings:

- R.string.card_click_type_one_click - for one click
- R.string.card_click_type_two_click - for two click
- R.string.card_click_type_none - for normal transaction

IS_SECURE - set to true if using 3D secure
```

## Tokenize Credit Card Details
We provide interface to make tokenize.. You  need to implement `CardTokenCallback` when make a tokenize to get card token.
It contains three implemented methods `onSuccess`, `onFailure` and `onError`.

```Java
public interface CardTokenCallback {

    public void onSuccess(TokenDetailsResponse response);

    public void onFailure(TokenDetailsResponse response, String reason);

    public void onError(Throwable error);
}
```

On success of get token api call, it will return token id and other parameters in `TokenDetailsResponse` response.

If secure flow is available then it will return redirect_url which will take user to web page for 3d secure validation.

To start the tokenize call code below.

```Java
// CardTokenRequest class contains card details required for getting token.
CardTokenRequest cardTokenRequest = new CardTokenRequest(cardNumber, cvv, month, year, midtransSDK.getClientKey());
// Optional mode
cardTokenRequest.setIsSaved(true); // Set to true if this token will be used later (one click or two click)
cardTokenRequest.setTwoClick(true); // Set to true if using two click
cardTokenRequest.setSavedTokenId(SAVED_TOKEN_ID); // Set the saved token id if using two click
// Start the tokenize
midtransSDK.getCardToken(cardTokenRequest, new CardTokenCallback() {
    @Override
    public void onSuccess(TokenDetailsResponse response) {
        //actionGetCardTokenSuccess(response);
    }

    @Override
    public void onFailure(TokenDetailsResponse response, String reason) {
        //actionGetCardTokenFailure(response, reason);
    }

    @Override
    public void onError(Throwable error) {
        //actionGetCardTokenError(error);
    }
});
```

### Handle 3D Secure Authorization

If you use secure mode when doing tokenize, you will get redirect URL on `TokenDetailsResponse`.

To get the redirect URL, use following code.

```Java
String redirectUrl = response.getRedirectUrl();
```

Then you need a webview with a custom client to capture redirect URL if the authorization was completed.

For example:

```Java
// Setup a custom `WebViewClient` to capture completed 3D secure authorization
class VeritransWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            // Check if redirect URL has "/token/callback/" to ensure authorization was completed
            if (url.contains("/token/callback/")) {
               // Authorization was completed
               // Handle the charging here
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }
}

// Set the VeritransWebViewClient on your WebView
webView.setWebViewClient(new VeritransWebViewClient());
```

## Payment(Charge) using Credit/Debit card

Before doing the charging, you must setup the event bus subscriber to your calling class.

We provide interface to make this easier. You just need to implement `TransactionCallback` in your class.

It contains four implemented methods onSuccess, onFailure and two general callback methods.

```Java
public interface TransactionCallback {

    public void onSuccess(TransactionResponse response);

    public void onFailure(TransactionResponse response, String reason);

    public void onError(Throwable error);
}
```

To start the payment, you need a `checkout_token`.

```Java
MidtransSDK.getInstance().paymentUsingCard(AUTHENTICATION_TOKEN, CARD_TOKEN, IS_SAVE_CARD, new TransactionCallback() {
        @Override
        public void onSuccess(TransactionResponse response) {
        // actionSuccess(response);
        }

        @Override
        public void onFailure(TransactionResponse response, String reason) {
        // actionFailure(response, reason);
        }

        @Override
        public void onError(Throwable error) {
        // actionError(error);
        }
    }
);
```

After successful transaction card information will be saved, if user permits. In `TransactionResponse` a *saved_token_id* field contains a new token that can be stored and reused in future transaction.