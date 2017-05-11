package com.midtrans.sdk.core.api.snap;

import com.midtrans.sdk.core.MidtransCoreCallback;
import com.midtrans.sdk.core.models.snap.BaseTransactionResponse;
import com.midtrans.sdk.core.models.snap.bank.bca.BcaBankTransferPaymentRequest;
import com.midtrans.sdk.core.models.snap.bank.bca.BcaBankTransferPaymentResponse;
import com.midtrans.sdk.core.models.snap.bank.bni.BniBankTransferPaymentRequest;
import com.midtrans.sdk.core.models.snap.bank.bni.BniBankTransferPaymentResponse;
import com.midtrans.sdk.core.models.snap.bank.mandiri.MandiriBankTransferPaymentRequest;
import com.midtrans.sdk.core.models.snap.bank.mandiri.MandiriBankTransferPaymentResponse;
import com.midtrans.sdk.core.models.snap.bank.other.OtherBankTransferPaymentRequest;
import com.midtrans.sdk.core.models.snap.bank.other.OtherBankTransferPaymentResponse;
import com.midtrans.sdk.core.models.snap.bank.permata.PermataBankTransferPaymentRequest;
import com.midtrans.sdk.core.models.snap.bank.permata.PermataBankTransferPaymentResponse;
import com.midtrans.sdk.core.models.snap.bins.BankBinsResponse;
import com.midtrans.sdk.core.models.snap.card.BankPointResponse;
import com.midtrans.sdk.core.models.snap.card.CreditCardPaymentRequest;
import com.midtrans.sdk.core.models.snap.card.CreditCardPaymentResponse;
import com.midtrans.sdk.core.models.snap.conveniencestore.indomaret.IndomaretPaymentRequest;
import com.midtrans.sdk.core.models.snap.conveniencestore.indomaret.IndomaretPaymentResponse;
import com.midtrans.sdk.core.models.snap.conveniencestore.kioson.KiosonPaymentRequest;
import com.midtrans.sdk.core.models.snap.conveniencestore.kioson.KiosonPaymentResponse;
import com.midtrans.sdk.core.models.snap.ebanking.bcaklikpay.BcaKlikpayPaymentRequest;
import com.midtrans.sdk.core.models.snap.ebanking.bcaklikpay.BcaKlikpayPaymentResponse;
import com.midtrans.sdk.core.models.snap.ebanking.cimbclicks.CimbClicksPaymentRequest;
import com.midtrans.sdk.core.models.snap.ebanking.cimbclicks.CimbClicksPaymentResponse;
import com.midtrans.sdk.core.models.snap.ebanking.epaybri.EpayBriPaymentRequest;
import com.midtrans.sdk.core.models.snap.ebanking.epaybri.EpayBriPaymentResponse;
import com.midtrans.sdk.core.models.snap.ebanking.klikbca.KlikBcaPaymentRequest;
import com.midtrans.sdk.core.models.snap.ebanking.klikbca.KlikBcaPaymentResponse;
import com.midtrans.sdk.core.models.snap.ebanking.mandiriclickpay.MandiriClickpayPaymentRequest;
import com.midtrans.sdk.core.models.snap.ebanking.mandiriclickpay.MandiriClickpayPaymentResponse;
import com.midtrans.sdk.core.models.snap.ebanking.mandiriecash.MandiriECashPaymentRequest;
import com.midtrans.sdk.core.models.snap.ebanking.mandiriecash.MandiriECashPaymentResponse;
import com.midtrans.sdk.core.models.snap.ewallet.indosatdompetku.IndosatDompetkuPaymentRequest;
import com.midtrans.sdk.core.models.snap.ewallet.indosatdompetku.IndosatDompetkuPaymentResponse;
import com.midtrans.sdk.core.models.snap.ewallet.tcash.TelkomselCashPaymentRequest;
import com.midtrans.sdk.core.models.snap.ewallet.tcash.TelkomselCashPaymentResponse;
import com.midtrans.sdk.core.models.snap.ewallet.xltunai.XlTunaiPaymentRequest;
import com.midtrans.sdk.core.models.snap.ewallet.xltunai.XlTunaiPaymentResponse;
import com.midtrans.sdk.core.models.snap.gci.GiftCardPaymentRequest;
import com.midtrans.sdk.core.models.snap.gci.GiftCardPaymentResponse;
import com.midtrans.sdk.core.models.snap.transaction.SnapTransaction;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by rakawm on 10/19/16.
 */

