package id.co.veritrans.sdk.coreflow.core;

import android.os.Build;
import android.text.TextUtils;

import java.security.cert.CertPathValidatorException;

import javax.net.ssl.SSLHandshakeException;

import id.co.veritrans.sdk.coreflow.BuildConfig;
import id.co.veritrans.sdk.coreflow.R;
import id.co.veritrans.sdk.coreflow.analytics.MixpanelEvent;
import id.co.veritrans.sdk.coreflow.analytics.MixpanelProperties;
import id.co.veritrans.sdk.coreflow.eventbus.bus.VeritransBusProvider;
import id.co.veritrans.sdk.coreflow.eventbus.events.AuthenticationEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.CardRegistrationFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.CardRegistrationSuccessEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.DeleteCardFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.DeleteCardSuccessEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.Events;
import id.co.veritrans.sdk.coreflow.eventbus.events.GeneralErrorEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.GetCardFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.GetCardsSuccessEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.GetOfferFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.GetOfferSuccessEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.GetTokenFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.GetTokenSuccessEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.RegisterCardFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.RegisterCardSuccessEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.SSLErrorEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.SaveCardFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.SaveCardSuccessEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.TransactionFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.TransactionStatusSuccessEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.TransactionSuccessEvent;
import id.co.veritrans.sdk.coreflow.models.AuthModel;
import id.co.veritrans.sdk.coreflow.models.BBMMoneyRequestModel;
import id.co.veritrans.sdk.coreflow.models.BCABankTransfer;
import id.co.veritrans.sdk.coreflow.models.BCAKlikPayModel;
import id.co.veritrans.sdk.coreflow.models.CIMBClickPayModel;
import id.co.veritrans.sdk.coreflow.models.CardRegistrationResponse;
import id.co.veritrans.sdk.coreflow.models.CardResponse;
import id.co.veritrans.sdk.coreflow.models.CardTokenRequest;
import id.co.veritrans.sdk.coreflow.models.CardTransfer;
import id.co.veritrans.sdk.coreflow.models.DeleteCardResponse;
import id.co.veritrans.sdk.coreflow.models.EpayBriTransfer;
import id.co.veritrans.sdk.coreflow.models.GetOffersResponseModel;
import id.co.veritrans.sdk.coreflow.models.IndomaretRequestModel;
import id.co.veritrans.sdk.coreflow.models.IndosatDompetkuRequest;
import id.co.veritrans.sdk.coreflow.models.MandiriBillPayTransferModel;
import id.co.veritrans.sdk.coreflow.models.MandiriClickPayRequestModel;
import id.co.veritrans.sdk.coreflow.models.MandiriECashModel;
import id.co.veritrans.sdk.coreflow.models.PermataBankTransfer;
import id.co.veritrans.sdk.coreflow.models.RegisterCardResponse;
import id.co.veritrans.sdk.coreflow.models.SaveCardRequest;
import id.co.veritrans.sdk.coreflow.models.SaveCardResponse;
import id.co.veritrans.sdk.coreflow.models.TokenDetailsResponse;
import id.co.veritrans.sdk.coreflow.models.TransactionResponse;
import id.co.veritrans.sdk.coreflow.models.TransactionStatusResponse;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

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
            PaymentAPI paymentAPI = VeritransRestAdapter.getApiClient();
            if (paymentAPI != null) {
                paymentAPI.registerCard(cardNumber, cardCvv, cardExpMonth, cardExpYear, veritransSDK.getClientKey(), new Callback<CardRegistrationResponse>() {
                    @Override
                    public void success(CardRegistrationResponse cardRegistrationResponse, Response response) {
                        if (cardRegistrationResponse != null) {
                            if (cardRegistrationResponse.getStatusCode().equals(veritransSDK.getContext().getString(R.string.success_code_200))) {
                                VeritransBusProvider.getInstance().post(new CardRegistrationSuccessEvent(cardRegistrationResponse, Events.CARD_REGISTRATION));
                            } else {
                                VeritransBusProvider.getInstance().post(new CardRegistrationFailedEvent(cardRegistrationResponse.getStatusMessage(), cardRegistrationResponse, Events.CARD_REGISTRATION));
                            }
                        } else {
                            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_empty_response), Events.CARD_REGISTRATION));
                            Logger.e(veritransSDK.getContext().getString(R.string.error_empty_response));
                        }

                        releaseResources();
                    }

                    @Override
                    public void failure(RetrofitError e) {
                        Logger.e("error while getting token : ", "" + e.getMessage());

                        if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                            VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.CARD_REGISTRATION));
                            Logger.i("Error in SSL Certificate. " + e.getMessage());
                        } else {
                            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage(), Events.CARD_REGISTRATION));
                            Logger.i("General error occurred " + e.getMessage());
                        }

                        releaseResources();
                    }
                });
            } else {
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_empty_response), Events.CARD_REGISTRATION));
                Logger.e(veritransSDK.getContext().getString(R.string.error_unable_to_connect));
                releaseResources();
            }
        } else {
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(Constants.ERROR_SDK_IS_NOT_INITIALIZED, Events.CARD_REGISTRATION));
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
            PaymentAPI apiInterface = VeritransRestAdapter.getApiClient();
            if (apiInterface != null) {
                apiInterface.registerCard(cardTokenRequest.getCardNumber(), cardTokenRequest.getCardExpiryMonth(),
                        cardTokenRequest.getCardExpiryYear(), cardTokenRequest.getClientKey(), new Callback<RegisterCardResponse>() {
                    @Override
                    public void success(RegisterCardResponse registerCardResponse, Response response) {
                        if (registerCardResponse != null) {
                            if (veritransSDK.isLogEnabled()) {
                                displayResponse(registerCardResponse);
                            }
                            if (registerCardResponse.getStatusCode().trim().equalsIgnoreCase(veritransSDK.getContext().getString(R.string.success_code_200))) {
                                registerCardResponse.setUserId(userId);
                                PaymentAPI apiInterface = VeritransRestAdapter.getMerchantApiClient();
                                if (apiInterface != null) {
                                    apiInterface.registerCard(merchantToken, registerCardResponse, new Callback<CardResponse>() {
                                        @Override
                                        public void success(CardResponse cardResponse, Response response) {

                                        }

                                        @Override
                                        public void failure(RetrofitError e) {
                                            Logger.e("CardSubscriber", e.getMessage());

                                        }
                                    });
//                                            apiInterface.registerCard(merchantToken, registerCardResponse)
//                                                    .subscribeOn(Schedulers.io())
//                                                    .observeOn(AndroidSchedulers.mainThread())
//                                                    .subscribe(new Observer<CardResponse>() {
//                                                        @Override
//                                                        public void onCompleted() {
//
//                                                        }
//
//                                                        @Override
//                                                        public void onError(Throwable e) {
//                                                            Logger.e("CardSubscriber", e.getMessage());
//                                                        }
//
//                                                        @Override
//                                                        public void onNext(CardResponse cardResponse) {
//                                                        }
//                                                    });

                                }
                                VeritransBusProvider.getInstance().post(new RegisterCardSuccessEvent(registerCardResponse, Events.REGISTER_CARD));
                            } else {
                                if (!TextUtils.isEmpty(registerCardResponse.getStatusMessage())) {
                                    VeritransBusProvider.getInstance().post(new RegisterCardFailedEvent(registerCardResponse.getStatusMessage(), registerCardResponse, Events.REGISTER_CARD));
                                } else {
                                    VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_empty_response), Events.REGISTER_CARD));
                                }
                            }

                        } else {
                            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_empty_response), Events.REGISTER_CARD));
                            Logger.e(veritransSDK.getContext().getString(R.string.error_empty_response), Events.REGISTER_CARD);
                        }
                        releaseResources();
                    }

                    @Override
                    public void failure(RetrofitError e) {
                        Logger.e("error while getting token : ", "" + e.getMessage());
                        if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                            VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.REGISTER_CARD));
                            Logger.i("Error in SSL Certificate. " + e.getMessage());
                        } else {
                            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage(), Events.REGISTER_CARD));
                            Logger.i("General error occurred " + e.getMessage());
                        }
                        releaseResources();
                    }
                });

            } else {
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_empty_response), Events.REGISTER_CARD));
                Logger.e(veritransSDK.getContext().getString(R.string.error_unable_to_connect));
                releaseResources();
            }

        } else {
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(Constants.ERROR_SDK_IS_NOT_INITIALIZED, Events.REGISTER_CARD));
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
            PaymentAPI apiInterface = VeritransRestAdapter.getApiClient();
            if (apiInterface != null) {
                if (cardTokenRequest.isTwoClick()) {
                    if (cardTokenRequest.isInstalment()) {
                        apiInterface.getTokenInstalmentOfferTwoClick(
                                cardTokenRequest.getCardCVV(),
                                cardTokenRequest.getSavedTokenId(),
                                cardTokenRequest.isTwoClick(),
                                cardTokenRequest.isSecure(),
                                cardTokenRequest.getGrossAmount(),
                                cardTokenRequest.getBank(),
                                cardTokenRequest.getClientKey(),
                                cardTokenRequest.isInstalment(),
                                cardTokenRequest.getFormattedInstalmentTerm(), new Callback<TokenDetailsResponse>() {
                                    @Override
                                    public void success(TokenDetailsResponse tokenDetailsResponse, Response response) {
                                        consumeTokenSuccesResponse(veritransSDK, start, tokenDetailsResponse);
                                    }

                                    @Override
                                    public void failure(RetrofitError error) {
                                        consumeTokenErrorResponse(start, error);
                                    }
                                });
                    } else {
                        apiInterface.getTokenTwoClick(
                                cardTokenRequest.getCardCVV(),
                                cardTokenRequest.getSavedTokenId(),
                                cardTokenRequest.isTwoClick(),
                                cardTokenRequest.isSecure(),
                                cardTokenRequest.getGrossAmount(),
                                cardTokenRequest.getBank(),
                                cardTokenRequest.getClientKey(), new Callback<TokenDetailsResponse>() {
                                    @Override
                                    public void success(TokenDetailsResponse tokenDetailsResponse, Response response) {
                                        consumeTokenSuccesResponse(veritransSDK, start, tokenDetailsResponse);
                                    }

                                    @Override
                                    public void failure(RetrofitError error) {
                                        consumeTokenErrorResponse(start, error);
                                    }
                                });
                    }

                } else {
                    if (cardTokenRequest.isInstalment()) {
                        apiInterface.get3DSTokenInstalmentOffers(cardTokenRequest.getCardNumber(),
                                cardTokenRequest.getCardCVV(),
                                cardTokenRequest.getCardExpiryMonth(), cardTokenRequest
                                        .getCardExpiryYear(),
                                cardTokenRequest.getClientKey(),
                                cardTokenRequest.getBank(),
                                cardTokenRequest.isSecure(),
                                cardTokenRequest.isTwoClick(),
                                cardTokenRequest.getGrossAmount(),
                                cardTokenRequest.isInstalment(),
                                cardTokenRequest.getFormattedInstalmentTerm(), new Callback<TokenDetailsResponse>() {
                                    @Override
                                    public void success(TokenDetailsResponse tokenDetailsResponse, Response response) {
                                        consumeTokenSuccesResponse(veritransSDK, start, tokenDetailsResponse);

                                    }

                                    @Override
                                    public void failure(RetrofitError error) {
                                        consumeTokenErrorResponse(start, error);
                                    }
                                });
                    } else {
                        apiInterface.get3DSToken(cardTokenRequest.getCardNumber(),
                                cardTokenRequest.getCardCVV(),
                                cardTokenRequest.getCardExpiryMonth(),
                                cardTokenRequest.getCardExpiryYear(),
                                cardTokenRequest.getClientKey(),
                                cardTokenRequest.getBank(),
                                cardTokenRequest.isSecure(),
                                cardTokenRequest.isTwoClick(),
                                cardTokenRequest.getGrossAmount(), new Callback<TokenDetailsResponse>() {
                                    @Override
                                    public void success(TokenDetailsResponse tokenDetailsResponse, Response response) {
                                        consumeTokenSuccesResponse(veritransSDK, start, tokenDetailsResponse);
                                    }

                                    @Override
                                    public void failure(RetrofitError error) {
                                        consumeTokenErrorResponse(start, error);
                                    }
                                });
                    }

                }
            } else {
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_unable_to_connect), Events.TOKENIZE));
                Logger.e(veritransSDK.getContext().getString(R.string.error_unable_to_connect));
                releaseResources();
            }

        } else {
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(Constants.ERROR_SDK_IS_NOT_INITIALIZED, Events.TOKENIZE));
            Logger.e(Constants.ERROR_SDK_IS_NOT_INITIALIZED);
            releaseResources();
        }
    }

    private static void consumeTokenSuccesResponse(VeritransSDK veritransSDK, long start, TokenDetailsResponse tokenDetailsResponse) {
        long end = System.currentTimeMillis();

        if (tokenDetailsResponse != null) {
            if (veritransSDK.isLogEnabled()) {
                displayTokenResponse(tokenDetailsResponse);
            }

            if (tokenDetailsResponse.getStatusCode().trim().equalsIgnoreCase(veritransSDK.getContext().getString(R.string.success_code_200))) {
                VeritransBusProvider.getInstance().post(new GetTokenSuccessEvent(tokenDetailsResponse, Events.TOKENIZE));

                // Track Mixpanel event
                trackMixpanel(KEY_TOKENIZE_SUCCESS, PAYMENT_TYPE_CREDIT_CARD, end - start);
            } else {
                if (!TextUtils.isEmpty(tokenDetailsResponse.getStatusMessage())) {
                    VeritransBusProvider.getInstance().post(new GetTokenFailedEvent(
                            tokenDetailsResponse.getStatusMessage(),
                            tokenDetailsResponse,
                            Events.TOKENIZE));

                    // Track Mixpanel event
                    trackMixpanel(KEY_TOKENIZE_FAILED, PAYMENT_TYPE_CREDIT_CARD, end - start, tokenDetailsResponse.getStatusMessage());
                } else {
                    VeritransBusProvider.getInstance().post(new GetTokenFailedEvent(
                            veritransSDK.getContext().getString(R.string.error_empty_response),
                            tokenDetailsResponse,
                            Events.TOKENIZE
                    ));
                }

            }

        } else {
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_empty_response), Events.TOKENIZE));
            Logger.e(veritransSDK.getContext().getString(R.string.error_empty_response));
        }
        releaseResources();
    }

    private static void consumeTokenErrorResponse(long start, RetrofitError e) {
        long end = System.currentTimeMillis();

        Logger.e("error while getting token : ", "" + e.getMessage());
        if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
            VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.TOKENIZE));
            Logger.i("Error in SSL Certificate. " + e.getMessage());
        } else {
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage(), Events.TOKENIZE));
            Logger.i("General error occurred " + e.getMessage());
        }

        // Track Mixpanel event
        trackMixpanel(KEY_TOKENIZE_FAILED, PAYMENT_TYPE_CREDIT_CARD, end - start, e.getMessage());

        releaseResources();
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
            PaymentAPI apiInterface = VeritransRestAdapter.getMerchantApiClient();
            if (apiInterface != null) {
                String merchantToken = veritransSDK.readAuthenticationToken();
                Logger.i("merchantToken:" + merchantToken);
                if (merchantToken != null) {
                    apiInterface.paymentUsingPermataBank(merchantToken, permataBankTransfer, new Callback<TransactionResponse>() {
                        @Override
                        public void success(TransactionResponse permataBankTransferResponse, Response response) {
                            long end = System.currentTimeMillis();

                            if (permataBankTransferResponse != null) {
                                if (veritransSDK.isLogEnabled()) {
                                    displayResponse(permataBankTransferResponse);
                                }
                                if (permataBankTransferResponse.getStatusCode().trim().equalsIgnoreCase(veritransSDK.getContext().getString(R.string.success_code_200))
                                        || permataBankTransferResponse.getStatusCode().trim().equalsIgnoreCase(veritransSDK.getContext().getString(R.string.success_code_201))) {
                                    VeritransBusProvider.getInstance().post(new TransactionSuccessEvent(permataBankTransferResponse, Events.PAYMENT));

                                    // Track Mixpanel event
                                    trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_BANK_TRANSFER, BANK_PERMATA, end - start);

                                } else {
                                    VeritransBusProvider.getInstance().post(new TransactionFailedEvent(permataBankTransferResponse.getStatusMessage(), permataBankTransferResponse, Events.PAYMENT));

                                    // Track Mixpanel event
                                    trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BANK_TRANSFER, BANK_PERMATA, end - start, permataBankTransferResponse.getStatusCode());
                                }

                            } else {
                                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_empty_response), Events.PAYMENT));
                                Logger.e(veritransSDK.getContext().getString(R.string.error_empty_response));
                            }
                            releaseResources();
                        }

                        @Override
                        public void failure(RetrofitError e) {
                            long end = System.currentTimeMillis();

                            if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                                VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.PAYMENT));
                                Logger.i("Error in SSL Certificate. " + e.getMessage());
                            } else {
                                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage(), Events.PAYMENT));
                                Logger.i("General error occurred " + e.getMessage());
                            }

                            // Track Mixpanel event
                            trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BANK_TRANSFER, BANK_PERMATA, end - start, e.getMessage());
                            releaseResources();
                        }
                    });
                } else {
                    Logger.e(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied));
                    VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied), Events.PAYMENT));
                    releaseResources();
                }
            } else {
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_unable_to_connect), Events.PAYMENT));
                Logger.e(veritransSDK.getContext().getString(R.string.error_unable_to_connect));
                releaseResources();
            }

        } else {
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(Constants.ERROR_SDK_IS_NOT_INITIALIZED, Events.PAYMENT));
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
            PaymentAPI apiInterface = VeritransRestAdapter.getMerchantApiClient();

            if (apiInterface != null) {
                String merchantToken = veritransSDK.readAuthenticationToken();
                Logger.i("merchantToken:" + merchantToken);
                if (merchantToken != null) {
                    apiInterface.paymentUsingBCAVA(merchantToken, bcaBankTransfer, new Callback<TransactionResponse>() {
                        @Override
                        public void success(TransactionResponse bcaBankTransferResponse, Response response) {
                            long end = System.currentTimeMillis();

                            if (bcaBankTransferResponse != null) {
                                if (veritransSDK.isLogEnabled()) {
                                    displayResponse(bcaBankTransferResponse);
                                }

                                if (bcaBankTransferResponse.getStatusCode().trim().equalsIgnoreCase(veritransSDK.getContext().getString(R.string.success_code_200))
                                        || bcaBankTransferResponse.getStatusCode().trim().equalsIgnoreCase(veritransSDK.getContext().getString(R.string.success_code_201))) {
                                    VeritransBusProvider.getInstance().post(new TransactionSuccessEvent(bcaBankTransferResponse, Events.PAYMENT));

                                    // Track Mixpanel Event
                                    trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_BANK_TRANSFER, BANK_BCA, end - start);
                                } else {
                                    VeritransBusProvider.getInstance().post(new TransactionFailedEvent(bcaBankTransferResponse.getStatusMessage(), bcaBankTransferResponse, Events.PAYMENT));

                                    // Track Mixpanel Event
                                    trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BANK_TRANSFER, BANK_BCA, end - start, bcaBankTransferResponse.getStatusMessage());
                                }

                            } else {
                                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_empty_response), Events.PAYMENT));
                                Logger.e(veritransSDK.getContext().getString(R.string.error_empty_response));
                            }
                            releaseResources();
                        }

                        @Override
                        public void failure(RetrofitError e) {
                            long end = System.currentTimeMillis();
                            if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                                VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.PAYMENT));
                                Logger.i("Error in SSL Certificate. " + e.getMessage());
                            } else {
                                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage(), Events.PAYMENT));
                                Logger.i("General error occurred " + e.getMessage());
                            }

                            //Track Mixpanel event
                            trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BANK_TRANSFER, BANK_BCA, end - start, e.getMessage());
                            releaseResources();
                        }
                    });
                } else {
                    Logger.e(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied));
                    VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied), Events.PAYMENT));
                    releaseResources();
                }
            } else {
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_unable_to_connect), Events.PAYMENT));
                Logger.e(veritransSDK.getContext().getString(R.string.error_unable_to_connect));
                releaseResources();
            }

        } else {
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(Constants.ERROR_SDK_IS_NOT_INITIALIZED, Events.PAYMENT));
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
            PaymentAPI apiInterface = VeritransRestAdapter.getMerchantApiClient();
            if (apiInterface != null) {
                String merchantToken = veritransSDK.readAuthenticationToken();
                Logger.i("merchantToken:" + merchantToken);
                if (merchantToken != null) {
                    apiInterface.paymentUsingCard(merchantToken, cardTransfer, new Callback<TransactionResponse>() {
                        @Override
                        public void success(TransactionResponse cardPaymentResponse, Response response) {
                            long end = System.currentTimeMillis();
                            if (cardPaymentResponse != null) {

                                if (cardPaymentResponse.getStatusCode().trim().equalsIgnoreCase(veritransSDK.getContext().getString(R.string.success_code_200))
                                        || cardPaymentResponse.getStatusCode().trim().equalsIgnoreCase(veritransSDK.getContext().getString(R.string.success_code_201))) {
                                    VeritransBusProvider.getInstance().post(new TransactionSuccessEvent(cardPaymentResponse, Events.PAYMENT));

                                    // Track Mixpanel event
                                    trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_CREDIT_CARD, end - start);
                                } else {
                                    VeritransBusProvider.getInstance().post(new TransactionFailedEvent(cardPaymentResponse.getStatusMessage(), cardPaymentResponse, Events.PAYMENT));

                                    // Track Mixpanel event
                                    trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_CREDIT_CARD, end - start, cardPaymentResponse.getStatusMessage());
                                }
                            } else {
                                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_empty_response), Events.PAYMENT));
                            }

                            releaseResources();
                        }

                        @Override
                        public void failure(RetrofitError e) {
                            long end = System.currentTimeMillis();

                            if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                                VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.PAYMENT));
                                Logger.i("Error in SSL Certificate. " + e.getMessage());
                            } else {
                                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage(), Events.PAYMENT));
                                Logger.i("General error occurred " + e.getMessage());
                            }

                            // Track Mixpanel event
                            trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_CREDIT_CARD, end - start, e.getMessage());

                            releaseResources();
                        }
                    });

                } else {
                    VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied), Events.PAYMENT));
                    Logger.e(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied));
                    releaseResources();
                }
            }

        } else {
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(Constants.ERROR_SDK_IS_NOT_INITIALIZED, Events.PAYMENT));
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
            PaymentAPI apiInterface = VeritransRestAdapter.getMerchantApiClient();
            if (apiInterface != null) {
                String merchantToken = veritransSDK.readAuthenticationToken();
                Logger.i("merchantToken:" + merchantToken);
                if (merchantToken != null) {
                    apiInterface.paymentUsingMandiriClickPay(merchantToken, mandiriClickPayRequestModel, new Callback<TransactionResponse>() {
                        @Override
                        public void success(TransactionResponse mandiriTransferResponse, Response response) {
                            long end = System.currentTimeMillis();

                            if (mandiriTransferResponse != null) {
                                if (veritransSDK.isLogEnabled()) {
                                    displayResponse(mandiriTransferResponse);
                                }
                                if (mandiriTransferResponse.getStatusCode().trim().equalsIgnoreCase(veritransSDK.getContext().getString(R.string.success_code_200))
                                        || mandiriTransferResponse.getStatusCode().trim().equalsIgnoreCase(veritransSDK.getContext().getString(R.string.success_code_201))) {
                                    VeritransBusProvider.getInstance().post(new TransactionSuccessEvent(mandiriTransferResponse, Events.PAYMENT));

                                    // Track Mixpanel event
                                    trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_MANDIRI_CLICKPAY, end - start);
                                } else {
                                    VeritransBusProvider.getInstance().post(new TransactionFailedEvent(mandiriTransferResponse.getStatusMessage(), mandiriTransferResponse, Events.PAYMENT));

                                    // Track Mixpanel event
                                    trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_MANDIRI_CLICKPAY, end - start, mandiriTransferResponse.getStatusMessage());
                                }
                            } else {
                                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_empty_response), Events.PAYMENT));
                                Logger.e(veritransSDK.getContext().getString(R.string.error_empty_response), null);
                            }

                            releaseResources();
                        }

                        @Override
                        public void failure(RetrofitError e) {
                            long end = System.currentTimeMillis();

                            if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                                VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.PAYMENT));
                                Logger.i("Error in SSL Certificate. " + e.getMessage());
                            } else {
                                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage(), Events.PAYMENT));
                                Logger.i("General error occurred " + e.getMessage());
                            }

                            // Track Mixpanel event
                            trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_MANDIRI_CLICKPAY, end - start, e.getMessage());

                            releaseResources();
                        }
                    });

                } else {
                    VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied), Events.PAYMENT));
                    Logger.e(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied));
                    releaseResources();
                }
            } else {
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_unable_to_connect), Events.PAYMENT));
                Logger.e(veritransSDK.getContext().getString(R.string.error_unable_to_connect));
                releaseResources();
            }

        } else {
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(Constants.ERROR_SDK_IS_NOT_INITIALIZED, Events.PAYMENT));
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
            PaymentAPI apiInterface = VeritransRestAdapter.getMerchantApiClient();
            if (apiInterface != null) {
                String merchantToken = veritransSDK.readAuthenticationToken();
                Logger.i("merchantToken:" + merchantToken);
                if (merchantToken != null) {
                    apiInterface.paymentUsingBCAKlikPay(merchantToken, bcaKlikPayModel, new Callback<TransactionResponse>() {
                        @Override
                        public void success(TransactionResponse bcaKlikPayResponse, Response response) {
                            long end = System.currentTimeMillis();

                            if (bcaKlikPayResponse != null) {
                                if (veritransSDK.isLogEnabled()) {
                                    displayResponse(bcaKlikPayResponse);
                                }
                                if (bcaKlikPayResponse.getStatusCode().trim().equalsIgnoreCase(veritransSDK.getContext().getString(R.string.success_code_200))
                                        || bcaKlikPayResponse.getStatusCode().trim().equalsIgnoreCase(veritransSDK.getContext().getString(R.string.success_code_201))) {
                                    VeritransBusProvider.getInstance().post(new TransactionSuccessEvent(bcaKlikPayResponse, Events.PAYMENT));

                                    // Track Mixpanel event
                                    trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_BCA_KLIKPAY, end - start);
                                } else {
                                    VeritransBusProvider.getInstance().post(new TransactionFailedEvent(bcaKlikPayResponse.getStatusMessage(), bcaKlikPayResponse, Events.PAYMENT));

                                    // Track Mixpanel event
                                    trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BCA_KLIKPAY, end - start, bcaKlikPayResponse.getStatusMessage());
                                }

                            } else {
                                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_empty_response), Events.PAYMENT));
                                Logger.e(veritransSDK.getContext().getString(R.string.error_empty_response), null);
                            }
                            releaseResources();
                        }

                        @Override
                        public void failure(RetrofitError e) {
                            long end = System.currentTimeMillis();

                            if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                                VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.PAYMENT));
                                Logger.i("Error in SSL Certificate. " + e.getMessage());
                            } else {
                                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage(), Events.PAYMENT));
                                Logger.i("General error occurred " + e.getMessage());
                            }

                            // Track Mixpanel event
                            trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BCA_KLIKPAY, end - start, e.getMessage());

                            releaseResources();
                        }
                    });
                } else {
                    VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied), Events.PAYMENT));
                    Logger.e(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied));
                    releaseResources();
                }
            } else {
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_unable_to_connect), Events.PAYMENT));
                Logger.e(veritransSDK.getContext().getString(R.string.error_unable_to_connect));
                releaseResources();
            }

        } else {
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(Constants.ERROR_SDK_IS_NOT_INITIALIZED, Events.PAYMENT));
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
            PaymentAPI apiInterface = VeritransRestAdapter.getMerchantApiClient();
            if (apiInterface != null) {
                String merchantToken = veritransSDK.readAuthenticationToken();
                Logger.i("merchantToken:" + merchantToken);
                if (merchantToken != null) {
                    apiInterface.paymentUsingMandiriBillPay(merchantToken, mandiriBillPayTransferModel, new Callback<TransactionResponse>() {
                        @Override
                        public void success(TransactionResponse transactionResponse, Response response) {
                            long end = System.currentTimeMillis();

                            if (transactionResponse != null) {
                                if (veritransSDK.isLogEnabled()) {
                                    displayResponse(transactionResponse);
                                }
                                if (transactionResponse.getStatusCode().trim().equalsIgnoreCase(veritransSDK.getContext().getString(R.string.success_code_200))
                                        || transactionResponse.getStatusCode().trim().equalsIgnoreCase(veritransSDK.getContext().getString(R.string.success_code_201))) {
                                    VeritransBusProvider.getInstance().post(new TransactionSuccessEvent(transactionResponse, Events.PAYMENT));

                                    // Track Mixpanel event
                                    trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_BANK_TRANSFER, BANK_MANDIRI, end - start);
                                } else {
                                    VeritransBusProvider.getInstance().post(new TransactionFailedEvent(transactionResponse.getStatusMessage(), transactionResponse, Events.PAYMENT));

                                    // Track Mixpanel event
                                    trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BANK_TRANSFER, BANK_MANDIRI, end - start, transactionResponse.getStatusMessage());
                                }

                            } else {
                                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_empty_response), Events.PAYMENT));
                                Logger.e(veritransSDK.getContext().getString(R.string.error_empty_response));
                            }

                            releaseResources();
                        }

                        @Override
                        public void failure(RetrofitError e) {
                            long end = System.currentTimeMillis();

                            if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                                VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.PAYMENT));
                                Logger.i("Error in SSL Certificate. " + e.getMessage());
                            } else {
                                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage(), Events.PAYMENT));
                                Logger.i("General error occurred " + e.getMessage());
                            }

                            // Track Mixpanel Event
                            trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BANK_TRANSFER, BANK_MANDIRI, end - start, e.getMessage());

                            releaseResources();
                        }
                    });

                } else {
                    VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied), Events.PAYMENT));
                    Logger.e(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied));
                    releaseResources();
                }
            } else {
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_unable_to_connect), Events.PAYMENT));
                Logger.e(veritransSDK.getContext().getString(R.string.error_unable_to_connect));
                releaseResources();
            }

        } else {
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(Constants.ERROR_SDK_IS_NOT_INITIALIZED, Events.PAYMENT));
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
            PaymentAPI apiInterface = VeritransRestAdapter.getMerchantApiClient();
            if (apiInterface != null) {
                String merchantToken = veritransSDK.readAuthenticationToken();
                Logger.i("merchantToken:" + merchantToken);
                if (merchantToken != null) {
                    apiInterface.paymentUsingCIMBClickPay(merchantToken, cimbClickPayModel, new Callback<TransactionResponse>() {
                        @Override
                        public void success(TransactionResponse cimbPayTransferResponse, Response response) {
                            long end = System.currentTimeMillis();

                            if (cimbPayTransferResponse != null) {
                                if (veritransSDK.isLogEnabled()) {
                                    displayResponse(cimbPayTransferResponse);
                                }
                                if (cimbPayTransferResponse.getStatusCode().trim()
                                        .equalsIgnoreCase(veritransSDK.getContext().getString(R.string.success_code_200))
                                        || cimbPayTransferResponse.getStatusCode()
                                        .trim().equalsIgnoreCase(veritransSDK.getContext().getString(R.string.success_code_201))) {
                                    VeritransBusProvider.getInstance().post(new TransactionSuccessEvent(cimbPayTransferResponse, Events.PAYMENT));

                                    // Track Mixpanel event
                                    trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_CIMB_CLICK, end - start);
                                } else {
                                    VeritransBusProvider.getInstance().post(new TransactionFailedEvent(
                                            cimbPayTransferResponse.getStatusMessage(),
                                            cimbPayTransferResponse,
                                            Events.PAYMENT
                                    ));

                                    // Track Mixpanel event
                                    trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_CIMB_CLICK, end - start, cimbPayTransferResponse.getStatusMessage());
                                }
                            } else {
                                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_empty_response), Events.PAYMENT));
                                Logger.e(veritransSDK.getContext().getString(R.string.error_empty_response));
                            }

                            releaseResources();
                        }

                        @Override
                        public void failure(RetrofitError e) {
                            long end = System.currentTimeMillis();

                            if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                                VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.PAYMENT));
                                Logger.i("Error in SSL Certificate. " + e.getMessage());
                            } else {
                                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage(), Events.PAYMENT));
                                Logger.i("General error occurred " + e.getMessage());
                            }

                            // Track Mixpanel event
                            trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_CIMB_CLICK, end - start, e.getMessage());

                            releaseResources();
                        }
                    });
                } else {
                    VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied), Events.PAYMENT));
                    Logger.e(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied));
                    releaseResources();
                }
            } else {
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_unable_to_connect), Events.PAYMENT));
                Logger.e(veritransSDK.getContext().getString(R.string.error_unable_to_connect));
                releaseResources();
            }
        } else {
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(Constants.ERROR_SDK_IS_NOT_INITIALIZED, Events.PAYMENT));
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
            PaymentAPI apiInterface = VeritransRestAdapter.getMerchantApiClient();
            if (apiInterface != null) {
                String merchantToken = veritransSDK.readAuthenticationToken();
                Logger.i("merchantToken:" + merchantToken);
                if (merchantToken != null) {
                    apiInterface.paymentUsingMandiriECash(merchantToken, mandiriECashModel, new Callback<TransactionResponse>() {
                        @Override
                        public void success(TransactionResponse transferResponse, Response response) {
                            long end = System.currentTimeMillis();

                            if (transferResponse != null) {
                                if (veritransSDK.isLogEnabled()) {
                                    displayResponse(transferResponse);
                                }
                                if (transferResponse.getStatusCode().trim()
                                        .equalsIgnoreCase(veritransSDK.getContext().getString(R.string.success_code_200))
                                        || transferResponse.getStatusCode()
                                        .trim().equalsIgnoreCase(veritransSDK.getContext().getString(R.string.success_code_201))) {
                                    VeritransBusProvider.getInstance().post(new TransactionSuccessEvent(transferResponse, Events.PAYMENT));

                                    // Track Mixpanel event
                                    trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_MANDIRI_ECASH, end - start);
                                } else {
                                    VeritransBusProvider.getInstance().post(new TransactionFailedEvent(
                                            transferResponse.getStatusMessage(),
                                            transferResponse,
                                            Events.PAYMENT
                                    ));

                                    // Track Mixpanel event
                                    trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_MANDIRI_ECASH, end - start, transferResponse.getStatusMessage());
                                }
                            } else {
                                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_empty_response), Events.PAYMENT));
                                Logger.e(veritransSDK.getContext().getString(R.string.error_empty_response));
                            }

                            releaseResources();
                        }

                        @Override
                        public void failure(RetrofitError e) {
                            long end = System.currentTimeMillis();
                            if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                                VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.PAYMENT));
                                Logger.i("Error in SSL Certificate. " + e.getMessage());
                            } else {
                                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage(), Events.PAYMENT));
                                Logger.i("General error occurred " + e.getMessage());
                            }

                            // Track Mixpanel event
                            trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_MANDIRI_ECASH, end - start, e.getMessage());

                            releaseResources();
                        }
                    });
                } else {
                    VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied), Events.PAYMENT));
                    Logger.e(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied));
                    releaseResources();
                }
            } else {
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_unable_to_connect), Events.PAYMENT));
                Logger.e(veritransSDK.getContext().getString(R.string.error_unable_to_connect));
                releaseResources();
            }
        } else {
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(Constants.ERROR_SDK_IS_NOT_INITIALIZED, Events.PAYMENT));
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
            PaymentAPI apiInterface = VeritransRestAdapter.getMerchantApiClient();
            if (apiInterface != null) {
                String merchantToken = veritransSDK.readAuthenticationToken();
                Logger.i("merchantToken:" + merchantToken);
                if (merchantToken != null) {
                    apiInterface.paymentUsingEpayBri(merchantToken, epayBriTransfer, new Callback<TransactionResponse>() {
                        @Override
                        public void success(TransactionResponse epayBriTransferResponse, Response response) {
                            long end = System.currentTimeMillis();
                            if (epayBriTransferResponse != null) {
                                if (veritransSDK.isLogEnabled()) {
                                    displayResponse(epayBriTransferResponse);
                                }
                                if (epayBriTransferResponse.getStatusCode().trim().equalsIgnoreCase(veritransSDK.getContext().getString(R.string.success_code_200))
                                        || epayBriTransferResponse.getStatusCode().trim().equalsIgnoreCase(veritransSDK.getContext().getString(R.string.success_code_201))) {
                                    VeritransBusProvider.getInstance().post(new TransactionSuccessEvent(epayBriTransferResponse, Events.PAYMENT));

                                    // Track Mixpanel event
                                    trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_BRI_EPAY, end - start);
                                } else {
                                    VeritransBusProvider.getInstance().post(new TransactionFailedEvent(epayBriTransferResponse.getStatusMessage(), epayBriTransferResponse, Events.PAYMENT));

                                    // Track Mixpanel event
                                    trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BRI_EPAY, end - start, epayBriTransferResponse.getStatusMessage());
                                }

                            } else {
                                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_empty_response), Events.PAYMENT));
                                Logger.e(veritransSDK.getContext().getString(R.string.error_empty_response));
                            }

                            releaseResources();
                        }

                        @Override
                        public void failure(RetrofitError e) {
                            long end = System.currentTimeMillis();

                            if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                                VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.PAYMENT));
                                Logger.i("Error in SSL Certificate. " + e.getMessage());
                            } else {
                                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage(), Events.PAYMENT));
                                Logger.i("General error occurred " + e.getMessage());
                            }

                            // Track Mixpanel event
                            trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BRI_EPAY, end - start, e.getMessage());

                            releaseResources();
                        }
                    });

                } else {
                    VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied), Events.PAYMENT));
                    Logger.e(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied));
                    releaseResources();
                }
            } else {
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_unable_to_connect), Events.PAYMENT));
                Logger.e(veritransSDK.getContext().getString(R.string.error_unable_to_connect));
                releaseResources();
            }

        } else {
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(Constants.ERROR_SDK_IS_NOT_INITIALIZED, Events.PAYMENT));
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
            PaymentAPI apiInterface = VeritransRestAdapter.getMerchantApiClient();
            if (apiInterface != null) {
                String merchantToken = veritransSDK.readAuthenticationToken();
                Logger.i("merchantToken:" + merchantToken);
                if (merchantToken != null) {
                    apiInterface.transactionStatus(merchantToken, id, new Callback<TransactionStatusResponse>() {
                        @Override
                        public void success(TransactionStatusResponse transactionStatusResponse, Response response) {
                            if (transactionStatusResponse != null) {
                                if (TextUtils.isEmpty(transactionStatusResponse.getStatusCode())) {
                                    if (transactionStatusResponse.getStatusCode().equalsIgnoreCase(veritransSDK.getContext().getString(R.string.success_code_200))
                                            || transactionStatusResponse.getStatusCode().equalsIgnoreCase(veritransSDK.getContext().getString(R.string.success_code_201))) {
                                        VeritransBusProvider.getInstance().post(new TransactionStatusSuccessEvent(transactionStatusResponse, Events.PAYMENT));
                                    }
                                } else {
                                    VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_empty_response), Events.PAYMENT));
                                }
                            } else {
                                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_empty_response), Events.PAYMENT));
                            }
                            releaseResources();
                        }

                        @Override
                        public void failure(RetrofitError e) {
                            if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                                VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.PAYMENT));
                                Logger.i("Error in SSL Certificate. " + e.getMessage());
                            } else {
                                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage(), Events.PAYMENT));
                                Logger.i("General error occurred " + e.getMessage());
                            }
                            releaseResources();
                        }
                    });

                } else {
                    VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied), Events.PAYMENT));
                    Logger.e(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied));
                    releaseResources();
                }
            } else {
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_unable_to_connect), Events.PAYMENT));
                Logger.e(veritransSDK.getContext().getString(R.string.error_unable_to_connect));
                releaseResources();
            }

        } else {
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(Constants.ERROR_SDK_IS_NOT_INITIALIZED, Events.PAYMENT));
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
            PaymentAPI apiInterface = VeritransRestAdapter.getMerchantApiClient();
            if (apiInterface != null) {
                String merchantToken = veritransSDK.readAuthenticationToken();
                Logger.i("merchantToken:" + merchantToken);
                if (merchantToken != null) {
                    apiInterface.paymentUsingIndosatDompetku(merchantToken, indosatDompetkuRequest, new Callback<TransactionResponse>() {
                        @Override
                        public void success(TransactionResponse transactionResponse, Response response) {
                            long end = System.currentTimeMillis();

                            if (transactionResponse != null) {
                                if (veritransSDK.isLogEnabled()) {
                                    displayResponse(transactionResponse);
                                }
                                if (transactionResponse.getStatusCode().trim().equalsIgnoreCase(veritransSDK.getContext().getString(R.string.success_code_200))
                                        || transactionResponse.getStatusCode().trim().equalsIgnoreCase(veritransSDK.getContext().getString(R.string.success_code_201))) {
                                    VeritransBusProvider.getInstance().post(new TransactionSuccessEvent(transactionResponse, Events.PAYMENT));

                                    // Track Mixpanel event
                                    trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_INDOSAT_DOMPETKU, end - start);
                                } else {
                                    VeritransBusProvider.getInstance().post(new TransactionFailedEvent(transactionResponse.getStatusMessage(), transactionResponse, Events.PAYMENT));

                                    // Track Mixpanel event
                                    trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_INDOSAT_DOMPETKU, end - start, transactionResponse.getStatusMessage());
                                }
                            } else {
                                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_empty_response), Events.PAYMENT));
                                Logger.e(veritransSDK.getContext().getString(R.string.error_empty_response));
                            }

                            releaseResources();
                        }

                        @Override
                        public void failure(RetrofitError e) {
                            long end = System.currentTimeMillis();

                            if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                                VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.PAYMENT));
                                Logger.i("Error in SSL Certificate. " + e.getMessage());
                            } else {
                                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage(), Events.PAYMENT));
                                Logger.i("General error occurred " + e.getMessage());
                            }

                            // Track Mixpanel event
                            trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_INDOSAT_DOMPETKU, end - start, e.getMessage());

                            releaseResources();
                        }
                    });

                } else {
                    VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied), Events.PAYMENT));
                    Logger.e(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied));
                    releaseResources();
                }
            } else {
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_unable_to_connect), Events.PAYMENT));
                Logger.e(veritransSDK.getContext().getString(R.string.error_unable_to_connect));
                releaseResources();
            }

        } else {
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(Constants.ERROR_SDK_IS_NOT_INITIALIZED, Events.PAYMENT));
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
            PaymentAPI apiInterface = VeritransRestAdapter.getMerchantApiClient();
            if (apiInterface != null) {
                String merchantToken = veritransSDK.readAuthenticationToken();
                Logger.i("merchantToken:" + merchantToken);
                if (merchantToken != null) {
                    apiInterface.paymentUsingIndomaret(merchantToken, indomaretRequestModel, new Callback<TransactionResponse>() {
                        @Override
                        public void success(TransactionResponse indomaretTransferResponse, Response response) {
                            long end = System.currentTimeMillis();

                            if (indomaretTransferResponse != null) {
                                if (veritransSDK.isLogEnabled()) {
                                    displayResponse(indomaretTransferResponse);
                                }
                                if (indomaretTransferResponse.getStatusCode().trim().equalsIgnoreCase(veritransSDK.getContext().getString(R.string.success_code_200))
                                        || indomaretTransferResponse.getStatusCode().trim().equalsIgnoreCase(veritransSDK.getContext().getString(R.string.success_code_201))) {
                                    VeritransBusProvider.getInstance().post(new TransactionSuccessEvent(indomaretTransferResponse, Events.PAYMENT));

                                    // Track Mixpanel event
                                    trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_INDOMARET, end - start);
                                } else {
                                    VeritransBusProvider.getInstance().post(new TransactionFailedEvent(indomaretTransferResponse.getStatusMessage(), indomaretTransferResponse, Events.PAYMENT));

                                    // Track Mixpanel event
                                    trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_INDOMARET, end - start, indomaretTransferResponse.getStatusMessage());
                                }

                            } else {
                                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_empty_response), Events.PAYMENT));
                                Logger.e(veritransSDK.getContext().getString(R.string.error_empty_response));
                            }
                            releaseResources();
                        }

                        @Override
                        public void failure(RetrofitError e) {
                            long end = System.currentTimeMillis();

                            if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                                VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.PAYMENT));
                                Logger.i("Error in SSL Certificate. " + e.getMessage());
                            } else {
                                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage(), Events.PAYMENT));
                                Logger.i("General error occurred " + e.getMessage());
                            }

                            // Track Mixpanel event
                            trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_INDOMARET, end - start, e.getMessage());

                            releaseResources();
                        }
                    });

                } else {
                    VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied), Events.PAYMENT));
                    Logger.e(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied));
                    releaseResources();
                }
            } else {
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_unable_to_connect), Events.PAYMENT));
                Logger.e(veritransSDK.getContext().getString(R.string.error_unable_to_connect));
                releaseResources();
            }

        } else {
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(Constants.ERROR_SDK_IS_NOT_INITIALIZED, Events.PAYMENT));
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
            PaymentAPI apiInterface = VeritransRestAdapter.getMerchantApiClient();

            if (apiInterface != null) {
                String merchantToken = veritransSDK.readAuthenticationToken();
                Logger.i("merchantToken:" + merchantToken);
                if (merchantToken != null) {
                    apiInterface.paymentUsingBBMMoney(merchantToken, bbmMoneyRequestModel, new Callback<TransactionResponse>() {
                        @Override
                        public void success(TransactionResponse bbmMoneyTransferResponse, Response response) {
                            long end = System.currentTimeMillis();

                            if (bbmMoneyTransferResponse != null) {
                                if (veritransSDK.isLogEnabled()) {
                                    displayResponse(bbmMoneyTransferResponse);
                                }
                                if (bbmMoneyTransferResponse.getStatusCode().trim().equalsIgnoreCase(veritransSDK.getContext().getString(R.string.success_code_200))
                                        || bbmMoneyTransferResponse.getStatusCode().trim().equalsIgnoreCase(veritransSDK.getContext().getString(R.string.success_code_201))) {
                                    VeritransBusProvider.getInstance().post(new TransactionSuccessEvent(bbmMoneyTransferResponse, Events.PAYMENT));

                                    // Track Mixpanel event
                                    trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_BBM_MONEY, end - start);
                                } else {
                                    VeritransBusProvider.getInstance().post(new TransactionFailedEvent(bbmMoneyTransferResponse.getStatusMessage(), bbmMoneyTransferResponse, Events.PAYMENT));

                                    // Track Mixpanel event
                                    trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BBM_MONEY, end - start, bbmMoneyTransferResponse.getStatusMessage());
                                }

                            } else {
                                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_empty_response), Events.PAYMENT));
                                Logger.e(veritransSDK.getContext().getString(R.string.error_empty_response));
                            }

                            releaseResources();

                        }

                        @Override
                        public void failure(RetrofitError e) {
                            long end = System.currentTimeMillis();

                            if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                                VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.PAYMENT));
                                Logger.i("Error in SSL Certificate. " + e.getMessage());
                            } else {
                                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage(), Events.PAYMENT));
                                Logger.i("General error occurred " + e.getMessage());
                            }

                            // Track Mixpanel event
                            trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BBM_MONEY, end - start, e.getMessage());

                            releaseResources();
                        }
                    });

                } else {
                    VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied), Events.PAYMENT));
                    Logger.e(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied));
                    releaseResources();
                }
            } else {
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_unable_to_connect), Events.PAYMENT));
                Logger.e(veritransSDK.getContext().getString(R.string.error_unable_to_connect));
                releaseResources();
            }

        } else {
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(Constants.ERROR_SDK_IS_NOT_INITIALIZED, Events.PAYMENT));
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
            PaymentAPI apiInterface = VeritransRestAdapter.getMerchantApiClient();
            if (apiInterface != null) {
                String auth = veritransSDK.readAuthenticationToken();
                Logger.i("Authentication token:" + auth);
                if (auth != null && !auth.equals("")) {
                    apiInterface.saveCard(auth, cardTokenRequest, new Callback<SaveCardResponse>() {
                        @Override
                        public void success(SaveCardResponse cardResponse, Response response) {
                            if (cardResponse != null) {
                                if (cardResponse.getCode() == 200 || cardResponse.getCode() == 201) {
                                    VeritransBusProvider.getInstance().post(new SaveCardSuccessEvent(cardResponse, Events.REGISTER_CARD));
                                } else {
                                    VeritransBusProvider.getInstance().post(new SaveCardFailedEvent(cardResponse.getStatus(), cardResponse, Events.REGISTER_CARD));
                                }
                            } else {
                                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_empty_response), Events.REGISTER_CARD));
                                Logger.e(veritransSDK.getContext().getString(R.string.error_empty_response));
                            }
                            releaseResources();
                        }

                        @Override
                        public void failure(RetrofitError e) {
                            if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                                VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.REGISTER_CARD));
                                Logger.i("Error in SSL Certificate. " + e.getMessage());
                            } else {
                                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage(), Events.REGISTER_CARD));
                                Logger.i("General error occurred " + e.getMessage());
                            }
                            releaseResources();
                        }
                    });

                } else {
                    VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied), Events.REGISTER_CARD));
                    Logger.e(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied));
                    releaseResources();
                }
            } else {
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_unable_to_connect), Events.REGISTER_CARD));
                Logger.e(veritransSDK.getContext().getString(R.string.error_unable_to_connect));
                releaseResources();
            }

        } else {
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(Constants.ERROR_SDK_IS_NOT_INITIALIZED, Events.REGISTER_CARD));
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
            PaymentAPI apiInterface = VeritransRestAdapter.getMerchantApiClient();

            if (apiInterface != null) {
                String auth = veritransSDK.readAuthenticationToken();
                Logger.i("Authentication token:" + auth);
                if (auth != null && !auth.equals("")) {
                    apiInterface.getCard(auth, new Callback<CardResponse>() {
                        @Override
                        public void success(CardResponse cardResponse, Response response) {
                            if (cardResponse != null) {
                                if (cardResponse.getCode() == 200) {
                                    VeritransBusProvider.getInstance().post(new GetCardsSuccessEvent(cardResponse, Events.GET_CARD));
                                } else {
                                    VeritransBusProvider.getInstance().post(new GetCardFailedEvent(cardResponse.getStatus(), cardResponse, Events.GET_CARD));
                                }
                            } else {
                                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_empty_response), Events.GET_CARD));
                                Logger.e(veritransSDK.getContext().getString(R.string.error_empty_response));
                            }
                            releaseResources();
                        }

                        @Override
                        public void failure(RetrofitError e) {
                            if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                                VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.GET_CARD));
                                Logger.i("Error in SSL Certificate. " + e.getMessage());
                            } else {
                                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage(), Events.GET_CARD));
                                Logger.i("General error occurred " + e.getMessage());
                            }
                            releaseResources();
                        }
                    });
                } else {
                    VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied), Events.GET_CARD));
                    Logger.e(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied));
                    releaseResources();
                }
            } else {
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_unable_to_connect), Events.GET_CARD));
                Logger.e(veritransSDK.getContext().getString(R.string.error_unable_to_connect));
                releaseResources();
            }

        } else {
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(Constants.ERROR_SDK_IS_NOT_INITIALIZED, Events.GET_CARD));
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
            PaymentAPI apiInterface = VeritransRestAdapter.getMerchantApiClient();
            if (apiInterface != null) {
                String auth = veritransSDK.readAuthenticationToken();
                Logger.i("Authentication token:" + auth);
                if (auth != null) {
                    apiInterface.deleteCard(auth, creditCard.getSavedTokenId(), new Callback<DeleteCardResponse>() {
                        @Override
                        public void success(DeleteCardResponse deleteCardResponse, Response response) {
                            if (deleteCardResponse != null) {
                                if (deleteCardResponse.getCode() == 200 || deleteCardResponse.getCode() == 204) {
                                    VeritransBusProvider.getInstance().post(new DeleteCardSuccessEvent(deleteCardResponse, Events.DELETE_CARD));
                                } else {
                                    VeritransBusProvider.getInstance().post(new DeleteCardFailedEvent(deleteCardResponse.getMessage(), deleteCardResponse, Events.DELETE_CARD));
                                }
                            } else {
                                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_empty_response), Events.DELETE_CARD));
                                Logger.e(veritransSDK.getContext().getString(R.string.error_empty_response));
                            }
                            releaseResources();
                        }

                        @Override
                        public void failure(RetrofitError e) {
                            if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                                VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.DELETE_CARD));
                                Logger.i("Error in SSL Certificate. " + e.getMessage());
                            } else {
                                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage(), Events.DELETE_CARD));
                                Logger.i("General error occurred " + e.getMessage());
                            }
                            releaseResources();
                        }
                    });

                } else {
                    VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied), Events.DELETE_CARD));
                    Logger.e(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied));
                    releaseResources();
                }
            } else {
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_unable_to_connect), Events.DELETE_CARD));
                Logger.e(veritransSDK.getContext().getString(R.string.error_unable_to_connect));
                releaseResources();
            }
        } else {
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(Constants.ERROR_SDK_IS_NOT_INITIALIZED, Events.DELETE_CARD));
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
            PaymentAPI apiInterface = VeritransRestAdapter.getMerchantApiClient();
            if (apiInterface != null) {
                String merchantToken = veritransSDK.readAuthenticationToken();
                Logger.i("merchantToken:" + merchantToken);
                if (merchantToken != null) {
                    apiInterface.getOffers(merchantToken, new Callback<GetOffersResponseModel>() {
                        @Override
                        public void success(GetOffersResponseModel getOffersResponseModel, Response response) {
                            if (getOffersResponseModel != null) {
                                if (getOffersResponseModel.getMessage().equalsIgnoreCase(veritransSDK.getContext().getString(R.string.success))) {
                                    VeritransBusProvider.getInstance().post(new GetOfferSuccessEvent(getOffersResponseModel, Events.GET_OFFER));
                                } else {
                                    VeritransBusProvider.getInstance().post(new GetOfferFailedEvent(getOffersResponseModel.getMessage(), getOffersResponseModel, Events.GET_OFFER));
                                }
                            } else {
                                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_empty_response), Events.GET_OFFER));
                                Logger.e(veritransSDK.getContext().getString(R.string.error_empty_response));
                            }

                            releaseResources();
                        }

                        @Override
                        public void failure(RetrofitError e) {
                            if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                                VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.GET_OFFER));
                                Logger.i("Error in SSL Certificate. " + e.getMessage());
                            } else {
                                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage(), Events.GET_OFFER));
                                Logger.i("General error occurred " + e.getMessage());
                            }

                            releaseResources();
                        }
                    });
                } else {
                    VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied), Events.GET_OFFER));
                    Logger.e(veritransSDK.getContext().getString(R.string.error_invalid_data_supplied));
                    releaseResources();
                }
            } else {
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_unable_to_connect), Events.GET_OFFER));
                Logger.e(veritransSDK.getContext().getString(R.string.error_unable_to_connect));
                releaseResources();
            }

        } else {
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(Constants.ERROR_SDK_IS_NOT_INITIALIZED, Events.GET_OFFER));
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
            PaymentAPI paymentAPI = VeritransRestAdapter.getMerchantApiClient();
            if (paymentAPI != null) {
                paymentAPI.getAuthenticationToken(new Callback<AuthModel>() {
                    @Override
                    public void success(AuthModel authModel, Response response) {
                        VeritransBusProvider.getInstance().post(new AuthenticationEvent(authModel));
                        releaseResources();
                }

                    @Override
                    public void failure(RetrofitError e) {
                        if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                            VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.AUTHENTICATION));
                            Logger.i("Error in SSL Certificate. " + e.getMessage());
                        } else {
                            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage(), Events.AUTHENTICATION));
                            Logger.i("General error occurred " + e.getMessage());
                        }
                        releaseResources();
                    }
                });

            } else {
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(veritransSDK.getContext().getString(R.string.error_unable_to_connect), Events.AUTHENTICATION));
                Logger.e(veritransSDK.getContext().getString(R.string.error_unable_to_connect));
                releaseResources();
            }
        } else {
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(Constants.ERROR_SDK_IS_NOT_INITIALIZED, Events.AUTHENTICATION));
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