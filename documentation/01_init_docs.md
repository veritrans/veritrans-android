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

## Quick Start

1. Installation
2. Configure & Initialize SDK
3. Quick Payment
4. SDK API Method Explanation
	* Get Payment Info
	* Starting Payment
		* Bank Transfer / VA
		* CIMB Clicks
		* Indomaret
		* BCA Klikpay
		* Klik BCA
		* Mandiri E-Cash
		* Mandiri Clickpay
		* BRI E-pay
		* GO-PAY
		* Akulaku
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
6. Status Code
7. Error Message

## Installation
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


## Configure & Initialize SDK

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


## Quick Payment

> **This guide assumes you've already follow Installation and Configure & Initialize SDK section.**

**This is quick payment guide, it's use very minimum requirements for making payment. First step is doing checkout, checkout provides your customers with a streamlined, mobile-ready payment experience.**

Our SDK provides a class called `CheckoutTransaction`, which is designed to make building your app's checkout flow as easy as possible. It handles payment options such as payment chanels, customer information and can also be used to collect shipping info.

### 1. Prepare your CheckoutTransaction (Mandatory)
> **This is very minimum requirements for making CheckoutTransaction object, it's will use many default setting. If you want to use some specific feature or other feature, please follow section `FEATURE` and combine it with this minimum CheckoutTransaction object.**

Checkout securely accepts your customer's payment details and directly passes them to Midtrans servers. Midtrans returns a token representation of those payment details, which can then be submitted to your server for use.

For starting payment with Midtrans, you'll need to write a `CheckoutTransaction` builder inside your class and set to the `MidtransSDK` instance before checkout. (Note, the code samples in this section are simply examples – your own implementation may differ depending on the structure of your app). Midtrans Checkout has 2 required parameters:

 1. **TRANSACTION_ID**
 
 This value must be unique, you can use it only once
 
 2. **AMOUNT**
 
 This value is your amount for making payment


Then you can put it all together to generate the `Snap Token` (payment token). Since `TRANSACTION_ID` and `AMOUNT` was required and mandatory, to create a CheckoutTransaction you should use this builder, so the builder will return minimum object required for making payment.

```Java
CheckoutTransaction checkoutTransaction = CheckoutTransaction
                .builder(TRANSACTION_ID, AMOUNT)
                .build();
```
Note :

- `TRANSACTION_ID` is an unique id for your transaction, maximum character length is 50.
- `AMOUNT` is charge amount.

### 2. Starting Checkout

That's minimum request for making payment, all of payment method you activated will be in default setting. Then for making payment you can simply pass the `CheckoutTransaction` object to the checkout method. Checkout return SnapToken as key for all other method in Midtrans SDK.

```Java
MidtransSdk.getInstance().checkoutWithTransaction(checkoutTransaction, new MidtransCallback<CheckoutWithTransactionResponse>() {
            @Override
            public void onFailed(Throwable throwable) {
                Logger.debug("Failed return error >>> " + throwable.getMessage());
            }

            @Override
            public void onSuccess(CheckoutWithTransactionResponse data) {
                Logger.debug("Success return snapToken " + data.getSnapToken());
            }
        });
```

Note: 

- This work with all default setting because it only pass `TRANSACTION_ID` and `AMOUNT`, not the custom setting. For custom setting please read next section.

### 3. Get Payment Info

After you checkout it will return SnapToken, and you can use snapToken for getting payment information. Before making payment you can get payment information by passing snap token to the method we provide, it will return all information for your account setting like enable payment, whitelist bin, blacklist bin, etc, so you can make your own layout for showing the payment method option and many more all you want. The response will wrap into `PaymentInfoResponse` model.

```Java
MidtransSdk.getInstance().getPaymentInfo(SNAP_TOKEN, new MidtransCallback<PaymentInfoResponse>() {
            @Override
            public void onSuccess(PaymentInfoResponse data) {
                Logger.debug("RESULT SUCCESS PAYMENT INFO ");
            }

            @Override
            public void onFailed(Throwable throwable) {
                Logger.debug("MIDTRANS SDK NEW RETURN ERROR >>> " + throwable.getMessage());

            }
        });
```

