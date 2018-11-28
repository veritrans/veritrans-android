# Midtrans SDK Guide

## Overview
Midtrans Android SDK makes it easy to build an excellent payment experience in your native Android application. It provides powerful, customizable to collect your users' payment details.

We also expose the low-level APIs that power those elements to make it easy to build fully custom forms. This guide will take you all the way from integrating our SDK to accepting payments from your users via our payment method that we provide.

## Supported Payments
1. Credit Card
2. VA / Bank Transfer
3. CIMB Clicks
4. Indomaret
5. BCA KlikPay
6. Klikbca
7. Mandiri E-Cash
8. Mandiri Clickpay
9. BRI E-Pay
10. GO-PAY
11. Akulaku

## Prerequsites

There are four parties involved in the payment process for making a payment:

1. Merchant Server: The merchant backend implementation
2. Merchant Host app
3. Midtrans Mobile SDK
4. Midtrans Backend

And there are some step you must do before using Midtrans SDK :

1. Create a merchant account in MAP.
2. Implementing Merchant Server. 
3. Setup your merchant accounts settings, in particular Notification URL.

Midtrans SDK General Transaction Flow :

**1. Initialize the SDK**
> Before all payment flow started, Host-app need to intialize SDK from library, set credentials and configure all need for checkout.

**2. Checkout**
> Host-app making request object before calling checkout method from Midtrans SDK. If success it will return SnapToken as main token for all transaction method in Midtrans SDK.

**3. Payment Info**
> After checkouting, Host-app can get the detail of SnapToken information by calling payment info method from Midtrans SDK. It will return all of detail for making payment like enbale payment method, etc.

**4. Starting Payment**
> Host-app can pass begin payment with calling start payment method and pass needed parameter, it will return object based on payment method. Some payment method will return your transaction number for making payment, the other return url link for completion payment and you must to open the url for finish it.

## Midtrans SDK Guide

1. Installation
2. Configure & Initialize SDK
3. Quickstart
4. Detailed Explanation
	* Method Documentation
	* Response
	* Error
5. Feature
 	* Currency
 	* Customer Info, Billing Info, Shipping Info
	* Items Detail
	* Enable Payment
   	* Custom expired
   	* Custom fields
	* Mandiri Bill Options
	* GO-PAY Options
   	* Credit card options

## <a id="installation"></a>Installation
We support installation from gradle dependencies.

1. Add Midtrans bintray repository classpath to your `build.gradle`.

	*Midtrans Bintray Repository*
	
	```Groovy
	repositories {
	    jcenter()
	    // Add the midtrans repository into the list of repositories
	    maven { url "http://dl.bintray.com/pt-midtrans/maven" }
	    maven { url "https://jitpack.io" }
	}
	```
2. And put Midtrans SDK dependencies to gradle app level.

	*Dependencies*
	
	```Groovy
	implementation 'com.midtrans.corekit:$VERSION'
	```


## <a id="configure-and-initialize-sdk"></a>Configure & Initialize SDK

This can be implemented in application class or your main activity class after you install Midtrans SDK from your gradle.

```Java
MidtransSDK
            .builder(this,
            	  CLIENT_KEY,
                  MERCHANT_BASE_URL)
            .setLogEnabled(true)
            .setEnvironment(Environment.SANDBOX)
            .build();
```

Note:

