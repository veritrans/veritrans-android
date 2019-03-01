package com.midtrans.sdk.uikit.utilities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.midtrans.sdk.corekit.base.enums.PaymentType;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.optional.Item;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.AkulakuResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.BcaBankTransferReponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.BniBankTransferResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.BriEpayPaymentResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.CimbClicksResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.DanamonOnlineResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.GopayResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.IndomaretPaymentResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.KlikBcaResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.MandiriBillResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.OtherBankTransferResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.PermataBankTransferResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.PaymentInfoResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.enablepayment.EnabledPayment;
import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.promo.Promo;
import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.promo.PromoDetails;
import com.midtrans.sdk.corekit.utilities.Logger;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.base.callback.PaymentResult;
import com.midtrans.sdk.uikit.base.callback.Result;
import com.midtrans.sdk.uikit.base.enums.CreditCardIssuer;
import com.midtrans.sdk.uikit.base.enums.CreditCardType;
import com.midtrans.sdk.uikit.base.enums.PaymentStatus;
import com.midtrans.sdk.uikit.base.model.BankTransfer;
import com.midtrans.sdk.uikit.base.model.PaymentResponse;
import com.midtrans.sdk.uikit.view.model.ItemViewDetails;
import com.midtrans.sdk.uikit.view.model.PaymentMethodsModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class PaymentListHelper {

    /**
     * Bank Transfer is Payment Group
     * Mandiri Echannel / Mandiri Bill is Bank Transfer Mandiri
     */
    public final static String BANK_TRANSFER = "bank_transfer";

    private static <T> void setCallback(
            PaymentResult callback,
            @PaymentStatus String paymentStatus,
            @PaymentType String paymentType,
            T response
    ) {
        callback.onPaymentFinished(
                new Result(paymentStatus, paymentType),
                PaymentListHelper.convertTransactionStatus(response)
        );
    }

    private static void setFailedCallback(PaymentResult callback, String message) {
        callback.onFailed(new Throwable(message));
    }

    public static void setActivityResult(int resultCode, Intent data, PaymentResult callback) {
        String paymentType = data.getStringExtra(Constants.INTENT_DATA_TYPE);
        switch (paymentType) {
            case PaymentType.BCA_VA:
                try {
                    BcaBankTransferReponse response = (BcaBankTransferReponse) data.getSerializableExtra(Constants.INTENT_DATA_CALLBACK);
                    if (resultCode == Activity.RESULT_OK) {
                        if (response != null) {
                            switch (response.getStatusCode()) {
                                case com.midtrans.sdk.corekit.utilities.Constants.STATUS_CODE_200:
                                    setCallback(callback, PaymentStatus.STATUS_SUCCESS, PaymentType.BCA_VA, response);
                                    break;
                                case Constants.STATUS_CODE_201:
                                    setCallback(callback, PaymentStatus.STATUS_PENDING, PaymentType.BCA_VA, response);
                                    break;
                                default:
                                    setCallback(callback, PaymentStatus.STATUS_FAILED, PaymentType.BCA_VA, response);
                                    break;
                            }
                        } else {
                            setCallback(callback, PaymentStatus.STATUS_INVALID, PaymentType.BCA_VA, response);
                        }
                    } else {
                        setCallback(callback, PaymentStatus.STATUS_CANCEL, PaymentType.BCA_VA, response);
                    }
                } catch (RuntimeException e) {
                    Logger.error("onActivityResult:" + e.getMessage());
                    setFailedCallback(callback, e.getMessage());
                    return;
                }
                break;
            case PaymentType.BNI_VA:
                try {
                    BniBankTransferResponse response = (BniBankTransferResponse) data.getSerializableExtra(Constants.INTENT_DATA_CALLBACK);
                    if (resultCode == Activity.RESULT_OK) {
                        if (response != null) {
                            switch (response.getStatusCode()) {
                                case com.midtrans.sdk.corekit.utilities.Constants.STATUS_CODE_200:
                                    setCallback(callback, PaymentStatus.STATUS_SUCCESS, PaymentType.BNI_VA, response);
                                    break;
                                case Constants.STATUS_CODE_201:
                                    setCallback(callback, PaymentStatus.STATUS_PENDING, PaymentType.BNI_VA, response);
                                    break;
                                default:
                                    setCallback(callback, PaymentStatus.STATUS_FAILED, PaymentType.BNI_VA, response);
                                    break;
                            }
                        } else {
                            setCallback(callback, PaymentStatus.STATUS_INVALID, PaymentType.BNI_VA, response);
                        }
                    } else {
                        setCallback(callback, PaymentStatus.STATUS_CANCEL, PaymentType.BNI_VA, response);
                    }
                } catch (RuntimeException e) {
                    Logger.error("onActivityResult:" + e.getMessage());
                    setFailedCallback(callback, e.getMessage());
                    return;
                }
                break;
            case PaymentType.PERMATA_VA:
                try {
                    PermataBankTransferResponse response = (PermataBankTransferResponse) data.getSerializableExtra(Constants.INTENT_DATA_CALLBACK);
                    if (resultCode == Activity.RESULT_OK) {
                        if (response != null) {
                            switch (response.getStatusCode()) {
                                case com.midtrans.sdk.corekit.utilities.Constants.STATUS_CODE_200:
                                    setCallback(callback, PaymentStatus.STATUS_SUCCESS, PaymentType.PERMATA_VA, response);
                                    break;
                                case Constants.STATUS_CODE_201:
                                    setCallback(callback, PaymentStatus.STATUS_PENDING, PaymentType.PERMATA_VA, response);
                                    break;
                                default:
                                    setCallback(callback, PaymentStatus.STATUS_FAILED, PaymentType.PERMATA_VA, response);
                                    break;
                            }
                        } else {
                            setCallback(callback, PaymentStatus.STATUS_INVALID, PaymentType.PERMATA_VA, response);
                        }
                    } else {
                        setCallback(callback, PaymentStatus.STATUS_CANCEL, PaymentType.PERMATA_VA, response);
                    }
                } catch (RuntimeException e) {
                    Logger.error("onActivityResult:" + e.getMessage());
                    setFailedCallback(callback, e.getMessage());
                    return;
                }
                break;
            case PaymentType.ECHANNEL:
                try {
                    MandiriBillResponse response = (MandiriBillResponse) data.getSerializableExtra(Constants.INTENT_DATA_CALLBACK);
                    if (resultCode == Activity.RESULT_OK) {
                        if (response != null) {
                            switch (response.getStatusCode()) {
                                case com.midtrans.sdk.corekit.utilities.Constants.STATUS_CODE_200:
                                    setCallback(callback, PaymentStatus.STATUS_SUCCESS, PaymentType.ECHANNEL, response);
                                    break;
                                case Constants.STATUS_CODE_201:
                                    setCallback(callback, PaymentStatus.STATUS_PENDING, PaymentType.ECHANNEL, response);
                                    break;
                                default:
                                    setCallback(callback, PaymentStatus.STATUS_FAILED, PaymentType.ECHANNEL, response);
                                    break;
                            }
                        } else {
                            setCallback(callback, PaymentStatus.STATUS_INVALID, PaymentType.ECHANNEL, response);
                        }
                    } else {
                        setCallback(callback, PaymentStatus.STATUS_CANCEL, PaymentType.ECHANNEL, response);
                    }
                } catch (RuntimeException e) {
                    Logger.error("onActivityResult:" + e.getMessage());
                    setFailedCallback(callback, e.getMessage());
                    return;
                }
                break;
            case PaymentType.OTHER_VA:
                try {
                    OtherBankTransferResponse response = (OtherBankTransferResponse) data.getSerializableExtra(Constants.INTENT_DATA_CALLBACK);
                    if (resultCode == Activity.RESULT_OK) {
                        if (response != null) {
                            switch (response.getStatusCode()) {
                                case com.midtrans.sdk.corekit.utilities.Constants.STATUS_CODE_200:
                                    setCallback(callback, PaymentStatus.STATUS_SUCCESS, PaymentType.OTHER_VA, response);
                                    break;
                                case Constants.STATUS_CODE_201:
                                    setCallback(callback, PaymentStatus.STATUS_PENDING, PaymentType.OTHER_VA, response);
                                    break;
                                default:
                                    setCallback(callback, PaymentStatus.STATUS_FAILED, PaymentType.OTHER_VA, response);
                                    break;
                            }
                        } else {
                            setCallback(callback, PaymentStatus.STATUS_INVALID, PaymentType.OTHER_VA, response);
                        }
                    } else {
                        setCallback(callback, PaymentStatus.STATUS_CANCEL, PaymentType.OTHER_VA, response);
                    }
                } catch (RuntimeException e) {
                    Logger.error("onActivityResult:" + e.getMessage());
                    setFailedCallback(callback, e.getMessage());
                    return;
                }
                break;
            case PaymentType.GOPAY:
                try {
                    GopayResponse response = (GopayResponse) data.getSerializableExtra(Constants.INTENT_DATA_CALLBACK);
                    if (resultCode == Activity.RESULT_OK) {
                        if (response != null) {
                            switch (response.getStatusCode()) {
                                case com.midtrans.sdk.corekit.utilities.Constants.STATUS_CODE_200:
                                    setCallback(callback, PaymentStatus.STATUS_SUCCESS, PaymentType.GOPAY, response);
                                    break;
                                case Constants.STATUS_CODE_201:
                                    setCallback(callback, PaymentStatus.STATUS_PENDING, PaymentType.GOPAY, response);
                                    break;
                                default:
                                    setCallback(callback, PaymentStatus.STATUS_FAILED, PaymentType.GOPAY, response);
                                    break;
                            }
                        } else {
                            setCallback(callback, PaymentStatus.STATUS_INVALID, PaymentType.GOPAY, response);
                        }
                    } else {
                        setCallback(callback, PaymentStatus.STATUS_CANCEL, PaymentType.GOPAY, response);
                    }
                } catch (RuntimeException e) {
                    Logger.error("onActivityResult:" + e.getMessage());
                    setFailedCallback(callback, e.getMessage());
                    return;
                }
                break;
            case PaymentType.INDOMARET:
                try {
                    IndomaretPaymentResponse response = (IndomaretPaymentResponse) data.getSerializableExtra(Constants.INTENT_DATA_CALLBACK);
                    if (resultCode == Activity.RESULT_OK) {
                        if (response != null) {
                            switch (response.getStatusCode()) {
                                case com.midtrans.sdk.corekit.utilities.Constants.STATUS_CODE_200:
                                    setCallback(callback, PaymentStatus.STATUS_SUCCESS, PaymentType.INDOMARET, response);
                                    break;
                                case Constants.STATUS_CODE_201:
                                    setCallback(callback, PaymentStatus.STATUS_PENDING, PaymentType.INDOMARET, response);
                                    break;
                                default:
                                    setCallback(callback, PaymentStatus.STATUS_FAILED, PaymentType.INDOMARET, response);
                                    break;
                            }
                        } else {
                            setCallback(callback, PaymentStatus.STATUS_INVALID, PaymentType.INDOMARET, response);
                        }
                    } else {
                        setCallback(callback, PaymentStatus.STATUS_CANCEL, PaymentType.INDOMARET, response);
                    }
                } catch (RuntimeException e) {
                    Logger.error("onActivityResult:" + e.getMessage());
                    setFailedCallback(callback, e.getMessage());
                    return;
                }
                break;
            case PaymentType.KLIK_BCA:
                try {
                    KlikBcaResponse response = (KlikBcaResponse) data.getSerializableExtra(Constants.INTENT_DATA_CALLBACK);
                    if (resultCode == Activity.RESULT_OK) {
                        if (response != null) {
                            switch (response.getStatusCode()) {
                                case com.midtrans.sdk.corekit.utilities.Constants.STATUS_CODE_200:
                                    setCallback(callback, PaymentStatus.STATUS_SUCCESS, PaymentType.KLIK_BCA, response);
                                    break;
                                case Constants.STATUS_CODE_201:
                                    setCallback(callback, PaymentStatus.STATUS_PENDING, PaymentType.KLIK_BCA, response);
                                    break;
                                default:
                                    setCallback(callback, PaymentStatus.STATUS_FAILED, PaymentType.KLIK_BCA, response);
                                    break;
                            }
                        } else {
                            setCallback(callback, PaymentStatus.STATUS_INVALID, PaymentType.KLIK_BCA, response);
                        }
                    } else {
                        setCallback(callback, PaymentStatus.STATUS_CANCEL, PaymentType.KLIK_BCA, response);
                    }
                } catch (RuntimeException e) {
                    Logger.error("onActivityResult:" + e.getMessage());
                    setFailedCallback(callback, e.getMessage());
                    return;
                }
                break;
            case PaymentType.CIMB_CLICKS:
                try {
                    CimbClicksResponse response = (CimbClicksResponse) data.getSerializableExtra(Constants.INTENT_DATA_CALLBACK);
                    if (resultCode == Activity.RESULT_OK) {
                        if (response != null) {
                            switch (response.getStatusCode()) {
                                case com.midtrans.sdk.corekit.utilities.Constants.STATUS_CODE_200:
                                    setCallback(callback, PaymentStatus.STATUS_SUCCESS, PaymentType.CIMB_CLICKS, response);
                                    break;
                                case Constants.STATUS_CODE_201:
                                    setCallback(callback, PaymentStatus.STATUS_PENDING, PaymentType.CIMB_CLICKS, response);
                                    break;
                                default:
                                    setCallback(callback, PaymentStatus.STATUS_FAILED, PaymentType.CIMB_CLICKS, response);
                                    break;
                            }
                        } else {
                            setCallback(callback, PaymentStatus.STATUS_INVALID, PaymentType.CIMB_CLICKS, response);
                        }
                    } else {
                        setCallback(callback, PaymentStatus.STATUS_CANCEL, PaymentType.CIMB_CLICKS, response);
                    }
                } catch (RuntimeException e) {
                    Logger.error("onActivityResult:" + e.getMessage());
                    setFailedCallback(callback, e.getMessage());
                    return;
                }
                break;
            case PaymentType.DANAMON_ONLINE:
                try {
                    DanamonOnlineResponse response = (DanamonOnlineResponse) data.getSerializableExtra(Constants.INTENT_DATA_CALLBACK);
                    if (resultCode == Activity.RESULT_OK) {
                        if (response != null) {
                            switch (response.getStatusCode()) {
                                case com.midtrans.sdk.corekit.utilities.Constants.STATUS_CODE_200:
                                    setCallback(callback, PaymentStatus.STATUS_SUCCESS, PaymentType.DANAMON_ONLINE, response);
                                    break;
                                case Constants.STATUS_CODE_201:
                                    setCallback(callback, PaymentStatus.STATUS_PENDING, PaymentType.DANAMON_ONLINE, response);
                                    break;
                                default:
                                    setCallback(callback, PaymentStatus.STATUS_FAILED, PaymentType.DANAMON_ONLINE, response);
                                    break;
                            }
                        } else {
                            setCallback(callback, PaymentStatus.STATUS_INVALID, PaymentType.DANAMON_ONLINE, response);
                        }
                    } else {
                        setCallback(callback, PaymentStatus.STATUS_CANCEL, PaymentType.DANAMON_ONLINE, response);
                    }
                } catch (RuntimeException e) {
                    Logger.error("onActivityResult:" + e.getMessage());
                    setFailedCallback(callback, e.getMessage());
                    return;
                }
                break;
            case PaymentType.AKULAKU:
                try {
                    AkulakuResponse response = (AkulakuResponse) data.getSerializableExtra(Constants.INTENT_DATA_CALLBACK);
                    if (resultCode == Activity.RESULT_OK) {
                        if (response != null) {
                            switch (response.getStatusCode()) {
                                case com.midtrans.sdk.corekit.utilities.Constants.STATUS_CODE_200:
                                    setCallback(callback, PaymentStatus.STATUS_SUCCESS, PaymentType.AKULAKU, response);
                                    break;
                                case Constants.STATUS_CODE_201:
                                    setCallback(callback, PaymentStatus.STATUS_PENDING, PaymentType.AKULAKU, response);
                                    break;
                                default:
                                    setCallback(callback, PaymentStatus.STATUS_FAILED, PaymentType.AKULAKU, response);
                                    break;
                            }
                        } else {
                            setCallback(callback, PaymentStatus.STATUS_INVALID, PaymentType.AKULAKU, response);
                        }
                    } else {
                        setCallback(callback, PaymentStatus.STATUS_CANCEL, PaymentType.AKULAKU, response);
                    }
                } catch (RuntimeException e) {
                    Logger.error("onActivityResult:" + e.getMessage());
                    setFailedCallback(callback, e.getMessage());
                    return;
                }
                break;
            case PaymentType.BRI_EPAY:
                try {
                    BriEpayPaymentResponse response = (BriEpayPaymentResponse) data.getSerializableExtra(Constants.INTENT_DATA_CALLBACK);
                    if (resultCode == Activity.RESULT_OK) {
                        if (response != null) {
                            switch (response.getStatusCode()) {
                                case com.midtrans.sdk.corekit.utilities.Constants.STATUS_CODE_200:
                                    setCallback(callback, PaymentStatus.STATUS_SUCCESS, PaymentType.BRI_EPAY, response);
                                    break;
                                case Constants.STATUS_CODE_201:
                                    setCallback(callback, PaymentStatus.STATUS_PENDING, PaymentType.BRI_EPAY, response);
                                    break;
                                default:
                                    setCallback(callback, PaymentStatus.STATUS_FAILED, PaymentType.BRI_EPAY, response);
                                    break;
                            }
                        } else {
                            setCallback(callback, PaymentStatus.STATUS_INVALID, PaymentType.BRI_EPAY, response);
                        }
                    } else {
                        setCallback(callback, PaymentStatus.STATUS_CANCEL, PaymentType.BRI_EPAY, response);
                    }
                } catch (RuntimeException e) {
                    Logger.error("onActivityResult:" + e.getMessage());
                    setFailedCallback(callback, e.getMessage());
                    return;
                }
                break;
            default:
                setCallback(callback, PaymentStatus.STATUS_CANCEL, null, null);
                break;
        }
    }

    public static <T> PaymentResponse convertTransactionStatus(T response) {
        PaymentResponse paymentResponse;
        if (response instanceof BcaBankTransferReponse) {
            BcaBankTransferReponse rawResponse = (BcaBankTransferReponse) response;
            paymentResponse = PaymentResponse
                    .builder(
                            rawResponse.getStatusCode(),
                            rawResponse.getStatusMessage(),
                            rawResponse.getTransactionId(),
                            rawResponse.getOrderId(),
                            rawResponse.getGrossAmount(),
                            rawResponse.getPaymentType(),
                            rawResponse.getTransactionTime(),
                            rawResponse.getTransactionStatus()
                    )
                    .setBcaExpiration(rawResponse.getBcaExpiration())
                    .setBcaVaNumber(rawResponse.getBcaVaNumber())
                    .setPdfUrl(rawResponse.getPdfUrl())
                    .setFraudStatus(rawResponse.getFraudStatus())
                    .build();
            return paymentResponse;
        } else if (response instanceof BniBankTransferResponse) {
            BniBankTransferResponse rawResponse = (BniBankTransferResponse) response;
            paymentResponse = PaymentResponse
                    .builder(
                            rawResponse.getStatusCode(),
                            rawResponse.getStatusMessage(),
                            rawResponse.getTransactionId(),
                            rawResponse.getOrderId(),
                            rawResponse.getGrossAmount(),
                            rawResponse.getPaymentType(),
                            rawResponse.getTransactionTime(),
                            rawResponse.getTransactionStatus()
                    )
                    .setBniExpiration(rawResponse.getBniExpiration())
                    .setBniVaNumber(rawResponse.getBniVaNumber())
                    .setPdfUrl(rawResponse.getPdfUrl())
                    .setFraudStatus(rawResponse.getFraudStatus())
                    .build();
            return paymentResponse;
        } else if (response instanceof MandiriBillResponse) {
            MandiriBillResponse rawResponse = (MandiriBillResponse) response;
            paymentResponse = PaymentResponse
                    .builder(
                            rawResponse.getStatusCode(),
                            rawResponse.getStatusMessage(),
                            rawResponse.getTransactionId(),
                            rawResponse.getOrderId(),
                            rawResponse.getGrossAmount(),
                            rawResponse.getPaymentType(),
                            rawResponse.getTransactionTime(),
                            rawResponse.getTransactionStatus()
                    )
                    .setBillerCode(rawResponse.getBillerCode())
                    .setBillKey(rawResponse.getBillKey())
                    .setBillPaymentExpiration(rawResponse.getBillPaymentExpiration())
                    .setPdfUrl(rawResponse.getPdfUrl())
                    .setFraudStatus(rawResponse.getFraudStatus())
                    .build();
            return paymentResponse;
        } else if (response instanceof PermataBankTransferResponse) {
            PermataBankTransferResponse rawResponse = (PermataBankTransferResponse) response;
            paymentResponse = PaymentResponse
                    .builder(
                            rawResponse.getStatusCode(),
                            rawResponse.getStatusMessage(),
                            rawResponse.getTransactionId(),
                            rawResponse.getOrderId(),
                            rawResponse.getGrossAmount(),
                            rawResponse.getPaymentType(),
                            rawResponse.getTransactionTime(),
                            rawResponse.getTransactionStatus()
                    )
                    .setPermataVaNumber(rawResponse.getPermataVaNumber())
                    .setPermataExpiration(rawResponse.getPermataExpiration())
                    .setAtmChannel(rawResponse.getAtmChannel())
                    .setPdfUrl(rawResponse.getPdfUrl())
                    .setFraudStatus(rawResponse.getFraudStatus())
                    .build();
            return paymentResponse;
        } else if (response instanceof OtherBankTransferResponse) {
            OtherBankTransferResponse rawResponse = (OtherBankTransferResponse) response;
            paymentResponse = PaymentResponse
                    .builder(
                            rawResponse.getStatusCode(),
                            rawResponse.getStatusMessage(),
                            rawResponse.getTransactionId(),
                            rawResponse.getOrderId(),
                            rawResponse.getGrossAmount(),
                            rawResponse.getPaymentType(),
                            rawResponse.getTransactionTime(),
                            rawResponse.getTransactionStatus()
                    )
                    .setPermataVaNumber(rawResponse.getPermataVaNumber())
                    .setPermataExpiration(rawResponse.getPermataExpiration())
                    .setBniExpiration(rawResponse.getBniExpiration())
                    .setBniVaNumber(rawResponse.getBniVaNumber())
                    .setAtmChannel(rawResponse.getAtmChannel())
                    .setPdfUrl(rawResponse.getPdfUrl())
                    .setFraudStatus(rawResponse.getFraudStatus())
                    .build();
            return paymentResponse;
        } else if (response instanceof GopayResponse) {
            GopayResponse rawResponse = (GopayResponse) response;
            paymentResponse = PaymentResponse
                    .builder(
                            rawResponse.getStatusCode(),
                            rawResponse.getStatusMessage(),
                            rawResponse.getTransactionId(),
                            rawResponse.getOrderId(),
                            rawResponse.getGrossAmount(),
                            rawResponse.getPaymentType(),
                            rawResponse.getTransactionTime(),
                            rawResponse.getTransactionStatus()
                    )
                    .setDeeplinkUrl(rawResponse.getDeeplinkUrl())
                    .setQrCodeUrl(rawResponse.getQrCodeUrl())
                    .setGopayExpiration(rawResponse.getGopayExpiration())
                    .setGopayExpirationRaw(rawResponse.getGopayExpirationRaw())
                    .setFraudStatus(rawResponse.getFraudStatus())
                    .build();
            return paymentResponse;
        } else if (response instanceof IndomaretPaymentResponse) {
            IndomaretPaymentResponse rawResponse = (IndomaretPaymentResponse) response;
            paymentResponse = PaymentResponse
                    .builder(
                            rawResponse.getStatusCode(),
                            rawResponse.getStatusMessage(),
                            rawResponse.getTransactionId(),
                            rawResponse.getOrderId(),
                            rawResponse.getGrossAmount(),
                            rawResponse.getPaymentType(),
                            rawResponse.getTransactionTime(),
                            rawResponse.getTransactionStatus()
                    )
                    .setPdfUrl(rawResponse.getPdfUrl())
                    .setPaymentCode(rawResponse.getPaymentCode())
                    .setIndomaretExpireTime(rawResponse.getIndomaretExpireTime())
                    .setStore(rawResponse.getStore())
                    .build();
            return paymentResponse;
        } else if (response instanceof KlikBcaResponse) {
            KlikBcaResponse rawResponse = (KlikBcaResponse) response;
            paymentResponse = PaymentResponse
                    .builder(
                            rawResponse.getStatusCode(),
                            rawResponse.getStatusMessage(),
                            rawResponse.getTransactionId(),
                            rawResponse.getOrderId(),
                            rawResponse.getGrossAmount(),
                            rawResponse.getPaymentType(),
                            rawResponse.getTransactionTime(),
                            rawResponse.getTransactionStatus()
                    )
                    .setRedirectUrl(rawResponse.getRedirectUrl())
                    .setApprovalCode(rawResponse.getApprovalCode())
                    .setKlikBcaExpireTime(rawResponse.getKlikBcaExpireTime())
                    .build();
            return paymentResponse;
        } else if (response instanceof CimbClicksResponse) {
            CimbClicksResponse rawResponse = (CimbClicksResponse) response;
            paymentResponse = PaymentResponse
                    .builder(
                            rawResponse.getStatusCode(),
                            rawResponse.getStatusMessage(),
                            rawResponse.getTransactionId(),
                            rawResponse.getOrderId(),
                            rawResponse.getGrossAmount(),
                            rawResponse.getPaymentType(),
                            rawResponse.getTransactionTime(),
                            rawResponse.getTransactionStatus()
                    )
                    .setRedirectUrl(rawResponse.getRedirectUrl())
                    .build();
            return paymentResponse;
        } else if (response instanceof DanamonOnlineResponse) {
            DanamonOnlineResponse rawResponse = (DanamonOnlineResponse) response;
            paymentResponse = PaymentResponse
                    .builder(
                            rawResponse.getStatusCode(),
                            rawResponse.getStatusMessage(),
                            rawResponse.getTransactionId(),
                            rawResponse.getOrderId(),
                            rawResponse.getGrossAmount(),
                            rawResponse.getPaymentType(),
                            rawResponse.getTransactionTime(),
                            rawResponse.getTransactionStatus()
                    )
                    .setRedirectUrl(rawResponse.getRedirectUrl())
                    .setApprovalCode(rawResponse.getFraudStatus())
                    .build();
            return paymentResponse;
        } else if (response instanceof BriEpayPaymentResponse) {
            BriEpayPaymentResponse rawResponse = (BriEpayPaymentResponse) response;
            paymentResponse = PaymentResponse
                    .builder(
                            rawResponse.getStatusCode(),
                            rawResponse.getStatusMessage(),
                            rawResponse.getTransactionId(),
                            rawResponse.getOrderId(),
                            rawResponse.getGrossAmount(),
                            rawResponse.getPaymentType(),
                            rawResponse.getTransactionTime(),
                            rawResponse.getTransactionStatus()
                    )
                    .setRedirectUrl(rawResponse.getRedirectUrl())
                    .setApprovalCode(rawResponse.getFraudStatus())
                    .build();
            return paymentResponse;
        }
        return null;
    }

    /**
     * This method used for mapping Item Details List
     *
     * @return List<ItemViewDetails>
     */
    public static List<ItemViewDetails> mappingItemDetails(Activity context, PaymentInfoResponse response) {
        String currency = response.getTransactionDetails().getCurrency();
        List<ItemViewDetails> itemViewDetails = new ArrayList<>();
        if (response.getItemDetails() != null) {
            for (Item item : response.getItemDetails()) {
                String price = CurrencyHelper.formatAmount(context, item.getPrice(), currency);
                String itemName = item.getName();

                if (item.getQuantity() > 1) {
                    itemName = context.getString(
                            R.string.text_item_name_format,
                            item.getName(),
                            item.getQuantity());
                }

                itemViewDetails.add(new ItemViewDetails(itemName,
                        price,
                        ItemViewDetails.TYPE_ITEM,
                        true));
            }
        }
        return itemViewDetails;
    }

    /**
     * This method used for mapping the enabled payment from PaymentInfoResponse
     * It will used for making Payment list in RecyclerView
     * For bank transfer, it's group so don't add all of it but only add 1 group
     *
     * @return List<PaymentMethodsModel>
     */
    public static List<PaymentMethodsModel> mappingEnabledPayment(Activity context, PaymentInfoResponse response) {
        boolean isBankTransferAdded = false;
        List<PaymentMethodsModel> data = new ArrayList<>();
        for (EnabledPayment enabledPayment : response.getEnabledPayments()) {
            if ((enabledPayment.getCategory() != null && enabledPayment.getCategory().equalsIgnoreCase(BANK_TRANSFER))
                    || enabledPayment.getType().equalsIgnoreCase(PaymentType.ECHANNEL)) {
                if (!isBankTransferAdded) {
                    PaymentMethodsModel model = getMethods(
                            response,
                            context,
                            BANK_TRANSFER,
                            EnabledPayment.STATUS_UP
                    );
                    if (model != null) {
                        isBankTransferAdded = true;
                        data.add(model);
                    }
                }
            } else {
                PaymentMethodsModel model = getMethods(
                        response,
                        context,
                        enabledPayment.getType(),
                        enabledPayment.getStatus()
                );
                if (model != null) {
                    data.add(model);
                }
            }
        }
        markPaymentMethodHavePromo(response, data);
        return data;
    }

    /**
     * This method used for marking payment list if payment have promo then set the promo variable as true
     * It not return list, just set the variable inside model to true
     */
    private static void markPaymentMethodHavePromo(PaymentInfoResponse response, List<PaymentMethodsModel> data) {
        PromoDetails promoDetails = response.getPromoDetails();
        if (promoDetails != null) {
            List<Promo> promos = promoDetails.getPromos();
            if (promos != null && !promos.isEmpty()) {
                if (data != null && !data.isEmpty()) {
                    for (PaymentMethodsModel model : data) {
                        Promo promo = findPromoByPaymentMethod(model, promos);
                        if (promo != null) {
                            model.setHavePromo(true);
                        }
                    }
                }
            }
        }
    }

    /**
     * This method used for searching promo inside list promo by checking one by one in payment type
     *
     * @return Promo
     */
    private static Promo findPromoByPaymentMethod(PaymentMethodsModel model, List<Promo> promos) {
        for (Promo promo : promos) {
            List<String> paymentTypes = promo.getPaymentTypes();
            if (paymentTypes != null && !paymentTypes.isEmpty()) {
                if (paymentTypes.contains(model.getPaymentType())) {
                    return promo;
                }
            }
        }
        return null;
    }

    public static List<EnabledPayment> mappingBankTransfer(PaymentInfoResponse response) {
        List<EnabledPayment> bankTransfers = new ArrayList<>();
        for (EnabledPayment enabledPayment : response.getEnabledPayments()) {
            if ((enabledPayment.getCategory() != null && enabledPayment.getCategory().equalsIgnoreCase(BANK_TRANSFER))
                    || enabledPayment.getType().equalsIgnoreCase(PaymentType.ECHANNEL)) {
                bankTransfers.add(enabledPayment);
            }
        }
        return bankTransfers;
    }

    /**
     * This method used for making payment list model, it will construct payment name, description, etc
     *
     * @return PaymentMethodModel
     */
    private static PaymentMethodsModel getMethods(
            PaymentInfoResponse response,
            Activity context,
            String paymentType,
            String status
    ) {
        if (paymentType.equalsIgnoreCase(PaymentType.CREDIT_CARD)) {
            return getMethodCreditCards(response, context, 1, paymentType, status);
        } else if (paymentType.equalsIgnoreCase(BANK_TRANSFER)) {
            return new PaymentMethodsModel(
                    context.getString(R.string.payment_method_bank_transfer),
                    context.getString(R.string.payment_method_description_bank_transfer),
                    R.drawable.ic_atm,
                    paymentType,
                    2,
                    status
            );
        } else if (paymentType.equalsIgnoreCase(PaymentType.BCA_KLIKPAY)) {
            return new PaymentMethodsModel(
                    context.getString(R.string.payment_method_bca_klikpay),
                    context.getString(R.string.payment_method_description_bca_klikpay),
                    R.drawable.ic_klikpay,
                    paymentType,
                    3,
                    status
            );
        } else if (paymentType.equalsIgnoreCase(PaymentType.KLIK_BCA)) {
            return new PaymentMethodsModel(
                    context.getString(R.string.payment_method_klik_bca),
                    context.getString(R.string.payment_method_description_klik_bca),
                    R.drawable.ic_klikbca, paymentType,
                    4,
                    status
            );
        } else if (paymentType.equalsIgnoreCase(PaymentType.BRI_EPAY)) {
            return new PaymentMethodsModel(
                    context.getString(R.string.payment_method_bri_epay),
                    context.getString(R.string.payment_method_description_epay_bri),
                    R.drawable.ic_epay,
                    paymentType,
                    5,
                    status
            );
        } else if (paymentType.equalsIgnoreCase(PaymentType.CIMB_CLICKS)) {
            return new PaymentMethodsModel(
                    context.getString(R.string.payment_method_cimb_clicks),
                    context.getString(R.string.payment_method_description_cimb_clicks),
                    R.drawable.ic_cimb,
                    paymentType,
                    6,
                    status
            );
        } else if (paymentType.equalsIgnoreCase(PaymentType.MANDIRI_CLICKPAY)) {
            return new PaymentMethodsModel(
                    context.getString(R.string.payment_method_mandiri_clickpay),
                    context.getString(R.string.payment_method_description_mandiri_clickpay),
                    R.drawable.ic_mandiri2,
                    paymentType,
                    7,
                    status
            );
        } else if (paymentType.equalsIgnoreCase(PaymentType.INDOMARET)) {
            return new PaymentMethodsModel(
                    context.getString(R.string.payment_method_indomaret),
                    context.getString(R.string.payment_method_description_indomaret),
                    R.drawable.ic_indomaret,
                    paymentType,
                    8,
                    status
            );
        } else if (paymentType.equalsIgnoreCase(PaymentType.TELKOMSEL_CASH)) {
            return new PaymentMethodsModel(
                    context.getString(R.string.payment_method_telkomsel_cash),
                    context.getString(R.string.payment_method_description_telkomsel_cash),
                    R.drawable.ic_telkomsel, paymentType,
                    10,
                    status
            );
        } else if (paymentType.equalsIgnoreCase(PaymentType.MANDIRI_ECASH)) {
            return new PaymentMethodsModel(
                    context.getString(R.string.payment_method_mandiri_ecash),
                    context.getString(R.string.payment_method_description_mandiri_ecash),
                    R.drawable.ic_mandiri_e_cash,
                    paymentType,
                    11,
                    status
            );
        } else if (paymentType.equalsIgnoreCase(PaymentType.GOPAY)) {
            return new PaymentMethodsModel(
                    context.getString(R.string.payment_method_gopay),
                    context.getString(R.string.payment_method_description_gopay),
                    R.drawable.ic_gopay,
                    paymentType,
                    15,
                    status
            );
        } else if (paymentType.equalsIgnoreCase(PaymentType.DANAMON_ONLINE)) {
            return new PaymentMethodsModel(
                    context.getString(R.string.payment_method_danamon_online),
                    context.getString(R.string.payment_method_description_danamon_online),
                    R.drawable.ic_danamon_online,
                    paymentType,
                    16,
                    status
            );
        } else if (paymentType.equalsIgnoreCase(PaymentType.AKULAKU)) {
            return new PaymentMethodsModel(
                    context.getString(R.string.payment_method_akulaku),
                    context.getString(R.string.payment_method_description_akulaku),
                    R.drawable.ic_akulaku,
                    paymentType,
                    17,
                    status
            );
        } else if (paymentType.equalsIgnoreCase(PaymentType.ALFAMART)) {
            return new PaymentMethodsModel(
                    context.getString(R.string.payment_method_alfamart),
                    context.getString(R.string.payment_method_description_alfamart),
                    R.drawable.ic_alfamart,
                    paymentType,
                    18,
                    status
            );
        } else {
            return null;
        }
    }

    /**
     * This method use for deciding what type of credit card then show the payment method icon based on type
     *
     * @return int
     */
    public static int getCreditCardIconType(PaymentInfoResponse response) {
        if (response.getMerchantData() != null) {
            List<String> principles = response.getMerchantData().getEnabledPrinciples();
            if (principles != null && principles.contains(CreditCardIssuer.MASTERCARD) && principles.contains(CreditCardIssuer.VISA)) {
                if (principles.contains(CreditCardIssuer.JCB)) {
                    if (principles.contains(CreditCardIssuer.AMEX)) {
                        return CreditCardType.TYPE_MASTER_VISA_JCB_AMEX;
                    }
                    return CreditCardType.TYPE_MASTER_VISA_JCB;
                } else if (principles.contains(CreditCardIssuer.AMEX)) {
                    return CreditCardType.TYPE_MASTER_VISA_AMEX;
                }
                return CreditCardType.TYPE_MASTER_VISA;
            }
        }

        return CreditCardType.TYPE_UNKNOWN;
    }

    /**
     * This method used for making payment list model for CreditCard, it will construct payment name, description, etc
     *
     * @return PaymentMethodModel
     */
    private static PaymentMethodsModel getMethodCreditCards(
            PaymentInfoResponse response,
            Activity context,
            int priority,
            String paymentType,
            String status
    ) {
        int creditCardSupportType = getCreditCardIconType(response);
        switch (creditCardSupportType) {
            case CreditCardType.TYPE_MASTER_VISA_JCB_AMEX:
                return new PaymentMethodsModel(
                        context.getString(R.string.payment_method_credit_card),
                        context.getString(R.string.payment_method_description_credit_card),
                        R.drawable.ic_credit,
                        paymentType,
                        priority,
                        status
                );
            case CreditCardType.TYPE_MASTER_VISA_JCB:
                return new PaymentMethodsModel(
                        context.getString(R.string.payment_method_credit_card),
                        context.getString(R.string.payment_method_description_credit_card_3),
                        R.drawable.ic_credit_3,
                        paymentType,
                        priority,
                        status
                );
            case CreditCardType.TYPE_MASTER_VISA_AMEX:
                return new PaymentMethodsModel(
                        context.getString(R.string.payment_method_credit_card),
                        context.getString(R.string.payment_method_description_credit_card_4),
                        R.drawable.ic_credit_4,
                        paymentType,
                        priority,
                        status
                );
            default:
                return new PaymentMethodsModel(
                        context.getString(R.string.payment_method_credit_card),
                        context.getString(R.string.payment_method_description_credit_card_2),
                        R.drawable.ic_credit_2,
                        paymentType,
                        priority,
                        status
                );
        }
    }

    public static BankTransfer createBankTransferModel(Context context, String type, String status) {
        BankTransfer bankTransfer = null;
        if (!TextUtils.isEmpty(type)) {
            switch (type) {
                case PaymentType.BCA_VA:
                    bankTransfer = new BankTransfer(type, context.getString(R.string.bca_bank_transfer), R.drawable.ic_bca, 1, context.getString(R.string.payment_bank_description_bca), status);
                    break;

                case PaymentType.ECHANNEL:
                    bankTransfer = new BankTransfer(type, context.getString(R.string.mandiri_bill), R.drawable.ic_mandiri_bill_payment2, 2, context.getString(R.string.payment_bank_description_mandiri), status);
                    break;

                case PaymentType.BNI_VA:
                    bankTransfer = new BankTransfer(type, context.getString(R.string.bni_bank_transfer), R.drawable.ic_bni, 4, context.getString(R.string.payment_bank_description_bni), status);
                    break;

                case PaymentType.PERMATA_VA:
                    bankTransfer = new BankTransfer(type, context.getString(R.string.permata_bank_transfer), R.drawable.ic_permata, 3, context.getString(R.string.payment_bank_description_permata), status);
                    break;

                case PaymentType.OTHER_VA:
                    bankTransfer = new BankTransfer(type, context.getString(R.string.all_bank_transfer), R.drawable.ic_atm, 5, context.getString(R.string.payment_bank_description_other), status);
                    break;
            }

        }

        return bankTransfer;
    }

    public static String mappingPaymentTitle(Context context, String type) {
        switch (type) {
            case PaymentType.CREDIT_CARD:
                return context.getString(R.string.payment_method_credit_card);
            case PaymentType.BCA_VA:
                return context.getString(R.string.bank_bca_transfer);
            case PaymentType.BNI_VA:
                return context.getString(R.string.bank_bni_transfer);
            case PaymentType.ECHANNEL:
                return context.getString(R.string.mandiri_bill_transfer);
            case PaymentType.OTHER_VA:
                return context.getString(R.string.other_bank_transfer);
            case PaymentType.PERMATA_VA:
                return context.getString(R.string.payment_permata);
            case PaymentType.BCA_KLIKPAY:
                return context.getString(R.string.payment_method_bca_klikpay);
            case PaymentType.KLIK_BCA:
                return context.getString(R.string.payment_method_klik_bca);
            case PaymentType.BRI_EPAY:
                return context.getString(R.string.payment_method_bri_epay);
            case PaymentType.CIMB_CLICKS:
                return context.getString(R.string.payment_method_cimb_clicks);
            case PaymentType.MANDIRI_CLICKPAY:
                return context.getString(R.string.payment_method_mandiri_clickpay);
            case PaymentType.INDOMARET:
                return context.getString(R.string.payment_method_indomaret);
            case PaymentType.TELKOMSEL_CASH:
                return context.getString(R.string.payment_method_telkomsel_cash);
            case PaymentType.MANDIRI_ECASH:
                return context.getString(R.string.payment_method_mandiri_ecash);
            case PaymentType.GOPAY:
                return context.getString(R.string.payment_method_gopay);
            case PaymentType.DANAMON_ONLINE:
                return context.getString(R.string.payment_method_danamon_online);
            case PaymentType.AKULAKU:
                return context.getString(R.string.payment_method_akulaku);
            case PaymentType.ALFAMART:
                return context.getString(R.string.payment_method_alfamart);
            default:
                return null;
        }
    }

    /**
     * Sorting bank payment method by priority (Ascending)
     */
    public static void sortBankTransferMethodsByPriority(List<BankTransfer> paymentMethodsModels) {
        if (paymentMethodsModels != null) {
            Collections.sort(paymentMethodsModels, new Comparator<BankTransfer>() {
                @Override
                public int compare(BankTransfer lhs, BankTransfer rhs) {
                    return lhs.getPriority().compareTo(rhs.getPriority());
                }
            });
        }
    }
}