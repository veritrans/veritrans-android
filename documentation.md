# **Veritrans SDK**
### Description 
Veritrans SDK is an android library project which supports to perform transaction using variouse payment methods supported by veritrans payment gateway. 

Veritrans SDK supports following payment methods -
1) Credit/Debit - helps to perform transaction using credit/debit card with 1 and 2 click support.
2) Mandiri ClickPay - 
3) CIMB Clicks -
4) ePay BRI  - 
5) BBM Money - 
6) Indosat Dompetku-
7) Mandiri e-Cash  - 
8) Bank Transfer   - suport payment using Permata Virtual Account. 
9) Mandiri Bill Payment - 
10) Indomaret -
11) Offers - supports offers like BIN, Discount etc.

Instantiate sdk after application finishes launching processes.

Following are  configurable parameters of sdk that can be used while performing transaction -
1) Server Endpoint- url of server to which transaction data will be sent.
2) Transaction details - contains payment information like amount, order Id, payment method etc.


### Library Integration
To use Veritrans SDK  in your android application perform following steps.

##### 1 Install Veritrans SDK library

**Step 1 -**  Add the library as a gradle dependency:
       
you can simply declare it as dependency in  **build.gradle** file as follow
```
        dependencies {
         compile project(':veritranssdk')
        }

      - add name of the library in **setting.gradle**
      include ':app', ':veritranssdk'
```

**Step 2 -** Add internet permissions to  **_AndroidManifest.xml_**  
  These permissions are required to allow the application to send
  events.
  ```
  <uses-permission
    android:name="android.permission.INTERNET" />

  <uses-permission
    android:name="android.permission.ACCESS_NETWORK_STATE" />
    
    <!--GCM permissions-->
    <uses-permission android:name="com.google.android.c2dm.permission.RECEIVE" />
    <uses-permission android:name="android.permission.WAKE_LOCK" />
    <uses-permission android:name="id.co.veritrans.sdk.example.permission.C2D_MESSAGE"/>

 ```
 
**Note** - To add this library project in eclipse follow the instructions given on following link 
 http://developer.android.com/tools/projects/projects-eclipse.html

 That's it! now you are ready to use Veritrans Sdk in your application.

# 2 Initializing the library
Once you've setup your build system or IDE to use the Veritrans sdk library. Veritrans SDK requires a merchant key and server key, to get these keys register to veritrans server at https://my.veritrans.co.id/register . 

Now initialize sdk
```
// sdk initialization process
        VeritransBuilder veritransBuilder = new
                VeritransBuilder(context, VT_CLIENT_KEY);
        veritransBuilder.enableLog(true);   // enable logs for debugging purpose.
    VeritransSDK mVeritransSDK = veritransBuilder.buildSDK();

/*
where -
VeritransBuilder - builder class which helps to create sdk instance. 
Context - application context.
VT_CLIENT_KEY - merchant/client key received from veritrans server.
*/
```

Instance of Veritrans SDK is singleton. In order to perform transactions set appropriate details to it using variouse setter methods, finally invoke payment method to perform transaction. You can perform only one transaction at a time.

You can also access already created sdk instance using following static method -
```
VeritransSDK.getVeritransSDK();
```

# 3 Set Transaction Details
Set required transaction information to sdk instance.
TransactionRequest contains following information -
- Order Id - _unique order id to identify this transaction_.
- Amount - _amount to charge_.
- Customer details - _holds an information about customer like name , email and phone_.
- Item details - _ holds an information about purchased item, like item id, price etc_.
- Billing Address - _holds an address information like city , country, zip code etc_.
- Shipping Address - _holds an address information like city , country, zip code etc_.
- Bill information - _contains custom details in key-value that you want to display on bill print. 

All of the above parameters are not nesseccessary to set, It depends on which type of payment method you want to execute. To get more information about required paramter see http://docs.veritrans.co.id/en/api/methods.html#Charge .
Sdk will throw error message in case you missed some information right before starting an payment flow.

Create instance of **TransactionRequest** class. 
```
TransactionRequest transactionRequest =
                new TransactionRequest(activity, order id, amount);

/*
where -
activity - instance of current activity class. 
order id - unique order id for this transaction.
amount - amount to charge.
*/
```
 

## Common request information -
Customer details , item details , billing and shipping  address these are common paramteres which requires in every type of transaction.  

1) Set customer details -

**CustomerDetails** class holds information about customer. To set information about customer in TransactionRequest use following code -

```
CustomerDetails customer = new CustomerDetails(String firstName, String lastName, String email, String phone);
transactionRequest.setCustomerDetails(customer);
```



