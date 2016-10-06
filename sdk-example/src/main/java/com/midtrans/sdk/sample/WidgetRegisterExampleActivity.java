package com.midtrans.sdk.sample;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.midtrans.sdk.corekit.models.SaveCardResponse;
import com.midtrans.sdk.widgets.CreditCardRegisterForm;

/**
 * Created by rakawm on 9/13/16.
 */
public class WidgetRegisterExampleActivity extends AppCompatActivity {

    private static final String randomUserId = "random-user-id-example";
    private CreditCardRegisterForm creditCardForm;
    private Button getToken;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_example_widget_register);
        dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.loading));
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);

        creditCardForm = (CreditCardRegisterForm) findViewById(R.id.credit_card_form);
        creditCardForm.setMidtransClientKey(BuildConfig.CLIENT_KEY);
        creditCardForm.setMerchantUrl(BuildConfig.BASE_URL);
        creditCardForm.setUserId(randomUserId);

        getToken = (Button) findViewById(R.id.btn_get_token);
        getToken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (creditCardForm.checkCardValidity()) {
                    dialog.show();
                    creditCardForm.register(new CreditCardRegisterForm.WidgetSaveCardCallback() {
                        @Override
                        public void onSucceed(SaveCardResponse transactionResponse) {
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Response message: " + transactionResponse.getMessage(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onFailed(Throwable throwable) {
                            dialog.dismiss();
                            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
}