- `CONTEXT` is application or activity context and this is mandatory for Android SDK implementation.
- `CLIENT_KEY` is Midtrans Client Key (you can get from MAP) and this is mandatory.
- `BASE_URL` is merchant server base URL and this is mandatory.
- `Environment` is environment you want to use, if you not set it will be `SANDBOX` as default.
- `Logger` can set by putting value true or false for showing SDK log, this is optional and default value is false.
- For merchant server implementation please see [this page](https://github.com/veritrans/veritrans-android/wiki/Implementation-for-Merchant-Server).

To get SDK instance you must init the SDK first and you can use this:

```Java
MidtransSDK midtransSDK = MidtransSDK.getInstance();
```
SDK instance is a simple way to access and implement all public method from Midtrans Core SDK that already initialize before, so you only need once to initialize the SDK then you can only access it from instance, if you not initialize the SDK first instance will return null and runtime error.


## Quickstart

> **This guide assumes you've already follow Installation and Configure & Initialize SDK section.**

**For quick payment, first step is doing checkout, checkout provides your customers with a streamlined, mobile-ready payment experience.**

Checkout securely accepts your customer's payment details and directly passes them to Midtrans servers. Midtrans returns a token representation of those payment details, which can then be submitted to your server for use.
	
Our SDK provides a class called `CheckoutTransaction`, which is designed to make building your app's checkout flow as easy as possible. It handles payment options such as payment chanels, customer information and can also be used to collect shipping info.

### 1. Prepare your checkout (Mandatory)

For starting payment with Midtrans, you'll need to write a `CheckoutTransaction` builder inside your class and set to the `MidtransSDK` instance before checkout. (Note, the code samples in this section are simply examples – your own implementation may differ depending on the structure of your app). Midtrans Checkout has 2 required parameters:

 1. **TRANSACTION_ID**
 
 This value must be unique, you can use it only once
 
 2. **AMOUNT**
 
 This value is your amount for making payment


Then you can put it all together to generate the `Snap Token` (payment token). Since `TRANSACTION_ID` and `AMOUNT` was required and mandatory, to create a transaction request you should use this builder, so the builder will return minimum object required for making payment.

```Java
CheckoutTransaction checkoutTransaction = CheckoutTransaction
                .builder(TRANSACTION_ID, AMOUNT)
                .build();
```
Note :

- `TRANSACTION_ID` is an unique id for your transaction, maximum character length is 50.
- `AMOUNT` is charge amount.

### 2. Starting checkout

That's minimum request for making payment, all of payment method you activated will be default setting. Then for making payment you can simply set the `CheckoutTransaction` object to the SDK Instance.

```Java
MidtransSdk.getInstance().setTransactionRequest(checkoutTransaction);
MidtransSdk.getInstance().checkout(new MidtransCallback<CheckoutResponse>() {
            @Override
            public void onFailed(Throwable throwable) {
                Logger.debug("Failed return error >>> " + throwable.getMessage());
            }

            @Override
            public void onSuccess(CheckoutResponse data) {
                Logger.debug("Success return snapToken " + data.getSnapToken());
            }
        });
```

Note: 

- This work with all default setting because it only pass `TRANSACTION_ID` and `AMOUNT`, not the custom setting. For custom setting please read next section.

## Detail Explanation

### Begin Checkout
- **Request Object**
- **Response**
- **Error Result**

### Getting Payment Information
- **Request Object**
- **Response**
- **Error Result**

### Starting Payment
- **Request Object**
- **Response**
- **Error Result**

## Feature
This guide covers how to use the individual feature of our SDK.

> **From this part, it will assumes you've already declare or making CheckoutTransaction object as previous section step. And assume you construct CheckoutTransaction with builder for all payment method, please use your needed.**

### Multi Currency

Multi currency is a feature to allow for multiple various currency display formatting. Currently you can use supported currency IDR (Indonesian Rupiah) and SGD (Singaporean Dollar). If you not set the currency, Midtrans will set it as Indonesian Rupiah or IDR.

```Java
CheckoutTransaction checkoutTransaction = CheckoutTransaction
                .builder(TRANSACTION_ID, AMOUNT)
                .setCurrency(Currency.IDR)
                .build();
```

### Customer Info, Billing Info, Shipping Info

The Customer Info, Billing Info, and Shipping Info is optional, so user can pass the customer detail when making transaction and get it in response from SDK.

```Java
CheckoutTransaction checkoutTransaction = CheckoutTransaction
                .builder(TRANSACTION_ID, AMOUNT)
                .setCustomerDetails(new CustomerDetails("First Name",
                        "Lastname",
                        "email@mail.com",
                        "6281234567890",
                        new ShippingAddress("First Name",
                                "Lastname",
                                "email@mail.com",
                                "City",
                                "1234",
                                "6281234567890",
                                "IDN"),
                        new BillingAddress("First Name",
                                "LastName",
                                "email@mail.com",
                                "City",
                                "1234",
                                "6281234567890",
                                "IDN")))
                .build();
```

### Item Details

You can set the detail of item of the transaction. If you put the item detail, you have to check the amount that previously you set in builder is same with total of price multiplied by quantity in Item Details ArrayList.

Item details are **required** for `Mandiri Bill/Mandiri Echannel` and `BCA KlikPay` payment, but it is **optional** for **other payment methods**. ItemDetails class holds information about item purchased by user. CheckoutTransaction takes an array list of item details.

```Java
CheckoutTransaction checkoutTransaction = CheckoutTransaction
                .builder(TRANSACTION_ID, AMOUNT)
                .setItemDetails(new ArrayList<>(Arrays.asList(
                        new ItemDetails(ITEM_ID,ITEM_PRICE,ITEM_QUANTITY,ITEM_NAME))
                ))
                .build();
```

### Enable payment

If you want to enable/disable some payment method without change the MAP setting or calling Midtrans's Support, you can set payment you want to activate.

```Java
CheckoutTransaction checkoutTransaction = CheckoutTransaction
                .builder(TRANSACTION_ID, AMOUNT)
                .setEnabledPayments(new ArrayList<>(Arrays.asList(
                        PaymentType.BCA_VA,
                        PaymentType.BNI_VA,
                        PaymentType.PERMATA_VA,
                        PaymentType.CREDIT_CARD,
                        PaymentType.GOPAY,
                        PaymentType.INDOMARET,
                        PaymentType.KLIK_BCA)))
                .build();
```
### Custom Expired

There is a feature on mobile SDK to enable custom transaction lifetime. 

| Parameter 	| Description                                                                                                                     	| Type            	|
|-----------	|---------------------------------------------------------------------------------------------------------------------------------	|-----------------	|
| startTime 	| Timestamp in yyyy-MM-dd HH:mm:ss Z format. If not specified, transaction time will be used as start time (when customer charge) 	| String          	|
| duration  	| Expiry duration                                                                                                                 	| Integer         	|
| unit      	| Expiry unit. (day, hour, minute)                                                                                                	| ExpiryModelUnit 	|

```Java
CheckoutTransaction checkoutTransaction = CheckoutTransaction
                .builder(TRANSACTION_ID, AMOUNT) 
                .setExpiry(new ExpiryModel("", ExpiryModelUnit.EXPIRY_UNIT_DAY, 1))
                .build();
```

### Custom Field

These 3 fields will be brought at payment so it will be available at MAP and a HTTP notification will be sent to the merchant.

```Java
CheckoutTransaction checkoutTransaction = CheckoutTransaction
                .builder(TRANSACTION_ID, AMOUNT)
                .setCustomField1("Custom Field 1")
                .setCustomField2("Custom Field 2")
                .setCustomField3("Custom Field 3")
                .build();
```

### GO-PAY Callback Deeplink (Specific for GO-PAY)

GO-PAY provide 2 type result of transaction that can be received by host app, first method is with callback deeplink (to host-app) and the second is without callback deeplink. If you want to use callback deeplink for getting result from GO-JEK app, please set your deeplink to `CheckoutTransaction` object that previously you created. 

For example if you set your deeplink like this `demo:://midtrans` then GO-JEK app will return callback like this for success `demo://midtrans?order_id=xxxx&result=success` and this for failure `demo://midtrans?order_id=xxxx&result=failure`.

```Java
CheckoutTransaction checkoutTransaction = CheckoutTransaction
                .builder(TRANSACTION_ID, AMOUNT)
                .setGopayCallbackDeepLink("demo://midtrans")
                .build();
```
### Bill Info (Specific for Mandiri Bill/Mandiri Echannel)

Bill Info is optional for `Mandiri Bill/Mandiri Echannel` payment only.

```Java
CheckoutTransaction checkoutTransaction = CheckoutTransaction
                .builder(TRANSACTION_ID, AMOUNT)
                .setBillInfoModel(new BillInfoModel("Note 1", "Note 2"))
                .build();
```
### Sub Company Code and Custom Virtual Account Number (Specific for BCA Bank Transfer VA)

This feature allows you to pass sub company code in VA payment and Make Custom Virtual Account Number. The sub company code must be exactly 5 digits of number. And you can pass free text to the inquiry and payment with multi language (Indonesia and English)


```Java
CheckoutTransaction checkoutTransaction = CheckoutTransaction
                .builder(TRANSACTION_ID, AMOUNT)
                .setBcaVa(new BcaBankTransferRequestModel("12345",
                        new BcaBankFreeText(new ArrayList<BcaBankFreeTextLanguage>(),
                        new ArrayList<BcaBankFreeTextLanguage>()),
                        "123123"))
                .build();
```
### Custom Virtual Account Number (Permata and BNI)

This feature allows you to make Custom Virtual Account Number.


```Java
CheckoutTransaction checkoutTransaction = CheckoutTransaction
                .builder(TRANSACTION_ID, AMOUNT)
                .setBniVa(new BankTransferRequestModel("123123"))
                .setPermataVa(new BankTransferRequestModel("123123"))
                .build();
```

### Complete Transaction Request

```Java
CheckoutTransaction checkoutTransaction = CheckoutTransaction
                .builder(TRANSACTION_ID, AMOUNT)
                .setCurrency(Currency.IDR)
                .setCustomerDetails(new CustomerDetails("First Name",
                        "Lastname",
                        "email@mail.com",
                        "6281234567890",
                        new ShippingAddress("First Name",
                                "Lastname",
                                "email@mail.com",
                                "City",
                                "1234",
                                "6281234567890",
                                "IDN"),
                        new BillingAddress("First Name",
                                "LastName",
                                "email@mail.com",
                                "City",
                                "1234",
                                "6281234567890",
                                "IDN")))
                .setItemDetails(new ArrayList<>(Arrays.asList(
                        new ItemDetails(ITEM_ID,ITEM_PRICE,ITEM_QUANTITY,ITEM_NAME))
                ))
                .setEnabledPayments(new ArrayList<>(Arrays.asList(
                        PaymentType.BCA_VA,
                        PaymentType.BNI_VA,
                        PaymentType.PERMATA_VA,
                        PaymentType.CREDIT_CARD,
                        PaymentType.GOPAY,
                        PaymentType.INDOMARET,
                        PaymentType.KLIK_BCA)))
                .setExpiry(new ExpiryModel("", ExpiryModelUnit.EXPIRY_UNIT_DAY, 1))
                .setCustomField1("Custom Field 1")
                .setCustomField2("Custom Field 2")
                .setCustomField3("Custom Field 3")
                .setGopayCallbackDeepLink("demo://midtrans")
                .setBillInfoModel(new BillInfoModel("Note 1", "Note 2"))
                .setBcaVa(new BcaBankTransferRequestModel("12345",
                        new BcaBankFreeText(new ArrayList<BcaBankFreeTextLanguage>(),
                        new ArrayList<BcaBankFreeTextLanguage>()),
                        "123123"))
                .setBniVa(new BankTransferRequestModel(""))
                .setPermataVa(new BankTransferRequestModel(""))
                .build();
```