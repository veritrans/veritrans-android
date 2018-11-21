### Core Flow Implementation Guide

#### Prepare Transaction Request

Before using the SDK for making payment, you must set the transaction details first into the SDK instance.

##### Transaction Request (Required)

`TRANSACTION_ID` and `TOTAL_AMOUNT` was required and mandatory to create a transaction request.

```Java
TransactionRequest transactionRequest = new TransactionRequest(TRANSACTION_ID, TOTAL_AMOUNT);
```
Note :

- `TRANSACTION_ID` is an unique id for your transaction, maximum character length is 50.
- `TOTAL_AMOUNT` is charge amount.

That's minimum request for making payment, all of payment method you activated will be default setting.

##### Complete Transaction Request (Based on payment method)
Some of payment method need more parameter for making payment. Here's some required parameter for complete transaction request.

##### Customer Details (Optional)
Customer Details was optional when creating transaction request.
```Java
CustomerDetails customer = new CustomerDetails(FIRST_NAME, LAST_NAME, EMAIL, PHONE_NUMBER);
transactionRequest.setCustomerDetails(customer);
```
Note: 
- This was assumed that you have created `transactionRequest` object using required parameters.

##### Item Details (Optional)

ItemDetails class holds information about item purchased by user. TransactionRequest takes an array list of item details.

```Java
ItemDetails itemDetails1 = new ItemDetails(ITEM_ID_1, ITEM_PRICE_1, ITEM_QUANTITY_1, ITEM_NAME_1);
ItemDetails itemDetails2 = new ItemDetails(ITEM_ID_2, ITEM_PRICE_2, ITEM_QUANTITY_2, ITEM_NAME_2);

// Create array list and add above item details in it and then set it to transaction request.
ArrayList<ItemDetails> itemDetailsList = new ArrayList<>();
itemDetailsList.add(itemDetails1);
itemDetailsList.add(itemdetails2);

// Set item details into the transaction request.
transactionRequest.setItemDetails(itemDetailsList);
```

Note: 
- This was assumed that you have created `transactionRequest` object using required parameters.
- `ITEM_NAME` maximum character length is 50.

##### Billing Address (Optional)

BillingAddress class holds information about billing. TransactionRequest takes an array list of billing details.

```Java
BillingAddress billingAddress1 = new BillingAddress(FIRST_NAME_1, LAST_NAME_1, ADDRESS_1, CITY_1, POSTAL_CODE_1, PHONE_NUMBER_1, COUNTRY_CODE_1);

BillingAddress billingAddress2 =new BillingAddress(FIRST_NAME_2, LAST_NAME_2, ADDRESS_2, CITY_2, POSTAL_CODE_2, PHONE_NUMBER_2, COUNTRY_CODE_2);

// Create array list and add above billing details in it and then set it to transaction request.
ArrayList<BillingAddress> billingAddressList = new ArrayList<>();
billingAddressList.add(billingAddress1);
billingAddressList.add(billingAddress2);

// Set billing address list into transaction request
transactionRequest.setBillingAddressArrayList(billingAddressList);
```

Note: 
- This was assumed that you have created `transactionRequest` object using required parameters.

##### Shipping Address (Optional)

ShippingAddress class holds information about shipping address. TransactionRequest takes an array list of shipping details.

```Java
ShippingAddress shippingAddress1 = new ShippingAddress(FIRST_NAME_1, LAST_NAME_1, ADDRESS_1, CITY_1, POSTAL_CODE_1, PHONE_NUMBER_1, COUNTRY_CODE_1);

ShippingAddress shippingAddress2 =new ShippingAddress(FIRST_NAME_2, LAST_NAME_2, ADDRESS_2, CITY_2, POSTAL_CODE_2, PHONE_NUMBER_2, COUNTRY_CODE_2);

// Create array list and add above shipping details in it and then set it to transaction request.
ArrayList<BillingAddress> shippingAddressList = new ArrayList<>();
shippingAddressList.add(shippingAddress1);
shippingAddressList.add(shippingAddress1);

// Set shipping address list into transaction request.
transactionRequest.setShippingAddressArrayList(shippingAddressList);
```

Note: 
- This was assumed that you have created `transactionRequest` object using required parameters.
##### Bill Info (Optional, for Mandiri Bill only)

