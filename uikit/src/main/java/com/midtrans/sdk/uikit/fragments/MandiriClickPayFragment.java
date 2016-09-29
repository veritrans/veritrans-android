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
import android.widget.Button;
import android.widget.EditText;

import com.midtrans.sdk.uikit.BuildConfig;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.activities.MandiriClickPayActivity;
import com.midtrans.sdk.uikit.activities.MandiriClickPayInstructionActivity;

/**
 * Created by shivam on 10/28/15.
 */
public class MandiriClickPayFragment extends Fragment {


    private static final String DUMMY_CARD_NUMBER = "4811111111111114";
    private EditText mEditTextDebitCardNumber = null;
    private EditText mEditTextChallengeToken = null;
    private Button btnSeeInstruction = null;

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
        btnSeeInstruction = (Button) view.findViewById(R.id.btn_see_instruction);

        setTextWatcher();

        if (BuildConfig.DEBUG) {
            mEditTextDebitCardNumber.setText(DUMMY_CARD_NUMBER);
        }

        btnSeeInstruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                getActivity().startActivity(new Intent(getActivity(), MandiriClickPayInstructionActivity.class));

            }
        });

    }

    private void setTextWatcher() {
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence text, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence text, int start, int before, int count) {

                if (getActivity() != null) {
                    ((MandiriClickPayActivity) getActivity()).onChangeOfDebitCardNumer(text);
                }
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

}