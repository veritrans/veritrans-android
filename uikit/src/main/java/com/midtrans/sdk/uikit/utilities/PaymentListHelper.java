package com.midtrans.sdk.uikit.utilities;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.midtrans.sdk.corekit.base.enums.PaymentType;
import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.optional.Item;
import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.PaymentInfoResponse;
import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.enablepayment.EnabledPayment;
import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.promo.Promo;
import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.promo.PromoDetails;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.base.enums.CreditCardIssuer;
import com.midtrans.sdk.uikit.base.enums.CreditCardType;
import com.midtrans.sdk.uikit.base.model.BankTransfer;
import com.midtrans.sdk.uikit.view.model.ItemViewDetails;
import com.midtrans.sdk.uikit.view.model.PaymentMethodsModel;

import java.util.ArrayList;
import java.util.List;

public class PaymentListHelper {

    /**
     * Bank Transfer is Payment Group
     * Mandiri Echannel / Mandiri Bill is Bank Transfer Mandiri
     */
    private final static String BANK_TRANSFER = "bank_transfer";

    /**
     * This method used for mapping Item Details List
     *
     * @return List<ItemViewDetails>
     */
    public static List<ItemViewDetails> mappingItemDetails(Activity context, PaymentInfoResponse response) {
        String currency = response.getTransactionDetails().getCurrency();
        List<ItemViewDetails> itemViewDetails = new ArrayList<>();
        if (response.getItemDetails() != null) {
            for (Item item : response.getItemDetails()) {
                String price = CurrencyHelper.formatAmount(context, item.getPrice(), currency);
                String itemName = item.getName();

                if (item.getQuantity() > 1) {
                    itemName = context.getString(
                            R.string.text_item_name_format,
                            item.getName(),
                            item.getQuantity());
                }

                itemViewDetails.add(new ItemViewDetails(itemName,
                        price,
                        ItemViewDetails.TYPE_ITEM,
                        true));
            }
        }
        return itemViewDetails;
    }

    /**
     * This method used for mapping the enabled payment from PaymentInfoResponse
     * It will used for making Payment list in RecyclerView
     * For bank transfer, it's group so don't add all of it but only add 1 group
     *
     * @return List<PaymentMethodsModel>
     */
    public static List<PaymentMethodsModel> mappingEnabledPayment(Activity context, PaymentInfoResponse response) {
        boolean isBankTransferAdded = false;
        List<PaymentMethodsModel> data = new ArrayList<>();
        for (EnabledPayment enabledPayment : response.getEnabledPayments()) {
            if ((enabledPayment.getCategory() != null && enabledPayment.getCategory().equalsIgnoreCase(BANK_TRANSFER))
                    || enabledPayment.getType().equalsIgnoreCase(PaymentType.ECHANNEL)) {
                if (!isBankTransferAdded) {
                    PaymentMethodsModel model = getMethods(
                            response,
                            context,
                            BANK_TRANSFER,
                            EnabledPayment.STATUS_UP
                    );
                    if (model != null) {
                        isBankTransferAdded = true;
                        data.add(model);
                    }
                }
            } else {
                PaymentMethodsModel model = getMethods(
                        response,
                        context,
                        enabledPayment.getType(),
                        enabledPayment.getStatus()
                );
                if (model != null) {
                    data.add(model);
                }
            }
        }
        markPaymentMethodHavePromo(response, data);
        return data;
    }

    /**
     * This method used for marking payment list if payment have promo then set the promo variable as true
     * It not return list, just set the variable inside model to true
     */
    private static void markPaymentMethodHavePromo(PaymentInfoResponse response, List<PaymentMethodsModel> data) {
        PromoDetails promoDetails = response.getPromoDetails();
        if (promoDetails != null) {
            List<Promo> promos = promoDetails.getPromos();
            if (promos != null && !promos.isEmpty()) {
                if (data != null && !data.isEmpty()) {
                    for (PaymentMethodsModel model : data) {
                        Promo promo = findPromoByPaymentMethod(model, promos);
                        if (promo != null) {
                            model.setHavePromo(true);
                        }
                    }
                }
            }
        }
    }

    /**
     * This method used for searching promo inside list promo by checking one by one in payment type
     *
     * @return Promo
     */
    private static Promo findPromoByPaymentMethod(PaymentMethodsModel model, List<Promo> promos) {
        for (Promo promo : promos) {
            List<String> paymentTypes = promo.getPaymentTypes();
            if (paymentTypes != null && !paymentTypes.isEmpty()) {
                if (paymentTypes.contains(model.getPaymentType())) {
                    return promo;
                }
            }
        }
        return null;
    }

