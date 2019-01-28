# Midtrans SDK Guide

##  1. <a name='Overview'></a>Overview
Midtrans Android SDK makes it easy to build an excellent payment experience in your native Android application. It provides powerful, customizable to collect your users' payment details.

We also expose the low-level APIs that power those elements to make it easy to build fully custom forms. This guide will take you all the way from integrating our SDK to accepting payments from your users via our payment method that we provide.

##  2. <a name='SupportedPayments'></a>Supported Payments
1. Credit Card
2. VA / Bank Transfer
	* Bank BNI
	* Bank Permata
	* Bank BCA
3. Cardless Credit
	* Akulaku
4. Direct Debit
	* Klik BCA
	* Mandiri Click Pay
5. E - Wallet
	* Mandiri E - Cash
	* Gopay
	* Telkomsel Cash
6. Online Debit Charge
	* Cimb Clicks
	* BCA Click Pay
	* BRI E - Pay
7. Store
	* Indomaret
8. CreditCard
	* Tokenize
	* Save Card
	* Charge (Payment)

##  3. <a name='Prerequsites'></a>Prerequsites

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
> Host-app making request object before calling checkout method from Midtrans SDK. If success it will return token as main token for all transaction method in Midtrans SDK.

**3. Payment Info**
> After checkouting, Host-app can get the detail of token information by calling payment info method from Midtrans SDK. It will return all of detail for making payment like enbale payment method, etc.

**4. Starting Payment**
> Host-app can pass begin payment with calling start payment method and pass needed parameter, it will return object based on payment method. Some payment method will return your transaction number for making payment, the other return url link for completion payment and you must to open the url for finish it.

##  4. <a name='QuickStart'></a>Quick Start

1. Installation
2. Configure & Initialize SDK
3. Quick Payment
4. SDK API Method Explanation
	* Get Payment Info
	* Starting Payment 
		+ VA / Bank Transfer 
			- Bank BNI 
			- Bank Permata
			- Bank BCA                                   
		+ Cardless Credit
			- Akulaku 
		+ Direct Debit
			- Klik BCA 
			- Mandiri Click Pay                
		+ E - Wallet
			- Mandiri E - Cash
			- Gopay
			- Telkomsel Cash
		+ Online Debit Charge
			- Cimb Clicks
			- BCA Click Pay
			- BRI E - Pay
		+ Store
			- Indomaret
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

##  5. <a name='Installation'></a>Installation
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


##  6. <a name='ConfigureInitializeSDK'></a>Configure & Initialize SDK

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

```Kotlin
MidtransSdk
            .builder(this,
                BuildConfig.CLIENT_KEY,
                BuildConfig.BASE_URL)
            .setApiRequestTimeOut(40)
            .setEnvironment(Environment.SANDBOX)
            .setLogEnabled(true)
            .build()
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

```Kotlin
val midtransSDK = MidtransSDK.getInstance();
```

SDK instance is a simple way to access and implement all public method from Midtrans Core SDK that already initialize before, so you only need once to initialize the SDK then you can only access it from instance, if you not initialize the SDK first instance will return null and runtime error.


##  7. <a name='QuickPayment'></a>Quick Payment

> **This guide assumes you've already follow Installation and Configure & Initialize SDK section.**

**This is quick payment guide, it's use very minimum requirements for making payment. First step is doing checkout, checkout provides your customers with a streamlined, mobile-ready payment experience.**

Our SDK provides a class called `CheckoutTransaction`, whTich is designed to make building your app's checkout flow as easy as possible. It handles payment options such as payment chanels, customer information and can also be used to collect shipping info.

###  7.1. <a name='PrepareyourCheckoutTransactionMandatory'></a>1. Prepare your CheckoutTransaction (Mandatory)
> **This is very minimum requirements for making CheckoutTransaction object, it's will use many default setting. If you want to use some specific feature or other feature, please follow section `FEATURE` and combine it with this minimum CheckoutTransaction object.**

Checkout securely accepts your customer's payment details and directly passes them to Midtrans servers. Midtrans returns a token representation of those payment details, which can then be submitted to your server for use.

For starting payment with Midtrans, you'll need to write a `CheckoutTransaction` builder inside your class and set to the `MidtransSDK` instance before checkout. (Note, the code samples in this section are simply examples – your own implementation may differ depending on the structure of your app). Midtrans Checkout has 2 required parameters:

 1. **ORDER_ID**
 
 This value must be unique, you can use it only once
 
 2. **AMOUNT**
 
 This value is your amount for making payment


Then you can put it all together to generate the `Snap Token` (payment token). Since `ORDER_ID` and `AMOUNT` was required and mandatory, to create a CheckoutTransaction you should use this builder, so the builder will return minimum object required for making payment.

```Java
CheckoutTransaction checkoutTransaction = CheckoutTransaction
                .builder(ORDER_ID, AMOUNT)
                .build();
```

```Kotlin
val checkoutTransaction = CheckoutTransaction
            .builder(ORDER_ID, AMOUNT)
            .build()
```

Note :

- `ORDER_ID` is an unique id for your transaction, maximum character length is 50.
- `AMOUNT` is charge amount.

###  7.2. <a name='StartingCheckout'></a>2. Starting Checkout

That's minimum request for making payment, all of payment method you activated will be in default setting. Then for making payment you can simply pass the `CheckoutTransaction` object to the checkout method. Checkout return token as key for all other method in Midtrans SDK.

```Java
MidtransSdk.getInstance().checkoutWithTransaction(checkoutTransaction, 
                new MidtransCallback<CheckoutWithTransactionResponse>() {
                
            @Override
            public void onFailed(Throwable throwable) {
                Logger.debug("Failed return error >>> " + throwable.getMessage());
            }

            @Override
            public void onSuccess(CheckoutWithTransactionResponse data) {
                Logger.debug("Success return token " + data.gettoken());
            }
        });
```

```Kotlin
MidtransSdk.getInstance().checkoutWithTransaction(checkoutTransaction,
            object : MidtransCallback<CheckoutWithTransactionResponse> {
            
                override fun onFailed(throwable: Throwable) {
                    Logger.debug("Failed return error >>> ${throwable.message}")
                }
                
                override fun onSuccess(data: CheckoutWithTransactionResponse) {
                    Logger.debug("Success return token ${data.token}")
                }
            })
