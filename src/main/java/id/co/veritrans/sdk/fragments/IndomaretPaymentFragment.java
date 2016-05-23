package id.co.veritrans.sdk.fragments;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.core.VeritransSDK;
import id.co.veritrans.sdk.models.TransactionResponse;
import id.co.veritrans.sdk.utilities.Utils;

/**
 * Displays status information about bank transfer's api call .
 *
 * Created by shivam on 10/27/15.
 */
public class IndomaretPaymentFragment extends Fragment {

    public static final String KEY_ARG = "arg";
    public static final String VALID_UNTIL = "Valid Until : ";
    private static final String LABEL_PAYMENT_CODE = "Payment Code";
    private TransactionResponse transactionResponse;

    //views

    private TextView mTextViewValidity = null;
    private TextView mTextViewPaymentCode = null;
    private TextView mTextViewCopyToClipboard = null;


    public static IndomaretPaymentFragment newInstance(TransactionResponse mTransactionResponse) {
        IndomaretPaymentFragment fragment = new IndomaretPaymentFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_ARG, mTransactionResponse);
        fragment.setArguments(bundle);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_indomaret_transfer_payment, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        // Get data from argument
        if (getArguments() != null && getArguments().getSerializable(KEY_ARG) != null) {
            transactionResponse = (TransactionResponse) getArguments().getSerializable(KEY_ARG);
        }

        initializeViews(view);
    }


    /**
     * initializes view and adds click listener for it.
     *
     * @param view  view that needed to be initialized
     */
    private void initializeViews(View view) {
        mTextViewValidity = (TextView) view.findViewById(R.id.text_validaty);
        mTextViewPaymentCode = (TextView) view.findViewById(R.id.text_payment_code);
        mTextViewCopyToClipboard = (TextView) view.findViewById(R.id.text_copy_va);

        if (transactionResponse != null) {
            if (transactionResponse.getStatusCode().trim().equalsIgnoreCase(getString(R.string.success_code_200))
                    || transactionResponse.getStatusCode().trim().equalsIgnoreCase(getString(R.string.success_code_201))) {
                mTextViewValidity.setText(VALID_UNTIL + Utils.getValidityTime(transactionResponse.getTransactionTime()));
            }
            if (transactionResponse.getPaymentCodeIndomaret() != null)
                mTextViewPaymentCode.setText(transactionResponse.getPaymentCodeIndomaret());

        }

        VeritransSDK veritransSDK = VeritransSDK.getVeritransSDK();
        if (veritransSDK != null) {
            mTextViewCopyToClipboard.setTextColor(veritransSDK.getThemeColor());
        }
        mTextViewCopyToClipboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyPaymentCode();
            }
        });
    }

    /**
     * Copy payment code into clipboard.
     */
    private void copyPaymentCode() {
        ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText(LABEL_PAYMENT_CODE, mTextViewPaymentCode.getText().toString());
        clipboard.setPrimaryClip(clip);
        // Show toast
        Toast.makeText(getContext(), R.string.copied_to_clipboard, Toast.LENGTH_SHORT).show();
    }
}