2) Set Item details -

**ItemDetails** class holds information about item purchased by user. TransactionRequest takes an array list of item details. To set this in TransactionRequest use following code -

```
ItemDetails itemDetails1 = new CustomerDetailsItemDetails(String id, double price, double quantity, String name);
ItemDetails itemDetails2 = new CustomerDetailsItemDetails(String id, double price, double quantity, String name);
.
.
//todo create array list and add above item details in it and then set it to transaction request. 

transactionRequest.setItemDetails(ArrayList<ItemDetails>);
```


3) Set Billing Address -
**BillingAddress** class holds information about billing. TransactionRequest takes an array list of billing details. To set this in TransactionRequest use following code - 

```

BillingAddress billingAddress1 = new BillingAddress(String firstName, String lastName, String address, String city, String postalCode, String phone, String countryCode);

BillingAddress billingAddress2 =new BillingAddress(String firstName, String lastName, String address, String city, String postalCode, String phone, String countryCode);
.
.
//todo create array list and add above billing details in it and then set it to transaction request. 

transactionRequest.setItemDetails(ArrayList<BillingAddress>);

```



4) Set Shipping Address -
**ShippingAddress** class holds information about billing. TransactionRequest takes an array list of shipping details. To set this in TransactionRequest use following code -

```

ShippingAddress shippingAddress1 = new ShippingAddress(String firstName, String lastName, String address, String city, String postalCode, String phone, String countryCode);

ShippingAddress shippingAddress2 =new ShippingAddress(String firstName, String lastName, String address, String city, String postalCode, String phone, String countryCode);
.
.
//todo create array list and add above shipping details in it and then set it to transaction request. 

transactionRequest.setItemDetails(ArrayList<ShippingAddress>);

```


# Payment Flow
Veritrans SDK has 2 types of payment flows first is using default ui proived by sdk and second is using core flow.
1) Payment flow  using default UI -
    here in this flow just set transaction request information to sdk and start payment flow using following code-
    ```
    if (transactionRequest != null && mVeritransSDK != null) {

                    // create transaction request information as shown above.
                    //set transaction information to sdk
                    mVeritransSDK.setTransactionRequest(transactionRequest);

                    // start ui flow
                    mVeritransSDK.startPaymentUiFlow();

                }
        
    ```

This will start Payment flow if all information is valid.


2) Payment flow  using core structure -
    
    In this flow we are assuming that you have created ui to take required information from user to execute transaction.  
    To perform transaction using core, follow the steps given below:  
     I) Set transaction request information to sdk.  
     II) Implement  TransactionCallback  to handle payment response.  
     III) Call the implemented method of the desired payment mode.
     
     **Note -** don't call  mVeritransSDK.startPaymentUiFlow(); for core.

## Mandiri Bill Payment
 
 To execute payment process using mandiri bill payment method  


