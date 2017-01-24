package com.midtrans.sdk.uikit.models;

import android.text.TextUtils;

import com.midtrans.sdk.corekit.models.snap.BankPointsResponse;
import com.midtrans.sdk.corekit.models.snap.BankBinsResponse;
import com.midtrans.sdk.corekit.models.snap.CreditCard;

import java.util.ArrayList;

/**
 * Created by ziahaqi on 1/19/17.
 */

public class CreditCardTransaction {

    private static final String BANK_OFFLINE = "offline";

    private CreditCardInstallment cardInstallment;
    private CreditCardBankPoint cardBankPoint;
    private CreditCard creditCard;
    private boolean whiteListBinsAvailable;
    private ArrayList<BankBinsResponse> bankBins;
    private boolean bankBinsAvailable;

    public CreditCardTransaction() {
        bankBins = new ArrayList<>();
        cardInstallment = new CreditCardInstallment();
        cardBankPoint = new CreditCardBankPoint();
        creditCard = new CreditCard();
    }

    public void setProperties(CreditCard creditCard, ArrayList<BankBinsResponse> bankBins) {
        if (creditCard != null) {
            this.creditCard = creditCard;
            cardInstallment.setInstallment(creditCard.getInstallment());
        }

        if (bankBins != null && !bankBins.isEmpty()) {
            this.bankBins.clear();
            this.bankBins = bankBins;
        }
        init();
    }

    private void init() {
        ArrayList<String> whitleListBins = creditCard.getWhitelistBins();
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
        if (isWhiteListBinsAvailable() && creditCard.getWhitelistBins().contains(cardBin)) {
            return true;
        }
        return false;
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
            if (savedBankBin.getBins() != null && !savedBankBin.getBins().isEmpty()) {
                String bankBin = findBankByCardBin(savedBankBin, cardBin);
                if (bankBin != null) {
                    return bankBin;
                }
            }
        }
        return null;
    }

    private String findBankByCardBin(BankBinsResponse savedBankBin, String cardBin) {
        for (String savedBin : savedBankBin.getBins()) {
            if (savedBin.contains(cardBin)) {
                return savedBankBin.getBank();
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

    public void setBankPointStatus(boolean bniPointActivated) {
        cardBankPoint.setStatus(bniPointActivated);
    }

    public void setBankPoint(BankPointsResponse response, String bankType) {
        cardBankPoint.setData(response, bankType);
    }

    public boolean isBankPointEnabled() {
        return cardBankPoint.isEnabled();
    }

    public void setBankPointRedeemed(long pointRedeemed) {
        cardBankPoint.setpointRedeemed(pointRedeemed);
    }

    public boolean isBankPointValid() {
        return cardBankPoint.isValid();
    }

    public long getBankPointRedeemed() {
        return cardBankPoint.getpointRedeemed();
    }
}
