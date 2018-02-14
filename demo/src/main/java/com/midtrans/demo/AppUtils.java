package com.midtrans.demo;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;

import com.midtrans.sdk.corekit.core.PaymentType;
import com.midtrans.sdk.corekit.models.PaymentMethodsModel;
import com.midtrans.sdk.corekit.models.snap.EnabledPayment;
import com.midtrans.sdk.uikit.PaymentMethods;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by ziahaqi on 11/17/17.
 */

public class AppUtils {

    public static final String CATEGORY_CREDIT_CARD = "Credit/Debit Card";
    public static final String CATEGORY_VA = "ATM/Bank Transfer";
    public static final String CATEGORY_DIRECT_DEBIT = "Direct Debit";
    public static final String CATEGORY_EMONEY = "e-Money";
    public static final String CATEGORY_CONVENIENT_STORE = "Convenient Store";
    public static final String CATEGORY_VOUCHER_CARD = "Voucher Card";
    private static final String GROUP_BANK_TRANSFER = "bank_transfer";
    private static final String PAYMENT_NAME_PERMATA_VA = "Permata VA";
    private static final String PAYMENT_NAME_BCA_VA = "BCA VA";
    private static final String PAYMENT_NAME_BNI_VA = "BNI VA";
    private static final String PAYMENT_NAME_OTHER_VA = "Other VA";
    private static final String PAYMENT_NAME_MANDIRI_ECHANNEL = "Mandiri VA";
    private static final String TYPE_PHONE = "PHONE";
    private static final String TYPE_TABLET = "TABLET";

    public static boolean isActivePaymentChannel(Context context, SelectPaymentMethodViewModel channel) {
        if (channel != null) {
            if (!channel.getMethodType().equals(context.getString(R.string.payment_indosat_dompetku))
                    && !channel.getMethodType().equals(context.getString(R.string.payment_xl_tunai)))

                return true;
        }

        return false;
    }

    public static List<EnabledPayment> getDefaultPaymentList(Activity activity) {
        List<EnabledPayment> paymentNameList = new ArrayList<>();
        paymentNameList.add(new EnabledPayment(PaymentType.CREDIT_CARD, CATEGORY_CREDIT_CARD));

        paymentNameList.add(new EnabledPayment(PaymentType.PERMATA_VA, CATEGORY_VA));
        paymentNameList.add(new EnabledPayment(PaymentType.BCA_VA, CATEGORY_VA));
        paymentNameList.add(new EnabledPayment(PaymentType.BNI_VA, CATEGORY_VA));
        paymentNameList.add(new EnabledPayment(PaymentType.ALL_VA, CATEGORY_VA));
        paymentNameList.add(new EnabledPayment(PaymentType.E_CHANNEL, CATEGORY_VA));

        paymentNameList.add(new EnabledPayment(PaymentType.KLIK_BCA, CATEGORY_DIRECT_DEBIT));
        paymentNameList.add(new EnabledPayment(PaymentType.BCA_KLIKPAY, CATEGORY_DIRECT_DEBIT));
        paymentNameList.add(new EnabledPayment(PaymentType.CIMB_CLICKS, CATEGORY_DIRECT_DEBIT));
        paymentNameList.add(new EnabledPayment(PaymentType.MANDIRI_CLICKPAY, CATEGORY_DIRECT_DEBIT));
        paymentNameList.add(new EnabledPayment(PaymentType.BRI_EPAY, CATEGORY_DIRECT_DEBIT));
        paymentNameList.add(new EnabledPayment(PaymentType.DANAMON_ONLINE, CATEGORY_DIRECT_DEBIT));

        paymentNameList.add(new EnabledPayment(PaymentType.GOPAY, CATEGORY_EMONEY));
        paymentNameList.add(new EnabledPayment(PaymentType.MANDIRI_ECASH, CATEGORY_EMONEY));
        paymentNameList.add(new EnabledPayment(PaymentType.TELKOMSEL_CASH, CATEGORY_EMONEY));
//        paymentNameList.add(new EnabledPayment(PaymentType.XL_TUNAI, null)); being deprecated

        paymentNameList.add(new EnabledPayment(PaymentType.INDOMARET, CATEGORY_CONVENIENT_STORE));
//        paymentNameList.add(new EnabledPayment(PaymentType.KIOSON, CATEGORY_CONVENIENT_STORE)); being deprecated
        paymentNameList.add(new EnabledPayment(PaymentType.GCI, CATEGORY_VOUCHER_CARD));
//        paymentNameList.add(new EnabledPayment(PaymentType.INDOSAT_DOMPETKU, null)); being deprecated

        return paymentNameList;
    }

