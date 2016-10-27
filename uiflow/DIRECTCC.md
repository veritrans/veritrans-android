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

## Authentication Token to Use Two Clicks or One Click 

This is only required when using two clicks or one click type payment.

### Setup event bus

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

Implement `GetAuthenticationBusCallback`. Add `@Subscribe` to each implemented methods.

```Java
    @Subscribe
    @Override
    public void onEvent(AuthenticationEvent authenticationEvent) {
        // Save auth token to differentiate each user saved token
        String auth = authenticationEvent.getResponse().getxAuth();
        LocalDataHandler.saveString(Constants.AUTH_TOKEN, auth);
    }

    @Subscribe
    @Override
    public void onEvent(NetworkUnavailableEvent networkUnavailableEvent) {
        // Handle network not available condition
    }

    @Subscribe
    @Override
    public void onEvent(GeneralErrorEvent generalErrorEvent) {
        // Handle generic error condition
    }
```

### Start Getting Authentication Token

```
VeritransSDK.getVeritransSDK().getAuthenticationToken();
```


## Initialize Transaction Request and Select Payment Type

In this flow you must set transaction request information to SDK.

```Java
// Create transaction request object
TransactionRequest transactionRequest = new TransactionRequest(TRANSACTION_ID, TRANSACTION_AMOUNT);
transactionRequest.setCardPaymentInfo(CARD_CLICK_TYPE, IS_SECURE);

// Define item details
ItemDetails itemDetails = new ItemDetails(ITEM_ID, ITEM_PRICE, ITEM_QUANTITY, ITEM_NAME);
ItemDetails itemDetails1 = new ItemDetails(ITEM_ID, ITEM_PRICE, ITEM_QUANTITY, ITEM_NAME);
ItemDetails itemDetails2 = new ItemDetails(ITEM_ID, ITEM_PRICE, ITEM_QUANTITY, ITEM_NAME);

// Add item details into item detail list.
ArrayList<ItemDetails> itemDetailsArrayList = new ArrayList<>();
itemDetailsArrayList.add(itemDetails);
itemDetailsArrayList.add(itemDetails1);
itemDetailsArrayList.add(itemDetails2);

// Set item details into transaction request object
transactionRequest.setItemDetails(itemDetailsArrayList);

// Set transaction request object into VeritransSDK instance
VeritransSDK.getVeritransSDK().setTransactionRequest(transactionRequest);
```

Note: 

- `CARD_CLICK_TYPE`: Type of payment. Please select between these values:
     - `normal` : Normal transaction. Save card options was disabled in this mode.
     - `two_click` : Two click transaction. Save card options was enabled in this mode.
     - `one_click` : One click transaction. Save card options was enabled in this mode.
- `IS_SECURE`
     - true : Enable 3D Secure authorization
     - false : Disable 3D Secure authorization
- Total amount from all item details must be same as `TRANSACTION_AMOUNT` provided in `TransactionRequest`
     
## Initialize User Details

To skip user details screen, you must set `UserDetails` using this codes

```Java
// Set user details
UserDetail userDetail = new UserDetail();
userDetail.setUserFullName(FULL_NAME);
userDetail.setEmail(EMAIL);
userDetail.setPhoneNumber(PHONE_NUMBER);

// Initiate address list
ArrayList<UserAddress> userAddresses = new ArrayList<>();

// Initiate and add shipping address
UserAddress shippingUserAddress = new UserAddress();
shippingUserAddress.setAddress(shippingAddress);
shippingUserAddress.setCity(shippingCity);
shippingUserAddress.setCountry(shippingCountry);
shippingUserAddress.setZipcode(shippingZipcode);
shippingUserAddress.setAddressType(Constants.ADDRESS_TYPE_SHIPPING);
userAddresses.add(shippingUserAddress);

// Initiate and add billing address
UserAddress billingUserAddress = new UserAddress();
billingUserAddress.setAddress(billingAddress);
billingUserAddress.setCity(billingCity);
billingUserAddress.setCountry(country);
billingUserAddress.setZipcode(zipcode);
billingUserAddress.setAddressType(Constants.ADDRESS_TYPE_BILLING);
userAddresses.add(billingUserAddress);

// if shipping address is same billing address
// you can user type Constants.ADDRESS_TYPE_BOTH 
// NOTE: if you use this, skip initiate shipping and billing address above
UserAddress userAddress = new UserAddress();
userAddress.setAddress(billingAddress);
userAddress.setCity(billingCity);
userAddress.setCountry(country);
userAddress.setZipcode(zipcode);
userAddress.setAddressType(Constants.ADDRESS_TYPE_BOTH);
userAddresses.add(userAddress);

// Set user address to user detail object
userDetail.setUserAddresses(userAddresses);

// Save the user detail. It will skip the user detail screen
LocalDataHandler.saveObject("user_details", userDetail);
```

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
