package com.midtrans.sdk.corekit.core;

import android.text.TextUtils;

import com.midtrans.sdk.corekit.callback.BankBinsCallback;
import com.midtrans.sdk.corekit.callback.BanksPointCallback;
import com.midtrans.sdk.corekit.callback.DeleteCardCallback;
import com.midtrans.sdk.corekit.callback.GetTransactionStatusCallback;
import com.midtrans.sdk.corekit.callback.TransactionCallback;
import com.midtrans.sdk.corekit.callback.TransactionOptionsCallback;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.corekit.models.snap.BankBinsResponse;
import com.midtrans.sdk.corekit.models.snap.BanksPointResponse;
import com.midtrans.sdk.corekit.models.snap.Transaction;
import com.midtrans.sdk.corekit.models.snap.TransactionStatusResponse;
import com.midtrans.sdk.corekit.models.snap.payment.BankTransferPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.BasePaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.CreditCardPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.DanamonOnlinePaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.GCIPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.GoPayPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.IndosatDompetkuPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.KlikBCAPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.NewMandiriClickPayPaymentRequest;
import com.midtrans.sdk.corekit.models.snap.payment.TelkomselEcashPaymentRequest;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ziahaqi on 3/27/18.
 */

public class SnapServiceManager extends BaseServiceManager {
    private static final String TAG = SnapServiceManager.class.getSimpleName();
    private static final String KEY_ERROR_MESSAGE = "error_messages";

    private SnapApiService service;

    SnapServiceManager(SnapApiService service) {
        this.service = service;
    }


