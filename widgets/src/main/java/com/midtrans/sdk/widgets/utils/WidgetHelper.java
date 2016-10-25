package com.midtrans.sdk.widgets.utils;

import com.midtrans.sdk.corekit.models.SaveCardRequest;
import com.midtrans.sdk.corekit.models.snap.SavedToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ziahaqi on 10/24/16.
 */

public class WidgetHelper {

    public static ArrayList<SaveCardRequest> convertSavedToken(List<SavedToken> savedTokens) {
        ArrayList<SaveCardRequest> saveCardRequests = new ArrayList<>();
        if(savedTokens != null && !savedTokens.isEmpty()){
            for (SavedToken savedToken : savedTokens) {
                saveCardRequests.add(new SaveCardRequest(savedToken.getToken(), savedToken.getMaskedCard(), savedToken.getTokenType()));
            }
        }

        return saveCardRequests;
    }
}
