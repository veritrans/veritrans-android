package com.midtrans.sdk.uikit.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.uikit.BuildConfig;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.activities.MandiriClickPayInstructionActivity;
import com.midtrans.sdk.uikit.constants.AnalyticsEventName;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;
import com.midtrans.sdk.uikit.widgets.FancyButton;

/**
 * Created by shivam on 10/28/15.
 */
public class MandiriClickPayFragment extends Fragment {


    private static final String DUMMY_CARD_NUMBER = "4811111111111114";
    private EditText mEditTextDebitCardNumber = null;
    private EditText mEditTextChallengeToken = null;
    private FancyButton btnSeeInstruction = null;
    private DefaultTextView textAppli, textInput1, textInput2, textInput3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mandiri_click_pay, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mEditTextChallengeToken = (EditText) view.findViewById(R.id.et_challenge_token);
        mEditTextDebitCardNumber = (EditText) view.findViewById(R.id.et_debit_card_no);
        btnSeeInstruction = (FancyButton) view.findViewById(R.id.btn_see_instruction);
        textInput1 = (DefaultTextView) view.findViewById(R.id.text_input_1);
        textInput2 = (DefaultTextView) view.findViewById(R.id.text_input_2);
        textInput3 = (DefaultTextView) view.findViewById(R.id.text_input_3);
        setTextWatcher();

        if (BuildConfig.DEBUG) {
            mEditTextDebitCardNumber.setText(DUMMY_CARD_NUMBER);
        }

        btnSeeInstruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //track page mandiri click pay overview
                MidtransSDK.getInstance().trackEvent(AnalyticsEventName.PAGE_MANDIRI_CLICKPAY_OVERVIEW);

                getActivity().startActivity(new Intent(getActivity(), MandiriClickPayInstructionActivity.class));
            }
        });

        textInput1.setText("");
        textInput2.setText(String.valueOf(MidtransSDK.getInstance().getTransactionRequest().getAmount()));
        textInput3.setText(String.valueOf(SdkUIFlowUtil.generateRandomNumber()));
    }

    private void setTextWatcher() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence text, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {
                onChangeOfDebitCardNumer(text);
            }

            @Override
            public void afterTextChanged(Editable test) {
                char space = ' ';

                if (test.length() > 0 && (test.length() % 5) == 0) {
                    final char c = test.charAt(test.length() - 1);
                    if (space == c) {
                        test.delete(test.length() - 1, test.length());
                    }
                }
                // Insert char where needed.
                if (test.length() > 0 && (test.length() % 5) == 0) {
                    char c = test.charAt(test.length() - 1);
                    // Only if its a digit where there should be a space we insert a space
                    if (Character.isDigit(c) && TextUtils.split(test.toString(), String.valueOf
                            (space)).length <= 3) {
                        test.insert(test.length() - 1, String.valueOf(space));
                    }
                }

            }
        };


        mEditTextDebitCardNumber.addTextChangedListener(textWatcher);
    }


    private void onChangeOfDebitCardNumer(CharSequence text) {

        if (text != null && text.length() > 0) {
            String cardNumber = text.toString().trim().replace(" ", "");

            if (cardNumber.length() > 10) {
                textInput1.setText(cardNumber.substring(cardNumber.length() - 10,
                        cardNumber.length()));
            } else {
                textInput1.setText(cardNumber);
            }
        } else {
            textInput1.setText("");
        }
    }

    public String getChallengeToken() {
        if (mEditTextChallengeToken != null) {
            return mEditTextChallengeToken.getText().toString();
        }
        return null;
    }


    public String getDebitCardNumber() {
        if (mEditTextDebitCardNumber != null) {
            return mEditTextDebitCardNumber.getText().toString();
        }
        return null;
    }

    public String getInput1() {
        if (textInput1 != null) {
            return textInput1.getText().toString();
        }
        return null;
    }

    public String getInput2() {
        if (textInput2 != null) {
            return textInput2.getText().toString();
        }
        return null;
    }

    public String getInput3() {
        if (textInput3 != null) {
            return textInput3.getText().toString();
        }
        return null;
    }
}