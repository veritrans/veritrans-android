package com.midtrans.demo;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import androidx.core.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatAutoCompleteTextView;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.CustomerDetails;
import com.midtrans.sdk.corekit.models.ShippingAddress;
import com.midtrans.sdk.uikit.models.CountryCodeModel;
import com.midtrans.sdk.uikit.widgets.FancyButton;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by Fajar on 21/11/17.
 */

public class UserAddressActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private TextInputLayout etZipCodeContainer;
    private AppCompatEditText etAddress, etCity, etZipCode;
    private AppCompatAutoCompleteTextView etCountry;
    private FancyButton saveBtn;

    private String countryCodeSelected;
    private ListCountryAdapter countryAdapter;
    private ArrayList<CountryCodeModel> countryCodeList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_address);
        bindView();
        retrieveCountryCode();
        bindData();
        bindThemes();
    }

    private void bindView() {
        etAddress = (AppCompatEditText) findViewById(R.id.et_address);
        etCity = (AppCompatEditText) findViewById(R.id.et_city);
        etZipCode = (AppCompatEditText) findViewById(R.id.et_zipcode);
        etCountry = (AppCompatAutoCompleteTextView) findViewById(R.id.et_country);
        etZipCodeContainer = (TextInputLayout) findViewById(R.id.zip_til);
        saveBtn = (FancyButton) findViewById(R.id.button_primary);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
    }

    private void bindData() {
        countryAdapter = new ListCountryAdapter(this, com.midtrans.sdk.uikit.R.layout.layout_row_country_code, countryCodeList);

        ShippingAddress shippingAddress = null;
        CustomerDetails userDetail = MidtransSDK.getInstance().getTransactionRequest().getCustomerDetails();
        if (userDetail != null && userDetail.getShippingAddress() != null) {
            shippingAddress = userDetail.getShippingAddress();
        }

        if (shippingAddress == null) {
            setResult(RESULT_CANCELED);
            finish();
        }

        etAddress.setText(shippingAddress.getAddress());
        etCity.setText(shippingAddress.getCity());
        etZipCode.setText(shippingAddress.getPostalCode());

        etCountry.setText(getCountryName(shippingAddress.getCountryCode()));
        etCountry.setAdapter(countryAdapter);
        etCountry.setThreshold(1);

        etZipCode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() == 5) {
                    etZipCodeContainer.setErrorEnabled(false);
                    saveBtn.setClickable(true);
                } else {
                    etZipCodeContainer.setError(getString(R.string.hint_error_zip_code));
                    saveBtn.setClickable(false);
                }
            }
        });

        saveBtn.setText(getString(R.string.delivery_address_save));
        saveBtn.setTextBold();
        saveBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValid()) {
                    saveData();
                } else {
                    Toast.makeText(UserAddressActivity.this, "Unable to save change(s). Please make sure there's no empty field or discard change(s).", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void bindThemes() {
        String themes = DemoPreferenceHelper.getStringPreference(this, DemoConfigActivity.COLOR_THEME);
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.back);
        if (themes != null && !TextUtils.isEmpty(themes)) {
            switch (themes) {
                case DemoThemeConstants.BLUE_THEME:
                    saveBtn.setBackgroundColor(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_HEX));
                    drawable.setColorFilter(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
                    break;
                case DemoThemeConstants.RED_THEME:
                    saveBtn.setBackgroundColor(Color.parseColor(DemoThemeConstants.RED_PRIMARY_DARK_HEX));
                    drawable.setColorFilter(Color.parseColor(DemoThemeConstants.RED_PRIMARY_HEX), PorterDuff.Mode.SRC_ATOP);
                    break;
                case DemoThemeConstants.GREEN_THEME:
                    saveBtn.setBackgroundColor(Color.parseColor(DemoThemeConstants.GREEN_PRIMARY_HEX));
                    drawable.setColorFilter(Color.parseColor(DemoThemeConstants.GREEN_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
                    break;
                case DemoThemeConstants.ORANGE_THEME:
                    saveBtn.setBackgroundColor(Color.parseColor(DemoThemeConstants.ORANGE_PRIMARY_HEX));
                    drawable.setColorFilter(Color.parseColor(DemoThemeConstants.ORANGE_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
                    break;
                case DemoThemeConstants.BLACK_THEME:
                    saveBtn.setBackgroundColor(Color.parseColor(DemoThemeConstants.BLACK_PRIMARY_HEX));
                    drawable.setColorFilter(Color.parseColor(DemoThemeConstants.BLACK_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
                    break;
                default:
                    saveBtn.setBackgroundColor(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_HEX));
                    drawable.setColorFilter(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
                    break;
            }
        } else {
            saveBtn.setBackgroundColor(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_HEX));
            drawable.setColorFilter(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
        }
        toolbar.setNavigationIcon(drawable);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }

    private void retrieveCountryCode() {
        ArrayList<CountryCodeModel> list;
        String data;
        try {
            InputStream is = this.getAssets().open("country_code.json");
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

    private void saveData() {

        try {
            CustomerDetails userDetail = MidtransSDK.getInstance().getTransactionRequest().getCustomerDetails();

            if (userDetail != null) {
                Logger.i("UserAddressActivity", "userDetails:" + userDetail.getFirstName());
            }

            ShippingAddress userAddresses = userDetail.getShippingAddress();
            String address = etAddress.getText().toString().trim();
            String city = etCity.getText().toString().trim();
            String zipcode = etZipCode.getText().toString().trim();
            String country = etCountry.getText().toString().trim();

            if (userAddresses != null) {
                ShippingAddress userAddress = new ShippingAddress();
                userAddress.setAddress(address);
                userAddress.setCity(city);
                userAddress.setPostalCode(zipcode);
                if (isCountryCodeExist(country)) {
                    userAddress.setCountryCode(countryCodeSelected);
                } else {
                    Toast.makeText(this, "The country you choose is invalid. Please try again.", Toast.LENGTH_SHORT).show();
                    return;
                }
                userDetail.setShippingAddress(userAddress);
            }

            setResult(RESULT_OK);
            finish();
        } catch (RuntimeException e) {
            Logger.e("UserAddressActivity", "validateAndSaveAddress:" + e.getMessage());
        }

    }

    private boolean isValid() {
        return !TextUtils.isEmpty(etAddress.getText().toString().trim()) && !TextUtils.isEmpty(etCity.getText().toString().trim())
            && !TextUtils.isEmpty(etZipCode.getText().toString().trim()) && !TextUtils.isEmpty(etCountry.getText().toString().trim());
    }

    private boolean isCountryCodeExist(String countryName) {
        for (CountryCodeModel country : countryCodeList) {
            if (country.getName().equalsIgnoreCase(countryName)) {
                countryCodeSelected = country.getCountryCodeAlpha();
                return true;
            }
        }
        return false;
    }

    private String getCountryName(String countryCode) {
        for (CountryCodeModel country : countryCodeList) {
            if (country.getCountryCodeAlpha().equalsIgnoreCase(countryCode)) {
                return country.getName();
            }
        }
        return "";
    }
}
