# Midtrans Corekit SDK

Midtrans Corekit SDK is set of payment utilities via Midtrans.


## How to install

Add this dependencies on your gradle script.

```groovy
compile 'com.midtrans.sdk:core:2.0.0'
```

## SDK initialisation

Initialise SDK instance in your application or main activity class.

```java
new MidtransCore.Builder()
                .enableLog(true) // To enable log
                .setEnvironment(Environment.SANDBOX) // Set environment
                .setClientKey(CLIENT_KEY) // Set client key
                .build();
```

To get SDK instance use this.

```java
MidtransCore midtransCore = MidtransCore.getInstance();
```

## Payment Flow

### Checkout Alternatives

#### 1. Provide checkout token

Basically you can provide your own checkout token with your own implementation.
Please see [Snap backend integration](https://snap-docs.midtrans.com/#backend-integration) to get the checkout token.

#### 2. Integrated Checkout

Another alternative was to use integrated checkout with provided function in this SDK.

##### Provide Merchant Endpoint

You just need an `endpoint` to be hit by this SDK.

This is the endpoint specification.

```
Example URL: 
- https://example-merchant.com/checkout
- https://example-merchant.com/charge
Method: POST
Headers:
- Content-Type : application/json
- Accept : application/json
```

Basically the endpoint purpose was forwarding incoming request to these Snap endpoints and adding base64 encoded server key header into the request based on environment:

- Sandbox : https://app.sandbox.midtrans.com/snap/v1/transactions
- Production : https://app.midtrans.com/snap/v1/transactions

Added header 
`Authorization: Basic Base64("ServerKey:")`

##### Create Checkout Request

Basically checkout function in SDK is wrapper of JSON object defined in Snap backend integration.

###### Minimal Checkout Request

Minimal data for creating checkout request were *order id* and *gross amount*.

```java
// Create checkout request
CheckoutTokenRequest checkoutTokenRequest = CheckoutTokenRequest.newMinimalCheckout(orderId, grossAmount);

// Do the checkout
MidtransCore.getInstance().checkout(MyApp.CHECKOUT_URL, initialiseCheckoutTokenRequest(), new MidtransCoreCallback<CheckoutTokenResponse>() {
                    @Override
                    public void onSuccess(CheckoutTokenResponse object) {
                        // Get checkout token
                        String checkoutToken = object.token;
                    }

                    @Override
                    public void onFailure(CheckoutTokenResponse object) {
                        // Handle failure here
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        // Handle error here
                    }
                });
            }
        });
```

###### Complete Checkout Request

Complete checkout request includes order details (required) and all optional fields.
Just set to `null` if you don't want to set the optional fields.

```java
CheckoutTokenRequest checkoutTokenRequest = CheckoutTokenRequest.newCompleteCheckout(
                userId,
                creditCardOptions,
                itemDetailsList,
                customerDetails,
                checkoutOrderDetails,
                expiry,
                customObject
        );
```

Note:

- `userId` : Used on two clicks transaction to save the card mapped to specific user.
- `creditCardOptions` : Used to customize credit card payment.
- `itemDetailsList` : Used to define item details from customer.
- `customerDetails` : Used to define customer details.
- `checkoutOrderDetails` : Used to define order details.
- `expiry` : Used to define custom expiry for transaction.
- `customObject` : Used to define custom key-value pairs in transaction.

**Credit Card Payment Options**

```java
// Create credit card Options
CreditCard creditCardOptions = new CreditCard();
// Select acquiring bank using BankType.BANK_NAME
creditCardOptions.setBank(BankType.BCA);
// Select acquiring channel using Channel.CHANNEL_NAME
// Only use Channel.MIGS on BCA, BRI or MAYBANK
creditCardOptions.setChannel(Channel.MIGS);
// Set 3D secure mode of the transaction
creditCardOptions.setSecure(true);
// Set save card mode of the transaction
creditCardOptions.setSaveCard(true);
```

**Item Details**

```java
// Create list of item details
List<ItemDetails> itemDetailsList = new ArrayList<>();
itemDetailsList.add(new ItemDetails(item1Id, item1Price, item1Quantity, item1Name));
itemDetailsList.add(new ItemDetails(item2Id, item2Price, item2Quantity, item2Name));
```

**Customer Details**

```java
// Create customer details
CustomerDetails customerDetails = new CustomerDetails(
    firstName,
    lastName,
    email,
    phone,
    // Set Shipping address details (Optional/Nullable)
    new Address(
        firstName,
        lastName,
        address,
        city,
        postalCode,
        phone,
        countryCode),
    // Set Billing address details (Optional/Nullable)
    new Address(
        firstName,
        lastName,
        address,
        city,
        postalCode,
        phone,
        countryCode
        ));
```

**Order Details (required)**

```java
// Create checkout order details
CheckoutOrderDetails checkoutOrderDetails = new CheckoutOrderDetails(orderId, grossAmount);
```

**Expiry**
 
```java
// Set custom expiry
Expiry expiry = new Expiry(
	startTime, // Must be ISO-8601 time formatted
	Expiry.UNIT,
	duration);

//Example for getting current time in ISO-8601 
Strint startTime = Utilities.getFormattedTime(System.currentTimeMillis());
```

**Custom Key-Value Pairs**

```java
// Set custom key-value pairs
Map<String, String> customObject = new HashMap<>();
customObject.put("key1", "value1");
customObject.put("key2", "value2");
```

##### Start Checkout

```java
MidtransCore.getInstance().checkout(CHECKOUT_URL, checkoutRequest, new MidtransCoreCallback<CheckoutTokenResponse>() {
                    @Override
                    public void onSuccess(CheckoutTokenResponse object) {
							 // Get checkout token
                        String checkoutToken = object.token;
                    }

                    @Override
                    public void onFailure(CheckoutTokenResponse object) {
                        // Handle failure
                    }

                    @Override
                    public void onError(Throwable throwable) {
                        // Handle failure
                    }
                });
```

### Start Payment using Credit Card

Credit card payment process has 2 steps:

1. Get card token using credit card details
2. Charge payment

#### Get Card Token

##### Create card token request.

###### Normal Transaction (Without 3D Secure)

```java
CardTokenRequest request = CardTokenRequest.newNormalCard(
                        cardNumber,
                        cardCvv,
                        cardExpiryMonth,
                        cardExpiryYear);
```

###### 3DS Transaction 

```java
CardTokenRequest request = CardTokenRequest.newNormalSecureCard(
                        cardNumber,
                        cardCvv,
                        cardExpiryMonth,
                        cardExpiryYear,
                        true,
                        grossAmount);
```

###### Two Click Transactions

```java
CardTokenRequest request = CardTokenRequest.newNormalTwoClicksCard(
                        savedTokenId,
                        cardCvv,
                        true,
                        grossAmount);
```

##### Start get token request

```java
MidtransCore.getInstance().getCardToken(CARD_TOKEN_REQUEST, new MidtransCoreCallback<CardTokenResponse>() {
                    @Override
                    public void onSuccess(CardTokenResponse response) {
                        // Get card token. This will be used to charge the transaction
                        String cardToken = response.tokenId;
                        
                        // Get redirect URL if using 3D secure enabled. 
                        // Show this into your webview to handle the authorization
                        // Token is valid only when authorization was completed
                        String redirectUrl = response.redirectUrl;
                        if (redirectUrl != null && !TextUtils.isEmpty(redirectUrl)) {
                            // show redirectUrl in WebView to start 3DS authorization
                            // after 3DS authorization was completed, please start charging
                        } else {
                            // Otherwise just start charging
                        }
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        // Handle failure here
                    }
                });
```

#### Charge payment 

##### Create your payment params and customer details.

```java
// Setup payment params
CreditCardPaymentParams paymentParams = CreditCardPaymentParams().newBasicPaymentParams(cardToken);
        
// Setup customer details
SnapCustomerDetails customerDetails = new SnapCustomerDetails("Raka Westu Mogandhi", "rakamogandhi@hotmail.com", "082140518011");
```

##### Start the charging request

```java
// CHECKOUT_TOKEN was obtained at checkout process 
// CC_PAYMENT_REQUEST was created on previous step
MidtransCore.getInstance().paymentUsingCreditCard(CHECKOUT_TOKEN, paymentParams, customerDetails, new MidtransCoreCallback<CreditCardPaymentResponse>() {
                        @Override
                        public void onSuccess(CreditCardPaymentResponse object) {
                            // Handle payment success here
                        }

                        @Override
                        public void onFailure(Throwable throwable) {
                            // Handle payment failure here
                        }
                    });
```

### Start Payment using Bank Transfer

Requirements:

- Customer Details (Optional)
    - Name
    - Email
    - Phone

Create payment request object
 
```java
SnapCustomerDetails customerDetails = new SnapCustomerDetails(Name, Email, Phone);
```
#### BCA

##### Charge payment

```java
MidtransCore.getInstance().paymentUsingBcaBankTransfer(checkoutToken, new MidtransCoreCallback<BcaBankTransferPaymentResponse>() {
                    @Override
                    public void onSuccess(BcaBankTransferPaymentResponse object) {
                        // Handle success payment
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        // Handle failed payment
                    }
                }); 
```

##### Charge payment with customer details

```java
MidtransCore.getInstance().paymentUsingBcaBankTransfer(checkoutToken, customerDetails, new MidtransCoreCallback<BcaBankTransferPaymentResponse>() {
                    @Override
                    public void onSuccess(BcaBankTransferPaymentResponse object) {
                        // Handle success payment
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        // Handle failed payment
                    }
                }); 
```

#### Permata

##### Charge payment

```java
MidtransCore.getInstance().paymentUsingPermataBankTransfer(checkoutToken, new MidtransCoreCallback<PermataBankTransferPaymentResponse>() {
                    @Override
                    public void onSuccess(PermataBankTransferPaymentResponse object) {
                        // Handle success payment
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        // Handle failed payment
                    }
                }); 
```

##### Charge payment with customer details

```java
MidtransCore.getInstance().paymentUsingPermataBankTransfer(checkoutToken, customerDetails, new MidtransCoreCallback<PermataBankTransferPaymentResponse>() {
                    @Override
                    public void onSuccess(PermataBankTransferPaymentResponse object) {
                        // Handle success payment
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        // Handle failed payment
                    }
                }); 
```

#### Mandiri Bill

##### Charge payment

```java
MidtransCore.getInstance().paymentUsingMandiriBankTransfer(checkoutToken, new MidtransCoreCallback<MandiriBankTransferPaymentResponse>() {
                    @Override
                    public void onSuccess(MandiriBankTransferPaymentResponse object) {
                        // Handle success payment
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        // Handle failed payment
                    }
                }); 
```

##### Charge payment with customer details

```java
MidtransCore.getInstance().paymentUsingMandiriBankTransfer(checkoutToken, customerDetails, new MidtransCoreCallback<MandiriBankTransferPaymentResponse>() {
                    @Override
                    public void onSuccess(MandiriBankTransferPaymentResponse object) {
                        // Handle success payment
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        // Handle failed payment
                    }
                }); 
```

#### Other Bank

##### Charge payment

```java
MidtransCore.getInstance().paymentUsingOtherBankTransfer(checkoutToken, new MidtransCoreCallback<OtherBankTransferPaymentResponse>() {
                    @Override
                    public void onSuccess(OtherBankTransferPaymentResponse object) {
                        // Handle success payment
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        // Handle failed payment
                    }
                }); 
```

##### Charge payment with customer details

```java
MidtransCore.getInstance().paymentUsingOtherBankTransfer(checkoutToken, customerDetails, new MidtransCoreCallback<OtherBankTransferPaymentResponse>() {
                    @Override
                    public void onSuccess(OtherBankTransferPaymentResponse object) {
                        // Handle success payment
                    }

                    @Override
                    public void onFailure(Throwable throwable) {
                        // Handle failed payment
                    }
                }); 
```

### Start Payment Using BCA KlikPay

```java
MidtransCore.getInstance().paymentUsingBcaKlikpay(checkoutToken, new MidtransCoreCallback<BcaKlikpayPaymentResponse>() {
                        @Override
                        public void onSuccess(BcaKlikpayPaymentResponse object) {
                            // Get redirect URL
                            String bcaKlikpayUrl = object.redirectUrl;
                            // Show bcaKlikpayUrl into WebView to complete the transaction
                        }

                        @Override
                        public void onFailure(BcaKlikpayPaymentResponse object) {
                            // Handle failure here
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            // Handle error here
                        }
                    });
```

### Start Payment Using KlikBCA

```
MidtransCore.getInstance().paymentUsingKlikBca(checkoutToken, klikBcaUserId, new MidtransCoreCallback<KlikBcaPaymentResponse>() {
                        @Override
                        public void onSuccess(KlikBcaPaymentResponse object) {
                            // Handle transaction here
                            // User must complete the transaction in KlikBCA website using provided klikBcaUserId
                        }

                        @Override
                        public void onFailure(KlikBcaPaymentResponse object) {
                            // Handle failure
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            // Handle error
                        }
                    });
```

### Start Payment Using e-Pay BRI

```java
MidtransCore.getInstance().paymentUsingEpayBri(checkoutToken, new MidtransCoreCallback<EpayBriPaymentResponse>() {
                        @Override
                        public void onSuccess(EpayBriPaymentResponse object) {
                            // Handle success here
                            // Get redirectUrl
                            String redirectUrl = object.redirectUrl;
                            // Show redirect URl into WebView to complete the transaction
                        }

                        @Override
                        public void onFailure(EpayBriPaymentResponse object) {
                            // Handle failure here
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            // Handle error here
                        }
                    });
```

### Start Payment Using CIMB Clicks

```java
MidtransCore.getInstance().paymentUsingCimbClicks(checkoutToken.getText().toString(), new MidtransCoreCallback<CimbClicksPaymentResponse>() {
                        @Override
                        public void onSuccess(CimbClicksPaymentResponse object) {
                            // Handle success here
                            // Get redirectUrl
                            String redirectUrl = object.redirectUrl;
                            // Show redirect URl into WebView to complete the transaction
                        }

                        @Override
                        public void onFailure(CimbClicksPaymentResponse object) {
                            // Handle failure here
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            // Handle error here
                        }
                    });
```

### Start Payment Using Mandiri E-Cash

```java
MidtransCore.getInstance().paymentUsingMandiriECash(checkoutToken, new MidtransCoreCallback<MandiriECashPaymentResponse>() {
                        @Override
                        public void onSuccess(MandiriECashPaymentResponse object) {
                            // Handle success here
                            // Get redirectUrl
                            String redirectUrl = object.redirectUrl;
                            // Show redirect URl into WebView to complete the transaction
                        }

                        @Override
                        public void onFailure(MandiriECashPaymentResponse object) {
                            // Handle failure here
                        }

                        @Override
                        public void onError(Throwable throwable) {
                            // Handle error here
                        }
                    });
```

### Start Payment Using Mandiri Clickpay

```java
// Build payment params
MandiriClickpayPaymentParams paymentParams = new MandiriClickpayPaymentParams(
    // Card number
    cardNumber,
    // 5 digits Random number
    String.valueOf(Utilities.generateRandomNumber()),
    // Mandiri Token Response
    tokenResponse
);

// Start payment
MidtransCore.getInstance().paymentUsingMandiriClickpay(checkoutToken, paymentParams, new MidtransCoreCallback<MandiriClickpayPaymentResponse>() {
    @Override
    public void onSuccess(MandiriClickpayPaymentResponse object) {
        // Handle success
    }

    @Override
    public void onFailure(MandiriClickpayPaymentResponse object) {
        // Handle failure
    }

    @Override
    public void onError(Throwable throwable) {
        // Handle error
    }
});
```

### Start Payment Using Telkomsel Cash

```java
MidtransCore.getInstance().paymentUsingTelkomselCash(checkoutToken, telkomselCashToken, new MidtransCoreCallback<TelkomselCashPaymentResponse>() {
    @Override
    public void onSuccess(TelkomselCashPaymentResponse object) {
        // Handle success
    }

    @Override
    public void onFailure(TelkomselCashPaymentResponse object) {
        // Handle failure
    }

    @Override
    public void onError(Throwable throwable) {
        // Handle error
    }
});
```

### Start Payment using Indosat Dompetku

```java
MidtransCore.getInstance().paymentUsingIndosatDompetku(checkoutToken, phoneNumber, new MidtransCoreCallback<IndosatDompetkuPaymentResponse>() {
    @Override
    public void onSuccess(IndosatDompetkuPaymentResponse object) {
        // Handle success
    }

    @Override
    public void onFailure(IndosatDompetkuPaymentResponse object) {
        // Handle failure
    }

    @Override
    public void onError(Throwable throwable) {
        // Handle error
    }
});
```

### Start Payment using XL Tunai

Payment using XL Tunai is similiar to Virtual Account payment.

User needs to complete the payment. SDK will provide payment id and merchant id in order to inform user about payment completion.

```java
MidtransCore.getInstance().paymentUsingXlTunai(checkoutToken, new MidtransCoreCallback<XlTunaiPaymentResponse>() {
    @Override
    public void onSuccess(XlTunaiPaymentResponse object) {
        // Get XL Tunai Order/Payment ID
        String xlTunaiPaymentId = object.xlTunaiOrderId;
        // Get XL Tunai Merchant ID
        String xlTunaiMerchantId = object.xlTunaiMerchantId;
    }

    @Override
    public void onFailure(XlTunaiPaymentResponse object) {
        // Handle failure
    }

    @Override
    public void onError(Throwable throwable) {
        // Handle error
    }
});
```

### Start Payment using Indomaret

Payment using Indomaret will be completed after user pay specific amount into payment code generated in this SDK to any Indomaret Store.

```java
MidtransCore.getInstance().paymentUsingIndomaret(checkoutToken, new MidtransCoreCallback<IndomaretPaymentResponse>() {
    @Override
    public void onSuccess(IndomaretPaymentResponse object) {
        // Get payment code
        String paymentCode = object.paymentCode;
    }

    @Override
    public void onFailure(IndomaretPaymentResponse object) {
        // Handle failure
    }

    @Override
    public void onError(Throwable throwable) {
        // Handle error
    }
});
```

### Start Payment using Kioson

```java
MidtransCore.getInstance().paymentUsingKioson(checkoutToken.getText().toString(), new MidtransCoreCallback<KiosonPaymentResponse>() {
    @Override
    public void onSuccess(KiosonPaymentResponse object) {
        // Get payment code
        String paymentCode = object.paymentCode;
    }

    @Override
    public void onFailure(KiosonPaymentResponse object) {
        // Handle failure
    }

    @Override
    public void onError(Throwable throwable) {
        // Handle error             
    }
});
```

### Start Payment using Gift Card (GCI)

```java
MidtransCore.getInstance().paymentUsingGiftCard(checkoutToken, giftCardNumber, giftCardPin, new MidtransCoreCallback<GiftCardPaymentResponse>() {
    @Override
    public void onSuccess(GiftCardPaymentResponse object) {
        // Get approval code
        String approvalCode = object.approvalCode
    }

    @Override
    public void onFailure(GiftCardPaymentResponse object) {
        // Handle failure
    }

    @Override
    public void onError(Throwable throwable) {
        // Handle error                 
    }
});
```