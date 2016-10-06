# Overview

There are two banks supported at this SDK.

- BCA
- Permata
- Other Banks (Using Permata)

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

## Start the payment
for transaction using bank transfer, You can use methods API of midtrans SDK below
```Java
//bank transfer BCA
midtransSDK.paymentUsingBankTransferBCA(
        AUTHENTICATION_TOKEN, EMAIL_USER, transactionCallback);
//bank tranfer permata
midtransSDK.paymentUsingBankTransferPermata(
        AUTHENTICATION_TOKEN, EMAIL_USER, transactionCallback);
//bank tranfer all
midtransSDK.paymentUsingBankTransferAllBank(
        AUTHENTICATION_TOKEN, EMAIL_USER, transactionCallback);
```
You need the checkout token and customer email before starting the payment.

Execute Transaction using following method to get response back. below sample transaction using bank tranfer permata

```Java
midtransSDK.paymentUsingBankTransferPermata(
        AUTHENTICATION_TOKEN,
        EMAIL_USER, new TransactionCallback() {
            @Override
            public void onSuccess(TransactionResponse response) {
                //actionTransactionSuccess(response);
            }

            @Override
            public void onFailure(TransactionResponse response, String reason) {
                //actionTransactionFailure(response, reason);
            }

            @Override
            public void onError(Throwable error) {
                //actionTransactionError(error);
            }
        }
);
```

## Get Virtual Account Information

User need to get the virtual account number. It is provided in `TransactionResponse`.

BCA and Permata has different data structure so we must handle it in different way.

### BCA Virtual Account

BCA can provide more than one virtual account, so we get a list of `BCAVANumber` object.

```Java
List<BCAVANumber> virtualAccounts = response.getAccountNumbers();
```

### Permata and Other Bank Virtual Account

```Java
String virtualAccount = response.getPermataVANumber();
```