package id.co.veritrans.sdk.sample.core;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.UUID;

import id.co.veritrans.sdk.coreflow.core.Constants;
import id.co.veritrans.sdk.coreflow.core.LocalDataHandler;
import id.co.veritrans.sdk.coreflow.core.Logger;
import id.co.veritrans.sdk.coreflow.core.TransactionRequest;
import id.co.veritrans.sdk.coreflow.core.VeritransSDK;
import id.co.veritrans.sdk.coreflow.eventbus.bus.VeritransBusProvider;
import id.co.veritrans.sdk.coreflow.eventbus.callback.GetSnapTokenCallback;
import id.co.veritrans.sdk.coreflow.eventbus.callback.GetSnapTransactionCallback;
import id.co.veritrans.sdk.coreflow.eventbus.events.snap.GetSnapTokenFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.snap.GetSnapTokenSuccessEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.snap.GetSnapTransactionFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.snap.GetSnapTransactionSuccessEvent;
import id.co.veritrans.sdk.coreflow.models.BillInfoModel;
import id.co.veritrans.sdk.coreflow.models.BillingAddress;
import id.co.veritrans.sdk.coreflow.models.CustomerDetails;
import id.co.veritrans.sdk.coreflow.models.ItemDetails;
import id.co.veritrans.sdk.coreflow.models.ShippingAddress;
import id.co.veritrans.sdk.coreflow.models.UserAddress;
import id.co.veritrans.sdk.coreflow.models.UserDetail;
import id.co.veritrans.sdk.sample.BCAPaymentActivity;
import id.co.veritrans.sdk.sample.CIMCBClickPaymentActivity;
import id.co.veritrans.sdk.sample.CreditCardPaymentActivity;
import id.co.veritrans.sdk.sample.IndomaretPaymentActivity;
import id.co.veritrans.sdk.sample.MandiriBillPaymentActivity;
import id.co.veritrans.sdk.sample.MandiriClickPaymentActivity;
import id.co.veritrans.sdk.sample.PermataVAPaymentActivity;
import id.co.veritrans.sdk.sample.R;
import id.co.veritrans.sdk.sample.utils.RecyclerItemClickListener;
/**
 * @author rakawm
 */
