# Overview

In this flow SDK provides an UI to take required information from user to execute transaction.

To perform transaction using this, follow the steps given below:

1. Setup SDK and customize it (optional).
2. Set transaction request information to SDK.
3. Add event bus subscriber to capture finished transaction response.
4. Specify which transaction method that is supported by merchant.
5. Call the startPaymentUiFlow().

# Usage
besides [initialization step](https://github.com/veritrans/veritrans-android/wiki/SDK-Initialization#uiflow-initialization) uiflow theme costumization can be done using VeritransSDK instance

## Theme Customization

To apply Custom fonts, you can use this code.

```Java
MidtransSDK midtransSDK = MidtransSDK.getInstance();
midtransSDK.setDefaultText("open_sans_regular.ttf");
midtransSDK.setSemiBoldText("open_sans_semibold.ttf");
midtransSDK.setBoldText("open_sans_bold.ttf");
```
Note: open_sans_regular.ttf, open_sans_semibold.ttf, open_sans_bold.ttf is path of the custom font on the assets directory.

Also you can set the color primary in your theme in `styles.xml`.

```xml
<!-- Base application theme. -->
<style name="AppTheme" parent="Theme.AppCompat.Light.NoActionBar">
        <item name="colorPrimary">@color/colorPrimary</item>
        <item name="colorAccent">@color/colorAccent</item>
        <item name="colorPrimaryDark">@color/colorPrimaryDark</item>
        <item name="colorButtonNormal">@color/colorButton</item>
</style>
```

Then to ensure this replace library theme, please add these lines into your `AndroidManifest.xml` application tag.

```xml
<application
        ...
        android:theme="AppTheme"
        tools:replace="android:theme">
```

## Event Bus Setup

In your calling activity, just implement `TransactionFinishedCallback`.

```Java

// Implemented methods of TransactionFinishedCallback interface
    public void onTransactionFinished(TransactionResult result);
```
## Starting UI Flow

There are two modes of UI flow.

### Payment Mode

In this flow you **must** set transaction request information to SDK.

```Java
TransactionRequest transactionRequest = new TransactionRequest(TRANSACTION_ID, TRANSACTION_AMOUNT);
midtransSDK.setTransactionRequest(transactionRequest);
```

If you support credit card payment, you must select one of three card click types.

```Java
transactionRequest.setCardPaymentInfo(CARD_CLICK_TYPE, IS_SECURE);

CARD_CLICK_TYPE - type of card use these resource strings:

- R.string.card_click_type_one_click - for one click
- R.string.card_click_type_two_click - for two click
- R.string.card_click_type_none - for normal transaction

IS_SECURE - set to true if using 3D secure
```

For more information about transaction request please read [this wiki](https://github.com/veritrans/veritrans-android/wiki/Core-Flow#setting-transaction-details).

To use default UI for Charging Transactions, you can call `startPaymentUiFlow` using current context.

```Java
// start ui flow using activity context
mMidtransSDK.startPaymentUiFlow(context);
```

