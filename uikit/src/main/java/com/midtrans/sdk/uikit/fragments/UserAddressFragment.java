package com.midtrans.sdk.uikit.fragments;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.SwitchCompat;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.midtrans.sdk.corekit.core.Constants;
import com.midtrans.sdk.corekit.core.LocalDataHandler;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.UserAddress;
import com.midtrans.sdk.corekit.models.UserDetail;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.activities.UserDetailsActivity;
import com.midtrans.sdk.uikit.adapters.ListCountryAdapter;
import com.midtrans.sdk.uikit.models.CountryCodeModel;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.widgets.FancyButton;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;

public class UserAddressFragment extends Fragment {
    private final String TAG = UserAddressFragment.class.getSimpleName();
    MidtransSDK midtransSDK;
    private TextInputLayout tilAddress;
    private TextInputLayout tilCity;
    private TextInputLayout tilZipCode;
    private TextInputLayout tilCountry;
    private AppCompatEditText etAddress;
    private AppCompatEditText etCity;
    private AppCompatEditText etZipCode;
    private AppCompatAutoCompleteTextView etCountry;
    private SwitchCompat cbShippingAddress;
    private RelativeLayout shippingAddressContainer;
    private TextInputLayout tilShippingAddress;
    private TextInputLayout tilShippingCity;
    private TextInputLayout tilShippingZipCode;
    private TextInputLayout tilShippingCountry;
    private AppCompatEditText etShippingAddress;
    private AppCompatEditText etShippingCity;
    private AppCompatEditText etShippingZipCode;
    private AppCompatAutoCompleteTextView etShippingCountry;
    private FancyButton btnNext;
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

        billingCountryAdapter = new ListCountryAdapter(getContext(), R.layout.layout_row_country_code, countryCodeList);
        shippingCountryAdapter = new ListCountryAdapter(getContext(), R.layout.layout_row_country_code, countryCodeList);

        etCountry.setAdapter(billingCountryAdapter);
        etCountry.setThreshold(1);

        etShippingCountry.setAdapter(shippingCountryAdapter);
        etShippingCountry.setThreshold(1);

