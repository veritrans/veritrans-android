package id.co.veritrans.sdk.callbacks;

import id.co.veritrans.sdk.models.PermataBankTransferResponse;

/**
 * Created by shivam on 10/29/15.
 */
public interface PermataBankTransferStatus extends Callback {
    public void onSuccess(PermataBankTransferResponse permataBankTransferResponse);
}