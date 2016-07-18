package id.co.veritrans.sdk.coreflow.core;

import android.content.Context;
import android.text.TextUtils;

import java.security.cert.CertPathValidatorException;

import javax.net.ssl.SSLHandshakeException;

import id.co.veritrans.sdk.coreflow.R;
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
import id.co.veritrans.sdk.coreflow.models.KlikBCAModel;
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
public class TransactionManager extends BaseTransactionManager{




    public TransactionManager(Context context, VeritransRestAPI veritransPaymentAPI,
                              MerchantRestAPI merchantPaymentAPI) {
        this.context = context;
        this.veritransPaymentAPI = veritransPaymentAPI;
        this.merchantPaymentAPI = merchantPaymentAPI;
    }


    /**
     * It will execute API call to get token from Veritrans that can be used later.
     *
     * @param cardNumber   credit card number
     * @param cardCvv      credit card CVV number
     * @param cardExpMonth credit card expired month
     * @param cardExpYear  credit card expired year
     */
    public void cardRegistration(String cardNumber,
                                 String cardCvv,
                                 String cardExpMonth,
                                 String cardExpYear, String clientKey) {
            veritransPaymentAPI.registerCard(cardNumber, cardCvv, cardExpMonth, cardExpYear, clientKey, new Callback<CardRegistrationResponse>() {
                @Override
                public void success(CardRegistrationResponse cardRegistrationResponse, Response response) {
                    releaseResources();

                    if (cardRegistrationResponse != null) {
                        if (cardRegistrationResponse.getStatusCode().equals(context.getString(R.string.success_code_200))) {
                            VeritransBusProvider.getInstance().post(new CardRegistrationSuccessEvent(cardRegistrationResponse, Events.CARD_REGISTRATION));
                        } else {
                            VeritransBusProvider.getInstance().post(new CardRegistrationFailedEvent(cardRegistrationResponse.getStatusMessage(), cardRegistrationResponse, Events.CARD_REGISTRATION));
                        }
                    } else {
                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_empty_response), Events.CARD_REGISTRATION));
                        Logger.e(context.getString(R.string.error_empty_response));
                    }
                }

