package com.midtrans.sdk.ui.views.transaction;

import android.content.Context;
import android.text.TextUtils;

import com.midtrans.sdk.core.MidtransCore;
import com.midtrans.sdk.core.MidtransCoreCallback;
import com.midtrans.sdk.core.models.merchant.CheckoutTokenRequest;
import com.midtrans.sdk.core.models.merchant.CheckoutTokenResponse;
import com.midtrans.sdk.core.models.merchant.ItemDetails;
import com.midtrans.sdk.core.models.snap.transaction.SnapEnabledPayment;
import com.midtrans.sdk.core.models.snap.transaction.SnapTransaction;
import com.midtrans.sdk.ui.MidtransUi;
import com.midtrans.sdk.ui.R;
import com.midtrans.sdk.ui.abtracts.BasePresenter;
import com.midtrans.sdk.ui.constants.PaymentCategory;
import com.midtrans.sdk.ui.constants.PaymentType;
import com.midtrans.sdk.ui.constants.SnapResponseMessagePattern;
import com.midtrans.sdk.ui.models.ItemDetail;
import com.midtrans.sdk.ui.models.PaymentMethodModel;
import com.midtrans.sdk.ui.models.PaymentResult;
import com.midtrans.sdk.ui.themes.ColorTheme;
import com.midtrans.sdk.ui.utils.PaymentMethodUtils;
import com.midtrans.sdk.ui.utils.Utils;

import java.util.ArrayList;
import java.util.List;

import static com.midtrans.sdk.ui.constants.PaymentType.E_CHANNEL;

/**
 * Created by ziahaqi on 2/19/17.
 */

public class TransactionPresenter extends BasePresenter implements TransactionContract.Presenter {
    private Context context;
    private List<PaymentMethodModel> paymentMethodList = new ArrayList<>();
    private List<PaymentMethodModel> bankTranferList = new ArrayList<>();
    private TransactionContract.View view;

    public TransactionPresenter(Context context, TransactionContract.View view) {
        midtransUiSdk = MidtransUi.getInstance();
        this.context = context;
        this.view = view;
    }

    /**
     * Init transaction.
     */
    public void init() {
        String checkoutToken = midtransUiSdk.getCheckoutToken();
        if (checkoutToken != null && !TextUtils.isEmpty(checkoutToken)) {
            getTransactionDetails(checkoutToken);
        } else {
            checkout();
        }
    }

    private void checkout() {
        MidtransCore.getInstance().checkout(
                midtransUiSdk.getCheckoutUrl(),
                midtransUiSdk.getCheckoutTokenRequest(),
                new MidtransCoreCallback<CheckoutTokenResponse>() {
            @Override
            public void onSuccess(CheckoutTokenResponse checkoutTokenResponse) {
                getTransactionDetails(checkoutTokenResponse.token);
            }

            @Override
            public void onFailure(CheckoutTokenResponse object) {
                view.showProgressContainer(false);
                view.showErrorContainer(createCheckoutErrorMessage(object), isErrorNeedRetry(object));
            }

            @Override
            public void onError(Throwable throwable) {
                view.showProgressContainer(false);
                view.showErrorContainer();
            }
        });
    }

    private String createCheckoutErrorMessage(CheckoutTokenResponse checkoutTokenResponse) {
        String errorMessage = context.getString(R.string.failed_get_snap_token);
        if (checkoutTokenResponse != null && checkoutTokenResponse.errorMessages != null) {
            if (checkoutTokenResponse.errorMessages.get(0).contains(SnapResponseMessagePattern.ERROR_PAID_ORDER_ID)) {
                errorMessage = context.getString(R.string.error_paid_order_id);
            }
        }
        return errorMessage;
    }

    private boolean isErrorNeedRetry(CheckoutTokenResponse checkoutTokenResponse) {
        if (checkoutTokenResponse != null && checkoutTokenResponse.errorMessages != null) {
            if (checkoutTokenResponse.errorMessages.get(0).contains(SnapResponseMessagePattern.ERROR_PAID_ORDER_ID)) {
                return true;
            }
        }
        return false;
    }