public class SnapApiManager {
    private final SnapApi snapApi;

    public SnapApiManager(SnapApi snapApi) {
        this.snapApi = snapApi;
    }

    /**
     * Get transaction details from Snap.
     *
     * @param checkoutToken checkout token.
     * @param callback      callback to be called after API call was finished.
     */
    public void getTransactionDetails(String checkoutToken, final MidtransCoreCallback<SnapTransaction> callback) {
        Call<SnapTransaction> response = snapApi.getTransactionDetails(checkoutToken);
        response.enqueue(new Callback<SnapTransaction>() {
            @Override
            public void onResponse(Call<SnapTransaction> call, Response<SnapTransaction> response) {
                handleGetTransactionResponse(response, callback);
            }

            @Override
            public void onFailure(Call<SnapTransaction> call, Throwable throwable) {
                callback.onError(new RuntimeException("Failed to get transaction details", throwable));
            }
        });
    }

    /**
     * Start payment using credit card.
     *
     * @param checkoutToken            checkout token generated on checkout process.
     * @param creditCardPaymentRequest credit card payment request.
     * @param callback                 callback to be called after API call was finished.
     */
    public void paymentUsingCreditCard(String checkoutToken, CreditCardPaymentRequest creditCardPaymentRequest, final MidtransCoreCallback<CreditCardPaymentResponse> callback) {
        Call<CreditCardPaymentResponse> response = snapApi.paymentUsingCreditCard(checkoutToken, creditCardPaymentRequest);
        response.enqueue(new Callback<CreditCardPaymentResponse>() {
            @Override
            public void onResponse(Call<CreditCardPaymentResponse> call, Response<CreditCardPaymentResponse> response) {
                handlePaymentResponse(response, callback);
            }

            @Override
            public void onFailure(Call<CreditCardPaymentResponse> call, Throwable throwable) {
                callback.onError(new RuntimeException("Failed to pay using credit card", throwable));
            }
        });
    }

    /**
     * Start payment using bank transfer via BCA.
     *
     * @param checkoutToken                 checkout token.
     * @param bcaBankTransferPaymentRequest BCA bank transfer payment details.
     * @param callback                      callback to be called after API call was finished.
     */
    public void paymentUsingBcaBankTransfer(String checkoutToken, BcaBankTransferPaymentRequest bcaBankTransferPaymentRequest, final MidtransCoreCallback<BcaBankTransferPaymentResponse> callback) {
        Call<BcaBankTransferPaymentResponse> response = snapApi.paymentUsingBcaBankTransfer(checkoutToken, bcaBankTransferPaymentRequest);
        response.enqueue(new Callback<BcaBankTransferPaymentResponse>() {
            @Override
            public void onResponse(Call<BcaBankTransferPaymentResponse> call, Response<BcaBankTransferPaymentResponse> response) {
                handlePaymentResponse(response, callback);
            }

            @Override
            public void onFailure(Call<BcaBankTransferPaymentResponse> call, Throwable throwable) {
                callback.onError(new RuntimeException("Failed to pay using BCA VIA", throwable));
            }
        });
    }

    /**
     * Start payment using bank transfer via BNI.
     *
     * @param checkoutToken                 checkout token.
     * @param bniBankTransferPaymentRequest BNI Bank transfer payment details.
     * @param callback                      callback to be called after API call was finished.
     */
    public void paymentUsingBniBankTransfer(String checkoutToken, BniBankTransferPaymentRequest bniBankTransferPaymentRequest, final MidtransCoreCallback<BniBankTransferPaymentResponse> callback) {
        Call<BniBankTransferPaymentResponse> response = snapApi.paymentUsingBniBankTransfer(checkoutToken, bniBankTransferPaymentRequest);
        response.enqueue(new Callback<BniBankTransferPaymentResponse>() {
            @Override
            public void onResponse(Call<BniBankTransferPaymentResponse> call, Response<BniBankTransferPaymentResponse> response) {
                handlePaymentResponse(response, callback);
            }

            @Override
            public void onFailure(Call<BniBankTransferPaymentResponse> call, Throwable throwable) {
                callback.onError(new RuntimeException("Failed to pay using BNI VA", throwable));
            }
        });
    }