```

Note: 

- This work with all default setting because it only pass `ORDER_ID` and `AMOUNT`, not the custom setting. For custom setting please read next section.

###  7.3. <a name='GetPaymentInfo'></a>3. Get Payment Info

After you checkout it will return token, and you can use token for getting payment information. Before making payment you can get payment information by passing snap token to the method we provide, it will return all information for your account setting like enable payment, whitelist bin, blacklist bin, etc, so you can make your own layout for showing the payment method option and many more all you want. The response will wrap into `PaymentInfoResponse` model.

```Java
MidtransSdk.getInstance().getPaymentInfo(TOKEN,
                new MidtransCallback<PaymentInfoResponse>() {
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

```Kotlin
MidtransSdk.getInstance().getPaymentInfo(TOKEN,
            object : MidtransCallback<PaymentInfoResponse> {
                override fun onSuccess(data: PaymentInfoResponse) {
                    Logger.debug("RESULT SUCCESS PAYMENT INFO")
                }

                override fun onFailed(throwable: Throwable) {
                    Logger.debug("Failed return error >>> ${throwable.message}")
                }
            })
```

###  7.4. <a name='StartingPayment'></a>4. Starting Payment

To making payment, you need a token from `checkoutWithTransaction()` to identify which transaction will be paid, payment parameters, and available payment method for making payment. Midtrans SDK provide method for each payment, but if method not available and you still use that it never been pay. For example we use GO-PAY for making payment.

```Java
EWalletCharge().paymentUsingGopay(TOKEN,
                GOPAY_ACCOUNT_NUMBER,
                new MidtransCallback<EwalletGopayPaymentResponse>() {
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

```Kotlin
EWalletCharge.paymentUsingGopay(TOKEN,
                GOPAY_ACCOUNT_NUMBER,
                new MidtransCallback<EwalletGopayPaymentResponse>() {
                    @Override
                    public void onSuccess(EwalletGopayPaymentResponse data) {

                    }

                    @Override
                    public void onFailed(Throwable throwable) {

                    }
                });
```

Some Payment Method need specific parameter, for example GO-PAY need `GOPAY_ACCOUNT_NUMBER` as specific paramter for making payment with GO-PAY.

Table of payment codes for payment.

| Group Payment   | Payment Type     | Payment Name          |
|-----------------|------------------|-----------------------|
| Bank Transfer   | BCA_VA           | Bank Transfer BCA     |
|                 | BNI_VA           | Bank Transfer BNI     |
|                 | PERMATA_VA       | Bank Transfer Permata |
| Cardless Credit | AKULAKU          | Akulaku               |
| Direct Debit    | KLIK_BCA         | Klik BCA              |
|                 | MANDIRI_CLICKPAY | Mandiri Clickpay      |
| E - Wallet      | MANDIRI_ECASH    | Mandiri E-Cash        |
|                 | GOPAY            | Gopay                 |
|                 | TELKOMSEL_CASH   | TELKOMSEL_CASH        |
| Online Debit    | CIMB_CLICKS      | CIMB Clicks           |
|                 | BCA_KLIKPAY      | BCA Klikpay           |
|                 | BRI_EPAY         | BRI E-Pay             |
| Store           | INDOMARET        | Indomaret             |

##  8. <a name='SDKAPIMethodExplanation'></a>SDK API Method Explanation

###  8.1. <a name='BeginCheckoutWithTransaction'></a>Begin Checkout With Transaction
- **Request Object**

	Checkout need `CheckoutTransaction` object as parameter for making payment, for making `CheckoutTransaction` object please follow this instruction.

	> **Once again, this is very minimum requirements for making CheckoutTransaction object and only sample for you, so it will use default setting. If you want to use some specific feature or other feature, please follow section `FEATURE` and combine it with this minimum CheckoutTransaction object.**

	For starting payment with Midtrans, you'll need to write a `CheckoutTransaction` builder inside your class and set to the `MidtransSDK` instance before checkout. (Note, the code samples in this section are simply examples – your own implementation may differ depending on the structure of your app). Midtrans Checkout has 2 required parameters:
	
	**ORDER_ID**
	
	This value must be unique, you can use it only once
 
    **AMOUNT**
 
    This value is your amount for making payment Since `ORDER_ID` and `AMOUNT` was required and mandatory, to create a checkout request you should use this builder, so the builder will return minimum object required for making payment.
        
 	```Java
CheckoutTransaction checkoutTransaction = CheckoutTransaction
                .builder(ORDER_ID, AMOUNT)
                .build();
	```

	```Kotlin
val checkoutTransaction = CheckoutTransaction
            .builder(ORDER_ID, AMOUNT)
            .build()
	```
    
    **The Method**
	
	Put CheckoutTransaction object together with callback in `checkoutWithTransaction()` method.

	```Java
MidtransSdk.getInstance().checkoutWithTransaction(checkoutTransaction, 
                new MidtransCallback<CheckoutWithTransactionResponse>() {
                
            @Override
            public void onFailed(Throwable throwable) {
                Logger.debug("Failed return error >>> " + throwable.getMessage());
            }

            @Override
            public void onSuccess(CheckoutWithTransactionResponse data) {
                Logger.debug("Success return token " + data.gettoken());
            }
        });
	```

	```Kotlin
MidtransSdk.getInstance().checkoutWithTransaction(checkoutTransaction,
            object : MidtransCallback<CheckoutWithTransactionResponse> {
            
                override fun onFailed(throwable: Throwable) {
                    Logger.debug("Failed return error >>> ${throwable.message}")
                }
                
                override fun onSuccess(data: CheckoutWithTransactionResponse) {
                    Logger.debug("Success return token ${data.token}")
                }
            })
	```

- **Success Midtrans Callback**
	
	Succes response will return `CheckoutWithTransactionResponse` as model, you can access it from `MidtransCallback` interface and here's the detail of `CheckoutWithTransactionResponse` model.
	

	| Property Name    | Type           |
	| ---------------- | ---------------------- |
	| errorMessage      | `List<String> `           |
	| token      | `String`          |
	
	
- **Failed Midtrans Callback**
	
	Failed response will return `Throwable` as model, you can get error message and identify why it happen. Midtrans SDK provide some validation and it will return here if not pass the validation. So Failed Midtrans callback will return all of error which is from Midtrans SDK validation or from other source like network error, etc. Here's error message from Midtrans SDK Validation

	| Message                                                          	| Cause                                                                                                      	|
|------------------------------------------------------------------	|------------------------------------------------------------------------------------------------------------	|
| Snap Token must not empty.                                       	| You not put the token and keep it null or empty when making payment or something that need token.          	|
| Merchant base url is empty. Please set merchant base url on SDK. 	| You not set the `MERCHANT_BASE_URL`, so SDK cannot making network request. Please initialize SDK properly. 	|
| Failed to retrieve response from server.                         	| Network request to server is success but it not return anything.                                           	|
| Error message not catchable.                                     	| SDK cannot cacth the error.                                                                                	|
| Failed to connect to server.                                     	| You not connected to any internet connection.                                                              	|
| Invalid or empty data supplied to SDK.                           	| Empty or wrong data input to the SDK.                                                                      	|

###  8.2. <a name='BeginCheckoutWithTransaction'></a>Starting Payment

- **Request Object**
- **The Method**
- **Success Midtrans Callback**
- **Failed Midtrans Callback**

### 8.2.1. <a name='PaymentUsingBankTransfer'></a>Payment Using Bank Transfer/VA

#### Payment Using BCA Bank Transfer

- **Request Object**
        
	Payment using BCA Bank Transfer need `CustomerDetailPayRequest` object as paramater, for making `CustomerDetailPayRequest` object please follow this instruction.

	> **Once again, this is very minimum requirements for making CustomerDetailPayRequest object and only sample for you, so it will use default setting. If you want to use some specific feature or other feature, please follow section `FEATURE` and combine it with this minimum CheckoutTransaction object.**

	For starting payment using BCA Bank Transfer with Midtrans, you'll need to write a `CustomerDetailPayRequest` object inside your class (Note, the code samples in this section are simply examples – your own implementation may differ depending on the structure of your app). To make `CustomerDetailPayRequest` you will need 3 required parameters as an example below.
	
    ```Java
        String fullName = "fullName", email = "email@mail.com", phone = "08123456789";
        CustomerDetailPayRequest customerDetailPayReq = new CustomerDetailPayRequest(fullName, email, phone);
    ```
    
    ```Kotlin
        val fullName = "fullName"
        val email = "email@mail.com"
        val phone = "08123456789"
        val customerDetailPayReq = CustomerDetailPayRequest(fullName, email, phone)
    ```
	
- **The Method**

  ```Java
    BankTransferCharge.paymentUsingBankTransferVaBca(
                 TOKEN,
                 customerDetailPayReq,
                 new MidtransCallback<BasePaymentResponse>() {
                     @Override
                     public void onSuccess(BankTransferVaBcaPaymentResponse data) {
                         
                     }
  
                     @Override
                     public void onFailed(Throwable throwable) {
  
                     }
                 }
         );
  ```
  
  ```Kotlin
  BankTransferCharge.paymentUsingBankTransferVaBca(
            TOKEN,
            customerDetailPayReq,
            object : MidtransCallback<BankTransferVaBcaPaymentResponse> {
                override fun onSuccess(data: BankTransferVaBcaPaymentResponse?) {

                }

                override fun onFailed(throwable: Throwable?) {
                }
            })
  ```
  
- **Success Midtrans Callback**

  Succes response will return `BankTransferVaBcaPaymentResponse` as model, you can access   it from `MidtransCallback` interface and you need to use `BankTransferVaBcaPaymentResponse` to get any corresponding response you need from `BankTransferVaBcaPaymentResponse `. As an example below.
  
  ```Java
  String bcaVaNumber = data.getBcaVaNumber();
  ```
  
  ```Kotlin
  val bcaVaNumber = data?.bcaVaNumber
  ```
 
  
- **Failed Midtrans Callback**
	
	Failed response will return `Throwable` as model, you can get error message and identify why it happen. Midtrans SDK provide some validation and it will return here if not pass the validation. So Failed Midtrans callback will return all of error which is from Midtrans SDK validation or from other source like network error, etc. Here's error message from Midtrans SDK Validation
	
	| Message                                                          	| Cause                                                                                                      	|
|------------------------------------------------------------------	|------------------------------------------------------------------------------------------------------------	|
| Snap Token must not empty.                                       	| You not put the token and keep it null or empty when making payment or something that need token.          	|
| Merchant base url is empty. Please set merchant base url on SDK. 	| You not set the `MERCHANT_BASE_URL`, so SDK cannot making network request. Please initialize SDK properly. 	|
| Failed to retrieve response from server.                         	| Network request to server is success but it not return anything.                                           	|
| Error message not catchable.                                     	| SDK cannot cacth the error.                                                                                	|
| Failed to connect to server.                                     	| You not connected to any internet connection.                                                              	|
| Invalid or empty data supplied to SDK.                           	| Empty or wrong data input to the SDK.                                                                      	|

#### Payment Using BNI Bank Transfer/VA
- **Request Object**
        
	Payment using BNI Bank Transfer need `CustomerDetailPayRequest` object as paramater, for making `CustomerDetailPayRequest` object please follow this instruction.

	> **Once again, this is very minimum requirements for making CustomerDetailPayRequest object and only sample for you, so it will use default setting. If you want to use some specific feature or other feature, please follow section `FEATURE` and combine it with this minimum CheckoutTransaction object.**

	For starting payment using BNI Bank Transfer with Midtrans, you'll need to write a `CustomerDetailPayRequest` object inside your class (Note, the code samples in this section are simply examples – your own implementation may differ depending on the structure of your app). To make `CustomerDetailPayRequest` you will need 3 required parameters as an example below.
	
    ```Java
        String fullName = "fullName", email = "email@mail.com", phone = "08123456789";
        CustomerDetailPayRequest customerDetailPayReq = new CustomerDetailPayRequest(fullName, email, phone);
    ```
    
    ```Kotlin
        val fullName = "fullName"
        val email = "email@mail.com"
        val phone = "08123456789"
        val customerDetailPayReq = CustomerDetailPayRequest(fullName, email, phone)
    ```
	
- **The Method**

  ```Java
    BankTransferCharge.paymentUsingBankTransferVaBni(
                 TOKEN,
                 customerDetailPayReq,
                 new MidtransCallback<BankTransferVaBniPaymentResponse>() {
                     @Override
                     public void onSuccess(BankTransferVaBniPaymentResponse data) {
                      
                     }
 
                     @Override
                     public void onFailed(Throwable throwable) {
 
                     }
                 }
         );
    ```
    
  ```Kotlin
    BankTransferCharge.paymentUsingBankTransferVaBni(
            TOKEN,
            customerDetailPayReq,
            object : MidtransCallback<BankTransferVaBniPaymentResponse> {
                override fun onSuccess(data: BankTransferVaBniPaymentResponse?) {
                    val bcaVaNumber = data?.bcaVaNumber
                }

                override fun onFailed(throwable: Throwable?) {
                }
            })
    ```
    
- **Success Midtrans Callback**

  Succes response will return `BankTransferVaBniPaymentResponse` as model, you can access   it from `MidtransCallback` interface and you need to use `BankTransferVaBniPaymentResponse` to get any corresponding response you need from `BankTransferVaBniPaymentResponse `. As an example below.
  
  ```Java
  String bniVaNumber = data.getBniVaNumber();
  ```
  
  ```Kotlin
  val bniVaNumber = data?.bniVaNumber
  ```
  
- **Failed Midtrans Callback**
	
	Failed response will return `Throwable` as model, you can get error message and identify why it happen. Midtrans SDK provide some validation and it will return here if not pass the validation. So Failed Midtrans callback will return all of error which is from Midtrans SDK validation or from other source like network error, etc. Here's error message from Midtrans SDK Validation

	| Message                                                          	| Cause                                                                                                      	|
|------------------------------------------------------------------	|------------------------------------------------------------------------------------------------------------	|
| Snap Token must not empty.                                       	| You not put the token and keep it null or empty when making payment or something that need token.          	|
| Merchant base url is empty. Please set merchant base url on SDK. 	| You not set the `MERCHANT_BASE_URL`, so SDK cannot making network request. Please initialize SDK properly. 	|
| Failed to retrieve response from server.                         	| Network request to server is success but it not return anything.                                           	|
| Error message not catchable.                                     	| SDK cannot cacth the error.                                                                                	|
| Failed to connect to server.                                     	| You not connected to any internet connection.                                                              	|
| Invalid or empty data supplied to SDK.                           	| Empty or wrong data input to the SDK.                                                                      	|


#### Payment Using PERMATA Bank Transfer/VA
- **Request Object**
        
	Payment using PERMATA Bank Transfer need `CustomerDetailPayRequest` object as paramater, for making `CustomerDetailPayRequest` object please follow this instruction.

	> **Once again, this is very minimum requirements for making CustomerDetailPayRequest object and only sample for you, so it will use default setting. If you want to use some specific feature or other feature, please follow section `FEATURE` and combine it with this minimum CheckoutTransaction object.**

	For starting payment using PERMATA Bank Transfer with Midtrans, you'll need to write a `CustomerDetailPayRequest` object inside your class (Note, the code samples in this section are simply examples – your own implementation may differ depending on the structure of your app). To make `CustomerDetailPayRequest` you will need 3 required parameters as an example below.
		
    ```Java
        String fullName = "fullName", email = "email@mail.com", phone = "08123456789";
        CustomerDetailPayRequest customerDetailPayReq = new CustomerDetailPayRequest(fullName, email, phone);
    ```
    
    ```Kotlin
        val fullName = "fullName"
        val email = "email@mail.com"
        val phone = "08123456789"
        val customerDetailPayReq = CustomerDetailPayRequest(fullName, email, phone)
    ```
    
- **The Method**

  ```Java
    BankTransferCharge.paymentUsingBankTransferVaPermata(
                 TOKEN,
                 customerDetailPayReq,
                 new MidtransCallback<BankTransferVaPermataPaymentResponse>() {
                     @Override
                     public void onSuccess(BankTransferVaPermataPaymentResponse data) {
                      
                     }
 
                     @Override
                     public void onFailed(Throwable throwable) {
 
                     }
                 }
         );
    ```
    
  ```Kotlin
    BankTransferCharge.paymentUsingBankTransferVaPermata(
            TOKEN,
            customerDetailPayReq,
            object : MidtransCallback<BankTransferVaPermataPaymentResponse> {
                override fun onSuccess(data: BankTransferVaPermataPaymentResponse?) {

                }

                override fun onFailed(throwable: Throwable?) {
                }
            })
    ```

- **Success Midtrans Callback**

  Succes response will return `BankTransferVaPermataPaymentResponse` as model, you can access   it from `MidtransCallback` interface and you need to use `BankTransferVaPermataPaymentResponse ` to get any corresponding response you need from `BasePaymentResponse`. As an example below.
  
  ```Java
  String permataVaNumber = data.getPermataVaNumber();
  ```
  
  ```Kotlin
  val permataVaNumber = data?.permataVaNumber
  ```
  
- **Failed Midtrans Callback**
	
	Failed response will return `Throwable` as model, you can get error message and identify why it happen. Midtrans SDK provide some validation and it will return here if not pass the validation. So Failed Midtrans callback will return all of error which is from Midtrans SDK validation or from other source like network error, etc. Here's error message from Midtrans SDK Validation

	| Message                                                          	| Cause                                                                                                      	|
|------------------------------------------------------------------	|------------------------------------------------------------------------------------------------------------	|
| Snap Token must not empty.                                       	| You not put the token and keep it null or empty when making payment or something that need token.          	|
| Merchant base url is empty. Please set merchant base url on SDK. 	| You not set the `MERCHANT_BASE_URL`, so SDK cannot making network request. Please initialize SDK properly. 	|
| Failed to retrieve response from server.                         	| Network request to server is success but it not return anything.                                           	|
| Error message not catchable.                                     	| SDK cannot cacth the error.                                                                                	|
| Failed to connect to server.                                     	| You not connected to any internet connection.                                                              	|
| Invalid or empty data supplied to SDK.                           	| Empty or wrong data input to the SDK.                                                                      	|


### 8.2.2. <a name='PaymentUsingEwallet'></a>Payment Using E-Wallet/VA

#### Payment Using Mandiri E-Cash
- **Request Object**

	Payment using Mandiri E-Cash need `CustomerDetailPayRequest` object as paramater, for making `CustomerDetailPayRequest` object please follow this instruction.

	> **Once again, this is very minimum requirements for making CustomerDetailPayRequest object and only sample for you, so it will use default setting. If you want to use some specific feature or other feature, please follow section `FEATURE` and combine it with this minimum CheckoutTransaction object.**

	For starting payment using Mandiri E-Cash with Midtrans, you'll need to write a `CustomerDetailPayRequest` object inside your class (Note, the code samples in this section are simply examples – your own implementation may differ depending on the structure of your app). To make `CustomerDetailPayRequest` you will need 3 required parameters as an example below.
   	
    ```Java
        String fullName = "fullName", email = "email@mail.com", phone = "08123456789";
        CustomerDetailPayRequest customerDetailPayReq = new CustomerDetailPayRequest(fullName, email, phone);
    ```
    
    ```Kotlin
        val fullName = "fullName"
        val email = "email@mail.com"
        val phone = "08123456789"
        val customerDetailPayReq = CustomerDetailPayRequest(fullName, email, phone)
    ```
    
- **The Method**

  ```Java
    EWalletCharge.paymentUsingMandiriEcash(
                 TOKEN,
                 customerDetailPayReq,
                 new MidtransCallback<EwalletMandiriEcashPaymentResponse>() {
                     @Override
                     public void onSuccess(EwalletMandiriEcashPaymentResponse data) {
                      
                     }
 
                     @Override
                     public void onFailed(Throwable throwable) {
 
                     }
                 }
         );
    ```
    
  ```Kotlin
    EWalletCharge.paymentUsingMandiriEcash(
                 TOKEN,
                 customerDetailPayReq,
                 object : MidtransCallback<EwalletMandiriEcashPaymentResponse> {
	                 override fun onSuccess(data: EwalletMandiriEcashPaymentResponse?) {
	                    
	                 }
	
	                 override fun onFailed(throwable: Throwable?) {
	                   
	                 }
	            })
    ```
    
- **Success Midtrans Callback**

  Succes response will return `EwalletMandiriEcashPaymentResponse ` as model, you can access   it from `MidtransCallback` interface and you need to use `EwalletMandiriEcashPaymentResponse` to get any corresponding response you need from `EwalletMandiriEcashPaymentResponse `. As an example below.
 
  ```Java
  String redirectUrl = data.getRedirectUrl();
  ```
  
  ```Kotlin
  val redirectUrl = data.redirectUrl
  ```
  
  and you need to load url inito your webview
  
  ```Java
  webview.loadUrl(redirectUrl);
  ```
  
  ```Kotlin
  webview.loadUrl(redirectUrl);
  ```
  
- **Failed Midtrans Callback**
	
	Failed response will return `Throwable` as model, you can get error message and identify why it happen. Midtrans SDK provide some validation and it will return here if not pass the validation. So Failed Midtrans callback will return all of error which is from Midtrans SDK validation or from other source like network error, etc. Here's error message from Midtrans SDK Validation

	| Message                                                          	| Cause                                                                                                      	|
|------------------------------------------------------------------	|------------------------------------------------------------------------------------------------------------	|
| Snap Token must not empty.                                       	| You not put the token and keep it null or empty when making payment or something that need token.          	|
| Merchant base url is empty. Please set merchant base url on SDK. 	| You not set the `MERCHANT_BASE_URL`, so SDK cannot making network request. Please initialize SDK properly. 	|
| Failed to retrieve response from server.                         	| Network request to server is success but it not return anything.                                           	|
| Error message not catchable.                                     	| SDK cannot cacth the error.                                                                                	|
| Failed to connect to server.                                     	| You not connected to any internet connection.                                                              	|
| Invalid or empty data supplied to SDK.                           	| Empty or wrong data input to the SDK.                                                                      	|


#### Payment Using GO-PAY
- **Request Object**

	For starting payment using GO-PAY with Midtrans, you'll need to put GO-PAY account number to payment method that Midtrans provide.
   	
    ```Java
        String gopayAccountNumber = "08123456789";
    ```
    
    ```Kotlin
        val gopayAccountNumber = "08123456789"
    ```
       
- **The Method**

  ```Java
    EWalletCharge.paymentUsingGopay(
                 TOKEN,
                 gopayAccountNumber,
                 new MidtransCallback<EwalletGopayPaymentResponse>() {
                     @Override
                     public void onSuccess(EwalletGopayPaymentResponse data) {
                      
                     }
 
                     @Override
                     public void onFailed(Throwable throwable) {
 
                     }
                 }
         );
    ```
    
  ```Kotlin
    EWalletCharge.paymentUsingGopay(
                 TOKEN,
                 customerDetailPayReq,
                 object : MidtransCallback<EwalletGopayPaymentResponse> {
	                 override fun onSuccess(data: EwalletGopayPaymentResponse?) {
	                    
	                 }
	
	                 override fun onFailed(throwable: Throwable?) {
	                   
	                 }
	            })
    ```
    
- **Success Midtrans Callback**

  Succes response will return `EwalletGopayPaymentResponse` as model, you can access   it from `MidtransCallback` interface and you need to use `EwalletGopayPaymentResponse` to get any corresponding response you need from `EwalletGopayPaymentResponse`. As an example below.
  
  ```Java
  String redirect = data.getRedirectUrl();
  ```
  
  ```Kotlin
  val redirect = data?.redirectUrl
  ```
  
  You need to call the redirect for open GOJEK app then user can make payment with it.
  
- **Failed Midtrans Callback**
	
	Failed response will return `Throwable` as model, you can get error message and identify why it happen. Midtrans SDK provide some validation and it will return here if not pass the validation. So Failed Midtrans callback will return all of error which is from Midtrans SDK validation or from other source like network error, etc. Here's error message from Midtrans SDK Validation

	| Message                                                          	| Cause                                                                                                      	|
|------------------------------------------------------------------	|------------------------------------------------------------------------------------------------------------	|
| Snap Token must not empty.                                       	| You not put the token and keep it null or empty when making payment or something that need token.          	|
| Merchant base url is empty. Please set merchant base url on SDK. 	| You not set the `MERCHANT_BASE_URL`, so SDK cannot making network request. Please initialize SDK properly. 	|
| Failed to retrieve response from server.                         	| Network request to server is success but it not return anything.                                           	|
| Error message not catchable.                                     	| SDK cannot cacth the error.                                                                                	|
| Failed to connect to server.                                     	| You not connected to any internet connection.                                                              	|
| Invalid or empty data supplied to SDK.                           	| Empty or wrong data input to the SDK.                                                                      	|


#### Payment Using Telkomsel Cash (T-Cash)
- **Request Object**

	For starting payment using Telkomsel Cash with Midtrans, you'll need put Telkomsel number to payment method that Midtrans provide.
   	
    ```Java
        String telkomselUserNumber = "08123456789";
    ```
    
    ```Kotlin
        val telkomselUserNumber = "08123456789"
    ```
     
- **The Method**

  ```Java
    EWalletCharge.paymentUsingTelkomselCash(
                 TOKEN,
                 telkomselUserNumber,
                 new MidtransCallback<EwalletTelkomselCashPaymentResponse>() {
                     @Override
                     public void onSuccess(EwalletTelkomselCashPaymentResponse data) {
                      
                     }
 
                     @Override
                     public void onFailed(Throwable throwable) {
 
                     }
                 }
         );
    ```
      
  ```Kotlin
    EWalletCharge.paymentUsingTelkomselCash(
                 TOKEN,
                 telkomselUserNumber,
                 object : MidtransCallback<EwalletTelkomselCashPaymentResponse> {
	                 override fun onSuccess(data: EwalletTelkomselCashPaymentResponse?) {
	                    
	                 }
	
	                 override fun onFailed(throwable: Throwable?) {
	                   
	                 }
	            })
    ```
    
- **Success Midtrans Callback**

  Succes response will return `EwalletTelkomselCashPaymentResponse` as model, you can access   it from `MidtransCallback` interface and you need to use `EwalletTelkomselCashPaymentResponse` to get any corresponding response you need from `EwalletTelkomselCashPaymentResponse`. As an example below.
  
  ```Java
  String redirectUrl = data.getRedirectUrl();
  ```
  
  ```Kotlin
  val redirectUrl = data.redirectUrl
  ```
  
  and you need to load url inito your webview
  
  ```Java
  webview.loadUrl(redirectUrl);
  ```
  
  ```Kotlin
  webview.loadUrl(redirectUrl);
  ```
  
- **Failed Midtrans Callback**
	
	Failed response will return `Throwable` as model, you can get error message and identify why it happen. Midtrans SDK provide some validation and it will return here if not pass the validation. So Failed Midtrans callback will return all of error which is from Midtrans SDK validation or from other source like network error, etc. Here's error message from Midtrans SDK Validation

	| Message                                                          	| Cause                                                                                                      	|
|------------------------------------------------------------------	|------------------------------------------------------------------------------------------------------------	|
| Snap Token must not empty.                                       	| You not put the token and keep it null or empty when making payment or something that need token.          	|
| Merchant base url is empty. Please set merchant base url on SDK. 	| You not set the `MERCHANT_BASE_URL`, so SDK cannot making network request. Please initialize SDK properly. 	|
| Failed to retrieve response from server.                         	| Network request to server is success but it not return anything.                                           	|
| Error message not catchable.                                     	| SDK cannot cacth the error.                                                                                	|
| Failed to connect to server.                                     	| You not connected to any internet connection.                                                              	|
| Invalid or empty data supplied to SDK.                           	| Empty or wrong data input to the SDK.                                                                      	|


### 8.2.3. <a name='PaymentUsingOnlineDebit'></a>Payment Using Online Debit/VA

#### Payment Using CIMB Clicks
- **Request Object**

	No specific request object needed for this payment method.
	
- **The Method**

  ```Java
    OnlineDebitCharge.paymentUsingCimbClicks(
                 TOKEN,
                 new MidtransCallback<OnlineDebitCimbClicksPaymentResponse>() {
                     @Override
                     public void onSuccess(OnlineDebitCimbClicksPaymentResponse data) {
                      
                     }
 
                     @Override
                     public void onFailed(Throwable throwable) {
 
                     }
                 }
     );
    ```
    
    ```Kotlin
    OnlineDebitCharge.paymentUsingCimbClicks(
            TOKEN,
            object : MidtransCallback<OnlineDebitCimbClicksPaymentResponse>{
                override fun onSuccess(data: OnlineDebitCimbClicksPaymentResponse?) {
                    
                }

                override fun onFailed(throwable: Throwable?) {
                    
                }
            })
    ```
    
- **Success Midtrans Callback**

  Succes response will return `OnlineDebitCimbClicksPaymentResponse` as model, you can access   it from `MidtransCallback` interface and you need to use `OnlineDebitCimbClicksPaymentResponse` to get any corresponding response you need from `OnlineDebitCimbClicksPaymentResponse`. As an example below.
  
  ```Java
  String redirectUrl = data.getRedirectUrl();
  ```
  
  ```Kotlin
  val redirectUrl = data.redirectUrl
  ```
  
  and you need to load url inito your webview
  
  ```Java
  webview.loadUrl(redirectUrl);
  ```
  
  ```Kotlin
  webview.loadUrl(redirectUrl);
  ```
  
- **Failed Midtrans Callback**
	
	Failed response will return `Throwable` as model, you can get error message and identify why it happen. Midtrans SDK provide some validation and it will return here if not pass the validation. So Failed Midtrans callback will return all of error which is from Midtrans SDK validation or from other source like network error, etc. Here's error message from Midtrans SDK Validation

	| Message                                                          	| Cause                                                                                                      	|
|------------------------------------------------------------------	|------------------------------------------------------------------------------------------------------------	|
| Snap Token must not empty.                                       	| You not put the token and keep it null or empty when making payment or something that need token.          	|
| Merchant base url is empty. Please set merchant base url on SDK. 	| You not set the `MERCHANT_BASE_URL`, so SDK cannot making network request. Please initialize SDK properly. 	|
| Failed to retrieve response from server.                         	| Network request to server is success but it not return anything.                                           	|
| Error message not catchable.                                     	| SDK cannot cacth the error.                                                                                	|
| Failed to connect to server.                                     	| You not connected to any internet connection.                                                              	|
| Invalid or empty data supplied to SDK.                           	| Empty or wrong data input to the SDK.                                                                      	|


#### Payment Using BRI E-PAY
- **Request Object**

	No specific request object needed for this payment method.
	
- **The Method**

  ```Java
    OnlineDebitCharge.paymentUsingBriEpay(
                 TOKEN,
                 new MidtransCallback<OnlineDebitBriEpayPaymentResponse>() {
                     @Override
                     public void onSuccess(OnlineDebitBriEpayPaymentResponse data) {
                      
                     }
 
                     @Override
                     public void onFailed(Throwable throwable) {
 
                     }
                 }
     );
    ```
    
    ```Kotlin
    OnlineDebitCharge.paymentUsingBriEpay(
            TOKEN,
            object : MidtransCallback<OnlineDebitBriEpayPaymentResponse>{
                override fun onSuccess(data: OnlineDebitBriEpayPaymentResponse?) {
                    
                }

                override fun onFailed(throwable: Throwable?) {
                    
                }
            })
    ```

- **Success Midtrans Callback**

  Succes response will return `OnlineDebitBriEpayPaymentResponse` as model, you can access   it from `MidtransCallback` interface and you need to use `OnlineDebitBriEpayPaymentResponse` to get any corresponding response you need from `OnlineDebitBriEpayPaymentResponse`. As an example below.
  
  ```Java
  String redirectUrl = data.getRedirectUrl();
  ```
  
  ```Kotlin
  val redirectUrl = data.redirectUrl
  ```
  
  and you need to load url inito your webview
  
  ```Java
  webview.loadUrl(redirectUrl);
  ```
  
  ```Kotlin
  webview.loadUrl(redirectUrl);
  ```

- **Failed Midtrans Callback**
	
	Failed response will return `Throwable` as model, you can get error message and identify why it happen. Midtrans SDK provide some validation and it will return here if not pass the validation. So Failed Midtrans callback will return all of error which is from Midtrans SDK validation or from other source like network error, etc. Here's error message from Midtrans SDK Validation

	| Message                                                          	| Cause                                                                                                      	|
|------------------------------------------------------------------	|------------------------------------------------------------------------------------------------------------	|
| Snap Token must not empty.                                       	| You not put the token and keep it null or empty when making payment or something that need token.          	|
| Merchant base url is empty. Please set merchant base url on SDK. 	| You not set the `MERCHANT_BASE_URL`, so SDK cannot making network request. Please initialize SDK properly. 	|
| Failed to retrieve response from server.                         	| Network request to server is success but it not return anything.                                           	|
| Error message not catchable.                                     	| SDK cannot cacth the error.                                                                                	|
| Failed to connect to server.                                     	| You not connected to any internet connection.                                                              	|
| Invalid or empty data supplied to SDK.                           	| Empty or wrong data input to the SDK.                                                                      	|

#### Payment Using BCA KLIKPAY
- **Request Object**

	No specific request object needed for this payment method.

- **The Method**

	```Java
	OnlineDebitCharge.paymentUsingBcaKlikpay(
                 TOKEN,
                 new MidtransCallback<OnlineDebitBcaKlikpayPaymentResponse>() {
                     @Override
                     public void onSuccess(OnlineDebitBcaKlikpayPaymentResponse data) {
                      
                     }
 
                     @Override
                     public void onFailed(Throwable throwable) {
 
                     }
                 }
     );
	```
	
	```Kotlin
	OnlineDebitCharge.paymentUsingBcaKlikpay(
            TOKEN,
            object : MidtransCallback<OnlineDebitBcaKlikpayPaymentResponse>{
                override fun onSuccess(data: OnlineDebitBcaKlikpayPaymentResponse?) {
                    
                }

                override fun onFailed(throwable: Throwable?) {
                    
                }
            })
	``` 

- **Success Midtrans Callback**

  Succes response will return `OnlineDebitBcaKlikpayPaymentResponse` as model, you can access   it from `MidtransCallback` interface and you need to use `OnlineDebitBcaKlikpayPaymentResponse` to get any corresponding response you need from `OnlineDebitBcaKlikpayPaymentResponse`. As an example below.
   
  ```Java
  String redirectUrl = data.getRedirectUrl();
  ```
  
  ```Kotlin
  val redirectUrl = data.redirectUrl
  ```
  
  and you need to load url inito your webview
  
  ```Java
  webview.loadUrl(redirectUrl);
  ```
  
  ```Kotlin
  webview.loadUrl(redirectUrl);
  ```

- **Failed Midtrans Callback**
	
	Failed response will return `Throwable` as model, you can get error message and identify why it happen. Midtrans SDK provide some validation and it will return here if not pass the validation. So Failed Midtrans callback will return all of error which is from Midtrans SDK validation or from other source like network error, etc. Here's error message from Midtrans SDK Validation

	| Message                                                          	| Cause                                                                                                      	|
|------------------------------------------------------------------	|------------------------------------------------------------------------------------------------------------	|
| Snap Token must not empty.                                       	| You not put the token and keep it null or empty when making payment or something that need token.          	|
| Merchant base url is empty. Please set merchant base url on SDK. 	| You not set the `MERCHANT_BASE_URL`, so SDK cannot making network request. Please initialize SDK properly. 	|
| Failed to retrieve response from server.                         	| Network request to server is success but it not return anything.                                           	|
| Error message not catchable.                                     	| SDK cannot cacth the error.                                                                                	|
| Failed to connect to server.                                     	| You not connected to any internet connection.                                                              	|
| Invalid or empty data supplied to SDK.                           	| Empty or wrong data input to the SDK.                                                                      	|


### 8.2.4. <a name='PaymentUsingDirectDebit'></a>Payment Using Direct Debit/VA

#### Payment Using Mandiri ClickPay
- **Request Object**
        
	Payment using Mandiri Click Pay need `MandiriClickpayParams` object as paramater, for making `MandiriClickpayParams` object please follow this instruction.

	> **Once again, this is very minimum requirements for making MandiriClickpayParams object and only sample for you, so it will use default setting. If you want to use some specific feature or other feature, please follow section `FEATURE` and combine it with this minimum MandiriClickpayParams object.**

	For starting payment using Mandiri Click Pay with Midtrans, you'll need to write a `MandiriClickpayParams` object inside your class (Note, the code samples in this section are simply examples – your own implementation may differ depending on the structure of your app). To make `MandiriClickpayParams` you will need 3 required parameters as an example below.
	   	
    ```Java
        String mandiriCardNumber = "mandiriCardNumber", input3 = "input3", tokenResponse = "123123";
        MandiriClickpayParams mandiriClickpayParam = new MandiriClickpayParams(mandiriCardNumber, input3, tokenResponse);
    ```
    
    ```Kotlin
        val mandiriCardNumber = "mandiriCardNumber"
        val input3 = "input3"
        val tokenResponse = "123123"
        val customerDetailPayReq = MandiriClickpayParams(mandiriCardNumber, input3, tokenResponse)
    ```
 
- **The Method**
 
  ```Java
     DirectDebitCharge.paymentUsingMandiriClickPay(
                 TOKEN,
                 mandiriClickpayParam,
                 new MidtransCallback<BasePaymentResponse>() {
                     @Override
                     public void onSuccess(BasePaymentResponse data) {
                      
                     }
 
                     @Override
                     public void onFailed(Throwable throwable) {
 
                     }
                 }
     );
    ```
    
  ```Kotlin
     DirectDebitCharge.paymentUsingMandiriClickPay(
                 TOKEN,
                 mandiriClickpayParam,
                 object : MidtransCallback<DirectDebitMandiriClickpayResponse>{
                override fun onSuccess(data: DirectDebitMandiriClickpayResponse?) {
                    
                }

                override fun onFailed(throwable: Throwable?) {
                    
                }
            }
        )
    ```
    
- **Success Midtrans Callback**

  Succes response will return `DirectDebitMandiriClickpayResponse` as model, you can access   it from `MidtransCallback` interface and you need to use `DirectDebitMandiriClickpayResponse` to get any corresponding response you need from `DirectDebitMandiriClickpayResponse`.
  
  ```Java
  String status = data.getTransactionStatus();
  ```
  
  ```Kotlin
  val status = data?.transactionStatus
  ```

- **Failed Midtrans Callback**
	
	Failed response will return `Throwable` as model, you can get error message and identify why it happen. Midtrans SDK provide some validation and it will return here if not pass the validation. So Failed Midtrans callback will return all of error which is from Midtrans SDK validation or from other source like network error, etc. Here's error message from Midtrans SDK Validation

	| Message                                                          	| Cause                                                                                                      	|
|------------------------------------------------------------------	|------------------------------------------------------------------------------------------------------------	|
| Snap Token must not empty.                                       	| You not put the token and keep it null or empty when making payment or something that need token.          	|
| Merchant base url is empty. Please set merchant base url on SDK. 	| You not set the `MERCHANT_BASE_URL`, so SDK cannot making network request. Please initialize SDK properly. 	|
| Failed to retrieve response from server.                         	| Network request to server is success but it not return anything.                                           	|
| Error message not catchable.                                     	| SDK cannot cacth the error.                                                                                	|
| Failed to connect to server.                                     	| You not connected to any internet connection.                                                              	|
| Invalid or empty data supplied to SDK.                           	| Empty or wrong data input to the SDK.                                                                      	|

#### Payment Using Klik BCA
- **Request Object**

	For starting payment using Telkomsel Cash with Midtrans, you'll need put Telkomsel number to payment method that Midtrans provide.
   	
    ```Java
        String klikBcaUserId = "klikBcaUserId";
    ```
    
    ```Kotlin
        val klikBcaUserId = "klikBcaUserId"
    ```
    
- **The Method**

  ```Java
     DirectDebitCharge.paymentUsingKlikBca(
                 TOKEN,
                 klikBcaUserId,
                 new MidtransCallback<DirectDebitKlikBcaResponse>() {
                     @Override
                     public void onSuccess(DirectDebitKlikBcaResponse data) {
                      
                     }
 
                     @Override
                     public void onFailed(Throwable throwable) {
 
                     }
                 }
     );
    ```
    
  ```Java
     DirectDebitCharge.paymentUsingKlikBca(
                 TOKEN,
                 klikBcaUserId,
                 object : MidtransCallback<DirectDebitKlikBcaResponse>{
                override fun onSuccess(data: DirectDebitKlikBcaResponse?) {
                    
                }

                override fun onFailed(throwable: Throwable?) {
                    
                }
            }
        )
    ```
    
- **Success Midtrans Callback**

  Succes response will return `DirectDebitKlikBcaResponse` as model, you can access   it from `MidtransCallback` interface and you need to use `DirectDebitKlikBcaResponse` to get any corresponding response you need from `BasePaymentResponse`.
  
  ```Java
  String status = data.getTransactionStatus();
  ```
  
  ```Kotlin
  val status = data?.transactionStatus
  ```
 
- **Failed Midtrans Callback**
	
	Failed response will return `Throwable` as model, you can get error message and identify why it happen. Midtrans SDK provide some validation and it will return here if not pass the validation. So Failed Midtrans callback will return all of error which is from Midtrans SDK validation or from other source like network error, etc. Here's error message from Midtrans SDK Validation

	| Message                                                          	| Cause                                                                                                      	|
|------------------------------------------------------------------	|------------------------------------------------------------------------------------------------------------	|
| Snap Token must not empty.                                       	| You not put the token and keep it null or empty when making payment or something that need token.          	|
| Merchant base url is empty. Please set merchant base url on SDK. 	| You not set the `MERCHANT_BASE_URL`, so SDK cannot making network request. Please initialize SDK properly. 	|
| Failed to retrieve response from server.                         	| Network request to server is success but it not return anything.                                           	|
| Error message not catchable.                                     	| SDK cannot cacth the error.                                                                                	|
| Failed to connect to server.                                     	| You not connected to any internet connection.                                                              	|
| Invalid or empty data supplied to SDK.                           	| Empty or wrong data input to the SDK.                                                                      	|

### 8.2.5. <a name='PaymentUsingCardlessCredit'></a>Payment Using Cardless Credit


### Payment Using Akulaku
- **Request Object**

	No specific request object needed for this payment method.
	
- **The Method**

  ```Java
    CardlessCreditCharge.paymentUsingAkulaku(
                 TOKEN,
                 new MidtransCallback<CardlessCreditAkulakuPaymentResponse>() {
                     @Override
                     public void onSuccess(CardlessCreditAkulakuPaymentResponse data) {
                      
                     }
 
                     @Override
                     public void onFailed(Throwable throwable) {
 
                     }
                 }
     );
    ```
    
    ```Kotlin
    CardlessCreditCharge.paymentUsingAkulaku(
            "",
            object : MidtransCallback<CardlessCreditAkulakuPaymentResponse>{
                override fun onSuccess(data: CardlessCreditAkulakuPaymentResponse?) {

                }

                override fun onFailed(throwable: Throwable?) {

                }
            }
        )
    ```
    
- **Success Midtrans Callback**

  Succes response will return `CardlessCreditAkulakuPaymentResponse` as model, you can access   it from `MidtransCallback` interface and you need to use `CardlessCreditAkulakuPaymentResponse` to get any corresponding response you need from `CardlessCreditAkulakuPaymentResponse`.  As an example below.
   
  ```Java
  String redirectUrl = data.getRedirectUrl();
  ```
  
  ```Kotlin
  val redirectUrl = data.redirectUrl
  ```
  
  and you need to load url inito your webview
  
  ```Java
  webview.loadUrl(redirectUrl);
  ```
  
  ```Kotlin
  webview.loadUrl(redirectUrl);
  ```

- **Failed Midtrans Callback**
	
	Failed response will return `Throwable` as model, you can get error message and identify why it happen. Midtrans SDK provide some validation and it will return here if not pass the validation. So Failed Midtrans callback will return all of error which is from Midtrans SDK validation or from other source like network error, etc. Here's error message from Midtrans SDK Validation

	| Message                                                          	| Cause                                                                                                      	|
|------------------------------------------------------------------	|------------------------------------------------------------------------------------------------------------	|
| Snap Token must not empty.                                       	| You not put the token and keep it null or empty when making payment or something that need token.          	|
| Merchant base url is empty. Please set merchant base url on SDK. 	| You not set the `MERCHANT_BASE_URL`, so SDK cannot making network request. Please initialize SDK properly. 	|
| Failed to retrieve response from server.                         	| Network request to server is success but it not return anything.                                           	|
| Error message not catchable.                                     	| SDK cannot cacth the error.                                                                                	|
| Failed to connect to server.                                     	| You not connected to any internet connection.                                                              	|
| Invalid or empty data supplied to SDK.                           	| Empty or wrong data input to the SDK.                                                                      	|

### 8.2.6. <a name='PaymentUsingStore'></a>Payment Using Convenience Store

### Payment Using Indomaret
- **Request Object**

	No specific request object needed for this payment method.

- **The Method**

  ```Java
    ConvenienceStoreCharge.paymentUsingIndomaret(
                 TOKEN,
                 new MidtransCallback<ConvenienceStoreIndomaretPaymentResponse>() {
                     @Override
                     public void onSuccess(ConvenienceStoreIndomaretPaymentResponse data) {
                      
                     }
 
                     @Override
                     public void onFailed(Throwable throwable) {
 
                     }
                 }
     );
    ```

  ```Kotlin
    ConvenienceStoreCharge.paymentUsingIndomaret(
            TOKEN,
            object : MidtransCallback<ConvenienceStoreIndomaretPaymentResponse> {
                override fun onSuccess(data: ConvenienceStoreIndomaretPaymentResponse) {

                }

                override fun onFailed(throwable: Throwable) {

                }
            }
        )
    ```

- **Success Midtrans Callback**

  Succes response will return `ConvenienceStoreIndomaretPaymentResponse ` as model, you can access   it from `MidtransCallback` interface and you need to use `ConvenienceStoreIndomaretPaymentResponse ` to get any corresponding response you need from `ConvenienceStoreIndomaretPaymentResponse `.  As an example below.
  
  ```Java
  String indomaretPaymentCode = data.getPaymentCodeIndomaret();
  ```
  
  ```Kotlin
  val indomaretPaymentCode = data?.paymentCodeIndomaret
  ```
  
- **Failed Midtrans Callback**
	
	Failed response will return `Throwable` as model, you can get error message and identify why it happen. Midtrans SDK provide some validation and it will return here if not pass the validation. So Failed Midtrans callback will return all of error which is from Midtrans SDK validation or from other source like network error, etc. Here's error message from Midtrans SDK Validation

	| Message                                                          	| Cause                                                                                                      	|
|------------------------------------------------------------------	|------------------------------------------------------------------------------------------------------------	|
| Snap Token must not empty.                                       	| You not put the token and keep it null or empty when making payment or something that need token.          	|
| Merchant base url is empty. Please set merchant base url on SDK. 	| You not set the `MERCHANT_BASE_URL`, so SDK cannot making network request. Please initialize SDK properly. 	|
| Failed to retrieve response from server.                         	| Network request to server is success but it not return anything.                                           	|
| Error message not catchable.                                     	| SDK cannot cacth the error.                                                                                	|
| Failed to connect to server.                                     	| You not connected to any internet connection.                                                              	|
| Invalid or empty data supplied to SDK.                           	| Empty or wrong data input to the SDK.                                                                      	|


### 8.2.7. <a name='CreditCard'></a>Payment Using CreditCard

Before starting credit card payment, credit card need to be tokenize first, so you must do the tokenize then you can starting payment

### Tokenize Card
- **Request Object**

	For starting payment using Credit Card with Midtrans, you'll need to Tokenize Credit Card first, here some stuff you need for tokenize card.
   	
    ```Java
        String cardNumber = "4111111111111111";
        String cardCvv = "123";
        String cardExpMonth = "02";
        String cardExpYear = "2020";
    ```
    
    ```Kotlin
        val cardNumber = "4111111111111111"
        val cardCvv = "123"
        val cardExpMonth = "02"
        val cardExpYear = "2020"
    ```

- **The Method**

  ```Java
    CreditCardCharge.cardRegistration(
                cardNumber,
                cardCvv,
                cardExpMonth,
                cardExpYear,
                new MidtransCallback<CardRegistrationResponse>() {
                    @Override
                    public void onSuccess(CardRegistrationResponse data) {
                        
                    }

                    @Override
                    public void onFailed(Throwable throwable) {

                    }
                });
     );
    ```

  ```Kotlin
    CreditCardCharge.cardRegistration(
                cardNumber,
                cardCvv,
                cardExpMonth,
                cardExpYear,
            object : MidtransCallback<CardRegistrationResponse> {
                override fun onSuccess(data: CardRegistrationResponse) {

                }

                override fun onFailed(throwable: Throwable) {

                }
            }
        )
    ```

- **Success Midtrans Callback**

  Succes response will return `CardRegistrationResponse ` as model, you can access   it from `MidtransCallback` interface and you need to use `CardRegistrationResponse ` to get any corresponding response you need from `CardRegistrationResponse `.  As an example below.
  
  ```Java
  String cardToken = data.getSavedTokenId();
  String maskedCard = data.getMaskedCard();
  ```
  
  ```Kotlin
  val cardToken = data?.savedTokenId
  val maskedCard = data?.maskedCard
  ```
  
- **Failed Midtrans Callback**
	
	Failed response will return `Throwable` as model, you can get error message and identify why it happen. Midtrans SDK provide some validation and it will return here if not pass the validation. So Failed Midtrans callback will return all of error which is from Midtrans SDK validation or from other source like network error, etc. Here's error message from Midtrans SDK Validation

	| Message                                                          	| Cause                                                                                                      	|
|------------------------------------------------------------------	|------------------------------------------------------------------------------------------------------------	|
| Snap Token must not empty.                                       	| You not put the token and keep it null or empty when making payment or something that need token.          	|
| Merchant base url is empty. Please set merchant base url on SDK. 	| You not set the `MERCHANT_BASE_URL`, so SDK cannot making network request. Please initialize SDK properly. 	|
| Failed to retrieve response from server.                         	| Network request to server is success but it not return anything.                                           	|
| Error message not catchable.                                     	| SDK cannot cacth the error.                                                                                	|
| Failed to connect to server.                                     	| You not connected to any internet connection.                                                              	|
| Invalid or empty data supplied to SDK.                           	| Empty or wrong data input to the SDK.                                                                      	|


### Payment Using Credit Card
- **Request Object**

	For starting payment using Credit Card with Midtrans, `savedTokenId` and `maskedCard` from Tokenize step. You can get it from MidtransCallback with CardRegistrationResponse model then input it in new model. After that you'll need to write a `CustomerDetailPayRequest` object inside your class (Note, the code samples in this section are simply examples – your own implementation may differ depending on the structure of your app). To make `CustomerDetailPayRequest` you will need 3 required parameters as an example below.
	
    ```Java
    	// Create CustomerDetailPayRequest object first
		String fullName = "fullName", email = "email@mail.com", phone = "08123456789";
		CustomerDetailPayRequest customerDetailPayReq = new CustomerDetailPayRequest(fullName, email, phone);
		
		// Create CreditCardPaymentParams
		String cardToken = data.getSavedTokenId();
		String maskedCard = data.getMaskedCard();
		CreditCardPaymentParams params = new CreditCardPaymentParams(cardToken, false, maskedCard);
    ```
    
    ```Kotlin
    	// Create CustomerDetailPayRequest object first
		val fullName = "fullName"
		val email = "email@mail.com"
		val phone = "08123456789"
		val customerDetailPayReq = CustomerDetailPayRequest(fullName, email, phone)
		
		// Create CreditCardPaymentParams
		val cardToken = data?.savedTokenId
		val maskedCard = data?.maskedCard 
		val params = CreditCardPaymentParams(cardToken, false, maskedCard)
    ```

- **The Method**

  
  ```Java
    CreditCardCharge.cardRegistration(
                cardNumber,
                cardCvv,
                cardExpMonth,
                cardExpYear,
                new MidtransCallback<CardRegistrationResponse>() {
                    @Override
                    public void onSuccess(CardRegistrationResponse data) {
                        
                    }

                    @Override
                    public void onFailed(Throwable throwable) {

                    }
                });
     );
    ```

  ```Kotlin
    CreditCardCharge.paymentUsingCard(
            TOKEN,
            params,
            customerDetailPayReq,
            object : MidtransCallback<CreditCardPaymentResponse> {
                override fun onSuccess(data: CreditCardPaymentResponse) {
                    data.transactionStatus
                }

                override fun onFailed(throwable: Throwable) {

                }
            })
    ```


- **Success Midtrans Callback**

  Succes response will return `CreditCardPaymentResponse ` as model, you can access   it from `MidtransCallback` interface and you need to use `CreditCardPaymentResponse ` to get any corresponding response you need from `CreditCardPaymentResponse `.  As an example below.
  
  ```Java
  String status = getTransactionStatus();
  ```
  
  ```Kotlin
  val status = data?.transactionStatus
  ```
  
- **Failed Midtrans Callback**
	
	Failed response will return `Throwable` as model, you can get error message and identify why it happen. Midtrans SDK provide some validation and it will return here if not pass the validation. So Failed Midtrans callback will return all of error which is from Midtrans SDK validation or from other source like network error, etc. Here's error message from Midtrans SDK Validation

	| Message                                                          	| Cause                                                                                                      	|
|------------------------------------------------------------------	|------------------------------------------------------------------------------------------------------------	|
| Snap Token must not empty.                                       	| You not put the token and keep it null or empty when making payment or something that need token.          	|
| Merchant base url is empty. Please set merchant base url on SDK. 	| You not set the `MERCHANT_BASE_URL`, so SDK cannot making network request. Please initialize SDK properly. 	|
| Failed to retrieve response from server.                         	| Network request to server is success but it not return anything.                                           	|
| Error message not catchable.                                     	| SDK cannot cacth the error.                                                                                	|
| Failed to connect to server.                                     	| You not connected to any internet connection.                                                              	|
| Invalid or empty data supplied to SDK.                           	| Empty or wrong data input to the SDK.                                                                      	|


### Save Credit Card
- **Request Object**

	After tokenize you can save your credit card with this method. For the response, you can get it from MidtransCallback with SaveCardResponse model. For start saving card you nee to make list of `SaveCardRequests` model. Please follow step below
	
    ```Java
    	List<SaveCardRequest> saveCardRequests = new ArrayList<>(Arrays
                .asList(
                        new SaveCardRequest(
                                SAVED_TOKEN_ID_FROM_TOKENIZE,
                                MASKED_CARD,
                                TYPE)
                ));
    ```
    
    ```Kotlin
    	val saveCardRequests = mutableListOf(
            SaveCardRequest(
                SAVED_TOKEN_ID_FROM_TOKENIZE,
                MASKED_CARD,
                TYPE)
        )
	```

- **The Method**

  
  ```Java
    CreditCardCharge.saveCards(
                USER_ID_FOR_SAVE_CARDd ,
                saveCardRequests,
                new MidtransCallback<SaveCardResponse>() {
                    @Override
                    public void onSuccess(SaveCardResponse data) {

                    }

                    @Override
                    public void onFailed(Throwable throwable) {

                    }
                });
    ```

  ```Kotlin
    CreditCardCharge.saveCards(
            USER_ID_FOR_SAVE_CARD,
            saveCardRequests,
            object : MidtransCallback<SaveCardResponse> {
                override fun onSuccess(data: SaveCardResponse) {

                }

                override fun onFailed(throwable: Throwable) {

                }
            })  
    ```


- **Success Midtrans Callback**

  Succes response will return `SaveCardResponse ` as model, you can access   it from `MidtransCallback` interface and you need to use `SaveCardResponse ` to get any corresponding response you need from `SaveCardResponse `.  As an example below.
  
  ```Java
  String status = data.getTransactionStatus();
  ```
  
  ```Kotlin
  val status = data?.transactionStatus
  ```
  
- **Failed Midtrans Callback**
	
	Failed response will return `Throwable` as model, you can get error message and identify why it happen. Midtrans SDK provide some validation and it will return here if not pass the validation. So Failed Midtrans callback will return all of error which is from Midtrans SDK validation or from other source like network error, etc. Here's error message from Midtrans SDK Validation

	| Message                                                          	| Cause                                                                                                      	|
|------------------------------------------------------------------	|------------------------------------------------------------------------------------------------------------	|
| Snap Token must not empty.                                       	| You not put the token and keep it null or empty when making payment or something that need token.          	|
| Merchant base url is empty. Please set merchant base url on SDK. 	| You not set the `MERCHANT_BASE_URL`, so SDK cannot making network request. Please initialize SDK properly. 	|
| Failed to retrieve response from server.                         	| Network request to server is success but it not return anything.                                           	|
| Error message not catchable.                                     	| SDK cannot cacth the error.                                                                                	|
| Failed to connect to server.                                     	| You not connected to any internet connection.                                                              	|
| Invalid or empty data supplied to SDK.                           	| Empty or wrong data input to the SDK.                                                                      	|


### 8.2.8. <a name='MidtransCallback'></a> MidtransCallback Payment Response Model

  All model for success extending the `BasePaymentResponse` model, so here's the list of model you can use for each type of payment :
  
| Payment Type  	| Model                                    	|
|---------------	|------------------------------------------	|
| BCA VA        	| BankTransferVaBcaPaymentResponse         	|
| BNI VA        	| BankTransferVaBniPaymentResponse         	|
| PERMATA VA    	| BankTransferVaPermataPaymentResponse     	|
| OTHER VA      	| BankTransferVaOtherPaymentResponse       	|
| AKULAKU       	| CardlessCreditAkulakuPaymentResponse     	|
| INDOMARET     	| ConvenienceStoreIndomaretPaymentResponse 	|
| CREDIT CARD   	| CreditCardPaymentResponse                	|
| DIRECT DEBIT  	| DirectDebitKlikBcaResponse               	|
| DIRECT DEBIT  	| DirectDebitMandiriClickpayResponse       	|
| CIMB CLICKS   	| OnlineDebitCimbClicksPaymentResponse     	|
| BCA KLIKPAY   	| OnlineDebitBcaKlikpayPaymentResponse     	|
| BRI EPAY      	| OnlineDebitBriEpayPaymentResponse        	|
| MANDIRI ECASH 	| EwalletMandiriEcashPaymentResponse       	|
| T-CASH        	| EwalletTelkomselCashPaymentResponse      	|
| GOPAY         	| EwalletGopayPaymentResponse              	|
| TOKENIZE CC   	| CardRegistrationResponse              	|
| CREDIT CARD   	| CreditCardPaymentResponse              	|
  

## Feature
This guide covers how to use the individual feature of our SDK.

> **From this part, it will assumes you've already declare or making CheckoutTransaction object as previous section step. And assume you construct CheckoutTransaction with builder for all payment method because most of feature will configure in CheckoutTransaction object, please use your needed feature.**

### Multi Currency

Multi currency is a feature to allow for multiple various currency display formatting. Currently you can use supported currency IDR (Indonesian Rupiah) and SGD (Singaporean Dollar). If you not set the currency, Midtrans will set it as Indonesian Rupiah or IDR.

```Java
CheckoutTransaction checkoutTransaction = CheckoutTransaction
                .builder(ORDER_ID, AMOUNT)
                .setCurrency(Currency.IDR)
                .build();
