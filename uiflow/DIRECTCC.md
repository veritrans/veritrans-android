# UI Flow - Direct Credit Card Screen

In UI flow you can use credit card payment screen directly.

## How to install

Add midtrans repo into your repository in gradle.
```Groovy
maven { url "http://dl.bintray.com/pt-midtrans/maven" }
```

Add this line into your dependency.

If you're using sandbox mode.
```Groovy
compile 'id.co.veritrans:uiflow:0.11.3-SANDBOX'
```

If you're using production mode.
```Groovy
compile 'id.co.veritrans:uiflow:0.11.3'
```

Or if you use different flavor in your `build.gradle`.
For example: sandbox and production.
```Groovy
sandboxCompile 'id.co.veritrans:uiflow:0.11.3-SANDBOX'
productionCompile 'id.co.veritrans:uiflow:0.11.3'
```

If you're got conflict when merging manifest, please add this line into your default `AndroidManifest.xml`.
```xml
<application
     ...
     android:theme="@style/AppTheme.Indigo"
     tools:replace="android:theme">
     ...
</application>
```

## How to Initialize UI Flow SDK

Initialise SDK using `SDKUiFlowBuilder`.
```Java
VeritransSDK veritransSDK = new SdkUIFlowBuilder(CONTEXT, CLIENT_KEY, BASE_URL)
                    .setUIFlow(new UIFlow())// initialization uiflow mode
                    .setExternalScanner(new ScanCard()) // Optional. Initialization for using external scancard
                    .enableLog(true)
                    .setMerchantLogoResourceId(R.drawable.MERCHANT_LOGO_ID)
                    .buildSDK();
```

## Setup Event Bus to Capture Transaction Finished

Implement `TransactionFinishedCallback` in your activity or calling class. 
It require you to implement one method. 
Please add `@Subscribe` in the method.
```Java
@Subscribe
@Override
public void onEvent(TransactionFinishedEvent transactionFinishedEvent) {
    // Handle success event
    // Capture transaction response
    TransactionResponse response = transactionFinishedEvent.getResponse();
}
```

## Start Credit Card Payment
```Java
VeritransSDK.getVeritransSDK().startCreditCardPayment(CONTEXT);
```
