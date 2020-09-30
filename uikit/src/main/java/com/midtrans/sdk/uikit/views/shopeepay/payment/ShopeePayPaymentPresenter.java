package com.midtrans.sdk.uikit.views.shopeepay.payment;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import com.midtrans.sdk.corekit.callback.GetTransactionStatusCallback;
import com.midtrans.sdk.corekit.callback.TransactionCallback;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.models.snap.TransactionStatusResponse;
import com.midtrans.sdk.uikit.BuildConfig;
import com.midtrans.sdk.uikit.abstracts.BasePaymentPresenter;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;
import com.midtrans.sdk.uikit.widgets.Utils;

class ShopeePayPaymentPresenter extends BasePaymentPresenter<ShopeePayPaymentView> {

    private Boolean isTablet, isShopeeInstalled;
    private static final String SHOPEE_PACKAGE_NAME = "com.shopee.id";

    ShopeePayPaymentPresenter(ShopeePayPaymentView view) {
        super();
        this.view = view;
    }

    void setTabletDevice(Activity activity) {
        isTablet = SdkUIFlowUtil.getDeviceType(activity).equals(SdkUIFlowUtil.TYPE_TABLET) && SdkUIFlowUtil.isDeviceTablet(activity);
    }

    void setShopeeInstalled(Context context) {
        if (isProductionBuild()){
            isShopeeInstalled = Utils.isAppInstalled(context, SHOPEE_PACKAGE_NAME);
        }
        isShopeeInstalled = false;
    }

    void openShopeeInPlayStore(Context context) {
        Utils.openAppInPlayStore(context, SHOPEE_PACKAGE_NAME);
    }

    Boolean isTablet() {
        return isTablet;
    }

    Boolean isShopeeInstalled() {
        return isShopeeInstalled;
    }

    Boolean isProductionBuild() {
        return BuildConfig.FLAVOR.equals(UiKitConstants.ENVIRONMENT_PRODUCTION);
    }

    void startShopeePayPayment() {
        String snapToken = getMidtransSDK().readAuthenticationToken();
        if (isTablet) {
            startShopeePayQrisPayment(snapToken);
        } else {
            startShopeePayDeeplinkPayment(snapToken);
        }
    }

    private void startShopeePayDeeplinkPayment(String snapToken) {
        getMidtransSDK().paymentUsingShopeePay(snapToken, new TransactionCallback() {
            @Override
            public void onSuccess(TransactionResponse response) {
                transactionResponse = response;
                view.onPaymentSuccess(response);
            }

            @Override
            public void onFailure(TransactionResponse response, String reason) {
                transactionResponse = response;
                view.onPaymentFailure(response);
            }

            @Override
            public void onError(Throwable error) {
                view.onPaymentError(error);
            }
        });
    }

    private void startShopeePayQrisPayment(String snapToken) {
        getMidtransSDK().paymentUsingShopeePayQris(snapToken, new TransactionCallback() {
            @Override
            public void onSuccess(TransactionResponse response) {
                transactionResponse = response;
                view.onPaymentSuccess(response);
            }

            @Override
            public void onFailure(TransactionResponse response, String reason) {
                transactionResponse = response;
                view.onPaymentFailure(response);
            }

            @Override
            public void onError(Throwable error) {
                view.onPaymentError(error);
            }
        });
    }

    void getPaymentStatus() {
        String snapToken = getMidtransSDK().readAuthenticationToken();
        getMidtransSDK().getTransactionStatus(snapToken, new GetTransactionStatusCallback() {
            @Override
            public void onSuccess(TransactionStatusResponse response) {
                if (response != null && !isPaymentPending(response)) {
                    transactionResponse = convertTransactionStatus(response);
                    view.onGetTransactionStatusSuccess(transactionResponse);
                }
            }

            @Override
            public void onFailure(TransactionStatusResponse response, String reason) {
                //do nothing
            }

            @Override
            public void onError(Throwable error) {
                view.onGetTransactionStatusError(error);
            }
        });
    }

    private boolean isPaymentPending(TransactionStatusResponse response) {
        String statusCode = response.getStatusCode();
        String transactionStatus = response.getTransactionStatus();

        return (!TextUtils.isEmpty(statusCode) && statusCode.equals(UiKitConstants.STATUS_CODE_201)
            || !TextUtils.isEmpty(transactionStatus) && transactionStatus.equalsIgnoreCase(UiKitConstants.STATUS_PENDING));
    }
}