    private void getTransactionDetails(String token) {
        midtransUiSdk.setCheckoutToken(token);
        MidtransCore.getInstance().getTransactionDetails(token, new MidtransCoreCallback<SnapTransaction>() {
            @Override
            public void onSuccess(SnapTransaction snapTransaction) {
                // Set transaction details
                midtransUiSdk.setTransaction(snapTransaction);
                // Set color theme
                midtransUiSdk.setColorTheme(new ColorTheme(context, snapTransaction.merchant.preference.colorScheme));
                // Dismiss progress
                view.showProgressContainer(false);
                // Show payment methods
                view.showPaymentMethods(createPaymentMethodsModel(snapTransaction.enabledPayments));
                // Show merchant name or logo
                view.showMerchantNameOrLogo(snapTransaction.merchant.preference.displayName, snapTransaction.merchant.preference.logoUrl);
                // Update color theme
                view.updateColorTheme();

            }

            @Override
            public void onFailure(SnapTransaction snapTransaction) {
                view.showProgressContainer(false);
                view.showErrorContainer();
            }

            @Override
            public void onError(Throwable throwable) {
                view.showProgressContainer(false);
                view.showErrorContainer();
            }
        });
    }

    private List<ItemDetail> createItemDetails() {
        List<ItemDetail> itemViewDetails = new ArrayList<>();

        CheckoutTokenRequest checkoutTokenRequest = midtransUiSdk.getCheckoutTokenRequest();

        // Add amount
        String amount = context.getString(R.string.prefix_money, Utils.getFormattedAmount(checkoutTokenRequest.transactionDetails.grossAmount));

        // Add header total amount
        itemViewDetails.add(new ItemDetail(
                null,
                amount,
                ItemDetail.TYPE_ITEM_HEADER,
                checkoutTokenRequest.itemDetails.size() > 0));

        // Add items
        for (ItemDetails itemDetails : midtransUiSdk.getCheckoutTokenRequest().itemDetails) {
            String price = context.getString(R.string.prefix_money, Utils.getFormattedAmount(itemDetails.quantity * itemDetails.price));
            String itemName = itemDetails.name;
            if (itemDetails.quantity > 1) {
                itemName = context.getString(R.string.text_item_name_format, itemDetails.name, itemDetails.quantity);
            }
            itemViewDetails.add(new ItemDetail(itemName, price, ItemDetail.TYPE_ITEM, true));
        }
        return itemViewDetails;
    }


    public List<ItemDetail> getItemDetails() {
        return createItemDetails();
    }

    private List<PaymentMethodModel> createPaymentMethodsModel(List<SnapEnabledPayment> enabledPayments) {
        bankTranferList.clear();

        for (SnapEnabledPayment enabledPayment : enabledPayments) {
            if ((enabledPayment.category != null && enabledPayment.category.equals(PaymentCategory.BANK_TRANSFER))
                    || enabledPayment.type.equalsIgnoreCase(E_CHANNEL)) {

                PaymentMethodModel bankTransferPaymentMethod = PaymentMethodUtils.createBankTransferPaymentMethod(context, enabledPayment.type);
                if (bankTransferPaymentMethod != null) {
                    bankTranferList.add(bankTransferPaymentMethod);
                }
            } else {
                PaymentMethodModel model = PaymentMethodUtils.createPaymentMethodModel(context, enabledPayment.type);
                if (model != null) {
                    paymentMethodList.add(model);
                }
            }
        }

        if (!bankTranferList.isEmpty()) {
            paymentMethodList.add(PaymentMethodUtils.createPaymentMethodModel(context, PaymentType.BANK_TRANSFER));
        }

        return paymentMethodList;
    }

    public void sendPaymentResult(PaymentResult result) {
        Utils.sendPaymentResult(result);
    }

    public int getPrimaryColor() {
        return midtransUiSdk.getColorTheme().getPrimaryColor();
    }
}
