package com.midtrans.sdk.core.models.snap.bank.bca;

import com.midtrans.sdk.core.models.snap.SnapCustomerDetails;
import com.midtrans.sdk.core.models.snap.bank.BankTransferPaymentRequest;

/**
 * Created by rakawm on 1/24/17.
 */

public class BcaBankTransferPaymentRequest extends BankTransferPaymentRequest {

    public static final String TYPE = "bca_va";

    public BcaBankTransferPaymentRequest(SnapCustomerDetails customerDetails) {
        super(TYPE, customerDetails);
    }
}
