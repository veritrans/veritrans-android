package com.midtrans.sdk.ui.models;

import android.text.TextUtils;

import com.midtrans.sdk.core.models.snap.CreditCard;
import com.midtrans.sdk.core.models.snap.CustomerDetails;
import com.midtrans.sdk.core.models.snap.ItemDetails;
import com.midtrans.sdk.core.models.snap.SnapCustomerDetails;
import com.midtrans.sdk.core.models.snap.transaction.SnapCallbacks;
import com.midtrans.sdk.core.models.snap.transaction.SnapEnabledPayment;
import com.midtrans.sdk.core.models.snap.transaction.SnapMerchantData;
import com.midtrans.sdk.core.models.snap.transaction.SnapTransaction;
import com.midtrans.sdk.core.models.snap.transaction.SnapTransactionDetails;
import com.midtrans.sdk.ui.themes.BaseColorTheme;
import com.midtrans.sdk.ui.themes.ColorTheme;

import java.util.List;

/**
 * Created by ziahaqi on 2/20/17.
 */

public class Transaction {

    private String checkoutToken;
    private String token;
    private SnapTransactionDetails transactionDetails;
    private CustomerDetails customerDetails;
    private List<ItemDetails> itemDetails;
    private SnapCallbacks callbacks;
    private List<SnapEnabledPayment> enabledPayments;
    private CreditCard creditCard;
    private SnapMerchantData merchant;

    public Transaction(SnapTransaction snapTransaction) {
        this.creditCard = snapTransaction.creditCard;
        this.merchant = snapTransaction.merchant;
        this.checkoutToken = snapTransaction.token;
        this.enabledPayments = snapTransaction.enabledPayments;
        this.callbacks = snapTransaction.callbacks;
        this.itemDetails = snapTransaction.itemDetails;
        this.customerDetails = snapTransaction.customerDetails;
        this.transactionDetails = snapTransaction.transactionDetails;
    }

    public boolean isCreditCardNormalMode() {
        if (!isCreditCardOneClickMode() && !isCreditCardTwoClickMode()) {
            return true;
        }
        return false;
    }

    public boolean isCreditCardOneClickMode() {
        if (creditCard != null) {
            if (creditCard.saveCard && creditCard.secure) {
                return true;
            }
        }
        return false;
    }

    public boolean isCreditCardTwoClickMode() {
        if (creditCard != null) {
            if (creditCard.saveCard && !creditCard.secure) {
                return true;
            }
        }
        return false;
    }

    public String getAcquiringBank() {
        if (hasCreditCard()) {
            return creditCard.bank;
        }
        return null;
    }

    private boolean hasCreditCard() {
        return creditCard != null;
    }

    public String getAquiringChannel() {
        if (hasCreditCard()) {
            return creditCard.channel;
        }
        return null;
    }

    public String getCheckoutToken() {
        return checkoutToken;
    }

    public CustomerDetails getCustomerDetails(){
        return this.customerDetails;
    }

    public SnapMerchantData getMerchant() {
        return merchant;
    }

    public SnapCustomerDetails createSnapCustomerDetails() {
        String fullName = TextUtils.isEmpty(customerDetails.lastName) ? customerDetails.firstName : customerDetails.firstName + " " + customerDetails.lastName;
        return new SnapCustomerDetails(fullName, customerDetails.email, customerDetails.phone);
    }


    public int getGrossAmount() {
        return transactionDetails.grossAmount;
    }

    public CreditCard getCreditCard() {
        return creditCard;
    }
}
