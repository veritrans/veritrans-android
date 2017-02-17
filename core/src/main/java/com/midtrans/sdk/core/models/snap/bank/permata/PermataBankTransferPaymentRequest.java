package com.midtrans.sdk.core.models.snap.bank.permata;

import com.midtrans.sdk.core.models.snap.SnapCustomerDetails;
import com.midtrans.sdk.core.models.snap.bank.BankTransferPaymentRequest;

/**
 * Created by rakawm on 1/24/17.
 */

public class PermataBankTransferPaymentRequest extends BankTransferPaymentRequest {
    public static final String TYPE = "permata_va";

    public PermataBankTransferPaymentRequest(SnapCustomerDetails customerDetails) {
        super(TYPE, customerDetails);
    }
}
