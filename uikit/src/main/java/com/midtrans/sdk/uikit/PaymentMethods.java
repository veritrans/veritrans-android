package com.midtrans.sdk.uikit;

import android.content.Context;

import com.midtrans.sdk.corekit.core.Constants;
import com.midtrans.sdk.corekit.models.BankTransferModel;
import com.midtrans.sdk.corekit.models.PaymentMethodsModel;
import com.midtrans.sdk.corekit.models.snap.EnabledPayment;
import com.midtrans.sdk.uikit.models.SectionedPaymentMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * Payment method list.
 *
 * @author rakawm
 */
public class PaymentMethods {

    public static final int SECTION_TYPE_CARD = 1;
    public static final int SECTION_TYPE_VA = 2;
    public static final int SECTION_TYPE_DIRECT_DEBIT = 3;
    public static final int SECTION_TYPE_EWALET = 4;
    public static final int SECTION_TYPE_CSTORE = 5;

    public static final int TYPE_SECTION = 1;
    public static final int TYPE_ITEM = 2;


    public static PaymentMethodsModel getMethods(Context context, String name) {
        if (name.equals(context.getString(R.string.payment_credit_debit))) {
            return getMethodCreditCards(context, 1);
        } else if (name.equals(context.getString(R.string.payment_bank_transfer))) {
            return getMethodBankTransfer(context, 2);
        } else if (name.equals(context.getString(R.string.payment_bca_click))) {
            return getMethodBCAKlikpay(context, 3);
        } else if (name.equals(context.getString(R.string.payment_klik_bca))) {
            return getMethodKlikBCA(context, 4);
        } else if (name.equals(context.getString(R.string.payment_epay_bri))) {
            return getMethodEpayBRI(context, 5);
        } else if (name.equals(context.getString(R.string.payment_cimb_clicks))) {
            return getMethodCIMBClicks(context, 6);
        } else if (name.equals(context.getString(R.string.payment_mandiri_clickpay))) {
            return getMethodMandiriClickpay(context, 7);
        } else if (name.equals(context.getString(R.string.payment_indomaret))) {
            return getMethodIndomaret(context, 8);
        } else if (name.equals(context.getString(R.string.payment_kioson))) {
            return getMethodKiosan(context, 9);
        } else if (name.equals(context.getString(R.string.payment_telkomsel_cash))) {
            return getMethodTelkomselCash(context, 10);
        } else if (name.equals(context.getString(R.string.payment_mandiri_ecash))) {
            return getMethodMandiriECash(context, 11);
        } else if (name.equals(context.getString(R.string.payment_indosat_dompetku))) {
            return getMethodIndosatDompetku(context, 12);
        } else if (name.equals(context.getString(R.string.payment_xl_tunai))) {
            return getMethodXLTunai(context, 13);
        }  else {
            return null;
        }
    }


