package id.co.veritrans.sdk.core;

import android.os.Build;
import android.text.TextUtils;

import java.security.cert.CertPathValidatorException;

import javax.net.ssl.SSLHandshakeException;

import id.co.veritrans.sdk.BuildConfig;
import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.analytics.MixpanelEvent;
import id.co.veritrans.sdk.analytics.MixpanelProperties;
import id.co.veritrans.sdk.eventbus.bus.VeritransBusProvider;
import id.co.veritrans.sdk.eventbus.events.AuthenticationEvent;
import id.co.veritrans.sdk.eventbus.events.CardRegistrationFailedEvent;
import id.co.veritrans.sdk.eventbus.events.CardRegistrationSuccessEvent;
import id.co.veritrans.sdk.eventbus.events.DeleteCardFailedEvent;
import id.co.veritrans.sdk.eventbus.events.DeleteCardSuccessEvent;
import id.co.veritrans.sdk.eventbus.events.GeneralErrorEvent;
import id.co.veritrans.sdk.eventbus.events.GetCardFailedEvent;
import id.co.veritrans.sdk.eventbus.events.GetCardsSuccessEvent;
import id.co.veritrans.sdk.eventbus.events.GetOfferFailedEvent;
import id.co.veritrans.sdk.eventbus.events.GetOfferSuccessEvent;
import id.co.veritrans.sdk.eventbus.events.GetTokenFailedEvent;
import id.co.veritrans.sdk.eventbus.events.GetTokenSuccessEvent;
import id.co.veritrans.sdk.eventbus.events.RegisterCardFailedEvent;
import id.co.veritrans.sdk.eventbus.events.RegisterCardSuccessEvent;
import id.co.veritrans.sdk.eventbus.events.SSLErrorEvent;
import id.co.veritrans.sdk.eventbus.events.SaveCardFailedEvent;
import id.co.veritrans.sdk.eventbus.events.SaveCardSuccessEvent;
import id.co.veritrans.sdk.eventbus.events.TransactionFailedEvent;
import id.co.veritrans.sdk.eventbus.events.TransactionStatusSuccessEvent;
import id.co.veritrans.sdk.eventbus.events.TransactionSuccessEvent;
import id.co.veritrans.sdk.models.AuthModel;
import id.co.veritrans.sdk.models.BBMMoneyRequestModel;
import id.co.veritrans.sdk.models.BCABankTransfer;
import id.co.veritrans.sdk.models.BCAKlikPayModel;
import id.co.veritrans.sdk.models.CIMBClickPayModel;
import id.co.veritrans.sdk.models.CardRegistrationResponse;
import id.co.veritrans.sdk.models.CardResponse;
import id.co.veritrans.sdk.models.CardTokenRequest;
import id.co.veritrans.sdk.models.CardTransfer;
import id.co.veritrans.sdk.models.DeleteCardResponse;
import id.co.veritrans.sdk.models.EpayBriTransfer;
import id.co.veritrans.sdk.models.GetOffersResponseModel;
import id.co.veritrans.sdk.models.IndomaretRequestModel;
import id.co.veritrans.sdk.models.IndosatDompetkuRequest;
import id.co.veritrans.sdk.models.MandiriBillPayTransferModel;
import id.co.veritrans.sdk.models.MandiriClickPayRequestModel;
import id.co.veritrans.sdk.models.MandiriECashModel;
import id.co.veritrans.sdk.models.PermataBankTransfer;
import id.co.veritrans.sdk.models.RegisterCardResponse;
import id.co.veritrans.sdk.models.SaveCardRequest;
import id.co.veritrans.sdk.models.SaveCardResponse;
import id.co.veritrans.sdk.models.TokenDetailsResponse;
import id.co.veritrans.sdk.models.TransactionResponse;
import id.co.veritrans.sdk.models.TransactionStatusResponse;
import rx.Observable;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * protected helper class , It contains an static methods which are used to execute the transaction.
 * <p>
 * Created by shivam on 10/29/15.
 */
class TransactionManager {
    // Event Name
    private static final String KEY_TRANSACTION_SUCCESS = "Transaction Success";
    private static final String KEY_TRANSACTION_FAILED = "Transaction Failed";
    private static final String KEY_TOKENIZE_SUCCESS = "Tokenize Success";
    private static final String KEY_TOKENIZE_FAILED = "Tokenize Failed";

    // Payment Name
    private static final String PAYMENT_TYPE_CIMB_CLICK = "cimb_click";
    private static final String PAYMENT_TYPE_BCA_KLIKPAY = "bca_klikpay";
    private static final String PAYMENT_TYPE_MANDIRI_CLICKPAY = "mandiri_clickpay";
    private static final String PAYMENT_TYPE_MANDIRI_ECASH = "mandiri_ecash";
    private static final String PAYMENT_TYPE_BANK_TRANSFER = "bank_transfer";
    private static final String PAYMENT_TYPE_CREDIT_CARD = "cc";
    private static final String PAYMENT_TYPE_BRI_EPAY = "bri_epay";
    private static final String PAYMENT_TYPE_BBM_MONEY = "bbm_money";
    private static final String PAYMENT_TYPE_INDOSAT_DOMPETKU = "indosat_dompetku";
    private static final String PAYMENT_TYPE_INDOMARET = "indomaret";

    // Bank transfer type
    private static final String BANK_PERMATA = "permata";
    private static final String BANK_BCA = "bca";
    private static final String BANK_MANDIRI = "mandiri";

    // Platform properties
    private static final String PLATFORM = "Android";

    /**
     * It will execute API call to get token from Veritrans that can be used later.
     *
     * @param cardNumber   credit card number
     * @param cardCvv      credit card CVV number
     * @param cardExpMonth credit card expired month
     * @param cardExpYear  credit card expired year
     */
    public static void cardRegistration(String cardNumber,
                                        String cardCvv,
                                        String cardExpMonth,
                                        String cardExpYear) {
        final VeritransSDK veritransSDK = VeritransSDK.getVeritransSDK();
        if (veritransSDK != null) {
            PaymentAPI paymentAPI = VeritransRestAdapter.getApiClient(true);
            if (paymentAPI != null) {
                paymentAPI.registerCard(cardNumber, cardCvv, cardExpMonth, cardExpYear, veritransSDK.getClientKey())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<CardRegistrationResponse>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                Logger.e("error while getting token : ", "" + e.getMessage());

                                if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                                    VeritransBusProvider.getInstance().post(new SSLErrorEvent());
                                    Logger.i("Error in SSL Certificate. " + e.getMessage());
                                } else {
                                    VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage()));
                                    Logger.i("General error occurred " + e.getMessage());
                                }

