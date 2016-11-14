package com.midtrans.sdk.uikit.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.midtrans.sdk.corekit.core.Constants;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.activities.KiosonActivity;

import java.util.regex.Pattern;

/**
 * Created by ziahaqi on 8/26/16.
 */
public class KiosonPaymentStatusFragment extends Fragment {
    private static final String KIOSON = "Kioson";
    private static final String DATA = "data";
    private static final String IS_FROM_KIOSON = "kioson";
    private TransactionResponse transactionResponse = null;
    // Views
    private TextView mTextViewAmount = null;
    private TextView mTextViewOrderId = null;
    private TextView mTextViewTransactionTime = null;
    private TextView mTextViewBankName = null;
    private TextView mTextViewTransactionStatus = null;
    private TextView mTextViewTitleTransactionStatus = null;
    private TextView mTextViewPaymentErrorMessage = null;
    private ImageView mImageViewTransactionStatus = null;
    private NestedScrollView layoutMain;
    private RelativeLayout layoutPaymentType;
    private RelativeLayout layoutTransactionTime;
    private boolean isFromKioson = false;

    public static KiosonPaymentStatusFragment newInstance(TransactionResponse transactionResponse, boolean isFromKioson) {
        KiosonPaymentStatusFragment fragment = new KiosonPaymentStatusFragment();
        Bundle data = new Bundle();
        data.putSerializable(DATA, transactionResponse);
        data.putBoolean(IS_FROM_KIOSON, isFromKioson);
        fragment.setArguments(data);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_kioson_payment_status, container, false);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        initializeViews(view);

        //retrieve data from bundle.
        Bundle data = getArguments();
        transactionResponse = (TransactionResponse) data.getSerializable(DATA);
        isFromKioson = data.getBoolean(IS_FROM_KIOSON);
        initializeDataToView();
    }

    /**
     * initializes view and adds click listener for it.
     *
     * @param view view that needed to be initialized
     */
    private void initializeViews(View view) {

        mTextViewAmount = (TextView) view.findViewById(R.id.text_amount);
        mTextViewOrderId = (TextView) view.findViewById(R.id.text_order_id);
        mTextViewBankName = (TextView) view.findViewById(R.id.text_payment_type);
        mTextViewTransactionTime = (TextView) view.findViewById(R.id.text_transaction_time);

        mImageViewTransactionStatus = (ImageView) view.findViewById(R.id.img_transaction_status);
        mTextViewTransactionStatus = (TextView) view.findViewById(R.id
                .text_transaction_status);
        mTextViewTitleTransactionStatus = (TextView) view.findViewById(R.id.text_title_payment_status);
        mTextViewPaymentErrorMessage = (TextView) view.findViewById(R.id
                .text_payment_error_message);

        layoutMain = (NestedScrollView) view.findViewById(R.id.layout_kioson_status);
        layoutPaymentType = (RelativeLayout) view.findViewById(R.id.layout_trans_payment_type);
        layoutTransactionTime = (RelativeLayout) view.findViewById(R.id.layout_trans_payment_time);

    }


    /**
     * apply data to view
     */
    private void initializeDataToView() {

        if (transactionResponse != null) {

            if (getActivity() != null) {

                if (!isFromKioson) {
                    if (((KiosonActivity) getActivity()).getPosition()
                            == Constants.PAYMENT_METHOD_KIOSON) {
                        mTextViewBankName.setText(KIOSON);
                    }
                } else {
                    mTextViewBankName.setText(getActivity().getResources().getString(R.string.kioson));
                }
            }

            if (TextUtils.isEmpty(transactionResponse.getTransactionTime())) {
                layoutTransactionTime.setVisibility(View.GONE);
            } else {
                mTextViewTransactionTime.setText(transactionResponse.getTransactionTime());
            }

            String orderId = transactionResponse.getOrderId() == null ?
                    MidtransSDK.getInstance().getTransactionRequest().getOrderId() : transactionResponse.getOrderId();
            mTextViewOrderId.setText(orderId);

            try {
                String amount = TextUtils.isEmpty(transactionResponse.getGrossAmount()) ?
                        MidtransSDK.getInstance().getTransactionRequest().getAmount() + "" : transactionResponse.getGrossAmount();
                if (!TextUtils.isEmpty(amount)) {
                    String formattedAmount = amount.split(Pattern.quote(".")).length == 2 ? amount.split(Pattern.quote("."))[0] : amount;
                    mTextViewAmount.setText(formattedAmount);
                }
            } catch (NullPointerException e) {
                Log.e("BankTransactionStatus", e.getMessage());
            }

            //noinspection StatementWithEmptyBody
            if (transactionResponse.getTransactionStatus().contains("Pending") ||
                    transactionResponse.getTransactionStatus().contains("pending")) {

            } else if (transactionResponse.getStatusCode().equalsIgnoreCase(getString(R.string.success_code_200))
                    || transactionResponse.getStatusCode().equalsIgnoreCase(getString(R.string.success_code_201))) {
                setUiForSuccess();
            } else {

                setUiForFailure();

                if (transactionResponse.getTransactionStatus().equalsIgnoreCase("deny")) {
                    mTextViewTransactionStatus.setText(getString(R.string.status_denied));
                } else if (transactionResponse.getStatusCode().equals(getString(R.string.failed_code_400))) {
                    String message = "";
                    if (transactionResponse.getValidationMessages() != null && !transactionResponse.getValidationMessages().isEmpty()) {
                        message = transactionResponse.getValidationMessages().get(0);
                    }

                    if (!TextUtils.isEmpty(message) && message.toLowerCase().contains(getString(R.string.label_expired))) {
                        mTextViewPaymentErrorMessage.setText(getString(R.string.message_payment_expired));
                        mTextViewTransactionStatus.setText(getString(R.string.status_expired));
                    } else {
                        mTextViewTransactionStatus.setText(getString(R.string.payment_unsuccessful));
                        mTextViewPaymentErrorMessage.setText(getString(R.string.message_cannot_proccessed));
                    }
                }

                if (getActivity() != null) {

                    if (isFromKioson) {
                        // change name of button to 'RETRY'
                        ((KiosonActivity) getActivity()).activateRetry();
                    }
                }
            }
        }
    }

    /**
     * enables ui related to failure of payment transaction.
     */
    private void setUiForFailure() {
        mImageViewTransactionStatus.setImageResource(R.drawable.ic_status_failed);
        mTextViewTransactionStatus.setText(getString(R.string.payment_unsuccessful));
        mTextViewTitleTransactionStatus.setText(getString(R.string.sorry));
        mTextViewPaymentErrorMessage.setVisibility(View.VISIBLE);
        layoutMain.setBackgroundColor(getResources().getColor(R.color.payment_status_failed));
    }

    /**
     * enables ui related to success of payment transaction.
     */
    private void setUiForSuccess() {
        mImageViewTransactionStatus.setImageResource(R.drawable.ic_status_success);
        mTextViewTitleTransactionStatus.setText(getString(R.string.payment_successful));
        mTextViewTransactionStatus.setText(getString(R.string.thank_you));
        mTextViewPaymentErrorMessage.setVisibility(View.GONE);
        layoutMain.setBackgroundColor(getResources().getColor(R.color.payment_status_success));
    }
}
