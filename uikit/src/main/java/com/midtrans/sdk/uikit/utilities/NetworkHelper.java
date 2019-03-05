package com.midtrans.sdk.uikit.utilities;

import android.content.Context;
import android.net.ConnectivityManager;
import android.text.TextUtils;

import com.midtrans.sdk.corekit.core.api.midtrans.model.tokendetails.TokenDetailsResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.BcaBankTransferReponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.BcaKlikPayResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.BniBankTransferResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.BriEpayPaymentResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.CimbClicksResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.DanamonOnlineResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.GopayResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.IndomaretPaymentResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.KlikBcaResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.MandiriBillResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.MandiriClickpayResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.MandiriEcashResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.OtherBankTransferResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.PermataBankTransferResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.TelkomselCashResponse;
import com.midtrans.sdk.corekit.utilities.Logger;

public class NetworkHelper {

    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService
                    (Context.CONNECTIVITY_SERVICE);
            if (connManager.getActiveNetworkInfo() != null && connManager.getActiveNetworkInfo()
                    .isAvailable() && connManager.getActiveNetworkInfo().isConnected()) {
                return true;
            }
        } catch (Exception ex) {
            Logger.error(ex.getMessage());
            return false;
        }
        return false;
    }

    public static <T> boolean isPaymentSuccess(T response) {
        if (response instanceof BcaBankTransferReponse) {
            BcaBankTransferReponse rawResponse = (BcaBankTransferReponse) response;
            return checkStatusCode(rawResponse.getStatusCode());
        } else if (response instanceof BniBankTransferResponse) {
            BniBankTransferResponse rawResponse = (BniBankTransferResponse) response;
            return checkStatusCode(rawResponse.getStatusCode());
        } else if (response instanceof MandiriBillResponse) {
            MandiriBillResponse rawResponse = (MandiriBillResponse) response;
            return checkStatusCode(rawResponse.getStatusCode());
        } else if (response instanceof PermataBankTransferResponse) {
            PermataBankTransferResponse rawResponse = (PermataBankTransferResponse) response;
            return checkStatusCode(rawResponse.getStatusCode());
        } else if (response instanceof OtherBankTransferResponse) {
            OtherBankTransferResponse rawResponse = (OtherBankTransferResponse) response;
            return checkStatusCode(rawResponse.getStatusCode());
        } else if (response instanceof GopayResponse) {
            GopayResponse rawResponse = (GopayResponse) response;
            return checkStatusCode(rawResponse.getStatusCode());
        } else if (response instanceof IndomaretPaymentResponse) {
            IndomaretPaymentResponse rawResponse = (IndomaretPaymentResponse) response;
            return checkStatusCode(rawResponse.getStatusCode());
        } else if (response instanceof KlikBcaResponse) {
            KlikBcaResponse rawResponse = (KlikBcaResponse) response;
            return checkStatusCode(rawResponse.getStatusCode());
        } else if (response instanceof CimbClicksResponse) {
            CimbClicksResponse rawResponse = (CimbClicksResponse) response;
            return checkStatusCode(rawResponse.getStatusCode());
        } else if (response instanceof DanamonOnlineResponse) {
            DanamonOnlineResponse rawResponse = (DanamonOnlineResponse) response;
            return checkStatusCode(rawResponse.getStatusCode());
        } else if (response instanceof BriEpayPaymentResponse) {
            BriEpayPaymentResponse rawResponse = (BriEpayPaymentResponse) response;
            return checkStatusCode(rawResponse.getStatusCode());
        } else if (response instanceof MandiriEcashResponse) {
            MandiriEcashResponse rawResponse = (MandiriEcashResponse) response;
            return checkStatusCode(rawResponse.getStatusCode());
        } else if (response instanceof BcaKlikPayResponse) {
            BcaKlikPayResponse rawResponse = (BcaKlikPayResponse) response;
            return checkStatusCode(rawResponse.getStatusCode());
        } else if (response instanceof TelkomselCashResponse) {
            TelkomselCashResponse rawResponse = (TelkomselCashResponse) response;
            return checkStatusCode(rawResponse.getStatusCode());
        } else if (response instanceof TokenDetailsResponse) {
            TokenDetailsResponse rawResponse = (TokenDetailsResponse) response;
            return checkStatusCode(rawResponse.getStatusCode());
        } else if (response instanceof MandiriClickpayResponse) {
            MandiriClickpayResponse rawResponse = (MandiriClickpayResponse) response;
            return checkStatusCode(rawResponse.getStatusCode());
        }
        return false;
    }

    private static boolean checkStatusCode(String statusCode) {
        return !TextUtils.isEmpty(statusCode)
                && (statusCode.equals(Constants.STATUS_CODE_200)
                || statusCode.equals(Constants.STATUS_CODE_201));
    }
}