package com.midtrans.demo;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.TaskStackBuilder;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.midtrans.demo.widgets.DemoTextView;
import com.midtrans.sdk.corekit.callback.TransactionFinishedCallback;
import com.midtrans.sdk.corekit.core.Constants;
import com.midtrans.sdk.corekit.core.LocalDataHandler;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.UserAddress;
import com.midtrans.sdk.corekit.models.UserDetail;
import com.midtrans.sdk.corekit.models.snap.TransactionResult;
import com.midtrans.sdk.uikit.models.CountryCodeModel;
import com.midtrans.sdk.uikit.widgets.FancyButton;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by rakawm on 3/16/17.
 */

public class DemoOrderReviewActivity extends AppCompatActivity implements TransactionFinishedCallback,
        OnClickListener {

    private static final long DELAY = 300;
    private final int EDIT_SHIPPING_ADDRESS = 1;
    private UserDetail userDetail;
    private RelativeLayout amountContainer;
    private TextView amountText;
    private FancyButton payBtn;
    private Toolbar toolbar;
    private ImageView editCustBtn, saveCustBtn, cancelCustBtn, editDelivBtn;
    private TextInputEditText editName, editEmail, editPhone;
    private DemoTextView deliveryAddress, cityAddress, postalCodeAddress;
    private boolean isInEditMode;
    private ArrayList<CountryCodeModel> countryCodeList = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        retrieveCountryCode();
        setContentView(R.layout.activity_order_review);
        bindViews();
        bindData();
        bindAddress();
        bindThemes();
        initPayButton();
        initEditButton();
        initMidtransSDK();
    }

    private void bindViews() {
        payBtn = (FancyButton) findViewById(R.id.button_primary);
        amountContainer = (RelativeLayout) findViewById(R.id.amount_container);
        amountText = (TextView) findViewById(R.id.product_price_amount);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        editCustBtn = (ImageView) findViewById(R.id.button_edit_customer_detail);
        saveCustBtn = (ImageView) findViewById(R.id.button_save_customer_detail);
        cancelCustBtn = (ImageView) findViewById(R.id.button_cancel_customer_detail);
        editDelivBtn = (ImageView) findViewById(R.id.button_edit_delivery_address);
        editName = (TextInputEditText) findViewById(R.id.edit_customer_name);
        editEmail = (TextInputEditText) findViewById(R.id.edit_customer_email);
        editPhone = (TextInputEditText) findViewById(R.id.edit_customer_phone);
        deliveryAddress = (DemoTextView) findViewById(R.id.delivery_address);
        cityAddress = (DemoTextView) findViewById(R.id.city_address);
        postalCodeAddress = (DemoTextView) findViewById(R.id.zip_address);
    }

    private void bindData() {
        userDetail = LocalDataHandler.readObject(getString(R.string.user_details), UserDetail.class);
        if (userDetail != null) {
            editName.setText(userDetail.getUserFullName());
            editEmail.setText(userDetail.getEmail());
            editPhone.setText(userDetail.getPhoneNumber());
        }
    }

    private void bindAddress() {
        UserDetail userDetail = LocalDataHandler.readObject(getString(R.string.user_details), UserDetail.class);
        if (userDetail != null) {
            ArrayList<UserAddress> addresses = userDetail.getUserAddresses();
            if (addresses != null && !addresses.isEmpty()) {
                for (UserAddress address : addresses) {
                    if (address.getAddressType() == Constants.ADDRESS_TYPE_BOTH
                            || address.getAddressType() == Constants.ADDRESS_TYPE_SHIPPING) {
                        String countryName = getCountryFullName(address.getCountry());
                        if (countryName.length() == 0) {
                            deliveryAddress.setText(address.getAddress());
                        } else {
                            deliveryAddress.setText(address.getAddress() + ", " + countryName);
                        }
                        cityAddress.setText(getString(R.string.order_review_city, address.getCity()));
                        postalCodeAddress.setText(getString(R.string.order_review_postal_code, address.getZipcode()));
                    }
                }
            }
        }
    }

    private void bindThemes() {
        String themes = DemoPreferenceHelper.getStringPreference(this, DemoConfigActivity.COLOR_THEME);
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.back);
        if (themes != null && !TextUtils.isEmpty(themes)) {
            switch (themes) {
                case DemoThemeConstants.BLUE_THEME:
                    amountContainer.setBackgroundColor(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_HEX));
                    amountText.setTextColor(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_DARK_HEX));
                    payBtn.setBackgroundColor(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_HEX));
                    drawable.setColorFilter(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
                    break;
                case DemoThemeConstants.RED_THEME:
                    amountContainer.setBackgroundColor(Color.parseColor(DemoThemeConstants.RED_PRIMARY_HEX));
                    amountText.setTextColor(Color.parseColor(DemoThemeConstants.RED_PRIMARY_DARK_HEX));
                    payBtn.setBackgroundColor(Color.parseColor(DemoThemeConstants.RED_PRIMARY_DARK_HEX));
                    drawable.setColorFilter(Color.parseColor(DemoThemeConstants.RED_PRIMARY_HEX), PorterDuff.Mode.SRC_ATOP);
                    break;
                case DemoThemeConstants.GREEN_THEME:
                    amountContainer.setBackgroundColor(Color.parseColor(DemoThemeConstants.GREEN_PRIMARY_HEX));
                    amountText.setTextColor(Color.parseColor(DemoThemeConstants.GREEN_PRIMARY_DARK_HEX));
                    payBtn.setBackgroundColor(Color.parseColor(DemoThemeConstants.GREEN_PRIMARY_HEX));
                    drawable.setColorFilter(Color.parseColor(DemoThemeConstants.GREEN_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
                    break;
                case DemoThemeConstants.ORANGE_THEME:
                    amountContainer.setBackgroundColor(Color.parseColor(DemoThemeConstants.ORANGE_PRIMARY_HEX));
                    amountText.setTextColor(Color.parseColor(DemoThemeConstants.ORANGE_PRIMARY_DARK_HEX));
                    payBtn.setBackgroundColor(Color.parseColor(DemoThemeConstants.ORANGE_PRIMARY_HEX));
                    drawable.setColorFilter(Color.parseColor(DemoThemeConstants.ORANGE_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
                    break;
                case DemoThemeConstants.BLACK_THEME:
                    amountContainer.setBackgroundColor(Color.parseColor(DemoThemeConstants.BLACK_PRIMARY_HEX));
                    amountText.setTextColor(Color.parseColor(DemoThemeConstants.BLACK_PRIMARY_DARK_HEX));
                    payBtn.setBackgroundColor(Color.parseColor(DemoThemeConstants.BLACK_PRIMARY_HEX));
                    drawable.setColorFilter(Color.parseColor(DemoThemeConstants.BLACK_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
                    break;
                default:
                    amountContainer.setBackgroundColor(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_HEX));
                    amountText.setTextColor(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_DARK_HEX));
                    payBtn.setBackgroundColor(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_HEX));
                    drawable.setColorFilter(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_DARK_HEX), PorterDuff.Mode.SRC_ATOP);
                    break;
            }
        } else {
            amountContainer.setBackgroundColor(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_HEX));
            amountText.setTextColor(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_DARK_HEX));
            payBtn.setBackgroundColor(Color.parseColor(DemoThemeConstants.BLUE_PRIMARY_HEX));
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.slide_in_back, R.anim.slide_out_back);
    }

    private void initPayButton() {
        payBtn.setText(getString(R.string.btn_pay_20000));
        payBtn.setTextBold();
        payBtn.setOnClickListener(this);
    }

    private void initEditButton() {
        editCustBtn.setOnClickListener(this);
        saveCustBtn.setOnClickListener(this);
        cancelCustBtn.setOnClickListener(this);
        editDelivBtn.setOnClickListener(this);
    }

    private void initMidtransSDK() {
        if (MidtransSDK.getInstance() != null) {
            MidtransSDK.getInstance().setTransactionFinishedCallback(this);
        }
    }

    @Override
    public void onTransactionFinished(TransactionResult result) {
        if (result.getResponse() != null) {
            switch (result.getStatus()) {
                case TransactionResult.STATUS_SUCCESS:
                    Toast.makeText(this, "Transaction Finished. ID: " + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();
                    break;
                case TransactionResult.STATUS_PENDING:
                    Toast.makeText(this, "Transaction Pending. ID: " + result.getResponse().getTransactionId(), Toast.LENGTH_LONG).show();
                    break;
                case TransactionResult.STATUS_FAILED:
                    Toast.makeText(this, "Transaction Failed. ID: " + result.getResponse().getTransactionId() + ". Message: " + result.getResponse().getStatusMessage(), Toast.LENGTH_LONG).show();
                    break;
            }
            result.getResponse().getValidationMessages();
            Logger.d("xresult", "xresponse:" + result.getResponse().getFraudStatus());
        } else if (result.isTransactionCanceled()) {
            Toast.makeText(this, "Transaction Canceled", Toast.LENGTH_LONG).show();
        } else {
            if (result.getStatus().equalsIgnoreCase(TransactionResult.STATUS_INVALID)) {
                Toast.makeText(this, "Transaction Invalid : " + result.getStatusMessage(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(this, "Transaction Finished with failure.", Toast.LENGTH_LONG).show();
            }
        }

        Intent intent = new Intent(DemoOrderReviewActivity.this, DemoConfigActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(DemoOrderReviewActivity.this);
        taskStackBuilder.addNextIntent(intent);
        taskStackBuilder.startActivities();
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_edit_customer_detail:
                editCustBtn.setVisibility(View.GONE);
                saveCustBtn.setVisibility(View.VISIBLE);
                cancelCustBtn.setVisibility(View.VISIBLE);
                openField(true);
                break;
            case R.id.button_save_customer_detail:
                userDetail = LocalDataHandler.readObject(getString(R.string.user_details), UserDetail.class);
                if (userDetail != null) {
                    if (!isEmailValid(editEmail.getText().toString())) {
                        Toast.makeText(this, "Unable to save change(s). Please make sure the email is valid.", Toast.LENGTH_SHORT).show();
                    } else {
                        if (isValid()) {
                            userDetail.setUserFullName(editName.getText().toString().trim());
                            userDetail.setEmail(editEmail.getText().toString().trim());
                            userDetail.setPhoneNumber(editPhone.getText().toString().trim());
                            LocalDataHandler
                                    .saveObject(getString(R.string.user_details), userDetail);

                            openField(false);
                            editCustBtn.setVisibility(View.VISIBLE);
                            saveCustBtn.setVisibility(View.GONE);
                            cancelCustBtn.setVisibility(View.GONE);
                        } else {
                            Toast.makeText(this,
                                    "Unable to save change(s). Please make sure there's no empty field or discard change(s).",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
            case R.id.button_cancel_customer_detail:
                bindData();
                openField(false);
                editCustBtn.setVisibility(View.VISIBLE);
                saveCustBtn.setVisibility(View.GONE);
                cancelCustBtn.setVisibility(View.GONE);
                break;
            case R.id.button_edit_delivery_address:
                Intent intent = new Intent(DemoOrderReviewActivity.this, UserAddressActivity.class);
                startActivityForResult(intent, EDIT_SHIPPING_ADDRESS);
                break;
            case R.id.button_primary:
                if (isInEditMode) {
                    Toast.makeText(this, "Please save or cancel your information changes first!", Toast.LENGTH_SHORT).show();
                } else {
                    MidtransSDK.getInstance().startPaymentUiFlow(DemoOrderReviewActivity.this);
                    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                }
                break;
        }
    }

    private void openField(boolean isEditable) {
        if (isEditable) {
            isInEditMode = true;
            editName.setEnabled(true);
            editName.setFocusable(true);
            editName.setFocusableInTouchMode(true);
            editEmail.setEnabled(true);
            editEmail.setFocusable(true);
            editEmail.setFocusableInTouchMode(true);
            editPhone.setEnabled(true);
            editPhone.setFocusable(true);
            editPhone.setFocusableInTouchMode(true);
        } else {
            isInEditMode = false;
            editName.setEnabled(false);
            editName.setFocusable(false);
            editEmail.setEnabled(false);
            editEmail.setFocusable(false);
            editPhone.setEnabled(false);
            editPhone.setFocusable(false);
        }
    }

    private boolean isValid() {
        return !TextUtils.isEmpty(editName.getText().toString())
                && !TextUtils.isEmpty(editPhone.getText().toString());
    }

    private boolean isEmailValid(String email) {
        if (!TextUtils.isEmpty(email)) {
            Pattern pattern = Pattern.compile(Constants.EMAIL_PATTERN, Pattern.CASE_INSENSITIVE);
            if (pattern != null) {
                Matcher matcher = pattern.matcher(email.trim());
                return matcher.matches();
            }
        }
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            bindAddress();
        }
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

    private String getCountryFullName(String countryCode) {
        for (CountryCodeModel country : countryCodeList) {
            if (country.getCountryCodeAlpha().equalsIgnoreCase(countryCode)) {
                return country.getName();
            }
        }
        return "";
    }
}
