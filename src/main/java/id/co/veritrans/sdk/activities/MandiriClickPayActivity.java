package id.co.veritrans.sdk.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;

import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.callbacks.TransactionCallback;
import id.co.veritrans.sdk.core.Constants;
import id.co.veritrans.sdk.core.Logger;
import id.co.veritrans.sdk.core.SdkUtil;
import id.co.veritrans.sdk.core.StorageDataHandler;
import id.co.veritrans.sdk.core.VeritransSDK;
import id.co.veritrans.sdk.fragments.MandiriClickPayFragment;
import id.co.veritrans.sdk.models.BillingAddress;
import id.co.veritrans.sdk.models.CustomerDetails;
import id.co.veritrans.sdk.models.MandiriClickPayModel;
import id.co.veritrans.sdk.models.MandiriClickPayRequestModel;
import id.co.veritrans.sdk.models.ShippingAddress;
import id.co.veritrans.sdk.models.TransactionDetails;
import id.co.veritrans.sdk.models.TransactionResponse;
import id.co.veritrans.sdk.models.UserAddress;
import id.co.veritrans.sdk.models.UserDetail;
import id.co.veritrans.sdk.widgets.TextViewFont;

/**
 * Created by shivam on 11/3/15.
 */
public class MandiriClickPayActivity extends AppCompatActivity implements View.OnClickListener {


    private MandiriClickPayFragment mMandiriClickPayFragment = null;
    private Button mButtonConfirmPayment = null;
    private Toolbar mToolbar = null;
    private TextViewFont mTextViewAppli = null;
    private TextViewFont mTextViewInput1 = null;
    private TextViewFont mTextViewInput2 = null;
    private TextViewFont mTextViewInput3 = null;
    private VeritransSDK mVeritransSDK = null;