    /**
     * Start payment using bank transfer via Permata.
     *
     * @param checkoutToken                     checkout token.
     * @param permataBankTransferPaymentRequest Permata bank transfer payment details.
     * @param callback                          callback to be called after API call was finished.
     */
    public void paymentUsingPermataBankTransfer(String checkoutToken, PermataBankTransferPaymentRequest permataBankTransferPaymentRequest, final MidtransCoreCallback<PermataBankTransferPaymentResponse> callback) {
        Call<PermataBankTransferPaymentResponse> response = snapApi.paymentUsingPermataBankTransfer(checkoutToken, permataBankTransferPaymentRequest);
        response.enqueue(new Callback<PermataBankTransferPaymentResponse>() {
            @Override
            public void onResponse(Call<PermataBankTransferPaymentResponse> call, Response<PermataBankTransferPaymentResponse> response) {
                handlePaymentResponse(response, callback);
            }

            @Override
            public void onFailure(Call<PermataBankTransferPaymentResponse> call, Throwable throwable) {
                callback.onError(new RuntimeException("Failed to pay using BCA VIA", throwable));
            }
        });
    }

    /**
     * Start payment using bank transfer via Mandiri.
     *
     * @param checkoutToken                     checkout token.
     * @param mandiriBankTransferPaymentRequest Mandiri bank transfer payment details.
     * @param callback                          callback to be called after API call was finished.
     */
    public void paymentUsingMandiriBankTransfer(String checkoutToken, MandiriBankTransferPaymentRequest mandiriBankTransferPaymentRequest, final MidtransCoreCallback<MandiriBankTransferPaymentResponse> callback) {
        Call<MandiriBankTransferPaymentResponse> response = snapApi.paymentUsingMandiriBankTransfer(checkoutToken, mandiriBankTransferPaymentRequest);
        response.enqueue(new Callback<MandiriBankTransferPaymentResponse>() {
            @Override
            public void onResponse(Call<MandiriBankTransferPaymentResponse> call, Response<MandiriBankTransferPaymentResponse> response) {
                handlePaymentResponse(response, callback);
            }

            @Override
            public void onFailure(Call<MandiriBankTransferPaymentResponse> call, Throwable throwable) {
                callback.onError(new RuntimeException("Failed to pay using Mandiri Bill pay", throwable));
            }
        });
    }

    /**
     * Start payment using bank transfer via Other Bank .
     *
     * @param checkoutToken                   checkout token.
     * @param otherBankTransferPaymentRequest Other bank transfer payment details.
     * @param callback                        callback to be called after API call was finished.
     */
    public void paymentUsingOtherBankTransfer(String checkoutToken, OtherBankTransferPaymentRequest otherBankTransferPaymentRequest, final MidtransCoreCallback<OtherBankTransferPaymentResponse> callback) {
        Call<OtherBankTransferPaymentResponse> response = snapApi.paymentUsingOtherBankTransfer(checkoutToken, otherBankTransferPaymentRequest);
        response.enqueue(new Callback<OtherBankTransferPaymentResponse>() {
            @Override
            public void onResponse(Call<OtherBankTransferPaymentResponse> call, Response<OtherBankTransferPaymentResponse> response) {
                handlePaymentResponse(response, callback);
            }

            @Override
            public void onFailure(Call<OtherBankTransferPaymentResponse> call, Throwable throwable) {
                callback.onError(new RuntimeException("Failed to pay using other Bank Transfer payment", throwable));
            }
        });
    }

