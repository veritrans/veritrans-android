package id.co.veritrans.sdk.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import java.io.FileNotFoundException;
import java.io.IOException;

import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.activities.UserDetailsActivity;
import id.co.veritrans.sdk.core.Constants;
import id.co.veritrans.sdk.core.Logger;
import id.co.veritrans.sdk.core.SdkUtil;
import id.co.veritrans.sdk.core.StorageDataHandler;
import id.co.veritrans.sdk.models.UserDetail;

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
        ((UserDetailsActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string
                .title_user_details));
        fullnameEt = (EditText) view.findViewById(R.id.et_full_name);
        phoneEt = (EditText) view.findViewById(R.id.et_phone);
        emailEt = (EditText) view.findViewById(R.id.et_email);
        nextBtn = (Button) view.findViewById(R.id.btn_next);
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
        SdkUtil.hideKeyboard(getActivity());
        String fullName = fullnameEt.getText().toString().trim();
        String email = emailEt.getText().toString().trim();
        String phoneNo = phoneEt.getText().toString().trim();

        if (TextUtils.isEmpty(fullName)) {
            SdkUtil.showSnackbar(getActivity(), getString(R.string.validation_full_name_empty));
            fullnameEt.requestFocus();
            return;
        } else if (TextUtils.isEmpty(email)) {
            SdkUtil.showSnackbar(getActivity(), getString(R.string.validation_email_empty));
            emailEt.requestFocus();
            return;
        } else if (!SdkUtil.isEmailValid(email)) {
            SdkUtil.showSnackbar(getActivity(), getString(R.string.validation_email_invalid));
            emailEt.requestFocus();
            return;
        } else if (TextUtils.isEmpty(phoneNo)) {
            SdkUtil.showSnackbar(getActivity(), getString(R.string.validation_phone_no_empty));
            phoneEt.requestFocus();
            return;
        } else if (!SdkUtil.isPhoneNumberValid(phoneNo)) {
            SdkUtil.showSnackbar(getActivity(), getString(R.string.validation_phone_no_invalid));
            phoneEt.requestFocus();
            return;
        }
        UserDetail userDetail = null;
        StorageDataHandler storageDataHandler = new StorageDataHandler();
        try {
            userDetail = (UserDetail) storageDataHandler.readObject(getActivity(), Constants.USER_DETAILS);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }
        if(userDetail == null) {
            userDetail = new UserDetail();
        }
        userDetail.setUserFullName(fullName);
        userDetail.setEmail(email);
        userDetail.setPhoneNumber(phoneNo);
        Logger.i("writting in file");
        storageDataHandler.writeObject(getActivity(), Constants.USER_DETAILS, userDetail);
        UserAddressFragment userAddressFragment = UserAddressFragment.newInstance();
        ((UserDetailsActivity) getActivity()).replaceFragment(userAddressFragment);

    }

}
