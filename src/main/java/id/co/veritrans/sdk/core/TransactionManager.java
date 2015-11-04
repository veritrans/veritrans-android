package id.co.veritrans.sdk.core;

import android.app.Activity;

import id.co.veritrans.sdk.callbacks.TransactionCallback;
import id.co.veritrans.sdk.callbacks.TokenCallBack;
import id.co.veritrans.sdk.models.CardTransfer;
import id.co.veritrans.sdk.models.MandiriBillPayTransferModel;
import id.co.veritrans.sdk.models.MandiriClickPayRequestModel;
import id.co.veritrans.sdk.models.PermataBankTransfer;
import id.co.veritrans.sdk.models.TokenDetailsResponse;
import id.co.veritrans.sdk.models.CardTokenRequest;
import id.co.veritrans.sdk.models.TransactionResponse;
import id.co.veritrans.sdk.utilities.Utils;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by shivam on 10/29/15.
 */
class TransactionManager {

    public static void getToken(Activity activity, CardTokenRequest cardTokenRequest, final
    TokenCallBack callBack) {

        final VeritransSDK veritransSDK = VeritransSDK.getVeritransSDK();

        if (veritransSDK != null) {
            VeritranceApiInterface apiInterface =
                    VeritransRestAdapter.getApiClient(activity, true);

            if (apiInterface != null) {

                Observable<TokenDetailsResponse> observable;
                if(cardTokenRequest.isTwoClick()){
                    observable = apiInterface.getTokenTwoClick(
                            cardTokenRequest.getCardCVV(),
                            cardTokenRequest.getSavedTokenId(),
                            cardTokenRequest.isTwoClick(),
                            cardTokenRequest.isSecure(),
                            cardTokenRequest.getGrossAmount(),
                            cardTokenRequest.getBank(),
                            cardTokenRequest.getClientKey());
                } else if (cardTokenRequest.isSecure()) {
                    observable = apiInterface.get3DSToken(cardTokenRequest.getCardNumber(),
                            cardTokenRequest.getCardCVV(),
                            cardTokenRequest.getCardExpiryMonth(), cardTokenRequest
                                    .getCardExpiryYear(),
                            cardTokenRequest.getClientKey(), cardTokenRequest.getBank(),
                            cardTokenRequest.isSecure(), cardTokenRequest.isTwoClick(),
                            cardTokenRequest.getGrossAmount());
                } else {
                    observable = apiInterface.getToken(cardTokenRequest.getCardNumber(),
                            cardTokenRequest.getCardCVV(),
                            cardTokenRequest.getCardExpiryMonth(), cardTokenRequest
                                    .getCardExpiryYear(),
                            cardTokenRequest.getClientKey());
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
                                callBack.onFailure(throwable.getMessage(),null);
                            }

                            @Override
                            public void onNext(TokenDetailsResponse tokenDetailsResponse) {

                                if (tokenDetailsResponse != null) {

                                    if (veritransSDK != null && veritransSDK.isLogEnabled()) {
                                        displayTokenResponse(tokenDetailsResponse);
                                    }

                                    if (tokenDetailsResponse.getStatusCode().trim()
                                            .equalsIgnoreCase(Constants.SUCCESS_CODE_200)) {
                                        callBack.onSuccess(tokenDetailsResponse);
                                    } else {
                                        callBack.onFailure(Constants.ERROR_EMPTY_RESPONSE,tokenDetailsResponse);
                                    }

                                } else {
                                    callBack.onFailure(Constants.ERROR_EMPTY_RESPONSE,null);
                                    Logger.e(Constants.ERROR_EMPTY_RESPONSE);
                                }

                            }
                        });

            } else {
                callBack.onFailure(Constants.ERROR_UNABLE_TO_CONNECT,null);
                Logger.e(Constants.ERROR_UNABLE_TO_CONNECT);
            }

        } else {
            callBack.onFailure(Constants.ERROR_SDK_IS_NOT_INITIALIZED,null);
            Logger.e(Constants.ERROR_SDK_IS_NOT_INITIALIZED);
        }

    }

    public static void paymentUsingPermataBank(final Activity activity, final PermataBankTransfer
            permataBankTransfer, final TransactionCallback callBack) {

        final VeritransSDK veritransSDK = VeritransSDK.getVeritransSDK();

        if (veritransSDK != null) {
            VeritranceApiInterface apiInterface =
                    VeritransRestAdapter.getApiClient(activity, true);

            if (apiInterface != null) {

                Observable<TransactionResponse> observable = null;

                String serverKey = Utils.calculateBase64(veritransSDK.getServerKey());
                if (serverKey != null) {

                    String authorization = "Basic " + serverKey;
                    observable = apiInterface.paymentUsingPermataBank(authorization,
                            permataBankTransfer);

                    observable.subscribeOn(Schedulers
                            .io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<TransactionResponse>() {

                                @Override
                                public void onCompleted() {
                                }

                                @Override
                                public void onError(Throwable throwable) {
                                    Logger.e("bank Transfer transaction error ", "" +
                                            throwable.getMessage());
                                    callBack.onFailure(throwable.getMessage(),null);
                                }

                                @Override
                                public void onNext(TransactionResponse
                                                           permataBankTransferResponse) {

                                    if (permataBankTransferResponse != null) {


                                        if (veritransSDK != null && veritransSDK.isLogEnabled()) {
                                            displayResponse(permataBankTransferResponse);
                                        }

                                        if (permataBankTransferResponse.getStatusCode().trim()
                                                .equalsIgnoreCase(Constants.SUCCESS_CODE_200)
                                                || permataBankTransferResponse.getStatusCode()
                                                .trim().equalsIgnoreCase(Constants.SUCCESS_CODE_201)) {

                                            callBack.onSuccess(permataBankTransferResponse);
                                        } else {
                                            callBack.onFailure(permataBankTransferResponse
                                                    .getStatusMessage(),permataBankTransferResponse);
                                        }

                                    } else {
                                        callBack.onFailure(Constants.ERROR_EMPTY_RESPONSE,null);
                                        Logger.e(Constants.ERROR_EMPTY_RESPONSE);
                                    }

                                }
                            });
                } else {
                    Logger.e(Constants.ERROR_INVALID_DATA_SUPPLIED);
                    callBack.onFailure(Constants.ERROR_INVALID_DATA_SUPPLIED,null);
                }
            } else {
                callBack.onFailure(Constants.ERROR_UNABLE_TO_CONNECT,null);
                Logger.e(Constants.ERROR_UNABLE_TO_CONNECT);
            }

        } else {
            Logger.e(Constants.ERROR_SDK_IS_NOT_INITIALIZED);
            callBack.onFailure(Constants.ERROR_SDK_IS_NOT_INITIALIZED,null);
        }
    }


    public static void paymentUsingCard(Activity activity, CardTransfer cardTransfer, final TransactionCallback cardPaymentTransactionCallback) {
        VeritransSDK veritransSDK = VeritransSDK.getVeritransSDK();

        if (veritransSDK != null) {
            VeritranceApiInterface apiInterface =
                    VeritransRestAdapter.getApiClient(activity, true);

            if (apiInterface != null) {

                Observable<TransactionResponse> observable = null;

                String serverKey = Utils.calculateBase64(veritransSDK.getServerKey());
                if (serverKey != null) {

                    String authorization = "Basic " + serverKey;
                    observable = apiInterface.paymentUsingCard(authorization,
                            cardTransfer);

                    observable.subscribeOn(Schedulers
                            .io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<TransactionResponse>() {
                                @Override
                                public void onCompleted() {

                                }

                                @Override
                                public void onError(Throwable e) {
                                    Logger.e("card Transfer transaction error ", "" +
                                            e.getMessage());
                                    cardPaymentTransactionCallback.onFailure(e.getMessage(),null);
                                }

                                @Override
                                public void onNext(TransactionResponse cardPaymentResponse) {
                                    Logger.i("@ onNext cardPayment:"+cardPaymentResponse);
                                    if (cardPaymentResponse != null) {

                                        if (cardPaymentResponse.getStatusCode().trim()
                                                .equalsIgnoreCase("200")
                                                || cardPaymentResponse.getStatusCode()
                                                .trim().equalsIgnoreCase("201")) {
                                            Logger.i("@ onNext cardPayment:"+cardPaymentResponse);
                                            cardPaymentTransactionCallback.onSuccess(cardPaymentResponse);
                                        } else {
                                            Logger.i("@ onNext cardPayment fail"+cardPaymentResponse.getStatusCode());
                                            cardPaymentTransactionCallback.onFailure(cardPaymentResponse
                                                    .getStatusMessage(),cardPaymentResponse);
                                        }

                                    } else {
                                        Logger.i("@ onNext cardPayment fail null");
                                        cardPaymentTransactionCallback.onFailure(Constants.ERROR_EMPTY_RESPONSE,null);
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




    public static void paymentUsingMandiriClickPay(final Activity activity, final MandiriClickPayRequestModel
            mandiriClickPayRequestModel, final TransactionCallback callBack) {

        final VeritransSDK veritransSDK = VeritransSDK.getVeritransSDK();

        if (veritransSDK != null) {
            VeritranceApiInterface apiInterface =
                    VeritransRestAdapter.getApiClient(activity, true);

            if (apiInterface != null) {

                Observable<TransactionResponse> observable = null;

                String serverKey = Utils.calculateBase64(veritransSDK.getServerKey());
                if (serverKey != null) {

                    String authorization = "Basic " + serverKey;
                    observable = apiInterface.paymentUsingMandiriClickPay(authorization,
                            mandiriClickPayRequestModel);

                    observable.subscribeOn(Schedulers
                            .io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<TransactionResponse>() {

                                @Override
                                public void onCompleted() {
                                }

                                @Override
                                public void onError(Throwable throwable) {
                                    Logger.e("bank Transfer transaction error ", "" +
                                            throwable.getMessage());
                                    callBack.onFailure(throwable.getMessage(),null);
                                }

                                @Override
                                public void onNext(TransactionResponse
                                                           mandiriTransferResponse) {

                                    if (mandiriTransferResponse != null) {


                                        if (veritransSDK != null && veritransSDK.isLogEnabled()) {
                                            displayResponse(mandiriTransferResponse);
                                        }

                                        if (mandiriTransferResponse.getStatusCode().trim()
                                                .equalsIgnoreCase(Constants.SUCCESS_CODE_200)
                                                || mandiriTransferResponse.getStatusCode()
                                                .trim().equalsIgnoreCase(Constants.SUCCESS_CODE_201)) {

                                            callBack.onSuccess(mandiriTransferResponse);
                                        } else {
                                            callBack.onFailure(mandiriTransferResponse
                                                    .getStatusMessage(),mandiriTransferResponse);
                                        }

                                    } else {
                                        callBack.onFailure(Constants.ERROR_EMPTY_RESPONSE,null);
                                        Logger.e(Constants.ERROR_EMPTY_RESPONSE,null);
                                    }

                                }
                            });
                } else {
                    Logger.e(Constants.ERROR_INVALID_DATA_SUPPLIED);
                    callBack.onFailure(Constants.ERROR_INVALID_DATA_SUPPLIED,null);
                }
            } else {
                callBack.onFailure(Constants.ERROR_UNABLE_TO_CONNECT,null);
                Logger.e(Constants.ERROR_UNABLE_TO_CONNECT);
            }

        } else {
            Logger.e(Constants.ERROR_SDK_IS_NOT_INITIALIZED);
            callBack.onFailure(Constants.ERROR_SDK_IS_NOT_INITIALIZED,null);
        }
    }




    public static void paymentUsingMandiriBillPay(Activity activity, MandiriBillPayTransferModel
            mandiriBillPayTransferModel, final TransactionCallback callBack) {

            final VeritransSDK veritransSDK = VeritransSDK.getVeritransSDK();

            if (veritransSDK != null) {
                VeritranceApiInterface apiInterface =
                        VeritransRestAdapter.getApiClient(activity, true);

                if (apiInterface != null) {

                    Observable<TransactionResponse> observable = null;

                    String serverKey = Utils.calculateBase64(veritransSDK.getServerKey());
                    if (serverKey != null) {

                        String authorization = "Basic " + serverKey;
                        observable = apiInterface.paymentUsingMandiriBillPay(authorization,
                                mandiriBillPayTransferModel);

                        observable.subscribeOn(Schedulers
                                .io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Observer<TransactionResponse>() {

                                    @Override
                                    public void onCompleted() {
                                    }

                                    @Override
                                    public void onError(Throwable throwable) {
                                        Logger.e("mandiri bill pay transaction error ", "" +
                                                throwable.getMessage());
                                        callBack.onFailure(throwable.getMessage(),null);
                                    }

                                    @Override
                                    public void onNext(TransactionResponse
                                                               permataBankTransferResponse) {

                                        if (permataBankTransferResponse != null) {


                                            if (veritransSDK != null && veritransSDK.isLogEnabled()) {
                                                displayResponse(permataBankTransferResponse);
                                            }

                                            if (permataBankTransferResponse.getStatusCode().trim()
                                                    .equalsIgnoreCase(Constants.SUCCESS_CODE_200)
                                                    || permataBankTransferResponse.getStatusCode()
                                                    .trim().equalsIgnoreCase(Constants.SUCCESS_CODE_201)) {

                                                callBack.onSuccess(permataBankTransferResponse);
                                            } else {
                                                callBack.onFailure(permataBankTransferResponse
                                                        .getStatusMessage(),permataBankTransferResponse);
                                            }

                                        } else {
                                            callBack.onFailure(Constants.ERROR_EMPTY_RESPONSE,null);
                                            Logger.e(Constants.ERROR_EMPTY_RESPONSE);
                                        }

                                    }
                                });
                    } else {
                        Logger.e(Constants.ERROR_INVALID_DATA_SUPPLIED);
                        callBack.onFailure(Constants.ERROR_INVALID_DATA_SUPPLIED,null);
                    }
                } else {
                    callBack.onFailure(Constants.ERROR_UNABLE_TO_CONNECT,null);
                    Logger.e(Constants.ERROR_UNABLE_TO_CONNECT);
                }

            } else {
                Logger.e(Constants.ERROR_SDK_IS_NOT_INITIALIZED);
                callBack.onFailure(Constants.ERROR_SDK_IS_NOT_INITIALIZED,null);
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


    private static void displayResponse(TransactionResponse
                                                transferResponse) {
        Logger.d("transfer response: virtual account" +
                " number ", "" +
                transferResponse.getPermataVANumber());

        Logger.d(" transfer response: status message " +
                "", "" +
                transferResponse.getStatusMessage());

        Logger.d(" transfer response: status code ",
                "" + transferResponse.getStatusCode());


        Logger.d(" transfer response: transaction Id ",
                "" + transferResponse
                        .getTransactionId());

        Logger.d(" transfer response: transaction " +
                        "status ",
                "" + transferResponse
                        .getTransactionStatus());
    }


}