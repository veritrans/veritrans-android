package com.midtrans.sdk.uikit.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.midtrans.sdk.corekit.core.Constants;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.core.PaymentType;
import com.midtrans.sdk.corekit.core.TransactionRequest;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.activities.BCAKlikPayActivity;
import com.midtrans.sdk.uikit.activities.BankTransferActivity;
import com.midtrans.sdk.uikit.activities.BankTransferInstructionActivity;
import com.midtrans.sdk.uikit.activities.CIMBClickPayActivity;
import com.midtrans.sdk.uikit.activities.CreditCardFlowActivity;
import com.midtrans.sdk.uikit.activities.EpayBriActivity;
import com.midtrans.sdk.uikit.activities.GCIActivity;
import com.midtrans.sdk.uikit.activities.IndomaretActivity;
import com.midtrans.sdk.uikit.activities.IndosatDompetkuActivity;
import com.midtrans.sdk.uikit.activities.KiosonActivity;
import com.midtrans.sdk.uikit.activities.KiosonInstructionActivity;
import com.midtrans.sdk.uikit.activities.KlikBCAActivity;
import com.midtrans.sdk.uikit.activities.KlikBCAInstructionActivity;
import com.midtrans.sdk.uikit.activities.MandiriClickPayActivity;
import com.midtrans.sdk.uikit.activities.MandiriClickPayInstructionActivity;
import com.midtrans.sdk.uikit.activities.MandiriECashActivity;
import com.midtrans.sdk.uikit.activities.TelkomselCashActivity;
import com.midtrans.sdk.uikit.activities.XLTunaiActivity;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;
import com.midtrans.sdk.uikit.widgets.FancyButton;

import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Created by shivam on 10/27/15.
 * Deprecated, please use {@link com.midtrans.sdk.uikit.views.status.PaymentStatusActivity}
 */
@Deprecated
public class PaymentTransactionStatusFragment extends Fragment {

    private static final String TRANSACTION_RESPONSE_PARAM = "transaction_response_param";
    private static final int STATUS_SUCCESS = 2;
    private static final int STATUS_PENDING = 1;
    private static final int STATUS_FAILED = 0;
    private static final String PAYMENT_TYPE = "payment_type";
    private static final java.lang.String TAG = PaymentTransactionStatusFragment.class.getSimpleName();
    private TransactionResponse transactionResponse;
    private boolean isSuccessful;

    // views
    private FancyButton actionBt = null;
    private ImageView paymentIv = null;
    private TextView paymentStatusTv = null;
    private TextView paymentMessageTv = null;
    private TextView amountTextView = null;
    private TextView orderIdTextView = null;
    private TextView paymentTypeTextView = null;
    private TextView textBank;
    private TextView textInstallmentTerm;
    private TextView textRedeemedPoint;
    private DefaultTextView textStatusTitle;
    private int count = 1;
    private LinearLayout detailsTable;
    private FrameLayout layoutMain;
    private RelativeLayout layoutDueTotalAmount, layoutInstallment, layoutBank, layoutPaymentType, layoutRedeemedPoint;
    private int mPaymentType = -1;
    private FancyButton buttonInstruction;

    public PaymentTransactionStatusFragment() {
        // Required empty public constructor
    }

    public static PaymentTransactionStatusFragment newInstance(TransactionResponse transactionResponse) {
        Logger.i("payment status get instance called");
        PaymentTransactionStatusFragment fragment = new PaymentTransactionStatusFragment();
        Bundle args = new Bundle();
        args.putSerializable(TRANSACTION_RESPONSE_PARAM, transactionResponse);
        fragment.setArguments(args);
        return fragment;
    }

