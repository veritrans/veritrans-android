# Midtrans UI SDK

Midtrans UI SDK is set of payment UI implementation for Midtrans payment gateway.

## 1. How to Install

Add this dependencies on your gradle script.

```groovy
compile 'com.midtrans.sdk:ui:2.0.0'
```

## 2. SDK Initialisation

Initialise SDK instance in your application or main activity class.

```java
new MidtransUi.Builder()
    .setContext(APPLICATION_CONTEXT)
    .setClientKey(CLIENT_KEY)
    .setEnvironment(Environment.SANDBOX|PRODUCTION)
    // Custom font path relative to assets directory
    .setDefaultFontPath(DEFAULT_FONT)
    // Custom font path relative to assets directory
    .setBoldFontPath(BOLD_FONT)
    // Custom font path relative to assets directory
    .setSemiBoldFontPath(SEMI_BOLD_FONT)
    .enableLog(true)
    .build();
```

## 3. Payment Flow

### 3.1. Provide Checkout Token

Basically you can provide your own checkout token with your own checkout implementation.
Please see [Snap backend integration](https://snap-docs.midtrans.com/#backend-integration) to get the checkout token.

```java
MidtransUi
    .getInstance()
    .runUiSdk(
        APPLICATION_CONTEXT, 
        CHECKOUT_TOKEN, 
        new MidtransUiCallback() {
            @Override
            public void onFinished(PaymentResult result) {
                
            }
        }
    );
```

### 3.2. Integrated Checkout

This flow require merchant to implement checkout API endpoint to Snap backend.

Basic implementation of this endpoint is to be a proxy between mobile SDK and Snap backend. 

Why need a proxy? 

- Because the request require a **server key** which can't be saved on client.
- For basic implementation, request body will be created on SDK so server don't need to build the request body. It just adds the **server key** on the header. 

**Note:** For advanced checkout implementation which includes advanced credit card features (for example: `whitelist_bins`, `installment`, etc.) or merchant just want to customize the checkout request, server can intercept the request body and rebuild the request body before sending the request to Snap backend.

So the flow will be like this:

1. Mobile SDK send checkout request to merchant checkout URL (**POST**)
2. Merchant server receive the checkout request from Mobile SDK
3. Merchant add header into the request : `Authorization : Base base64(ServerKey:)`
4. Merchant send the request
    - If merchant want to modify the request body they can intercept the request, modify it then send the modified request to Snap backend.
    - Else they just send the request to Snap backend
5. Snap backend receive the final checkout request
6. Snap backend send the response to merchant server
7. Merchant send the response from Snap backend (unmodified) to merchant server
8. Mobile SDK receive the checkout token response
9. Mobile SDK proceed to payment page with received checkout token

### 3.2.1 Build Checkout Token Request

Complete checkout token request JSON format are available at [Snap backend integration](https://snap-docs.midtrans.com/#backend-integration). 

This SDK will help build and send checkout token request to merchant checkout endpoint.

Here are an example to build checkout token request.

```java
private CheckoutTokenRequest initialiseCheckoutTokenRequest() {
        // Setup order details
        CheckoutOrderDetails checkoutOrderDetails = new CheckoutOrderDetails(ORDER_ID, GROSS_AMOUNT);
        // Setup customer details
        CustomerDetails customerDetails = new CustomerDetails(
                FIRST_NAME, LAST_NAME, EMAIL, PHONE_NUMBER,
                // Shipping Address
                new Address(FIRST_NAME, LAST_NAME, ADDRESS, CITY, POSTAL_CODE, PHONE_NUMBER, COUNTRY_CODE),
                // Billing Address
                new Address(FIRST_NAME, LAST_NAME, ADDRESS, CITY, POSTAL_CODE, PHONE_NUMBER, COUNTRY_CODE));
                
        // Setup item details
        List<ItemDetails> itemDetailsList = new ArrayList<>();
        ItemDetails itemDetails = new ItemDetails(ITEM_ID, ITEM_PRICE, ITEM_QUANTITY, ITEM_NAME);
        ItemDetails itemDetails2 = new ItemDetails(ITEM_ID, ITEM_PRICE, ITEM_QUANTITY, ITEM_NAME);
        itemDetailsList.add(itemDetails);
        itemDetailsList.add(itemDetails2);
        
        // Create credit card payment options
        
        CreditCard creditCard = new CreditCard();
        
        // Set acquiring bank if merchant needs to direct the payment to specific bank
        if (isUseMandiriAcquiringBank()) {
            // Set bank to Mandiri
            creditCard.setBank(BankType.MANDIRI);
        } else if (isUseBniAcquiringBank()) {
            // Set bank to BNI
            creditCard.setBank(BankType.BNI);
        } else if (isUseBcaAcquiringBank()) {
            //Set bank to BCA
            creditCard.setBank(BankType.BCA);
            // credit card payment using bank BCA need migs channel
            creditCard.setChannel(Channel.MIGS);
        } else if (isUseMaybankAcquiringBank()) {
            //Set bank to Maybank
            creditCard.setBank(BankType.MAYBANK);
            // credit card payment using bank Maybank need migs channel
            creditCard.setChannel(Channel.MIGS);
        } else if (isUseBriAcquiringBank()) {
            // Set bank to BRI
            creditCard.setBank(BankType.BRI);
            // credit card payment using bank BRI need migs channel
            creditCard.setChannel(Channel.MIGS);
        }

        if (isPreAuthorizationModeEnabled()) {
            // Set Pre Auth mode
            creditCard.setType(CardTokenRequest.TYPE_AUTHORIZE);
        }
        
        // Enable or disable 3D Secure authorization
        creditCard.setSecure(true|false);
        // Enable or disable save card
        creditCard.setSaveCard(true);
        
        // Custom field supports
        String customField1 = etCustomField1.getText().toString();
        String customField2 = etCustomField2.getText().toString();
        String customField3 = etCustomField3.getText().toString();

        return CheckoutTokenRequest.newCompleteCheckout(USER_ID, creditCard, itemDetailsList, customerDetails, checkoutOrderDetails, null, customField1, customField2, customField3);
    }
```

Note:

- `GROSS_AMOUNT` and `ITEM_PRICE` are in `integer` type.
- `COUNTRY_CODE` uses [ISO 3166-1 alpha-3](https://en.wikipedia.org/wiki/ISO_3166-1_alpha-3) standard. For example: Indonesia code are `IDN`.
- Maximum length of `FIRST_NAME` and `LAST_NAME` are **20**.
- Maximum length of `ITEM_NAME` is **50**.
- Subtotal (`ITEM_PRICE` multiplied by `ITEM_QUANTITY`) of all items **needs** to be **exactly the same** as `GROSS_AMOUNT` in the `checkoutOrderDetails`.
- `USER_ID` is needed when you want to let Snap backend save customer's card token so it can be used to next payment by providing same `USER_ID` in next checkout. Saved card token types are divided into two:
    - One click token require merchant to have recurring MID and also 3D Secure enabled in the first transaction.
    - By default saved card token have `two_clicks` type. 

### 3.2.2 Start Payment 

Assume that step 3.2.1 is completed, payment now can be started.

```java
MidtransUi
    .getInstance()
    .runUiSdk(
        APPLICATION_CONTEXT, 
        CHECKOUT_URL, CHECKOUT_TOKEN_REQUEST, 
        new MidtransUiCallback() {
            @Override
            public void onFinished(PaymentResult result) {
                
            }
        }
    );
```

### Determine Payment Result Status

```java
if (result.isCanceled()) {
    // Payment is canceled
} else {
    // Payment is finished
    // Check the status
    String status = result.getPaymentStatus();
    switch (status) {
        case PaymentStatus.PENDING:
            // Pending status for VA (BCA, Mandiri, Permata), KlikBCA, Indomaret and Kioson
            
            // For E-Banking transaction such as BCAKlikpay, CIMB Clicks, BRI E-Pay, Mandiri E-Cash 
            // Merchant need to get latest transaction status using Midtrans API.
            break;
        case PaymentStatus.CHALLENGE:
            // Credit debit card only
            // Transaction is questioned by FDS
            break;
        case PaymentStatus.DENY:
            // Credit card only
            // Denied by FDS or bank
            break;
        case PaymentStatus.INVALID:
            // Invalid transaction
            // No error message
            break;
        case PaymentStatus.FAILED:
            // Error transaction
            // Internal error detected
            break;
        }
    }
```

Note for **E-Banking Transactions** (BCA KlikPay, CIMB Clicks, BRI E-Pay, Mandiri E-Cash) :

- Customer complete the transaction on WebView so SDK don't know the latest transaction status whether it is succeed or failed.
- Mobile SDK only returns transaction response when doing the charge to Midtrans API. 
- Transaction response returned by Mobile SDK contains `transaction_id`. Merchant should get transaction status from Midtrans [Get Transaction Status API](https://api-docs.midtrans.com/#get-transaction-status) using that `transaction_id`. 


## 4. UI Customisation

There are some UI elements that can be customized.

### 4.1 UI Behavior

There are some Midtrans UI behavior that can be customised.

- Page transition animation
- Default checked state when save card on credit card options was enabled.
- Payment status that is shown after transaction was finished.

```java
CustomSetting customSetting = new CustomSetting();
        
customSetting.setAnimationEnabled(true);
customSetting.setSaveCardChecked(true);
customSetting.setShowPaymentStatus(true);

MidtransUi.getInstance().setCustomSetting(customSetting);
```

### 4.2 Custom Color Theme

By default Midtrans UI color theme will follow Snap preferences that merchant set on **MAP -> Settings -> Snap Preferences**.

This feature can be used if merchant want to override configuration from Snap preferences by setting static color theme in this SDK.

Merchant needs to provide 3 colors:

- *PRIMARY*
- *PRIMARY_DARK*
- *SECONDARY*

```java
CustomColorTheme customColorTheme = new CustomColorTheme(
    HEX_COLOR_PRIMARY, 
    HEX_COLOR_PRIMARY_DARK, 
    HEX_COLOR_SECONDARY);
        
MidtransUi.getInstance().setColorTheme(customColorTheme);
```