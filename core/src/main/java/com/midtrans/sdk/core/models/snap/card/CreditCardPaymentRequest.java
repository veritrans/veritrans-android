package com.midtrans.sdk.core.models.snap.card;

import com.midtrans.sdk.core.models.snap.BasePaymentRequest;
import com.midtrans.sdk.core.models.snap.SnapCustomerDetails;

/**
 * Created by rakawm on 10/19/16.
 */

public class CreditCardPaymentRequest extends BasePaymentRequest {
    public static final String TYPE = "credit_card";

    public final CreditCardPaymentParams paymentParams;
    public final SnapCustomerDetails customerDetails;

    public CreditCardPaymentRequest(CreditCardPaymentParams paymentParams,
                                    SnapCustomerDetails customerDetails) {
        super(TYPE);
        this.paymentParams = paymentParams;
        this.customerDetails = customerDetails;
    }
}