```

```Kotlin
val checkoutTransaction = CheckoutTransaction
                .builder(ORDER_ID, AMOUNT)
                .setCurrency(Currency.IDR)
                .build()
```

### Customer Info, Billing Info, Shipping Info

The Customer Info, Billing Info, and Shipping Info is optional, so user can pass the customer detail when making transaction and get it in response from SDK.

```Java
CheckoutTransaction checkoutTransaction = CheckoutTransaction
                .builder(ORDER_ID, AMOUNT)
                .setCustomerDetails(
                CustomerDetails
                    .builder()
                    .setFirstName("FirstName")
                    .setLastName("LastName")
                    .setEmail("mail@mailbox.com")
                    .setPhone("08123456789")
                    .setBillingAddress(
                        Address
                            .builder()
                            .setFirstName("FirstName")
                            .setLastName("LastName")
                            .setAddress("address")
                            .setCity("City")
                            .setPostalCode("12345")
                            .setPhone("08123456789")
                            .setCountryCode("IDR")
                            .build()
                    )
                    .setShippingAddress(
                        Address
                            .builder()
                            .setFirstName("FirstName")
                            .setLastName("LastName")
                            .setAddress("address")
                            .setCity("City")
                            .setPostalCode("12345")
                            .setPhone("08123456789")
                            .setCountryCode("IDR")
                            .build()
                    )
                    .build()
            	)
                .build();
