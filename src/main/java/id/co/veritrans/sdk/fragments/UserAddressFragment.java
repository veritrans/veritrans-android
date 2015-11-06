package id.co.veritrans.sdk.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;

import java.io.IOException;
import java.util.ArrayList;

import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.activities.PaymentMethodsActivity;
import id.co.veritrans.sdk.activities.UserDetailsActivity;
import id.co.veritrans.sdk.core.Constants;
import id.co.veritrans.sdk.core.Logger;
import id.co.veritrans.sdk.core.SdkUtil;
import id.co.veritrans.sdk.core.StorageDataHandler;
import id.co.veritrans.sdk.models.UserAddress;
import id.co.veritrans.sdk.models.UserDetail;

public class UserAddressFragment extends Fragment {

    private EditText etAddress;
    private EditText etCity;
    private EditText etZipcode;
    private EditText etCountry;
    private CheckBox cbShippingAddress;
    private RelativeLayout shippingAddressContainer;
    private EditText etShippingAddress;
    private EditText etShippingCity;
    private EditText etShippingZipcode;
    private EditText etShippingCountry;
    private Button btnNext;


    public UserAddressFragment() {
        // Required empty public constructor
    }

    public static UserAddressFragment newInstance() {
        UserAddressFragment fragment = new UserAddressFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((UserDetailsActivity) getActivity()).getSupportActionBar().setTitle(getString(R.string
                .title_shipping_billing_address));


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_address, container, false);
        findViews(view);

        return view;
    }

    private void findViews(View view) {
        etAddress = (EditText) view.findViewById(R.id.et_address);
        etCity = (EditText) view.findViewById(R.id.et_city);
        etZipcode = (EditText) view.findViewById(R.id.et_zipcode);
        etCountry = (EditText) view.findViewById(R.id.et_country);
        cbShippingAddress = (CheckBox) view.findViewById(R.id.cb_shipping_address);
        shippingAddressContainer = (RelativeLayout) view.findViewById(R.id
                .shipping_address_container);
        etShippingAddress = (EditText) view.findViewById(R.id.et_shipping_address);
        etShippingCity = (EditText) view.findViewById(R.id.et_shipping_city);
        etShippingZipcode = (EditText) view.findViewById(R.id.et_shipping_zipcode);
        etShippingCountry = (EditText) view.findViewById(R.id.et_shipping_country);
        btnNext = (Button) view.findViewById(R.id.btn_next);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndSaveAddress();
            }
        });

        cbShippingAddress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    shippingAddressContainer.setVisibility(View.GONE);
                } else {
                    shippingAddressContainer.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void validateAndSaveAddress() {
        SdkUtil.hideKeyboard(getActivity());
        StorageDataHandler storageDataHandler = new StorageDataHandler();
        UserDetail userDetail = null;
        try {
            userDetail = (UserDetail) storageDataHandler.readObject(getActivity(), Constants
                    .USER_DETAILS);

        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            Logger.i("userDetails:" + userDetail.getUserFullName());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        ArrayList<UserAddress> userAddresses = new ArrayList<>();
        String billingAddress = etAddress.getText().toString().trim();
        String billingCity = etCity.getText().toString().trim();
        String zipcode = etZipcode.getText().toString().trim();
        String country = etCountry.getText().toString().trim();
        String shippingAddress = etShippingAddress.getText().toString().trim();
        String shippingCity = etShippingCity.getText().toString().trim();
        String shippingZipcode = etShippingZipcode.getText().toString().trim();
        String shippingCountry = etShippingCountry.getText().toString().trim();

        if (TextUtils.isEmpty(billingAddress)) {
            SdkUtil.showSnackbar(getActivity(), getString(R.string
                    .validation_billingaddress_empty));
            etAddress.requestFocus();
            return;
        } else if (TextUtils.isEmpty(billingCity)) {
            SdkUtil.showSnackbar(getActivity(), getString(R.string.validation_billingcity_empty));
            etCity.requestFocus();
            return;
        } else if (TextUtils.isEmpty(zipcode)) {
            SdkUtil.showSnackbar(getActivity(), getString(R.string
                    .validation_billingzipcode_empty));
            etZipcode.requestFocus();
            return;
        } else if (zipcode.length() < Constants.ZIPCODE_LENGTH) {
            SdkUtil.showSnackbar(getActivity(), getString(R.string
                    .validation_billingzipcode_invalid));
            etZipcode.requestFocus();
            return;
        } else if (TextUtils.isEmpty(country)) {
            SdkUtil.showSnackbar(getActivity(), getString(R.string
                    .validation_billingcountry_empty));
            etCountry.requestFocus();
            return;
        } else if (!cbShippingAddress.isChecked()) {
            if (TextUtils.isEmpty(shippingAddress)) {
                SdkUtil.showSnackbar(getActivity(), getString(R.string
                        .validation_shippingaddress_empty));
                etShippingAddress.requestFocus();
                return;
            } else if (TextUtils.isEmpty(shippingCity)) {
                SdkUtil.showSnackbar(getActivity(), getString(R.string
                        .validation_shippingcity_empty));
                etShippingCity.requestFocus();
                return;
            } else if (TextUtils.isEmpty(shippingZipcode)) {
                SdkUtil.showSnackbar(getActivity(), getString(R.string
                        .validation_shippingzipcode_empty));
                etShippingZipcode.requestFocus();
                return;
            } else if (shippingZipcode.length() < Constants.ZIPCODE_LENGTH) {
                SdkUtil.showSnackbar(getActivity(), getString(R.string
                        .validation_shippingzipcode_invalid));
                etShippingZipcode.requestFocus();
                return;
            } else if (TextUtils.isEmpty(shippingCountry)) {
                SdkUtil.showSnackbar(getActivity(), getString(R.string
                        .validation_shippingcountry_empty));
                etShippingCountry.requestFocus();
                return;
            }
            UserAddress shippingUserAddress = new UserAddress();
            shippingUserAddress.setAddress(shippingAddress);
            shippingUserAddress.setCity(shippingCity);
            shippingUserAddress.setCountry(shippingCountry);
            shippingUserAddress.setZipcode(shippingZipcode);
            shippingUserAddress.setAddressType(Constants.ADDRESS_TYPE_SHIPPING);
            userAddresses.add(shippingUserAddress);
           /* try {
                storageDataHandler.writeObject(getActivity(), Constants.USER_ADDRESS_DETAILS,
                shippingUserAddress);
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        }
        UserAddress billingUserAddress = new UserAddress();
        billingUserAddress.setAddress(billingAddress);
        billingUserAddress.setCity(billingCity);
        billingUserAddress.setCountry(country);
        billingUserAddress.setZipcode(zipcode);
        if (cbShippingAddress.isChecked()) {
            billingUserAddress.setAddressType(Constants.ADDRESS_TYPE_BOTH);
        } else {
            billingUserAddress.setAddressType(Constants.ADDRESS_TYPE_BILLING);
        }
        userAddresses.add(billingUserAddress);

        try {
            if (userDetail == null) {
                userDetail = new UserDetail();
            }
            userDetail.setUserAddresses(userAddresses);
            storageDataHandler.writeObject(getActivity(), Constants.USER_DETAILS, userDetail);
            Intent selectPaymentIntent = new Intent(getActivity(), PaymentMethodsActivity.class);
            startActivity(selectPaymentIntent);
            getActivity().finish();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
