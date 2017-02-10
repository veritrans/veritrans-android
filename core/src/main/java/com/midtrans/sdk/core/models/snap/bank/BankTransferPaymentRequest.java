package com.midtrans.sdk.core.models.snap.bank;

import com.midtrans.sdk.core.models.snap.BasePaymentRequest;
import com.midtrans.sdk.core.models.snap.SnapCustomerDetails;

/**
 * Created by rakawm on 1/24/17.
 */

public class BankTransferPaymentRequest extends BasePaymentRequest {
    public final SnapCustomerDetails customerDetails;

    public BankTransferPaymentRequest(String paymentType, SnapCustomerDetails customerDetails) {
        super(paymentType);
        this.customerDetails = customerDetails;
    }
}