```

```Kotlin
val checkoutTransaction = CheckoutTransaction
                .builder(ORDER_ID, AMOUNT)
                .setCustomerDetails(
                CustomerDetails
                    .builder()
                    .setFirstName("FirstName")
                    .setLastName("LastName")
                    .setEmail("mail@mailbox.com")
                    .setPhone("08123456789")
                    .setBillingAddress(
                        Address
                            .builder()
                            .setFirstName("FirstName")
                            .setLastName("LastName")
                            .setAddress("address")
                            .setCity("City")
                            .setPostalCode("12345")
                            .setPhone("08123456789")
                            .setCountryCode("IDR")
                            .build()
                    )
                    .setShippingAddress(
                        Address
                            .builder()
                            .setFirstName("FirstName")
                            .setLastName("LastName")
                            .setAddress("address")
                            .setCity("City")
                            .setPostalCode("12345")
                            .setPhone("08123456789")
                            .setCountryCode("IDR")
                            .build()
                    )
                    .build()
            	)
                .build()
```

### Item Details

You can set the detail of item of the transaction. If you put the item detail, you have to check the amount that previously you set in builder is same with total of price multiplied by quantity in Item Details List.

Item details are **required** for `Mandiri Bill/Mandiri Echannel` and `BCA KlikPay` payment, but it is **optional** for **other payment methods**. ItemDetails class holds information about item purchased by user. CheckoutTransaction takes an array list of item details.

```Java
CheckoutTransaction checkoutTransaction = CheckoutTransaction
                .builder(ORDER_ID, AMOUNT)
                .setItemDetails(new ArrayList<>(Arrays.asList(
                    new ItemDetails(ITEM_ID,
                        ITEM_PRICE,
                        ITEM_QUANTITY,
                        ITEM_NAME),
                    new ItemDetails(ITEM_ID,
                        ITEM_PRICE,
                        ITEM_QUANTITY,
                        ITEM_NAME),)
                ))
                .build();