    public static PaymentMethodsModel getVaPaymentMethods(Context context, String type, String status) {
        PaymentMethodsModel model = PaymentMethods.getMethods(context, type, status);

        if (model == null && !TextUtils.isEmpty(type)) {
            switch (type) {
                case PaymentType.PERMATA_VA:
                    model = new PaymentMethodsModel(PAYMENT_NAME_PERMATA_VA, null, 0, -1, 4, status);
                    break;

                case PaymentType.BCA_VA:
                    model = new PaymentMethodsModel(PAYMENT_NAME_BCA_VA, null, 0, -1, 2, status);
                    break;

                case PaymentType.BNI_VA:
                    model = new PaymentMethodsModel(PAYMENT_NAME_BNI_VA, null, 0, -1, 5, status);
                    break;

                case PaymentType.ALL_VA:
                    model = new PaymentMethodsModel(PAYMENT_NAME_OTHER_VA, null, 0, -1, 6, status);
                    break;

                case PaymentType.E_CHANNEL:
                    model = new PaymentMethodsModel(PAYMENT_NAME_MANDIRI_ECHANNEL, null, 0, -1, 3, status);
                    break;
            }
        }
        return model;
    }


    private static List<SelectPaymentMethodViewModel> createHeaderModel() {
        List<SelectPaymentMethodViewModel> headers = new ArrayList<>();
        headers.add(new SelectPaymentMethodViewModel(CATEGORY_CREDIT_CARD, "", false, ""));
        headers.add(new SelectPaymentMethodViewModel(CATEGORY_VA, "", false, ""));
        headers.add(new SelectPaymentMethodViewModel(CATEGORY_DIRECT_DEBIT, "", false, ""));
        headers.add(new SelectPaymentMethodViewModel(CATEGORY_EMONEY, "", false, ""));
        headers.add(new SelectPaymentMethodViewModel(CATEGORY_CONVENIENT_STORE, "", false, ""));
        headers.add(new SelectPaymentMethodViewModel(CATEGORY_VOUCHER_CARD, "", false, ""));
        return headers;
    }


    public static List<SelectPaymentMethodViewModel> mapPaymentMethods(Context context, List<EnabledPayment> enabledPayments) {
        List<SelectPaymentMethodViewModel> viewModels = new ArrayList<>();
        for (int i = 0; i < enabledPayments.size(); i++) {
            EnabledPayment enabledPayment = enabledPayments.get(i);
            Log.d("xchannel", "type:" + enabledPayment.getType());

            PaymentMethodsModel model = getVaPaymentMethods(context, enabledPayment.getType(), enabledPayment.getStatus());
            if (model != null) {
                SelectPaymentMethodViewModel newModel = createSelectPaymentMethodViewModel(model, enabledPayment);
                viewModels.add(newModel);
            }
        }

        Collections.sort(viewModels, new Comparator<SelectPaymentMethodViewModel>() {
            @Override
            public int compare(SelectPaymentMethodViewModel o1, SelectPaymentMethodViewModel o2) {
                return o1.getPriority().compareTo(o2.getPriority());
            }
        });

        return viewModels;
    }