    public static List<EnabledPayment> mappingBankTransfer(PaymentInfoResponse response) {
        List<EnabledPayment> bankTransfers = new ArrayList<>();
        for (EnabledPayment enabledPayment : response.getEnabledPayments()) {
            if ((enabledPayment.getCategory() != null && enabledPayment.getCategory().equalsIgnoreCase(BANK_TRANSFER))
                    || enabledPayment.getType().equalsIgnoreCase(PaymentType.ECHANNEL)) {
                bankTransfers.add(enabledPayment);
            }
        }
        return bankTransfers;
    }

    /**
     * This method used for making payment list model, it will construct payment name, description, etc
     *
     * @return PaymentMethodModel
     */
    private static PaymentMethodsModel getMethods(
            PaymentInfoResponse response,
            Activity context,
            String paymentType,
            String status
    ) {
        if (paymentType.equalsIgnoreCase(PaymentType.CREDIT_CARD)) {
            return getMethodCreditCards(response, context, 1, paymentType, status);
        } else if (paymentType.equalsIgnoreCase(BANK_TRANSFER)) {
            return new PaymentMethodsModel(
                    context.getString(R.string.payment_method_bank_transfer),
                    context.getString(R.string.payment_method_description_bank_transfer),
                    R.drawable.ic_atm,
                    paymentType,
                    2,
                    status
            );
        } else if (paymentType.equalsIgnoreCase(PaymentType.BCA_KLIKPAY)) {
            return new PaymentMethodsModel(
                    context.getString(R.string.payment_method_bca_klikpay),
                    context.getString(R.string.payment_method_description_bca_klikpay),
                    R.drawable.ic_klikpay,
                    paymentType,
                    3,
                    status
            );
        } else if (paymentType.equalsIgnoreCase(PaymentType.KLIK_BCA)) {
            return new PaymentMethodsModel(
                    context.getString(R.string.payment_method_klik_bca),
                    context.getString(R.string.payment_method_description_klik_bca),
                    R.drawable.ic_klikbca, paymentType,
                    4,
                    status
            );
        } else if (paymentType.equalsIgnoreCase(PaymentType.BRI_EPAY)) {
            return new PaymentMethodsModel(
                    context.getString(R.string.payment_method_bri_epay),
                    context.getString(R.string.payment_method_description_epay_bri),
                    R.drawable.ic_epay,
                    paymentType,
                    5,
                    status
            );
        } else if (paymentType.equalsIgnoreCase(PaymentType.CIMB_CLICKS)) {
            return new PaymentMethodsModel(
                    context.getString(R.string.payment_method_cimb_clicks),
                    context.getString(R.string.payment_method_description_cimb_clicks),
                    R.drawable.ic_cimb,
                    paymentType,
                    6,
                    status
            );
        } else if (paymentType.equalsIgnoreCase(PaymentType.MANDIRI_CLICKPAY)) {
            return new PaymentMethodsModel(
                    context.getString(R.string.payment_method_mandiri_clickpay),
                    context.getString(R.string.payment_method_description_mandiri_clickpay),
                    R.drawable.ic_mandiri2,
                    paymentType,
                    7,
                    status
            );
        } else if (paymentType.equalsIgnoreCase(PaymentType.INDOMARET)) {
            return new PaymentMethodsModel(
                    context.getString(R.string.payment_method_indomaret),
                    context.getString(R.string.payment_method_description_indomaret),
                    R.drawable.ic_indomaret,
                    paymentType,
                    8,
                    status
            );
        } else if (paymentType.equalsIgnoreCase(PaymentType.TELKOMSEL_CASH)) {
            return new PaymentMethodsModel(
                    context.getString(R.string.payment_method_telkomsel_cash),
                    context.getString(R.string.payment_method_description_telkomsel_cash),
                    R.drawable.ic_telkomsel, paymentType,
                    10,
                    status
            );
        } else if (paymentType.equalsIgnoreCase(PaymentType.MANDIRI_ECASH)) {
            return new PaymentMethodsModel(
                    context.getString(R.string.payment_method_mandiri_ecash),
                    context.getString(R.string.payment_method_description_mandiri_ecash),
                    R.drawable.ic_mandiri_e_cash,
                    paymentType,
                    11,
                    status
            );
        } else if (paymentType.equalsIgnoreCase(PaymentType.GOPAY)) {
            return new PaymentMethodsModel(
                    context.getString(R.string.payment_method_gopay),
                    context.getString(R.string.payment_method_description_gopay),
                    R.drawable.ic_gopay,
                    paymentType,
                    15,
                    status
            );
        } else if (paymentType.equalsIgnoreCase(PaymentType.DANAMON_ONLINE)) {
            return new PaymentMethodsModel(
                    context.getString(R.string.payment_method_danamon_online),
                    context.getString(R.string.payment_method_description_danamon_online),
                    R.drawable.ic_danamon_online,
                    paymentType,
                    16,
                    status
            );
        } else if (paymentType.equalsIgnoreCase(PaymentType.AKULAKU)) {
            return new PaymentMethodsModel(
                    context.getString(R.string.payment_method_akulaku),
                    context.getString(R.string.payment_method_description_akulaku),
                    R.drawable.ic_akulaku,
                    paymentType,
                    17,
                    status
            );
        } else if (paymentType.equalsIgnoreCase(PaymentType.ALFAMART)) {
            return new PaymentMethodsModel(
                    context.getString(R.string.payment_method_alfamart),
                    context.getString(R.string.payment_method_description_alfamart),
                    R.drawable.ic_alfamart,
                    paymentType,
                    18,
                    status
            );
        } else {
            return null;
        }
    }