```

```Kotlin
val checkoutTransaction = CheckoutTransaction
            .builder(ORDER_ID, AMOUNT)
            .setItemDetails(
                mutableListOf(
                    ItemDetails(ITEM_ID,
                        ITEM_PRICE,
                        ITEM_QUANTITY,
                        ITEM_NAME),
                    ItemDetails(ITEM_ID,
                        ITEM_PRICE,
                        ITEM_QUANTITY,
                        ITEM_NAME),
                )
            )
            .build()
```

### Enable payment

If you want to enable/disable some payment method without change the MAP setting or calling Midtrans's Support, you can set payment you want to activate.

```Java
CheckoutTransaction checkoutTransaction = CheckoutTransaction
                .builder(ORDER_ID, AMOUNT)
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

```Kotlin
val checkoutTransaction = CheckoutTransaction
            .builder(ORDER_ID, AMOUNT)
            .setEnabledPayments(
                mutableListOf(
                    BCA_VA,
                    BNI_VA,
                    PERMATA_VA,
                    CREDIT_CARD,
                    GOPAY,
                    INDOMARET,
                    KLIK_BCA)
            )
            .build()
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
                .builder(ORDER_ID, AMOUNT) 
                .setExpiry(new ExpiryModel("", ExpiryModelUnit.EXPIRY_UNIT_DAY, 1))
                .build();
```