    public static PaymentMethodsModel getMethodOffers(Context context) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_offers), R.drawable.ic_offers, Constants.PAYMENT_METHOD_NOT_SELECTED, 0);
    }

    public static PaymentMethodsModel getMethodCreditCards(Context context, int priority) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_credit_card), R.drawable.ic_credit, Constants.PAYMENT_METHOD_NOT_SELECTED , priority);
    }

    public static PaymentMethodsModel getMethodBankTransfer(Context context, int priority) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_bank_transfer), R.drawable.ic_atm, Constants.PAYMENT_METHOD_NOT_SELECTED , priority);
    }

    public static PaymentMethodsModel getMethodBCAKlikpay(Context context, int priority) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_bca_klikpay), R.drawable.ic_bca, Constants.PAYMENT_METHOD_NOT_SELECTED, priority);
    }

    public static PaymentMethodsModel getMethodKlikBCA(Context context, int priority) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_klik_bca), R.drawable.ic_klikbca, Constants.PAYMENT_METHOD_NOT_SELECTED, priority);
    }

    public static PaymentMethodsModel getMethodEpayBRI(Context context, int priority) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_bri_epay), R.drawable.ic_epay, Constants.PAYMENT_METHOD_NOT_SELECTED, priority);
    }

    public static PaymentMethodsModel getMethodCIMBClicks(Context context, int priority) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_cimb_clicks), R.drawable.ic_cimb, Constants.PAYMENT_METHOD_NOT_SELECTED, priority);
    }

    public static PaymentMethodsModel getMethodMandiriClickpay(Context context, int priority) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_mandiri_clickpay), R.drawable.ic_mandiri2, Constants.PAYMENT_METHOD_NOT_SELECTED, priority);
    }

    public static PaymentMethodsModel getMethodIndomaret(Context context, int priority) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_indomaret), R.drawable.ic_indomaret, Constants.PAYMENT_METHOD_NOT_SELECTED, priority);
    }

    private static PaymentMethodsModel getMethodKiosan(Context context, int priority) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_kioson), R.drawable.ic_kioson, Constants.PAYMENT_METHOD_NOT_SELECTED, priority);
    }

    public static PaymentMethodsModel getMethodTelkomselCash(Context context, int priority) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_telkomsel_cash), R.drawable.ic_telkomsel, Constants.PAYMENT_METHOD_NOT_SELECTED, priority);
    }

    public static PaymentMethodsModel getMethodMandiriECash(Context context, int priority) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_mandiri_ecash), R.drawable.ic_mandiri_e_cash, Constants.PAYMENT_METHOD_NOT_SELECTED, priority);
    }

    public static PaymentMethodsModel getMethodIndosatDompetku(Context context, int priority) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_indosat_dompetku), R.drawable.ic_indosat, Constants.PAYMENT_METHOD_NOT_SELECTED , priority);
    }

    public static PaymentMethodsModel getMethodXLTunai(Context context, int priority) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_xl_tunai), R.drawable.ic_xl, Constants.PAYMENT_METHOD_NOT_SELECTED, priority);
    }

    public static ArrayList<BankTransferModel> getBankTransferList(Context context) {
        ArrayList<BankTransferModel> models = new ArrayList<>();
        models.add(new BankTransferModel(context.getString(R.string.bca_bank_transfer), R.drawable.ic_bca, true));
        models.add(new BankTransferModel(context.getString(R.string.permata_bank_transfer), R.drawable.ic_permata, true));
        models.add(new BankTransferModel(context.getString(R.string.all_bank_transfer), R.drawable.ic_other_bank, true));
        models.add(new BankTransferModel(context.getString(R.string.mandiri_bank_transfer), R.drawable.ic_mandiri_bill_payment2, true));
        return models;
    }

    public static BankTransferModel getBankTransferModel(Context context, String name) {
        if (name.equals(context.getString(R.string.payment_bca_va))) {
            return new BankTransferModel(context.getString(R.string.bca_bank_transfer), R.drawable.ic_bca, true);
        } else if (name.equals(context.getString(R.string.payment_permata_va))) {
            return new BankTransferModel(context.getString(R.string.permata_bank_transfer), R.drawable.ic_permata, true);
        } else if (name.equals(context.getString(R.string.payment_all_va))) {
            return new BankTransferModel(context.getString(R.string.all_bank_transfer), R.drawable.ic_other_bank, true);
        } else if (name.equals(context.getString(R.string.payment_mandiri_bill_payment))) {
            return new BankTransferModel(context.getString(R.string.mandiri_bank_transfer), R.drawable.ic_mandiri_bill_payment2, true);
        } else {
            return null;
        }
    }

    public static List<EnabledPayment> getDefaultPaymentList(Context context) {
        List<EnabledPayment> paymentNameList = new ArrayList<>();
        paymentNameList.add(new EnabledPayment(context.getString(R.string.payment_credit_debit), null));
        paymentNameList.add(new EnabledPayment(context.getString(R.string.payment_bank_transfer), context.getString(R.string.enabled_payment_category_banktransfer)));
        paymentNameList.add(new EnabledPayment(context.getString(R.string.payment_klik_bca), null));
        paymentNameList.add(new EnabledPayment(context.getString(R.string.payment_bca_click), null));
        paymentNameList.add(new EnabledPayment(context.getString(R.string.payment_mandiri_clickpay), null));
        paymentNameList.add(new EnabledPayment(context.getString(R.string.payment_mandiri_ecash), null));
        paymentNameList.add(new EnabledPayment(context.getString(R.string.payment_epay_bri), null));
        paymentNameList.add(new EnabledPayment(context.getString(R.string.payment_cimb_clicks), null));
        paymentNameList.add(new EnabledPayment(context.getString(R.string.payment_indosat_dompetku), null));
        paymentNameList.add(new EnabledPayment(context.getString(R.string.payment_indomaret), context.getString(R.string.enabled_payment_category_cstore)));
        paymentNameList.add(new EnabledPayment(context.getString(R.string.payment_telkomsel_cash), null));
        paymentNameList.add(new EnabledPayment(context.getString(R.string.payment_xl_tunai), null));
        return paymentNameList;
    }

    public static ArrayList<SectionedPaymentMethod> getSections(Context context) {
        ArrayList<SectionedPaymentMethod> sections = new ArrayList<>();
        sections.add(SectionedPaymentMethod.createSection(new PaymentMethodsModel(context.getString(R.string.section_card)), SECTION_TYPE_CARD));
        sections.add(SectionedPaymentMethod.createSection(new PaymentMethodsModel(context.getString(R.string.section_va)), SECTION_TYPE_VA));
        sections.add(SectionedPaymentMethod.createSection(new PaymentMethodsModel(context.getString(R.string.section_direct_debit)), SECTION_TYPE_DIRECT_DEBIT));
        sections.add(SectionedPaymentMethod.createSection(new PaymentMethodsModel(context.getString(R.string.section_ewallet)), SECTION_TYPE_EWALET));
        sections.add(SectionedPaymentMethod.createSection(new PaymentMethodsModel(context.getString(R.string.section_cstore)), SECTION_TYPE_CSTORE));
        return  sections;
    }

    public static SectionedPaymentMethod getSectionedPaymentMethod(Context context, String name) {
        if (name.equals(context.getString(R.string.payment_credit_debit))) {
            return SectionedPaymentMethod.createItem(getMethodCreditCards(context, 1), SECTION_TYPE_CARD);
        } else if (name.equals(context.getString(R.string.payment_bank_transfer))) {
            return SectionedPaymentMethod.createItem(getMethodBankTransfer(context, 2), SECTION_TYPE_VA);
        } else if (name.equals(context.getString(R.string.payment_bca_click))) {
            return SectionedPaymentMethod.createItem(getMethodBCAKlikpay(context, 3), SECTION_TYPE_DIRECT_DEBIT);
        } else if (name.equals(context.getString(R.string.payment_klik_bca))) {
            return SectionedPaymentMethod.createItem(getMethodKlikBCA(context, 4), SECTION_TYPE_DIRECT_DEBIT);
        } else if (name.equals(context.getString(R.string.payment_epay_bri))) {
            return SectionedPaymentMethod.createItem(getMethodEpayBRI(context, 5), SECTION_TYPE_DIRECT_DEBIT);
        } else if (name.equals(context.getString(R.string.payment_cimb_clicks))) {
            return SectionedPaymentMethod.createItem(getMethodCIMBClicks(context, 6), SECTION_TYPE_DIRECT_DEBIT);
        } else if (name.equals(context.getString(R.string.payment_mandiri_clickpay))) {
            return SectionedPaymentMethod.createItem(getMethodMandiriClickpay(context, 7), SECTION_TYPE_DIRECT_DEBIT);
        } else if (name.equals(context.getString(R.string.payment_indomaret))) {
            return SectionedPaymentMethod.createItem(getMethodIndomaret(context, 8), SECTION_TYPE_CSTORE);
        } else if (name.equals(context.getString(R.string.payment_kioson))) {
            return SectionedPaymentMethod.createItem(getMethodKiosan(context, 9), SECTION_TYPE_CSTORE);
        } else if (name.equals(context.getString(R.string.payment_telkomsel_cash))) {
            return SectionedPaymentMethod.createItem(getMethodTelkomselCash(context, 10), SECTION_TYPE_EWALET);
        } else if (name.equals(context.getString(R.string.payment_mandiri_ecash))) {
            return SectionedPaymentMethod.createItem(getMethodMandiriECash(context, 11), SECTION_TYPE_EWALET);
        } else if (name.equals(context.getString(R.string.payment_indosat_dompetku))) {
            return SectionedPaymentMethod.createItem(getMethodIndosatDompetku(context, 12), SECTION_TYPE_EWALET);
        } else if (name.equals(context.getString(R.string.payment_xl_tunai))) {
            return SectionedPaymentMethod.createItem(getMethodXLTunai(context, 13), SECTION_TYPE_EWALET);
        }  else {
            return null;
        }
    }
}
