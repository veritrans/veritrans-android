package id.co.veritrans.sdk.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.core.SdkUtil;
import id.co.veritrans.sdk.widgets.TextViewFont;

/**
 * Created by shivam on 10/27/15.
 */
public class PaymentTransactionStatusFragment extends Fragment {

    private static final String PARAM = "param";
    private boolean isSuccessful;
    private Button actionBt;
    private ImageView paymentIv;
    private TextViewFont paymentStatusTv;
    private TextViewFont paymentMessageTv;

    public static PaymentTransactionStatusFragment newInstance(boolean isSuccessful) {
        PaymentTransactionStatusFragment fragment = new PaymentTransactionStatusFragment();
        Bundle args = new Bundle();
        args.putBoolean(PARAM, isSuccessful);

        fragment.setArguments(args);
        return fragment;
    }

    public PaymentTransactionStatusFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            isSuccessful = getArguments().getBoolean(PARAM);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_payment_transaction_status, container, false);
        actionBt = (Button) view.findViewById(R.id.btn_action);
        paymentIv = (ImageView) view.findViewById(R.id.image_payment);
        paymentStatusTv = (TextViewFont) view.findViewById(R.id.text_payment_status);
        paymentMessageTv = (TextViewFont) view.findViewById(R.id.text_payment_message);
        if (isSuccessful) {
            actionBt.setText(getString(R.string.done));
            paymentIv.setImageResource(R.drawable.ic_successful);
            paymentStatusTv.setText(getString(R.string.payment_successful));
            paymentMessageTv.setVisibility(View.GONE);
        } else {
            actionBt.setText(getString(R.string.retry));
            paymentIv.setImageResource(R.drawable.ic_failure);
            paymentStatusTv.setText(getString(R.string.payment_unsuccessful));
            paymentMessageTv.setVisibility(View.VISIBLE);
        }
        actionBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isSuccessful) {
                    getActivity().finish();
                } else {
                    SdkUtil.showSnackbar(getActivity(), getString(R.string.coming_soon));
                }
            }
        });
        return view;
    }

}