    public static PaymentTransactionStatusFragment newInstance(TransactionResponse transactionResponse, int paymentType) {
        Logger.i("payment status get instance called");
        PaymentTransactionStatusFragment fragment = new PaymentTransactionStatusFragment();
        Bundle args = new Bundle();
        args.putSerializable(TRANSACTION_RESPONSE_PARAM, transactionResponse);
        args.putInt(PAYMENT_TYPE, paymentType);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        if (args != null) {
            transactionResponse = (TransactionResponse) getArguments().getSerializable(TRANSACTION_RESPONSE_PARAM);
            mPaymentType = args.getInt(PAYMENT_TYPE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fragment_payment_transaction_status, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        initializeViews(view);
        bindDataToView();

    }

    private void initializeViews(View view) {
        amountTextView = (TextView) view.findViewById(R.id.text_status_amount);
        orderIdTextView = (TextView) view.findViewById(R.id.text_order_id);
        paymentTypeTextView = (TextView) view.findViewById(R.id.text_payment_type);
        actionBt = (FancyButton) view.findViewById(R.id.button_primary);
        paymentIv = (ImageView) view.findViewById(R.id.image_payment);
        paymentStatusTv = (TextView) view.findViewById(R.id.text_payment_status);
        paymentMessageTv = (TextView) view.findViewById(R.id.text_payment_message);
        detailsTable = (LinearLayout) view.findViewById(R.id.transaction_info_layout);
        layoutInstallment = (RelativeLayout) view.findViewById(R.id.layout_status_due_installment);
        layoutDueTotalAmount = (RelativeLayout) view.findViewById(R.id.layout_status_due_amount);
        layoutInstallment = (RelativeLayout) view.findViewById(R.id.layout_status_due_installment);
        layoutPaymentType = (RelativeLayout) view.findViewById(R.id.layout_status_payment_type);
        layoutBank = (RelativeLayout) view.findViewById(R.id.layout_status_bank);
        textBank = (TextView) view.findViewById(R.id.text_status_bank);
        textInstallmentTerm = (TextView) view.findViewById(R.id.text_status_due_installment);
        textStatusTitle = (DefaultTextView) view.findViewById(R.id.text_title_payment_status);
        layoutMain = (FrameLayout) view.findViewById(R.id.layout_transaction_status);
        buttonInstruction = (FancyButton) view.findViewById(R.id.btn_see_instruction);
        layoutRedeemedPoint = (RelativeLayout) view.findViewById(R.id.layout_status_point_amount);
        textRedeemedPoint = (TextView) view.findViewById(R.id.text_point_amount);

        // hide chevron image
        view.findViewById(R.id.button_chevron).setVisibility(View.GONE);

        MidtransSDK midtransSDK = MidtransSDK.getInstance();
        if (midtransSDK != null && midtransSDK.getColorTheme() != null) {
            if (midtransSDK.getColorTheme().getPrimaryColor() != 0) {
                // Set background for action button
                actionBt.setBackgroundColor(midtransSDK.getColorTheme().getPrimaryColor());
            }

            buttonInstruction.setTextColor(ContextCompat.getColor(getContext(), R.color.white));
            buttonInstruction.setIconColorFilter(ContextCompat.getColor(getContext(), R.color.white));
        }
    }

    private void setPaymentStatusValues() {
        if (transactionResponse.getStatusCode().equalsIgnoreCase(getString(R.string.success_code_200)) ||
                transactionResponse.getTransactionStatus().equalsIgnoreCase(getString(R.string.capital_success)) ||
                transactionResponse.getTransactionStatus().equalsIgnoreCase(getString(R.string.settlement))) {

            paymentIv.setImageResource(R.drawable.ic_status_success);
            textStatusTitle.setText(getString(R.string.payment_successful));
            paymentStatusTv.setText(getString(R.string.thank_you));
            paymentMessageTv.setVisibility(View.GONE);
            setInstallmentStatus();
            setupStatusBarColor(STATUS_SUCCESS);
            setupStatusInfo();
        } else if (transactionResponse.getStatusCode().equalsIgnoreCase(getString(R.string.success_code_201)) ||
                transactionResponse.getTransactionStatus().equalsIgnoreCase(getString(R.string.pending))) {
            setupStatusBarColor(STATUS_PENDING);
            setupStatusInfo();

            paymentIv.setImageResource(R.drawable.ic_status_pending);
            paymentMessageTv.setVisibility(View.GONE);
            paymentStatusTv.setText(getString(R.string.thank_you));
            if (transactionResponse.getFraudStatus().equalsIgnoreCase(getString(R.string.challenge))) {
                textStatusTitle.setText(getString(R.string.payment_challenged));
            } else {
                textStatusTitle.setText(getString(R.string.payment_pending));
            }
        } else {
            setUiForFailure();
        }

    }

    private void setupStatusInfo() {

        //set payment type
        setPaymentType();

        TransactionRequest request = MidtransSDK.getInstance().getTransactionRequest();

        //set order id
        String orderId = TextUtils.isEmpty(transactionResponse.getOrderId()) ? request.getOrderId() : transactionResponse.getOrderId();
        orderIdTextView.setText(orderId);

        //set total amount
        String amount = TextUtils.isEmpty(transactionResponse.getGrossAmount()) ? String.valueOf(request.getAmount()) : transactionResponse.getGrossAmount();
        try {
            if (!TextUtils.isEmpty(amount)) {
                String formattedAmount = amount.split(Pattern.quote(".")).length == 2 ? amount.split(Pattern.quote("."))[0] : amount;
                amountTextView.setText(formattedAmount);
            }

        } catch (NullPointerException e) {
            Logger.e(TAG, e.getMessage());
        }

        // setbank
        setBankInfo();

        //installment
        if (!TextUtils.isEmpty(transactionResponse.getInstallmentTerm())) {
            textInstallmentTerm.setText(transactionResponse.getInstallmentTerm());
            layoutInstallment.setVisibility(View.VISIBLE);
        } else {
            layoutInstallment.setVisibility(View.GONE);
        }

        // Set point
        if (transactionResponse.getPointRedeemAmount() != 0) {
            String formattedBalance;
            if (transactionResponse.getPointRedeemAmount() == (long) transactionResponse.getPointRedeemAmount()) {
                formattedBalance = String.format(Locale.getDefault(), "%d", (long) transactionResponse.getPointRedeemAmount());
            } else {
                formattedBalance = String.format("%s", transactionResponse.getPointRedeemAmount());
            }
            layoutRedeemedPoint.setVisibility(View.VISIBLE);
            textRedeemedPoint.setText(formattedBalance);
        } else {
            layoutRedeemedPoint.setVisibility(View.GONE);
        }
    }

    private void setBankInfo() {
        if (mPaymentType == Constants.PAYMENT_METHOD_CREDIT_OR_DEBIT) {
            if (!TextUtils.isEmpty(transactionResponse.getBank())) {
                textBank.setText(transactionResponse.getBank());
                layoutBank.setVisibility(View.VISIBLE);
            }
        } else if (mPaymentType == Constants.BANK_TRANSFER_PERMATA
                || mPaymentType == Constants.BANK_TRANSFER_BCA
                || mPaymentType == Constants.PAYMENT_METHOD_MANDIRI_BILL_PAYMENT
                || mPaymentType == Constants.PAYMENT_METHOD_BANK_TRANSFER_ALL_BANK) {

        } else if (mPaymentType == Constants.PAYMENT_METHOD_INDOSAT_DOMPETKU) {
            textBank.setText(getActivity().getResources().getString(R.string
                    .indosat_dompetku));
        } else if (mPaymentType == Constants.PAYMENT_METHOD_TELKOMSEL_CASH) {
            textBank.setText(getString(R.string.payment_method_telkomsel_cash));
        } else if (mPaymentType == Constants.PAYMENT_METHOD_MANDIRI_CLICK_PAY) {
            textBank.setText(getActivity().getResources().getString(R.string
                    .mandiri_click_pay));
        } else if (mPaymentType == Constants.PAYMENT_METHOD_GCI) {
            textBank.setText(getString(R.string.payment_method_gci));
        } else {
            layoutBank.setVisibility(View.GONE);
        }
    }

    private void setupStatusBarColor(int status) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (status == STATUS_SUCCESS) {
                getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), R.color.payment_status_success));
            } else if (status == STATUS_PENDING) {
                getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), R.color.payment_status_pending));
            } else {
                getActivity().getWindow().setStatusBarColor(ContextCompat.getColor(getContext(), R.color.payment_status_failed));
            }
        }

        if (status == STATUS_SUCCESS) {
            layoutMain.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.payment_status_success));
        } else if (status == STATUS_PENDING) {
            layoutMain.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.payment_status_pending));
        } else {
            layoutMain.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.payment_status_failed));
        }
    }

    private void setInstallmentStatus() {
        if (!TextUtils.isEmpty(transactionResponse.getInstallmentTerm())) {
            textBank.setText(transactionResponse.getBank());
            textInstallmentTerm.setText(getString(R.string.installment_months, transactionResponse.getInstallmentTerm()));
            layoutInstallment.setVisibility(View.VISIBLE);
        }
    }

    private void bindDataToView() {

        if (transactionResponse != null) {
            try {
                Logger.i("transactionstatus:" + transactionResponse.getString());
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            setPaymentStatusValues();
        } else {
            setUiForFailure();
        }

        buttonInstruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showInstruction();
            }
        });

        actionBt.setText(getString(R.string.done));
        actionBt.setTextBold();

        actionBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("statusactivity", "from:" + getActivity().getClass().getName());
                if (getActivity() instanceof CreditCardFlowActivity) {
                    ((CreditCardFlowActivity) getActivity()).setResultCode(Activity.RESULT_OK);
                    ((CreditCardFlowActivity) getActivity()).setResultAndFinish();
                } else if (getActivity() instanceof EpayBriActivity) {
                    ((EpayBriActivity) getActivity()).setResultCode(Activity.RESULT_OK);
                    ((EpayBriActivity) getActivity()).setResultAndFinish();
                } else if (getActivity() instanceof CIMBClickPayActivity) {
                    ((CIMBClickPayActivity) getActivity())
                            .setResultCode(Activity.RESULT_OK);
                    ((CIMBClickPayActivity) getActivity()).setResultAndFinish();
                } else if (getActivity() instanceof MandiriECashActivity) {
                    ((MandiriECashActivity) getActivity()).setResultCode(Activity.RESULT_OK);
                    ((MandiriECashActivity) getActivity()).setResultAndFinish();
                } else if (getActivity() instanceof BCAKlikPayActivity) {
                    ((BCAKlikPayActivity) getActivity()).setResultCode(Activity.RESULT_OK);
                    ((BCAKlikPayActivity) getActivity()).setResultAndFinish();
                } else if (getActivity() instanceof BankTransferActivity) {
                    ((BankTransferActivity) getActivity()).setResultCode(Activity.RESULT_OK);
                    getActivity().onBackPressed();
                } else if (getActivity() instanceof KlikBCAActivity) {
                    ((KlikBCAActivity) getActivity()).setResultCode(Activity.RESULT_OK);
                    getActivity().onBackPressed();
                } else if (getActivity() instanceof TelkomselCashActivity) {
                    ((TelkomselCashActivity) getActivity()).setResultCode(Activity.RESULT_OK);
                    getActivity().onBackPressed();
                } else if (getActivity() instanceof IndosatDompetkuActivity) {
                    ((IndosatDompetkuActivity) getActivity()).setResultCode(Activity.RESULT_OK);
                    getActivity().onBackPressed();
                } else if (getActivity() instanceof XLTunaiActivity) {
                    ((XLTunaiActivity) getActivity()).setResultCode(Activity.RESULT_OK);
                    getActivity().onBackPressed();
                } else if (getActivity() instanceof KiosonActivity) {
                    ((KiosonActivity) getActivity()).setResultCode(Activity.RESULT_OK);
                    getActivity().onBackPressed();
                } else if (getActivity() instanceof IndomaretActivity) {
                    ((IndomaretActivity) getActivity()).setResultCode(Activity.RESULT_OK);
                    getActivity().onBackPressed();
                } else if (getActivity() instanceof MandiriClickPayActivity) {
                    ((MandiriClickPayActivity) getActivity()).setResultCode(Activity.RESULT_OK);
                    getActivity().onBackPressed();
                } else if (getActivity() instanceof GCIActivity) {
                    ((GCIActivity) getActivity()).setResultCode(Activity.RESULT_OK);
                    getActivity().onBackPressed();
                }
            }

        });
    }

    private void setUiForFailure() {
        isSuccessful = false;
        paymentIv.setImageResource(R.drawable.ic_status_failed);
        paymentStatusTv.setText(getString(R.string.sorry));
        textStatusTitle.setText(getString(R.string.payment_unsuccessful));
        setupStatusBarColor(STATUS_FAILED);
        setupStatusInfo();
        buttonInstruction.setVisibility(View.GONE);

        if (transactionResponse == null) {
            paymentMessageTv.setVisibility(View.VISIBLE);
            paymentMessageTv.setText(getString(R.string.api_fail_message));
            detailsTable.setVisibility(View.GONE);
            return;
        }

        try {
            Logger.i("fail_message" + transactionResponse.getStatusMessage());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (transactionResponse.getTransactionStatus().equalsIgnoreCase(getString(R.string.deny))) {
            paymentMessageTv.setVisibility(View.VISIBLE);
            paymentMessageTv.setText(getString(R.string.payment_deny));
        } else if (transactionResponse.getStatusCode().equalsIgnoreCase(getString(R.string.failed_code_400))) {
            paymentMessageTv.setVisibility(View.VISIBLE);
            String message = "";
            if (transactionResponse.getValidationMessages() != null && !transactionResponse.getValidationMessages().isEmpty()) {
                message = transactionResponse.getValidationMessages().get(0);
            }

            if (!TextUtils.isEmpty(message) && message.toLowerCase().contains(getString(R.string.label_expired))) {
                paymentMessageTv.setText(getString(R.string.message_payment_expired));
            } else {
                paymentMessageTv.setText(getString(R.string.message_cannot_proccessed));
            }

        } else {
            if (!TextUtils.isEmpty(transactionResponse.getStatusMessage())) {
                paymentMessageTv.setVisibility(View.VISIBLE);
                paymentMessageTv.setText(getString(R.string.message_payment_failed));
            } else {
                paymentMessageTv.setVisibility(View.GONE);
            }
        }
    }

    private void setPaymentType() {
        try {
            Logger.i("PaymentType:" + transactionResponse.getPaymentType());
        } catch (NullPointerException e) {

        }
        if (transactionResponse == null) {
            return;
        }

        if (transactionResponse.getPaymentType().equals(PaymentType.BRI_EPAY)) {
            paymentTypeTextView.setText(R.string.epay_bri);
        } else if (transactionResponse.getPaymentType().equals(PaymentType.E_CHANNEL)) {
            paymentTypeTextView.setText(R.string.mandiri_bill_payment);
        } else if (transactionResponse.getPaymentType().equals(PaymentType.CIMB_CLICKS)) {
            paymentTypeTextView.setText(R.string.cimb_clicks);
        } else if (transactionResponse.getPaymentType().equals(PaymentType.MANDIRI_ECASH)) {
            paymentTypeTextView.setText(R.string.mandiri_e_cash);
            buttonInstruction.setVisibility(View.GONE);
        } else if (transactionResponse.getPaymentType().equals(PaymentType.MANDIRI_CLICKPAY)) {
            paymentTypeTextView.setText(R.string.mandiri_click_pay);
        } else if (transactionResponse.getPaymentType().equals(PaymentType.CREDIT_CARD)) {
            paymentTypeTextView.setText(R.string.credit_card);
            buttonInstruction.setVisibility(View.GONE);
        } else if (transactionResponse.getPaymentType().equals(PaymentType.INDOSAT_DOMPETKU)) {
            paymentTypeTextView.setText(R.string.indosat_dompetku);
            buttonInstruction.setVisibility(View.GONE);
        } else if (transactionResponse.getPaymentType().equals(PaymentType.TELKOMSEL_CASH)) {
            paymentTypeTextView.setText(R.string.telkomsel_cash);
            buttonInstruction.setVisibility(View.GONE);
        } else if (transactionResponse.getPaymentType().equals(PaymentType.XL_TUNAI)) {
            paymentTypeTextView.setText(R.string.xl_tunai);
            buttonInstruction.setVisibility(View.GONE);
        } else if (transactionResponse.getPaymentType().equals(PaymentType.BCA_KLIKPAY)) {
            paymentTypeTextView.setText(getString(R.string.payment_method_bca_klikpay));
        } else if (transactionResponse.getPaymentType().equals(PaymentType.KLIK_BCA)) {
            paymentTypeTextView.setText(getString(R.string.payment_method_klik_bca));
        } else if (transactionResponse.getPaymentType().equals(getString(R.string.cstore_payment))) {

            if (mPaymentType == Constants.PAYMENT_METHOD_INDOMARET) {
                paymentTypeTextView.setText(getString(R.string.indomaret));
                buttonInstruction.setVisibility(View.GONE);
            } else if (mPaymentType == Constants.PAYMENT_METHOD_KIOSON) {
                paymentTypeTextView.setText(getString(R.string.payment_method_kioson));
            }

        } else if (transactionResponse.getPaymentType().equals(PaymentType.GCI)) {
            paymentTypeTextView.setText(getString(R.string.payment_method_gci));
            buttonInstruction.setVisibility(View.GONE);
        } else if (transactionResponse.getPaymentType().equals(getString(R.string.payment_bank_transfer)) ||
                transactionResponse.getPaymentType().equals(PaymentType.BCA_VA) ||
                transactionResponse.getPaymentType().equals(PaymentType.PERMATA_VA) ||
                transactionResponse.getPaymentType().equals(PaymentType.ALL_VA)) {
            paymentTypeTextView.setText(getString(R.string.bank_transfer));
        } else {
            buttonInstruction.setVisibility(View.GONE);
            layoutPaymentType.setVisibility(View.GONE);
            layoutBank.setVisibility(View.GONE);
        }
    }

    private void showInstruction() {
        if (mPaymentType != -1) {
            Intent intent = null;
            switch (mPaymentType) {
                case Constants.PAYMENT_METHOD_MANDIRI_CLICK_PAY:
                    intent = new Intent(getActivity(), MandiriClickPayInstructionActivity.class);
                    break;
                case Constants.PAYMENT_METHOD_MANDIRI_BILL_PAYMENT:
                    intent = new Intent(getActivity(), BankTransferInstructionActivity.class);
                    intent.putExtra(BankTransferInstructionActivity.BANK, BankTransferInstructionActivity.TYPE_MANDIRI_BILL);
                    break;
                case Constants.BANK_TRANSFER_PERMATA:
                    intent = new Intent(getActivity(), BankTransferInstructionActivity.class);
                    intent.putExtra(BankTransferInstructionActivity.BANK, BankTransferInstructionActivity.TYPE_PERMATA);
                    break;
                case Constants.PAYMENT_METHOD_PERMATA_VA_BANK_TRANSFER:
                    intent = new Intent(getActivity(), BankTransferInstructionActivity.class);
                    intent.putExtra(BankTransferInstructionActivity.BANK, BankTransferInstructionActivity.TYPE_PERMATA);
                    break;
                case Constants.BANK_TRANSFER_BCA:
                    intent = new Intent(getActivity(), BankTransferInstructionActivity.class);
                    intent.putExtra(BankTransferInstructionActivity.BANK, BankTransferInstructionActivity.TYPE_BCA);
                    break;
                case Constants.PAYMENT_METHOD_BANK_TRANSFER_ALL_BANK:
                    intent = new Intent(getActivity(), BankTransferInstructionActivity.class);
                    intent.putExtra(BankTransferInstructionActivity.BANK, BankTransferInstructionActivity.TYPE_ALL_BANK);
                    break;
                case Constants.PAYMENT_METHOD_KLIKBCA:
                    intent = new Intent(getActivity(), KlikBCAInstructionActivity.class);
                    break;
                case Constants.PAYMENT_METHOD_KIOSON:
                    intent = new Intent(getActivity(), KiosonInstructionActivity.class);
                    break;
            }

            if (transactionResponse != null && !TextUtils.isEmpty(transactionResponse.getPdfUrl())) {
                if (intent != null) {
                    intent.putExtra(BankTransferInstructionActivity.DOWNLOAD_URL, transactionResponse.getPdfUrl());
                }
            }
            getActivity().startActivity(intent);
            if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                    && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                getActivity().overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
            }
        }
    }

}