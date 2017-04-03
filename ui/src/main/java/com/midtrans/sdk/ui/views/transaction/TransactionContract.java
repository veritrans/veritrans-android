package com.midtrans.sdk.ui.views.transaction;

import com.midtrans.sdk.ui.models.PaymentMethodModel;

import java.util.List;

/**
 * Created by ziahaqi on 2/20/17.
 */

public interface TransactionContract {


    interface View {
        void showMerchantNameOrLogo(String merchantName, String merchantLogoUrl);

        void showProgressContainer(boolean show);

        void showPaymentMethods(List<PaymentMethodModel> paymentMethodsModel);

        void updateColorTheme();

        void showErrorContainer();

        void showErrorContainer(String message, boolean needRetry);
    }

    interface Presenter {

    }

}