                                releaseResources();
                            }

                            @Override
                            public void onNext(CardRegistrationResponse cardRegistrationResponse) {
                                if (cardRegistrationResponse != null) {
                                    if (cardRegistrationResponse.getStatusCode().equals(veritransSDK.getContext().getString(R.string.success_code_200))) {
                                        VeritransBusProvider.getInstance().post(new CardRegistrationSuccessEvent(cardRegistrationResponse));
                                    } else {
                                        VeritransBusProvider.getInstance().post(new CardRegistrationFailedEvent(cardRegistrationResponse.getStatusMessage(), cardRegistrationResponse));
                                    }
                                } else {
                                    VeritransBusProvider.getInstance().post(new TransactionFailedEvent(veritransSDK.getContext().getString(R.string.error_empty_response), null));
                                    Logger.e(veritransSDK.getContext().getString(R.string.error_empty_response));
                                }

                                releaseResources();
                            }
                        });
            } else {
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_empty_response)));
                Logger.e(veritransSDK.getContext().getString(R.string.error_unable_to_connect));
                releaseResources();
            }
        } else {
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(Constants.ERROR_SDK_IS_NOT_INITIALIZED));
            Logger.e(Constants.ERROR_SDK_IS_NOT_INITIALIZED);
            releaseResources();
        }
    }

    /**
     * It will execute API call to save token to merchant server that can be used
     * to pay later.
     *
     * @param cardTokenRequest card token request model
     * @param userId           user identifier
     */
    public static void registerCard(CardTokenRequest cardTokenRequest, final String userId) {
        final VeritransSDK veritransSDK = VeritransSDK.getVeritransSDK();
        if (veritransSDK != null) {
            final String merchantToken = veritransSDK.readAuthenticationToken();
            PaymentAPI apiInterface = VeritransRestAdapter.getApiClient(true);
            if (apiInterface != null) {
                apiInterface.registerCard(cardTokenRequest.getCardNumber(), cardTokenRequest.getCardExpiryMonth(), cardTokenRequest.getCardExpiryYear(), cardTokenRequest.getClientKey())
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<RegisterCardResponse>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                Logger.e("error while getting token : ", "" + e.getMessage());
                                if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                                    VeritransBusProvider.getInstance().post(new SSLErrorEvent());
                                    Logger.i("Error in SSL Certificate. " + e.getMessage());
                                } else {
                                    VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage()));
                                    Logger.i("General error occurred " + e.getMessage());
                                }
                                releaseResources();
                            }

                            @Override
                            public void onNext(RegisterCardResponse registerCardResponse) {
                                if (registerCardResponse != null) {
                                    if (veritransSDK.isLogEnabled()) {
                                        displayResponse(registerCardResponse);
                                    }
                                    if (registerCardResponse.getStatusCode().trim().equalsIgnoreCase(veritransSDK.getContext().getString(R.string.success_code_200))) {
                                        registerCardResponse.setUserId(userId);
                                        PaymentAPI apiInterface = VeritransRestAdapter.getMerchantApiClient(true);
                                        if (apiInterface != null) {
                                            apiInterface.registerCard(merchantToken, registerCardResponse)
                                                    .subscribeOn(Schedulers.io())
                                                    .observeOn(AndroidSchedulers.mainThread())
                                                    .subscribe(new Observer<CardResponse>() {
                                                        @Override
                                                        public void onCompleted() {

                                                        }

                                                        @Override
                                                        public void onError(Throwable e) {
                                                            Logger.e("CardSubscriber", e.getMessage());
                                                        }

                                                        @Override
                                                        public void onNext(CardResponse cardResponse) {
                                                        }
                                                    });

                                        }
                                        VeritransBusProvider.getInstance().post(new RegisterCardSuccessEvent(registerCardResponse));
                                    } else {
                                        if (!TextUtils.isEmpty(registerCardResponse.getStatusMessage())) {
                                            VeritransBusProvider.getInstance().post(new RegisterCardFailedEvent(registerCardResponse.getStatusMessage(), registerCardResponse));
                                        } else {
                                            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_empty_response)));
                                        }
                                    }

                                } else {
                                    VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_empty_response)));
                                    Logger.e(veritransSDK.getContext().getString(R.string.error_empty_response));
                                }
                                releaseResources();
                            }
                        });
            } else {
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_empty_response)));
                Logger.e(veritransSDK.getContext().getString(R.string.error_unable_to_connect));
                releaseResources();
            }

        } else {
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(Constants.ERROR_SDK_IS_NOT_INITIALIZED));
            Logger.e(Constants.ERROR_SDK_IS_NOT_INITIALIZED);
            releaseResources();
        }

    }


    /**
     * It will execute an api call to get token from server, and after completion of request it
     * will </p> call appropriate method using registered {@link GetTokenSuccessEvent}.
     *
     * @param cardTokenRequest information about credit card.
     */
    public static void getToken(CardTokenRequest cardTokenRequest) {
        final VeritransSDK veritransSDK = VeritransSDK.getVeritransSDK();
        final long start = System.currentTimeMillis();

        if (veritransSDK != null) {
            PaymentAPI apiInterface = VeritransRestAdapter.getApiClient(true);
            if (apiInterface != null) {
                Observable<TokenDetailsResponse> observable;
                if (cardTokenRequest.isTwoClick()) {
                    if (cardTokenRequest.isInstalment()) {
                        observable = apiInterface.getTokenInstalmentOfferTwoClick(
                                cardTokenRequest.getCardCVV(),
                                cardTokenRequest.getSavedTokenId(),
                                cardTokenRequest.isTwoClick(),
                                cardTokenRequest.isSecure(),
                                cardTokenRequest.getGrossAmount(),
                                cardTokenRequest.getBank(),
                                cardTokenRequest.getClientKey(),
                                cardTokenRequest.isInstalment(),
                                cardTokenRequest.getFormattedInstalmentTerm());
                    } else {
                        observable = apiInterface.getTokenTwoClick(
                                cardTokenRequest.getCardCVV(),
                                cardTokenRequest.getSavedTokenId(),
                                cardTokenRequest.isTwoClick(),
                                cardTokenRequest.isSecure(),
                                cardTokenRequest.getGrossAmount(),
                                cardTokenRequest.getBank(),
                                cardTokenRequest.getClientKey());
                    }

                } else {
                    if (cardTokenRequest.isInstalment()) {
                        observable = apiInterface.get3DSTokenInstalmentOffers(cardTokenRequest.getCardNumber(),
                                cardTokenRequest.getCardCVV(),
                                cardTokenRequest.getCardExpiryMonth(), cardTokenRequest
                                        .getCardExpiryYear(),
                                cardTokenRequest.getClientKey(),
                                cardTokenRequest.getBank(),
                                cardTokenRequest.isSecure(),
                                cardTokenRequest.isTwoClick(),
                                cardTokenRequest.getGrossAmount(),
                                cardTokenRequest.isInstalment(),
                                cardTokenRequest.getFormattedInstalmentTerm());
                    } else {
                        observable = apiInterface.get3DSToken(cardTokenRequest.getCardNumber(),
                                cardTokenRequest.getCardCVV(),
                                cardTokenRequest.getCardExpiryMonth(),
                                cardTokenRequest.getCardExpiryYear(),
                                cardTokenRequest.getClientKey(),
                                cardTokenRequest.getBank(),
                                cardTokenRequest.isSecure(),
                                cardTokenRequest.isTwoClick(),
                                cardTokenRequest.getGrossAmount());
                    }

                }

                observable.subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<TokenDetailsResponse>() {

                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                long end = System.currentTimeMillis();

                                Logger.e("error while getting token : ", "" + e.getMessage());
                                if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                                    VeritransBusProvider.getInstance().post(new SSLErrorEvent());
                                    Logger.i("Error in SSL Certificate. " + e.getMessage());
                                } else {
                                    VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage()));
                                    Logger.i("General error occurred " + e.getMessage());
                                }

                                // Track Mixpanel event
                                trackMixpanel(KEY_TOKENIZE_FAILED, PAYMENT_TYPE_CREDIT_CARD, end - start, e.getMessage());

                                releaseResources();
                            }

                            @Override
                            public void onNext(TokenDetailsResponse tokenDetailsResponse) {
                                long end = System.currentTimeMillis();

                                if (tokenDetailsResponse != null) {
                                    if (veritransSDK.isLogEnabled()) {
                                        displayTokenResponse(tokenDetailsResponse);
                                    }

                                    if (tokenDetailsResponse.getStatusCode().trim().equalsIgnoreCase(veritransSDK.getContext().getString(R.string.success_code_200))) {
                                        VeritransBusProvider.getInstance().post(new GetTokenSuccessEvent(tokenDetailsResponse));

                                        // Track Mixpanel event
                                        trackMixpanel(KEY_TOKENIZE_SUCCESS, PAYMENT_TYPE_CREDIT_CARD, end - start);
                                    } else {
                                        if (!TextUtils.isEmpty(tokenDetailsResponse.getStatusMessage())) {
                                            VeritransBusProvider.getInstance().post(new GetTokenFailedEvent(
                                                    tokenDetailsResponse.getStatusMessage(),
                                                    tokenDetailsResponse));

                                            // Track Mixpanel event
                                            trackMixpanel(KEY_TOKENIZE_FAILED, PAYMENT_TYPE_CREDIT_CARD, end - start, tokenDetailsResponse.getStatusMessage());
                                        } else {
                                            VeritransBusProvider.getInstance().post(new GetTokenFailedEvent(
                                                    veritransSDK.getContext().getString(R.string.error_empty_response),
                                                    tokenDetailsResponse
                                            ));
                                        }

                                    }

                                } else {
                                    VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_empty_response)));
                                    Logger.e(veritransSDK.getContext().getString(R.string.error_empty_response));
                                }
                                releaseResources();
                            }
                        });

            } else {
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_unable_to_connect)));
                Logger.e(veritransSDK.getContext().getString(R.string.error_unable_to_connect));
                releaseResources();
            }

        } else {
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(Constants.ERROR_SDK_IS_NOT_INITIALIZED));
            Logger.e(Constants.ERROR_SDK_IS_NOT_INITIALIZED);
            releaseResources();
        }
    }

    /**
     * It will execute an api call to perform transaction using permata bank, and after
     * completion of request it will </p> call appropriate method using registered {@link TransactionSuccessEvent}
     * or {@link TransactionFailedEvent}.
     *
     * @param permataBankTransfer information required perform transaction using permata bank
     */
    public static void paymentUsingPermataBank(final PermataBankTransfer permataBankTransfer) {
        final VeritransSDK veritransSDK = VeritransSDK.getVeritransSDK();
        final long start = System.currentTimeMillis();

        if (veritransSDK != null) {
            PaymentAPI apiInterface = VeritransRestAdapter.getMerchantApiClient(true);
            if (apiInterface != null) {
                String merchantToken = veritransSDK.readAuthenticationToken();
                Logger.i("merchantToken:" + merchantToken);
                if (merchantToken != null) {
                    apiInterface.paymentUsingPermataBank(merchantToken, permataBankTransfer)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<TransactionResponse>() {

                                @Override
                                public void onCompleted() {
                                }

                                @Override
                                public void onError(Throwable e) {
                                    long end = System.currentTimeMillis();

                                    if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                                        VeritransBusProvider.getInstance().post(new SSLErrorEvent());
                                        Logger.i("Error in SSL Certificate. " + e.getMessage());
                                    } else {
                                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage()));
                                        Logger.i("General error occurred " + e.getMessage());
                                    }

                                    // Track Mixpanel event
                                    trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BANK_TRANSFER, BANK_PERMATA, end - start, e.getMessage());
                                    releaseResources();
                                }

                                @Override
                                public void onNext(TransactionResponse permataBankTransferResponse) {
                                    long end = System.currentTimeMillis();

                                    if (permataBankTransferResponse != null) {
                                        if (veritransSDK.isLogEnabled()) {
                                            displayResponse(permataBankTransferResponse);
                                        }
                                        if (permataBankTransferResponse.getStatusCode().trim().equalsIgnoreCase(veritransSDK.getContext().getString(R.string.success_code_200))
                                                || permataBankTransferResponse.getStatusCode().trim().equalsIgnoreCase(veritransSDK.getContext().getString(R.string.success_code_201))) {
                                            VeritransBusProvider.getInstance().post(new TransactionSuccessEvent(permataBankTransferResponse));

                                            // Track Mixpanel event
                                            trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_BANK_TRANSFER, BANK_PERMATA, end - start);

                                        } else {
                                            VeritransBusProvider.getInstance().post(new TransactionFailedEvent(permataBankTransferResponse.getStatusMessage(), permataBankTransferResponse));

                                            // Track Mixpanel event
                                            trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BANK_TRANSFER, BANK_PERMATA, end - start, permataBankTransferResponse.getStatusCode());
                                        }

                                    } else {
                                        VeritransBusProvider.getInstance().post(new TransactionFailedEvent(veritransSDK.getContext().getString(R.string.error_empty_response), null));
                                        Logger.e(veritransSDK.getContext().getString(R.string.error_empty_response));
                                    }
                                    releaseResources();
                                }
                            });
                } else {
                    Logger.e(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied));
                    VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied)));
                    releaseResources();
                }
            } else {
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_unable_to_connect)));
                Logger.e(veritransSDK.getContext().getString(R.string.error_unable_to_connect));
                releaseResources();
            }

        } else {
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(Constants.ERROR_SDK_IS_NOT_INITIALIZED));
            Logger.e(Constants.ERROR_SDK_IS_NOT_INITIALIZED);
            releaseResources();
        }
    }

    /**
     * it will execute an api call to perform transaction using permata bank, and after
     * completion of request it will </p> call appropriate method using registered {@link TransactionSuccessEvent} or
     * {@link TransactionFailedEvent}.
     *
     * @param bcaBankTransfer information required perform transaction using BCA bank
     */
    public static void paymentUsingBCATransfer(final BCABankTransfer bcaBankTransfer) {
        final VeritransSDK veritransSDK = VeritransSDK.getVeritransSDK();
        final long start = System.currentTimeMillis();

        if (veritransSDK != null) {
            PaymentAPI apiInterface = VeritransRestAdapter.getMerchantApiClient(true);

            if (apiInterface != null) {
                String merchantToken = veritransSDK.readAuthenticationToken();
                Logger.i("merchantToken:" + merchantToken);
                if (merchantToken != null) {
                    apiInterface.paymentUsingBCAVA(merchantToken, bcaBankTransfer)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<TransactionResponse>() {
                                @Override
                                public void onCompleted() {
                                }

                                @Override
                                public void onError(Throwable e) {
                                    long end = System.currentTimeMillis();
                                    if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                                        VeritransBusProvider.getInstance().post(new SSLErrorEvent());
                                        Logger.i("Error in SSL Certificate. " + e.getMessage());
                                    } else {
                                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage()));
                                        Logger.i("General error occurred " + e.getMessage());
                                    }

                                    //Track Mixpanel event
                                    trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BANK_TRANSFER, BANK_BCA, end - start, e.getMessage());
                                    releaseResources();
                                }

                                @Override
                                public void onNext(TransactionResponse bcaBankTransferResponse) {
                                    long end = System.currentTimeMillis();

                                    if (bcaBankTransferResponse != null) {
                                        if (veritransSDK.isLogEnabled()) {
                                            displayResponse(bcaBankTransferResponse);
                                        }

                                        if (bcaBankTransferResponse.getStatusCode().trim().equalsIgnoreCase(veritransSDK.getContext().getString(R.string.success_code_200))
                                                || bcaBankTransferResponse.getStatusCode().trim().equalsIgnoreCase(veritransSDK.getContext().getString(R.string.success_code_201))) {
                                            VeritransBusProvider.getInstance().post(new TransactionSuccessEvent(bcaBankTransferResponse));

                                            // Track Mixpanel Event
                                            trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_BANK_TRANSFER, BANK_BCA, end - start);
                                        } else {
                                            VeritransBusProvider.getInstance().post(new TransactionFailedEvent(bcaBankTransferResponse.getStatusMessage(), bcaBankTransferResponse));

                                            // Track Mixpanel Event
                                            trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BANK_TRANSFER, BANK_BCA, end - start, bcaBankTransferResponse.getStatusMessage());
                                        }

                                    } else {
                                        VeritransBusProvider.getInstance().post(new TransactionFailedEvent(veritransSDK.getContext().getString(R.string.error_empty_response), null));
                                        Logger.e(veritransSDK.getContext().getString(R.string.error_empty_response));
                                    }
                                    releaseResources();
                                }
                            });
                } else {
                    Logger.e(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied));
                    VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied)));
                    releaseResources();
                }
            } else {
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_unable_to_connect)));
                Logger.e(veritransSDK.getContext().getString(R.string.error_unable_to_connect));
                releaseResources();
            }

        } else {
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(Constants.ERROR_SDK_IS_NOT_INITIALIZED));
            Logger.e(Constants.ERROR_SDK_IS_NOT_INITIALIZED);
            releaseResources();
        }
    }

    /**
     * it will execute an api call to perform transaction using credit card, and after
     * completion of request it will </p> call appropriate method using registered {@link TransactionSuccessEvent} or
     * {@link TransactionFailedEvent}.
     *
     * @param cardTransfer information required perform transaction using credit card
     */
    public static void paymentUsingCard(CardTransfer cardTransfer) {
        final VeritransSDK veritransSDK = VeritransSDK.getVeritransSDK();
        final long start = System.currentTimeMillis();

        if (veritransSDK != null) {
            PaymentAPI apiInterface = VeritransRestAdapter.getMerchantApiClient(true);
            if (apiInterface != null) {
                String merchantToken = veritransSDK.readAuthenticationToken();
                Logger.i("merchantToken:" + merchantToken);
                if (merchantToken != null) {
                    apiInterface.paymentUsingCard(merchantToken, cardTransfer)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<TransactionResponse>() {
                                @Override
                                public void onCompleted() {
                                }

                                @Override
                                public void onError(Throwable e) {
                                    long end = System.currentTimeMillis();

                                    if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                                        VeritransBusProvider.getInstance().post(new SSLErrorEvent());
                                        Logger.i("Error in SSL Certificate. " + e.getMessage());
                                    } else {
                                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage()));
                                        Logger.i("General error occurred " + e.getMessage());
                                    }

                                    // Track Mixpanel event
                                    trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_CREDIT_CARD, end - start, e.getMessage());

                                    releaseResources();
                                }

                                @Override
                                public void onNext(TransactionResponse cardPaymentResponse) {
                                    long end = System.currentTimeMillis();
                                    if (cardPaymentResponse != null) {

                                        if (cardPaymentResponse.getStatusCode().trim().equalsIgnoreCase(veritransSDK.getContext().getString(R.string.success_code_200))
                                                || cardPaymentResponse.getStatusCode().trim().equalsIgnoreCase(veritransSDK.getContext().getString(R.string.success_code_201))) {
                                            VeritransBusProvider.getInstance().post(new TransactionSuccessEvent(cardPaymentResponse));

                                            // Track Mixpanel event
                                            trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_CREDIT_CARD, end - start);
                                        } else {
                                            VeritransBusProvider.getInstance().post(new TransactionFailedEvent(cardPaymentResponse.getStatusMessage(), cardPaymentResponse));

                                            // Track Mixpanel event
                                            trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_CREDIT_CARD, end - start, cardPaymentResponse.getStatusMessage());
                                        }
                                    } else {
                                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_empty_response)));
                                    }

                                    releaseResources();
                                }

                            });
                } else {
                    VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied)));
                    Logger.e(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied));
                    releaseResources();
                }
            }

        } else {
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(Constants.ERROR_SDK_IS_NOT_INITIALIZED));
            Logger.e(Constants.ERROR_SDK_IS_NOT_INITIALIZED);
            releaseResources();
        }
    }

    /**
     * it will execute an api call to perform transaction using mandiri click pay, and after
     * completion of request it will </p> call appropriate method using registered {@link TransactionSuccessEvent} or
     * {@link TransactionFailedEvent}.
     *
     * @param mandiriClickPayRequestModel information required perform transaction using mandiri click pay.
     */
    public static void paymentUsingMandiriClickPay(final MandiriClickPayRequestModel mandiriClickPayRequestModel) {
        final VeritransSDK veritransSDK = VeritransSDK.getVeritransSDK();
        final long start = System.currentTimeMillis();

        if (veritransSDK != null) {
            PaymentAPI apiInterface = VeritransRestAdapter.getMerchantApiClient(true);
            if (apiInterface != null) {
                String merchantToken = veritransSDK.readAuthenticationToken();
                Logger.i("merchantToken:" + merchantToken);
                if (merchantToken != null) {
                    apiInterface.paymentUsingMandiriClickPay(merchantToken, mandiriClickPayRequestModel)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<TransactionResponse>() {
                                @Override
                                public void onCompleted() {
                                }

                                @Override
                                public void onError(Throwable e) {
                                    long end = System.currentTimeMillis();

                                    if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                                        VeritransBusProvider.getInstance().post(new SSLErrorEvent());
                                        Logger.i("Error in SSL Certificate. " + e.getMessage());
                                    } else {
                                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage()));
                                        Logger.i("General error occurred " + e.getMessage());
                                    }

                                    // Track Mixpanel event
                                    trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_MANDIRI_CLICKPAY, end - start, e.getMessage());

                                    releaseResources();
                                }

                                @Override
                                public void onNext(TransactionResponse mandiriTransferResponse) {
                                    long end = System.currentTimeMillis();

                                    if (mandiriTransferResponse != null) {
                                        if (veritransSDK.isLogEnabled()) {
                                            displayResponse(mandiriTransferResponse);
                                        }
                                        if (mandiriTransferResponse.getStatusCode().trim().equalsIgnoreCase(veritransSDK.getContext().getString(R.string.success_code_200))
                                                || mandiriTransferResponse.getStatusCode().trim().equalsIgnoreCase(veritransSDK.getContext().getString(R.string.success_code_201))) {
                                            VeritransBusProvider.getInstance().post(new TransactionSuccessEvent(mandiriTransferResponse));

                                            // Track Mixpanel event
                                            trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_MANDIRI_CLICKPAY, end - start);
                                        } else {
                                            VeritransBusProvider.getInstance().post(new TransactionFailedEvent(mandiriTransferResponse.getStatusMessage(), mandiriTransferResponse));

                                            // Track Mixpanel event
                                            trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_MANDIRI_CLICKPAY, end - start, mandiriTransferResponse.getStatusMessage());
                                        }
                                    } else {
                                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_empty_response)));
                                        Logger.e(veritransSDK.getContext().getString(R.string.error_empty_response), null);
                                    }

                                    releaseResources();
                                }
                            });
                } else {
                    VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied)));
                    Logger.e(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied));
                    releaseResources();
                }
            } else {
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_unable_to_connect)));
                Logger.e(veritransSDK.getContext().getString(R.string.error_unable_to_connect));
                releaseResources();
            }

        } else {
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(Constants.ERROR_SDK_IS_NOT_INITIALIZED));
            Logger.e(Constants.ERROR_SDK_IS_NOT_INITIALIZED);
            releaseResources();
        }
    }

    /**
     * it will execute an api call to perform transaction using BCA KlikPay, and after
     * completion of request it will </p> call appropriate method using registered {@link TransactionSuccessEvent} or
     * {@link TransactionFailedEvent}.
     *
     * @param bcaKlikPayModel information required perform transaction using BCA KlikPay.
     */
    public static void paymentUsingBCAKlikPay(final BCAKlikPayModel bcaKlikPayModel) {

        final VeritransSDK veritransSDK = VeritransSDK.getVeritransSDK();
        final long start = System.currentTimeMillis();

        if (veritransSDK != null) {
            PaymentAPI apiInterface = VeritransRestAdapter.getMerchantApiClient(true);
            if (apiInterface != null) {
                String merchantToken = veritransSDK.readAuthenticationToken();
                Logger.i("merchantToken:" + merchantToken);
                if (merchantToken != null) {
                    apiInterface.paymentUsingBCAKlikPay(merchantToken, bcaKlikPayModel)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<TransactionResponse>() {

                                @Override
                                public void onCompleted() {
                                }

                                @Override
                                public void onError(Throwable e) {
                                    long end = System.currentTimeMillis();

                                    if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                                        VeritransBusProvider.getInstance().post(new SSLErrorEvent());
                                        Logger.i("Error in SSL Certificate. " + e.getMessage());
                                    } else {
                                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage()));
                                        Logger.i("General error occurred " + e.getMessage());
                                    }

                                    // Track Mixpanel event
                                    trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BCA_KLIKPAY, end - start, e.getMessage());

                                    releaseResources();
                                }

                                @Override
                                public void onNext(TransactionResponse bcaKlikPayResponse) {
                                    long end = System.currentTimeMillis();

                                    if (bcaKlikPayResponse != null) {
                                        if (veritransSDK.isLogEnabled()) {
                                            displayResponse(bcaKlikPayResponse);
                                        }
                                        if (bcaKlikPayResponse.getStatusCode().trim().equalsIgnoreCase(veritransSDK.getContext().getString(R.string.success_code_200))
                                                || bcaKlikPayResponse.getStatusCode().trim().equalsIgnoreCase(veritransSDK.getContext().getString(R.string.success_code_201))) {
                                            VeritransBusProvider.getInstance().post(new TransactionSuccessEvent(bcaKlikPayResponse));

                                            // Track Mixpanel event
                                            trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_BCA_KLIKPAY, end - start);
                                        } else {
                                            VeritransBusProvider.getInstance().post(new TransactionFailedEvent(bcaKlikPayResponse.getStatusMessage(), bcaKlikPayResponse));

                                            // Track Mixpanel event
                                            trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BCA_KLIKPAY, end - start, bcaKlikPayResponse.getStatusMessage());
                                        }

                                    } else {
                                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_empty_response)));
                                        Logger.e(veritransSDK.getContext().getString(R.string.error_empty_response), null);
                                    }
                                    releaseResources();
                                }
                            });
                } else {
                    VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied)));
                    Logger.e(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied));
                    releaseResources();
                }
            } else {
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_unable_to_connect)));
                Logger.e(veritransSDK.getContext().getString(R.string.error_unable_to_connect));
                releaseResources();
            }

        } else {
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(Constants.ERROR_SDK_IS_NOT_INITIALIZED));
            Logger.e(Constants.ERROR_SDK_IS_NOT_INITIALIZED);
            releaseResources();
        }
    }


    /**
     * it will execute an api call to perform transaction using mandiri bill pay, and after
     * completion of request it will </p> call appropriate method using registered {@link TransactionSuccessEvent} or
     * {@link TransactionFailedEvent}.
     *
     * @param mandiriBillPayTransferModel information required perform transaction using mandiri bill pay.
     */
    public static void paymentUsingMandiriBillPay(MandiriBillPayTransferModel mandiriBillPayTransferModel) {

        final VeritransSDK veritransSDK = VeritransSDK.getVeritransSDK();
        final long start = System.currentTimeMillis();

        if (veritransSDK != null) {
            PaymentAPI apiInterface = VeritransRestAdapter.getMerchantApiClient(true);
            if (apiInterface != null) {
                String merchantToken = veritransSDK.readAuthenticationToken();
                Logger.i("merchantToken:" + merchantToken);
                if (merchantToken != null) {
                    apiInterface.paymentUsingMandiriBillPay(merchantToken, mandiriBillPayTransferModel)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<TransactionResponse>() {
                                @Override
                                public void onCompleted() {
                                }

                                @Override
                                public void onError(Throwable e) {
                                    long end = System.currentTimeMillis();

                                    if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                                        VeritransBusProvider.getInstance().post(new SSLErrorEvent());
                                        Logger.i("Error in SSL Certificate. " + e.getMessage());
                                    } else {
                                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage()));
                                        Logger.i("General error occurred " + e.getMessage());
                                    }

                                    // Track Mixpanel Event
                                    trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BANK_TRANSFER, BANK_MANDIRI, end - start, e.getMessage());

                                    releaseResources();
                                }

                                @Override
                                public void onNext(TransactionResponse transactionResponse) {
                                    long end = System.currentTimeMillis();

                                    if (transactionResponse != null) {
                                        if (veritransSDK.isLogEnabled()) {
                                            displayResponse(transactionResponse);
                                        }
                                        if (transactionResponse.getStatusCode().trim().equalsIgnoreCase(veritransSDK.getContext().getString(R.string.success_code_200))
                                                || transactionResponse.getStatusCode().trim().equalsIgnoreCase(veritransSDK.getContext().getString(R.string.success_code_201))) {
                                            VeritransBusProvider.getInstance().post(new TransactionSuccessEvent(transactionResponse));

                                            // Track Mixpanel event
                                            trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_BANK_TRANSFER, BANK_MANDIRI, end - start);
                                        } else {
                                            VeritransBusProvider.getInstance().post(new TransactionFailedEvent(transactionResponse.getStatusMessage(), transactionResponse));

                                            // Track Mixpanel event
                                            trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BANK_TRANSFER, BANK_MANDIRI, end - start, transactionResponse.getStatusMessage());
                                        }

                                    } else {
                                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_empty_response)));
                                        Logger.e(veritransSDK.getContext().getString(R.string.error_empty_response));
                                    }

                                    releaseResources();
                                }
                            });
                } else {
                    VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied)));
                    Logger.e(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied));
                    releaseResources();
                }
            } else {
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_unable_to_connect)));
                Logger.e(veritransSDK.getContext().getString(R.string.error_unable_to_connect));
                releaseResources();
            }

        } else {
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(Constants.ERROR_SDK_IS_NOT_INITIALIZED));
            Logger.e(Constants.ERROR_SDK_IS_NOT_INITIALIZED);
            releaseResources();
        }
    }

    /**
     * It will execute API call to pay using CIMB click.
     *
     * @param cimbClickPayModel CIMB click pay model
     */
    public static void paymentUsingCIMBPay(final CIMBClickPayModel cimbClickPayModel) {
        final VeritransSDK veritransSDK = VeritransSDK.getVeritransSDK();
        final long start = System.currentTimeMillis();

        if (veritransSDK != null) {
            PaymentAPI apiInterface = VeritransRestAdapter.getMerchantApiClient(true);
            if (apiInterface != null) {
                String merchantToken = veritransSDK.readAuthenticationToken();
                Logger.i("merchantToken:" + merchantToken);
                if (merchantToken != null) {
                    apiInterface.paymentUsingCIMBClickPay(merchantToken, cimbClickPayModel)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<TransactionResponse>() {
                                @Override
                                public void onCompleted() {
                                }

                                @Override
                                public void onError(Throwable e) {
                                    long end = System.currentTimeMillis();

                                    if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                                        VeritransBusProvider.getInstance().post(new SSLErrorEvent());
                                        Logger.i("Error in SSL Certificate. " + e.getMessage());
                                    } else {
                                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage()));
                                        Logger.i("General error occurred " + e.getMessage());
                                    }

                                    // Track Mixpanel event
                                    trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_CIMB_CLICK, end - start, e.getMessage());

                                    releaseResources();
                                }

                                @Override
                                public void onNext(TransactionResponse cimbPayTransferResponse) {
                                    long end = System.currentTimeMillis();

                                    if (cimbPayTransferResponse != null) {
                                        if (veritransSDK.isLogEnabled()) {
                                            displayResponse(cimbPayTransferResponse);
                                        }
                                        if (cimbPayTransferResponse.getStatusCode().trim()
                                                .equalsIgnoreCase(veritransSDK.getContext().getString(R.string.success_code_200))
                                                || cimbPayTransferResponse.getStatusCode()
                                                .trim().equalsIgnoreCase(veritransSDK.getContext().getString(R.string.success_code_201))) {
                                            VeritransBusProvider.getInstance().post(new TransactionSuccessEvent(cimbPayTransferResponse));

                                            // Track Mixpanel event
                                            trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_CIMB_CLICK, end - start);
                                        } else {
                                            VeritransBusProvider.getInstance().post(new TransactionFailedEvent(
                                                    cimbPayTransferResponse.getStatusMessage(),
                                                    cimbPayTransferResponse
                                            ));

                                            // Track Mixpanel event
                                            trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_CIMB_CLICK, end - start, cimbPayTransferResponse.getStatusMessage());
                                        }
                                    } else {
                                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_empty_response)));
                                        Logger.e(veritransSDK.getContext().getString(R.string.error_empty_response));
                                    }

                                    releaseResources();
                                }
                            });
                } else {
                    VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied)));
                    Logger.e(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied));
                    releaseResources();
                }
            } else {
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_unable_to_connect)));
                Logger.e(veritransSDK.getContext().getString(R.string.error_unable_to_connect));
                releaseResources();
            }
        } else {
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(Constants.ERROR_SDK_IS_NOT_INITIALIZED));
            Logger.e(Constants.ERROR_SDK_IS_NOT_INITIALIZED);
            releaseResources();
        }
    }

    /**
     * It will execute API call to pay using Mandiri E-Cash.
     *
     * @param mandiriECashModel mandiri E-Cash model
     */
    public static void paymentUsingMandiriECash(MandiriECashModel mandiriECashModel) {
        final VeritransSDK veritransSDK = VeritransSDK.getVeritransSDK();
        final long start = System.currentTimeMillis();

        if (veritransSDK != null) {
            PaymentAPI apiInterface = VeritransRestAdapter.getMerchantApiClient(true);
            if (apiInterface != null) {
                String merchantToken = veritransSDK.readAuthenticationToken();
                Logger.i("merchantToken:" + merchantToken);
                if (merchantToken != null) {
                    apiInterface.paymentUsingMandiriECash(merchantToken, mandiriECashModel)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<TransactionResponse>() {
                                @Override
                                public void onCompleted() {
                                }

                                @Override
                                public void onError(Throwable e) {
                                    long end = System.currentTimeMillis();
                                    if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                                        VeritransBusProvider.getInstance().post(new SSLErrorEvent());
                                        Logger.i("Error in SSL Certificate. " + e.getMessage());
                                    } else {
                                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage()));
                                        Logger.i("General error occurred " + e.getMessage());
                                    }

                                    // Track Mixpanel event
                                    trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_MANDIRI_ECASH, end - start, e.getMessage());

                                    releaseResources();
                                }

                                @Override
                                public void onNext(TransactionResponse transferResponse) {
                                    long end = System.currentTimeMillis();

                                    if (transferResponse != null) {
                                        if (veritransSDK.isLogEnabled()) {
                                            displayResponse(transferResponse);
                                        }
                                        if (transferResponse.getStatusCode().trim()
                                                .equalsIgnoreCase(veritransSDK.getContext().getString(R.string.success_code_200))
                                                || transferResponse.getStatusCode()
                                                .trim().equalsIgnoreCase(veritransSDK.getContext().getString(R.string.success_code_201))) {
                                            VeritransBusProvider.getInstance().post(new TransactionSuccessEvent(transferResponse));

                                            // Track Mixpanel event
                                            trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_MANDIRI_ECASH, end - start);
                                        } else {
                                            VeritransBusProvider.getInstance().post(new TransactionFailedEvent(
                                                    transferResponse.getStatusMessage(),
                                                    transferResponse
                                            ));

                                            // Track Mixpanel event
                                            trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_MANDIRI_ECASH, end - start, transferResponse.getStatusMessage());
                                        }
                                    } else {
                                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_empty_response)));
                                        Logger.e(veritransSDK.getContext().getString(R.string.error_empty_response));
                                    }

                                    releaseResources();
                                }
                            });
                } else {
                    VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied)));
                    Logger.e(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied));
                    releaseResources();
                }
            } else {
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_unable_to_connect)));
                Logger.e(veritransSDK.getContext().getString(R.string.error_unable_to_connect));
                releaseResources();
            }
        } else {
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(Constants.ERROR_SDK_IS_NOT_INITIALIZED));
            Logger.e(Constants.ERROR_SDK_IS_NOT_INITIALIZED);
            releaseResources();
        }
    }

    /**
     * It will execute API call to pay using BRI Epay.
     *
     * @param epayBriTransfer BRI Epay request model
     */
    public static void paymentUsingEpayBri(final EpayBriTransfer epayBriTransfer) {
        final VeritransSDK veritransSDK = VeritransSDK.getVeritransSDK();
        final long start = System.currentTimeMillis();

        if (veritransSDK != null) {
            PaymentAPI apiInterface = VeritransRestAdapter.getMerchantApiClient(true);
            if (apiInterface != null) {
                String merchantToken = veritransSDK.readAuthenticationToken();
                Logger.i("merchantToken:" + merchantToken);
                if (merchantToken != null) {
                    apiInterface.paymentUsingEpayBri(merchantToken, epayBriTransfer)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<TransactionResponse>() {

                                @Override
                                public void onCompleted() {
                                }

                                @Override
                                public void onError(Throwable e) {
                                    long end = System.currentTimeMillis();

                                    if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                                        VeritransBusProvider.getInstance().post(new SSLErrorEvent());
                                        Logger.i("Error in SSL Certificate. " + e.getMessage());
                                    } else {
                                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage()));
                                        Logger.i("General error occurred " + e.getMessage());
                                    }

                                    // Track Mixpanel event
                                    trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BRI_EPAY, end - start, e.getMessage());

                                    releaseResources();
                                }

                                @Override
                                public void onNext(TransactionResponse epayBriTransferResponse) {
                                    long end = System.currentTimeMillis();
                                    if (epayBriTransferResponse != null) {
                                        if (veritransSDK.isLogEnabled()) {
                                            displayResponse(epayBriTransferResponse);
                                        }
                                        if (epayBriTransferResponse.getStatusCode().trim().equalsIgnoreCase(veritransSDK.getContext().getString(R.string.success_code_200))
                                                || epayBriTransferResponse.getStatusCode().trim().equalsIgnoreCase(veritransSDK.getContext().getString(R.string.success_code_201))) {
                                            VeritransBusProvider.getInstance().post(new TransactionSuccessEvent(epayBriTransferResponse));

                                            // Track Mixpanel event
                                            trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_BRI_EPAY, end - start);
                                        } else {
                                            VeritransBusProvider.getInstance().post(new TransactionFailedEvent(epayBriTransferResponse.getStatusMessage(), epayBriTransferResponse));

                                            // Track Mixpanel event
                                            trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BRI_EPAY, end - start, epayBriTransferResponse.getStatusMessage());
                                        }

                                    } else {
                                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_empty_response)));
                                        Logger.e(veritransSDK.getContext().getString(R.string.error_empty_response));
                                    }

                                    releaseResources();
                                }
                            });
                } else {
                    VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied)));
                    Logger.e(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied));
                    releaseResources();
                }
            } else {
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_unable_to_connect)));
                Logger.e(veritransSDK.getContext().getString(R.string.error_unable_to_connect));
                releaseResources();
            }

        } else {
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(Constants.ERROR_SDK_IS_NOT_INITIALIZED));
            Logger.e(Constants.ERROR_SDK_IS_NOT_INITIALIZED);
            releaseResources();
        }
    }

    /**
     * It will execute API call to get payment status based on transaction ID.
     *
     * @param id    transaction identifier
     */
    public static void getPaymentStatus(String id) {
        final VeritransSDK veritransSDK = VeritransSDK.getVeritransSDK();
        if (veritransSDK != null) {
            PaymentAPI apiInterface = VeritransRestAdapter.getMerchantApiClient(true);
            if (apiInterface != null) {
                String merchantToken = veritransSDK.readAuthenticationToken();
                Logger.i("merchantToken:" + merchantToken);
                if (merchantToken != null) {
                    apiInterface.transactionStatus(merchantToken, id)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<TransactionStatusResponse>() {
                                @Override
                                public void onCompleted() {
                                }

                                @Override
                                public void onError(Throwable e) {
                                    if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                                        VeritransBusProvider.getInstance().post(new SSLErrorEvent());
                                        Logger.i("Error in SSL Certificate. " + e.getMessage());
                                    } else {
                                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage()));
                                        Logger.i("General error occurred " + e.getMessage());
                                    }
                                    releaseResources();
                                }

                                @Override
                                public void onNext(TransactionStatusResponse transactionStatusResponse) {
                                    if (transactionStatusResponse != null) {
                                        if (TextUtils.isEmpty(transactionStatusResponse.getStatusCode())) {
                                            if (transactionStatusResponse.getStatusCode().equalsIgnoreCase(veritransSDK.getContext().getString(R.string.success_code_200))
                                                    || transactionStatusResponse.getStatusCode().equalsIgnoreCase(veritransSDK.getContext().getString(R.string.success_code_201))) {
                                                VeritransBusProvider.getInstance().post(new TransactionStatusSuccessEvent(transactionStatusResponse));
                                            }
                                        } else {
                                            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_empty_response)));
                                        }
                                    } else {
                                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_empty_response)));
                                    }
                                    releaseResources();
                                }
                            });

                } else {
                    VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied)));
                    Logger.e(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied));
                    releaseResources();
                }
            } else {
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_unable_to_connect)));
                Logger.e(veritransSDK.getContext().getString(R.string.error_unable_to_connect));
                releaseResources();
            }

        } else {
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(Constants.ERROR_SDK_IS_NOT_INITIALIZED));
            Logger.e(Constants.ERROR_SDK_IS_NOT_INITIALIZED);
            releaseResources();
        }
    }

    /**
     * It will execute API call to pay using Indosat Dompetku.
     *
     * @param indosatDompetkuRequest    Indosat Dompetku request model
     */
    public static void paymentUsingIndosatDompetku(final IndosatDompetkuRequest indosatDompetkuRequest) {
        final VeritransSDK veritransSDK = VeritransSDK.getVeritransSDK();
        final long start = System.currentTimeMillis();

        if (veritransSDK != null) {
            PaymentAPI apiInterface = VeritransRestAdapter.getMerchantApiClient(true);
            if (apiInterface != null) {
                String merchantToken = veritransSDK.readAuthenticationToken();
                Logger.i("merchantToken:" + merchantToken);
                if (merchantToken != null) {
                    apiInterface.paymentUsingIndosatDompetku(merchantToken, indosatDompetkuRequest)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<TransactionResponse>() {
                                @Override
                                public void onCompleted() {
                                }

                                @Override
                                public void onError(Throwable e) {
                                    long end = System.currentTimeMillis();

                                    if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                                        VeritransBusProvider.getInstance().post(new SSLErrorEvent());
                                        Logger.i("Error in SSL Certificate. " + e.getMessage());
                                    } else {
                                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage()));
                                        Logger.i("General error occurred " + e.getMessage());
                                    }

                                    // Track Mixpanel event
                                    trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_INDOSAT_DOMPETKU, end - start, e.getMessage());

                                    releaseResources();
                                }

                                @Override
                                public void onNext(TransactionResponse transactionResponse) {
                                    long end = System.currentTimeMillis();

                                    if (transactionResponse != null) {
                                        if (veritransSDK.isLogEnabled()) {
                                            displayResponse(transactionResponse);
                                        }
                                        if (transactionResponse.getStatusCode().trim().equalsIgnoreCase(veritransSDK.getContext().getString(R.string.success_code_200))
                                                || transactionResponse.getStatusCode().trim().equalsIgnoreCase(veritransSDK.getContext().getString(R.string.success_code_201))) {
                                            VeritransBusProvider.getInstance().post(new TransactionSuccessEvent(transactionResponse));

                                            // Track Mixpanel event
                                            trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_INDOSAT_DOMPETKU, end - start);
                                        } else {
                                            VeritransBusProvider.getInstance().post(new TransactionFailedEvent(transactionResponse.getStatusMessage(), transactionResponse));

                                            // Track Mixpanel event
                                            trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_INDOSAT_DOMPETKU, end - start, transactionResponse.getStatusMessage());
                                        }
                                    } else {
                                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_empty_response)));
                                        Logger.e(veritransSDK.getContext().getString(R.string.error_empty_response));
                                    }

                                    releaseResources();
                                }
                            });
                } else {
                    VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied)));
                    Logger.e(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied));
                    releaseResources();
                }
            } else {
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_unable_to_connect)));
                Logger.e(veritransSDK.getContext().getString(R.string.error_unable_to_connect));
                releaseResources();
            }

        } else {
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(Constants.ERROR_SDK_IS_NOT_INITIALIZED));
            Logger.e(Constants.ERROR_SDK_IS_NOT_INITIALIZED);
            releaseResources();
        }
    }

    /**
     * It will execute API call to pay using Indomaret.
     *
     * @param indomaretRequestModel    Indomaret payment request model
     */
    public static void paymentUsingIndomaret(final IndomaretRequestModel indomaretRequestModel) {
        final VeritransSDK veritransSDK = VeritransSDK.getVeritransSDK();
        final long start = System.currentTimeMillis();

        if (veritransSDK != null) {
            PaymentAPI apiInterface = VeritransRestAdapter.getMerchantApiClient(true);
            if (apiInterface != null) {
                String merchantToken = veritransSDK.readAuthenticationToken();
                Logger.i("merchantToken:" + merchantToken);
                if (merchantToken != null) {
                    apiInterface.paymentUsingIndomaret(merchantToken, indomaretRequestModel)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<TransactionResponse>() {
                                @Override
                                public void onCompleted() {
                                }

                                @Override
                                public void onError(Throwable e) {
                                    long end = System.currentTimeMillis();

                                    if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                                        VeritransBusProvider.getInstance().post(new SSLErrorEvent());
                                        Logger.i("Error in SSL Certificate. " + e.getMessage());
                                    } else {
                                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage()));
                                        Logger.i("General error occurred " + e.getMessage());
                                    }

                                    // Track Mixpanel event
                                    trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_INDOMARET, end - start, e.getMessage());

                                    releaseResources();
                                }

                                @Override
                                public void onNext(TransactionResponse indomaretTransferResponse) {
                                    long end = System.currentTimeMillis();

                                    if (indomaretTransferResponse != null) {
                                        if (veritransSDK.isLogEnabled()) {
                                            displayResponse(indomaretTransferResponse);
                                        }
                                        if (indomaretTransferResponse.getStatusCode().trim().equalsIgnoreCase(veritransSDK.getContext().getString(R.string.success_code_200))
                                                || indomaretTransferResponse.getStatusCode().trim().equalsIgnoreCase(veritransSDK.getContext().getString(R.string.success_code_201))) {
                                            VeritransBusProvider.getInstance().post(new TransactionSuccessEvent(indomaretTransferResponse));

                                            // Track Mixpanel event
                                            trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_INDOMARET, end - start);
                                        } else {
                                            VeritransBusProvider.getInstance().post(new TransactionFailedEvent(indomaretTransferResponse.getStatusMessage(), indomaretTransferResponse));

                                            // Track Mixpanel event
                                            trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_INDOMARET, end - start, indomaretTransferResponse.getStatusMessage());
                                        }

                                    } else {
                                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_empty_response)));
                                        Logger.e(veritransSDK.getContext().getString(R.string.error_empty_response));
                                    }
                                    releaseResources();
                                }
                            });
                } else {
                    VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied)));
                    Logger.e(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied));
                    releaseResources();
                }
            } else {
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_unable_to_connect)));
                Logger.e(veritransSDK.getContext().getString(R.string.error_unable_to_connect));
                releaseResources();
            }

        } else {
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(Constants.ERROR_SDK_IS_NOT_INITIALIZED));
            Logger.e(Constants.ERROR_SDK_IS_NOT_INITIALIZED);
            releaseResources();
        }
    }

    /**
     * It will execute API call to pay using BBM Money.
     *
     * @param bbmMoneyRequestModel  BBM Money payment request model
     */
    public static void paymentUsingBBMMoney(final BBMMoneyRequestModel bbmMoneyRequestModel) {
        final VeritransSDK veritransSDK = VeritransSDK.getVeritransSDK();
        final long start = System.currentTimeMillis();

        if (veritransSDK != null) {
            PaymentAPI apiInterface = VeritransRestAdapter.getMerchantApiClient(true);

            if (apiInterface != null) {
                String merchantToken = veritransSDK.readAuthenticationToken();
                Logger.i("merchantToken:" + merchantToken);
                if (merchantToken != null) {
                    apiInterface.paymentUsingBBMMoney(merchantToken, bbmMoneyRequestModel)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<TransactionResponse>() {
                                @Override
                                public void onCompleted() {
                                }

                                @Override
                                public void onError(Throwable e) {
                                    long end = System.currentTimeMillis();

                                    if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                                        VeritransBusProvider.getInstance().post(new SSLErrorEvent());
                                        Logger.i("Error in SSL Certificate. " + e.getMessage());
                                    } else {
                                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage()));
                                        Logger.i("General error occurred " + e.getMessage());
                                    }

                                    // Track Mixpanel event
                                    trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BBM_MONEY, end - start, e.getMessage());

                                    releaseResources();
                                }

                                @Override
                                public void onNext(TransactionResponse bbmMoneyTransferResponse) {
                                    long end = System.currentTimeMillis();

                                    if (bbmMoneyTransferResponse != null) {
                                        if (veritransSDK.isLogEnabled()) {
                                            displayResponse(bbmMoneyTransferResponse);
                                        }
                                        if (bbmMoneyTransferResponse.getStatusCode().trim().equalsIgnoreCase(veritransSDK.getContext().getString(R.string.success_code_200))
                                                || bbmMoneyTransferResponse.getStatusCode().trim().equalsIgnoreCase(veritransSDK.getContext().getString(R.string.success_code_201))) {
                                            VeritransBusProvider.getInstance().post(new TransactionSuccessEvent(bbmMoneyTransferResponse));

                                            // Track Mixpanel event
                                            trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_BBM_MONEY, end - start);
                                        } else {
                                            VeritransBusProvider.getInstance().post(new TransactionFailedEvent(bbmMoneyTransferResponse.getStatusMessage(), bbmMoneyTransferResponse));

                                            // Track Mixpanel event
                                            trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BBM_MONEY, end - start, bbmMoneyTransferResponse.getStatusMessage());
                                        }

                                    } else {
                                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_empty_response)));
                                        Logger.e(veritransSDK.getContext().getString(R.string.error_empty_response));
                                    }

                                    releaseResources();

                                }
                            });
                } else {
                    VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied)));
                    Logger.e(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied));
                    releaseResources();
                }
            } else {
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_unable_to_connect)));
                Logger.e(veritransSDK.getContext().getString(R.string.error_unable_to_connect));
                releaseResources();
            }

        } else {
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(Constants.ERROR_SDK_IS_NOT_INITIALIZED));
            Logger.e(Constants.ERROR_SDK_IS_NOT_INITIALIZED);
            releaseResources();
        }
    }

    /**
     * It will execute API call to save card based
     * @param cardTokenRequest  card token request model
     */
    public static void saveCards(final SaveCardRequest cardTokenRequest) {

        final VeritransSDK veritransSDK = VeritransSDK.getVeritransSDK();

        if (veritransSDK != null) {
            PaymentAPI apiInterface = VeritransRestAdapter.getMerchantApiClient(true);
            if (apiInterface != null) {
                String auth = veritransSDK.readAuthenticationToken();
                Logger.i("Authentication token:" + auth);
                if (auth != null && !auth.equals("")) {
                    apiInterface.saveCard(auth, cardTokenRequest)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<SaveCardResponse>() {
                                @Override
                                public void onCompleted() {
                                }

                                @Override
                                public void onError(Throwable e) {
                                    if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                                        VeritransBusProvider.getInstance().post(new SSLErrorEvent());
                                        Logger.i("Error in SSL Certificate. " + e.getMessage());
                                    } else {
                                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage()));
                                        Logger.i("General error occurred " + e.getMessage());
                                    }
                                    releaseResources();
                                }

                                @Override
                                public void onNext(SaveCardResponse cardResponse) {
                                    if (cardResponse != null) {
                                        if (cardResponse.getCode() == 200 || cardResponse.getCode() == 201) {
                                            VeritransBusProvider.getInstance().post(new SaveCardSuccessEvent(cardResponse));
                                        } else {
                                            VeritransBusProvider.getInstance().post(new SaveCardFailedEvent(cardResponse.getStatus(), cardResponse));
                                        }
                                    } else {
                                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_empty_response)));
                                        Logger.e(veritransSDK.getContext().getString(R.string.error_empty_response));
                                    }
                                    releaseResources();
                                }
                            });
                } else {
                    VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied)));
                    Logger.e(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied));
                    releaseResources();
                }
            } else {
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_unable_to_connect)));
                Logger.e(veritransSDK.getContext().getString(R.string.error_unable_to_connect));
                releaseResources();
            }

        } else {
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(Constants.ERROR_SDK_IS_NOT_INITIALIZED));
            Logger.e(Constants.ERROR_SDK_IS_NOT_INITIALIZED);
            releaseResources();
        }
    }

    /**
     * It will call execute API call to get saved cards.
     */
    public static void getCards() {
        final VeritransSDK veritransSDK = VeritransSDK.getVeritransSDK();

        if (veritransSDK != null) {
            PaymentAPI apiInterface = VeritransRestAdapter.getMerchantApiClient(true);

            if (apiInterface != null) {
                String auth = veritransSDK.readAuthenticationToken();
                Logger.i("Authentication token:" + auth);
                if (auth != null && !auth.equals("")) {
                    apiInterface.getCard(auth)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<CardResponse>() {

                                @Override
                                public void onCompleted() {
                                }

                                @Override
                                public void onError(Throwable e) {
                                    if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                                        VeritransBusProvider.getInstance().post(new SSLErrorEvent());
                                        Logger.i("Error in SSL Certificate. " + e.getMessage());
                                    } else {
                                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage()));
                                        Logger.i("General error occurred " + e.getMessage());
                                    }
                                    releaseResources();
                                }

                                @Override
                                public void onNext(CardResponse cardResponse) {
                                    if (cardResponse != null) {
                                        if (cardResponse.getCode() == 200) {
                                            VeritransBusProvider.getInstance().post(new GetCardsSuccessEvent(cardResponse));
                                        } else {
                                            VeritransBusProvider.getInstance().post(new GetCardFailedEvent(cardResponse.getStatus(), cardResponse));
                                        }
                                    } else {
                                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_empty_response)));
                                        Logger.e(veritransSDK.getContext().getString(R.string.error_empty_response));
                                    }
                                    releaseResources();
                                }
                            });
                } else {
                    VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied)));
                    Logger.e(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied));
                    releaseResources();
                }
            } else {
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_unable_to_connect)));
                Logger.e(veritransSDK.getContext().getString(R.string.error_unable_to_connect));
                releaseResources();
            }

        } else {
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(Constants.ERROR_SDK_IS_NOT_INITIALIZED));
            Logger.e(Constants.ERROR_SDK_IS_NOT_INITIALIZED);
            releaseResources();
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

    private static void releaseResources() {
        VeritransSDK veritransSDK = VeritransSDK.getVeritransSDK();
        if (veritransSDK != null) {
            veritransSDK.isRunning = false;
            Logger.i("released transaction");
        }
    }

    /**
     * It will execute API call to delete saved card from merchant server.
     * @param creditCard    credit card detail
     */
    public static void deleteCard(SaveCardRequest creditCard) {
        final VeritransSDK veritransSDK = VeritransSDK.getVeritransSDK();

        if (veritransSDK != null) {
            PaymentAPI apiInterface = VeritransRestAdapter.getMerchantApiClient(true);
            if (apiInterface != null) {
                String auth = veritransSDK.readAuthenticationToken();
                Logger.i("Authentication token:" + auth);
                if (auth != null) {
                    apiInterface.deleteCard(auth, creditCard.getSavedTokenId())
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<DeleteCardResponse>() {

                                @Override
                                public void onCompleted() {
                                }

                                @Override
                                public void onError(Throwable e) {
                                    if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                                        VeritransBusProvider.getInstance().post(new SSLErrorEvent());
                                        Logger.i("Error in SSL Certificate. " + e.getMessage());
                                    } else {
                                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage()));
                                        Logger.i("General error occurred " + e.getMessage());
                                    }
                                    releaseResources();
                                }

                                @Override
                                public void onNext(DeleteCardResponse deleteCardResponse) {
                                    if (deleteCardResponse != null) {
                                        if (deleteCardResponse.getCode() == 200 || deleteCardResponse.getCode() == 204) {
                                            VeritransBusProvider.getInstance().post(new DeleteCardSuccessEvent(deleteCardResponse));
                                        } else {
                                            VeritransBusProvider.getInstance().post(new DeleteCardFailedEvent(deleteCardResponse.getMessage(), deleteCardResponse));
                                        }
                                    } else {
                                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_empty_response)));
                                        Logger.e(veritransSDK.getContext().getString(R.string.error_empty_response));
                                    }
                                    releaseResources();
                                }
                            });
                } else {
                    VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied)));
                    Logger.e(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied));
                    releaseResources();
                }
            } else {
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_unable_to_connect)));
                Logger.e(veritransSDK.getContext().getString(R.string.error_unable_to_connect));
                releaseResources();
            }
        } else {
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(Constants.ERROR_SDK_IS_NOT_INITIALIZED));
            Logger.e(Constants.ERROR_SDK_IS_NOT_INITIALIZED);
            releaseResources();
        }
    }

    /**
     * It will execute API call to get offers.
     */
    public static void getOffers() {
        final VeritransSDK veritransSDK = VeritransSDK.getVeritransSDK();

        if (veritransSDK != null) {
            PaymentAPI apiInterface = VeritransRestAdapter.getMerchantApiClient(true);
            if (apiInterface != null) {
                String merchantToken = veritransSDK.readAuthenticationToken();
                Logger.i("merchantToken:" + merchantToken);
                if (merchantToken != null) {
                    apiInterface.getOffers(merchantToken)
                            .subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Observer<GetOffersResponseModel>() {
                                @Override
                                public void onCompleted() {
                                }

                                @Override
                                public void onError(Throwable e) {
                                    if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                                        VeritransBusProvider.getInstance().post(new SSLErrorEvent());
                                        Logger.i("Error in SSL Certificate. " + e.getMessage());
                                    } else {
                                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage()));
                                        Logger.i("General error occurred " + e.getMessage());
                                    }

                                    releaseResources();
                                }

                                @Override
                                public void onNext(GetOffersResponseModel getOffersResponseModel) {
                                    if (getOffersResponseModel != null) {
                                        if (getOffersResponseModel.getMessage().equalsIgnoreCase(veritransSDK.getContext().getString(R.string.success))) {
                                            VeritransBusProvider.getInstance().post(new GetOfferSuccessEvent(getOffersResponseModel));
                                        } else {
                                            VeritransBusProvider.getInstance().post(new GetOfferFailedEvent(getOffersResponseModel.getMessage(), getOffersResponseModel));
                                        }
                                    } else {
                                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_empty_response)));
                                        Logger.e(veritransSDK.getContext().getString(R.string.error_empty_response));
                                    }

                                    releaseResources();
                                }
                            });
                } else {
                    VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied)));
                    Logger.e(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied));
                    releaseResources();
                }
            } else {
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_unable_to_connect)));
                Logger.e(veritransSDK.getContext().getString(R.string.error_unable_to_connect));
                releaseResources();
            }

        } else {
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(Constants.ERROR_SDK_IS_NOT_INITIALIZED));
            Logger.e(Constants.ERROR_SDK_IS_NOT_INITIALIZED);
            releaseResources();
        }
    }

    /**
     * It will execute API call to get authentication token from merchant server.
     */
    public static void getAuthenticationToken() {
        final VeritransSDK veritransSDK = VeritransSDK.getVeritransSDK();

        if (veritransSDK != null) {
            PaymentAPI paymentAPI = VeritransRestAdapter.getMerchantApiClient(true);
            if (paymentAPI != null) {
                paymentAPI.getAuthenticationToken()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<AuthModel>() {
                            @Override
                            public void onCompleted() {
                            }

                            @Override
                            public void onError(Throwable e) {
                                if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                                    VeritransBusProvider.getInstance().post(new SSLErrorEvent());
                                    Logger.i("Error in SSL Certificate. " + e.getMessage());
                                } else {
                                    VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage()));
                                    Logger.i("General error occurred " + e.getMessage());
                                }

                                releaseResources();
                            }

                            @Override
                            public void onNext(AuthModel authModel) {
                                VeritransBusProvider.getInstance().post(new AuthenticationEvent(authModel));
                                releaseResources();
                            }
                        });
            } else {
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_unable_to_connect)));
                Logger.e(veritransSDK.getContext().getString(R.string.error_unable_to_connect));
                releaseResources();
            }
        } else {
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(Constants.ERROR_SDK_IS_NOT_INITIALIZED));
            Logger.e(Constants.ERROR_SDK_IS_NOT_INITIALIZED);
            releaseResources();
        }
    }


    // Mixpanel event tracker
    private static void trackMixpanel(String eventName, String paymentType, String bankType, long responseTime) {
        MixpanelEvent event = new MixpanelEvent();
        event.setEvent(eventName);

        MixpanelProperties properties = new MixpanelProperties();
        properties.setOsVersion(Build.VERSION.RELEASE);
        properties.setVersion(BuildConfig.VERSION_NAME);
        properties.setPlatform(PLATFORM);
        properties.setDeviceId(SdkUtil.getDeviceId());
        properties.setToken(BuildConfig.MIXPANEL_TOKEN);
        properties.setMerchant(VeritransSDK.getVeritransSDK() != null &&
                VeritransSDK.getVeritransSDK().getMerchantName() != null ?
                VeritransSDK.getVeritransSDK().getMerchantName() :
                VeritransSDK.getVeritransSDK().getClientKey());
        properties.setPaymentType(paymentType);
        if (bankType != null && !bankType.equals("")) {
            properties.setBank(bankType);
        }
        properties.setResponseTime(responseTime);

        event.setProperties(properties);

        MixpanelAnalyticsManager.trackEvent(event);
    }

    private static void trackMixpanel(String eventName, String paymentType, long responseTime) {
        MixpanelEvent event = new MixpanelEvent();
        event.setEvent(eventName);

        MixpanelProperties properties = new MixpanelProperties();
        properties.setOsVersion(Build.VERSION.RELEASE);
        properties.setVersion(BuildConfig.VERSION_NAME);
        properties.setPlatform(PLATFORM);
        properties.setDeviceId(SdkUtil.getDeviceId());
        properties.setToken(BuildConfig.MIXPANEL_TOKEN);
        properties.setMerchant(VeritransSDK.getVeritransSDK() != null &&
                VeritransSDK.getVeritransSDK().getMerchantName() != null ?
                VeritransSDK.getVeritransSDK().getMerchantName() :
                VeritransSDK.getVeritransSDK().getClientKey());
        properties.setPaymentType(paymentType);
        properties.setResponseTime(responseTime);

        event.setProperties(properties);

        MixpanelAnalyticsManager.trackEvent(event);
    }

    private static void trackMixpanel(String eventName, String paymentType, long responseTime, String errorMessage) {
        MixpanelEvent event = new MixpanelEvent();
        event.setEvent(eventName);

        MixpanelProperties properties = new MixpanelProperties();
        properties.setOsVersion(Build.VERSION.RELEASE);
        properties.setVersion(BuildConfig.VERSION_NAME);
        properties.setPlatform(PLATFORM);
        properties.setDeviceId(SdkUtil.getDeviceId());
        properties.setToken(BuildConfig.MIXPANEL_TOKEN);
        properties.setPaymentType(paymentType);
        properties.setMerchant(VeritransSDK.getVeritransSDK() != null &&
                VeritransSDK.getVeritransSDK().getMerchantName() != null ?
                VeritransSDK.getVeritransSDK().getMerchantName() :
                VeritransSDK.getVeritransSDK().getClientKey());
        properties.setResponseTime(responseTime);
        properties.setMessage(errorMessage);

        event.setProperties(properties);

        MixpanelAnalyticsManager.trackEvent(event);
    }

    private static void trackMixpanel(String eventName, String paymentType, String bank, long responseTime, String errorMessage) {
        MixpanelEvent event = new MixpanelEvent();
        event.setEvent(eventName);

        MixpanelProperties properties = new MixpanelProperties();
        properties.setOsVersion(Build.VERSION.RELEASE);
        properties.setVersion(BuildConfig.VERSION_NAME);
        properties.setPlatform(PLATFORM);
        properties.setDeviceId(SdkUtil.getDeviceId());
        properties.setToken(BuildConfig.MIXPANEL_TOKEN);
        properties.setPaymentType(paymentType);
        properties.setBank(bank);
        properties.setMerchant(VeritransSDK.getVeritransSDK() != null &&
                VeritransSDK.getVeritransSDK().getMerchantName() != null ?
                VeritransSDK.getVeritransSDK().getMerchantName() :
                VeritransSDK.getVeritransSDK().getClientKey());
        properties.setResponseTime(responseTime);
        properties.setMessage(errorMessage);

        event.setProperties(properties);

        MixpanelAnalyticsManager.trackEvent(event);
    }
}