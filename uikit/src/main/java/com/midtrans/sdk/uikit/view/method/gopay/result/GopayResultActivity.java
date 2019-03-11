package com.midtrans.sdk.uikit.view.method.gopay.result;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.koushikdutta.ion.Ion;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.response.GopayResponse;
import com.midtrans.sdk.corekit.utilities.Logger;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.base.composer.BasePaymentActivity;
import com.midtrans.sdk.uikit.utilities.Constants;
import com.midtrans.sdk.uikit.utilities.DateTimeHelper;
import com.midtrans.sdk.uikit.widget.BoldTextView;
import com.midtrans.sdk.uikit.widget.DefaultTextView;
import com.midtrans.sdk.uikit.widget.FancyButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;

public class GopayResultActivity extends BasePaymentActivity {

    private static final String TAG = GopayResultActivity.class.getSimpleName();
    private final int DEFAULT_EXPIRATION_IN_MINUTE = 15;
    private BoldTextView expirationText;
    private BoldTextView merchantName;
    private ImageView qrCodeContainer;
    private FancyButton qrCodeRefresh;
    private DefaultTextView expirationDesc;
    private GopayResponse response;

    private boolean isInstructionShown = true;
    private boolean isAlertDialogShow = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_ewallet_gopay_result);
        bindData();
        initCompletePaymentButton();
    }

    protected void initCompletePaymentButton() {
        buttonCompletePayment.setOnClickListener(view -> onBackPressed());
    }

    @Override
    protected void initTheme() {
        setPrimaryBackgroundColor(buttonCompletePayment);
    }

    private void bindData() {
        response = (GopayResponse) getIntent().getSerializableExtra(Constants.INTENT_EXTRA_PAYMENT_STATUS);
        if (response != null) {
            showProgressLayout();

            final LinearLayout instructionLayout = findViewById(R.id.gopay_instruction_layout);
            merchantName = findViewById(R.id.gopay_merchant_name);
            final DefaultTextView instructionToggle = findViewById(R.id.gopay_instruction_toggle);
            instructionToggle.setOnClickListener(v -> {
                isInstructionShown = !isInstructionShown;
                if (isInstructionShown) {
                    instructionToggle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_chevron_up, 0);
                    instructionLayout.setVisibility(View.VISIBLE);
                } else {
                    instructionToggle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_chevron_down, 0);
                    instructionLayout.setVisibility(View.GONE);
                }
            });

            //process qr code
            final String qrCodeUrl = response.getQrCodeUrl();
            qrCodeContainer = findViewById(R.id.gopay_qr_code);
            qrCodeRefresh = findViewById(R.id.gopay_reload_qr_button);
            setTextColor(qrCodeRefresh);
            setIconColorFilter(qrCodeRefresh);
            qrCodeRefresh.setOnClickListener(v -> {
                showProgressLayout();
                loadQrCode(qrCodeUrl, qrCodeContainer);
            });
            loadQrCode(qrCodeUrl, qrCodeContainer);

            //process expiration
            expirationText = findViewById(R.id.gopay_expiration_text);
            expirationDesc = findViewById(R.id.gopay_expiration_desc);

            String startTime = response.getTransactionTime();
            if (isExpirationTimeNotAvailable(response) && TextUtils.isEmpty(startTime)) {
                expirationDesc.setVisibility(View.GONE);
                expirationText.setVisibility(View.GONE);
            } else {

                long duration = getPaymentDuration(response);
                if (duration > 1000) {
                    setTimer(duration);
                } else {
                    expirationText.setVisibility(View.GONE);
                    expirationDesc.setText(getString(R.string.gopay_expiration_expired));
                }
            }

            buttonCompletePayment.setText(getString(R.string.done));
            buttonCompletePayment.setTextBold();
        }
        setTitle(getString(R.string.gopay_status_title));
    }

    private boolean isExpirationTimeNotAvailable(GopayResponse response) {
        return TextUtils.isEmpty(response.getGopayExpiration()) && TextUtils.isEmpty(response.getGopayExpirationRaw());
    }

    private long getPaymentDuration(GopayResponse response) {
        String startTime = response.getTransactionTime();
        long expiryTimeOnMillis;

        if (TextUtils.isEmpty(response.getGopayExpirationRaw())) {
            String expirationTime = TextUtils.isEmpty(response.getGopayExpiration()) ? getExpiryTime(response.getTransactionTime()) : response.getGopayExpiration();
            expiryTimeOnMillis = getDuration(startTime, expirationTime);
        } else {
            expiryTimeOnMillis = getDurationByExpirationRaw(startTime, response.getGopayExpirationRaw());
        }

        return expiryTimeOnMillis;
    }

    private long getDurationByExpirationRaw(String startTime, String expirationTime) {
        long startMillis = 0, endMillis = 0;
        SimpleDateFormat startDf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);
        SimpleDateFormat expiryDf = new SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.US);

        try {
            Date dateStart = startDf.parse(startTime);
            Date dateEnd = expiryDf.parse(expirationTime);

            startMillis = dateStart.getTime();
            endMillis = dateEnd.getTime();

        } catch (ParseException e) {
            Logger.error(e.getMessage());
        }

        return endMillis - startMillis;
    }

    /**
     * A method for loading QR code based on url that is part of GO-PAY payment response
     * We use Glide listener in order to detect whether image downloading is good or not
     * If it is good, then display the QR code; else display reload button so this method is
     * called once again.
     *
     * @param url       location of QR code to be downloaded
     * @param container view to display the image
     */
    private void loadQrCode(String url, final ImageView container) {
        Ion.with(container).load(url).setCallback((e, result) -> {
            if (e == null) {
                FrameLayout frameLayout = findViewById(R.id.gopay_qr_code_frame);
                frameLayout.setBackgroundColor(0);
                qrCodeRefresh.setVisibility(View.GONE);
                setMerchantName(true);
                hideProgressLayout();
            } else {
                FrameLayout frameLayout = findViewById(R.id.gopay_qr_code_frame);
                frameLayout.setBackgroundColor(getResources().getColor(R.color.light_gray));
                qrCodeRefresh.setVisibility(View.VISIBLE);
                Logger.error(TAG, e.getMessage());
                setMerchantName(false);
                hideProgressLayout();
                Toast.makeText(GopayResultActivity.this, getString(R.string.error_qr_code), Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!isAlertDialogShow) {
            showConfirmationDialog(getString(R.string.confirm_gopay_qr_scan_tablet));
        } else {
            super.onBackPressed();
        }
    }

    private void showConfirmationDialog(String message) {
        try {
            isAlertDialogShow = true;
            AlertDialog dialog = new AlertDialog.Builder(GopayResultActivity.this, R.style.AlertDialogCustom)
                    .setPositiveButton(R.string.text_yes, (dialog1, which) -> {
                        if (!GopayResultActivity.this.isFinishing()) {
                            dialog1.dismiss();
                            setResult(RESULT_OK);
                            finish();
                            isAlertDialogShow = false;
                        }
                    })
                    .setNegativeButton(R.string.text_no, (dialog12, which) -> {
                        if (!GopayResultActivity.this.isFinishing()) {
                            dialog12.dismiss();
                            isAlertDialogShow = false;
                        }
                    })
                    .setTitle(R.string.cancel_transaction)
                    .setMessage(message)
                    .create();
            dialog.show();
        } catch (Exception e) {
            Logger.error(TAG, "showDialog:" + e.getMessage());
        }
    }

    private void setMerchantName(boolean isQrLoaded) {
        if (isQrLoaded) {
            if (paymentInfoResponse != null && !TextUtils.isEmpty(paymentInfoResponse.getMerchantData().getPreference().getDisplayName())) {
                merchantName.setText(paymentInfoResponse.getMerchantData().getPreference().getDisplayName());
                merchantName.setVisibility(View.VISIBLE);
            } else {
                merchantName.setVisibility(View.GONE);
            }
        } else {
            merchantName.setVisibility(View.GONE);
        }
    }

    /**
     * Get default expiry time for GO-PAY transaction
     *
     * @param transactionTime when transaction started
     * @return formatted time that already added with 15 minutes
     */
    private String getExpiryTime(String transactionTime) {

        if (transactionTime != null && transactionTime.split(" ").length > 1) {
            try {
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(simpleDateFormat.parse(transactionTime));
                calendar.add(Calendar.MINUTE, DEFAULT_EXPIRATION_IN_MINUTE);
                String date = simpleDateFormat.format(calendar.getTime());

                String time = date.split(" ")[1] + " WIB";

                String splitedDate[] = date.split(" ")[0].split("-");
                String month = DateTimeHelper.getMonth(Integer.parseInt(splitedDate[1]));

                return splitedDate[2] + " " + month + " " + splitedDate[0] + ", " + time;
            } catch (ParseException | RuntimeException ex) {
                Logger.error("Error while parsing date : " + ex.getMessage());
                return "";
            }
        }
        return transactionTime;
    }

    /**
     * Hack-ish approach for getting duration for the timer, as both start and end timestamp
     * are in different format.
     *
     * @param start transaction start time, example : 2018-02-09 18:14:52
     * @param end   GO-PAY expiry time, example : 09 February 18:29 WIB
     */
    private long getDuration(String start, String end) {
        long startMillis = 0, endMillis = 0;
        //use US locale so parser understands months in English (like February)
        SimpleDateFormat startDf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.US);
        SimpleDateFormat expiryDf = new SimpleDateFormat("dd MMMM HH:mm", Locale.US);
        Date date;
        try {
            date = startDf.parse(start);
            startMillis = date.getTime();

            //for end timestamp, we need to manually add year, same as start time year
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int year = cal.get(Calendar.YEAR);

            date = expiryDf.parse(end.replace(" WIB", ""));
            cal.setTime(date);
            cal.add(Calendar.YEAR, year - cal.get(Calendar.YEAR));
            date = cal.getTime();
            endMillis = date.getTime();
        } catch (ParseException e) {
            Logger.error(e.getMessage());
        }

        return endMillis - startMillis;
    }

    private void setTimer(long totalDurationInMillis) {
        if (expirationText != null && expirationDesc != null) {
            new CountDownTimer(totalDurationInMillis, 1000) {

                public void onTick(long millisUntilFinished) {
                    expirationText.setText(" " + DateTimeHelper.fromMillisToMinutes(GopayResultActivity.this, millisUntilFinished) + ".");
                }

                public void onFinish() {
                    expirationText.setVisibility(View.GONE);
                    expirationDesc.setText(getString(R.string.gopay_expiration_expired));
                }
            }.start();
        }
    }
}