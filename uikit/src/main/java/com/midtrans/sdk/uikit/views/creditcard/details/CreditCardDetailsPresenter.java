package com.midtrans.sdk.uikit.views.creditcard.details;

import com.midtrans.sdk.corekit.callback.BankBinsCallback;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.snap.BankBinsResponse;
import com.midtrans.sdk.corekit.models.snap.CreditCard;
import com.midtrans.sdk.uikit.views.creditcard.details.CreditCardDetailsView;
import com.midtrans.sdk.uikit.models.CreditCardTransaction;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ziahaqi on 7/12/17.
 */

public class CreditCardDetailsPresenter {
    private CreditCardDetailsView view;
    CreditCardTransaction creditCardTransaction;

    public CreditCardDetailsPresenter(CreditCardDetailsView view) {
        this.view = view;
        this.creditCardTransaction = new CreditCardTransaction();
    }

    public void setTransactionProperties(CreditCard creditCard, List<BankBinsResponse> bankBins) {
        this.creditCardTransaction.setProperties(creditCard, new ArrayList<>(bankBins));
        getBankBins();
    }

    private void getBankBins() {
        MidtransSDK.getInstance().getBankBins(new BankBinsCallback() {
            @Override
            public void onSuccess(ArrayList<BankBinsResponse> response) {
                creditCardTransaction.setBankBins(response);
            }

            @Override
            public void onFailure(String reason) {

            }

            @Override
            public void onError(Throwable error) {

            }
        });
    }
}