                @Override
                public void failure(RetrofitError e) {
                    releaseResources();

                    Logger.e("error while getting token : ", "" + e.getMessage());

                    if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                        VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.CARD_REGISTRATION));
                        Logger.i("Error in SSL Certificate. " + e.getMessage());
                    } else {
                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage(), Events.CARD_REGISTRATION));
                        Logger.i("General error occurred " + e.getMessage());
                    }
                }
            });
    }


    /**
     * It will execute an api call to get token from server, and after completion of request it
     * will </p> call appropriate method using registered {@link GetTokenSuccessEvent}.
     *
     * @param cardTokenRequest information about credit card.
     */
    public void getToken(CardTokenRequest cardTokenRequest) {
        final long start = System.currentTimeMillis();
                if (cardTokenRequest.isTwoClick()) {
                    if (cardTokenRequest.isInstalment()) {
                        veritransPaymentAPI.getTokenInstalmentOfferTwoClick(
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
                                        consumeTokenSuccesResponse( start, tokenDetailsResponse);
                                    }

                                    @Override
                                    public void failure(RetrofitError error) {
                                        consumeTokenErrorResponse(start, error);
                                    }
                                });
                    } else {
                        veritransPaymentAPI.getTokenTwoClick(
                                cardTokenRequest.getCardCVV(),
                                cardTokenRequest.getSavedTokenId(),
                                cardTokenRequest.isTwoClick(),
                                cardTokenRequest.isSecure(),
                                cardTokenRequest.getGrossAmount(),
                                cardTokenRequest.getBank(),
                                cardTokenRequest.getClientKey(), new Callback<TokenDetailsResponse>() {
                                    @Override
                                    public void success(TokenDetailsResponse tokenDetailsResponse, Response response) {
                                        consumeTokenSuccesResponse(start, tokenDetailsResponse);
                                    }

                                    @Override
                                    public void failure(RetrofitError error) {
                                        consumeTokenErrorResponse(start, error);
                                    }
                                });
                    }

                } else {
                    if (cardTokenRequest.isInstalment()) {
                        veritransPaymentAPI.get3DSTokenInstalmentOffers(cardTokenRequest.getCardNumber(),
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
                                        consumeTokenSuccesResponse(start, tokenDetailsResponse);

                                    }

                                    @Override
                                    public void failure(RetrofitError error) {
                                        consumeTokenErrorResponse(start, error);
                                    }
                                });
                    } else {
                        //normal request
                        veritransPaymentAPI.get3DSToken(cardTokenRequest.getCardNumber(),
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
                                        consumeTokenSuccesResponse(start, tokenDetailsResponse);
                                    }

                                    @Override
                                    public void failure(RetrofitError error) {
                                        consumeTokenErrorResponse(start, error);
                                    }
                                });
                    }

                }
    }

    private  void consumeTokenSuccesResponse(long start, TokenDetailsResponse tokenDetailsResponse) {
        releaseResources();

        long end = System.currentTimeMillis();

        if (tokenDetailsResponse != null) {
            if (isSDKLogEnabled) {
                displayTokenResponse(tokenDetailsResponse);
            }
            if (tokenDetailsResponse.getStatusCode().trim().equalsIgnoreCase(context.getString(R.string.success_code_200))) {
                VeritransBusProvider.getInstance().post(new GetTokenSuccessEvent(tokenDetailsResponse, Events.TOKENIZE));

                // Track Mixpanel event
                analyticsManager.trackMixpanel(KEY_TOKENIZE_SUCCESS, PAYMENT_TYPE_CREDIT_CARD, end - start);
            } else {

                if (!TextUtils.isEmpty(tokenDetailsResponse.getStatusMessage())) {
                    VeritransBusProvider.getInstance().post(new GetTokenFailedEvent(
                            tokenDetailsResponse.getStatusMessage(),
                            tokenDetailsResponse,
                            Events.TOKENIZE));

                    // Track Mixpanel event
                    analyticsManager.trackMixpanel(KEY_TOKENIZE_FAILED, PAYMENT_TYPE_CREDIT_CARD, end - start, tokenDetailsResponse.getStatusMessage());
                } else {
                    VeritransBusProvider.getInstance().post(new GetTokenFailedEvent(
                            context.getString(R.string.error_empty_response),
                            tokenDetailsResponse,
                            Events.TOKENIZE
                    ));
                }

            }

        } else {
            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_empty_response), Events.TOKENIZE));
            Logger.e(context.getString(R.string.error_empty_response));
        }
    }

    private void consumeTokenErrorResponse(long start, RetrofitError e) {
        releaseResources();
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
        analyticsManager.trackMixpanel(KEY_TOKENIZE_FAILED, PAYMENT_TYPE_CREDIT_CARD, end - start, e.getMessage());

    }

    /**
     * It will execute an api call to perform transaction using permata bank, and after
     * completion of request it will </p> call appropriate method using registered {@link TransactionSuccessEvent}
     * or {@link TransactionFailedEvent}.
     *
     * @param permataBankTransfer information required perform transaction using permata bank
     */
    public void paymentUsingPermataBank(final PermataBankTransfer permataBankTransfer, String authenticationToken) {
        final long start = System.currentTimeMillis();
                String merchantToken = authenticationToken;
                Logger.i("merchantToken:" + merchantToken);
                if (merchantToken != null) {
                    merchantPaymentAPI.paymentUsingPermataBank(merchantToken, permataBankTransfer, new Callback<TransactionResponse>() {
                        @Override
                        public void success(TransactionResponse permataBankTransferResponse, Response response) {
                            releaseResources();

                            long end = System.currentTimeMillis();

                            if (permataBankTransferResponse != null) {
                                if (isSDKLogEnabled) {
                                    displayResponse(permataBankTransferResponse);
                                }

                                if (permataBankTransferResponse.getStatusCode().trim().equalsIgnoreCase(context.getString(R.string.success_code_200))
                                        || permataBankTransferResponse.getStatusCode().trim().equalsIgnoreCase(context.getString(R.string.success_code_201))) {
                                    VeritransBusProvider.getInstance().post(new TransactionSuccessEvent(permataBankTransferResponse, Events.PAYMENT));

                                    // Track Mixpanel event
                                    analyticsManager.trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_BANK_TRANSFER, BANK_PERMATA, end - start);

                                } else {
                                    VeritransBusProvider.getInstance().post(new TransactionFailedEvent(permataBankTransferResponse.getStatusMessage(), permataBankTransferResponse, Events.PAYMENT));

                                    // Track Mixpanel event
                                    analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BANK_TRANSFER, BANK_PERMATA, end - start, permataBankTransferResponse.getStatusCode());
                                }

                            } else {
                                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_empty_response), Events.PAYMENT));
                                Logger.e(context.getString(R.string.error_empty_response));
                            }
                        }

                        @Override
                        public void failure(RetrofitError e) {
                            releaseResources();
                            long end = System.currentTimeMillis();

                            if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                                VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.PAYMENT));
                                Logger.i("Error in SSL Certificate. " + e.getMessage());
                            } else {
                                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage(), Events.PAYMENT));
                                Logger.i("General error occurred " + e.getMessage());
                            }

                            // Track Mixpanel event
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BANK_TRANSFER, BANK_PERMATA, end - start, e.getMessage());
                        }
                    });
                } else {
                    releaseResources();
                    Logger.e(context.getString(R.string.error_invalid_data_supplied));
                    VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_invalid_data_supplied), Events.PAYMENT));
                }
    }

    /**
     * it will execute an api call to perform transaction using permata bank, and after
     * completion of request it will </p> call appropriate method using registered {@link TransactionSuccessEvent} or
     * {@link TransactionFailedEvent}.
     *
     * @param bcaBankTransfer information required perform transaction using BCA bank
     * @param authenticationToken
     */
    public void paymentUsingBCATransfer(final BCABankTransfer bcaBankTransfer, String authenticationToken) {
        final long start = System.currentTimeMillis();

                String merchantToken = authenticationToken;
                Logger.i("merchantToken:" + merchantToken);
                if (merchantToken != null) {
                    merchantPaymentAPI.paymentUsingBCAVA(merchantToken, bcaBankTransfer, new Callback<TransactionResponse>() {
                        @Override
                        public void success(TransactionResponse bcaBankTransferResponse, Response response) {
                            releaseResources();
                            long end = System.currentTimeMillis();

                            if (bcaBankTransferResponse != null) {
                                if (isSDKLogEnabled) {
                                    displayResponse(bcaBankTransferResponse);
                                }

                                if (bcaBankTransferResponse.getStatusCode().trim().equalsIgnoreCase(context.getString(R.string.success_code_200))
                                        || bcaBankTransferResponse.getStatusCode().trim().equalsIgnoreCase(context.getString(R.string.success_code_201))) {
                                    VeritransBusProvider.getInstance().post(new TransactionSuccessEvent(bcaBankTransferResponse, Events.PAYMENT));

                                    // Track Mixpanel Event
                                    analyticsManager.trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_BANK_TRANSFER, BANK_BCA, end - start);
                                } else {
                                    VeritransBusProvider.getInstance().post(new TransactionFailedEvent(bcaBankTransferResponse.getStatusMessage(), bcaBankTransferResponse, Events.PAYMENT));

                                    // Track Mixpanel Event
                                    analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BANK_TRANSFER, BANK_BCA, end - start, bcaBankTransferResponse.getStatusMessage());
                                }

                            } else {
                                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_empty_response), Events.PAYMENT));
                                Logger.e(context.getString(R.string.error_empty_response));
                            }
                        }

                        @Override
                        public void failure(RetrofitError e) {
                            releaseResources();
                            long end = System.currentTimeMillis();
                            if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                                VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.PAYMENT));
                                Logger.i("Error in SSL Certificate. " + e.getMessage());
                            } else {
                                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage(), Events.PAYMENT));
                                Logger.i("General error occurred " + e.getMessage());
                            }

                            //Track Mixpanel event
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BANK_TRANSFER, BANK_BCA, end - start, e.getMessage());
                        }
                    });
                } else {
                    releaseResources();
                    Logger.e(context.getString(R.string.error_invalid_data_supplied));
                    VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_invalid_data_supplied), Events.PAYMENT));
                }
    }

    /**
     * it will execute an api call to perform transaction using credit card, and after
     * completion of request it will </p> call appropriate method using registered {@link TransactionSuccessEvent} or
     * {@link TransactionFailedEvent}.
     *
     * @param cardTransfer information required perform transaction using credit card
     * @param authenticationToken
     */
    public void paymentUsingCard(CardTransfer cardTransfer, String authenticationToken) {
        final long start = System.currentTimeMillis();
            String merchantToken = authenticationToken;
            Logger.i("merchantToken:" + merchantToken);
            if (merchantToken != null) {
                merchantPaymentAPI.paymentUsingCard(merchantToken, cardTransfer, new Callback<TransactionResponse>() {
                    @Override
                    public void success(TransactionResponse cardPaymentResponse, Response response) {
                        releaseResources();

                        long end = System.currentTimeMillis();
                        if (cardPaymentResponse != null) {

                            if (cardPaymentResponse.getStatusCode().trim().equalsIgnoreCase(context.getString(R.string.success_code_200))
                                    || cardPaymentResponse.getStatusCode().trim().equalsIgnoreCase(context.getString(R.string.success_code_201))) {
                                VeritransBusProvider.getInstance().post(new TransactionSuccessEvent(cardPaymentResponse, Events.PAYMENT));

                                // Track Mixpanel event
                                analyticsManager.trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_CREDIT_CARD, end - start);
                            } else {
                                VeritransBusProvider.getInstance().post(new TransactionFailedEvent(cardPaymentResponse.getStatusMessage(), cardPaymentResponse, Events.PAYMENT));

                                // Track Mixpanel event
                                analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_CREDIT_CARD, end - start, cardPaymentResponse.getStatusMessage());
                            }
                        } else {
                            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_empty_response), Events.PAYMENT));
                        }
                    }

                    @Override
                    public void failure(RetrofitError e) {
                        releaseResources();
                        long end = System.currentTimeMillis();

                        if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                            VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.PAYMENT));
                            Logger.i("Error in SSL Certificate. " + e.getMessage());
                        } else {
                            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage(), Events.PAYMENT));
                            Logger.i("General error occurred " + e.getMessage());
                        }

                        // Track Mixpanel event
                        analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_CREDIT_CARD, end - start, e.getMessage());

                    }
                });

            } else {
                releaseResources();
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_invalid_data_supplied), Events.PAYMENT));
                Logger.e(context.getString(R.string.error_invalid_data_supplied));
            }
    }

    /**
     * it will execute an api call to perform transaction using mandiri click pay, and after
     * completion of request it will </p> call appropriate method using registered {@link TransactionSuccessEvent} or
     * {@link TransactionFailedEvent}.
     *
     * @param mandiriClickPayRequestModel information required perform transaction using mandiri click pay.
     */
    public void paymentUsingMandiriClickPay(final MandiriClickPayRequestModel mandiriClickPayRequestModel, String authenticationToken) {
        final long start = System.currentTimeMillis();

            String merchantToken = authenticationToken;
            Logger.i("merchantToken:" + merchantToken);
            if (merchantToken != null) {
                merchantPaymentAPI.paymentUsingMandiriClickPay(merchantToken, mandiriClickPayRequestModel, new Callback<TransactionResponse>() {
                    @Override
                    public void success(TransactionResponse mandiriTransferResponse, Response response) {
                        releaseResources();
                        long end = System.currentTimeMillis();
                        if (mandiriTransferResponse != null) {
                            if (isSDKLogEnabled) {
                                displayResponse(mandiriTransferResponse);
                            }
                            if (mandiriTransferResponse.getStatusCode().trim().equalsIgnoreCase(context.getString(R.string.success_code_200))
                                    || mandiriTransferResponse.getStatusCode().trim().equalsIgnoreCase(context.getString(R.string.success_code_201))) {
                                VeritransBusProvider.getInstance().post(new TransactionSuccessEvent(mandiriTransferResponse, Events.PAYMENT));

                                // Track Mixpanel event
                                analyticsManager.trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_MANDIRI_CLICKPAY, end - start);
                            } else {
                                VeritransBusProvider.getInstance().post(new TransactionFailedEvent(mandiriTransferResponse.getStatusMessage(), mandiriTransferResponse, Events.PAYMENT));

                                // Track Mixpanel event
                                analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_MANDIRI_CLICKPAY, end - start, mandiriTransferResponse.getStatusMessage());
                            }
                        } else {
                            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_empty_response), Events.PAYMENT));
                            Logger.e(context.getString(R.string.error_empty_response), null);
                        }

                    }

                    @Override
                    public void failure(RetrofitError e) {
                        releaseResources();
                        long end = System.currentTimeMillis();

                        if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                            VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.PAYMENT));
                            Logger.i("Error in SSL Certificate. " + e.getMessage());
                        } else {
                            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage(), Events.PAYMENT));
                            Logger.i("General error occurred " + e.getMessage());
                        }

                        // Track Mixpanel event
                        analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_MANDIRI_CLICKPAY, end - start, e.getMessage());

                    }
                });

            } else {
                releaseResources();
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_invalid_data_supplied), Events.PAYMENT));
                Logger.e(context.getString(R.string.error_invalid_data_supplied));
            }

    }

    /**
     * it will execute an api call to perform transaction using BCA KlikPay, and after
     * completion of request it will </p> call appropriate method using registered {@link TransactionSuccessEvent} or
     * {@link TransactionFailedEvent}.
     *
     * @param bcaKlikPayModel information required perform transaction using BCA KlikPay.
     */
    public void paymentUsingBCAKlikPay(final BCAKlikPayModel bcaKlikPayModel, String authenticationToken) {

        final long start = System.currentTimeMillis();
            String merchantToken = authenticationToken;
            Logger.i("merchantToken:" + merchantToken);
            if (merchantToken != null) {
                merchantPaymentAPI.paymentUsingBCAKlikPay(merchantToken, bcaKlikPayModel, new Callback<TransactionResponse>() {
                    @Override
                    public void success(TransactionResponse bcaKlikPayResponse, Response response) {
                        releaseResources();

                        long end = System.currentTimeMillis();

                        if (bcaKlikPayResponse != null) {
                            if (isSDKLogEnabled) {
                                displayResponse(bcaKlikPayResponse);
                            }
                            if (bcaKlikPayResponse.getStatusCode().trim().equalsIgnoreCase(context.getString(R.string.success_code_200))
                                    || bcaKlikPayResponse.getStatusCode().trim().equalsIgnoreCase(context.getString(R.string.success_code_201))) {
                                VeritransBusProvider.getInstance().post(new TransactionSuccessEvent(bcaKlikPayResponse, Events.PAYMENT));

                                // Track Mixpanel event
                                analyticsManager.trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_BCA_KLIKPAY, end - start);
                            } else {
                                VeritransBusProvider.getInstance().post(new TransactionFailedEvent(bcaKlikPayResponse.getStatusMessage(), bcaKlikPayResponse, Events.PAYMENT));

                                // Track Mixpanel event
                                analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BCA_KLIKPAY, end - start, bcaKlikPayResponse.getStatusMessage());
                            }

                        } else {
                            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_empty_response), Events.PAYMENT));
                            Logger.e(context.getString(R.string.error_empty_response), null);
                        }
                    }

                    @Override
                    public void failure(RetrofitError e) {
                        releaseResources();

                        long end = System.currentTimeMillis();

                        if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                            VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.PAYMENT));
                            Logger.i("Error in SSL Certificate. " + e.getMessage());
                        } else {
                            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage(), Events.PAYMENT));
                            Logger.i("General error occurred " + e.getMessage());
                        }
                        // Track Mixpanel event
                        analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BCA_KLIKPAY, end - start, e.getMessage());
                    }
                });
            } else {
                releaseResources();
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_invalid_data_supplied), Events.PAYMENT));
                Logger.e(context.getString(R.string.error_invalid_data_supplied));
            }

    }


    /**
     * it will execute an api call to perform transaction using mandiri bill pay, and after
     * completion of request it will </p> call appropriate method using registered {@link TransactionSuccessEvent} or
     * {@link TransactionFailedEvent}.
     *
     * @param mandiriBillPayTransferModel information required perform transaction using mandiri bill pay.
     * @param authenticationToken
     */
    public void paymentUsingMandiriBillPay(MandiriBillPayTransferModel mandiriBillPayTransferModel, String authenticationToken) {

        final long start = System.currentTimeMillis();

            String merchantToken = authenticationToken;
            Logger.i("merchantToken:" + merchantToken);
            if (merchantToken != null) {
                merchantPaymentAPI.paymentUsingMandiriBillPay(merchantToken, mandiriBillPayTransferModel, new Callback<TransactionResponse>() {
                    @Override
                    public void success(TransactionResponse transactionResponse, Response response) {
                        releaseResources();
                        long end = System.currentTimeMillis();

                        if (transactionResponse != null) {
                            if (isSDKLogEnabled) {
                                displayResponse(transactionResponse);
                            }
                            if (transactionResponse.getStatusCode().trim().equalsIgnoreCase(context.getString(R.string.success_code_200))
                                    || transactionResponse.getStatusCode().trim().equalsIgnoreCase(context.getString(R.string.success_code_201))) {
                                VeritransBusProvider.getInstance().post(new TransactionSuccessEvent(transactionResponse, Events.PAYMENT));

                                // Track Mixpanel event
                                analyticsManager.trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_BANK_TRANSFER, BANK_MANDIRI, end - start);
                            } else {
                                VeritransBusProvider.getInstance().post(new TransactionFailedEvent(transactionResponse.getStatusMessage(), transactionResponse, Events.PAYMENT));

                                // Track Mixpanel event
                                analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BANK_TRANSFER, BANK_MANDIRI, end - start, transactionResponse.getStatusMessage());
                            }

                        } else {
                            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_empty_response), Events.PAYMENT));
                            Logger.e(context.getString(R.string.error_empty_response));
                        }

                    }

                    @Override
                    public void failure(RetrofitError e) {
                        releaseResources();
                        long end = System.currentTimeMillis();

                        if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                            VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.PAYMENT));
                            Logger.i("Error in SSL Certificate. " + e.getMessage());
                        } else {
                            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage(), Events.PAYMENT));
                            Logger.i("General error occurred " + e.getMessage());
                        }

                        // Track Mixpanel Event
                        analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BANK_TRANSFER, BANK_MANDIRI, end - start, e.getMessage());

                    }
                });

            } else {
                releaseResources();
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_invalid_data_supplied), Events.PAYMENT));
                Logger.e(context.getString(R.string.error_invalid_data_supplied));
            }

    }

    /**
     * It will execute API call to pay using CIMB click.
     *
     * @param cimbClickPayModel CIMB click pay model
     * @param authenticationToken
     */
    public void paymentUsingCIMBPay(final CIMBClickPayModel cimbClickPayModel, String authenticationToken) {
        final long start = System.currentTimeMillis();

            String merchantToken = authenticationToken;
            Logger.i("merchantToken:" + merchantToken);
            if (merchantToken != null) {
                merchantPaymentAPI.paymentUsingCIMBClickPay(merchantToken, cimbClickPayModel, new Callback<TransactionResponse>() {
                    @Override
                    public void success(TransactionResponse cimbPayTransferResponse, Response response) {
                        releaseResources();
                        long end = System.currentTimeMillis();

                        if (cimbPayTransferResponse != null) {
                            if (isSDKLogEnabled) {
                                displayResponse(cimbPayTransferResponse);
                            }
                            if (cimbPayTransferResponse.getStatusCode().trim()
                                    .equalsIgnoreCase(context.getString(R.string.success_code_200))
                                    || cimbPayTransferResponse.getStatusCode()
                                    .trim().equalsIgnoreCase(context.getString(R.string.success_code_201))) {
                                VeritransBusProvider.getInstance().post(new TransactionSuccessEvent(cimbPayTransferResponse, Events.PAYMENT));

                                // Track Mixpanel event
                                analyticsManager.trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_CIMB_CLICK, end - start);
                            } else {
                                VeritransBusProvider.getInstance().post(new TransactionFailedEvent(
                                        cimbPayTransferResponse.getStatusMessage(),
                                        cimbPayTransferResponse,
                                        Events.PAYMENT
                                ));
                                // Track Mixpanel event
                                analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_CIMB_CLICK, end - start, cimbPayTransferResponse.getStatusMessage());
                            }
                        } else {
                            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_empty_response), Events.PAYMENT));
                            Logger.e(context.getString(R.string.error_empty_response));
                        }
                    }

                    @Override
                    public void failure(RetrofitError e) {
                        releaseResources();
                        long end = System.currentTimeMillis();

                        if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                            VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.PAYMENT));
                            Logger.i("Error in SSL Certificate. " + e.getMessage());
                        } else {
                            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage(), Events.PAYMENT));
                            Logger.i("General error occurred " + e.getMessage());
                        }
                        // Track Mixpanel event
                        analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_CIMB_CLICK, end - start, e.getMessage());

                    }
                });
            } else {
                releaseResources();
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_invalid_data_supplied), Events.PAYMENT));
                Logger.e(context.getString(R.string.error_invalid_data_supplied));
            }
    }

    /**
     * It will execute API call to pay using Mandiri E-Cash.
     *
     * @param mandiriECashModel mandiri E-Cash model
     * @param authenticationToken
     */
    public void paymentUsingMandiriECash(MandiriECashModel mandiriECashModel, String authenticationToken) {
        final long start = System.currentTimeMillis();

            String merchantToken = authenticationToken;
            Logger.i("merchantToken:" + merchantToken);
            if (merchantToken != null) {
                merchantPaymentAPI.paymentUsingMandiriECash(merchantToken, mandiriECashModel, new Callback<TransactionResponse>() {
                    @Override
                    public void success(TransactionResponse transferResponse, Response response) {
                        releaseResources();
                        long end = System.currentTimeMillis();

                        if (transferResponse != null) {
                            if (isSDKLogEnabled) {
                                displayResponse(transferResponse);
                            }
                            if (transferResponse.getStatusCode().trim()
                                    .equalsIgnoreCase(context.getString(R.string.success_code_200))
                                    || transferResponse.getStatusCode()
                                    .trim().equalsIgnoreCase(context.getString(R.string.success_code_201))) {
                                VeritransBusProvider.getInstance().post(new TransactionSuccessEvent(transferResponse, Events.PAYMENT));

                                // Track Mixpanel event
                                analyticsManager.trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_MANDIRI_ECASH, end - start);
                            } else {
                                VeritransBusProvider.getInstance().post(new TransactionFailedEvent(
                                        transferResponse.getStatusMessage(),
                                        transferResponse,
                                        Events.PAYMENT
                                ));

                                // Track Mixpanel event
                                analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_MANDIRI_ECASH, end - start, transferResponse.getStatusMessage());
                            }
                        } else {
                            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_empty_response), Events.PAYMENT));
                            Logger.e(context.getString(R.string.error_empty_response));
                        }
                    }

                    @Override
                    public void failure(RetrofitError e) {
                        releaseResources();

                        long end = System.currentTimeMillis();
                        if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                            VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.PAYMENT));
                            Logger.i("Error in SSL Certificate. " + e.getMessage());
                        } else {
                            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage(), Events.PAYMENT));
                            Logger.i("General error occurred " + e.getMessage());
                        }

                        // Track Mixpanel event
                        analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_MANDIRI_ECASH, end - start, e.getMessage());
                    }
                });
            } else {
                releaseResources();
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_invalid_data_supplied), Events.PAYMENT));
                Logger.e(context.getString(R.string.error_invalid_data_supplied));
            }
    }

    /**
     * It will execute API call to pay using BRI Epay.
     *
     * @param epayBriTransfer BRI Epay request model
     * @param authenticationToken
     */
    public void paymentUsingEpayBri(final EpayBriTransfer epayBriTransfer, String authenticationToken) {
        final long start = System.currentTimeMillis();

            String merchantToken = authenticationToken;
            Logger.i("merchantToken:" + merchantToken);
            if (merchantToken != null) {
                merchantPaymentAPI.paymentUsingEpayBri(merchantToken, epayBriTransfer, new Callback<TransactionResponse>() {
                    @Override
                    public void success(TransactionResponse epayBriTransferResponse, Response response) {
                        releaseResources();

                        long end = System.currentTimeMillis();
                        if (epayBriTransferResponse != null) {
                            if (isSDKLogEnabled) {
                                displayResponse(epayBriTransferResponse);
                            }
                            if (epayBriTransferResponse.getStatusCode().trim().equalsIgnoreCase(context.getString(R.string.success_code_200))
                                    || epayBriTransferResponse.getStatusCode().trim().equalsIgnoreCase(context.getString(R.string.success_code_201))) {
                                VeritransBusProvider.getInstance().post(new TransactionSuccessEvent(epayBriTransferResponse, Events.PAYMENT));

                                // Track Mixpanel event
                                analyticsManager.trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_BRI_EPAY, end - start);
                            } else {
                                VeritransBusProvider.getInstance().post(new TransactionFailedEvent(epayBriTransferResponse.getStatusMessage(), epayBriTransferResponse, Events.PAYMENT));

                                // Track Mixpanel event
                                analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BRI_EPAY, end - start, epayBriTransferResponse.getStatusMessage());
                            }

                        } else {
                            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_empty_response), Events.PAYMENT));
                            Logger.e(context.getString(R.string.error_empty_response));
                        }
                    }

                    @Override
                    public void failure(RetrofitError e) {
                        releaseResources();

                        long end = System.currentTimeMillis();

                        if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                            VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.PAYMENT));
                            Logger.i("Error in SSL Certificate. " + e.getMessage());
                        } else {
                            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage(), Events.PAYMENT));
                            Logger.i("General error occurred " + e.getMessage());
                        }

                        // Track Mixpanel event
                        analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BRI_EPAY, end - start, e.getMessage());

                    }
                });

            } else {
                releaseResources();
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_invalid_data_supplied), Events.PAYMENT));
                Logger.e(context.getString(R.string.error_invalid_data_supplied));
            }
    }


    /**
     * It will execute API call to pay using Indosat Dompetku.
     *
     * @param indosatDompetkuRequest Indosat Dompetku request model
     * @param authenticationToken
     */
    public void paymentUsingIndosatDompetku(final IndosatDompetkuRequest indosatDompetkuRequest, String authenticationToken) {
        final long start = System.currentTimeMillis();
            String merchantToken = authenticationToken;
            Logger.i("merchantToken:" + merchantToken);
            if (merchantToken != null) {
                merchantPaymentAPI.paymentUsingIndosatDompetku(merchantToken, indosatDompetkuRequest, new Callback<TransactionResponse>() {
                    @Override
                    public void success(TransactionResponse transactionResponse, Response response) {
                        releaseResources();

                        long end = System.currentTimeMillis();

                        if (transactionResponse != null) {
                            if (isSDKLogEnabled) {
                                displayResponse(transactionResponse);
                            }
                            if (transactionResponse.getStatusCode().trim().equalsIgnoreCase(context.getString(R.string.success_code_200))
                                    || transactionResponse.getStatusCode().trim().equalsIgnoreCase(context.getString(R.string.success_code_201))) {
                                VeritransBusProvider.getInstance().post(new TransactionSuccessEvent(transactionResponse, Events.PAYMENT));

                                // Track Mixpanel event
                                analyticsManager.trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_INDOSAT_DOMPETKU, end - start);
                            } else {
                                VeritransBusProvider.getInstance().post(new TransactionFailedEvent(transactionResponse.getStatusMessage(), transactionResponse, Events.PAYMENT));

                                // Track Mixpanel event
                                analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_INDOSAT_DOMPETKU, end - start, transactionResponse.getStatusMessage());
                            }
                        } else {
                            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_empty_response), Events.PAYMENT));
                            Logger.e(context.getString(R.string.error_empty_response));
                        }

                    }

                    @Override
                    public void failure(RetrofitError e) {
                        releaseResources();

                        long end = System.currentTimeMillis();

                        if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                            VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.PAYMENT));
                            Logger.i("Error in SSL Certificate. " + e.getMessage());
                        } else {
                            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage(), Events.PAYMENT));
                            Logger.i("General error occurred " + e.getMessage());
                        }

                        // Track Mixpanel event
                        analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_INDOSAT_DOMPETKU, end - start, e.getMessage());
                    }
                });

            } else {
                releaseResources();
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_invalid_data_supplied), Events.PAYMENT));
                Logger.e(context.getString(R.string.error_invalid_data_supplied));
            }
    }

    /**
     * It will execute API call to pay using Indomaret.
     *
     * @param indomaretRequestModel Indomaret payment request model
     * @param authenticationToken
     */
    public void paymentUsingIndomaret(final IndomaretRequestModel indomaretRequestModel, String authenticationToken) {
        final long start = System.currentTimeMillis();
            String merchantToken = authenticationToken;
            Logger.i("merchantToken:" + merchantToken);
            if (merchantToken != null) {
                merchantPaymentAPI.paymentUsingIndomaret(merchantToken, indomaretRequestModel, new Callback<TransactionResponse>() {
                    @Override
                    public void success(TransactionResponse indomaretTransferResponse, Response response) {
                        releaseResources();
                        long end = System.currentTimeMillis();

                        if (indomaretTransferResponse != null) {
                            if (isSDKLogEnabled) {
                                displayResponse(indomaretTransferResponse);
                            }
                            if (indomaretTransferResponse.getStatusCode().trim().equalsIgnoreCase(context.getString(R.string.success_code_200))
                                    || indomaretTransferResponse.getStatusCode().trim().equalsIgnoreCase(context.getString(R.string.success_code_201))) {
                                VeritransBusProvider.getInstance().post(new TransactionSuccessEvent(indomaretTransferResponse, Events.PAYMENT));

                                // Track Mixpanel event
                                analyticsManager.trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_INDOMARET, end - start);
                            } else {
                                VeritransBusProvider.getInstance().post(new TransactionFailedEvent(indomaretTransferResponse.getStatusMessage(), indomaretTransferResponse, Events.PAYMENT));

                                // Track Mixpanel event
                                analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_INDOMARET, end - start, indomaretTransferResponse.getStatusMessage());
                            }

                        } else {
                            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_empty_response), Events.PAYMENT));
                            Logger.e(context.getString(R.string.error_empty_response));
                        }
                    }

                    @Override
                    public void failure(RetrofitError e) {
                        releaseResources();

                        long end = System.currentTimeMillis();

                        if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                            VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.PAYMENT));
                            Logger.i("Error in SSL Certificate. " + e.getMessage());
                        } else {
                            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage(), Events.PAYMENT));
                            Logger.i("General error occurred " + e.getMessage());
                        }

                        // Track Mixpanel event
                        analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_INDOMARET, end - start, e.getMessage());

                    }
                });

            } else {
                releaseResources();
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_invalid_data_supplied), Events.PAYMENT));
                Logger.e(context.getString(R.string.error_invalid_data_supplied));
            }
    }

    /**
     * It will execute API call to pay using BBM Money.
     *
     * @param bbmMoneyRequestModel BBM Money payment request model
     * @param authenticationToken
     */
    public void paymentUsingBBMMoney(final BBMMoneyRequestModel bbmMoneyRequestModel, String authenticationToken) {
        final long start = System.currentTimeMillis();
            String merchantToken = authenticationToken;
            Logger.i("merchantToken:" + merchantToken);
            if (merchantToken != null) {
                merchantPaymentAPI.paymentUsingBBMMoney(merchantToken, bbmMoneyRequestModel, new Callback<TransactionResponse>() {
                    @Override
                    public void success(TransactionResponse bbmMoneyTransferResponse, Response response) {
                        releaseResources();

                        long end = System.currentTimeMillis();

                        if (bbmMoneyTransferResponse != null) {
                            if (isSDKLogEnabled) {
                                displayResponse(bbmMoneyTransferResponse);
                            }
                            if (bbmMoneyTransferResponse.getStatusCode().trim().equalsIgnoreCase(context.getString(R.string.success_code_200))
                                    || bbmMoneyTransferResponse.getStatusCode().trim().equalsIgnoreCase(context.getString(R.string.success_code_201))) {
                                VeritransBusProvider.getInstance().post(new TransactionSuccessEvent(bbmMoneyTransferResponse, Events.PAYMENT));

                                // Track Mixpanel event
                                analyticsManager.trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_BBM_MONEY, end - start);
                            } else {
                                VeritransBusProvider.getInstance().post(new TransactionFailedEvent(bbmMoneyTransferResponse.getStatusMessage(), bbmMoneyTransferResponse, Events.PAYMENT));

                                // Track Mixpanel event
                                analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BBM_MONEY, end - start, bbmMoneyTransferResponse.getStatusMessage());
                            }

                        } else {
                            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_empty_response), Events.PAYMENT));
                            Logger.e(context.getString(R.string.error_empty_response));
                        }
                    }

                    @Override
                    public void failure(RetrofitError e) {
                        releaseResources();

                        long end = System.currentTimeMillis();

                        if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                            VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.PAYMENT));
                            Logger.i("Error in SSL Certificate. " + e.getMessage());
                        } else {
                            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage(), Events.PAYMENT));
                            Logger.i("General error occurred " + e.getMessage());
                        }

                        // Track Mixpanel event
                        analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_BBM_MONEY, end - start, e.getMessage());

                    }
                });

            } else {
                releaseResources();
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_invalid_data_supplied), Events.PAYMENT));
                Logger.e(context.getString(R.string.error_invalid_data_supplied));
            }
    }

    /**
     * It will execute API call to save card based
     *
     * @param cardTokenRequest card token request model
     * @param authenticationToken
     */
    //test
    public void saveCards(final SaveCardRequest cardTokenRequest, String authenticationToken) {
            String auth = authenticationToken;
            Logger.i("Authentication token:" + auth);
            if (auth != null && !auth.equals("")) {
                merchantPaymentAPI.saveCard(auth, cardTokenRequest, new Callback<SaveCardResponse>() {
                    @Override
                    public void success(SaveCardResponse cardResponse, Response response) {
                        releaseResources();

                        if (cardResponse != null) {
                            if (cardResponse.getCode() == 200 || cardResponse.getCode() == 201) {
                                VeritransBusProvider.getInstance().post(new SaveCardSuccessEvent(cardResponse, Events.REGISTER_CARD));
                            } else {
                                VeritransBusProvider.getInstance().post(new SaveCardFailedEvent(cardResponse.getStatus(), cardResponse, Events.REGISTER_CARD));
                            }
                        } else {
                            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_empty_response), Events.REGISTER_CARD));
                            Logger.e(context.getString(R.string.error_empty_response));
                        }
                    }

                    @Override
                    public void failure(RetrofitError e) {
                        releaseResources();

                        if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                            VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.REGISTER_CARD));
                            Logger.i("Error in SSL Certificate. " + e.getMessage());
                        } else {
                            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage(), Events.REGISTER_CARD));
                            Logger.i("General error occurred " + e.getMessage());
                        }
                    }
                });

            } else {
                releaseResources();
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_invalid_data_supplied), Events.REGISTER_CARD));
                Logger.e(context.getString(R.string.error_invalid_data_supplied));
            }
    }

    /**
     * It will call execute API call to get saved cards.
     * @param authenticationToken
     */
    public void getCards(String authenticationToken) {
            String auth = authenticationToken;
            Logger.i("Authentication token:" + auth);
            if (auth != null && !auth.equals("")) {
                merchantPaymentAPI.getCard(auth, new Callback<CardResponse>() {
                    @Override
                    public void success(CardResponse cardResponse, Response response) {
                        releaseResources();

                        if (cardResponse != null) {
                            if (cardResponse.getCode() == 200) {
                                VeritransBusProvider.getInstance().post(new GetCardsSuccessEvent(cardResponse, Events.GET_CARD));
                            } else {
                                VeritransBusProvider.getInstance().post(new GetCardFailedEvent(cardResponse.getStatus(), cardResponse, Events.GET_CARD));
                            }
                        } else {
                            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_empty_response), Events.GET_CARD));
                            Logger.e(context.getString(R.string.error_empty_response));
                        }
                    }

                    @Override
                    public void failure(RetrofitError e) {
                        releaseResources();

                        if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                            VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.GET_CARD));
                            Logger.i("Error in SSL Certificate. " + e.getMessage());
                        } else {
                            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage(), Events.GET_CARD));
                            Logger.i("General error occurred " + e.getMessage());
                        }
                    }
                });
            } else {
                releaseResources();

                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_invalid_data_supplied), Events.GET_CARD));
                Logger.e(context.getString(R.string.error_invalid_data_supplied));
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



    /**
     * It will execute API call to delete saved card from merchant server.
     *
     * @param creditCard credit card detail
     * @param authenticationToken
     */
    public void deleteCard(SaveCardRequest creditCard, String authenticationToken) {
            String auth = authenticationToken;
            Logger.i("Authentication token:" + auth);
            if (auth != null) {
                merchantPaymentAPI.deleteCard(auth, creditCard.getSavedTokenId(), new Callback<DeleteCardResponse>() {
                    @Override
                    public void success(DeleteCardResponse deleteCardResponse, Response response) {
                        releaseResources();

                        if (deleteCardResponse != null) {
                            if (deleteCardResponse.getCode() == 200 || deleteCardResponse.getCode() == 204) {
                                VeritransBusProvider.getInstance().post(new DeleteCardSuccessEvent(deleteCardResponse, Events.DELETE_CARD));
                            } else {
                                VeritransBusProvider.getInstance().post(new DeleteCardFailedEvent(deleteCardResponse.getMessage(), deleteCardResponse, Events.DELETE_CARD));
                            }
                        } else {
                            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_empty_response), Events.DELETE_CARD));
                            Logger.e(context.getString(R.string.error_empty_response));
                        }
                    }

                    @Override
                    public void failure(RetrofitError e) {
                        releaseResources();
                        if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                            VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.DELETE_CARD));
                            Logger.i("Error in SSL Certificate. " + e.getMessage());
                        } else {
                            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage(), Events.DELETE_CARD));
                            Logger.i("General error occurred " + e.getMessage());
                        }
                    }
                });

            } else {
                releaseResources();
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_invalid_data_supplied), Events.DELETE_CARD));
                Logger.e(context.getString(R.string.error_invalid_data_supplied));
            }
    }

    /**
     * It will execute API call to get offers.
     * @param authenticationToken
     */

    public void getOffers(String authenticationToken) {
            String merchantToken = authenticationToken;
            if (merchantToken != null) {
                merchantPaymentAPI.getOffers(merchantToken, new Callback<GetOffersResponseModel>() {
                    @Override
                    public void success(GetOffersResponseModel getOffersResponseModel, Response response) {
                        releaseResources();
                        if (getOffersResponseModel != null) {
                            if (getOffersResponseModel.getMessage().equalsIgnoreCase(context.getString(R.string.success))) {
                                VeritransBusProvider.getInstance().post(new GetOfferSuccessEvent(getOffersResponseModel, Events.GET_OFFER));
                            } else {
                                VeritransBusProvider.getInstance().post(new GetOfferFailedEvent(getOffersResponseModel.getMessage(), getOffersResponseModel, Events.GET_OFFER));
                            }
                        } else {
                            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_empty_response), Events.GET_OFFER));
                            Logger.e(context.getString(R.string.error_empty_response));
                        }

                    }

                    @Override
                    public void failure(RetrofitError e) {
                        releaseResources();

                        if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                            VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.GET_OFFER));
                            Logger.i("Error in SSL Certificate. " + e.getMessage());
                        } else {
                            VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage(), Events.GET_OFFER));
                            Logger.i("General error occurred " + e.getMessage());
                        }
                    }
                });
            } else {
                releaseResources();
                VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_invalid_data_supplied), Events.GET_OFFER));
                Logger.e(context.getString(R.string.error_invalid_data_supplied));
            }
    }

    /**
     * It will execute API call to get authentication token from merchant server.
     */
    public void getAuthenticationToken() {
            merchantPaymentAPI.getAuthenticationToken(new Callback<AuthModel>() {
                @Override
                public void success(AuthModel authModel, Response response) {
                    releaseResources();

                    VeritransBusProvider.getInstance().post(new AuthenticationEvent(authModel));
                }

                @Override
                public void failure(RetrofitError e) {
                    releaseResources();

                    if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                        VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.AUTHENTICATION));
                        Logger.i("Error in SSL Certificate. " + e.getMessage());
                    } else {
                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage(), Events.AUTHENTICATION));
                        Logger.i("General error occurred " + e.getMessage());
                    }
                }
            });
    }

    public void paymentUsingKlikBCA(KlikBCAModel klikBCAModel) {
        final long start = System.currentTimeMillis();

            merchantPaymentAPI.paymentUsingKlikBCA(klikBCAModel, new Callback<TransactionResponse>() {
                @Override
                public void success(TransactionResponse response, Response r) {
                    releaseResources();

                    long end = System.currentTimeMillis();

                    if (response != null) {
                        if (isSDKLogEnabled) {
                            displayResponse(response);
                        }
                        if (response.getStatusCode().trim().equalsIgnoreCase(context.getString(R.string.success_code_200))
                                || response.getStatusCode().trim().equalsIgnoreCase(context.getString(R.string.success_code_201))) {
                            VeritransBusProvider.getInstance().post(new TransactionSuccessEvent(response, Events.PAYMENT));

                            // Track Mixpanel event
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_SUCCESS, PAYMENT_TYPE_KLIK_BCA, end - start);
                        } else {
                            VeritransBusProvider.getInstance().post(new TransactionFailedEvent(response.getStatusMessage(), response, Events.PAYMENT));

                            // Track Mixpanel event
                            analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_KLIK_BCA, end - start, response.getStatusMessage());
                        }
                    } else {
                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(context.getString(R.string.error_empty_response), Events.PAYMENT));
                        Logger.e(context.getString(R.string.error_empty_response), null);
                    }
                }

                @Override
                public void failure(RetrofitError e) {
                    releaseResources();

                    long end = System.currentTimeMillis();

                    if (e.getCause() instanceof SSLHandshakeException || e.getCause() instanceof CertPathValidatorException) {
                        VeritransBusProvider.getInstance().post(new SSLErrorEvent(Events.PAYMENT));
                        Logger.i("Error in SSL Certificate. " + e.getMessage());
                    } else {
                        VeritransBusProvider.getInstance().post(new GeneralErrorEvent(e.getMessage(), Events.PAYMENT));
                        Logger.i("General error occurred " + e.getMessage());
                    }
                    // Track Mixpanel event
                    analyticsManager.trackMixpanel(KEY_TRANSACTION_FAILED, PAYMENT_TYPE_KLIK_BCA, end - start, e.getMessage());
                }
            });

    }


}