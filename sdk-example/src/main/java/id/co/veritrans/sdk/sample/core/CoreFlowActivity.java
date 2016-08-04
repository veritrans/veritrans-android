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
import id.co.veritrans.sdk.coreflow.models.ItemDetails;
import id.co.veritrans.sdk.sample.BCAKlikPayActivity;
import id.co.veritrans.sdk.sample.BankTransferPaymentActivity;
import id.co.veritrans.sdk.sample.CIMBClickPayPaymentActivity;
import id.co.veritrans.sdk.sample.CreditCardPaymentActivity;
import id.co.veritrans.sdk.sample.EpayBRIPaymentActivity;
import id.co.veritrans.sdk.sample.IndomaretPaymentActivity;
import id.co.veritrans.sdk.sample.KlikBcaPaymentActivity;
import id.co.veritrans.sdk.sample.MandiriBillPaymentActivity;
import id.co.veritrans.sdk.sample.MandiriClickPaymentActivity;
import id.co.veritrans.sdk.sample.R;
import id.co.veritrans.sdk.sample.utils.RecyclerItemClickListener;
/**
 * @author rakawm
 */
public class CoreFlowActivity extends AppCompatActivity implements GetSnapTokenCallback, GetSnapTransactionCallback {
    private static final int REQ_PAYMENT = 12;
    private RecyclerView coreMethods;
    private CoreFlowListAdapter adapter;
    private ProgressDialog dialog;
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
        //checkout to merchant server
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
                if(coreViewModel.getTitle().equalsIgnoreCase(getString(R.string.name_credit_card))){
                    goToActivity(new Intent(getApplicationContext(), CreditCardPaymentActivity.class));
                }else if(coreViewModel.getTitle().equalsIgnoreCase(getString(R.string.name_bca_klikpay))){
                    goToActivity(new Intent(getApplicationContext(), BCAKlikPayActivity.class));
                }else if(coreViewModel.getTitle().equalsIgnoreCase(getString(R.string.name_klik_bca))){
                    goToActivity(new Intent(getApplicationContext(), KlikBcaPaymentActivity.class));
                }else if(coreViewModel.getTitle().equals(getString(R.string.name_epay_bri))){
                    goToActivity(new Intent(getApplicationContext(), EpayBRIPaymentActivity.class));
                }else if(coreViewModel.getTitle().equals(getString(R.string.name_cimb_clicks))){
                    goToActivity(new Intent(getApplicationContext(), CIMBClickPayPaymentActivity.class));
                }else if(coreViewModel.getTitle().equals(getString(R.string.name_mandiri_click_pay))){
                    goToActivity(new Intent(getApplicationContext(), MandiriClickPaymentActivity.class));
                }else if(coreViewModel.getTitle().equals(getString(R.string.name_mandiri_bill_payment))){
                    goToActivity(new Intent(getApplicationContext(), MandiriBillPaymentActivity.class));
                }else if(coreViewModel.getTitle().equals(getString(R.string.name_indomaret))){
                    goToActivity(new Intent(getApplicationContext(), IndomaretPaymentActivity.class));
                }else if(coreViewModel.getTitle().equals(getString(R.string.name_bank_transfer_bca))){
                    Intent intent = new Intent(getApplicationContext(), BankTransferPaymentActivity.class);
                    intent.putExtra(BankTransferPaymentActivity.TRANSFER_TYPE, getString(R.string.label_bank_transfer_bca));
                    goToActivity(intent);
                }else if(coreViewModel.getTitle().equals(getString(R.string.name_bank_transfer_permata))){
                    Intent intent = new Intent(getApplicationContext(), BankTransferPaymentActivity.class);
                    intent.putExtra(BankTransferPaymentActivity.TRANSFER_TYPE, getString(R.string.label_bank_transfer_permata));
                    goToActivity(intent);
                }else if(coreViewModel.getTitle().equals(getString(R.string.name_bank_transfer_mandiri))){
                    Intent intent = new Intent(getApplicationContext(), BankTransferPaymentActivity.class);
                    intent.putExtra(BankTransferPaymentActivity.TRANSFER_TYPE, getString(R.string.label_bank_transfer_mandiri));
                    goToActivity(intent);
                }else if(coreViewModel.getTitle().equals(getString(R.string.name_bank_transfer_others))){
                    Intent intent = new Intent(getApplicationContext(), BankTransferPaymentActivity.class);
                    intent.putExtra(BankTransferPaymentActivity.TRANSFER_TYPE, getString(R.string.label_bank_transfer_other));
                    goToActivity(intent);
                }
            }
        }));
    }

    private CoreViewModel generateCoreViewModels(String name) {
        if (name.equals(getString(R.string.label_credit_card))) {
           return (new CoreViewModel(getString(R.string.name_credit_card), R.mipmap.ic_credit_sample));
        } else if (name.equals(getString(R.string.label_bca_klikpay))) {
            return (new CoreViewModel(getString(R.string.name_bca_klikpay),R.mipmap.ic_klikbca_sample));
        }  else if (name.equals(getString(R.string.label_bca_klik_bca))) {
           return (new CoreViewModel(getString(R.string.name_klik_bca),R.mipmap.ic_klikbca_sample));
        } else if (name.equals(getString(R.string.label_bri_epay))) {
            return (new CoreViewModel(getString(R.string.name_epay_bri),R.mipmap.bri_epay_sample));
        }else if (name.equals(getString(R.string.label_cimbclick))) {
            return (new CoreViewModel(getString(R.string.name_cimb_clicks),R.mipmap.ic_cimb_sample));
        } else if (name.equals(getString(R.string.label_mandiri_clickpay))) {
           return (new CoreViewModel(getString(R.string.name_mandiri_click_pay),R.mipmap.ic_mandiri_clickpay_sample));
        } else if (name.equals(getString(R.string.label_mandiri_bill))) {
            return (new CoreViewModel(getString(R.string.name_mandiri_bill_payment),R.mipmap.ic_mandiri_bill_payment2_sample));
        } else if (name.equals(getString(R.string.label_payment_indomaret))) {
           return (new CoreViewModel(getString(R.string.name_indomaret),R.mipmap.ic_indomaret_sample));
        } else if (name.equals(getString(R.string.label_payment_mandiri_ecash))) {
           return (new CoreViewModel(getString(R.string.name_payment_method_mandiri_ecash),R.mipmap.ic_mandiri_e_cash_sample));
        } else if (name.equals(getString(R.string.label_indosat_dompetku))) {
           return (new CoreViewModel(getString(R.string.name_indosat_dompetku),R.mipmap.ic_indosat_sample));
        }
        else{
            return null;
        }
    }

    private void goToActivity(Intent intent) {
        startActivityForResult(intent, REQ_PAYMENT);
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
        if(dialog.isShowing()){
            dialog.dismiss();
        }
        paymentMethodList.clear();
        for(String method : event.getResponse().getTransactionData().getEnabledPayments()){
            if(method.equalsIgnoreCase(getString(R.string.label_bank_transfer))){
                for(String bank : event.getResponse().getTransactionData().getBankTransfer().getBanks()){
                    //mandiri bank tranfer & other bank transfer unsupported yet
                    paymentMethodList.add(generateBankViewModels(bank));
                }
            }else{
                paymentMethodList.add(generateCoreViewModels(method));
            }
        }

        adapter.setData(paymentMethodList);
        adapter.notifyDataSetChanged();
    }

    private CoreViewModel generateBankViewModels(String bank) {
        if(bank.equals(getString(R.string.label_bank_transfer_bca))){
            return new CoreViewModel(getString(R.string.name_bank_transfer_bca), R.drawable.ic_atm);
        }else if(bank.equals(getString(R.string.label_bank_transfer_permata))){
            return new CoreViewModel(getString(R.string.name_bank_transfer_permata), R.drawable.ic_atm);
        }else if(bank.equals(getString(R.string.label_bank_transfer_mandiri))){
            return new CoreViewModel(getString(R.string.name_bank_transfer_mandiri), R.drawable.ic_atm);
        }else{
            return new CoreViewModel(getString(R.string.name_bank_transfer_others), R.drawable.ic_atm);
        }
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == REQ_PAYMENT && resultCode == RESULT_OK){
            finish();
        }
    }
}
