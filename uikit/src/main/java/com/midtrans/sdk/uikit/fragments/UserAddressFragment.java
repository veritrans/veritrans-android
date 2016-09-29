package com.midtrans.sdk.uikit.fragments;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.midtrans.sdk.corekit.core.Constants;
import com.midtrans.sdk.corekit.core.LocalDataHandler;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.UserAddress;
import com.midtrans.sdk.corekit.models.UserDetail;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.activities.PaymentMethodsActivity;
import com.midtrans.sdk.uikit.activities.UserDetailsActivity;
import com.midtrans.sdk.uikit.adapters.ListCountryAdapter;
import com.midtrans.sdk.uikit.models.CountryCodeModel;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;

import java.io.InputStream;
import java.util.ArrayList;

public class UserAddressFragment extends Fragment {

    MidtransSDK midtransSDK;
    private EditText etAddress;
    private EditText etCity;
    private EditText etZipcode;
    private AutoCompleteTextView etCountry;
    private CheckBox cbShippingAddress;
    private RelativeLayout shippingAddressContainer;
    private EditText etShippingAddress;
    private EditText etShippingCity;
    private EditText etShippingZipcode;
    private AutoCompleteTextView etShippingCountry;
    private Button btnNext;
    private ListCountryAdapter billingCountryAdapter;
    private ListCountryAdapter shippingCountryAdapter;
    private ArrayList<CountryCodeModel> countryCodeList = new ArrayList<>();
    private String billingCountryCodeSelected;
    private String shippingCountryCodeSelected;


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
        UserDetailsActivity userDetailsActivity = (UserDetailsActivity) getActivity();
        if (userDetailsActivity != null) {
            if (userDetailsActivity.getSupportActionBar() != null)
                userDetailsActivity.getSupportActionBar().setTitle(getString(R.string.title_shipping_billing_address));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_user_address, container, false);
    }

    private void retrieveCountryCode() {
        ArrayList<CountryCodeModel> list;
        String data;
        try {
            InputStream is = getContext().getAssets().open("country_code.json");
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            data = new String(buffer, "UTF-8");

            Gson gson = new Gson();
            list = gson.fromJson(data, new TypeToken<ArrayList<CountryCodeModel>>() {
            }.getType());
            if (list != null) {
                this.countryCodeList = list;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        midtransSDK = MidtransSDK.getInstance();
        findViews(view);
        retrieveCountryCode();

        billingCountryAdapter = new ListCountryAdapter(getContext(), R.layout.layout_row_contry_code, countryCodeList);
        shippingCountryAdapter = new ListCountryAdapter(getContext(), R.layout.layout_row_contry_code, countryCodeList);

        etCountry.setAdapter(billingCountryAdapter);
        etCountry.setThreshold(1);

        etShippingCountry.setAdapter(shippingCountryAdapter);
        etShippingCountry.setThreshold(1);

        super.onViewCreated(view, savedInstanceState);
    }

    private void findViews(View view) {
        etAddress = (EditText) view.findViewById(R.id.et_address);
        etCity = (EditText) view.findViewById(R.id.et_city);
        etZipcode = (EditText) view.findViewById(R.id.et_zipcode);
        etCountry = (AutoCompleteTextView) view.findViewById(R.id.et_country);
        cbShippingAddress = (CheckBox) view.findViewById(R.id.cb_shipping_address);
        shippingAddressContainer = (RelativeLayout) view.findViewById(R.id
                .shipping_address_container);
        etShippingAddress = (EditText) view.findViewById(R.id.et_shipping_address);
        etShippingCity = (EditText) view.findViewById(R.id.et_shipping_city);
        etShippingZipcode = (EditText) view.findViewById(R.id.et_shipping_zipcode);
        etShippingCountry = (AutoCompleteTextView) view.findViewById(R.id.et_shipping_country);
        btnNext = (Button) view.findViewById(R.id.btn_next);


        if (midtransSDK != null && midtransSDK.getSemiBoldText() != null) {
            btnNext.setTypeface(Typeface.createFromAsset(getContext().getAssets(), midtransSDK.getSemiBoldText()));
        }
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
        SdkUIFlowUtil.hideKeyboard(getActivity());
        UserDetail userDetail = LocalDataHandler.readObject(getString(R.string.user_details), UserDetail.class);
        if (userDetail != null) Logger.i("userDetails:" + userDetail.getUserFullName());

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
            SdkUIFlowUtil.showSnackbar(getActivity(), getString(R.string
                    .validation_billingaddress_empty));
            etAddress.requestFocus();
            return;
        } else if (TextUtils.isEmpty(billingCity)) {
            SdkUIFlowUtil.showSnackbar(getActivity(), getString(R.string.validation_billingcity_empty));
            etCity.requestFocus();
            return;
        } else if (TextUtils.isEmpty(zipcode)) {
            SdkUIFlowUtil.showSnackbar(getActivity(), getString(R.string
                    .validation_billingzipcode_empty));
            etZipcode.requestFocus();
            return;
        } else if (zipcode.length() < Constants.ZIPCODE_LENGTH) {
            SdkUIFlowUtil.showSnackbar(getActivity(), getString(R.string
                    .validation_billingzipcode_invalid));
            etZipcode.requestFocus();
            return;
        } else if (TextUtils.isEmpty(country)) {
            SdkUIFlowUtil.showSnackbar(getActivity(), getString(R.string
                    .validation_billingcountry_empty));
            etCountry.requestFocus();
            return;
        } else if (!isCountryCodeExist(country, true)) {
            SdkUIFlowUtil.showSnackbar(getActivity(), getString(R.string
                    .validation_billingcountry_notexist));
            etCountry.requestFocus();
            return;
        } else if (!cbShippingAddress.isChecked()) {
            if (TextUtils.isEmpty(shippingAddress)) {
                SdkUIFlowUtil.showSnackbar(getActivity(), getString(R.string
                        .validation_shippingaddress_empty));
                etShippingAddress.requestFocus();
                return;
            } else if (TextUtils.isEmpty(shippingCity)) {
                SdkUIFlowUtil.showSnackbar(getActivity(), getString(R.string
                        .validation_shippingcity_empty));
                etShippingCity.requestFocus();
                return;
            } else if (TextUtils.isEmpty(shippingZipcode)) {
                SdkUIFlowUtil.showSnackbar(getActivity(), getString(R.string
                        .validation_shippingzipcode_empty));
                etShippingZipcode.requestFocus();
                return;
            } else if (shippingZipcode.length() < Constants.ZIPCODE_LENGTH) {
                SdkUIFlowUtil.showSnackbar(getActivity(), getString(R.string
                        .validation_shippingzipcode_invalid));
                etShippingZipcode.requestFocus();
                return;
            } else if (TextUtils.isEmpty(shippingCountry)) {
                SdkUIFlowUtil.showSnackbar(getActivity(), getString(R.string
                        .validation_shippingcountry_empty));
                etShippingCountry.requestFocus();
                return;
            } else if (!isCountryCodeExist(shippingCountry, false)) {
                SdkUIFlowUtil.showSnackbar(getActivity(), getString(R.string
                        .validation_billingcountry_notexist));
                etCountry.requestFocus();
                return;
            }
            UserAddress shippingUserAddress = new UserAddress();
            shippingUserAddress.setAddress(shippingAddress);
            shippingUserAddress.setCity(shippingCity);
            shippingUserAddress.setCountry(shippingCountryCodeSelected);
            shippingUserAddress.setZipcode(shippingZipcode);
            shippingUserAddress.setAddressType(Constants.ADDRESS_TYPE_SHIPPING);
            userAddresses.add(shippingUserAddress);
        }
        UserAddress billingUserAddress = new UserAddress();
        billingUserAddress.setAddress(billingAddress);
        billingUserAddress.setCity(billingCity);
        billingUserAddress.setCountry(billingCountryCodeSelected);
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
            LocalDataHandler.saveObject(getString(R.string.user_details), userDetail);
            Intent selectPaymentIntent = new Intent(getActivity(), PaymentMethodsActivity.class);
            startActivity(selectPaymentIntent);
            getActivity().finish();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public boolean isCountryCodeExist(String countryName, boolean isBillingAddress) {
        for (CountryCodeModel country : countryCodeList) {
            if (country.getName().equalsIgnoreCase(countryName)) {
                if (isBillingAddress) {
                    billingCountryCodeSelected = country.getCountryCodeAlpha();
                } else {
                    shippingCountryCodeSelected = country.getCountryCodeAlpha();
                }
                return true;
            }
        }
        return false;
    }
}
