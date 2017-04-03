package com.midtrans.sdk.ui.views.creditcard.saved;

import com.midtrans.sdk.core.models.snap.SavedToken;

/**
 * Created by rakawm on 4/3/17.
 */

public interface SavedCardView {
    void onDeleteCardSuccess(SavedToken savedToken);

    void onDeleteCardFailure(String errorMessage);
}
