# Core Kit SDK

This flow assumes that the App Developer implements the User interface necessary for receiving user inputs to and library only provides the payments infrastructure.

To perform transaction using core, follow the steps given below:

1. Set transaction request information to SDK.
2. Setup `TransactionCallback` to catch the transaction results.
3. Call the implemented method of the desired payment mode.

## Differences between core kit and the UI kit.

The main difference between the core and UI kit is that

* Get payment response -
  * In Core Kit - Use TransactionCallback
  * In UI Kit - Use TransactionFinishedCallback

* Payment Selection Method -
  * In Core Kit - Call paymentUsingXXX().
  * In UI Kit - Call startPaymentUiFlow() to see all specified payment methods.

# Usage
this new SDK you don't need to use event bus, like on the previous VeritransSDK. in this version we replace whole the event bus stuff with java callback, to make the SDK usage easier.

Each API usage has its own `Callback` interface to capture finished API access. Please implement one of this callback interface that accessing the SDK.

* `CheckoutCallback`
* `TransactionOptionsCallback`
* `CardTokenCallback`
* `TransactionCallback`
* `SaveCardCallback`
* `GetCardCallback`
* `CardRegistrationCallback`
* `TransactionFinishedCallback`

All of this interface will implement 3 method, `onSuccess`, `onFailure` and `onError`.
For example, `TransactionFinished` will have to implement these methods.

```Java
//transaction success with response body
@Override
public void onSuccess(TransactionResponse response) {
// actionSuccess(response);
}
//transaction failed with response body and the reason
@Override
public void onFailure(TransactionResponse response, String reason) {
// actionFailure(response, reason);
}

//general error(on network unavailable, on transaction failed, on callback unimplemented etc)
@Override
public void onError(Throwable error) {
// actionError(error);
}
```

### Initialisation

To initialize the core SDK, just use these codes.

```Java
SdkCoreFlowBuilder.init(CONTEXT, CLIENT_KEY, BASE_URL)
    .enableLog(true)
    .buildSDK();
```

### Common request information

These are common parameters which are required in all types of transaction.

### Set customer details

**CustomerDetails** class holds information about customer. To set information about customer in TransactionRequest use following code -

```Java
CustomerDetails customer = new CustomerDetails(String firstName, String lastName, String email, String phone);
// Set customer details on
transactionRequest.setCustomerDetails(customer);
```

### Set Item details

**ItemDetails** class holds information about item purchased by user. TransactionRequest takes an array list of item details. To set this in TransactionRequest use following code -

```Java
ItemDetails itemDetails1 = new CustomerDetailsItemDetails(String id, double price, double quantity, String name);
ItemDetails itemDetails2 = new CustomerDetailsItemDetails(String id, double price, double quantity, String name);

// Create array list and add above item details in it and then set it to transaction request.
ArrayList<ItemDetails> itemDetailsList = new ArrayList<>();
itemDetailsList.add(itemDetails1);
itemDetailsList.add(itemdetails2);

// Set item details into the transaction request.
transactionRequest.setItemDetails(itemDetailsList);
```

### Set Billing Address

**BillingAddress** class holds information about billing. TransactionRequest takes an array list of billing details. To set this in TransactionRequest use following code -

```Java
BillingAddress billingAddress1 = new BillingAddress(String firstName, String lastName, String address, String city, String postalCode, String phone, String countryCode);

BillingAddress billingAddress2 =new BillingAddress(String firstName, String lastName, String address, String city, String postalCode, String phone, String countryCode);

// Create array list and add above billing details in it and then set it to transaction request.
ArrayList<BillingAddress> billingAddressList = new ArrayList<>();
billingAddressList.add(billingAddress1);
billingAddressList.add(billingAddress2);

// Set billing address list into transaction request
transactionRequest.setBillingAddressArrayList(billingAddressList);
```

### Set Shipping Address

**ShippingAddress** class holds information about shipping address. TransactionRequest takes an array list of shipping details. To set this in TransactionRequest use following code -

```Java
ShippingAddress shippingAddress1 = new ShippingAddress(String firstName, String lastName, String address, String city, String postalCode, String phone, String countryCode);

ShippingAddress shippingAddress2 =new ShippingAddress(String firstName, String lastName, String address, String city, String postalCode, String phone, String countryCode);

// Create array list and add above shipping details in it and then set it to transaction request.
ArrayList<BillingAddress> shippingAddressList = new ArrayList<>();
shippingAddressList.add(shippingAddress1);
shippingAddressList.add(shippingAddress1);

// Set shipping address list into transaction request.
transactionRequest.setShippingAddressArrayList(shippingAddressList);
```

### Set Bill Info

**BillInfoModel** class holds information about billing information that will be shown at billing details.

To set this in TransactionRequest use following code -

```Java
BillInfoModel billInfoModel = new BillInfoModel(BILL_INFO_KEY, BILL_INFO_VALUE);
// Set the bill info on transaction details
transactionRequest.setBillInfoModel(billInfoModel);
```

###  Setting Transaction Details

