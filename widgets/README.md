# Midtrans Payment Widget

This is different from Midtrans core and UI SDK. Instead of using single UI flow SDK that can support many payment methods, user can select the widget they want to use to be fit into their UI.

This widget is a custom Android view that supports payment using Midtrans payment gateway.

## Installation

Just add this line into your `build.gradle` dependencies. 

```Groovy
// Sandbox
compile 'com.midtrans:widget:1.0.0-SANDBOX'

// Production
compile 'com.midtrans:widget:1.0.0'
```

**Tips** : If you use different product flavor for sandbox/development and production, use both with different compile strategy.

```Groovy
developmentCompile 'com.midtrans:widget:1.0.0-SANDBOX'
productionCompile 'com.midtrans:widget:1.0.0'
```

## Implementation

For now there is only one widget that can be used.

### Credit Card Widget


#### Credit Card Form Initialisation

Implementation in layout.xml

```xml
<com.midtrans.widgets.CreditCardForm
	android:id="@+id/credit_card_form"
	android:layout_width="match_parent"
	android:layout_height="wrap_content"
	app:mtcc_show_pay="false"
	app:mtcc_client_key="@string/vt_client_key"
	app:mtcc_merchant_url="@string/merchant_url" />
```

*Note*: 

- `vtcc_show_pay` : flag to show payment button provided on widget or not.
- `vtcc_client_key` : Midtrans payment client key required to enable the payment.
- `vtcc_merchant_url` : Merchant server base URL for charging the payment. Merchant server must use `SNAP`. Implementation follow [this link](https://github.com/veritrans/?utf8=%E2%9C%93&query=snap).

###### Java implementation.

to implement the widget, you need to intiatate it in your code like below. if you want to enable `two click`, you just need to call  `setEnableTwoClick(true)` method.

```Java
CreditCardForm creditCardForm = (CreditCardForm) findViewById(R.id.credit_card_form);
// Set client key (string)
creditCardForm.setMidtransClientKey(VT_CLIENT_KEY);
// Set merchant URL (string)
creditCardForm.setMerchantUrl(MERCHANT_URL);
//enable twoclick feature
creditCardForm.setEnableTwoClick(true);

```

## Payment
*Note*: You must complete previous part before proceed to start the payment.

First, you need to define the transaction details.

```Java
		// Create new Transaction Request
        TransactionRequest transactionRequest = new TransactionRequest(TRANSACTION_ID, TRANSACTION_AMOUNT);
        //define customer detail (mandatory)
        CustomerDetails mCustomerDetails = new CustomerDetails();
        mCustomerDetails.setPhone("24234234234");
        mCustomerDetails.setFirstName("samle full name");
        mCustomerDetails.setEmail("mail@mail.com");

        ShippingAddress shippingAddress = new ShippingAddress();
        shippingAddress.setAddress("jalan kalangan");
        shippingAddress.setCity("yogyakarta");
        shippingAddress.setCountryCode("IDN");
        shippingAddress.setPostalCode("72168");
        shippingAddress.setFirstName(mCustomerDetails.getFirstName());
        shippingAddress.setPhone(mCustomerDetails.getPhone());
        mCustomerDetails.setShippingAddress(shippingAddress);

        BillingAddress billingAddress = new BillingAddress();
        billingAddress.setAddress("jalan kalangan");
        billingAddress.setCity("yogyakarta");
        billingAddress.setCountryCode("IDN");
        billingAddress.setPostalCode("72168");
        billingAddress.setFirstName(mCustomerDetails.getFirstName());
        billingAddress.setPhone(mCustomerDetails.getPhone());
        mCustomerDetails.setBillingAddress(billingAddress);
        transactionRequestNew.setCustomerDetails(mCustomerDetails);

        // Define item details
        ItemDetails itemDetails = new ItemDetails(ITEM1_ID, ITEM1_PRICE, ITEM1_AMOUNT, ITEM1_NAME);
        ItemDetails itemDetails1 = new ItemDetails(ITEM2_ID, ITEM2_PRICE, ITEM2_AMOUNT, ITEM2_NAME);
        ItemDetails itemDetails2 = new ItemDetails(ITEM3_ID, ITEM3_PRICE, ITEM3_AMOUNT, ITEM3_NAME);

        // Add item details into item detail list.
        ArrayList<ItemDetails> itemDetailsArrayList = new ArrayList<>();
        itemDetailsArrayList.add(itemDetails);
        itemDetailsArrayList.add(itemDetails1);
        itemDetailsArrayList.add(itemDetails2);
        transactionRequest(itemDetailsArrayList);

		// Set Credit Card payment details
        CreditCard creditCard = new CreditCard();
        // Set to true if you want to activate 3D secure authorization
        creditCard.setSecure(true);
        transactionRequest(creditCard);
```


```Java
if (creditCardForm.checkCardValidity()) {
                    creditCardForm.pay(transactionRequest, new CreditCardForm.TransactionCallback() {
                        @Override
                        public void onSucceed(TransactionResponse response) {
                            // Handle payment success
                        }

                        @Override
                        public void onFailed(Throwable throwable) {
                            // Handle payment failure
                        }
                    });
                }
```

### Register Card Widget

#### Credit Card Form Initialisation

Implementation in layout.xml

```xml
<com.midtrans.widgets.CreditCardRegisterForm
	android:id="@+id/credit_card_form"
	android:layout_width="match_parent"
	android:layout_height="wrap_content"
	app:mtcc_show_pay="false"
	app:mtcc_client_key="@string/vt_client_key"
	app:mtcc_merchant_url="@string/merchant_url"
	app:mtcc_user_id="@string/user_id/>
```

*Note*:

- `vtcc_show_pay` : flag to show payment button provided on widget or not.
- `vtcc_client_key` : Midtrans payment client key required to enable the payment.
- `vtcc_merchant_url` : Merchant server base URL for charging the payment. Merchant server must use `SNAP`. Implementation follow [this link](https://github.com/veritrans/?utf8=%E2%9C%93&query=snap).
- `vtcc_user_id` : User specific identifier required to save the card specific to user.

Java implementation.

```Java
CreditCardRegisterForm creditCardForm = (CreditCardRegisterForm) findViewById(R.id.credit_card_form);
// Set client key (string)
creditCardForm.setMidtransClientKey(VT_CLIENT_KEY);
// Set merchant URL (string)
creditCardForm.setMerchantUrl(MERCHANT_URL);
// Set user ID (string)
creditCardForm.setUserId(USER_ID);
```

#### Registration

```Java
if (creditCardForm.checkCardValidity()) {
                    creditCardForm.register(new CreditCardRegisterForm.WidgetSaveCardCallback() {
                        @Override
                        public void onSucceed(SaveCardResponse transactionResponse) {
                            // Handle registration success
                        }

                        @Override
                        public void onFailed(Throwable throwable) {
                            // Handle registration failure
                        }
                    });
                }
```
