package com.midtrans.sdk.uikit.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.utilities.Utils;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.activities.CreditDebitCardFlowActivity;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;
import com.midtrans.sdk.uikit.widgets.FancyButton;

/**
 * Created by ziahaqi on 1/16/17.
 */

public class BanksPointFragment extends Fragment implements View.OnClickListener {

    private static final String ARGS_POINT = "point_balance";
    private static final String ARGS_BANK = "point_bank";
    private DefaultTextView textPointConverted, textAmountToPay, textPointUsed, textTotalPoint;
    private FancyButton buttonIncrease, buttonDecrease;
    private Button buttonRedeemPoint;
    private long pointBalance;
    private double pointConverted;
    private double amountToPay;
    private long pointWillBeUsed;
    private ImageView imagePlus, imageMinus;
    private String pointBank;

    public static BanksPointFragment newInstance(long balance, String bank) {
        BanksPointFragment fragment = new BanksPointFragment();
        Bundle bundle = new Bundle();
        bundle.putLong(ARGS_POINT, balance);
        bundle.putString(ARGS_BANK, bank);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initValues();
        return inflater.inflate(R.layout.fragment_banks_points, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((CreditDebitCardFlowActivity) getActivity()).getTitleHeaderTextView().setText(getString(R.string.redeem_bank_point_title, this.pointBank));

        textPointConverted = (DefaultTextView) view.findViewById(R.id.text_point_converted);
        textAmountToPay = (DefaultTextView) view.findViewById(R.id.text_amount_to_pay);
        textPointUsed = (DefaultTextView) view.findViewById(R.id.text_point_used);
        textTotalPoint = (DefaultTextView) view.findViewById(R.id.text_total_point);
        imageMinus = (ImageView) view.findViewById(R.id.image_point_min);
        imagePlus = (ImageView) view.findViewById(R.id.image_point_plus);
        buttonIncrease = (FancyButton) view.findViewById(R.id.button_point_increase);
        buttonDecrease = (FancyButton) view.findViewById(R.id.button_point_decrease);
        buttonRedeemPoint = (Button) view.findViewById(R.id.btn_redeem_point);

        setupDefaultView();
        bindValues();
        buttonDecrease.setOnClickListener(this);
        buttonIncrease.setOnClickListener(this);
        buttonRedeemPoint.setOnClickListener(this);
    }

    private void setupDefaultView() {
        try {
            int imageFilter = SdkUIFlowUtil.getThemeColor(getContext(), R.attr.colorPrimary);
            imagePlus.setImageDrawable(SdkUIFlowUtil.filterDrawableImage(getContext(), R.drawable.ic_plus_new, imageFilter));
            imageMinus.setImageDrawable(SdkUIFlowUtil.filterDrawableImage(getContext(), R.drawable.ic_minus_new, imageFilter));

        } catch (Exception e) {

        }
    }

    private void bindValues() {
        textAmountToPay.setText(Utils.getFormattedAmount(amountToPay));
        textPointConverted.setText(Utils.getFormattedAmount(pointConverted));
        textPointUsed.setText(String.valueOf(pointWillBeUsed));
        textTotalPoint.setText(getString(R.string.total_point, String.valueOf(pointBalance)));
    }

    private void initValues() {
        this.amountToPay = MidtransSDK.getInstance().getTransactionRequest().getAmount();
        this.pointConverted = 0;
        this.pointWillBeUsed = 0;

        Bundle bundle = getArguments();
        if (bundle != null) {
            this.pointBalance = bundle.getLong(ARGS_POINT);
            this.pointBank = bundle.getString(ARGS_BANK);
        }
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.button_point_increase) {

        } else if (viewId == R.id.button_point_decrease) {

        } else if (viewId == R.id.btn_redeem_point) {
            String strPoint = textPointUsed.getText().toString().trim();
            long redeemedPoint = -1;
            try {
                redeemedPoint = Long.valueOf(strPoint);
            }catch (Exception e){

            }
            ((CreditDebitCardFlowActivity)getActivity()).payWithBankPoint(redeemedPoint);
        }
    }
}
