package com.midtrans.sdk.uikit.models;

import com.midtrans.sdk.corekit.models.SaveCardRequest;

import java.io.Serializable;
import java.util.List;

/**
 * Created by rakawm on 3/7/17.
 */

public class SavedCardList implements Serializable {
    public final List<SaveCardRequest> saveCardRequests;

    public SavedCardList(List<SaveCardRequest> saveCardRequests) {
        this.saveCardRequests = saveCardRequests;
    }
}
