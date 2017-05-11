package com.midtrans.sdk.core.models.snap.bank.bni;

import com.midtrans.sdk.core.models.snap.SnapCustomerDetails;
import com.midtrans.sdk.core.models.snap.bank.BankTransferPaymentRequest;

/**
 * Created by rakawm on 5/11/17.
 */

public class BniBankTransferPaymentRequest extends BankTransferPaymentRequest {
    public static final String TYPE = "bni_va";

    public BniBankTransferPaymentRequest(SnapCustomerDetails customerDetails) {
        super(TYPE, customerDetails);
    }
}
