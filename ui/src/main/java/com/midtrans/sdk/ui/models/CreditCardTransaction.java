package com.midtrans.sdk.ui.models;

import android.text.TextUtils;

import com.midtrans.sdk.core.models.BankType;
import com.midtrans.sdk.core.models.snap.CreditCard;
import com.midtrans.sdk.core.models.snap.bins.BankBinsResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ziahaqi on 1/19/17.
 */

public class CreditCardTransaction {

    private static final String BANK_OFFLINE = "offline";

    private CreditCardInstallment cardInstallment;
    private CreditCard creditCard;
    private boolean whiteListBinsAvailable;
    private ArrayList<BankBinsResponse> bankBins;
    private boolean bankBinsAvailable;

    public CreditCardTransaction() {
        bankBins = new ArrayList<>();
        cardInstallment = new CreditCardInstallment();
    }

    public void setProperties(CreditCard creditCard, ArrayList<BankBinsResponse> bankBins) {
        if (creditCard != null) {
            this.creditCard = creditCard;
            cardInstallment.setInstallment(creditCard.installment);
        }

        if (bankBins != null && !bankBins.isEmpty()) {
            this.bankBins.clear();
            this.bankBins = bankBins;
        }
        init();
    }

    private void init() {
        List<String> whitleListBins = creditCard.whitelistBins;
        this.whiteListBinsAvailable = whitleListBins != null && !whitleListBins.isEmpty();
        this.bankBinsAvailable = bankBins != null && !bankBins.isEmpty();
    }


    public boolean isWhiteListBinsAvailable() {
        return whiteListBinsAvailable;
    }

    public boolean isInstallmentAvailable() {
        return cardInstallment.isInstallmentAvailable();
    }

    public boolean isBankBinsAvailable() {
        return bankBinsAvailable;
    }

    public void setBankBins(ArrayList<BankBinsResponse> bankBins) {
        this.bankBins.clear();
        this.bankBins.addAll(bankBins);
    }

    public boolean isInWhiteList(String cardBin) {
        return isWhiteListBinsAvailable() && creditCard.whitelistBins.contains(cardBin);
    }


    public ArrayList<Integer> getInstallmentTerms(String cardBin) {

        String bank = getBankByBin(cardBin);
        if (TextUtils.isEmpty(bank)) {
            bank = BANK_OFFLINE;
        }

        ArrayList<Integer> installmentTerms = cardInstallment.getTerms(bank);
        if (installmentTerms != null) {
            return installmentTerms;
        }

        return null;
    }

    /**
     * @param cardBin get bank by card bin
     * @return string Bank
     */
    public String getBankByBin(String cardBin) {
        for (BankBinsResponse savedBankBin : bankBins) {
            if (savedBankBin.bins != null && !savedBankBin.bins.isEmpty()) {
                String bankBin = findBankByCardBin(savedBankBin, cardBin);
                if (bankBin != null) {
                    return bankBin;
                }
            }
        }
        return null;
    }

    public boolean isMandiriCardDebit(String cardBin) {
        if (getMandiriDebitResponse() != null) {
            String bankBin = findBankByCardBin(getMandiriDebitResponse(), cardBin);
            return bankBin != null;
        }
        return false;
    }

    private BankBinsResponse getMandiriDebitResponse() {
        for (BankBinsResponse bankBinsResponse : bankBins) {
            if (bankBinsResponse.bank.equals(BankType.MANDIRI_DEBIT)) {
                return bankBinsResponse;
            }
        }
        return null;
    }

    private String findBankByCardBin(BankBinsResponse savedBankBin, String cardBin) {
        for (String savedBin : savedBankBin.bins) {
            if (savedBin.contains(cardBin)) {
                return savedBankBin.bank;
            }
        }
        return null;
    }

    /**
     * @param currentPosition index position
     * @return term of installment
     */
    public Integer getInstallmentTerm(int currentPosition) {
        return cardInstallment.getTermByPosition(currentPosition);
    }

    public void setInstallment(int termPosition) {
        cardInstallment.setTermSelected(termPosition);
    }

    public boolean isInstallmentValid() {
        return cardInstallment.isInstallmentValid();
    }

    public void setInstallmentAvailableStatus(boolean installmentStatus) {
        cardInstallment.setAvailableStatus(installmentStatus);
    }

    public int getInstallmentTermSelected() {
        return cardInstallment.getTermSelected();
    }

    public String getInstallmentBankSelected() {
        return cardInstallment.getBankSelected();
    }

    public boolean isBankSupportInstallment() {
        String bank = creditCard.bank;
        if(!TextUtils.isEmpty(bank)){
            if(bank.equals(BankType.BRI) || bank.equals(BankType.MAYBANK)){
                return false;
            }
        }
        return true;
    }
}