    /**
     * Start payment using BCA KlikPay.
     *
     * @param checkoutToken            checkout token.
     * @param bcaKlikpayPaymentRequest BCA KlikPay payment details.
     * @param callback                 callback to be called after API call was finished.
     */
    public void paymentUsingBcaKlikpay(String checkoutToken, BcaKlikpayPaymentRequest bcaKlikpayPaymentRequest, final MidtransCoreCallback<BcaKlikpayPaymentResponse> callback) {
        Call<BcaKlikpayPaymentResponse> response = snapApi.paymentUsingBcaKlikpay(checkoutToken, bcaKlikpayPaymentRequest);
        response.enqueue(new Callback<BcaKlikpayPaymentResponse>() {
            @Override
            public void onResponse(Call<BcaKlikpayPaymentResponse> call, Response<BcaKlikpayPaymentResponse> response) {
                handlePaymentResponse(response, callback);
            }

            @Override
            public void onFailure(Call<BcaKlikpayPaymentResponse> call, Throwable throwable) {
                callback.onError(new RuntimeException("Failed to pay using BCA KlikPay", throwable));
            }
        });
    }

    /**
     * Start payment using KlikBCA
     *
     * @param checkoutToken         checkout token.
     * @param klikBcaPaymentRequest KlikBCA payment details.
     * @param callback              callback to be called after API call was finished.
     */
    public void paymentUsingKlikBca(String checkoutToken, KlikBcaPaymentRequest klikBcaPaymentRequest, final MidtransCoreCallback<KlikBcaPaymentResponse> callback) {
        Call<KlikBcaPaymentResponse> response = snapApi.paymentUsingKlikBca(checkoutToken, klikBcaPaymentRequest);
        response.enqueue(new Callback<KlikBcaPaymentResponse>() {
            @Override
            public void onResponse(Call<KlikBcaPaymentResponse> call, Response<KlikBcaPaymentResponse> response) {
                handlePaymentResponse(response, callback);
            }

            @Override
            public void onFailure(Call<KlikBcaPaymentResponse> call, Throwable throwable) {
                callback.onError(new RuntimeException("Failed to pay using KlikBCA", throwable));
            }
        });
    }

    /**
     * Start payment using Epay BRI.
     *
     * @param checkoutToken         checkout token.
     * @param epayBriPaymentRequest Epay BRI payment details.
     * @param callback              callback to be called after API call was finished.
     */
    public void paymentUsingEpayBri(String checkoutToken, EpayBriPaymentRequest epayBriPaymentRequest, final MidtransCoreCallback<EpayBriPaymentResponse> callback) {
        Call<EpayBriPaymentResponse> response = snapApi.paymentUsingEpayBri(checkoutToken, epayBriPaymentRequest);
        response.enqueue(new Callback<EpayBriPaymentResponse>() {
            @Override
            public void onResponse(Call<EpayBriPaymentResponse> call, Response<EpayBriPaymentResponse> response) {
                handlePaymentResponse(response, callback);
            }

            @Override
            public void onFailure(Call<EpayBriPaymentResponse> call, Throwable throwable) {
                callback.onError(new RuntimeException("Failed to pay using Epay BRI", throwable));
            }
        });
    }

    /**
     * Start payment using CIMB Clicks.
     *
     * @param checkoutToken            checkout token.
     * @param cimbClicksPaymentRequest CIMB Clicks payment details.
     * @param callback                 callback to be called after API call was finished.
     */
    public void paymentUsingCimbClicks(String checkoutToken, CimbClicksPaymentRequest cimbClicksPaymentRequest, final MidtransCoreCallback<CimbClicksPaymentResponse> callback) {
        Call<CimbClicksPaymentResponse> response = snapApi.paymentUsingCimbClicks(checkoutToken, cimbClicksPaymentRequest);
        response.enqueue(new Callback<CimbClicksPaymentResponse>() {
            @Override
            public void onResponse(Call<CimbClicksPaymentResponse> call, Response<CimbClicksPaymentResponse> response) {
                handlePaymentResponse(response, callback);
            }

            @Override
            public void onFailure(Call<CimbClicksPaymentResponse> call, Throwable throwable) {
                callback.onError(new RuntimeException("Failed to pay using CIMB Clicks", throwable));
            }
        });
    }

