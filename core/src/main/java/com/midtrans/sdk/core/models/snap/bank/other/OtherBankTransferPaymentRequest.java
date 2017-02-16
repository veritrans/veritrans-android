package com.midtrans.sdk.core.models.snap.bank.other;

import com.midtrans.sdk.core.models.snap.SnapCustomerDetails;
import com.midtrans.sdk.core.models.snap.bank.BankTransferPaymentRequest;

/**
 * Created by rakawm on 1/24/17.
 */

public class OtherBankTransferPaymentRequest extends BankTransferPaymentRequest {
    public static final String TYPE = "other_va";

    public OtherBankTransferPaymentRequest(SnapCustomerDetails customerDetails) {
        super(TYPE, customerDetails);
    }
}