```Kotlin
val checkoutTransaction = CheckoutTransaction
                .builder(ORDER_ID, AMOUNT) 
                .setExpiry(ExpiryModel(START_TIME, ExpiryModelUnit.EXPIRY_UNIT_DAY, 1))
                .build();
```

### Custom Field

These 3 fields will be brought at payment so it will be available at MAP and a HTTP notification will be sent to the merchant.

```Java
CheckoutTransaction checkoutTransaction = CheckoutTransaction
                .builder(ORDER_ID, AMOUNT)
                .setCustomField1("Custom Field 1")
                .setCustomField2("Custom Field 2")
                .setCustomField3("Custom Field 3")
                .build();
```

```Kotlin
val checkoutTransaction = CheckoutTransaction
                .builder(ORDER_ID, AMOUNT)
                .setCustomField1("Custom Field 1")
                .setCustomField2("Custom Field 2")
                .setCustomField3("Custom Field 3")
                .build()
```

### GO-PAY Callback Deeplink (Specific for GO-PAY)

GO-PAY provide 2 type result of transaction that can be received by host app, first method is with callback deeplink (to host-app) and the second is without callback deeplink. If you want to use callback deeplink for getting result from GO-JEK app, please set your deeplink to `CheckoutTransaction` object that previously you created. 

