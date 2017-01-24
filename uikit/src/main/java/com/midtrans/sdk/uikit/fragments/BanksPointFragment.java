package com.midtrans.sdk.uikit.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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
    private static final long MULTPIPLY = 10000;
    private final String TAG = getClass().getSimpleName();
    private DefaultTextView textPointConverted, textAmountToPay, textTotalPoint;
    private EditText editPointRedeemed;
    private FancyButton buttonIncrease, buttonDecrease;
    private Button buttonRedeemPoint;
    private long pointBalance;
    private double pointConverted;
    private double amountToPay;
    private long pointRedeemed;
    private ImageView imagePlus, imageMinus;
    private String pointBank;
    private double totalAmount;
    private boolean inputPointFromButtons;
    private String latestValidPoint;

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
        editPointRedeemed = (EditText) view.findViewById(R.id.text_point_used);
        textTotalPoint = (DefaultTextView) view.findViewById(R.id.text_total_point);
        imageMinus = (ImageView) view.findViewById(R.id.image_point_min);
        imagePlus = (ImageView) view.findViewById(R.id.image_point_plus);
        buttonIncrease = (FancyButton) view.findViewById(R.id.button_point_increase);
        buttonDecrease = (FancyButton) view.findViewById(R.id.button_point_decrease);
        buttonRedeemPoint = (Button) view.findViewById(R.id.btn_redeem_point);

        setupDefaultView();
        bindValues();
        updateViews();
        buttonDecrease.setOnClickListener(this);
        buttonIncrease.setOnClickListener(this);
        buttonRedeemPoint.setOnClickListener(this);

        editPointRedeemed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (inputPointFromButtons) {
                    return;
                }

                String inputString = editable.toString();
                if (editable.length() == 0) {
                    editable.insert(0, "0");
                } else if (inputString.length() > 1 && inputString.charAt(0) == '0') {
                    editable.delete(0, 1);
                }

                if (isValidInputPoint(inputString)) {
                    latestValidPoint = inputString;
                } else {
                    editPointRedeemed.setText(latestValidPoint);
                }
                updateViews();
            }
        });
    }

    public boolean isValidInputPoint(String inputString) {
        long currentBalance = 0;
        try {
            currentBalance = Long.parseLong(inputString);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }

        if (currentBalance >= 0 && currentBalance <= pointBalance) {
            calculateAmount(currentBalance);
            return true;
        }
        return false;
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
        textAmountToPay.setText(getString(R.string.prefix_money, Utils.getFormattedAmount(amountToPay)));
        textPointConverted.setText(getString(R.string.prefix_money, Utils.getFormattedAmount(pointConverted)));
        editPointRedeemed.setText(String.valueOf(pointRedeemed));
        textTotalPoint.setText(getString(R.string.total_point, String.valueOf(pointBalance)));
    }

    private void initValues() {
        double totalAmount = MidtransSDK.getInstance().getTransactionRequest().getAmount();
        this.amountToPay = totalAmount;
        this.totalAmount = totalAmount;
        this.pointConverted = 0;
        this.pointRedeemed = 0;

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
            onIncreasePoint();
        } else if (viewId == R.id.button_point_decrease) {
            onDecrasePoint();
        } else if (viewId == R.id.btn_redeem_point) {
            String strPoint = editPointRedeemed.getText().toString().trim();
            long redeemedPoint = -1;
            try {
                redeemedPoint = Long.valueOf(strPoint);
            } catch (Exception e) {

            }
            ((CreditDebitCardFlowActivity) getActivity()).payWithBankPoint(redeemedPoint);
        }
    }

    private void updatePointButtonStatus() {
        boolean enableIncrease = true;
        boolean enableDecrease = true;

        if (pointRedeemed < 0 || (pointRedeemed - MULTPIPLY) < 0) {
            enableDecrease = false;
        }

        if (pointRedeemed > pointBalance || (pointRedeemed + MULTPIPLY) > pointBalance) {
            enableIncrease = false;
        }
        setEnablePointButtons(enableDecrease, enableIncrease);
    }

    private void setEnablePointButtons(boolean enableDecreaseButton, boolean enableIncreaseButton) {
        if (enableDecreaseButton) {
            buttonDecrease.setEnabled(true);
        } else {
            buttonDecrease.setEnabled(false);
        }

        if (enableIncreaseButton) {
            buttonIncrease.setEnabled(true);
        } else {
            buttonIncrease.setEnabled(false);
        }
    }

    private void onDecrasePoint() {
        inputPointFromButtons = true;
        long currentBalance = getCurrentPoint();
        long newBalance = currentBalance - MULTPIPLY;
        if (isValidCurrentBalance(currentBalance) && newBalance >= 0) {
            calculateAmount(newBalance);
        }

        updateViews();
        inputPointFromButtons = false;
    }

    private void onIncreasePoint() {
        inputPointFromButtons = true;
        long currentBalance = getCurrentPoint();
        long newBalance = currentBalance + MULTPIPLY;

        if (isValidCurrentBalance(currentBalance) && newBalance <= pointBalance) {
            calculateAmount(newBalance);
        }

        updateViews();
        inputPointFromButtons = false;
    }

    private boolean isValidCurrentBalance(long currentBalance) {
        if (pointBalance >= MULTPIPLY && currentBalance <= this.pointBalance) {
            return true;
        }
        return false;
    }

    private void updateViews() {
        if (inputPointFromButtons) {
            editPointRedeemed.setText(String.valueOf(pointRedeemed));
        }

        textAmountToPay.setText(getString(R.string.prefix_money, Utils.getFormattedAmount(amountToPay)));
        textPointConverted.setText(getString(R.string.prefix_money, Utils.getFormattedAmount(pointConverted)));

        updatePointButtonStatus();
    }

    private void calculateAmount(long currentBalance) {
        double pointAmount;
        if (currentBalance == 0) {
            pointAmount = currentBalance;
        } else {
            pointAmount = currentBalance / 100;
        }

        this.pointRedeemed = currentBalance;
        this.pointConverted = pointAmount;
        this.amountToPay = totalAmount - pointConverted;
    }

    private long getCurrentPoint() {
        long currentBalance = 0;
        String strCurrentBalance = editPointRedeemed.getText().toString().trim();
        try {
            currentBalance = Long.parseLong(strCurrentBalance);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
        return currentBalance;
    }

}