        super.onViewCreated(view, savedInstanceState);
    }

    private void findViews(View view) {
        tilAddress = (TextInputLayout) view.findViewById(R.id.address_til);
        tilCity = (TextInputLayout) view.findViewById(R.id.city_til);
        tilZipCode = (TextInputLayout) view.findViewById(R.id.zip_til);
        tilCountry = (TextInputLayout) view.findViewById(R.id.country_til);
        etAddress = (AppCompatEditText) view.findViewById(R.id.et_address);
        etCity = (AppCompatEditText) view.findViewById(R.id.et_city);
        etZipCode = (AppCompatEditText) view.findViewById(R.id.et_zipcode);
        etCountry = (AppCompatAutoCompleteTextView) view.findViewById(R.id.et_country);
        cbShippingAddress = (SwitchCompat) view.findViewById(R.id.cb_shipping_address);
        shippingAddressContainer = (RelativeLayout) view.findViewById(R.id.shipping_address_container);
        tilShippingAddress = (TextInputLayout) view.findViewById(R.id.shipping_address_til);
        tilShippingCity = (TextInputLayout) view.findViewById(R.id.shipping_city_til);
        tilShippingZipCode = (TextInputLayout) view.findViewById(R.id.shipping_zip_til);
        tilShippingCountry = (TextInputLayout) view.findViewById(R.id.shipping_country_til);
        etShippingAddress = (AppCompatEditText) view.findViewById(R.id.et_shipping_address);
        etShippingCity = (AppCompatEditText) view.findViewById(R.id.et_shipping_city);
        etShippingZipCode = (AppCompatEditText) view.findViewById(R.id.et_shipping_zipcode);
        etShippingCountry = (AppCompatAutoCompleteTextView) view.findViewById(R.id.et_shipping_country);
        btnNext = (FancyButton) view.findViewById(R.id.btn_next);
        etAddress.setSingleLine();
        etAddress.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        etCity.setSingleLine();
        etCity.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        etCountry.setSingleLine();
        etCountry.setImeOptions(EditorInfo.IME_ACTION_DONE);
        etShippingAddress.setSingleLine();
        etShippingAddress.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        etShippingCity.setSingleLine();
        etShippingCity.setImeOptions(EditorInfo.IME_ACTION_NEXT);
        etShippingCountry.setSingleLine();
        etShippingCountry.setImeOptions(EditorInfo.IME_ACTION_DONE);


        if (midtransSDK != null) {
            if (midtransSDK.getSemiBoldText() != null) {
                btnNext.setCustomTextFont(midtransSDK.getSemiBoldText());
            }

            if (midtransSDK.getColorTheme() != null) {
                if (midtransSDK.getColorTheme().getSecondaryColor() != 0) {
                    // Set color filter in edit text
                    try {
                        // Set on address
                        Field fDefaultTextColorAddress = TextInputLayout.class.getDeclaredField("mDefaultTextColor");
                        fDefaultTextColorAddress.setAccessible(true);
                        fDefaultTextColorAddress.set(tilAddress, new ColorStateList(new int[][]{{0}}, new int[]{midtransSDK.getColorTheme().getSecondaryColor()}));

                        Field fFocusedTextColorAddress = TextInputLayout.class.getDeclaredField("mFocusedTextColor");
                        fFocusedTextColorAddress.setAccessible(true);
                        fFocusedTextColorAddress.set(tilAddress, new ColorStateList(new int[][]{{0}}, new int[]{midtransSDK.getColorTheme().getSecondaryColor()}));

                        etAddress.setSupportBackgroundTintList(new ColorStateList(new int[][]{{0}}, new int[]{midtransSDK.getColorTheme().getSecondaryColor()}));

                        // Set on city
                        Field fDefaultTextColorCity = TextInputLayout.class.getDeclaredField("mDefaultTextColor");
                        fDefaultTextColorCity.setAccessible(true);
                        fDefaultTextColorCity.set(tilCity, new ColorStateList(new int[][]{{0}}, new int[]{midtransSDK.getColorTheme().getSecondaryColor()}));

                        Field fFocusedTextColorCity = TextInputLayout.class.getDeclaredField("mFocusedTextColor");
                        fFocusedTextColorCity.setAccessible(true);
                        fFocusedTextColorCity.set(tilCity, new ColorStateList(new int[][]{{0}}, new int[]{midtransSDK.getColorTheme().getSecondaryColor()}));

                        etCity.setSupportBackgroundTintList(new ColorStateList(new int[][]{{0}}, new int[]{midtransSDK.getColorTheme().getSecondaryColor()}));

                        // Set on zip
                        Field fDefaultTextColorZipCode = TextInputLayout.class.getDeclaredField("mDefaultTextColor");
                        fDefaultTextColorZipCode.setAccessible(true);
                        fDefaultTextColorZipCode.set(tilZipCode, new ColorStateList(new int[][]{{0}}, new int[]{midtransSDK.getColorTheme().getSecondaryColor()}));

                        Field fFocusedTextColorZipCode = TextInputLayout.class.getDeclaredField("mFocusedTextColor");
                        fFocusedTextColorZipCode.setAccessible(true);
                        fFocusedTextColorZipCode.set(tilZipCode, new ColorStateList(new int[][]{{0}}, new int[]{midtransSDK.getColorTheme().getSecondaryColor()}));

                        etZipCode.setSupportBackgroundTintList(new ColorStateList(new int[][]{{0}}, new int[]{midtransSDK.getColorTheme().getSecondaryColor()}));

                        // Set on country
                        Field fDefaultTextColorCountry = TextInputLayout.class.getDeclaredField("mDefaultTextColor");
                        fDefaultTextColorCountry.setAccessible(true);
                        fDefaultTextColorCountry.set(tilCountry, new ColorStateList(new int[][]{{0}}, new int[]{midtransSDK.getColorTheme().getSecondaryColor()}));

                        Field fFocusedTextColorCountry = TextInputLayout.class.getDeclaredField("mFocusedTextColor");
                        fFocusedTextColorCountry.setAccessible(true);
                        fFocusedTextColorCountry.set(tilCountry, new ColorStateList(new int[][]{{0}}, new int[]{midtransSDK.getColorTheme().getSecondaryColor()}));

                        etCountry.setSupportBackgroundTintList(new ColorStateList(new int[][]{{0}}, new int[]{midtransSDK.getColorTheme().getSecondaryColor()}));

                        // Set on shipping address
                        Field fDefaultTextColorShippingAddress = TextInputLayout.class.getDeclaredField("mDefaultTextColor");
                        fDefaultTextColorShippingAddress.setAccessible(true);
                        fDefaultTextColorShippingAddress.set(tilShippingAddress, new ColorStateList(new int[][]{{0}}, new int[]{midtransSDK.getColorTheme().getSecondaryColor()}));

                        Field fFocusedTextColorShippingAddress = TextInputLayout.class.getDeclaredField("mFocusedTextColor");
                        fFocusedTextColorShippingAddress.setAccessible(true);
                        fFocusedTextColorShippingAddress.set(tilShippingAddress, new ColorStateList(new int[][]{{0}}, new int[]{midtransSDK.getColorTheme().getSecondaryColor()}));

                        etShippingAddress.setSupportBackgroundTintList(new ColorStateList(new int[][]{{0}}, new int[]{midtransSDK.getColorTheme().getSecondaryColor()}));

                        // Set on shipping city
                        Field fDefaultTextColorShippingCity = TextInputLayout.class.getDeclaredField("mDefaultTextColor");
                        fDefaultTextColorShippingCity.setAccessible(true);
                        fDefaultTextColorShippingCity.set(tilShippingCity, new ColorStateList(new int[][]{{0}}, new int[]{midtransSDK.getColorTheme().getSecondaryColor()}));

                        Field fFocusedTextColorShippingCity = TextInputLayout.class.getDeclaredField("mFocusedTextColor");
                        fFocusedTextColorShippingCity.setAccessible(true);
                        fFocusedTextColorShippingCity.set(tilShippingCity, new ColorStateList(new int[][]{{0}}, new int[]{midtransSDK.getColorTheme().getSecondaryColor()}));

                        etShippingCity.setSupportBackgroundTintList(new ColorStateList(new int[][]{{0}}, new int[]{midtransSDK.getColorTheme().getSecondaryColor()}));

                        // Set on shipping zip
                        Field fDefaultTextColorShippingZipCode = TextInputLayout.class.getDeclaredField("mDefaultTextColor");
                        fDefaultTextColorShippingZipCode.setAccessible(true);
                        fDefaultTextColorShippingZipCode.set(tilShippingZipCode, new ColorStateList(new int[][]{{0}}, new int[]{midtransSDK.getColorTheme().getSecondaryColor()}));

                        Field fFocusedTextColorShippingZipCode = TextInputLayout.class.getDeclaredField("mFocusedTextColor");
                        fFocusedTextColorShippingZipCode.setAccessible(true);
                        fFocusedTextColorShippingZipCode.set(tilShippingZipCode, new ColorStateList(new int[][]{{0}}, new int[]{midtransSDK.getColorTheme().getSecondaryColor()}));

                        etShippingZipCode.setSupportBackgroundTintList(new ColorStateList(new int[][]{{0}}, new int[]{midtransSDK.getColorTheme().getSecondaryColor()}));

                        // Set on shipping country
                        Field fDefaultTextColorShippingCountry = TextInputLayout.class.getDeclaredField("mDefaultTextColor");
                        fDefaultTextColorShippingCountry.setAccessible(true);
                        fDefaultTextColorShippingCountry.set(tilShippingCountry, new ColorStateList(new int[][]{{0}}, new int[]{midtransSDK.getColorTheme().getSecondaryColor()}));

                        Field fFocusedTextColorShippingCountry = TextInputLayout.class.getDeclaredField("mFocusedTextColor");
                        fFocusedTextColorShippingCountry.setAccessible(true);
                        fFocusedTextColorCountry.set(tilShippingCountry, new ColorStateList(new int[][]{{0}}, new int[]{midtransSDK.getColorTheme().getSecondaryColor()}));

                        etShippingCountry.setSupportBackgroundTintList(new ColorStateList(new int[][]{{0}}, new int[]{midtransSDK.getColorTheme().getSecondaryColor()}));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (midtransSDK.getColorTheme().getPrimaryDarkColor() != 0) {
                        int[][] states = new int[][]{
                                new int[]{-android.R.attr.state_checked},
                                new int[]{android.R.attr.state_checked},
                        };

                        int[] thumbColors = new int[]{
                                Color.WHITE,
                                midtransSDK.getColorTheme().getPrimaryDarkColor(),
                        };

                        int[] trackColors = new int[]{
                                Color.GRAY,
                                midtransSDK.getColorTheme().getSecondaryColor(),
                        };
                        DrawableCompat.setTintList(DrawableCompat.wrap(cbShippingAddress.getThumbDrawable()), new ColorStateList(states, thumbColors));
                        DrawableCompat.setTintList(DrawableCompat.wrap(cbShippingAddress.getTrackDrawable()), new ColorStateList(states, trackColors));
                    }
                }

                if (midtransSDK.getColorTheme().getPrimaryColor() != 0) {
                    btnNext.setBackgroundColor(midtransSDK.getColorTheme().getPrimaryColor());
                }
            }
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

        try {
            UserDetail userDetail = SdkUIFlowUtil.getSavedUserDetails();

            if (userDetail != null) {
                Logger.i(TAG, "userDetails:" + userDetail.getUserFullName());
            }

            ArrayList<UserAddress> userAddresses = new ArrayList<>();
            String billingAddress = etAddress.getText().toString().trim();
            String billingCity = etCity.getText().toString().trim();
            String zipcode = etZipCode.getText().toString().trim();
            String country = etCountry.getText().toString().trim();
            String shippingAddress = etShippingAddress.getText().toString().trim();
            String shippingCity = etShippingCity.getText().toString().trim();
            String shippingZipcode = etShippingZipCode.getText().toString().trim();
            String shippingCountry = etShippingCountry.getText().toString().trim();

            if (!TextUtils.isEmpty(zipcode) && zipcode.length() < Constants.ZIPCODE_LENGTH) {
                SdkUIFlowUtil.showToast(getActivity(), getString(R.string
                        .validation_billingzipcode_invalid));
                etZipCode.requestFocus();
                return;
            } else if (!TextUtils.isEmpty(country) && !isCountryCodeExist(country, true)) {
                SdkUIFlowUtil.showToast(getActivity(), getString(R.string
                        .validation_billingcountry_notexist));
                etCountry.requestFocus();
                return;
            } else if (!cbShippingAddress.isChecked()) {
                if (!TextUtils.isEmpty(shippingZipcode) && shippingZipcode.length() < Constants.ZIPCODE_LENGTH) {
                    SdkUIFlowUtil.showToast(getActivity(), getString(R.string
                            .validation_shippingzipcode_invalid));
                    etShippingZipCode.requestFocus();
                    return;
                } else if (!TextUtils.isEmpty(shippingCountry) && !isCountryCodeExist(shippingCountry, false)) {
                    SdkUIFlowUtil.showToast(getActivity(), getString(R.string
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

            if (userDetail == null) {
                userDetail = new UserDetail();
            }

            userDetail.setUserAddresses(userAddresses);
            LocalDataHandler.saveObject(getString(R.string.user_details), userDetail);

            ((UserDetailsActivity) getActivity()).showPaymentpage();
        } catch (RuntimeException e) {
            Logger.e(TAG, "validateAndSaveAddress:" + e.getMessage());
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