    /**
     * This will create a HTTP request to Snap to get transaction option.
     *
     * @param snapToken Snap Token.
     * @param callback  callback of payment option
     */
    public void getTransactionOptions(final String snapToken, final TransactionOptionsCallback callback) {

        if (service == null) {
            doOnApiServiceUnAvailable(callback);
            return;
        }

        Call<Transaction> call = service.getPaymentOption(snapToken);
        call.enqueue(new Callback<Transaction>() {
            @Override
            public void onResponse(Call<Transaction> call, Response<Transaction> response) {
                releaseResources();

                Transaction transaction = response.body();

                if (transaction != null) {
                    if (response.code() == 200 && !TextUtils.isEmpty(transaction.getToken())) {
                        callback.onSuccess(transaction);
                    } else {
                        callback.onFailure(transaction, response.message());
                    }

                    return;
                }

                try {
                    if (response.errorBody() != null) {

                        String strErrorBody = response.errorBody().string();
                        JSONObject jsonObject = new JSONObject(strErrorBody);
                        String errorMessage = response.message();

                        if (jsonObject != null && jsonObject.getJSONArray(KEY_ERROR_MESSAGE) != null) {
                            JSONArray jsonArray = jsonObject.getJSONArray(KEY_ERROR_MESSAGE);
                            if (jsonArray.get(0) != null) {
                                errorMessage = jsonArray.get(0).toString();
                            }
                        }

                        callback.onError(new Throwable(errorMessage));

                    } else {
                        callback.onError(new Throwable(Constants.MESSAGE_ERROR_EMPTY_RESPONSE));
                        Logger.e(TAG, Constants.MESSAGE_ERROR_EMPTY_RESPONSE);
                    }

                } catch (Exception e) {
                    callback.onError(new Throwable(Constants.MESSAGE_ERROR_EMPTY_RESPONSE));
                    Logger.e(TAG, "e:" + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<Transaction> call, Throwable t) {
                doOnResponseFailure(t, callback);
            }
        });

    }

    /**
     * This method is used for card payment using snap backend.
     *
     * @param paymentRequest Payment details.
     * @param callback       Transaction callback
     */

    public void paymentUsingCreditCard(final String snapToken, CreditCardPaymentRequest paymentRequest, final TransactionCallback callback) {
        if (service == null) {
            doOnApiServiceUnAvailable(callback);
            return;
        }

        Call<TransactionResponse> call = service.paymentUsingCreditCard(snapToken, paymentRequest);
        call.enqueue(new Callback<TransactionResponse>() {
            @Override
            public void onResponse(Call<TransactionResponse> call, Response<TransactionResponse> response) {
                doOnPaymentResponseSuccess(response, callback);
            }

            @Override
            public void onFailure(Call<TransactionResponse> call, Throwable t) {
                doOnResponseFailure(t, callback);
            }
        });
    }


    /**
     * This method is used for Payment Using Bank Transfer BCA
     *
     * @param paymentRequest Payment Details.
     * @param callback       Transaction callback
     */
    public void paymentUsingVa(final String snapToken, BankTransferPaymentRequest paymentRequest, final TransactionCallback callback) {
        if (service == null) {
            doOnApiServiceUnAvailable(callback);
            return;
        }

        Call<TransactionResponse> call = service.paymentUsingVa(snapToken, paymentRequest);
        call.enqueue(new Callback<TransactionResponse>() {
            @Override
            public void onResponse(Call<TransactionResponse> call, Response<TransactionResponse> response) {
                doOnPaymentResponseSuccess(response, callback);
            }

            @Override
            public void onFailure(Call<TransactionResponse> call, Throwable t) {
                doOnResponseFailure(t, callback);
            }
        });
    }

    /**
     * This method is used for payment methods that using Base payment model
     * payment methods : kioson, BCA KlikPay, CIMB Click, BRI epay, Mandiri Ecash, XL Tunai, Indomaret, Akulaku
     *
     * @param snap
     * @param paymentRequest BasePaymentRequest
     * @param callback       transaction callback
     */
    public void paymentUsingBaseMethod(final String snap, BasePaymentRequest paymentRequest, final TransactionCallback callback) {
        if (service == null) {
            doOnApiServiceUnAvailable(callback);
            return;
        }

        Call<TransactionResponse> call = service.paymentUsingBaseMethod(snap, paymentRequest);
        call.enqueue(new Callback<TransactionResponse>() {
            @Override
            public void onResponse(Call<TransactionResponse> call, Response<TransactionResponse> response) {
                doOnPaymentResponseSuccess(response, callback);
            }

            @Override
            public void onFailure(Call<TransactionResponse> call, Throwable t) {
                doOnResponseFailure(t, callback);
            }
        });
    }

    /**
     * This method is used for payment using new flow of Mandiri Click Pay.
     *
     * @param snapToken
     * @param paymentRequest payment request for Mandiri Click Pay
     * @param callback       transaction callback
     */
    public void paymentUsingMandiriClickPay(final String snapToken, NewMandiriClickPayPaymentRequest paymentRequest, final TransactionCallback callback) {
        if (service == null) {
            doOnApiServiceUnAvailable(callback);
            return;
        }

        Call<TransactionResponse> call = service.paymentUsingMandiriClickPay(snapToken, paymentRequest);
        call.enqueue(new Callback<TransactionResponse>() {
            @Override
            public void onResponse(Call<TransactionResponse> call, Response<TransactionResponse> response) {
                doOnPaymentResponseSuccess(response, callback);
            }

            @Override
            public void onFailure(Call<TransactionResponse> call, Throwable t) {
                doOnResponseFailure(t, callback);
            }
        });
    }

    /**
     * This method is used for payment using Telkomsel E-Cash
     *
     * @param paymentRequest payment request for Telkomsel E-Cash
     * @param callback       transaction callback
     */
    public void paymentUsingTelkomselCash(final String snapToken, TelkomselEcashPaymentRequest paymentRequest, final TransactionCallback callback) {
        if (service == null) {
            doOnApiServiceUnAvailable(callback);
            return;
        }

        Call<TransactionResponse> call = service.paymentUsingTelkomselEcash(snapToken, paymentRequest);
        call.enqueue(new Callback<TransactionResponse>() {
            @Override
            public void onResponse(Call<TransactionResponse> call, Response<TransactionResponse> response) {
                doOnPaymentResponseSuccess(response, callback);
            }

            @Override
            public void onFailure(Call<TransactionResponse> call, Throwable t) {
                doOnResponseFailure(t, callback);
            }
        });
    }

    /**
     * This method is used for payment using Indosat Dompetku
     *
     * @param snapToken
     * @param paymentRequest payment request for Indosat Dompetku
     * @param callback       transaction callback
     */
    public void paymentUsingIndosatDompetku(final String snapToken, IndosatDompetkuPaymentRequest paymentRequest, final TransactionCallback callback) {
        if (service == null) {
            doOnApiServiceUnAvailable(callback);
            return;
        }

        Call<TransactionResponse> call = service.paymentUsingIndosatDompetku(snapToken, paymentRequest);
        call.enqueue(new Callback<TransactionResponse>() {
            @Override
            public void onResponse(Call<TransactionResponse> call, Response<TransactionResponse> response) {
                doOnPaymentResponseSuccess(response, callback);
            }

            @Override
            public void onFailure(Call<TransactionResponse> call, Throwable t) {
                doOnResponseFailure(t, callback);
            }
        });
    }

    public void paymentUsingGci(final String authenticationToken, GCIPaymentRequest paymentRequest, final TransactionCallback callback) {
        if (service == null) {
            doOnApiServiceUnAvailable(callback);
            return;
        }

        Call<TransactionResponse> call = service.paymentUsingGci(authenticationToken, paymentRequest);
        call.enqueue(new Callback<TransactionResponse>() {
            @Override
            public void onResponse(Call<TransactionResponse> call, Response<TransactionResponse> response) {
                doOnPaymentResponseSuccess(response, callback);
            }

            @Override
            public void onFailure(Call<TransactionResponse> call, Throwable t) {
                doOnResponseFailure(t, callback);
            }
        });
    }

    /**
     * This method is used for payment using Klik BCA.
     *
     * @param paymentRequest Payment details
     * @param callback       transaction callback
     */
    public void paymentUsingKlikBca(final String authenticationToken, KlikBCAPaymentRequest paymentRequest, final TransactionCallback callback) {
        if (service == null) {
            doOnApiServiceUnAvailable(callback);
            return;
        }

        Call<TransactionResponse> call = service.paymentUsingKlikBca(authenticationToken, paymentRequest);
        call.enqueue(new Callback<TransactionResponse>() {
            @Override
            public void onResponse(Call<TransactionResponse> call, Response<TransactionResponse> response) {
                doOnPaymentResponseSuccess(response, callback);
            }

            @Override
            public void onFailure(Call<TransactionResponse> call, Throwable t) {
                doOnResponseFailure(t, callback);
            }
        });
    }


    /**
     * This method is used for payment using GoPay
     *
     * @param paymentRequest
     * @param callback
     */
    public void paymentUsingGoPay(String snapToken, GoPayPaymentRequest paymentRequest, final TransactionCallback callback) {
        if (service == null) {
            doOnApiServiceUnAvailable(callback);
            return;
        }

        Call<TransactionResponse> call = service.paymentUsingGoPay(snapToken, paymentRequest);
        call.enqueue(new Callback<TransactionResponse>() {
            @Override
            public void onResponse(Call<TransactionResponse> call, Response<TransactionResponse> response) {
                doOnPaymentResponseSuccess(response, callback);
            }

            @Override
            public void onFailure(Call<TransactionResponse> call, Throwable t) {
                doOnResponseFailure(t, callback);
            }
        });
    }

    /**
     * This method is used for payment using Danamon Online
     *
     * @param snapToken      SnapToken
     * @param paymentRequest DanamonOnlinePaymentRequest
     * @param callback       TransactionCallback
     */
    public void paymentUsingDanamonOnline(String snapToken, DanamonOnlinePaymentRequest paymentRequest, final TransactionCallback callback) {
        if (service == null) {
            doOnApiServiceUnAvailable(callback);
            return;
        }

        Call<TransactionResponse> call = service.paymentUsingDanamonOnline(snapToken, paymentRequest);
        call.enqueue(new Callback<TransactionResponse>() {
            @Override
            public void onResponse(Call<TransactionResponse> call, Response<TransactionResponse> response) {
                doOnPaymentResponseSuccess(response, callback);
            }

            @Override
            public void onFailure(Call<TransactionResponse> call, Throwable t) {
                doOnResponseFailure(t, callback);
            }
        });
    }

    private void doOnPaymentResponseSuccess(Response<TransactionResponse> response, TransactionCallback callback) {
        releaseResources();
        TransactionResponse transactionResponse = response.body();

        if (transactionResponse != null) {
            String statusCode = transactionResponse.getStatusCode();

            if (!TextUtils.isEmpty(statusCode)
                    && (statusCode.equals(Constants.STATUS_CODE_200)
                    || statusCode.equals(Constants.STATUS_CODE_201))) {
                callback.onSuccess(transactionResponse);

            } else {
                if (statusCode.equals(Constants.STATUS_CODE_400)) {
                    String message;
                    if (transactionResponse.getValidationMessages() != null && !transactionResponse.getValidationMessages().isEmpty()) {
                        message = transactionResponse.getValidationMessages().get(0);
                    } else {
                        message = transactionResponse.getStatusMessage();
                    }
                    callback.onFailure(transactionResponse, message);
                } else {
                    callback.onFailure(transactionResponse, transactionResponse.getStatusMessage());
                }
            }
        } else {
            callback.onError(new Throwable(Constants.MESSAGE_ERROR_EMPTY_RESPONSE));
        }
    }

    /**
     * It will delete saved credit card number on Snap Api
     *
     * @param snapToken
     * @param maskedCard masked card number
     * @param callback   DeleteCardCallback
     */
    public void deleteCard(String snapToken, String maskedCard, final DeleteCardCallback callback) {
        if (service == null) {
            doOnApiServiceUnAvailable(callback);
            return;
        }

        Call<Void> call = service.deleteCard(snapToken, maskedCard);
        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                releaseResources();
                if (response.code() == 200 || response.code() == 201) {
                    callback.onSuccess(response.body());
                } else {
                    callback.onFailure(response.body());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                doOnResponseFailure(t, callback);
            }
        });

    }


