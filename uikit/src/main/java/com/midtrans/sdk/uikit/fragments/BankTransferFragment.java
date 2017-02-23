package com.midtrans.sdk.uikit.fragments;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.midtrans.sdk.corekit.core.LocalDataHandler;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.UserDetail;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.activities.BankTransferInstructionActivity;
import com.midtrans.sdk.uikit.widgets.FancyButton;

import java.lang.reflect.Field;

/**
 * It displays payment related instructions on the screen. Created by shivam on 10/27/15.
 */
public class BankTransferFragment extends Fragment {

    private FancyButton btnSeeInstruction = null;
    private TextInputLayout mTextInputEmailId = null;
    private AppCompatEditText mEditTextEmailId = null;
    private UserDetail userDetail;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bank_transfer, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initializeViews(view);
    }

    /**
     * initializes view and adds click listener for it.
     *
     * @param view view that needed to be initialized
     */
    private void initializeViews(View view) {
        btnSeeInstruction = (FancyButton) view.findViewById(R.id.btn_see_instruction);
        mEditTextEmailId = (AppCompatEditText) view.findViewById(R.id.et_email);
        mTextInputEmailId = (TextInputLayout) view.findViewById(R.id.email_til);
        try {
            userDetail = LocalDataHandler.readObject(getString(R.string.user_details), UserDetail.class);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            mEditTextEmailId.setText(userDetail.getEmail());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        btnSeeInstruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity() != null) {
                    Intent intent = new Intent(getActivity(), BankTransferInstructionActivity.class);
                    intent.putExtra(BankTransferInstructionActivity.BANK, getArguments().getString(BankTransferInstructionActivity.BANK));
                    getActivity().startActivity(intent);
                }
            }
        });

        MidtransSDK midtransSDK = MidtransSDK.getInstance();
        if (midtransSDK != null && midtransSDK.getColorTheme() != null) {
            if (midtransSDK.getColorTheme().getSecondaryColor() != 0) {
                // Set color filter in edit text
                try {
                    Field fDefaultTextColor = TextInputLayout.class.getDeclaredField("mDefaultTextColor");
                    fDefaultTextColor.setAccessible(true);
                    fDefaultTextColor.set(mTextInputEmailId, new ColorStateList(new int[][]{{0}}, new int[]{midtransSDK.getColorTheme().getSecondaryColor()}));

                    Field fFocusedTextColor = TextInputLayout.class.getDeclaredField("mFocusedTextColor");
                    fFocusedTextColor.setAccessible(true);
                    fFocusedTextColor.set(mTextInputEmailId, new ColorStateList(new int[][]{{0}}, new int[]{midtransSDK.getColorTheme().getSecondaryColor()}));

                    mEditTextEmailId.setSupportBackgroundTintList(new ColorStateList(new int[][]{{0}}, new int[]{midtransSDK.getColorTheme().getSecondaryColor()}));

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (midtransSDK.getColorTheme().getPrimaryDarkColor() != 0) {
                btnSeeInstruction.setBorderColor(midtransSDK.getColorTheme().getPrimaryDarkColor());
                btnSeeInstruction.setTextColor(midtransSDK.getColorTheme().getPrimaryDarkColor());
            }
        }
    }


    /**
     * created to give access to email id field from {
     * .BankTransferActivity}.
     *
     * @return email identifier
     */
    public String getEmailId() {
        if (mEditTextEmailId != null) {
            return mEditTextEmailId.getText().toString();
        } else {
            return null;
        }
    }
}