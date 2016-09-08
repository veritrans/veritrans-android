# Overview
Our SDK has been provide feature to allow merchant save credit card info to enable payment using `two_click` and `one_click` options. to enable this feature make sure your merchant server has save/get card mechanism.
# Save Credit Card
If you want to save credit card to your merchant server, you have to to set `SAVE_CARD`  option when make credit card transaction. if success the transaction response will provide `save_card_token` and credit card number that has been masked. you can get those data by this way
```Java
    //get save card token
    String saveCardToken = response.getSavedTokenId();

    //get card number
    String maskedCardNumber = response.getCardNumber();
}
```
We provide interface `SaveCardCallback` to  save creditcard to marchant server. You  need to implement SaveCardCallback when invoke a saveCard method.
It contains three implemented methods `onSuccess`, `onFailure` and `onError`.

```Java
public interface SaveCardCallback {
    //transaction response when success
    public void onSuccess(SaveCardResponse response);

    //response when transaction failed
    public void onFailure(SaveCardResponse response, String reason);

    //general error
    public void onError(Throwable error);
}
```

## Start the Save Card
To save card you need a `USER_ID` that is unique id for every user to identify user that hav those card. and don't forget to use `saveCard` method from `MidtransSDK` to make save card request.

```Java
//list of credit card will save
ArrayList<SaveCardRequest> saveCardRequests = new ArrayList<>();
saveCardRequests.add(new SaveCardRequest(creditCard.getSavedTokenId(), creditCard.getMaskedCard(), cardType));

midtransSDK.saveCard(USER_ID, saveCardRequests, new SaveCardCallback() {
    @Override
    public void onSuccess(SaveCardResponse response) {
    }

    @Override
    public void onFailure(String reason) {

    }

    @Override
    public void onError(Throwable error) {
    }
});
```

# Get Credit Card
To save get cards from merchant server  you need a `USER_ID` that you have been used for saved the credit cards and use `getCards` method from `MidtransSDK` to make get credit cards request.
```Java
midtransSDK.getCards(USER_ID, new GetCardCallback() {
        @Override
        public void onSuccess(ArrayList<SaveCardRequest> response) {
        // action when success
        }

        @Override
        public void onFailure(String reason) {
        // action when failed
        }

        @Override
        public void onError(Throwable error) {
        // action when error
        }
    });
```
