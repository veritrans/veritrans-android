package com.midtrans.sdk.uikit.repository;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.midtrans.sdk.corekit.callback.BankBinCallback;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.snap.BankSingleBinResponse;
import com.midtrans.sdk.uikit.callbacks.Call1;

public class BankBinRepository {

    private static BankBinRepository INSTANCE;
    static final String BANK_BIN_SHARED_PREF_FILE = "com.midtrans.sdk.uikit.repository.BankBinRepository";
    static final String BANK_BIN_KEY_PREFIX = "BANK_BIN_";
    private final SharedPreferences bankBinPreferences;
    private final Gson gson;

    private BankBinRepository(Context context) {
        bankBinPreferences = context.getSharedPreferences(BANK_BIN_SHARED_PREF_FILE, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    private void put(String key, BankSingleBinResponse.BankBin value) {
        bankBinPreferences.edit().putString(BANK_BIN_KEY_PREFIX + key, gson.toJson(value)).apply();
    }

    private BankSingleBinResponse.BankBin get(String key) {
        String bankBinString = bankBinPreferences.getString(BANK_BIN_KEY_PREFIX + key, null);
        if (bankBinString != null) {
            return gson.fromJson(bankBinString, BankSingleBinResponse.BankBin.class);
        }
        return null;
    }

    public void getBankBin(final String binNumber, final Call1<BankSingleBinResponse.BankBin> callback) {
        BankSingleBinResponse.BankBin result = get(binNumber);
        if (result != null) {
            callback.onSuccess(result);
        } else {
            MidtransSDK.getInstance().getBankBin(binNumber, new BankBinCallback() {
                @Override
                public void onSuccess(BankSingleBinResponse.BankBin response) {
                    put(binNumber, response);
                    callback.onSuccess(response);
                }

                @Override
                public void onError(Throwable error) {
                    callback.onError(error);
                }
            });
        }
    }

    private static MidtransSDK getMidtransSdk() {
        return MidtransSDK.getInstance();
    }

    public static BankBinRepository getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new BankBinRepository(getMidtransSdk().getContext());
        }
        return INSTANCE;
    }
}
