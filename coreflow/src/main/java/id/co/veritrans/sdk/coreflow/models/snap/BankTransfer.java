package id.co.veritrans.sdk.coreflow.models.snap;

import java.util.List;

/**
 * @author rakawm
 */
public class BankTransfer {

    private List<String> banks;

    public BankTransfer() {
    }

    public BankTransfer(List<String> banks) {
        setBanks(banks);
    }

    public List<String> getBanks() {
        return banks;
    }

    public void setBanks(List<String> banks) {
        this.banks = banks;
    }
}
