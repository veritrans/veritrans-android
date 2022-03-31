package com.midtrans.sdk.corekit.callback;

import com.midtrans.sdk.corekit.models.snap.BankSingleBinResponse;


public interface BankBinCallback extends HttpRequestCallback{

    void onSuccess(BankSingleBinResponse.BankBin response);

}
