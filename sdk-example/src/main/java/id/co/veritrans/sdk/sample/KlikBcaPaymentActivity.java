package id.co.veritrans.sdk.sample;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import org.greenrobot.eventbus.Subscribe;

import id.co.veritrans.sdk.coreflow.core.VeritransSDK;
import id.co.veritrans.sdk.coreflow.eventbus.bus.VeritransBusProvider;
import id.co.veritrans.sdk.coreflow.eventbus.callback.TransactionBusCallback;
import id.co.veritrans.sdk.coreflow.eventbus.events.GeneralErrorEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.NetworkUnavailableEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.TransactionFailedEvent;
import id.co.veritrans.sdk.coreflow.eventbus.events.TransactionSuccessEvent;

public class KlikBcaPaymentActivity extends AppCompatActivity implements TransactionBusCallback{

    private String sampleUserId = "userid";

    ProgressDialog dialog;
    private Button buttonPay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        VeritransBusProvider.getInstance().register(this);
        setContentView(R.layout.activity_base_payment_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.title_activity_klik_bca_payment));

        dialog = new ProgressDialog(this);
        dialog.setIndeterminate(true);
        dialog.setMessage("Loading");

        buttonPay = (Button) findViewById(R.id.btn_payment);
        buttonPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
                VeritransSDK.getVeritransSDK().snapPaymentUsingKlikBCA(
                        VeritransSDK.getVeritransSDK().readAuthenticationToken(),
                        sampleUserId
                );
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        VeritransBusProvider.getInstance().unregister(this);
    }

    @Subscribe
    @Override
    public void onEvent(TransactionSuccessEvent transactionSuccessEvent) {
        // Handle success transaction
        dialog.dismiss();
        Toast.makeText(this, "transaction successfull (" + transactionSuccessEvent.getResponse().getStatusMessage() + ")", Toast.LENGTH_LONG).show();
        setResult(RESULT_OK);
        finish();
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
                .setMessage(R.string.no_network)
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
