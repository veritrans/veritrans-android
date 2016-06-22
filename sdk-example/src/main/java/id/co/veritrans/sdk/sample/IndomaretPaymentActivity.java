package id.co.veritrans.sdk.sample;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.UUID;

import id.co.veritrans.sdk.coreflow.core.TransactionRequest;
import id.co.veritrans.sdk.coreflow.core.VeritransSDK;
import id.co.veritrans.sdk.coreflow.eventbus.bus.VeritransBusProvider;
import id.co.veritrans.sdk.coreflow.eventbus.callback.TransactionBusCallback;
import id.co.veritrans.sdk.coreflow.eventbus.events.GeneralErrorEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.NetworkUnavailableEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.TransactionFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.TransactionSuccessEvent;
import id.co.veritrans.sdk.coreflow.models.BillInfoModel;
import id.co.veritrans.sdk.coreflow.models.CstoreEntity;
import id.co.veritrans.sdk.coreflow.models.ItemDetails;

public class IndomaretPaymentActivity extends AppCompatActivity implements TransactionBusCallback {
    Button payBtn;
    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indomaret_payment);
        // Register this class into event bus
        VeritransBusProvider.getInstance().register(this);
        initView();
    }

    @Override
    protected void onDestroy() {
        // Unregister this class into event bus
        VeritransBusProvider.getInstance().unregister(this);
        super.onDestroy();
    }

    private void initView() {
        //Initialize progress dialog
        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setMessage("Loading");


        payBtn = (Button) findViewById(R.id.btn_payment);
        payBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show progress dialog
                dialog.show();
                // Create transaction request
                String orderId = UUID.randomUUID().toString();
                TransactionRequest request = new TransactionRequest(orderId, 360000);
                // Set item details
                ItemDetails itemDetails = new ItemDetails("1", 360000, 1, "shoes");
                ArrayList<ItemDetails> itemDetailsArrayList = new ArrayList<>();
                itemDetailsArrayList.add(itemDetails);
                request.setItemDetails(itemDetailsArrayList);
                // bill info
                BillInfoModel billInfoModel = new BillInfoModel("demo_label", "demo_value");
                request.setBillInfoModel(billInfoModel);
                // Set transaction request
                VeritransSDK.getVeritransSDK().setTransactionRequest(request);
                // Do payment
                CstoreEntity entity = new CstoreEntity();
                entity.setMessage("Sample message");
                entity.setStore("Indomaret Store");
                VeritransSDK.getVeritransSDK().paymentUsingIndomaret(entity);
            }
        });
    }

    @Subscribe
    @Override
    public void onEvent(TransactionSuccessEvent transactionSuccessEvent) {
        // Handle success transaction
        dialog.dismiss();
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage("Payment is Successful")
                .create();
        dialog.show();
    }

    @Subscribe
    @Override
    public void onEvent(TransactionFailedEvent transactionFailedEvent) {
        // Handle failed transaction
        dialog.dismiss();
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(transactionFailedEvent.getMessage())
                .create();
        dialog.show();
    }

    @Subscribe
    @Override
    public void onEvent(NetworkUnavailableEvent networkUnavailableEvent) {
        // Handle network not available condition
        dialog.dismiss();
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage("Network is unavailable")
                .create();
        dialog.show();
    }

    @Subscribe
    @Override
    public void onEvent(GeneralErrorEvent generalErrorEvent) {
        // Handle generic error condition
        dialog.dismiss();
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage("Unknown error: " + generalErrorEvent.getMessage())
                .create();
        dialog.show();
    }
}