    /**
     * it will be used to get bank bins on the snap API
     *
     * @param callback BankbinsCallback instance
     */
    public void getBankBins(final BankBinsCallback callback) {
        if (service == null) {
            doOnApiServiceUnAvailable(callback);
            return;
        }

        Call<List<BankBinsResponse>> call = service.getBankBins();
        call.enqueue(new Callback<List<BankBinsResponse>>() {
            @Override
            public void onResponse(Call<List<BankBinsResponse>> call, Response<List<BankBinsResponse>> response) {
                releaseResources();
                List<BankBinsResponse> bankBinsResponses = response.body();

                if (bankBinsResponses != null && !bankBinsResponses.isEmpty()) {
                    if (response.code() == 200 || response.code() == 201) {
                        callback.onSuccess(new ArrayList<>(bankBinsResponses));
                    } else {
                        callback.onFailure(response.message());
                    }
                } else {
                    callback.onError(new Throwable(Constants.MESSAGE_ERROR_EMPTY_RESPONSE));
                }
            }

            @Override
            public void onFailure(Call<List<BankBinsResponse>> call, Throwable t) {
                doOnResponseFailure(t, callback);
            }
        });
    }

    /**
     * Get points of given card
     *
     * @param snapToken snap token
     * @param cardToken credit card token
     * @param callback  BNI points callback instance
     */
    public void getBanksPoint(String snapToken, String cardToken, final BanksPointCallback callback) {
        if (service == null) {
            doOnApiServiceUnAvailable(callback);
            return;
        }

        Call<BanksPointResponse> call = service.getBanksPoint(snapToken, cardToken);
        call.enqueue(new Callback<BanksPointResponse>() {
            @Override
            public void onResponse(Call<BanksPointResponse> call, Response<BanksPointResponse> response) {
                releaseResources();
                BanksPointResponse bankPointResponse = response.body();

                if (bankPointResponse != null) {
                    if (bankPointResponse.getStatusCode() != null && bankPointResponse.getStatusCode().equals(Constants.STATUS_CODE_200)) {
                        callback.onSuccess(bankPointResponse);
                    } else {
                        callback.onFailure(response.message());
                    }
                } else {
                    callback.onError(new Throwable(Constants.MESSAGE_ERROR_EMPTY_RESPONSE));
                }
            }

            @Override
            public void onFailure(Call<BanksPointResponse> call, Throwable t) {
                doOnResponseFailure(t, callback);
            }
        });
    }

