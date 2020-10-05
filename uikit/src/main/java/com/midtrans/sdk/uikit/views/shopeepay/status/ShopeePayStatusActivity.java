package com.midtrans.sdk.uikit.views.shopeepay.status;

import static com.midtrans.sdk.corekit.utilities.Utils.getMonth;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.abstracts.BasePaymentActivity;
import com.midtrans.sdk.uikit.utilities.TimeUtils;
import com.midtrans.sdk.uikit.widgets.BoldTextView;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;
import com.midtrans.sdk.uikit.widgets.FancyButton;
import com.midtrans.sdk.uikit.widgets.SemiBoldTextView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ShopeePayStatusActivity extends BasePaymentActivity {

    public static final String EXTRA_PAYMENT_STATUS = "extra.status";
    public static final String DATE_TIME_FORMAT_1 = "yyyy-MM-dd HH:mm";
    public static final String DATE_TIME_FORMAT_2= "dd MMMM HH:mm";
    public static final String DATE_TIME_FORMAT_FULL = "yyyy-MM-dd HH:mm:ss";
    public static final String SUFFIX_TIME_WIB = " WIB";
    private static final String TAG = ShopeePayStatusActivity.class.getSimpleName();
    private final int DEFAULT_EXPIRATION_IN_MINUTE = 15;
    private FancyButton buttonPrimary;
    private BoldTextView expirationText;
    private SemiBoldTextView textTitle;
    private BoldTextView merchantName;
    private ImageView qrCodeContainer;
    private FancyButton qrCodeRefresh;
    private DefaultTextView expirationDesc;
    private boolean isInstructionShown = true;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopeepay_status);
        bindData();
    }

    @Override
    public void onBackPressed() {
        if (isDetailShown) {
            displayOrHideItemDetails();
        } else {
            showConfirmationDialog(getString(R.string.confirm_shopeepay_qr_scan_tablet));
        }
    }

    private void bindData() {
        final TransactionResponse response = (TransactionResponse) getIntent().getSerializableExtra(EXTRA_PAYMENT_STATUS);
        if (response != null) {
            showProgressLayout();

            final LinearLayout instructionLayout = findViewById(R.id.shopeepay_instruction_layout);
            merchantName = findViewById(R.id.shopeepay_merchant_name);
            final DefaultTextView instructionToggle = findViewById(R.id.shopeepay_instruction_toggle);
            instructionToggle.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    isInstructionShown = !isInstructionShown;
                    if (isInstructionShown) {
                        instructionToggle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_chevron_up, 0);
                        instructionLayout.setVisibility(View.VISIBLE);
                    } else {
                        instructionToggle.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_chevron_down, 0);
                        instructionLayout.setVisibility(View.GONE);
                    }
                }
            });

            //process qr code
            final String qrCodeUrl = response.getQrisUrl();
            qrCodeContainer = findViewById(R.id.shopeepay_qr_code);
            qrCodeRefresh = findViewById(R.id.shopeepay_reload_qr_button);
            setTextColor(qrCodeRefresh);
            setIconColorFilter(qrCodeRefresh);
            qrCodeRefresh.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    showProgressLayout();
                    loadQrCode(qrCodeUrl, qrCodeContainer);
                }
            });
            loadQrCode(qrCodeUrl, qrCodeContainer);

            //process expiration
            expirationText = findViewById(R.id.shopeepay_expiration_text);
            expirationDesc = findViewById(R.id.shopeepay_expiration_desc);

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
                    expirationDesc.setText(getString(R.string.shopeepay_expiration_expired));
                }
            }

            buttonPrimary.setText(getString(R.string.done));
            buttonPrimary.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    showConfirmationDialog(getString(R.string.confirm_shopeepay_qr_scan_tablet));
                }
            });
            buttonPrimary.setTextBold();
        }
        textTitle.setText(getString(R.string.shopeepay_status_title));
    }

    private boolean isExpirationTimeNotAvailable(TransactionResponse response) {
        return TextUtils.isEmpty(response.getGopayExpiration()) && TextUtils.isEmpty(response.getGopayExpirationRaw());
    }

    private long getPaymentDuration(TransactionResponse response) {
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
        SimpleDateFormat startDf = new SimpleDateFormat(DATE_TIME_FORMAT_1, Locale.US);
        SimpleDateFormat expiryDf = new SimpleDateFormat(DATE_TIME_FORMAT_1, Locale.US);

        try {
            Date dateStart = startDf.parse(startTime);
            Date dateEnd = expiryDf.parse(expirationTime);

            startMillis = dateStart.getTime();
            endMillis = dateEnd.getTime();

        } catch (ParseException e) {
            Logger.e(e.getMessage());
        }

        return endMillis - startMillis;
    }

    @Override
    public void bindViews() {
        textTitle = findViewById(R.id.text_page_title);
        buttonPrimary = findViewById(R.id.button_primary);
    }

    @Override
    public void initTheme() {
        setPrimaryBackgroundColor(buttonPrimary);
    }

    /**
     * A method for loading QR code based on url that is part of GoPay payment response
     * We use Glide listener in order to detect whether image downloading is good or not
     * If it is good, then display the QR code; else display reload button so this method is
     * called once again.
     *
     * @param url       location of QR code to be downloaded
     * @param container view to display the image
     */
    private void loadQrCode(String url, final ImageView container) {
        Ion.with(container).load(url).setCallback(new FutureCallback<ImageView>() {
            @Override
            public void onCompleted(Exception e, ImageView result) {
                if (e == null) {
                    FrameLayout frameLayout = findViewById(R.id.shopeepay_qr_code_frame);
                    frameLayout.setBackgroundColor(0);
                    qrCodeRefresh.setVisibility(View.GONE);
                    setMerchantName(true);
                    hideProgressLayout();

                } else {
                    FrameLayout frameLayout = findViewById(R.id.shopeepay_qr_code_frame);
                    frameLayout.setBackgroundColor(getResources().getColor(R.color.light_gray));
                    qrCodeRefresh.setVisibility(View.VISIBLE);
                    Logger.e(TAG, e.getMessage());
                    setMerchantName(false);
                    hideProgressLayout();
                    Toast
                        .makeText(ShopeePayStatusActivity.this, getString(R.string.error_qr_code), Toast.LENGTH_SHORT)
                        .show();
                }
            }
        });
    }

    private void showConfirmationDialog(String message) {
        try {
            AlertDialog dialog = new AlertDialog.Builder(ShopeePayStatusActivity.this, R.style.AlertDialogCustom)
                .setPositiveButton(R.string.text_yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!ShopeePayStatusActivity.this.isFinishing()) {
                            dialog.dismiss();
                            finish();
                        }
                    }
                })
                .setNegativeButton(R.string.text_no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (!ShopeePayStatusActivity.this.isFinishing()) {
                            dialog.dismiss();
                        }
                    }
                })
                .setTitle(R.string.cancel_transaction)
                .setMessage(message)
                .create();
            dialog.show();
        } catch (Exception e) {
            Logger.e(TAG, "showDialog:" + e.getMessage());
        }
    }

    private void setMerchantName(boolean isQrLoaded) {
        if (isQrLoaded) {
            MidtransSDK midtransSDK = MidtransSDK.getInstance();
            if (midtransSDK != null && !TextUtils.isEmpty(midtransSDK.getMerchantName())) {
                merchantName.setText(midtransSDK.getMerchantName());
                merchantName.setVisibility(View.VISIBLE);
            } else {
                merchantName.setVisibility(View.GONE);
            }
        } else {
            merchantName.setVisibility(View.GONE);
        }
    }

    /**
     * Get default expiry time for GoPay transaction
     *
     * @param transactionTime when transaction started
     * @return formatted time that already added with 15 minutes
     */
    private String getExpiryTime(String transactionTime) {

        if (transactionTime != null && transactionTime.split(" ").length > 1) {
            try {
                @SuppressLint("SimpleDateFormat")
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_TIME_FORMAT_1);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(simpleDateFormat.parse(transactionTime));
                calendar.add(Calendar.MINUTE, DEFAULT_EXPIRATION_IN_MINUTE);
                String date = simpleDateFormat.format(calendar.getTime());

                String time = date.split(" ")[1] + SUFFIX_TIME_WIB;

                String[] splitedDate = date.split(" ")[0].split("-");
                String month = getMonth(Integer.parseInt(splitedDate[1]));

                return splitedDate[2] + " " + month + " " + splitedDate[0] + ", " + time;
            } catch (ParseException | RuntimeException ex) {
                Logger.e("Error while parsing date : " + ex.getMessage());
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
     * @param end   expiry time, example : 09 February 18:29 WIB
     * @return
     */
    private long getDuration(String start, String end) {
        long startMillis = 0, endMillis = 0;
        //use US locale so parser understands months in English (like February)
        SimpleDateFormat startDf = new SimpleDateFormat(DATE_TIME_FORMAT_FULL, Locale.US);
        SimpleDateFormat expiryDf = new SimpleDateFormat(DATE_TIME_FORMAT_2, Locale.US);
        Date date;
        try {
            date = startDf.parse(start);
            startMillis = date.getTime();

            //for end timestamp, we need to manually add year, same as start time year
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            int year = cal.get(Calendar.YEAR);

            date = expiryDf.parse(end.replace(SUFFIX_TIME_WIB, ""));
            cal.setTime(date);
            cal.add(Calendar.YEAR, year - cal.get(Calendar.YEAR));
            date = cal.getTime();
            endMillis = date.getTime();
        } catch (ParseException e) {
            Logger.e(e.getMessage());
        }

        return endMillis - startMillis;
    }

    private void setTimer(long totalDurationInMillis) {
        if (expirationText != null && expirationDesc != null) {
            new CountDownTimer(totalDurationInMillis, 1000) {

                @SuppressLint("SetTextI18n")
                public void onTick(long millisUntilFinished) {
                    expirationText.setText(" " + TimeUtils
                        .fromMillisToMinutes(ShopeePayStatusActivity.this, millisUntilFinished) + ".");
                }

                public void onFinish() {
                    expirationText.setVisibility(View.GONE);
                    expirationDesc.setText(getString(R.string.shopeepay_expiration_expired));
                }
            }.start();
        }
    }
}