    /**
     * Start payment using Mandiri Clickpay.
     *
     * @param checkoutToken                 checkout token.
     * @param mandiriClickpayPaymentRequest Mandiri Clickpay payment details.
     * @param callback                      callback to be called after API call was finished.
     */
    public void paymentUsingMandiriClickpay(String checkoutToken, MandiriClickpayPaymentRequest mandiriClickpayPaymentRequest, final MidtransCoreCallback<MandiriClickpayPaymentResponse> callback) {
        Call<MandiriClickpayPaymentResponse> response = snapApi.paymentUsingMandiriClickpay(checkoutToken, mandiriClickpayPaymentRequest);
        response.enqueue(new Callback<MandiriClickpayPaymentResponse>() {
            @Override
            public void onResponse(Call<MandiriClickpayPaymentResponse> call, Response<MandiriClickpayPaymentResponse> response) {
                handlePaymentResponse(response, callback);
            }

            @Override
            public void onFailure(Call<MandiriClickpayPaymentResponse> call, Throwable throwable) {
                callback.onError(new RuntimeException("Failed to pay using Mandiri Clickpay", throwable));
            }
        });
    }

    /**
     * Start payment using Mandiri E Cash.
     *
     * @param checkoutToken              checkout token.
     * @param mandiriECashPaymentRequest Mandiri E Cash payment details.
     * @param callback                   callback to be called after API call was finished.
     */
    public void paymentUsingMandiriECash(String checkoutToken, MandiriECashPaymentRequest mandiriECashPaymentRequest, final MidtransCoreCallback<MandiriECashPaymentResponse> callback) {
        Call<MandiriECashPaymentResponse> response = snapApi.paymentUsingMandiriECash(checkoutToken, mandiriECashPaymentRequest);
        response.enqueue(new Callback<MandiriECashPaymentResponse>() {
            @Override
            public void onResponse(Call<MandiriECashPaymentResponse> call, Response<MandiriECashPaymentResponse> response) {
                handlePaymentResponse(response, callback);
            }

            @Override
            public void onFailure(Call<MandiriECashPaymentResponse> call, Throwable throwable) {
                callback.onError(new RuntimeException("Failed to pay using Mandiri E Cash", throwable));
            }
        });
    }

    /**
     * Start payment using Telkomsel Cash.
     *
     * @param checkoutToken               checkout token.
     * @param telkomselCashPaymentRequest Telkomsel Cash Payment Details.
     * @param callback                    callback to be called after API call was finished.
     */
    public void paymentUsingTelkomselCash(String checkoutToken, TelkomselCashPaymentRequest telkomselCashPaymentRequest, final MidtransCoreCallback<TelkomselCashPaymentResponse> callback) {
        Call<TelkomselCashPaymentResponse> response = snapApi.paymentUsingTelkomselCash(checkoutToken, telkomselCashPaymentRequest);
        response.enqueue(new Callback<TelkomselCashPaymentResponse>() {
            @Override
            public void onResponse(Call<TelkomselCashPaymentResponse> call, Response<TelkomselCashPaymentResponse> response) {
                handlePaymentResponse(response, callback);
            }

            @Override
            public void onFailure(Call<TelkomselCashPaymentResponse> call, Throwable throwable) {
                callback.onError(new RuntimeException("Failed to pay using Telkomsel Cash", throwable));
            }
        });
    }

    /**
     * Start payment using Indosat Dompetku.
     *
     * @param checkoutToken                 checkout token.
     * @param indosatDompetkuPaymentRequest Indosat Dompetku payment details.
     * @param callback                      callback to be called after API call was finished.
     */
    public void paymentUsingIndosatDompetku(String checkoutToken, IndosatDompetkuPaymentRequest indosatDompetkuPaymentRequest, final MidtransCoreCallback<IndosatDompetkuPaymentResponse> callback) {
        Call<IndosatDompetkuPaymentResponse> response = snapApi.paymentUsingIndosatDompetku(checkoutToken, indosatDompetkuPaymentRequest);
        response.enqueue(new Callback<IndosatDompetkuPaymentResponse>() {
            @Override
            public void onResponse(Call<IndosatDompetkuPaymentResponse> call, Response<IndosatDompetkuPaymentResponse> response) {
                handlePaymentResponse(response, callback);
            }

            @Override
            public void onFailure(Call<IndosatDompetkuPaymentResponse> call, Throwable throwable) {
                callback.onError(new RuntimeException("Failed to pay using Indosat Dompetku", throwable));
            }
        });
    }

