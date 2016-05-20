package id.co.veritrans.sdk.models;

import com.google.gson.annotations.SerializedName;

/**
 * @author rakawm
 */
public class BCAVANumber {
    private String bank;
    @SerializedName("va_number")
    private String accountNumber;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }
}
