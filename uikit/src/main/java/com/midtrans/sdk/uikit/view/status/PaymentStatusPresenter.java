package com.midtrans.sdk.uikit.view.status;

import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.AkulakuResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.BcaKlikpayResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.BriEpayPaymentResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.CimbClicksResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.CreditCardResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.DanamonOnlineResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.GopayResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.IndomaretPaymentResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.KlikBcaResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.MandiriClickpayResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.TelkomselCashResponse;
import com.midtrans.sdk.uikit.base.composer.BasePaymentPresenter;
import com.midtrans.sdk.uikit.base.contract.BasePaymentContract;

public class PaymentStatusPresenter extends BasePaymentPresenter<BasePaymentContract> {

    protected GopayResponse gopayResponse;
    protected CreditCardResponse creditCardResponse;
    protected AkulakuResponse akulakuResponse;
    protected BcaKlikpayResponse bcaKlikpayResponse;
    protected BriEpayPaymentResponse briEpayPaymentResponse;
    protected CimbClicksResponse cimbClicksResponse;
    protected DanamonOnlineResponse danamonOnlineResponse;
    protected IndomaretPaymentResponse indomaretPaymentResponse;
    protected KlikBcaResponse klikBcaResponse;
    protected MandiriClickpayResponse mandiriClickpayResponse;
    protected TelkomselCashResponse telkomselCashResponse;

    public PaymentStatusPresenter() {
        super();
    }

    public <T> PaymentStatusPresenter(T response) {
        super();
        if (response != null) {
            if (response instanceof GopayResponse) {
                this.gopayResponse = (GopayResponse) response;
            } else if (response instanceof CreditCardResponse) {
                this.creditCardResponse = (CreditCardResponse) response;
            } else if (response instanceof AkulakuResponse) {
                this.akulakuResponse = (AkulakuResponse) response;
            } else if (response instanceof BcaKlikpayResponse) {
                this.bcaKlikpayResponse = (BcaKlikpayResponse) response;
            } else if (response instanceof BriEpayPaymentResponse) {
                this.briEpayPaymentResponse = (BriEpayPaymentResponse) response;
            } else if (response instanceof CimbClicksResponse) {
                this.cimbClicksResponse = (CimbClicksResponse) response;
            } else if (response instanceof DanamonOnlineResponse) {
                this.danamonOnlineResponse = (DanamonOnlineResponse) response;
            } else if (response instanceof IndomaretPaymentResponse) {
                this.indomaretPaymentResponse = (IndomaretPaymentResponse) response;
            } else if (response instanceof KlikBcaResponse) {
                this.klikBcaResponse = (KlikBcaResponse) response;
            } else if (response instanceof MandiriClickpayResponse) {
                this.mandiriClickpayResponse = (MandiriClickpayResponse) response;
            } else if (response instanceof TelkomselCashResponse) {
                this.telkomselCashResponse = (TelkomselCashResponse) response;
            }
        }
    }

}