### 4. Starting Payment

To making payment, you need a SnapToken from `checkoutWithTransaction()` to identify which transaction will be paid, payment parameters, and available payment method for making payment. Midtrans SDK provide method for each payment, but if method not available and you still use that it never been pay. For example we use GO-PAY for making payment.

```Java
MidtransSdk.getInstance().paymentUsingGopay(SNAP_TOKEN,
                GOPAY_ACCOUNT_NUMBER,
                new MidtransCallback<BasePaymentResponse>() {
                    @Override
                    public void onSuccess(BasePaymentResponse data) {
                			Logger.debug("RESULT SUCCESS, CONTINUE PAYMENT BASED ON RESPONSE");  
                    }

                    @Override
                    public void onFailed(Throwable throwable) {
                			Logger.debug("MIDTRANS SDK NEW RETURN ERROR >>> " + throwable.getMessage()); 
                    }
                });
```
Some Payment Method need specific parameter, for example GO-PAY need `GOPAY_ACCOUNT_NUMBER` as specific paramter for making payment with GO-PAY.

Table of payment codes for payment.

| Payment Type     | Payment Name           |
| ---------------- | ---------------------- |
| CREDIT_CARD      | Credit Card            |
| E_CHANNEL          | Mandiri Bill           |
| BCA_VA           | Bank Transfer BCA      |
| BNI_VA           | Bank Transfer BNI      |
| PERMATA_VA       | Bank Transfer Permata  |
| OTHER_VA         | Bank Transfer All Bank |
| DANAMON_ONLINE   | Bank Transfer Danamon  |
| KLIK_BCA      | KlikBCA                |
| BCA_KLIKPAY      | BCA Klikpay            |
| MANDIRI_CLICKPAY | Mandiri Clickpay       |
| CIMB_CLICKS      | CIMB Clicks            |
| BRI_EPAY         | BRI E-Pay              |
| GOPAY            | Gopay                  |
| MANDIRI_ECASH    | Mandiri E-Cash         |
| TELKOMSEL_CASH   | Telkomsel Cash         |
| INDOMARET        | Indomaret              |
| AKULAKU          | Akulaku                |

## SDK API Method Explanation

### Begin Checkout With Transaction
- **Request Object**

	Checkout need `CheckoutTransaction` object as parameter for making payment, for making `CheckoutTransaction` object please follow this instruction.

	> **Once again, this is very minimum requirements for making CheckoutTransaction object and only sample for you, so it will use default setting. If you want to use some specific feature or other feature, please follow section `FEATURE` and combine it with this minimum CheckoutTransaction object.**

	For starting payment with Midtrans, you'll need to write a `CheckoutTransaction` builder inside your class and set to the `MidtransSDK` instance before checkout. (Note, the code samples in this section are simply examples – your own implementation may differ depending on the structure of your app). Midtrans Checkout has 2 required parameters:

 **TRANSACTION_ID**
 
 This value must be unique, you can use it only once
 
 **AMOUNT**
 
 This value is your amount for making payment

	Since `TRANSACTION_ID` and `AMOUNT` was required and mandatory, to create a checkout request you should use this builder, so the builder will return minimum object required for making payment.
	
	```Java
	CheckoutTransaction checkoutTransaction = CheckoutTransaction
	                .builder(TRANSACTION_ID, AMOUNT)
	                .build();
	```
	Note :
	
	- `TRANSACTION_ID` is an unique id for your transaction, maximum character length is 50.
	- `AMOUNT` is charge amount.

- **The Method**
	
	Put CheckoutTransaction object together with callback in `checkoutWithTransaction()` method.

	```Java
MidtransSdk.getInstance().checkoutWithTransaction(checkoutTransaction, new MidtransCallback<CheckoutWithTransactionResponse>() {
            @Override
            public void onFailed(Throwable throwable) {
                Logger.debug("Failed return error >>> " + throwable.getMessage());
            }

            @Override
            public void onSuccess(CheckoutWithTransactionResponse data) {
                Logger.debug("Success return snapToken " + data.getSnapToken());
            }
        });
```