    private static SelectPaymentMethodViewModel createSelectPaymentMethodViewModel(PaymentMethodsModel model, EnabledPayment enablePayment) {

        SelectPaymentMethodViewModel newModel = new SelectPaymentMethodViewModel(model.getName(), enablePayment.getType(), true, null);

        switch (newModel.getMethodType()) {

            case PaymentType.CREDIT_CARD:
                newModel.setCategory(CATEGORY_CREDIT_CARD);
                newModel.setPriority(1);
                break;

            case PaymentType.PERMATA_VA:
            case PaymentType.BNI_VA:
            case PaymentType.BCA_VA:
            case PaymentType.ALL_VA:
            case PaymentType.E_CHANNEL:
                newModel.setCategory(CATEGORY_VA);

                break;
            case PaymentType.KLIK_BCA:
                newModel.setCategory(CATEGORY_DIRECT_DEBIT);
                newModel.setPriority(7);
                break;
            case PaymentType.BCA_KLIKPAY:
                newModel.setCategory(CATEGORY_DIRECT_DEBIT);
                newModel.setPriority(8);
                break;
            case PaymentType.MANDIRI_CLICKPAY:
                newModel.setCategory(CATEGORY_DIRECT_DEBIT);
                newModel.setPriority(9);
                break;
            case PaymentType.BRI_EPAY:
                newModel.setCategory(CATEGORY_DIRECT_DEBIT);
                newModel.setPriority(10);
                break;
            case PaymentType.CIMB_CLICKS:
                newModel.setCategory(CATEGORY_DIRECT_DEBIT);
                newModel.setPriority(11);
                break;
            case PaymentType.DANAMON_ONLINE:
                newModel.setCategory(CATEGORY_DIRECT_DEBIT);
                newModel.setPriority(12);
                break;
            case PaymentType.GOPAY:
                newModel.setCategory(CATEGORY_EMONEY);
                newModel.setPriority(13);
                break;
            case PaymentType.MANDIRI_ECASH:
                newModel.setCategory(CATEGORY_EMONEY);
                newModel.setPriority(14);
                break;
            case PaymentType.TELKOMSEL_CASH:
                newModel.setCategory(CATEGORY_EMONEY);
                newModel.setPriority(15);
                break;
            case PaymentType.XL_TUNAI:
                newModel.setCategory(CATEGORY_DIRECT_DEBIT);
                newModel.setPriority(16);
                break;
            case PaymentType.INDOMARET:
                newModel.setCategory(CATEGORY_CONVENIENT_STORE);
                newModel.setPriority(17);
                break;
            case PaymentType.KIOSON:
                newModel.setCategory(CATEGORY_CONVENIENT_STORE);
                newModel.setPriority(17);
                break;
            case PaymentType.GCI:
                newModel.setCategory(CATEGORY_VOUCHER_CARD);
                newModel.setPriority(17);
                break;
        }
        return newModel;
    }


    public static List<SelectPaymentMethodViewModel> createPaymentMethodViewModels(Context context, List<EnabledPayment> enabledPayments) {
        List<SelectPaymentMethodViewModel> paymentMethods = mapPaymentMethods(context, enabledPayments);
        return createPaymentMethodViewModels(paymentMethods);
    }


    public static List<SelectPaymentMethodViewModel> createPaymentMethodViewModels(List<SelectPaymentMethodViewModel> enabledPaymentModels) {
        List<SelectPaymentMethodViewModel> paymentMethods = enabledPaymentModels;
        List<SelectPaymentMethodViewModel> headers = createHeaderModel();
        List<SelectPaymentMethodViewModel> viewModels = new ArrayList<>();
        for (SelectPaymentMethodViewModel header : headers) {

            switch (header.getMethodName()) {

                case CATEGORY_CREDIT_CARD:
                    viewModels.add(header);
                    viewModels.addAll(findViewModelByCategory(paymentMethods, CATEGORY_CREDIT_CARD));
                    break;

                case CATEGORY_VA:
                    viewModels.add(header);
                    viewModels.addAll(findViewModelByCategory(paymentMethods, CATEGORY_VA));
                    break;

                case CATEGORY_DIRECT_DEBIT:
                    viewModels.add(header);
                    viewModels.addAll(findViewModelByCategory(paymentMethods, CATEGORY_DIRECT_DEBIT));
                    break;

                case CATEGORY_EMONEY:
                    viewModels.add(header);
                    viewModels.addAll(findViewModelByCategory(paymentMethods, CATEGORY_EMONEY));
                    break;

                case CATEGORY_CONVENIENT_STORE:
                    viewModels.add(header);
                    viewModels.addAll(findViewModelByCategory(paymentMethods, CATEGORY_CONVENIENT_STORE));
                    break;

                case CATEGORY_VOUCHER_CARD:
                    viewModels.add(header);
                    viewModels.addAll(findViewModelByCategory(paymentMethods, CATEGORY_VOUCHER_CARD));
                    break;
            }
        }
        return viewModels;
    }


    private static List<SelectPaymentMethodViewModel> findViewModelByCategory(List<SelectPaymentMethodViewModel> paymentMethods, String category) {
        List<SelectPaymentMethodViewModel> filteredItem = new ArrayList<>();
        for (SelectPaymentMethodViewModel model : paymentMethods) {
            if (!TextUtils.isEmpty(model.getCategory()) && model.getCategory().equals(category)) {
                filteredItem.add(model);
            }
        }
        return filteredItem;
    }


