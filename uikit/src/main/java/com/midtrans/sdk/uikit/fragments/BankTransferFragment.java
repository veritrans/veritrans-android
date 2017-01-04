package com.midtrans.sdk.uikit.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.midtrans.sdk.corekit.core.LocalDataHandler;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.UserDetail;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.activities.BankTransferInstructionActivity;
import com.midtrans.sdk.uikit.widgets.FancyButton;

/**
 * It displays payment related instructions on the screen. Created by shivam on 10/27/15.
 */
public class BankTransferFragment extends Fragment {

    private FancyButton btnSeeInstruction = null;
    private EditText mEditTextEmailId = null;
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
        try {
            userDetail = LocalDataHandler.readObject(getString(R.string.user_details), UserDetail.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mEditTextEmailId = (EditText) view.findViewById(R.id.et_email);
        MidtransSDK midtransSDK = MidtransSDK.getInstance();
        try {
            mEditTextEmailId.setText(userDetail.getEmail());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        if (midtransSDK != null) {
            mEditTextEmailId.setHintTextColor(midtransSDK.getThemeColor());
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