Set required transaction information to sdk instance.

TransactionRequest contains following information

- Order Id - _unique order id to identify this transaction_.
- Amount - _amount to charge_.
- Customer details - _holds an information about customer like name , email and phone_.
- Item details - _ holds an information about purchased item, like item id, price etc_.
- Billing Address - _holds an address information like city , country, zip code etc_.
- Shipping Address - _holds an address information like city , country, zip code etc_.
- Bill information - _contains custom details in key-value that you want to display on bill print.

All of the above parameters are not nesseccessary to set, It depends on which type of payment method you want to execute. To get more information about required paramter see http://docs.veritrans.co.id/en/api/methods.html#Charge .
Sdk will throw error message in case you missed some information right before starting an payment flow.

Create instance of **TransactionRequest** class.

```Java
TransactionRequest transactionRequest = new TransactionRequest(TRANSACTION_ID, TOTAL_AMOUNT);
// Optional Parameters
transactionRequest.setCustomerDetails(CUSTOMER_DETAILS);
transactionRequest.setItemDetails(ITEM_DETAIL_LIST);
transactionRequest.setBillInfoModel(BILL_INFO);
transactionRequest.setBillingAddressArrayList(BILLING_ADDRESS_LIST);
transactionRequest.setShippingAddressArrayList(SHIPPING_ADDRESS_LIST);
```

Then set the `transactionRequest` into `VeritransSDK` instance.

```Java
VeritransSDK.getVeritransSDK().setTransactionRequest(transactionRequest);
```

### Get Checkout Token

After set the transaction details, you must checkout your transaction and get a `authentication_token` by using this function to be able to proceed the payment.

**Note : This is must do process before do the payment charging because the charging needs the checkout token.**

### Setup CheckoutCallback handler for Checkout Token

You have to implement `CheckoutCallback` when make a chackout to merchant server. There will be three implemented methods.

```Java
    public void onSuccess(Token token);

    public void onFailure(Token token, String reason);

    public void onError(Throwable error);
```

### Proceed to Checkout a Transaction

Do the checkout using this code.

```Java
MidtransSDK.getInstance().checkout(new CheckoutCallback() {
    @Override
    public void onSuccess(Token token) {
    //acton when Successed
    }

    @Override
    public void onFailure(Token token, String reason) {
    //action whe  failed
    }

    @Override
    public void onError(Throwable error) {
    //action when error
    }
});
```

### Payment Options

There are several payment methods supported by this SDK.

### Get Enabled Transaction Options

To get all enabled payment methods (also full transaction details) you can use `getTransactionOptions` function from `MidtransSDK` instance with a `authentication_token`.

```Java
MidtransSDK.getInstance().getTransactionOptions();
```

Implement `TransactionOptionsCallback`.

```Java
    public void onSuccess(Transaction transaction);

    public void onFailure(Transaction transaction, String reason);

    public void onError(Throwable error);
```


### Credit Card

To implement credit card payment read [this wiki](https://github.com/veritrans/veritrans-android/wiki/Credit-Card-Payment-Usage).

There are some optional features for Credit Card Payment.

- [Offers or Promotion](https://github.com/veritrans/veritrans-android/wiki/Offers-for-credit-cards)
- [Register Card](https://github.com/veritrans/veritrans-android/wiki/Register-Credit-Card-Usage)

### Bank Transfer

To implement bank transfer payment read [this wiki](https://github.com/veritrans/veritrans-android/wiki/Bank-Transfer-Payment-Usage).

### KlikBCA

To implement KlikBCA payment read [this wiki](https://github.com/veritrans/veritrans-android/wiki/KlikBCA-Payment-Usage).

### BCA Klikpay

To implement BCA Klikpay payment read [this wiki](https://github.com/veritrans/veritrans-android/wiki/BCA-Klikpay-Payment-Usage).

### Mandiri Clickpay

To implement Mandiri Click payment read [this wiki](https://github.com/veritrans/veritrans-android/wiki/Mandiri-Clickpay-Payment-Usage).

### Mandiri E-Cash

To implement Mandiri E-Cash payment read [this wiki](https://github.com/veritrans/veritrans-android/wiki/Mandiri-E-Cash-Payment-Usage).

### Mandiri Bill

To implement Mandiri Bill payment read [this wiki](https://github.com/veritrans/veritrans-android/wiki/Mandiri-Bill-Payment-Usage).

### Epay BRI

To implement Epay BRI payment read [this wiki](https://github.com/veritrans/veritrans-android/wiki/Epay-BRI-Payment-Usage).

### CIMB Clicks

To implement CIMB Clicks payment read [this wiki](https://github.com/veritrans/veritrans-android/wiki/CIMB-Clicks-Payment-Usage).

### Indosat Dompetku

To implement Indosat Dompetku payment read [this wiki](https://github.com/veritrans/veritrans-android/wiki/Indosat-Dompetku-Payment-Usage).

### Indomaret

To implement Indomaret payment read [this wiki](https://github.com/veritrans/veritrans-android/wiki/Indomaret-Payment-Usage).
