package id.co.veritrans.sdk.coreflow.models.snap;

import java.util.List;

/**
 * @author rakawm
 */
public class TransactionData {
    private String id;
    private String transactionId;
    private String kind;

    private TransactionDetails transactionDetails;
    private List<String> enabledPayments;
    private BankTransfer bankTransfer;

    private CustomerDetails customerDetails;
    private List<ItemDetails> itemDetails;
}