For example if you set your deeplink like this `demo:://midtrans` then GO-JEK app will return callback like this for success `demo://midtrans?order_id=xxxx&result=success` and this for failure `demo://midtrans?order_id=xxxx&result=failure`.

```Java
CheckoutTransaction checkoutTransaction = CheckoutTransaction
                .builder(ORDER_ID, AMOUNT)
                .setGopayCallbackDeepLink("demo://midtrans")
                .build();
```

```Kotlin
val checkoutTransaction = CheckoutTransaction
                .builder(ORDER_ID, AMOUNT)
                .setGopayCallbackDeepLink("demo://midtrans")
                .build()
```

### Bill Info (Specific for Mandiri Bill/Mandiri Echannel)

Bill Info is optional for `Mandiri Bill/Mandiri Echannel` payment only.

```Java
CheckoutTransaction checkoutTransaction = CheckoutTransaction
                .builder(ORDER_ID, AMOUNT)
                .setBillInfoModel(new BillInfoModel("Note 1", "Note 2"))
                .build();
```

```Kotlin
val checkoutTransaction = CheckoutTransaction
                .builder(ORDER_ID, AMOUNT)
                .setBillInfoModel(BillInfoModel("Note 1", "Note 2"))
                .build()
```

### Sub Company Code and Custom Virtual Account Number (Specific for BCA Bank Transfer VA)

This feature allows you to pass sub company code in VA payment and Make Custom Virtual Account Number. The sub company code must be exactly 5 digits of number. And you can pass free text to the inquiry and payment with multi language (Indonesia and English)


```Java
CheckoutTransaction checkoutTransaction = CheckoutTransaction
                .builder(ORDER_ID, AMOUNT)
                .setBcaVa(new BcaBankTransferRequestModel("12345",
                        new BcaBankFreeText(new ArrayList<BcaBankFreeTextLanguage>(),
                        new ArrayList<BcaBankFreeTextLanguage>()),
                        "123123"))
                .build();
```

```Kotlin
val checkoutTransaction = CheckoutTransaction
            .builder(ORDER_ID, AMOUNT)
            .setBcaVa(BcaBankTransferRequestModel(
                VA_NUMBER,
                BcaBankFreeText(
                    mutableListOf<BcaBankFreeTextLanguage>(), 
                    mutableListOf<BcaBankFreeTextLanguage>()
                )
            ))
            .build()
```

### Custom Virtual Account Number (Permata and BNI)

This feature allows you to make Custom Virtual Account Number.


```Java
CheckoutTransaction checkoutTransaction = CheckoutTransaction
                .builder(ORDER_ID, AMOUNT)
                .setBniVa(new BankTransferRequestModel("123123"))
                .setPermataVa(new BankTransferRequestModel("123123"))
                .build();
```

```Kotlin
val checkoutTransaction = CheckoutTransaction
                .builder(ORDER_ID, AMOUNT)
                .setBniVa(BankTransferRequestModel("123123"))
                .setPermataVa(BankTransferRequestModel("123123"))
                .build()
```

### Credit Card Options

This feature allows you to custom and setting credit card payment, CreditCard object use builder pattern and give 3 type of constructor based on type of creditcard payment, OneClick, TwoClick, and Normal. 

OneClick

```Java
        List<String> whiteList = new ArrayList<>(Arrays.asList("493496", "451197"));
        List<String> blackList = new ArrayList<>(Arrays.asList("493496", "451197"));
```