public class CoreFlowActivity extends AppCompatActivity implements GetSnapTokenCallback, GetSnapTransactionCallback {
    private RecyclerView coreMethods;
    private CoreFlowListAdapter adapter;
    private ProgressDialog dialog;
    private ArrayList<String> bankList = new ArrayList<>();
    private ArrayList<CoreViewModel> paymentMethodList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_core_flow);
        VeritransBusProvider.getInstance().register(this);
        dialog = new ProgressDialog(this);

        init();
        getpaymentPages();

    }

    private void getpaymentPages() {
        dialog.setIndeterminate(true);
        dialog.setMessage("get payment methods");
        dialog.show();

        String orderId = UUID.randomUUID().toString();
        TransactionRequest request = new TransactionRequest(orderId, 360000);
        // Set item details
        ItemDetails itemDetails = new ItemDetails("1", 360000, 1, "shoes");
        ArrayList<ItemDetails> itemDetailsArrayList = new ArrayList<>();
        itemDetailsArrayList.add(itemDetails);
        request.setItemDetails(itemDetailsArrayList);
        // Set Bill Info
        request.setBillInfoModel(new BillInfoModel("Bill Info Sample", "Bill Info Sample 2"));

//        //set user detail (optional)
//        UserDetail userDetail = new UserDetail();
//        userDetail.setUserFullName("Costumer Name");
//        userDetail.setEmail("sample@email.com");
//        userDetail.setPhoneNumber("085310102020");
//
//        ArrayList<UserAddress> addressList = new ArrayList<>();
//        UserAddress address = new UserAddress();
//        address.setCountry("IDN");
//        address.setAddress("Sample Alamat");
//        address.setZipcode("12312");
//        address.setCity("Yogyakarta");
//        addressList.add(address);
//        userDetail.setUserAddresses(addressList);
//
//        CustomerDetails details = new CustomerDetails(userDetail.getUserFullName(), null,
//                userDetail.getEmail(), userDetail.getPhoneNumber());
//
//        BillingAddress billingAddress = new BillingAddress(userDetail.getUserFullName(), null,
//                address.getAddress(), address.getCity(), address.getZipcode(), userDetail.getPhoneNumber(),
//                address.getCountry());
//        details.setBillingAddress(billingAddress);
//
//        ShippingAddress shippingAddress = new ShippingAddress();
//        shippingAddress.setAddress(address.getAddress());
//        shippingAddress.setCity(address.getCity());
//        shippingAddress.setCountryCode(address.getCountry());
//        shippingAddress.setFirstName(userDetail.getUserFullName());
//        shippingAddress.setPhone(userDetail.getPhoneNumber());
//        shippingAddress.setPostalCode(address.getZipcode());
//        details.setBillingAddress(billingAddress);
//        request.setCustomerDetails(details);
        // Set transaction request
        VeritransSDK.getVeritransSDK().setTransactionRequest(request);

        VeritransSDK.getVeritransSDK().getSnapToken();
    }

    private void init() {
        coreMethods = (RecyclerView) findViewById(R.id.core_flow_list);

        // Init recycler view
        coreMethods.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CoreFlowListAdapter();
        coreMethods.setAdapter(adapter);
        coreMethods.addOnItemTouchListener(new RecyclerItemClickListener(this, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                CoreViewModel coreViewModel = adapter.getItem(position);
                if(coreViewModel.getTitle().equalsIgnoreCase(getString(R.string.credit_card))){
                    goToActivity(new Intent(getApplicationContext(), CreditCardPaymentActivity.class));
                }else if(coreViewModel.getTitle().equalsIgnoreCase(getString(R.string.klik_bca))){

                }else if(coreViewModel.getTitle().equals(getString(R.string.mandiri_click_pay))){

                }else if(coreViewModel.getTitle().equals(getString(R.string.payment_bca_click))){

                }else if(coreViewModel.getTitle().equals(getString(R.string.cimb_clicks))){

                }else if(coreViewModel.getTitle().equals(getString(R.string.epay_bri))){

                }else if(coreViewModel.getTitle().equals(getString(R.string.epay_bri))){

                }else if(coreViewModel.getTitle().equals(getString(R.string.indomaret))){

                }else if(coreViewModel.getTitle().equals(getString(R.string.payment_method_mandiri_ecash))){

                }else if(coreViewModel.getTitle().equals(getString(R.string.mandiri_bill_payment))){

                }else if(coreViewModel.getTitle().equals(getString(R.string.indosat_dompetku))){

                }
                switch (coreViewModel.getImage()) {
                    case R.drawable.ic_credit:
                        goToActivity(new Intent(getApplicationContext(), CreditCardPaymentActivity.class));
                        break;
                    case R.drawable.ic_bca:
                        goToActivity(new Intent(getApplicationContext(), BCAPaymentActivity.class));
                        break;
                    case R.drawable.ic_permata:
                        goToActivity(new Intent(getApplicationContext(), PermataVAPaymentActivity.class));
                        break;
                    case R.drawable.ic_mandiri_bill_payment2:
                        goToActivity(new Intent(getApplicationContext(), MandiriBillPaymentActivity.class));
                        break;
                    case R.drawable.ic_mandiri2:
                        goToActivity(new Intent(getApplicationContext(), MandiriClickPaymentActivity.class));
                        break;
                    case R.drawable.ic_cimb:
                        goToActivity(new Intent(getApplicationContext(), CIMCBClickPaymentActivity.class));
                        break;
                    case R.drawable.ic_indomaret:
                        goToActivity(new Intent(getApplicationContext(), IndomaretPaymentActivity.class));
                        break;
                    default:
                        break;
                }
            }
        }));
    }

    private CoreViewModel generateCoreViewModels(String name) {
        if (name.equals(getString(R.string.label_credit_card))) {
           return (new CoreViewModel(getString(R.string.credit_card), R.mipmap.ic_credit_sample));
        } else if (name.equals(getString(R.string.label_bank_transfer))) {
           return (new CoreViewModel(getString(R.string.payment_bank_transfer), R.mipmap.ic_atm_sample));
        } else if (name.equals(getString(R.string.label_bca_klik_bca))) {
           return (new CoreViewModel(getString(R.string.klik_bca),R.mipmap.ic_klikbca_sample));
        } else if (name.equals(getString(R.string.label_mandiri_clickpay))) {
           return (new CoreViewModel(getString(R.string.mandiri_click_pay),R.mipmap.ic_mandiri_clickpay_sample));
        } else if (name.equals(getString(R.string.label_bca_klikpay))) {
           return (new CoreViewModel(getString(R.string.bca_klikpay),R.mipmap.ic_klikbca_sample));
        } else if (name.equals(getString(R.string.label_cimbclick))) {
           return (new CoreViewModel(getString(R.string.cimb_clicks),R.mipmap.ic_cimb_sample));
        } else if (name.equals(getString(R.string.label_bri_epay))) {
           return (new CoreViewModel(getString(R.string.epay_bri),R.mipmap.bri_epay_sample));
        } else if (name.equals(getString(R.string.label_payment_indomaret))) {
           return (new CoreViewModel(getString(R.string.indomaret),R.mipmap.ic_indomaret_sample));
        } else if (name.equals(getString(R.string.label_payment_mandiri_ecash))) {
           return (new CoreViewModel(getString(R.string.payment_method_mandiri_ecash),R.mipmap.ic_mandiri_e_cash_sample));
        } else if (name.equals(getString(R.string.label_mandiri_bill))) {
           return (new CoreViewModel(getString(R.string.mandiri_bill_payment),R.mipmap.ic_mandiri_bill_payment2_sample));
        } else if (name.equals(getString(R.string.label_indosat_dompetku))) {
           return (new CoreViewModel(getString(R.string.indosat_dompetku),R.mipmap.ic_indosat_sample));
        }
        else{
            return null;
        }

    }

    private void goToActivity(Intent intent) {
        startActivity(intent);
    }

    @Subscribe
    @Override
    public void onEvent(GetSnapTokenSuccessEvent event) {
        Logger.i("snaptoken>success");
        String token = event.getResponse().getTokenId();
        LocalDataHandler.saveString(Constants.AUTH_TOKEN, token);
        VeritransSDK.getVeritransSDK().getSnapTransaction(token);
    }

    @Subscribe
    @Override
    public void onEvent(GetSnapTokenFailedEvent event) {
        Logger.i("snaptoken>error");

        if(dialog.isShowing()){
            dialog.dismiss();
        }
        adapter.clear();
        showAlertDialog(event.getMessage());
    }

    @Subscribe
    @Override
    public void onEvent(GetSnapTransactionSuccessEvent event) {
        Logger.i("snaptransaction>success");
        if(dialog.isShowing()){
            dialog.dismiss();
        }
        paymentMethodList.clear();
        for(String method : event.getResponse().getTransactionData().getEnabledPayments()){
            if(method.equalsIgnoreCase(getString(R.string.label_bank_transfer))){
                for(String bank : event.getResponse().getTransactionData().getBankTransfer().getBanks()){
                    paymentMethodList.add(new CoreViewModel(bank, (R.mipmap.ic_atm_sample)));
                }
            }else{
                paymentMethodList.add(generateCoreViewModels(method));
            }
        }

        adapter.setData(paymentMethodList);
        adapter.notifyDataSetChanged();
    }

    @Subscribe
    @Override
    public void onEvent(GetSnapTransactionFailedEvent event) {
        Logger.i("snaptransaction>error");
        if(dialog.isShowing()){
            dialog.dismiss();
        }

    }

    private void showAlertDialog(String message){
            AlertDialog alert = new AlertDialog.Builder(this)
                    .setMessage(message)
                    .setPositiveButton(id.co.veritrans.sdk.uiflow.R.string.btn_retry, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            getpaymentPages();
                        }
                    })
                    .setNegativeButton(id.co.veritrans.sdk.uiflow.R.string.btn_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            finish();
                        }
                    })
                    .create();
            alert.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (VeritransBusProvider.getInstance().isRegistered(this)) {
            VeritransBusProvider.getInstance().unregister(this);
        }
    }
}
