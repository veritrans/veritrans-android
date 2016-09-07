# Veritrans Payment Widget

This is different from Veritrans core and UI SDK. Instead of using single UI flow SDK that can support many payment methods, user can select the widget they want to use to be fit into their UI.

This widget is a custom Android view that supports payment using Veritrans payment gateway.

## Installation

Just add this line into your `build.gradle` dependencies. 

```Groovy
// Sandbox
compile 'id.co.veritrans:widget:1.0.0-SANDBOX'

// Production
compile 'id.co.veritrans:widget:1.0.0'
```

**Tips** : If you use different product flavor for sandbox/development and production, use both with different compile strategy.

```Groovy
developmentCompile 'id.co.veritrans:widget:1.0.0-SANDBOX'
productionCompile 'id.co.veritrans:widget:1.0.0'
```

## Implementation

For now there is only one widget that can be used.

### Credit Card Widget


#### Credit Card Form Initialisation

Implementation in layout.xml

```xml
<CreditCardForm
	android:id="@+id/credit_card_form"
	android:layout_width="match_parent"
	android:layout_height="wrap_content"
	app:vtcc_show_pay="false"
	app:vtcc_client_key="@string/vt_client_key"
	app:vtcc_merchant_url="@string/merchant_url" />
```

*Note*: 

- `vtcc_show_pay` : flag to show payment button provided on widget or not.
- `vtcc_client_key` : Veritrans payment client key required to enable the payment.
- `vtcc_merchant_url` : Merchant server base URL for charging the payment. Merchant server must use `SNAP`. Implementation follow [this link](https://github.com/veritrans/?utf8=%E2%9C%93&query=snap).

Java implementation.

```Java
CreditCardForm creditCardForm = (CreditCardForm) findViewById(R.id.credit_card_form);
// Set client key (string)
creditCardForm.setVeritransClientKey(VT_CLIENT_KEY);
// Set merchant URL (string)
creditCardForm.setMerchantUrl(MERCHANT_URL);
```

#### Payment

*Note*: You must complete previous part before proceed to start the payment.

First, you need to define the transaction details.

```Java
		// Create new Transaction Request
        TransactionRequest transactionRequest = new TransactionRequest(TRANSACTION_ID, TRANSACTION_AMOUNT);

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

Then start the payment.

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