```Kotlin
        val whiteList = mutableListOf(WHITE_LIST_BIN, WHITE_LIST_BIN)
        val blackList = mutableListOf(BLACK_LIST_BIN, BLACK_LIST_BIN)
```

TwoClick

```Java
        CheckoutTransaction checkoutTransaction = CheckoutTransaction
                .builder("", 20.0)
                .setCreditCard(CreditCard
                        .twoClickBuilder(false)
                        .build())
                .build();
```

```Kotlin
        val whiteList = mutableListOf(WHITE_LIST_BIN, WHITE_LIST_BIN)
        val blackList = mutableListOf(BLACK_LIST_BIN, BLACK_LIST_BIN)
```

NormalClick

```Java
        List<String> whiteList = new ArrayList<>(Arrays.asList("493496", "451197"));
        List<String> blackList = new ArrayList<>(Arrays.asList("493496", "451197"));
```

```Kotlin
        val whiteList = mutableListOf(WHITE_LIST_BIN, WHITE_LIST_BIN)
        val blackList = mutableListOf(BLACK_LIST_BIN, BLACK_LIST_BIN)
```

if you want to enable whiteList or blacklist bins, please make new list of bins.

```Java
        List<String> whiteList = new ArrayList<>(Arrays.asList("493496", "451197"));
        List<String> blackList = new ArrayList<>(Arrays.asList("493496", "451197"));
```

```Kotlin
        val whiteList = mutableListOf(WHITE_LIST_BIN, WHITE_LIST_BIN)
        val blackList = mutableListOf(BLACK_LIST_BIN, BLACK_LIST_BIN)
```

If you want to enable installment, please make a hashmap contain bank and terms, it can be required.

```Java
        HashMap<String,List<Integer>> installment = new HashMap<>();
        installment.put("bca", new ArrayList<>(Arrays.asList(3,6,12)));
        installment.put("bni", new ArrayList<>(Arrays.asList(2,4,6)));
        installment.put("offline", new ArrayList<>(Arrays.asList(6,12,24)));
```

```Kotlin
        val installment = hashMapOf<String, MutableList<Int>>()
        installment["bca"] = mutableListOf(3, 6, 12)
        installment["bri"] = mutableListOf(2, 4, 6)
        installment["offline"] = mutableListOf(6,12,24)
```

If you want to enable save card, just set the saveCard true.

```Java
CheckoutTransaction checkoutTransaction = CheckoutTransaction
                .builder(ORDER_ID, AMOUNT)
                .setCreditCard(CreditCard
                        .normalClickBuilder(false, CreditCard.AUTHENTICATION_TYPE_NONE)
                        .setSaveCard(true)
                        .build())
                .build();

```

```Kotlin
        val checkoutTransaction = CheckoutTransaction
            .builder(ORDER_ID, AMOUNT)
            .setCreditCard(CreditCard
                .normalClickBuilder(false, CreditCard.AUTHENTICATION_TYPE_NONE)
                .setSaveCard(true)
                .build())
            .build()
```

**Complete code of credit card options**

```Java
        List<String> whiteList = new ArrayList<>(Arrays.asList("493496", "451197"));
        List<String> blackList = new ArrayList<>(Arrays.asList("493496", "451197"));
        HashMap<String,List<Integer>> installment = new HashMap<>();
        installment.put("bca", new ArrayList<>(Arrays.asList(3,6,12)));
        installment.put("bni", new ArrayList<>(Arrays.asList(2,4,6)));
        installment.put("offline", new ArrayList<>(Arrays.asList(6,12,24)));

        CheckoutTransaction checkoutTransaction = CheckoutTransaction
                .builder("", 20.0)
                .setCreditCard(CreditCard
                        .normalClickBuilder(false, CreditCard.AUTHENTICATION_TYPE_NONE)
                        .setSaveCard(true)
                        .setBank(BankType.BNI)
                        .setInstallment(true, installment)
                        .setBlackListBins(blackList)
                        .setWhiteListBins(whiteList)
                        .setChannel(CreditCard.MIGS)
                        .build())
                .build();
```

```Kotlin
        val whiteList = mutableListOf(WHITE_LIST_BIN, WHITE_LIST_BIN)
        val blackList = mutableListOf(BLACK_LIST_BIN, BLACK_LIST_BIN)
        val installment = hashMapOf<String, MutableList<Int>>()
        installment["bca"] = mutableListOf(3, 6, 12)
        installment["bri"] = mutableListOf(2, 4, 6)
        installment["offline"] = mutableListOf(6,12,24)

        val checkoutTransaction = CheckoutTransaction
            .builder(ORDER_ID, AMOUNT)
            .setCreditCard(CreditCard
                .normalClickBuilder(false, CreditCard.AUTHENTICATION_TYPE_NONE)
                .setSaveCard(true)
                .setBank(BankType.BNI)
                .setInstallment(true, installment)
                .setBlackListBins(blackList)
                .setWhiteListBins(whiteList)
                .setChannel(CreditCard.MIGS)
                .build())
            .build()
```


### Complete Checkout Transaction Object

```Java
CheckoutTransaction checkoutTransaction = CheckoutTransaction
                .builder(ORDER_ID, AMOUNT)
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
                .setCustomField1(STRING_CUSTOM_FIELD_1)
                .setCustomField2(STRING_CUSTOM_FIELD_2)
                .setCustomField3(STRING_CUSTOM_FIELD_3)
                .setGopayCallbackDeepLink("demo://midtrans")
                .setBillInfoModel(new BillInfoModel(BILL_INFO_1, BILL_INFO_2))
                .setBcaVa(new BcaBankTransferRequestModel(CUSTOM_VA,
                        new BcaBankFreeText(new ArrayList<BcaBankFreeTextLanguage>(),
                        new ArrayList<BcaBankFreeTextLanguage>()),FREE_TEXT))
                .setBniVa(new BankTransferRequestModel(""))
                .setPermataVa(new BankTransferRequestModel(""))
                .build();
```

```Kotlin
val checkoutTransaction = CheckoutTransaction
            .builder(ORDER_ID, AMOUNT)
            .setCurrency(Currency.IDR)
            .setCustomerDetails(
                CustomerDetails(
                    "FirstName",
                    "LastName",
                    "email@mail.com",
                    "6281234567890",
                    ShippingAddress(
                        "First Name",
                        "Lastname",
                        "email@mail.com",
                        "City",
                        "1234",
                        "6281234567890",
                        "IDN"
                    ),
                    BillingAddress(
                        "First Name",
                        "LastName",
                        "email@mail.com",
                        "City",
                        "1234",
                        "6281234567890",
                        "IDN"
                    )
                )
            )
            .setItemDetails(
                mutableListOf(
                    ItemDetails(ITEM_ID,
                        ITEM_PRICE,
                        ITEM_QUANTITY,
                        ITEM_NAME),
                    ItemDetails(ITEM_ID,
                        ITEM_PRICE,
                        ITEM_QUANTITY,
                        ITEM_NAME),
                    )
            )
            .setEnabledPayments(
                mutableListOf(
                    BCA_VA,
                    BNI_VA,
                    PERMATA_VA,
                    CREDIT_CARD,
                    GOPAY,
                    INDOMARET,
                    KLIK_BCA)
            )
            .setExpiry(
                ExpiryModel(
                    START_TIME,
                    ExpiryModelUnit.EXPIRY_UNIT_DAY,
                    1
                )
            )
            .setCustomField1("Custom Field 1")
            .setCustomField2("Custom Field 2")
            .setCustomField3("Custom Field 3")
            .setGopayCallbackDeepLink("demo://midtrans")
            .setBillInfoModel(
                BillInfoModel(
                    STRING_BILL_INFO,
                    STRING_BILL_INFO
                )
            )
            .setBcaVa(
                BcaBankTransferRequestModel(
                    CUSTOM_VA,
                    BcaBankFreeText(
                        mutableListOf<BcaBankFreeTextLanguage>(),
                        mutableListOf<BcaBankFreeTextLanguage>()
                    )
                ))
            .setBniVa(BankTransferRequestModel("123123"))
            .setPermataVa(BankTransferRequestModel("123123"))
            .setCreditCard(CreditCard
                .normalClickBuilder(false, CreditCard.AUTHENTICATION_TYPE_NONE)
                .setSaveCard(true)
                .setBank(BankType.BNI)
                .setInstallment(false, installment)
                .setBlackListBins(blackList)
                .setWhiteListBins(whiteList)
                .setChannel(CreditCard.MIGS)
                .build())
            .build()
```

## 8.3. <a name='enum'></a>Enum and Static Variable

### Bank Type

| BankType 	| Description  	|
|----------	|--------------	|
| CIMB     	| CIMB         	|
| BCA      	| BCA          	|
| MANDIRI  	| Bank Mandiri 	|
| BNI      	| BNI          	|
| PERMATA  	| Bank Permata 	|
| BRI      	| BRI          	|


### Currency

| Currency 	| Description      	|
|----------	|------------------	|
| IDR      	| Indonesia Rupiah 	|
| SGD      	| Singapore Dollar 	|


### Payment Type

| PaymentType      	| Description              	|
|------------------	|--------------------------	|
| CREDIT_CARD      	| Credit Card              	|
| BNI_VA           	| Bank Transfer/VA BNI     	|
| BCA_VA           	| Bank Transfer/VA BCA     	|
| PERMATA_VA       	| Bank Transfer/VA Permata 	|
| OTHER_VA         	| Bank Transfer/VA Other   	|
| GOPAY            	| GO-PAY                   	|
| TELKOMSEL_CASH   	| Telkomsel Cash (T-Cash)  	|
| MANDIRI_ECASH    	| Mandiri Ecash            	|
| BCA_KLIKPAY      	| BCA Klikpay              	|
| CIMB_CLICKS      	| CIMB Clicks              	|
| BRI_EPAY         	| BRI Epay                 	|
| MANDIRI_CLICKPAY 	| Mandiri Clickpay         	|
| KLIK_BCA         	| Klik BCA                 	|
| INDOMARET        	| Indomaret                	|
| AKULAKU          	| Akulaku                  	|


### Authentication

| Environment 	| Description                 	|
|-------------	|-----------------------------	|
| AUTH_3DS    	| Use if you want to use 3DS  	|
| AUTH_RBA    	| Use if you want to use RBA  	|
| AUTH_NONE   	| Use if you not use any auth 	|


### Environment

| Environment 	| Description                         	|
|-------------	|-------------------------------------	|
| SANDBOX     	| Use this for SANDBOX environment    	|
| PRODUCTION  	| Use this for PRODUCTION environment 	|


### Expiry Unit

| ExpiryModelUnit    	| Description     	|
|--------------------	|-----------------	|
| `EXPIRY_UNIT_MINUTE`	| count as minute 	|
| `EXPIRY_UNIT_HOUR`   	| count as hour   	|
| `EXPIRY_UNIT_DAY`    	| count as day    	|