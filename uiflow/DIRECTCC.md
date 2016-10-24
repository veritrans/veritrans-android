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

Initialize SDK using `SDKUiFlowBuilder`.
```Java
VeritransSDK veritransSDK = new SdkUIFlowBuilder(CONTEXT, CLIENT_KEY, BASE_URL)
                    .setUIFlow(new UIFlow())// initialization uiflow mode
                    .setExternalScanner(new ScanCard()) // Optional. Initialization for using external scancard
                    .enableLog(true)
                    .setMerchantName(MERCHANT_NAME)
                    .setMerchantLogoResourceId(R.drawable.MERCHANT_LOGO_ID)
                    .buildSDK();
```

## Initialize Transaction Request and Select Payment Type

In this flow you must set transaction request information to SDK.

```Java
TransactionRequest transactionRequest = new TransactionRequest(TRANSACTION_ID, TRANSACTION_AMOUNT);
transactionRequest.setCardPaymentInfo(CARD_CLICK_TYPE, IS_SECURE);
veritransSDK.setTransactionRequest(transactionRequest);
```

Note: 

- `CARD_CLICK_TYPE`: Type of payment. Please select between these values:
     - `normal` : Normal transaction. Save card options was disabled in this mode.
     - `two_click` : Two click transaction. Save card options was enabled in this mode.
     - `one_click` : One click transaction. Save card options was enabled in this mode.
- `IS_SECURE`
     - true : Enable 3D Secure authorization
     - false : Disable 3D Secure authorization

## Setup Event Bus to Capture Transaction Finished

Register your activity or calling class to `VeritransBus` by using `VeritransBusProvider`.

```Java
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VeritransBusProvider.getInstance().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VeritransBusProvider.getInstance().unregister(this);
    }
```

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