- **Success Midtrans Callback**
	
	Succes response will return `CheckoutWithTransactionResponse` as model, you can access it from `MidtransCallback` interface and here's the detail of `CheckoutWithTransactionResponse` model.

	| Property Name    | Type           |
	| ---------------- | ---------------------- |
	| errorMessage      | `ArrayList<String> `           |
	| snapToken      | `String`          |
	
- **Failed Midtrans Callback**
	
	Failed response will return `Throwable` as model, you can get error message and identify why it happen. Midtrans SDK provide some validation and it will return here if not pass the validation. So Failed Midtrans callback will return all of error which is from Midtrans SDK validation or from other source like network error, etc. Here's error message from Midtrans SDK Validation

	| Message    | Cause           |
	| ---------------- | ---------------------- |
	| Snap Token must not empty.      | You not put the SnapToken and keep it null or empty           |
	| Merchant base url is empty. Please set merchant base url on SDK.      | You not set the `MERCHATN_BASE_URL` so SDK cannot making network request.          |
	| Failed to retrieve response from server.      | Server response is success but it never return anyting.           |
	| Error message not catchable.      | SDK cannot catch the error.           |
	| Failed to connect to server.      | You not connected to any internet connection.           |

### Getting Payment Information
- **The Method**
- **Success Midtrans Callback**
- **Failed Midtrans Callback**

### Payment Using BCA Bank Transfer/VA
- **Request Object**
- **The Method**
- **Success Midtrans Callback**
- **Failed Midtrans Callback**

### Payment Using BNI Bank Transfer/VA
- **Request Object**
- **The Method**
- **Success Midtrans Callback**
- **Failed Midtrans Callback**

### Payment Using PERMATA Bank Transfer/VA
- **Request Object**
- **The Method**
- **Success Midtrans Callback**
- **Failed Midtrans Callback**

### Payment Using Others Bank Transfer/VA
- **Request Object**
- **The Method**
- **Success Midtrans Callback**
- **Failed Midtrans Callback**

### Payment Using Mandiri E-Cash
- **Request Object**
- **The Method**
- **Success Midtrans Callback**
- **Failed Midtrans Callback**

### Payment Using Mandiri ClickPay
- **Request Object**
- **The Method**
- **Success Midtrans Callback**
- **Failed Midtrans Callback**

### Payment Using CIMB Clicks
- **Request Object**
- **The Method**
- **Success Midtrans Callback**
- **Failed Midtrans Callback**

### Payment Using Akulaku
- **Request Object**
- **The Method**
- **Success Midtrans Callback**
- **Failed Midtrans Callback**

### Payment Using GO-PAY
- **Request Object**
- **The Method**
- **Success Midtrans Callback**
- **Failed Midtrans Callback**

### Payment Using Telkomsel Cash (T-Cash)
- **Request Object**
- **The Method**
- **Success Midtrans Callback**
- **Failed Midtrans Callback**

### Payment Using Indomaret
- **Request Object**
- **The Method**
- **Success Midtrans Callback**
- **Failed Midtrans Callback**

### Payment Using BRI E-PAY
- **Request Object**
- **The Method**
- **Success Midtrans Callback**
- **Failed Midtrans Callback**

### Payment Using Klik BCA
- **Request Object**
- **The Method**
- **Success Midtrans Callback**
- **Failed Midtrans Callback**

## Feature
This guide covers how to use the individual feature of our SDK.

> **From this part, it will assumes you've already declare or making CheckoutTransaction object as previous section step. And assume you construct CheckoutTransaction with builder for all payment method because most of feature will configure in CheckoutTransaction object, please use your needed feature.**

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
                        BCA_VA,
                        BNI_VA,
                        PERMATA_VA,
                        CREDIT_CARD,
                        GOPAY,
                        INDOMARET,
                        KLIK_BCA)))
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

### Complete Checkout Transaction Object

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
                        BCA_VA,
                        BNI_VA,
                        PERMATA_VA,
                        CREDIT_CARD,
                        GOPAY,
                        INDOMARET,
                        KLIK_BCA)))
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

## Status Code

## Error Message