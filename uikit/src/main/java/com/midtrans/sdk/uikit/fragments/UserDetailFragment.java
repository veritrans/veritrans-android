package com.midtrans.sdk.uikit.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.midtrans.sdk.corekit.core.LocalDataHandler;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.UserDetail;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.activities.UserDetailsActivity;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;

import java.io.IOException;
import java.util.UUID;

public class UserDetailFragment extends Fragment {

    private EditText fullnameEt;
    private EditText phoneEt;
    private EditText emailEt;
    private Button nextBtn;

    public UserDetailFragment() {

    }

    public static UserDetailFragment newInstance() {
        UserDetailFragment fragment = new UserDetailFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        MidtransSDK midtransSDK = MidtransSDK.getInstance();
        UserDetailsActivity userDetailsActivity = (UserDetailsActivity) getActivity();
        if (userDetailsActivity != null && userDetailsActivity.getSupportActionBar() != null) {
            userDetailsActivity.getSupportActionBar().setTitle(getString(R.string.title_user_details));
        }
        fullnameEt = (EditText) view.findViewById(R.id.et_full_name);
        phoneEt = (EditText) view.findViewById(R.id.et_phone);
        emailEt = (EditText) view.findViewById(R.id.et_email);
        nextBtn = (Button) view.findViewById(R.id.btn_next);
        if (midtransSDK != null && midtransSDK.getSemiBoldText() != null) {
            nextBtn.setTypeface(Typeface.createFromAsset(getContext().getAssets(), midtransSDK.getSemiBoldText()));
        }
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    validateSaveData();
                } catch (IOException e) {
                    e.printStackTrace();

                }
            }
        });
        super.onViewCreated(view, savedInstanceState);
    }

    private void validateSaveData() throws IOException {
        SdkUIFlowUtil.hideKeyboard(getActivity());
        String fullName = fullnameEt.getText().toString().trim();
        String email = emailEt.getText().toString().trim();
        String phoneNo = phoneEt.getText().toString().trim();

        if (TextUtils.isEmpty(fullName)) {
            SdkUIFlowUtil.showSnackbar(getActivity(), getString(R.string.validation_full_name_empty));
            fullnameEt.requestFocus();
            return;
        } else if (TextUtils.isEmpty(email)) {
            SdkUIFlowUtil.showSnackbar(getActivity(), getString(R.string.validation_email_empty));
            emailEt.requestFocus();
            return;
        } else if (!SdkUIFlowUtil.isEmailValid(email)) {
            SdkUIFlowUtil.showSnackbar(getActivity(), getString(R.string.validation_email_invalid));
            emailEt.requestFocus();
            return;
        } else if (TextUtils.isEmpty(phoneNo)) {
            SdkUIFlowUtil.showSnackbar(getActivity(), getString(R.string.validation_phone_no_empty));
            phoneEt.requestFocus();
            return;
        } else if (!SdkUIFlowUtil.isPhoneNumberValid(phoneNo)) {
            SdkUIFlowUtil.showSnackbar(getActivity(), getString(R.string.validation_phone_no_invalid));
            phoneEt.requestFocus();
            return;
        }
        UserDetail userDetail = null;
        try {
            userDetail = LocalDataHandler.readObject(getString(R.string.user_details), UserDetail.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (userDetail == null) {
            userDetail = new UserDetail();
        }
        userDetail.setUserFullName(fullName);
        userDetail.setEmail(email);
        userDetail.setPhoneNumber(phoneNo);
        userDetail.setUserId(UUID.randomUUID().toString());
        Logger.i("writting in file");
        LocalDataHandler.saveObject(getString(R.string.user_details), userDetail);
        UserAddressFragment userAddressFragment = UserAddressFragment.newInstance();
        ((UserDetailsActivity) getActivity()).replaceFragment(userAddressFragment);
    }

}