BillInfoModel class holds information about billing information that will be shown at billing details. Bill Info is optional for `Mandiri Bill` payment only.

```Java
BillInfoModel billInfoModel = new BillInfoModel(BILL_INFO_KEY, BILL_INFO_VALUE);
// Set the bill info on transaction details
transactionRequest.setBillInfoModel(billInfoModel);
```

Note: 
- This was assumed that you have created `transactionRequest` object using required parameters.
##### GO-PAY Callback Deeplink (Optional, for GO-PAY only)

GopayDeeplink class holds information about deeplink callback that GO-JEK app will call after transaction with GO-PAY success/failure. This is optional for `GO-PAY` payment only.

```Java
GopayDeeplink gopayDeeplink = new GopayDeeplink("demo://midtrans");
// Set the gopay deeplink to transaction request
transactionRequest.setGopay(billInfoModel);
```

Note: 
- This was assumed that you have created `transactionRequest` object using required parameters.
##### Credit Card (for Credit Card only)

Credit card payment has several steps to do for checkout.

1. _Tokenize_ - Wrap and encrypt your secure credit card information to one time token that will be used for charging.
2. _Charging_ - Use the snaptoken in step 1 to do the payment.

CreditCard class holds information about credit card payment method. You must know there are **3 types** of flow for **credit card payments**.

1. _One click_ : Allows merchants to securely tokenize the credit card, and use it for transactions later, without users entering the card details again.
2. _Two click_ : Allows merchants to securely tokenize the credit card and use it for the later transactions, with the user entering the CVV.
3. _Normal_ : This is the simple case where user enters his credit card information every time a transaction is needed to be done.

**Setup for Normal payment**
```Java
CreditCard creditCard = new CreditCard();
// True when using two clicks or one click
creditCard.setSaveCard(false);
// It can true or false
creditCard.setSecure(false);
// It can AUTHENTICATION_TYPE_NONE/AUTHENTICATION_TYPE_3DS/AUTHENTICATION_TYPE_RBA
creditCard.setAuthentication(CreditCard.AUTHENTICATION_TYPE_NONE)
// Set bank name when using MIGS channel
creditCard.setBank(BankType.BANK_NAME);
// Set MIGS channel (ONLY for BCA, BRI and Maybank Acquiring bank)
creditCard.setChannel(CreditCard.MIGS);

// Set credit card options into transaction request
transactionRequest.setCreditCard(creditCard);
```

**Setup for Two Click payment**
```Java
CreditCard creditCard = new CreditCard();
// True when using two clicks or one click
creditCard.setSaveCard(true);
// It can true or false
creditCard.setSecure(false);
// It can AUTHENTICATION_TYPE_NONE/AUTHENTICATION_TYPE_3DS/AUTHENTICATION_TYPE_RBA
creditCard.setAuthentication(CreditCard.AUTHENTICATION_TYPE_3DS)
// Set bank name when using MIGS channel
creditCard.setBank(BankType.BANK_NAME);
// Set MIGS channel (ONLY for BCA, BRI and Maybank Acquiring bank)
creditCard.setChannel(CreditCard.MIGS);

// Set credit card options into transaction request
transactionRequest.setCreditCard(creditCard);
```

**Setup for One Click payment**
```Java
CreditCard creditCard = new CreditCard();
// True when using two clicks or one click
creditCard.setSaveCard(true);
// Enable 3ds for one oneclick mode this is mandatory
creditCard.setSecure(true);
credirCard.setAuthentication(CreditCard.AUTHENTICATION_TYPE_3DS)
// Set bank name when using MIGS channel
creditCard.setBank(BankType.BANK_NAME);
// Set MIGS channel (ONLY for BCA, BRI and Maybank Acquiring bank)
creditCard.setChannel(CreditCard.MIGS);

// Set credit card options into transaction request
transactionRequest.setCreditCard(creditCard);
```

Note: 
- This was assumed that you have created `transactionRequest` object using required parameters.
- You can only use 1 method when making payment.
- For more details about Credit Card payment please read Credit Card section.
#### Set Transaction Request into SDK Instance

After creating transaction request, you must set it into SDK instance before making payment.

```
MidtransSDK.getInstance().setTransactionRequest(transactionRequest);
```

Note: 
- `transactionRequest` is `TransactionRequest` object created in previous step.