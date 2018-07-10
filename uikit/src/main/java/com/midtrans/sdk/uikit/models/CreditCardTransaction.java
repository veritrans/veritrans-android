package com.midtrans.sdk.uikit.models;

import android.text.TextUtils;

import com.midtrans.sdk.corekit.models.BankType;
import com.midtrans.sdk.corekit.models.promo.Promo;
import com.midtrans.sdk.corekit.models.snap.BankBinsResponse;
import com.midtrans.sdk.corekit.models.snap.BanksPointResponse;
import com.midtrans.sdk.corekit.models.snap.CreditCard;

import java.util.ArrayList;
import java.util.List;

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
    private boolean blackListBinsAvailable;
    private Promo selectedPromo;

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
        List<String> blackListBins = creditCard.getBlacklistBins();

        this.whiteListBinsAvailable = whitleListBins != null && !whitleListBins.isEmpty();
        this.blackListBinsAvailable = blackListBins != null && !blackListBins.isEmpty();
        this.bankBinsAvailable = bankBins != null && !bankBins.isEmpty();
    }


    public boolean isWhiteListBinsAvailable() {
        return whiteListBinsAvailable;
    }

    public boolean isBlackListBinsAvailable() {
        return blackListBinsAvailable;
    }

    public void setBlackListBinsAvailable(boolean blackListBinsAvailable) {
        this.blackListBinsAvailable = blackListBinsAvailable;
    }

    public boolean isInstallmentAvailable() {
        return cardInstallment.isInstallmentAvailable();
    }

    public boolean isBankBinsAvailable() {
        return bankBinsAvailable;
    }

    public void setBankBins(ArrayList<BankBinsResponse> bankBins) {
        // do nothing
    }

    public boolean isInWhiteList(String cardBin) {
        return isWhiteListBinsAvailable() && creditCard.getWhitelistBins().contains(cardBin);
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

    /**
     * get bank name bu card number
     *
     * @param cardNumber
     * @return bank name
     */
    public String getBankByCardNumber(String cardNumber) {
        for (BankBinsResponse savedBankBin : bankBins) {
            if (savedBankBin.getBins() != null && !savedBankBin.getBins().isEmpty()) {
                String bankName = findBankByCardNumber(savedBankBin, cardNumber);
                if (bankName != null) {
                    return bankName;
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
            if (bankBinsResponse.getBank().equals(BankType.MANDIRI_DEBIT)) {
                return bankBinsResponse;
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


    private String findBankByCardNumber(BankBinsResponse savedBankBin, String cardNumber) {
        for (String savedBin : savedBankBin.getBins()) {
            if (!TextUtils.isEmpty(savedBin) && cardNumber.startsWith(savedBin)) {
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

    public void setBankPoint(BanksPointResponse response, String bankType) {
        cardBankPoint.setData(response, bankType);
    }

    public String getBankName() {
        return cardBankPoint.getBankType();
    }

    public boolean isBankPointEnabled() {
        return cardBankPoint.isEnabled();
    }

    public boolean isBankPointValid() {
        return cardBankPoint.isValid();
    }

    public float getBankPointRedeemed() {
        return cardBankPoint.getPointRedeemed();
    }

    public void setBankPointRedeemed(float pointRedeemed) {
        cardBankPoint.setPointRedeemed(pointRedeemed);
    }

    public boolean checkCardBinValidity(String cardNumber) {
        return !TextUtils.isEmpty(cardNumber)
                && cardNumber.length() >= 7
                && isCardBinValid(cardNumber);
    }


    private boolean isCardBinValid(String cardNumber) {
        String cardBin = cardNumber.replace(" ", "").substring(0, 6);
        return isInWhiteList(cardBin);
    }

    /**
     * check whether card number consists of one of whitelist bins
     *
     * @param cardNumber
     * @return boolean
     */
    private boolean isWhitelistBinContainCardNumber(String cardNumber) {
        if (!TextUtils.isEmpty(cardNumber) && isWhiteListBinsAvailable()) {
            for (String bin : creditCard.getWhitelistBins()) {
                if (!TextUtils.isEmpty(bin)) {
                    if (TextUtils.isDigitsOnly(bin)) {
                        if (cardNumber.startsWith(bin)) {
                            return true;
                        }
                    } else {
                        String bank = getBankByCardNumber(cardNumber);
                        if (bin.equalsIgnoreCase(bank)) {
                            return true;
                        }
                    }
                }
            }
            return false;
        }

        return true;
    }

    /**
     * check whether card number consists of one of blacklist bins
     *
     * @param cardNumber
     * @return boolean
     */
    public boolean isBlacklistContainCardNumber(String cardNumber) {
        if (!TextUtils.isEmpty(cardNumber) && isBlackListBinsAvailable()) {
            for (String bin : creditCard.getBlacklistBins()) {
                if (!TextUtils.isEmpty(bin)) {
                    if (TextUtils.isDigitsOnly(bin)) {
                        if (cardNumber.startsWith(bin)) {
                            return true;
                        }
                    } else {
                        String bank = getBankByCardNumber(cardNumber);
                        if (bin.equalsIgnoreCase(bank)) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    public boolean isCardBinBlocked(String cardNumber) {
        if (!isWhitelistBinContainCardNumber(cardNumber) || isBlacklistContainCardNumber(cardNumber)) {
            return true;
        }

        return false;
    }

    public void setSelectedPromo(Promo seletedPromo) {
        this.selectedPromo = seletedPromo;
    }

    public Promo getSelectedPromo() {
        return selectedPromo;
    }

    public boolean isSelectedPromoAvailable() {
        return selectedPromo != null;
    }

    public boolean isInstallmentOptionRequired() {
        return (cardInstallment != null && cardInstallment.isInstallmentOptionRequired());
    }
}