    /**
     * Start payment using XL Tunai.
     *
     * @param checkoutToken         checkout token.
     * @param xlTunaiPaymentRequest XL Tunai payment details.
     * @param callback              callback to be called after API call was finished.
     */
    public void paymentUsingXlTunai(String checkoutToken, XlTunaiPaymentRequest xlTunaiPaymentRequest, final MidtransCoreCallback<XlTunaiPaymentResponse> callback) {
        Call<XlTunaiPaymentResponse> response = snapApi.paymentUsingXlTunai(checkoutToken, xlTunaiPaymentRequest);
        response.enqueue(new Callback<XlTunaiPaymentResponse>() {
            @Override
            public void onResponse(Call<XlTunaiPaymentResponse> call, Response<XlTunaiPaymentResponse> response) {
                handlePaymentResponse(response, callback);
            }

            @Override
            public void onFailure(Call<XlTunaiPaymentResponse> call, Throwable throwable) {
                callback.onError(new RuntimeException("Failed to pay using XL Tunai", throwable));
            }
        });
    }

    /**
     * Start payment using Indomaret.
     *
     * @param checkoutToken           checkout token.
     * @param indomaretPaymentRequest Indomaret Payment Details.
     * @param callback                callback to be called after API call was finished.
     */
    public void paymentUsingIndomaret(String checkoutToken, IndomaretPaymentRequest indomaretPaymentRequest, final MidtransCoreCallback<IndomaretPaymentResponse> callback) {
        Call<IndomaretPaymentResponse> response = snapApi.paymentUsingIndomaret(checkoutToken, indomaretPaymentRequest);
        response.enqueue(new Callback<IndomaretPaymentResponse>() {
            @Override
            public void onResponse(Call<IndomaretPaymentResponse> call, Response<IndomaretPaymentResponse> response) {
                handlePaymentResponse(response, callback);
            }

            @Override
            public void onFailure(Call<IndomaretPaymentResponse> call, Throwable throwable) {
                callback.onError(new RuntimeException("Failed to pay using Indomaret.", throwable));
            }
        });
    }

    /**
     * Start payment using Kioson.
     *
     * @param checkoutToken        checkout token.
     * @param kiosonPaymentRequest Kioson payment details.
     * @param callback             callback to be called after API call was finished.
     */
    public void paymentUsingKioson(String checkoutToken, KiosonPaymentRequest kiosonPaymentRequest, final MidtransCoreCallback<KiosonPaymentResponse> callback) {
        Call<KiosonPaymentResponse> response = snapApi.paymentUsingKioson(checkoutToken, kiosonPaymentRequest);
        response.enqueue(new Callback<KiosonPaymentResponse>() {
            @Override
            public void onResponse(Call<KiosonPaymentResponse> call, Response<KiosonPaymentResponse> response) {
                handlePaymentResponse(response, callback);
            }

            @Override
            public void onFailure(Call<KiosonPaymentResponse> call, Throwable throwable) {
                callback.onError(new RuntimeException("Failed to pay using Kioson.", throwable));
            }
        });
    }

    /**
     * Start payment using Gift Card.
     *
     * @param checkoutToken          checkout token.
     * @param giftCardPaymentRequest Gift Card payment details.
     * @param callback               callback to be called after API call was finished.
     */
    public void paymentUsingGiftCard(String checkoutToken, GiftCardPaymentRequest giftCardPaymentRequest, final MidtransCoreCallback<GiftCardPaymentResponse> callback) {
        Call<GiftCardPaymentResponse> response = snapApi.paymentUsingGiftCard(checkoutToken, giftCardPaymentRequest);
        response.enqueue(new Callback<GiftCardPaymentResponse>() {
            @Override
            public void onResponse(Call<GiftCardPaymentResponse> call, Response<GiftCardPaymentResponse> response) {
                handlePaymentResponse(response, callback);
            }

            @Override
            public void onFailure(Call<GiftCardPaymentResponse> call, Throwable throwable) {
                callback.onError(new RuntimeException("Failed to pay using Gift Card.", throwable));
            }
        });
    }