2.[User detail screen](#user_detail_screen)  
- _User details like name and contact info_

3.[User address screen](#user_address_screen)   
- _User billing and shipping address details_  

4.[Select Payment Method](select_payment_method)
- _Choose the payment option from list_  

5.[Credit card Flow](credit_card_flow)
- _Saved cards with no saved cards_
- _Add new card_
- _Payment status screen_
- _Saved card screen with cards_
- _One click flow_
- _Two click flow_

6.[Bank transfer Flow](bank_transfer_Flow)  
- _Bank transfer main screen_
- _Instruction screen_
- _Payment detail screen_
- _Payment status screen_  

7.[Mandiri bill payment](mandiri_bill_payment)  
- _Mandiri bill payment main screen_
- _Instruction screen_
- _Payment detail screen_
- _Payment status screen_  



## Payment-options

[image insert]  

**This screen is created for testing purpose.**  
On this screen we can set some options required for testing purpose.  

* Set amount for payment (default is 100)
* There are **3 types** of flow for **card payments**.  
**1. _One click_**  
**2. _Two click_**  
**3. _Normal_**  
We will go in detail about these flows ahead.

* For **normal flow** we can do secure and Insecure way.  

    	Secure : With 3d secure validation.
	    Insecure : Without 3d secure validation.

* **One click** and **two click** use **secure** flow only.  

* **Delete cards** - Tester can clear all the credit cards locally saved using this option.
* You can customize List of payment option using given checkbox.
* On click of payment button new **_order id_** will be created and take user to user detail screen if user details had already provided then it will take user to select payment option screen.  

## User detail screen
[image insert]   
On this screen we are taking user details like user full name, email and phone no from user.
which used in future payment process, saving user detail in **_UserDetail_** class maintained in models folder of VeritransSdk.

## User address screen
[image insert]  
Here we are taking user billing address and shipping address.
User can have different billing and shipping address or same.   
If user have different shipping and billing address, user have to unchecked the bottom checkbox.
User addresses are maintained in **_UserAddress class_**. This class is entity of **_UserDetail class_**.

To **fetch user details** from file system refer following code.


         StorageDataHandler storageDataHandler = new StorageDataHandler();
         UserDetail userDetail = null;
        try {
           userDetail = (UserDetail) storageDataHandler.readObject(getActivity(), Constants
                   .USER_DETAILS);
        
        } catch (ClassNotFoundException e) {
           e.printStackTrace();
        } catch (IOException e) {
           e.printStackTrace();
        }

To **write user details** to file system refer following code.

```  

    storageDataHandler.writeObject(getActivity(), Constants.USER_DETAILS, userDetail);

```

Here ***Constant.USER_DETAIL*** is file name stored locally. UserDetail is serialized.   

Refer UserDetailFragment class for further implementation of flow.

## Select payment option  
[insert image]  
This is first screen of main flow. We are showing list of available payment methods. ***PaymentMehodActivity*** class holds the code for list.

## Credit card flow
[image insert]
### Add new card   
[image insert]  

We can add new card here  
List of actions as follows
- Take card details from user
- Do validation of card detail  
- On successful validation adding bank name and card type from list of BNI available in asset folder of SDK.
- Once we got all the details we are making api call for token

***VeritransSDK class*** contains all important methods required for payment transaction.
Code to get VeritransSDK object  

    VeritransSDK veritransSDK = VeritransSDK.getVeritransSDK();  

**Mehod to get token**

    veritransSDK.getToken(activity, cardTokenRequest, tokenCallBack);  

***CardTokenRequest*** class contains card details required for getting token.  

## TokenCallBack Interface
It contains two methods onSuccess and onFailure -  


    @Override  
    public void onSuccess(TokenDetailsResponse tokenDetailsResponse) {  
        // get called in case of success
        //write your code here to save this token
    }    
    
    @Override  
    public void onFailure(String errorMessage, TokenDetailsResponse tokenDetailsResponse) {  
        // handle error here
    }
    On success of get token api call, it will return token id and other parameters in TokenDetailsResponse. 

If secure flow is available then we will get redirect_url which will take user to web page for 3d secure validation.
After successful validation we are making charge api call.

For doing charge api call we have to call following function from VeritransSDK.

     veritransSDK.paymentUsingCard(activity, cardTransfer, transactionCallback);

In above code we have to supply ** _CardTransfer_ ** class object, and implemented interface TransactionCallback which have functions onSuccess and onFailure.

    @Override
    public void onSuccess(TransactionResponse cardPaymentResponse) {
    	//write code after success
    }  
    @Override
    public void onFailure(String errorMessage, TransactionResponse transactionResponse) {    
    	//write code after failure
    }

    
after successful transaction card information will be get saved if user permits. In TransactionResponse we get ***saved_token_id*** which is used in future transaction.
## Save Card Details
To save card details use code given below:

    try {
      storageDataHandler.writeObject(this, Constants.USERS_SAVED_CARD, creditCards)
    } catch (IOException e) {
    	e.printStackTrace();
    }  

## Payment status screen  
[insert image]  

To show status of payment we are calling ***PaymentTransactionStatusFragment*** and passing ***TransactionResponse*** object which we got in onSuccess or onFailure method.  
At PaymentTransactionStatusFragment we are showing transaction related information or failure reasons and Retry option.  

## Saved card screen with saved cards  
[insert image]  

When user completes successful transaction with permission to save cards we save credit card details(Not cvv).  
This saved cards are shown here. User can use this cards for future transactions.

## One click flow  
* After initial transaction we use  ***_saved_token_id*** for future transaction as follows 

```

            {
                // Required
                "payment_type": "credit_card",
                "credit_card": {
                    // New Token Generated from One Click Clicks Initial Charge Response saved_token_id
                    "token_id": "saved_token_id",
                    "bank": "bni"
                },
                // Required
                "transaction_details": {
                    "order_id": "A87551",
                    "gross_amount": 145000
                }
            }

```

* Response for api call is same as normal transaction.
* We can use this saved_token_id for all future transaction call.

## Two click flow  
* After initial transaction we save ***saved_token_id***.
* In future transactions we take cvv for saved card from view pager.
* With the help of saved_token_id and card detail execute api call for getToken.
* Gettoken api call gives new token_id,
* If secure flow is available then we will get redirect_url which will take user to browser for 3d secure validation.
* After successfull validation we are making charge api call.

## Bank transfer flow
### 1) Using core flow
###### To perform transaction using bank transfer method follow the steps given below:
* Create the instance of veritrans library using **VeritransBuilder** class.
* Create instance of  **TransactionRequest** and add required fields like item details, 
shipping address etc.
* Then add Transaction details in previously created veritrans object using 

        mVeritransSDK.setTransactionRequest(TransactionRequest);
    
* Then execute Transaction using following method and add **TransactionCallback** to get response back.


```

  
       mVeritransSDK.paymentUsingPermataBank(MainActivity.this, new TransactionCallback() {
                            
            @Override
            public void onFailure(String errorMessage, TransactionResponse transactionResponse) {
                    Log.d(" Transaction failed ", ""+errorMessage);
                    // write your code here to take appropriate action 
                }
    
            @Override
            public void onSuccess(TransactionResponse transactionResponse) {
                    Log.d(" Transaction status ", ""+transactionResponse.getStatusMessage());
                     // write your code here to handle success.
                    }
        });

      
```

* Take appropriate action in onFailure() or  onSuccess() of TransactionCallback.
  
### 2) Using Ui Flow
###### To perform transaction using bank transfer method follow the steps given below:
* Create the instance of veritrans library using VeritransBuilder class.
* Create instance of TransactionRequest and add required fields like ItemDetail, 
   shipping address etc.
* Then add Transaction details in previously created veritrans object using 

```

        mVeritransSDK.setTransactionRequest(TransactionRequest);

```

* Then start default UI flow using 


```

       mVeritransSDK.startPaymentUiFlow();

```

It will start payment flow. 

* If you haven't filled up the user details information then it will take you to the user details    screen. After filling up all required information it will take you to the "Select Payment Method" screen.
* Select "Bank Transfer" from  avaible payments method. 
* Fill up an email id in email address field if you want to receive payment instructions on your email id, This is an optional step.
* Confirm payment by tapping of **CONFIRM PAYMENT** button.
* It will start a **charge** api call for bank transfer.
* After successfully completion of transaction, you will see an **Virtual Account Number** and its **expiry time**.
* Then to get more information about transaction tap on **COMPLETE PAYMENT AT ATM** button.  
* Finally, select **Done** to finish the payment process or select **RETRY** in case of failure.
    

## Mandiri bill payment

### 1) Using core flow
###### To perform transaction using mandiri bill payment method follow the steps given below:
* Create the instance of veritrans library using **VeritransBuilder** class.
* Create instance of **TransactionRequest** and add required fields like item details, 
   shipping address etc.
* Then add Transaction details in previously created veritrans object using 

```

        mVeritransSDK.setTransactionRequest(TransactionRequest);

```

* Then execute Transaction using following method and add **TransactionCallback** to get response back

```

        mVeritransSDK.paymentUsingMandiriBillPay(MainActivity.this, new TransactionCallback() {
   
                            
            @Override
            public void onFailure(String errorMessage, TransactionResponse transactionResponse) {
                    Log.d(" Transaction failed ", ""+errorMessage);
                    // write your code here to take appropriate action 
                }
        
                @Override
                public void onSuccess(TransactionResponse transactionResponse) {
                    Log.d(" Transaction status ", ""+transactionResponse.getStatusMessage());
                    // write your code here.
                }
        });
        
```


* Take appropriate action in onFailure() or onSuccess() of TransactionCallback.
  

### 2) Using Ui Flow
 
###### To perform transaction using mandiri bill payment method follow the steps given below:
* Create the instance of veritrans library using VeritransBuilder class.
* Create instance of TransactionRequest and add required fields like ItemDetail, 
   shipping address etc.
* Then add Transaction details in previously created  veritrans object using 


```

           mVeritransSDK.setTransactionRequest(TransactionRequest);
           
```

* Then start default UI flow using 

```

            mVeritransSDK.startPaymentUiFlow();
            //It will start payment flow. 

```

* If you haven't filled up the user details information then it will take you to the user details    screen. After filling up all required information it will take you to the "Select Payment Method" screen.
* Select "Mandiri Bill Payment" from  avaible payments method. 
* Fill up an email id in email address field if you want to receive payment instructions details on your email id, This is an optional step.
* Confirm payment by tapping of **CONFIRM PAYMENT** button.
* It will start a **charge** api call for mandiri bill payment.
* After successfully completion of transaction, you will see a **company code, bill code and validaty time**.
* Then to get more information about transaction tap on **COMPLETE PAYMENT AT ATM** button.  
* Finally, select **Done** to finish the payment process or  **RETRY** in case of failure.
