package com.midtrans.sdk.core.models.snap.transaction;

import com.midtrans.sdk.core.models.snap.CreditCard;
import com.midtrans.sdk.core.models.snap.CustomerDetails;
import com.midtrans.sdk.core.models.snap.ItemDetails;

import java.util.List;

/**
 * Created by rakawm on 10/20/16.
 */

public class SnapTransaction {
    public final String token;
    public final SnapTransactionDetails transactionDetails;
    public final CustomerDetails customerDetails;
    public final List<ItemDetails> itemDetails;
    public final SnapCallbacks callbacks;
    public final List<SnapEnabledPayment> enabledPayments;
    public final CreditCard creditCard;
    public final SnapMerchantData merchantData;

    public SnapTransaction(String token,
                           SnapTransactionDetails transactionDetails,
                           CustomerDetails customerDetails,
                           List<ItemDetails> itemDetails,
                           SnapCallbacks callbacks,
                           List<SnapEnabledPayment> enabledPayments,
                           CreditCard creditCard,
                           SnapMerchantData merchantData) {
        this.token = token;
        this.transactionDetails = transactionDetails;
        this.customerDetails = customerDetails;
        this.itemDetails = itemDetails;
        this.callbacks = callbacks;
        this.enabledPayments = enabledPayments;
        this.creditCard = creditCard;
        this.merchantData = merchantData;
    }
}