    /**
     * Start delete card from token storage.
     *
     * @param checkoutToken checkout token.
     * @param maskedCard masked card.
     * @param callback callback to be called after process was finished.
     */
    public void deleteCard(String checkoutToken, String maskedCard, final MidtransCoreCallback<Void> callback) {
        Call<Void> response = snapApi.deleteCard(checkoutToken, maskedCard);
        response.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                handleDeleteCardResponse(response, callback);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable throwable) {
                callback.onError(new RuntimeException("Failed to delete card.", throwable));
            }
        });
    }

    /**
     * Start getting bank bins from Snap.
     *
     * @param callback callback to be called after API call was finished.
     */
    public void getBankBins(final MidtransCoreCallback<List<BankBinsResponse>> callback) {
        Call<List<BankBinsResponse>> response = snapApi.getBankBins();
        response.enqueue(new Callback<List<BankBinsResponse>>() {
            @Override
            public void onResponse(Call<List<BankBinsResponse>> call, Response<List<BankBinsResponse>> response) {
                handleGetBankBinsResponse(response, callback);
            }

            @Override
            public void onFailure(Call<List<BankBinsResponse>> call, Throwable throwable) {
                callback.onError(new RuntimeException("Failed to get bank bins", throwable));
            }
        });
    }

    /**
     * Get bank point.
     *
     * @param checkoutToken checkout token.
     * @param cardToken     card token.
     * @param callback      callback to be called after the process was finished.
     */
    public void getBankPoint(String checkoutToken, String cardToken, final MidtransCoreCallback<BankPointResponse> callback) {
        Call<BankPointResponse> response = snapApi.getBankPoint(checkoutToken, cardToken);
        response.enqueue(new Callback<BankPointResponse>() {
            @Override
            public void onResponse(Call<BankPointResponse> call, Response<BankPointResponse> response) {
                handleGetBankPointResponse(response, callback);
            }

            @Override
            public void onFailure(Call<BankPointResponse> call, Throwable throwable) {
                callback.onError(new RuntimeException("Failed to get bank point", throwable));
            }
        });
    }

    private void handleGetBankPointResponse(Response<BankPointResponse> response, MidtransCoreCallback<BankPointResponse> callback) {
        if (response.isSuccessful()
                && response.code() == 200
                && response.body().statusCode.equalsIgnoreCase("200")) {
            callback.onSuccess(response.body());
        } else {
            callback.onFailure(response.body());
        }
    }

    private <T extends BaseTransactionResponse> void handlePaymentResponse(Response<T> response, MidtransCoreCallback<T> callback) {
        if (response.isSuccessful()
                && response.code() == 200
                && (response.body().statusCode.equalsIgnoreCase("200")
                || response.body().statusCode.equalsIgnoreCase("201"))) {
            callback.onSuccess(response.body());
        } else {
            callback.onFailure(response.body());
        }
    }

    private void handleGetTransactionResponse(Response<SnapTransaction> response, MidtransCoreCallback<SnapTransaction> callback) {
        if (response.isSuccessful()
                && response.code() == 200) {
            callback.onSuccess(response.body());
        } else {
            callback.onFailure(response.body());
        }
    }

    private void handleGetBankBinsResponse(Response<List<BankBinsResponse>> response, MidtransCoreCallback<List<BankBinsResponse>> callback) {
        if (response.isSuccessful()
                && response.code() == 200) {
            callback.onSuccess(response.body());
        } else {
            callback.onFailure(response.body());
        }
    }

    private void handleDeleteCardResponse(Response<Void> response, MidtransCoreCallback<Void> callback) {
        if (response.isSuccessful()) {
            callback.onSuccess(response.body());
        } else {
            callback.onFailure(response.body());
        }
    }
}
