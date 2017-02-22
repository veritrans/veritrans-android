package com.midtrans.sdk.ui.presenters;

import android.content.Context;

import com.midtrans.sdk.core.MidtransCore;
import com.midtrans.sdk.core.MidtransCoreCallback;
import com.midtrans.sdk.core.models.merchant.CheckoutTokenRequest;
import com.midtrans.sdk.core.models.merchant.CheckoutTokenResponse;
import com.midtrans.sdk.core.models.merchant.ItemDetails;
import com.midtrans.sdk.core.models.snap.transaction.SnapEnabledPayment;
import com.midtrans.sdk.core.models.snap.transaction.SnapTransaction;
import com.midtrans.sdk.ui.MidtransUi;
import com.midtrans.sdk.ui.R;
import com.midtrans.sdk.ui.constants.PaymentCategory;
import com.midtrans.sdk.ui.constants.PaymentType;
import com.midtrans.sdk.ui.constants.SnapResponseMessagePattern;
import com.midtrans.sdk.ui.models.ItemDetail;
import com.midtrans.sdk.ui.models.PaymentMethodModel;
import com.midtrans.sdk.ui.models.TransactionOption;
import com.midtrans.sdk.ui.utils.PaymentMethodHelper;
import com.midtrans.sdk.ui.utils.Utils;
import com.midtrans.sdk.ui.views.activities.TransactionView;

import java.util.ArrayList;
import java.util.List;

import static com.midtrans.sdk.ui.constants.PaymentType.E_CHANNEL;

/**
 * Created by ziahaqi on 2/19/17.
 */

public class TransactionPresenter {
    private MidtransUi sdk;
    private Context context;
    private TransactionView view;
    private List<PaymentMethodModel> paymentMethodList = new ArrayList<>();
    private List<PaymentMethodModel> bankTranferList = new ArrayList<>();

    public TransactionPresenter(Context context, TransactionView view) {
        sdk = MidtransUi.getInstance();
        this.context = context;
        this.view = view;
    }

    public void checkout() {
        MidtransCore.getInstance().checkout(sdk.getCheckoutUrl(), sdk.getCheckoutTokenRequest(), new MidtransCoreCallback<CheckoutTokenResponse>() {
            @Override
            public void onSuccess(CheckoutTokenResponse checkoutTokenResponse) {
                getTransactionDetails(checkoutTokenResponse);
            }

            @Override
            public void onFailure(CheckoutTokenResponse object) {
                view.showProgressContainer(false);
                view.showConfirmationDialog(createCheckoutErrorMessage(object));
            }

            @Override
            public void onError(Throwable throwable) {
                view.showProgressContainer(false);
                view.showConfirmationDialog(context.getString(R.string.failed_get_snap_token));
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

    private void getTransactionDetails(CheckoutTokenResponse checkoutTokenResponse) {
        sdk.setCheckoutToken(checkoutTokenResponse.token);
        MidtransCore.getInstance().getTransactionDetails(checkoutTokenResponse.token, new MidtransCoreCallback<SnapTransaction>() {
            @Override
            public void onSuccess(SnapTransaction snapTransaction) {
                sdk.setTransactionOption(new TransactionOption(snapTransaction));
                sdk.setMerchantName(snapTransaction.merchant.preference.displayName);
                sdk.setMerchantLogoUrl(snapTransaction.merchant.preference.logoUrl);

                view.showProgressContainer(false);
                view.showPaymentMethods(createPaymentMethodsModel(snapTransaction.enabledPayments));
            }

            @Override
            public void onFailure(SnapTransaction snapTransaction) {
                view.showProgressContainer(false);
            }

            @Override
            public void onError(Throwable throwable) {
                view.showProgressContainer(false);
            }
        });
    }

    private List<ItemDetail> createItemDetails() {
        List<ItemDetail> itemViewDetails = new ArrayList<>();

        CheckoutTokenRequest checkoutTokenRequest = sdk.getCheckoutTokenRequest();

        // Add amount
        String amount = context.getString(R.string.prefix_money, Utils.getFormattedAmount(checkoutTokenRequest.transactionDetails.grossAmount));

        // Add header total amount
        itemViewDetails.add(new ItemDetail(
                null,
                amount,
                ItemDetail.TYPE_ITEM_HEADER,
                checkoutTokenRequest.itemDetails.size() > 0));

        // Add items
        for (ItemDetails itemDetails : sdk.getCheckoutTokenRequest().itemDetails) {
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

                PaymentMethodModel bankTransferPaymentMethod = PaymentMethodHelper.createBankTransferPaymentMethod(context, enabledPayment.type);
                if (bankTransferPaymentMethod != null) {
                    bankTranferList.add(bankTransferPaymentMethod);
                }
            } else {
                PaymentMethodModel model = PaymentMethodHelper.createPaymentMethodModel(context, enabledPayment.type);
                if (model != null) {
                    paymentMethodList.add(model);
                }
            }
        }

        if (!bankTranferList.isEmpty()) {
            paymentMethodList.add(PaymentMethodHelper.createPaymentMethodModel(context, PaymentType.BANK_TRANSFER));
        }

        return paymentMethodList;
    }
}
