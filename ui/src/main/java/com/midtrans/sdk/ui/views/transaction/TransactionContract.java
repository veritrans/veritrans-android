package com.midtrans.sdk.ui.views.transaction;

import com.midtrans.sdk.ui.models.PaymentMethodModel;

import java.util.List;

/**
 * Created by ziahaqi on 2/20/17.
 */

public interface TransactionContract {


    public interface View{
        void showProgressContainer(boolean show);

        void showConfirmationDialog(String errorMessage);

        void showPaymentMethods(List<PaymentMethodModel> paymentMethodsModel, String merchantName);
    }

    public interface Presenter {

    }

}
