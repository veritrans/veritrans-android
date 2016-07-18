package id.co.veritrans.sdk.coreflow.models.snap;

/**
 * @author rakawm
 */
public class Transaction {

    private TransactionData transactionData;
    private MerchantData merchantData;

    public Transaction() {
    }

    public Transaction(TransactionData transactionData, MerchantData merchantData) {
        setTransactionData(transactionData);
        setMerchantData(merchantData);
    }

    public TransactionData getTransactionData() {
        return transactionData;
    }

    public void setTransactionData(TransactionData transactionData) {
        this.transactionData = transactionData;
    }

    public MerchantData getMerchantData() {
        return merchantData;
    }

    public void setMerchantData(MerchantData merchantData) {
        this.merchantData = merchantData;
    }
}
