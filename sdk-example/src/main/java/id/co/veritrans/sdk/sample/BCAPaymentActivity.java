package id.co.veritrans.sdk.sample;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import id.co.veritrans.sdk.coreflow.callback.TransactionCallback;
import id.co.veritrans.sdk.coreflow.core.VeritransSDK;
import id.co.veritrans.sdk.coreflow.models.TransactionResponse;

public class BCAPaymentActivity extends AppCompatActivity{
    Button payBtn;
    ProgressDialog dialog;
    private String sampleEmail = "test@veritrans.co.id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bcapayment);
        initView();
    }

    @Override
    protected void onDestroy() {
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
                VeritransSDK.getInstance().snapPaymentUsingBankTransferBCA(
                        VeritransSDK.getInstance().readAuthenticationToken(),
                        sampleEmail, new TransactionCallback() {
                            @Override
                            public void onSuccess(TransactionResponse response) {
                                actionTransactionSuccess(response);
                            }

                            @Override
                            public void onFailure(TransactionResponse response, String reason) {
                                actionTransactionFailure(response, reason);
                            }

                            @Override
                            public void onError(Throwable error) {
                                actionTransactionError(error);
                            }
                        });
            }
        });
    }

    private void actionTransactionError(Throwable error) {
        dialog.dismiss();
        AlertDialog dialog = new AlertDialog.Builder(BCAPaymentActivity.this)
                .setMessage("Unknown error: " + error.getMessage())
                .create();
        dialog.show();
    }

    private void actionTransactionFailure(TransactionResponse response, String reason) {
        dialog.dismiss();
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(reason)
                .create();
        dialog.show();
    }

    private void actionTransactionSuccess(TransactionResponse response) {
        // Handle success transaction
        dialog.dismiss();
        Toast.makeText(this, "transaction successfull (" + response.getStatusMessage() + ")", Toast.LENGTH_LONG).show();
        setResult(RESULT_OK);
        finish();
    }
}
