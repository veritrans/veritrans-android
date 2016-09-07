package id.co.veritrans.sdk.uiflow.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;

import id.co.veritrans.sdk.uiflow.R;
import id.co.veritrans.sdk.uiflow.fragments.PaymentTransactionStatusFragment;
import com.midtrans.sdk.coreflow.models.TransactionResponse;

/**
 * Created by chetan on 01/12/15.
 */
public class NotificationActivity extends BaseActivity {
    private PaymentTransactionStatusFragment paymentStatusFragment;
    private FragmentManager fragmentManager;
    private String currentFragmentName;
    private TransactionResponse transactionResponse;
    private Toolbar toolbar;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        transactionResponse = (TransactionResponse) getIntent().getSerializableExtra(getString(R.string.payment_status));
        fragmentManager = getSupportFragmentManager();
        setContentView(R.layout.activity_notification);
        initializeTheme();
        toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null)
            getSupportActionBar().setTitle(getString(R.string.title_payment_status));
        setUpFragment();
    }

    private void setUpFragment() {
        // setup  fragment
        if (transactionResponse != null) {
            paymentStatusFragment = PaymentTransactionStatusFragment.newInstance(transactionResponse);
            replaceFragment(paymentStatusFragment, R.id.notification_container_layout, false, false);
        }
    }

}
