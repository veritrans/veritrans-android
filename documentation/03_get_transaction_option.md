### Core Flow Implementation Guide

#### Get Transaction Options

After done the checkout, you can get Transaction Options including enabled payment method and others information by using `getTransactionOptions` from SDK instance.

This transaction options will be helpful if you're using custom UI to show all enabled payments from MAP.

```Java
MidtransSDK.getInstance().getTransactionOptions(SNAP_TOKEN, new TransactionOptionsCallback() {
            @Override
            public void onSuccess(TransactionOptionsResponse transaction) {
                
            }

            @Override
            public void onFailure(TransactionOptionsResponse transaction, String reason) {

            }

            @Override
            public void onError(Throwable error) {

            }
        });
```
Note :
- `SNAP_TOKEN` is an unique token after you making `checkout`.