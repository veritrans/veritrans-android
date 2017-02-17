package com.midtrans.sdk.core.models.snap.bank.mandiri;

import com.midtrans.sdk.core.models.snap.SnapCustomerDetails;
import com.midtrans.sdk.core.models.snap.bank.BankTransferPaymentRequest;

/**
 * Created by rakawm on 1/24/17.
 */

public class MandiriBankTransferPaymentRequest extends BankTransferPaymentRequest {
    public static final String TYPE = "echannel";

    public MandiriBankTransferPaymentRequest(SnapCustomerDetails customerDetails) {
        super(TYPE, customerDetails);
    }
}
