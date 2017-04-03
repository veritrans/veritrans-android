package com.midtrans.sdk.ui.views.creditcard;

import android.support.annotation.NonNull;

import com.midtrans.sdk.core.models.papi.CardTokenResponse;
import com.midtrans.sdk.core.models.snap.card.CreditCardPaymentResponse;
import com.midtrans.sdk.ui.abtracts.BaseView;
import com.midtrans.sdk.ui.models.CreditCardDetails;

import java.util.List;

/**
 * Created by ziahaqi on 2/24/17.
 */

public interface CreditCardContract {


    interface CreditCardDetailView extends BaseView<Presenter> {

        void onGetCardTokenSuccess(CardTokenResponse response);

        void onGetCardTokenFailure(CardTokenResponse response);

        void onGetCardTokenError(String message);

        void onCreditCardPaymentError(Throwable throwable);

        void onCreditCardPaymentFailure(CreditCardPaymentResponse response);

        void onCreditCardPaymentSuccess(CreditCardPaymentResponse response);

        String getMaskedCardNumber();

        boolean isSaveEnabled();
    }

    interface SavedCreditCardsView extends BaseView<Presenter> {

    }

    interface Presenter {

        void oneClickPayment(@NonNull String maskedCardNumber);

        void twoClicksPayment(@NonNull CreditCardDetails cardDetailModel, @NonNull String cardCVV);

        void normalPayment(String cardNumber, String cvv, String month, String year, boolean checked);

        boolean isValidInstallment();

        boolean isSaveCardChecked();

        boolean isWhiteListBinsAvailable();

        boolean isCardBinValid(String cardBin);

        List<Integer> getInstallmentTerms(String cleanCardNumber);

        boolean isBankSupportInstallment();

        int getInstallmentTerm(int installmentCurrentPosition);

        void setInstallment(int installmentTerm);

        String getBankByBin(String cleanCardNumber);

        boolean isInstallmentAvailable();

        void setInstallmentAvailableStatus(boolean availableStatus);

        boolean isSecureCardpayment();

        void payUsingCard();

        void getTotalAmount();

        boolean isNormalCardPayment();
    }
}