    /**
     * This method use for deciding what type of credit card then show the payment method icon based on type
     *
     * @param response
     * @return int
     */
    public static int getCreditCardIconType(PaymentInfoResponse response) {
        if (response.getMerchantData() != null) {
            List<String> principles = response.getMerchantData().getEnabledPrinciples();
            if (principles != null && principles.contains(CreditCardIssuer.MASTERCARD) && principles.contains(CreditCardIssuer.VISA)) {
                if (principles.contains(CreditCardIssuer.JCB)) {
                    if (principles.contains(CreditCardIssuer.AMEX)) {
                        return CreditCardType.TYPE_MASTER_VISA_JCB_AMEX;
                    }
                    return CreditCardType.TYPE_MASTER_VISA_JCB;
                } else if (principles.contains(CreditCardIssuer.AMEX)) {
                    return CreditCardType.TYPE_MASTER_VISA_AMEX;
                }
                return CreditCardType.TYPE_MASTER_VISA;
            }
        }

        return CreditCardType.TYPE_UNKNOWN;
    }

    /**
     * This method used for making payment list model for CreditCard, it will construct payment name, description, etc
     *
     * @return PaymentMethodModel
     */
    private static PaymentMethodsModel getMethodCreditCards(
            PaymentInfoResponse response,
            Activity context,
            int priority,
            String paymentType,
            String status
    ) {
        int creditCardSupportType = getCreditCardIconType(response);
        switch (creditCardSupportType) {
            case CreditCardType.TYPE_MASTER_VISA_JCB_AMEX:
                return new PaymentMethodsModel(
                        context.getString(R.string.payment_method_credit_card),
                        context.getString(R.string.payment_method_description_credit_card),
                        R.drawable.ic_credit,
                        paymentType,
                        priority,
                        status
                );
            case CreditCardType.TYPE_MASTER_VISA_JCB:
                return new PaymentMethodsModel(
                        context.getString(R.string.payment_method_credit_card),
                        context.getString(R.string.payment_method_description_credit_card_3),
                        R.drawable.ic_credit_3,
                        paymentType,
                        priority,
                        status
                );
            case CreditCardType.TYPE_MASTER_VISA_AMEX:
                return new PaymentMethodsModel(
                        context.getString(R.string.payment_method_credit_card),
                        context.getString(R.string.payment_method_description_credit_card_4),
                        R.drawable.ic_credit_4,
                        paymentType,
                        priority,
                        status
                );
            default:
                return new PaymentMethodsModel(
                        context.getString(R.string.payment_method_credit_card),
                        context.getString(R.string.payment_method_description_credit_card_2),
                        R.drawable.ic_credit_2,
                        paymentType,
                        priority,
                        status
                );
        }
    }

    public static BankTransfer createBankTransferModel(Context context, String type, String status) {
        BankTransfer bankTransfer = null;
        if (!TextUtils.isEmpty(type)) {
            switch (type) {
                case PaymentType.BCA_VA:
                    bankTransfer = new BankTransfer(type, context.getString(R.string.bca_bank_transfer), R.drawable.ic_bca, 1, context.getString(R.string.payment_bank_description_bca), status);
                    break;

                case PaymentType.ECHANNEL:
                    bankTransfer = new BankTransfer(type, context.getString(R.string.mandiri_bill), R.drawable.ic_mandiri_bill_payment2, 2, context.getString(R.string.payment_bank_description_mandiri), status);
                    break;

                case PaymentType.BNI_VA:
                    bankTransfer = new BankTransfer(type, context.getString(R.string.bni_bank_transfer), R.drawable.ic_bni, 4, context.getString(R.string.payment_bank_description_bni), status);
                    break;

                case PaymentType.PERMATA_VA:
                    bankTransfer = new BankTransfer(type, context.getString(R.string.permata_bank_transfer), R.drawable.ic_permata, 3, context.getString(R.string.payment_bank_description_permata), status);
                    break;

                case PaymentType.OTHER_VA:
                    bankTransfer = new BankTransfer(type, context.getString(R.string.all_bank_transfer), R.drawable.ic_atm, 5, context.getString(R.string.payment_bank_description_other), status);
                    break;
            }

        }

        return bankTransfer;
    }
}