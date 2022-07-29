package com.midtrans.sdk.uikit.models;

import android.text.TextUtils;

import com.midtrans.sdk.corekit.models.BankType;
import com.midtrans.sdk.corekit.models.promo.Promo;
import com.midtrans.sdk.corekit.models.snap.BankBinsResponse;
import com.midtrans.sdk.corekit.models.snap.BankSingleBinResponse;
import com.midtrans.sdk.corekit.models.snap.BanksPointResponse;
import com.midtrans.sdk.corekit.models.snap.CreditCard;
import com.midtrans.sdk.uikit.BuildConfig;
import com.midtrans.sdk.uikit.callbacks.Call1;
import com.midtrans.sdk.uikit.repository.BankBinRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ziahaqi on 1/19/17.
 */

public class CreditCardTransaction {

    public static final String BANK_OFFLINE = "offline";

    private CreditCardInstallment cardInstallment;
    private CreditCardBankPoint cardBankPoint;
    private CreditCard creditCard;
    private boolean whiteListBinsAvailable;
    private boolean bankBinsAvailable;
    private boolean blackListBinsAvailable;
    private Promo selectedPromo;
    private final BankBinRepository bankBinRepository;
    private final Integer binDigit = Integer.parseInt(BuildConfig.BIN_RANGE);

    public CreditCardTransaction() {
        cardInstallment = new CreditCardInstallment();
        cardBankPoint = new CreditCardBankPoint();
        creditCard = new CreditCard();
        bankBinRepository = BankBinRepository.getInstance();
    }

    public void setProperties(CreditCard creditCard/*, ArrayList<BankBinsResponse> bankBins*/) {
        if (creditCard != null) {
            this.creditCard = creditCard;
            cardInstallment.setInstallment(creditCard.getInstallment());
        }
        init();
    }

    private void init() {
        ArrayList<String> whitleListBins = creditCard.getWhitelistBins();
        List<String> blackListBins = creditCard.getBlacklistBins();

        this.whiteListBinsAvailable = whitleListBins != null && !whitleListBins.isEmpty();
        this.blackListBinsAvailable = blackListBins != null && !blackListBins.isEmpty();
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

    public boolean isOfflineInstallmentAvailable() {
        return cardInstallment.isOfflineInstallmentAvailable();
    }

    public boolean isBankBinsAvailable() {
        return bankBinsAvailable;
    }

    public boolean isInWhiteList(String cardBin) {
        return isWhiteListBinsAvailable() && creditCard.getWhitelistBins().contains(cardBin);
    }


    public void getInstallmentTerms(String cardBin, final Call1<ArrayList<Integer>> callback) {

        getBankCodeByBin(cardBin, new Call1<String>() {
            @Override
            public void onSuccess(String bank) {
                if (TextUtils.isEmpty(bank)) {
                    bank = BANK_OFFLINE;
                }

                ArrayList<Integer> installmentTerms = cardInstallment.getTerms(bank);
                if (installmentTerms != null) {
                    callback.onSuccess(installmentTerms);
                } else {
                    callback.onSuccess(null);
                }

            }
        });

    }

    public void getOfflineInstallmentTerms(String cardBin, final Call1<ArrayList<Integer>> callback) {
        bankBinRepository.getBankBin(cardBin, new Call1<BankSingleBinResponse.BankBin>() {
            @Override
            public void onSuccess(BankSingleBinResponse.BankBin bank) {
                if (bank != null && bank.binType.equals("debit")) callback.onSuccess(null);

                ArrayList<Integer> installmentTerms = cardInstallment.getTerms(BANK_OFFLINE);
                if (installmentTerms != null) {
                    callback.onSuccess(installmentTerms);
                } else {
                    callback.onSuccess(null);
                }
            }
        });

    }

    /**
     * @param cardBin get bank by card bin
     * @return string Bank
     */
    public void getBankCodeByBin(String cardBin, final Call1<String> callback) {
        bankBinRepository.getBankBin(cardBin, new Call1<BankSingleBinResponse.BankBin>() {
            @Override
            public void onSuccess(BankSingleBinResponse.BankBin result) {
                callback.onSuccess(String.format("%s",result.bankCode));
            }

            @Override
            public void onError(Throwable t) {
                callback.onError(t);
            }
        });
    }

    /**
     * get bank name bu card number
     *
     * @param cardNumber
     * @return bank name
     */
    public void getBankCodeByCardNumber(String cardNumber, final Call1<String> callback) {
        bankBinRepository.getBankBin(cardNumber.substring(0, binDigit), new Call1<BankSingleBinResponse.BankBin>() {
            @Override
            public void onSuccess(BankSingleBinResponse.BankBin result) {
                callback.onSuccess(result.bankCode);
            }
        });
    }

    public void isMandiriCardDebit(String cardBin, final Call1<Boolean> callback) {
        bankBinRepository.getBankBin(cardBin, new Call1<BankSingleBinResponse.BankBin>() {
            @Override
            public void onSuccess(BankSingleBinResponse.BankBin result) {
                callback.onSuccess((result.bankCode == BankType.MANDIRI) && (result.binType == "credit"));
            }
        });
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
        String cardBin = cardNumber.replace(" ", "").substring(0, binDigit);
        return isInWhiteList(cardBin);
    }

    /**
     * check whether card number consists of one of whitelist bins
     *
     * @param cardNumber
     * @return boolean
     */
    private void isWhitelistBinContainCardNumber(String cardNumber, final Call1<Boolean> callback) {
        if (!TextUtils.isEmpty(cardNumber) && isWhiteListBinsAvailable()) {
            for (String bin : creditCard.getWhitelistBins()) {
                if (!TextUtils.isEmpty(bin)) {
                    if (cardNumber.startsWith(bin)) {
                        callback.onSuccess(true);
                        return;
                    }
                }
            }
            callback.onSuccess(false);
        }
        callback.onSuccess(true);
    }

    /**
     * check whether card number consists of one of blacklist bins
     *
     * @param cardNumber
     * @return boolean
     */
    public void isBlacklistContainCardNumber(String cardNumber, final Call1<Boolean> callback) {
        if (!TextUtils.isEmpty(cardNumber) && isBlackListBinsAvailable()) {
            for (String bin : creditCard.getBlacklistBins()) {
                if (!TextUtils.isEmpty(bin)) {
                    if (cardNumber.startsWith(bin)) {
                        callback.onSuccess(true);
                        return;
                    }
                }
            }
        } else {
            callback.onSuccess(false);
        }
    }

    public void isCardBinBlocked(final String cardNumber, final Call1<Boolean> callback) {

        isWhitelistBinContainCardNumber(cardNumber, new Call1<Boolean>() {
            @Override
            public void onSuccess(Boolean isWhiteListed) {
                if (!isWhiteListed) {
                    callback.onSuccess(true);
                } else {
                    isBlacklistContainCardNumber(cardNumber, new Call1<Boolean>() {
                        @Override
                        public void onSuccess(Boolean isBlacklisted) {
                            if (isBlacklisted) {
                                callback.onSuccess(true);
                            } else {
                                callback.onSuccess(false);
                            }
                        }
                    });
                }
            }
        });
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