    public static List<EnabledPayment> getPaymentList(Context context, List<String> methods) {
        List<EnabledPayment> enabledPayments = new ArrayList<>();
        for (String name : methods) {
            if (name.equals(PaymentType.CREDIT_CARD)) {
                enabledPayments.add(new EnabledPayment(PaymentType.CREDIT_CARD, CATEGORY_CREDIT_CARD));
            } else if (name.equals(PaymentType.BCA_KLIKPAY)) {
                enabledPayments.add(new EnabledPayment(PaymentType.BCA_KLIKPAY, CATEGORY_DIRECT_DEBIT));
            } else if (name.equals(PaymentType.KLIK_BCA)) {
                enabledPayments.add(new EnabledPayment(PaymentType.KLIK_BCA, CATEGORY_DIRECT_DEBIT));
            } else if (name.equals(PaymentType.BRI_EPAY)) {
                enabledPayments.add(new EnabledPayment(PaymentType.BRI_EPAY, CATEGORY_DIRECT_DEBIT));
            } else if (name.equals(PaymentType.CIMB_CLICKS)) {
                enabledPayments.add(new EnabledPayment(PaymentType.CIMB_CLICKS, CATEGORY_DIRECT_DEBIT));
            } else if (name.equals(PaymentType.MANDIRI_CLICKPAY)) {
                enabledPayments.add(new EnabledPayment(PaymentType.MANDIRI_CLICKPAY, CATEGORY_DIRECT_DEBIT));
            } else if (name.equals(PaymentType.INDOMARET)) {
                enabledPayments.add(new EnabledPayment(PaymentType.INDOMARET, CATEGORY_CONVENIENT_STORE));
            } else if (name.equals(PaymentType.KIOSON)) {
                enabledPayments.add(new EnabledPayment(PaymentType.KIOSON, CATEGORY_CONVENIENT_STORE));
            } else if (name.equals(PaymentType.TELKOMSEL_CASH)) {
                enabledPayments.add(new EnabledPayment(PaymentType.TELKOMSEL_CASH, CATEGORY_EMONEY));
            } else if (name.equals(PaymentType.MANDIRI_ECASH)) {
                enabledPayments.add(new EnabledPayment(PaymentType.MANDIRI_ECASH, CATEGORY_EMONEY));
            } else if (name.equals(PaymentType.INDOSAT_DOMPETKU)) {
                enabledPayments.add(new EnabledPayment(PaymentType.INDOSAT_DOMPETKU, CATEGORY_EMONEY));
            } else if (name.equals(PaymentType.XL_TUNAI)) {
                enabledPayments.add(new EnabledPayment(PaymentType.XL_TUNAI, CATEGORY_EMONEY));
            } else if (name.equals(PaymentType.GCI)) {
                enabledPayments.add(new EnabledPayment(PaymentType.GCI, CATEGORY_VOUCHER_CARD));
            } else if (name.equals(PaymentType.BCA_VA)) {
                enabledPayments.add(new EnabledPayment(PaymentType.BCA_VA, CATEGORY_VA));
            } else if (name.equals(PaymentType.PERMATA_VA)) {
                enabledPayments.add(new EnabledPayment(PaymentType.PERMATA_VA, CATEGORY_VA));
            } else if (name.equals(PaymentType.ALL_VA)) {
                enabledPayments.add(new EnabledPayment(PaymentType.ALL_VA, CATEGORY_VA));
            } else if (name.equals(PaymentType.E_CHANNEL)) {
                enabledPayments.add(new EnabledPayment(PaymentType.E_CHANNEL, CATEGORY_VA));
            } else if (name.equals(PaymentType.BNI_VA)) {
                enabledPayments.add(new EnabledPayment(PaymentType.BNI_VA, CATEGORY_VA));
            } else if (name.equals(PaymentType.DANAMON_ONLINE)) {
                enabledPayments.add(new EnabledPayment(PaymentType.DANAMON_ONLINE, CATEGORY_DIRECT_DEBIT));
            } else if (name.equals(PaymentType.GOPAY)) {
                enabledPayments.add(new EnabledPayment(PaymentType.GOPAY, CATEGORY_EMONEY));
            }
        }
        return enabledPayments;
    }

    public static String getDeviceType(Activity activity) {
        String deviceType;
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        float yInches = metrics.heightPixels / metrics.ydpi;
        float xInches = metrics.widthPixels / metrics.xdpi;
        double diagonalInches = Math.sqrt(xInches * xInches + yInches * yInches);

        if (diagonalInches >= 6.5) {
            deviceType = TYPE_TABLET;
        } else {
            deviceType = TYPE_PHONE;
        }

        return deviceType;
    }
}
