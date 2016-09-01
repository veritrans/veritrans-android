package id.co.veritrans.sdk.coreflow.callback.exception;

import id.co.veritrans.sdk.coreflow.models.snap.Transaction;

/**
 * Created by ziahaqi on 8/31/16.
 */
public class PaymentOptionError  extends BaseError {

    private Transaction transaction;

    public PaymentOptionError(Transaction transaction, String detailMessage, String errorType) {
        super(detailMessage, errorType);
        this.transaction = transaction;
    }

    public PaymentOptionError(String detailMessage, String errorType) {
        super(detailMessage, errorType);
    }

    public Transaction getTransaction() {
        return transaction;
    }
}
