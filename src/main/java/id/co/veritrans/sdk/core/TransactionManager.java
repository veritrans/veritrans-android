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


    public static void getToken(Activity activity, TokenRequestModel tokenRequestModel, final
    TokenCallBack callBack) {

        VeritransSDK veritransSDK = VeritransSDK.getVeritransSDK();

        if (veritransSDK != null) {
            VeritranceApiInterface apiInterface =
                    VeritransRestAdapter.getApiClient(activity, true);


            if (apiInterface != null) {

                Observable<TokenDetailsResponse> observable = null;

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

                                Logger.e("token response: status code ", "" +
                                        throwable.getMessage());
                                callBack.onFailure(throwable.getMessage());
                            }

                            @Override
                            public void onNext(TokenDetailsResponse tokenDetailsResponse) {

                                if (tokenDetailsResponse != null) {

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

                                    callBack.onSuccess(tokenDetailsResponse);

                                } else {
                                    callBack.onFailure(Constants.ERROR_EMPTY_RESPONSE);
                                }

                            }
                        });

            }

        } else {
            Logger.e(Constants.ERROR_SDK_IS_NOT_INITIALIZED);
        }

    }


    public static void paymentUsingPermataBank(Activity activity, PermataBankTransfer
            permataBankTransfer,  final PermataBankTransferStatus callBack) {

        VeritransSDK veritransSDK = VeritransSDK.getVeritransSDK();

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
                                public void onError(Throwable e) {

                                }

                                @Override
                                public void onNext(PermataBankTransferResponse
                                                           permataBankTransferResponse) {


                                    if (permataBankTransferResponse != null) {


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

                                        if( permataBankTransferResponse.getStatus_code().trim().equalsIgnoreCase("200")
                                                || permataBankTransferResponse.getStatus_code().trim().equalsIgnoreCase("201")) {

                                            callBack.onSuccess(permataBankTransferResponse);
                                        }else  {
                                            callBack.onFailure(permataBankTransferResponse.getStatus_message());
                                        }

                                    } else {
                                        callBack.onFailure(Constants.ERROR_EMPTY_RESPONSE);
                                    }
                                }
                            });
                } else {
                    Logger.e(Constants.ERROR_INVALID_DATA_SUPPLIED);
                }
            }

        } else {
            Logger.e(Constants.ERROR_SDK_IS_NOT_INITIALIZED);
        }
    }


}