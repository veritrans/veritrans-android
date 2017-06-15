package com.midtrans.sdk.uikit.fragments;

import android.content.res.ColorStateList;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatEditText;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.midtrans.sdk.corekit.core.LocalDataHandler;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.UserDetail;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.activities.UserDetailsActivity;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.widgets.FancyButton;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.UUID;

public class UserDetailFragment extends Fragment {

    private TextInputLayout fullNameTil;
    private TextInputLayout phoneTil;
    private TextInputLayout emailTil;
    private AppCompatEditText fullnameEt;
    private AppCompatEditText phoneEt;
    private AppCompatEditText emailEt;
    private FancyButton nextBtn;

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
        fullnameEt = (AppCompatEditText) view.findViewById(R.id.et_full_name);
        phoneEt = (AppCompatEditText) view.findViewById(R.id.et_phone);
        emailEt = (AppCompatEditText) view.findViewById(R.id.et_email);
        fullNameTil = (TextInputLayout) view.findViewById(R.id.full_name_til);
        phoneTil = (TextInputLayout) view.findViewById(R.id.phone_til);
        emailTil = (TextInputLayout) view.findViewById(R.id.email_til);
        nextBtn = (FancyButton) view.findViewById(R.id.btn_next);
        if (midtransSDK != null) {
            if (midtransSDK.getSemiBoldText() != null) {
                nextBtn.setCustomTextFont(midtransSDK.getSemiBoldText());
            }

            if (midtransSDK.getColorTheme() != null) {
                if (midtransSDK.getColorTheme().getSecondaryColor() != 0) {
                    // Set color filter in edit text
                    try {
                        // Set on name
                        Field fDefaultTextColorName = TextInputLayout.class.getDeclaredField("mDefaultTextColor");
                        fDefaultTextColorName.setAccessible(true);
                        fDefaultTextColorName.set(fullNameTil, new ColorStateList(new int[][]{{0}}, new int[]{midtransSDK.getColorTheme().getSecondaryColor()}));

                        Field fFocusedTextColorChallengeToken = TextInputLayout.class.getDeclaredField("mFocusedTextColor");
                        fFocusedTextColorChallengeToken.setAccessible(true);
                        fFocusedTextColorChallengeToken.set(fullNameTil, new ColorStateList(new int[][]{{0}}, new int[]{midtransSDK.getColorTheme().getSecondaryColor()}));

                        fullnameEt.setSupportBackgroundTintList(new ColorStateList(new int[][]{{0}}, new int[]{midtransSDK.getColorTheme().getSecondaryColor()}));

                        // Set on debit phone
                        Field fDefaultTextColorPhone = TextInputLayout.class.getDeclaredField("mDefaultTextColor");
                        fDefaultTextColorPhone.setAccessible(true);
                        fDefaultTextColorPhone.set(phoneTil, new ColorStateList(new int[][]{{0}}, new int[]{midtransSDK.getColorTheme().getSecondaryColor()}));

                        Field fFocusedTextColorPhone = TextInputLayout.class.getDeclaredField("mFocusedTextColor");
                        fFocusedTextColorPhone.setAccessible(true);
                        fFocusedTextColorPhone.set(phoneTil, new ColorStateList(new int[][]{{0}}, new int[]{midtransSDK.getColorTheme().getSecondaryColor()}));

                        phoneEt.setSupportBackgroundTintList(new ColorStateList(new int[][]{{0}}, new int[]{midtransSDK.getColorTheme().getSecondaryColor()}));

                        // Set on debit email
                        Field fDefaultTextColorEmail = TextInputLayout.class.getDeclaredField("mDefaultTextColor");
                        fDefaultTextColorEmail.setAccessible(true);
                        fDefaultTextColorEmail.set(emailTil, new ColorStateList(new int[][]{{0}}, new int[]{midtransSDK.getColorTheme().getSecondaryColor()}));

                        Field fFocusedTextColorEmail = TextInputLayout.class.getDeclaredField("mFocusedTextColor");
                        fFocusedTextColorEmail.setAccessible(true);
                        fFocusedTextColorEmail.set(emailTil, new ColorStateList(new int[][]{{0}}, new int[]{midtransSDK.getColorTheme().getSecondaryColor()}));

                        emailEt.setSupportBackgroundTintList(new ColorStateList(new int[][]{{0}}, new int[]{midtransSDK.getColorTheme().getSecondaryColor()}));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                if (midtransSDK.getColorTheme().getPrimaryColor() != 0) {
                    nextBtn.setBackgroundColor(midtransSDK.getColorTheme().getPrimaryColor());
                }
            }
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

        if (!TextUtils.isEmpty(email) && !SdkUIFlowUtil.isEmailValid(email)) {
            SdkUIFlowUtil.showToast(getActivity(), getString(R.string.validation_email_invalid));
            emailEt.requestFocus();
            return;
        } else if (!TextUtils.isEmpty(phoneNo) && !SdkUIFlowUtil.isPhoneNumberValid(phoneNo)) {
            SdkUIFlowUtil.showToast(getActivity(), getString(R.string.validation_phone_no_invalid));
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
