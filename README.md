# Veritrans Android SDK

[![Join the chat at https://gitter.im/veritrans/veritrans-android](https://badges.gitter.im/veritrans/veritrans-android.svg)](https://gitter.im/veritrans/veritrans-android?utm_source=badge&utm_medium=badge&utm_campaign=pr-badge&utm_content=badge)

## Overview

Veritrans SDK is an android library that simplifies the process of making transactions on [Veritrans Payment gateway](https://veritrans.co.id).

### Transaction Flow

![Transaction Flow Figure](http://docs.veritrans.co.id/images/vtdirect-mobile-flow.png "Transaction Flow Figure")

There are three parties involved in the payment process for making a payment:
* merchant,
* customers
* Veritrans.

This is how the Mobile SDK works

1. *Click Pay*: Customers enter credit card details on your mobile application, and press the button `PAY` to complete the payment.
2. *Sending data*: When customers press the button `PAY`, our mobile SDK will incorporate sensitive credit card information with a key client. The combined data is sent to the Veritrans server that is exchanged into *onetime-token* ( "token").
3. **Getting Token**: Veritrans Token will return the application to customers android phone. Token will be valid for **3 minutes** at a regular credit card transactions and **10 minutes** if the feature is 3D Secure is used. Token cannot be used again if the expired time was passed.
4. **Sending Token**: Then Token is sent to merchant server. Please note, merchant will only receive a token in as the exchange for credit card details. **Merchant should not store or process information from credit cards**. This ensures that the merchant is PCI-DSS compliant.
5. **Request Charge**: Merchant server requests a charge transaction to Veritrans using Token plus some other information (example: customer details, product details, address).
6. **Transaction Response Handling**: Veritrans returns the response status of the transaction, such as: Capture, Settlement
7. **Transaction information**: Merchant tells the transaction results to customers, ie.The purchase is confirmed.

### Payment types Supported
1. Credit/Debit - Support for making payments via credit cards and or debit cards using our one-click or two-clicks feature
2. Mandiri ClickPay -
3. CIMB Clicks -
4. ePay BRI  -
5. BBM Money -
6. Indosat Dompetku -
7. Mandiri e-Cash  -
8. Bank Transfer   - Support payment using Permata Virtual Account.
9. Mandiri Bill Payment
10. Indomaret - Payment via convineience Stores

### Other features
1. Offers - supports offers like BIN Promos, Discounts , Installments  etc.
2. Register Card : Securely store the credit card tokens with the merchants for seamless payment experience


### Supported Modes
1. *UI flow* (Recommended) : We provide the drop in User interface for making transactions on all the payment types supported by Veritrans. Watch the [video](link) for the default UI flow example. 
2. *Core flow* : We provide an API only implementation for all payment types, This allows users to `Bring your own UI` to the mobile App. 

### Prerequsites

1. Please familiarize yourself with our [documentation](http://docs.veritrans.co.id/en/welcome/index.html)
2. Create a merchant account in [MAP](https://my.veritrans.co.id)
3. Setup your merchant accounts settings, in particular Notification URL.
4. Checkout the [Veritrans SDK Demo App](https://github.com/veritrans/veritrans-android-example) and walk through the implementation.
5. Checkout the [Veritrans Merchant server Reference Implementation](https://github.com/veritrans/mobile-merchant-server), and walk through the API's that you may need to implement on your backend server.

### Security Aspects

* There are 2 seperate keys *CLIENT_KEY* and *SERVER_KEY* (available on [MAP](https://my.veritrans.co.id))
  * CLIENT_KEY is used for tokenizing the credit card. It can only be used from the Client(mobile device)
  * SERVER_KEY is *not* to be used from the Device, all API requests that use the SERVER_KEY need to be made from the Merchant Server.
* We use strong encryption for making connections to Merchant server, please make sure it has valid https Certificate.
* Please be careful logging payment related information to the Logs(especialy when using Core flow).


Following are  configurable parameters of sdk that can be used while performing transaction -

1. Merchant server Endpoint- url of server to which transaction data will be sent. This will also be referred to as a merchant server
2. Transaction details - contains payment information like amount, order Id, payment method etc.
3. Veritrans Client Key - token that specified by merchant server to enable the transaction using `credit card`. Available on the [MAP](https://my.veritrans.co.id)



### Setting up Android SDK

We publish our SDK to [bintray repository](www.bintray.com). Please add bintray in your repository list(as below)

1. Add the following into your _build.gradle_

```groovy
dependencies {
    // For using the Veritrans Sandbox
    compile 'id.co.veritrans:androidsdk:0.10.3-SANDBOX'
    // For using the Veritrans Production
    compile 'id.co.veritrans:androidsdk:0.10.3'
}
```
Following bintray repository needs to be added to your repository section in _build.gradle_

```groovy
repositories {
    jcenter()
        // Add the midtrans repository into the list of repositories
        maven { url "http://dl.bintray.com/pt-midtrans/maven" }
    }
```


2. Add the Merchant server URL and the Veritrans client Key in the configuration into your _build.gradle_ file (Note: We have different CLIENT_KEY values for *sandbox* and *production* so it's better if you have two flavors in `build.gradle` script and define them here.)

```groovy
productFlavors {
        development {
            buildConfigField "String", "BASE_URL", <MERCHANT_BASE_URL_SANDBOX>
            buildConfigField "String", "CLIENT_KEY", <MERCHANT_CLIENT_KEY_SANDBOX>
        }
        production {
            buildConfigField "String", "BASE_URL", <MERCHANT_BASE_URL_PRODUCTION>
            buildConfigField "String", "CLIENT_KEY", <MERCHANT_CLIENT_KEY_PRODUCTION>
        }
}
```

3. Modify the  **_AndroidManifest.xml_** file to allow network access

  These permissions are required to allow the application to send
  events.

  ```xml
  <uses-permission
    android:name="android.permission.INTERNET" />

  <uses-permission
    android:name="android.permission.ACCESS_NETWORK_STATE" />

  ```


4. Setup Proguard Rules




#### IDE Setup

##### Eclipse:
**Note** - To add this library project in eclipse follow the instructions given on following link
 http://developer.android.com/tools/projects/projects-eclipse.html

##### Android Studio
TBD


#### Initializing the SDK

Instantiate sdk after application finishes launching processes. This can be done on Application class or your Main Activity class then call it at `onCreate` method.

```Java
// SDK initialization process
VeritransBuilder veritransBuilder = new VeritransBuilder(context, VT_CLIENT_KEY, BASE_URL_MERCHANT);
veritransBuilder.enableLog(true);   // enable logs for debugging purpose.
veritransBuilder.buildSDK();
```

Or if you have defined the client key and in your `build.gradle` script just like installation tutorial above.

```Java
VeritransBuilder veritransBuilder = new VeritransBuilder(context, BuildConfig.CLIENT_KEY, BuildConfig.BASE_URL);
veritransBuilder.enableLog(true);   // enable logs for debugging purpose.
veritransBuilder.buildSDK();
```

- context - Application context.
- CLIENT_KEY - Provided by MAP
- BASE_URL - Merchant server Base URL


Instance of Veritrans SDK is singleton.

The SDK instance is accessible via

```Java
VeritransSDK.getVeritransSDK();
```

In order to perform transactions set appropriate details to it using various setter methods,
invoke payment method to perform transaction. You can perform only one transaction at a time.

## Implementation

There are two implementation modes on this SDK.

## UI Flow

To see the implementation guide please read [this wiki](https://github.com/veritrans/veritrans-android/wiki/UI-Flow).

## Core Flow

To see the implementation guide please read [this wiki](https://github.com/veritrans/veritrans-android/wiki/Core-Flow).

Please keep in mind that [VTAndroidLib](https://github.com/veritrans/vt-Androidlib) a different library than the current Veritrans SDK, and is not considered depricated and we provide no backward compatibility.
