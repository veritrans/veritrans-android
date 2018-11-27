# Midtrans SDK Guide

## Overview
Midtrans Android SDK makes it easy to build an excellent payment experience in your native Android application. It provides powerful, customizable to collect your users' payment details.

We also expose the low-level APIs that power those elements to make it easy to build fully custom forms. This guide will take you all the way from integrating our SDK to accepting payments from your users via our payment method that we provide.

## Prerequsites

1. Create a merchant account in MAP
2. Setup your merchant accounts settings, in particular Notification URL.
3. [Installation] (#installation)
4. [Configure & Initialize SDK] (#configure-and-initialize-sdk)
5. [Checkout] (#sdk-checkout)
 * [Standard] (#sdk-checkout-standard)
 * [Custom with Options] (#sdk-checkout-custom)
 		- [Customer info](#sdk-checkout-custom-customer-info)
		- [Items info](#sdk-checkout-custom-items-info)
   		- [Credit card options] (#sdk-checkout-custom-items-cc-options)
   		- [Gopay options] (#sdk-checkout-custom-items-gopay-options)
   		- Custom expired
   		- Custom fields
 * Get payment info
 * Charge
 		- Credit Card
		- VA / Bank Transfer
		- CIMB Clicks
		- Indomaret
		- BCA KlikPay
		- Klikbca
		- Mandiri E-Cash
		- Mandiri Clickpay
		- BRI E-Pay
		- Kios ON
		- Akulaku


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
            .init(this,
            	  BuildConfig.CLIENT_KEY,
                  BuildConfig.BASE_URL)
            .setLogEnabled(true)
            .setEnvironment(Environment.SANDBOX)
            .buildSDK();
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

## <a id="sdk-checkout"></a> Checkout
**Checkout provides your customers with a streamlined, mobile-ready payment experience.**

Checkout securely accepts your customer's payment details and directly passes them to Midtrans servers. Midtrans returns a token representation of those payment details, which can then be submitted to your server for use.
	
###<a id="sdk-checkout-standard"></a> Standard

Our SDK provides a class called `TransactionRequest`, which is designed to make building your app's checkout flow as easy as possible. It handles payment options such as payment chanels, customer information and can also be used to collect shipping info.

**Setting your checkout**

To work with Midtrans Checkout, you'll need to write a `TransactionRequest` builder inside your class before making payment and set to the `MidtransSDK` instance. (Note, the code samples in this section are simply examples – your own implementation may differ depending on the structure of your app). Midtrans Checkout has 2 required parameters:

 1. **TRANSACTION_ID**
 
 This value must be unique, you can use it only once
 
 2. **AMOUNT**
 
 This value is your amount for making payment


Then you can put it all together to generate the `Snap Token` (payment token). Since `TRANSACTION_ID` and `AMOUNT` was required and mandatory, to create a transaction request you should use this builder, so the builder will return minimum object required for making payment.

```Java
TransactionRequest transactionRequest = TransactionRequest
                .builder(TRANSACTION_ID, AMOUNT)
                .build();
```
Note :

- `TRANSACTION_ID` is an unique id for your transaction, maximum character length is 50.
- `AMOUNT` is charge amount.

That's minimum request for making payment, all of payment method you activated will be default setting. Then for making payment you can simply set the `TransactionRequest` object to the SDK Instance.

```Java
MidtransSdk.getInstance().setTransactionRequest(transactionRequest);
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

- This work with all default setting because it only pass `TRANSACTION_ID` and `AMOUNT`.