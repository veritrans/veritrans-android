package com.midtrans.sdk.ui.views.ebanking.klikbca;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.midtrans.sdk.ui.R;
import com.midtrans.sdk.ui.abtracts.BaseStatusActivity;
import com.midtrans.sdk.ui.widgets.FancyButton;

/**
 * Created by rakawm on 4/7/17.
 */

public class KlikBcaStatusActivity extends BaseStatusActivity {
    public static final String EXTRA_VALIDITY = "klikbca.validity";
    private TextView expireText;

    private FancyButton seeInstructionsButton;

    private String validity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_klikbca_status);
        initExtras();
        initViews();
        initThemes();
        initValues();
        initSeeInstructionsButton();
    }

    private void initExtras() {
        validity = getIntent().getStringExtra(EXTRA_VALIDITY);
    }

    private void initViews() {
        seeInstructionsButton = (FancyButton) findViewById(R.id.btn_see_instruction);
        expireText = (TextView) findViewById(R.id.expire_text);
    }

    private void initThemes() {
        setBorderColor(seeInstructionsButton);
        setTextColor(seeInstructionsButton);
    }

    private void initValues() {
        expireText.setText(validity);
    }

    @Override
    public void onBackPressed() {
        finishPayment();
    }

    private void initSeeInstructionsButton() {
        seeInstructionsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startKlikBcaInstructions();
            }
        });
    }

    private void startKlikBcaInstructions() {
        Intent intent = new Intent(this, KlikBcaInstructionActivity.class);
        startActivity(intent);
    }

    @Override
    protected void finishPayment() {
        setResult(RESULT_OK);
        finish();
    }
}
