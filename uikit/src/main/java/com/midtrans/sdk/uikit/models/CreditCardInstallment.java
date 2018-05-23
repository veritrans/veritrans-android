package com.midtrans.sdk.uikit.models;

import android.text.TextUtils;

import com.midtrans.sdk.corekit.models.snap.Installment;

import java.util.ArrayList;

/**
 * Created by ziahaqi on 1/19/17.
 */

public class CreditCardInstallment {
    private Installment installment;
    private String bankSelected;
    private ArrayList<Integer> installmentTerms = new ArrayList<>();
    private int termSelected;
    private boolean installmentAvailable;

    public void setInstallment(Installment installment) {
        this.installment = installment;
        init();
    }

    private void init() {
        installmentAvailable = installment != null && installment.getTerms() != null && !installment.getTerms().isEmpty();

    }

    public boolean isInstallmentAvailable() {
        return installmentAvailable;
    }

    public ArrayList<Integer> getTerms(String bank) {
        if (isInstallmentAvailable()) {

            for (String key : installment.getTerms().keySet()) {
                if (key.equals(bank)) {
                    this.bankSelected = bank;
                    this.installmentTerms.clear();
                    this.installmentTerms.add(0, 0);
                    this.installmentTerms.addAll(installment.getTerms().get(key));

                    return this.installmentTerms;
                }
            }
        }

        return null;
    }

    public int getTermByPosition(int currentPosition) {
        return installmentTerms.get(currentPosition);
    }

    public void setTermSelected(int termPosition) {
        this.termSelected = this.installmentTerms.size() == 0 ? 0 : this.installmentTerms.get(termPosition);
    }

    public boolean isInstallmentValid() {
        if (isInstallmentAvailable() && installment.isRequired()) {
            if (termSelected == 0 || TextUtils.isEmpty(bankSelected)) {
                return false;
            }
        }
        return true;
    }

    public boolean isInstallmentOptionRequired() {
        return (installment != null && installment.isRequired());
    }

    public void setAvailableStatus(boolean availableStatus) {
        this.installmentAvailable = availableStatus;
    }

    public int getTermSelected() {
        return this.termSelected;
    }

    public String getBankSelected() {
        return bankSelected;
    }

    public Installment getInstallment() {
        return installment;
    }
}
