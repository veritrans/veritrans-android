package id.co.veritrans.sdk.core;

import android.app.Activity;

import id.co.veritrans.sdk.callbacks.PermataBankTransferStatus;
import id.co.veritrans.sdk.callbacks.TokenCallBack;
import id.co.veritrans.sdk.models.PermataBankTransfer;
import id.co.veritrans.sdk.models.PermataBankTransferResponse;
import id.co.veritrans.sdk.models.TokenDetailsResponse;
import id.co.veritrans.sdk.models.TokenRequestModel;
import id.co.veritrans.sdk.utilities.Utils;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by shivam on 10/29/15.
 */
class TransactionManager {

    public static final String SUCCESS_CODE = "200";

    public static void getToken(Activity activity, TokenRequestModel tokenRequestModel, final
    TokenCallBack callBack) {

        final VeritransSDK veritransSDK = VeritransSDK.getVeritransSDK();

        if (veritransSDK != null) {
            VeritranceApiInterface apiInterface =
                    VeritransRestAdapter.getApiClient(activity, true);


            if (apiInterface != null) {

                Observable<TokenDetailsResponse> observable;

                if (tokenRequestModel.isSecure()) {
                    observable = apiInterface.get3DSToken(tokenRequestModel.getCardNumber(),
                            tokenRequestModel.getCardCVV(),
                            tokenRequestModel.getCardExpiryMonth(), tokenRequestModel
                                    .getCardExpiryYear(),
                            tokenRequestModel.getClientKey(), tokenRequestModel.getBank(),
                            tokenRequestModel.isSecure(), tokenRequestModel.isTwoClick(),
                            tokenRequestModel.getGrossAmount());
                } else {
                    observable = apiInterface.getToken(tokenRequestModel.getCardNumber(),
                            tokenRequestModel.getCardCVV(),
                            tokenRequestModel.getCardExpiryMonth(), tokenRequestModel
                                    .getCardExpiryYear(),
                            tokenRequestModel.getClientKey());
                }


                observable.subscribeOn(Schedulers
                        .io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<TokenDetailsResponse>() {

                            @Override
                            public void onCompleted() {

                            }

                            @Override
                            public void onError(Throwable throwable) {

                                Logger.e("error while getting token : ", "" +
                                        throwable.getMessage());
                                callBack.onFailure(throwable.getMessage());
                            }

                            @Override
                            public void onNext(TokenDetailsResponse tokenDetailsResponse) {

                                if (tokenDetailsResponse != null) {

                                    if (veritransSDK != null && veritransSDK.isLogEnabled()) {
                                        displayTokenResponse(tokenDetailsResponse);
                                    }

                                    if (tokenDetailsResponse.getStatusCode().trim()
                                            .equalsIgnoreCase(SUCCESS_CODE)) {
                                        callBack.onSuccess(tokenDetailsResponse);
                                    } else {
                                        callBack.onFailure(Constants.ERROR_EMPTY_RESPONSE);
                                    }

                                } else {
                                    callBack.onFailure(Constants.ERROR_EMPTY_RESPONSE);
                                    Logger.e(Constants.ERROR_EMPTY_RESPONSE);
                                }

                            }
                        });

            } else {
                callBack.onFailure(Constants.ERROR_UNABLE_TO_CONNECT);
                Logger.e(Constants.ERROR_UNABLE_TO_CONNECT);
            }

        } else {
            callBack.onFailure(Constants.ERROR_SDK_IS_NOT_INITIALIZED);
            Logger.e(Constants.ERROR_SDK_IS_NOT_INITIALIZED);
        }

    }


    public static void paymentUsingPermataBank(final Activity activity, final PermataBankTransfer
            permataBankTransfer, final PermataBankTransferStatus callBack) {

        final VeritransSDK veritransSDK = VeritransSDK.getVeritransSDK();

        if (veritransSDK != null) {
            VeritranceApiInterface apiInterface =
                    VeritransRestAdapter.getApiClient(activity, true);


            if (apiInterface != null) {

                Observable<PermataBankTransferResponse> observable = null;

                String serverKey = Utils.calculateBase64(veritransSDK.getServerKey());
                if (serverKey != null) {

                    String authorization = "Basic " + serverKey;
                    observable = apiInterface.paymentUsingPermataBank(authorization,
                            permataBankTransfer);

                    observable.subscribeOn(Schedulers
                            .io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<PermataBankTransferResponse>() {

                                @Override
                                public void onCompleted() {
                                }

                                @Override
                                public void onError(Throwable throwable) {
                                    Logger.e("bank Transfer transaction error ", "" +
                                            throwable.getMessage());
                                    callBack.onFailure(throwable.getMessage());
                                }

                                @Override
                                public void onNext(PermataBankTransferResponse
                                                           permataBankTransferResponse) {

                                    if (permataBankTransferResponse != null) {

                                        if (veritransSDK != null && veritransSDK.isLogEnabled()) {
                                            displayPermataBankResponse(permataBankTransferResponse);
                                        }

                                        if (permataBankTransferResponse.getStatus_code().trim()
                                                .equalsIgnoreCase(SUCCESS_CODE)
                                                || permataBankTransferResponse.getStatus_code()
                                                .trim().equalsIgnoreCase("201")) {

                                            callBack.onSuccess(permataBankTransferResponse);
                                        } else {
                                            callBack.onFailure(permataBankTransferResponse
                                                    .getStatus_message());
                                        }

                                    } else {
                                        callBack.onFailure(Constants.ERROR_EMPTY_RESPONSE);
                                        Logger.e(Constants.ERROR_EMPTY_RESPONSE);
                                    }

                                }
                            });
                } else {
                    Logger.e(Constants.ERROR_INVALID_DATA_SUPPLIED);
                    callBack.onFailure(Constants.ERROR_INVALID_DATA_SUPPLIED);
                }
            } else {
                callBack.onFailure(Constants.ERROR_UNABLE_TO_CONNECT);
                Logger.e(Constants.ERROR_UNABLE_TO_CONNECT);
            }

        } else {
            Logger.e(Constants.ERROR_SDK_IS_NOT_INITIALIZED);
            callBack.onFailure(Constants.ERROR_SDK_IS_NOT_INITIALIZED);
        }
    }


    private static void displayTokenResponse(TokenDetailsResponse tokenDetailsResponse) {
        Logger.d("token response: status code ", "" +
                tokenDetailsResponse.getStatusCode());
        Logger.d("token response: status message ", "" +
                tokenDetailsResponse.getStatusMessage());
        Logger.d("token response: token Id ", "" + tokenDetailsResponse
                .getTokenId());
        Logger.d("token response: redirect url ", "" +
                tokenDetailsResponse.getRedirectUrl());
        Logger.d("token response: bank ", "" + tokenDetailsResponse
                .getBank());
    }


    private static void displayPermataBankResponse(PermataBankTransferResponse
                                                           permataBankTransferResponse) {
        Logger.d("permata bank transfer response: virtual account" +
                " number ", "" +
                permataBankTransferResponse.getPermata_va_number());

        Logger.d("permata bank transfer response: status message " +
                "", "" +
                permataBankTransferResponse.getStatus_message());

        Logger.d("permata bank transfer response: status code ",
                "" + permataBankTransferResponse.getStatus_code());


        Logger.d("permata bank transfer response: transaction Id ",
                "" + permataBankTransferResponse
                        .getTransaction_id());

        Logger.d("permata bank transfer response: transaction " +
                        "status ",
                "" + permataBankTransferResponse
                        .getTransaction_status());
    }

}