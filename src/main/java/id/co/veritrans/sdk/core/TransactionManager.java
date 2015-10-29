package id.co.veritrans.sdk.core;

import android.app.Activity;

import id.co.veritrans.sdk.callbacks.TokenCallBack;
import id.co.veritrans.sdk.models.TokenDetailsResponse;
import id.co.veritrans.sdk.models.TokenRequestModel;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by shivam on 10/29/15.
 */
class TransactionManager {


    public static void getToken(Activity activity, TokenRequestModel tokenRequestModel, final TokenCallBack callBack) {

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

                            }
                        });

            }

        } else {
            Logger.e("sdk is not initialized.");
        }

    }

}