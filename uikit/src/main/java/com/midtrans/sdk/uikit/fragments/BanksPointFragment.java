package com.midtrans.sdk.uikit.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.BankType;
import com.midtrans.sdk.corekit.utilities.Utils;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.activities.CreditCardFlowActivity;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;
import com.midtrans.sdk.uikit.widgets.FancyButton;
import java.util.Locale;
import java.util.TimerTask;

/**
 * Created by ziahaqi on 1/16/17.
 */
@Deprecated
public class BanksPointFragment extends Fragment implements View.OnClickListener {

    private static final String ARGS_POINT = "point_balance";
    private static final String ARGS_BANK = "point_bank";
    private static final long MULTIPLY = 100;
    private final String TAG = getClass().getSimpleName();
    private DefaultTextView textAmountToPay, textTotalPoint;
    private EditText editPointRedeemed;
    private FancyButton buttonIncrease, buttonDecrease;
    private FancyButton buttonRedeemPoint;
    private FancyButton containerAmount, containerTotalPoint;
    private float pointBalance;
    private double amountToPay;
    private float pointRedeemed;
    private ImageView imagePlus, imageMinus;
    private String pointBank;
    private double totalAmount;
    private boolean inputPointFromButtons;
    private String latestValidPoint;

    public static BanksPointFragment newInstance(float balance, String bank) {
        BanksPointFragment fragment = new BanksPointFragment();
        Bundle bundle = new Bundle();
        bundle.putFloat(ARGS_POINT, balance);
        bundle.putString(ARGS_BANK, bank);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initValues();
        return inflater.inflate(R.layout.fragment_banks_point, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        switch (pointBank) {
            case BankType.BNI:
                ((CreditCardFlowActivity) getActivity()).getTitleHeaderTextView().setText(getString(R.string.redeem_bank_point_title, getString(R.string.bank_bni)));
                break;
        }

        textAmountToPay = (DefaultTextView) view.findViewById(R.id.text_amount_to_pay);
        editPointRedeemed = (EditText) view.findViewById(R.id.redeemed_point_field);
        textTotalPoint = (DefaultTextView) view.findViewById(R.id.text_total_point);
        imageMinus = (ImageView) view.findViewById(R.id.image_point_min);
        imagePlus = (ImageView) view.findViewById(R.id.image_point_plus);
        buttonIncrease = (FancyButton) view.findViewById(R.id.button_point_increase);
        buttonDecrease = (FancyButton) view.findViewById(R.id.button_point_decrease);
        buttonRedeemPoint = (FancyButton) view.findViewById(R.id.btn_redeem_point);
        containerAmount = (FancyButton) view.findViewById(R.id.container_amount);
        containerTotalPoint = (FancyButton) view.findViewById(R.id.container_total_point);

        setupDefaultView();
        bindValues();
        updateViews();
        initThemes();
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
                    editPointRedeemed.setSelection(editPointRedeemed.getText().length());
                }
                updateViews();
            }
        });

        new Handler().postDelayed(new TimerTask() {
            @Override
            public void run() {
                // Request focus for edit text
                editPointRedeemed.requestFocus();
                editPointRedeemed.setSelection(editPointRedeemed.getText().toString().length());
                InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editPointRedeemed, InputMethodManager.SHOW_IMPLICIT);
            }
        }, 500);
    }

    private void initThemes() {
        try {
            MidtransSDK midtransSDK = MidtransSDK.getInstance();
            if (midtransSDK != null && midtransSDK.getColorTheme() != null) {
                if (midtransSDK.getColorTheme().getPrimaryColor() != 0) {
                    buttonRedeemPoint.setBackgroundColor(midtransSDK.getColorTheme().getPrimaryColor());
                }

                if (midtransSDK.getColorTheme().getSecondaryColor() != 0) {
                    containerAmount.setBackgroundColor(midtransSDK.getColorTheme().getSecondaryColor());
                    containerAmount.setAlpha(0.5f);
                    containerTotalPoint.setBackgroundColor(midtransSDK.getColorTheme().getSecondaryColor());
                    containerTotalPoint.setAlpha(0.5f);
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "theme:" + e.getMessage());
        }
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
        String formattedBalance;
        if (pointBalance == (long) pointBalance) {
            formattedBalance = String.format(Locale.getDefault(), "%d", (long) pointBalance);
        } else {
            formattedBalance = String.format("%s", pointBalance);
        }
        latestValidPoint = formattedBalance;
        calculateAmount(pointBalance);
        editPointRedeemed.setText(formattedBalance);
        textTotalPoint.setText(formattedBalance);
        textAmountToPay.setText(getString(R.string.prefix_money, Utils.getFormattedAmount(amountToPay)));
    }

    private void initValues() {
        double totalAmount = MidtransSDK.getInstance().getTransactionRequest().getAmount();
        this.amountToPay = totalAmount;
        this.totalAmount = totalAmount;
        this.pointRedeemed = 0;

        Bundle bundle = getArguments();
        if (bundle != null) {
            this.pointBalance = bundle.getFloat(ARGS_POINT);
            this.pointBank = bundle.getString(ARGS_BANK);
        }
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.button_point_increase) {
            onIncreasePoint();
        } else if (viewId == R.id.button_point_decrease) {
            onDecreasePoint();
        } else if (viewId == R.id.btn_redeem_point) {
            String strPoint = editPointRedeemed.getText().toString().trim();
            float redeemedPoint = -1;
            try {
                redeemedPoint = Float.valueOf(strPoint);
            } catch (Exception e) {

            }
            ((CreditCardFlowActivity) getActivity()).payWithBankPoint(redeemedPoint);
        }
    }

    private void updatePointButtonStatus() {
        boolean enableIncrease = true;
        boolean enableDecrease = true;

        if (pointRedeemed <= 0) {
            enableDecrease = false;
        }

        if (pointRedeemed >= pointBalance) {
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

    private void onDecreasePoint() {
        inputPointFromButtons = true;
        long currentBalance = getCurrentPoint();
        long newBalance = currentBalance - MULTIPLY;

        if (isValidCurrentBalance(newBalance)) {
            calculateAmount(newBalance);
        }

        updateViews();
        inputPointFromButtons = false;
    }

    private void onIncreasePoint() {
        inputPointFromButtons = true;
        long currentBalance = getCurrentPoint();
        long newBalance = currentBalance + MULTIPLY;
        if (isValidCurrentBalance(newBalance)) {
            calculateAmount(newBalance);
        }

        updateViews();
        inputPointFromButtons = false;
    }

    private boolean isValidCurrentBalance(long currentBalance) {
        return currentBalance >= 0 && currentBalance <= this.pointBalance;
    }

    private void updateViews() {
        if (inputPointFromButtons) {
            editPointRedeemed.setText(String.valueOf(pointRedeemed));
        }
        textAmountToPay.setText(getString(R.string.prefix_money, Utils.getFormattedAmount(amountToPay)));
        updatePointButtonStatus();
    }

    private void calculateAmount(float currentBalance) {
        this.pointRedeemed = currentBalance;
        this.amountToPay = totalAmount - pointRedeemed;
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