    /**
     * Get transaction status from Snap API
     *
     * @param snapToken
     * @param callback
     */
    public void getTransactionStatus(String snapToken, final GetTransactionStatusCallback callback) {
        if (service == null) {
            doOnApiServiceUnAvailable(callback);
            return;
        }

        Call<TransactionStatusResponse> call = service.getTransactionStatus(snapToken);
        call.enqueue(new Callback<TransactionStatusResponse>() {
            @Override
            public void onResponse(Call<TransactionStatusResponse> call, Response<TransactionStatusResponse> response) {
                releaseResources();
                TransactionStatusResponse transactionStatusResponse = response.body();

                if (transactionStatusResponse != null) {
                    if (transactionStatusResponse.getStatusCode() != null && transactionStatusResponse.getStatusCode().equals(Constants.STATUS_CODE_200)) {
                        callback.onSuccess(transactionStatusResponse);
                    } else {
                        callback.onFailure(transactionStatusResponse, response.message());
                    }
                } else {
                    callback.onError(new Throwable(Constants.MESSAGE_ERROR_EMPTY_RESPONSE));
                }
            }

            @Override
            public void onFailure(Call<TransactionStatusResponse> call, Throwable t) {
                doOnResponseFailure(t, callback);
            }
        });
    }

    void setService(SnapApiService service) {
        this.service = service;
    }
}