    private ArrayList<BillingAddress> mBillingAddressArrayList = new ArrayList<>();
    private ArrayList<ShippingAddress> mShippingAddressArrayList = new ArrayList<>();
    private CustomerDetails mCustomerDetails = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mandiri_clickpay);

        mVeritransSDK = VeritransSDK.getVeritransSDK();

        if (mVeritransSDK == null) {
            SdkUtil.showSnackbar(MandiriClickPayActivity.this, Constants
                    .ERROR_SDK_IS_NOT_INITIALIZED);
            finish();
        }


        initializeViews();
        bindDataToView();
        setUpFragment();

    }


    private void initializeViews() {

        mButtonConfirmPayment = (Button) findViewById(R.id.btn_confirm_payment);
        mToolbar = (Toolbar) findViewById(R.id.main_toolbar);
        mTextViewInput1 = (TextViewFont) findViewById(R.id.text_input_1);
        mTextViewInput2 = (TextViewFont) findViewById(R.id.text_input_2);
        mTextViewInput3 = (TextViewFont) findViewById(R.id.text_input_3);

        mToolbar.setTitle("");
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mButtonConfirmPayment.setOnClickListener(this);
    }

    private void bindDataToView() {
        mTextViewInput1.setText("");
        mTextViewInput2.setText("" + mVeritransSDK.getTransactionRequest().getAmount());
        mTextViewInput3.setText("" + SdkUtil.generateRandomNumber());
    }

    private void setUpFragment() {

        // setup  fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        mMandiriClickPayFragment = new MandiriClickPayFragment();

        fragmentTransaction.add(R.id.mandiri_clickpay_container,
                mMandiriClickPayFragment);
        fragmentTransaction.commit();
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return false;
    }

    public void onChangeOfDebitCardNumer(CharSequence text) {

        if (text != null && text.length() > 0) {
            String cardNumber = text.toString().trim().replace(" ", "");

            if (cardNumber.length() > 10) {
                mTextViewInput1.setText(cardNumber.substring(cardNumber.length() - 10,
                        cardNumber.length()));
            } else {
                mTextViewInput1.setText(cardNumber);
            }
        } else {
            mTextViewInput1.setText("");
        }
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.btn_confirm_payment) {
            validateInformation();
        }
    }


    private void validateInformation() {

        if (mMandiriClickPayFragment != null && !mMandiriClickPayFragment.isDetached()) {

            String challengeToken = mMandiriClickPayFragment.getChallengeToken();
            String debitCardNumber = mMandiriClickPayFragment.getDebitCardNumber();

            if ( !TextUtils.isEmpty(challengeToken) && !TextUtils.isEmpty(debitCardNumber)) {

                debitCardNumber = debitCardNumber.replace(" ", "");

                if (debitCardNumber.length() < 16 || !SdkUtil.isValidCardNumber(debitCardNumber)) {
                    SdkUtil.showSnackbar(MandiriClickPayActivity.this,
                            getString(R.string.validation_message_invalid_card_no));
                }else if ( challengeToken.trim().length() != 6 ){
                    SdkUtil.showSnackbar(MandiriClickPayActivity.this,
                            getString(R.string.validation_message_invalid_token_no));
                }
                else {

                    MandiriClickPayModel mandiriClickPayModel = new MandiriClickPayModel();
                    mandiriClickPayModel.setCardNumber(debitCardNumber);
                    mandiriClickPayModel.setInput1(mTextViewInput1.getText().toString());
                    mandiriClickPayModel.setInput2(mTextViewInput2.getText().toString());
                    mandiriClickPayModel.setInput3(mTextViewInput3.getText().toString());
                    mandiriClickPayModel.setToken(challengeToken);

                    makeTransaction(mandiriClickPayModel);

                }

            } else {
                SdkUtil.showSnackbar(
                        MandiriClickPayActivity.this,
                        "Please fill up information properly."
                );
            }

        } else {
            SdkUtil.showSnackbar(MandiriClickPayActivity.this, Constants
                    .ERROR_SOMETHING_WENT_WRONG);
            finish();
        }

    }

    private void makeTransaction(MandiriClickPayModel mandiriClickPayModel) {

        getUserDetails();
        int amount = 100;

        //transaction details
        TransactionDetails transactionDetails =
                new TransactionDetails( "" + amount, mVeritransSDK.getTransactionRequest().getOrderId());

        MandiriClickPayRequestModel mandiriClickPayRequestModel =
                new MandiriClickPayRequestModel(mandiriClickPayModel, transactionDetails, null,
                        mBillingAddressArrayList, mShippingAddressArrayList,
                        mCustomerDetails);


        mVeritransSDK.paymentUsingMandiriClickPay(MandiriClickPayActivity.this,
                mandiriClickPayRequestModel, new TransactionCallback() {

            @Override
            public void onFailure(String errorMessage,TransactionResponse transactionResponse) {
                Toast.makeText(getApplicationContext(), "failed : " + errorMessage, Toast
                        .LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(TransactionResponse transactionResponse) {
                Toast.makeText(getApplicationContext(), "success", Toast.LENGTH_SHORT).show();
            }
        });


    }


    private void getUserDetails() {

        UserDetail userDetail = null;

        try {
            userDetail = (UserDetail) StorageDataHandler.readObject(getApplicationContext(),
                    Constants.USER_DETAILS);

            if (userDetail != null && !TextUtils.isEmpty(userDetail.getUserFullName())) {

                ArrayList<UserAddress> userAddresses = userDetail.getUserAddresses();
                if (userAddresses != null && !userAddresses.isEmpty()) {

                    Logger.i("Found " + userAddresses.size() + " user addresses.");

                    mCustomerDetails = new CustomerDetails();
                    mCustomerDetails.setPhone(userDetail.getPhoneNumber());
                    mCustomerDetails.setFirstName(userDetail.getUserFullName());
                    mCustomerDetails.setLastName(" ");
                    mCustomerDetails.setEmail("");

                    //add email which is entered in editText
                    //mCustomerDetails.setEmail(userDetail.getEmail());


                    extractUserAddress(userDetail, userAddresses);
                }

            } else {
                SdkUtil.showSnackbar(MandiriClickPayActivity.this, "User details not available.");
                finish();
            }
        } catch (ClassNotFoundException ex) {
            Logger.e("Error while fetching user details : " + ex.getMessage());
        } catch (IOException ex) {
            Logger.e("Error while fetching user details : " + ex.getMessage());
        }
    }


    private void extractUserAddress(UserDetail userDetail, ArrayList<UserAddress> userAddresses) {

        for (int i = 0; i < userAddresses.size(); i++) {

            UserAddress userAddress = userAddresses.get(i);

            if (userAddress.getAddressType() == Constants.ADDRESS_TYPE_BILLING) {
                BillingAddress billingAddress = new BillingAddress();
                billingAddress.setCity(userAddress.getCity());
                billingAddress.setFirstName(userDetail.getUserFullName());
                billingAddress.setLastName("");
                billingAddress.setPhone(userDetail.getPhoneNumber());
                billingAddress.setCountryCode(userAddress.getCountry());
                billingAddress.setPostalCode(userAddress.getZipcode());
                mBillingAddressArrayList.add(billingAddress);
            } else {

                ShippingAddress shippingAddress = new ShippingAddress();
                shippingAddress.setCity(userAddress.getCity());
                shippingAddress.setFirstName(userDetail.getUserFullName());
                shippingAddress.setLastName("");
                shippingAddress.setPhone(userDetail.getPhoneNumber());
                shippingAddress.setCountryCode(userAddress.getCountry());
                shippingAddress.setPostalCode(userAddress.getZipcode());
                mShippingAddressArrayList.add(shippingAddress);

            }

        }
    }

}