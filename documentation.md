# **Veritrans SDK**
## Installation
## Steps to include VeritransSdk in application
- Create new application in android studio.
- Copy veritranssdk folder to application folder as sub module.
- Open **settings.gradle** file your application.
- Add veritranssdk module to it using code given below:

```
   
        include ':app',':veritranssdk'
        
```

- Open **build.gradle** file of app, add following line in it.

```

        compile project(':veritranssdk')

```

  That's it now you are ready to use VeritransSdk in your application.

## Initialization of VeritransSdk
- Register to Veritrans Sandbox/Production account. [Click ](https://my.veritrans.co.id/register) here to register.
- After successful login goto dashboard.
- Now goto **Settings** -> **Access keys**
- Get your ** Client key** and **Server key**.

for example:

```

           public static final String VT_CLIENT_KEY = "VT-client-Lre_JFh5klhfGefF";
           public static final String VT_SERVER_KEY = "VT-server-pVDm2d9BT3TIbDls_UEmt8wE";

```

- Initialise VeritransSdk as follows


```

         VeritransBuilder veritransBuilder = new
                      VeritransBuilder(getApplicationContext(),
                      VT_CLIENT_KEY, VT_SERVER_KEY);
              veritransBuilder.enableLog(true);   // enable logs of sdk for debugging purpose.
            
         VeritransSDK  mVeritransSDK = veritransBuilder.buildSDK();
        
```    
---
## Table Of Content

1.[Payment options](#payment_options)  
- _Select option for payment and select available list of payments_

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

```
    
        mVeritransSDK.setTransactionRequest(TransactionRequest);


```
    
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
