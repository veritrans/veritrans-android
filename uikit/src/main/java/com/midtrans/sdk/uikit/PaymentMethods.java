package com.midtrans.sdk.uikit;

import android.content.Context;

import com.midtrans.sdk.corekit.core.Constants;
import com.midtrans.sdk.corekit.models.BankTransferModel;
import com.midtrans.sdk.corekit.models.PaymentMethodsModel;
import com.midtrans.sdk.corekit.models.snap.EnabledPayment;
import com.midtrans.sdk.uikit.models.CreditCardType;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Payment method list.
 *
 * @author rakawm
 */
public class PaymentMethods {

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
        } else if (name.equals(context.getString(R.string.payment_gci))) {
            return getMethodGCI(context, 14);
        } else {
            return null;
        }
    }

    private static PaymentMethodsModel getMethodCreditCards(Context context, int priority) {
        int creditCardSupportType = SdkUIFlowUtil.getCreditCardIconType();
        switch (creditCardSupportType) {
            case CreditCardType.TYPE_MASTER_VISA_JCB_AMEX:
                return new PaymentMethodsModel(context.getString(R.string.payment_method_credit_card), context.getString(R.string.payment_method_description_credit_card), R.drawable.ic_credit, Constants.PAYMENT_METHOD_NOT_SELECTED, priority);
            case CreditCardType.TYPE_MASTER_VISA_JCB:
                return new PaymentMethodsModel(context.getString(R.string.payment_method_credit_card), context.getString(R.string.payment_method_description_credit_card_3), R.drawable.ic_credit_3, Constants.PAYMENT_METHOD_NOT_SELECTED, priority);
            case CreditCardType.TYPE_MASTER_VISA_AMEX:
                return new PaymentMethodsModel(context.getString(R.string.payment_method_credit_card), context.getString(R.string.payment_method_description_credit_card_4), R.drawable.ic_credit_4, Constants.PAYMENT_METHOD_NOT_SELECTED, priority);
            default:
                return new PaymentMethodsModel(context.getString(R.string.payment_method_credit_card), context.getString(R.string.payment_method_description_credit_card_2), R.drawable.ic_credit_2, Constants.PAYMENT_METHOD_NOT_SELECTED, priority);
        }
    }

    private static PaymentMethodsModel getMethodBankTransfer(Context context, int priority) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_bank_transfer), context.getString(R.string.payment_method_description_bank_transfer), R.drawable.ic_atm, Constants.PAYMENT_METHOD_NOT_SELECTED, priority);
    }

    private static PaymentMethodsModel getMethodBCAKlikpay(Context context, int priority) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_bca_klikpay), context.getString(R.string.payment_method_description_bca_klikpay), R.drawable.ic_klikpay, Constants.PAYMENT_METHOD_NOT_SELECTED, priority);
    }

    private static PaymentMethodsModel getMethodKlikBCA(Context context, int priority) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_klik_bca), context.getString(R.string.payment_method_description_klik_bca), R.drawable.ic_klikbca, Constants.PAYMENT_METHOD_NOT_SELECTED, priority);
    }

    private static PaymentMethodsModel getMethodEpayBRI(Context context, int priority) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_bri_epay), context.getString(R.string.payment_method_description_epay_bri), R.drawable.ic_epay, Constants.PAYMENT_METHOD_NOT_SELECTED, priority);
    }

    private static PaymentMethodsModel getMethodCIMBClicks(Context context, int priority) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_cimb_clicks), context.getString(R.string.payment_method_description_cimb_clicks), R.drawable.ic_cimb, Constants.PAYMENT_METHOD_NOT_SELECTED, priority);
    }

    private static PaymentMethodsModel getMethodMandiriClickpay(Context context, int priority) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_mandiri_clickpay), context.getString(R.string.payment_method_description_mandiri_clickpay), R.drawable.ic_mandiri2, Constants.PAYMENT_METHOD_NOT_SELECTED, priority);
    }

    private static PaymentMethodsModel getMethodIndomaret(Context context, int priority) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_indomaret), context.getString(R.string.payment_method_description_indomaret), R.drawable.ic_indomaret, Constants.PAYMENT_METHOD_NOT_SELECTED, priority);
    }

    private static PaymentMethodsModel getMethodKiosan(Context context, int priority) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_kioson), context.getString(R.string.payment_method_description_kioson), R.drawable.ic_kioson, Constants.PAYMENT_METHOD_NOT_SELECTED, priority);
    }

    private static PaymentMethodsModel getMethodTelkomselCash(Context context, int priority) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_telkomsel_cash), context.getString(R.string.payment_method_description_telkomsel_cash), R.drawable.ic_telkomsel, Constants.PAYMENT_METHOD_NOT_SELECTED, priority);
    }

    private static PaymentMethodsModel getMethodMandiriECash(Context context, int priority) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_mandiri_ecash), context.getString(R.string.payment_method_description_mandiri_ecash), R.drawable.ic_mandiri_e_cash, Constants.PAYMENT_METHOD_NOT_SELECTED, priority);
    }

    private static PaymentMethodsModel getMethodIndosatDompetku(Context context, int priority) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_indosat_dompetku), context.getString(R.string.payment_method_description_indosat_dompetku), R.drawable.ic_indosat, Constants.PAYMENT_METHOD_NOT_SELECTED, priority);
    }

    static PaymentMethodsModel getMethodXLTunai(Context context, int priority) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_xl_tunai), context.getString(R.string.payment_method_description_xl_tunai), R.drawable.ic_xl, Constants.PAYMENT_METHOD_NOT_SELECTED, priority);
    }

    private static PaymentMethodsModel getMethodGCI(Context context, int priority) {
        return new PaymentMethodsModel(context.getString(R.string.payment_method_gci), context.getString(R.string.payment_method_description_gci), R.drawable.ic_gci, Constants.PAYMENT_METHOD_NOT_SELECTED, priority);
    }

    public static ArrayList<BankTransferModel> getBankTransferList(Context context) {
        ArrayList<BankTransferModel> models = new ArrayList<>();
        models.add(getBankTransferModel(context, context.getString(R.string.payment_bca_va)));
        models.add(getBankTransferModel(context, context.getString(R.string.payment_mandiri_bill_payment)));
        models.add(getBankTransferModel(context, context.getString(R.string.payment_permata_va)));
        models.add(getBankTransferModel(context, context.getString(R.string.payment_all_va)));
        return models;
    }

    public static BankTransferModel getBankTransferModel(Context context, String name) {
        if (name.equals(context.getString(R.string.payment_bca_va))) {
            return new BankTransferModel(context.getString(R.string.bca_bank_transfer), R.drawable.ic_bca, true, 1, context.getString(R.string.payment_bank_description_bca));
        } else if (name.equals(context.getString(R.string.payment_permata_va))) {
            return new BankTransferModel(context.getString(R.string.permata_bank_transfer), R.drawable.ic_permata, true, 3, context.getString(R.string.payment_bank_description_permata));
        } else if (name.equals(context.getString(R.string.payment_all_va))) {
            return new BankTransferModel(context.getString(R.string.all_bank_transfer), R.drawable.ic_atm, true, 5, context.getString(R.string.payment_bank_description_other));
        } else if (name.equals(context.getString(R.string.payment_mandiri_bill_payment))) {
            return new BankTransferModel(context.getString(R.string.mandiri_bank_transfer), R.drawable.ic_mandiri_bill_payment2, true, 2, context.getString(R.string.payment_bank_description_mandiri));
        } else if (name.equals(context.getString(R.string.payment_bni_va))) {
            return new BankTransferModel(context.getString(R.string.bni_bank_transfer), R.drawable.ic_bni, true, 4, context.getString(R.string.payment_bank_description_bni));
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

    public static List<EnabledPayment> getPaymentList(Context context, List<String> methods) {
        List<EnabledPayment> enabledPayments = new ArrayList<>();
        for (String name : methods) {
            if (name.equals(context.getString(R.string.payment_credit_debit))) {
                enabledPayments.add(new EnabledPayment(context.getString(R.string.payment_credit_debit), null));
            } else if (name.equals(context.getString(R.string.payment_bank_transfer))) {
                enabledPayments.add(new EnabledPayment(context.getString(R.string.payment_bank_transfer), context.getString(R.string.enabled_payment_category_banktransfer)));
            } else if (name.equals(context.getString(R.string.payment_bca_click))) {
                enabledPayments.add(new EnabledPayment(context.getString(R.string.payment_bca_click), null));
            } else if (name.equals(context.getString(R.string.payment_klik_bca))) {
                enabledPayments.add(new EnabledPayment(context.getString(R.string.payment_klik_bca), null));
            } else if (name.equals(context.getString(R.string.payment_epay_bri))) {
                enabledPayments.add(new EnabledPayment(context.getString(R.string.payment_epay_bri), null));
            } else if (name.equals(context.getString(R.string.payment_cimb_clicks))) {
                enabledPayments.add(new EnabledPayment(context.getString(R.string.payment_cimb_clicks), null));
            } else if (name.equals(context.getString(R.string.payment_mandiri_clickpay))) {
                enabledPayments.add(new EnabledPayment(context.getString(R.string.payment_mandiri_clickpay), null));
            } else if (name.equals(context.getString(R.string.payment_indomaret))) {
                enabledPayments.add(new EnabledPayment(context.getString(R.string.payment_indomaret), context.getString(R.string.enabled_payment_category_cstore)));
            } else if (name.equals(context.getString(R.string.payment_kioson))) {
                enabledPayments.add(new EnabledPayment(context.getString(R.string.payment_kioson), context.getString(R.string.enabled_payment_category_cstore)));
            } else if (name.equals(context.getString(R.string.payment_telkomsel_cash))) {
                enabledPayments.add(new EnabledPayment(context.getString(R.string.payment_telkomsel_cash), null));
            } else if (name.equals(context.getString(R.string.payment_mandiri_ecash))) {
                enabledPayments.add(new EnabledPayment(context.getString(R.string.payment_mandiri_ecash), null));
            } else if (name.equals(context.getString(R.string.payment_indosat_dompetku))) {
                enabledPayments.add(new EnabledPayment(context.getString(R.string.payment_indosat_dompetku), null));
            } else if (name.equals(context.getString(R.string.payment_xl_tunai))) {
                enabledPayments.add(new EnabledPayment(context.getString(R.string.payment_xl_tunai), null));
            } else if (name.equals(context.getString(R.string.payment_gci))) {
                enabledPayments.add(new EnabledPayment(context.getString(R.string.payment_gci), null));
            } else if (name.equals(context.getString(R.string.payment_bca_va))) {
                enabledPayments.add(new EnabledPayment(context.getString(R.string.payment_bca_va), context.getString(R.string.payment_bank_transfer)));
            } else if (name.equals(context.getString(R.string.payment_permata_va))) {
                enabledPayments.add(new EnabledPayment(context.getString(R.string.payment_permata_va), context.getString(R.string.payment_bank_transfer)));
            } else if (name.equals(context.getString(R.string.payment_all_va))) {
                enabledPayments.add(new EnabledPayment(context.getString(R.string.payment_all_va), context.getString(R.string.payment_bank_transfer)));
            } else if (name.equals(context.getString(R.string.payment_mandiri_bill_payment))) {
                enabledPayments.add(new EnabledPayment(context.getString(R.string.payment_mandiri_bill_payment), context.getString(R.string.payment_bank_transfer)));
            } else if (name.equals(context.getString(R.string.payment_bni_va))) {
                enabledPayments.add(new EnabledPayment(context.getString(R.string.payment_bni_va), context.getString(R.string.payment_bank_transfer)));
            }
        }
        return enabledPayments;
    }
}
