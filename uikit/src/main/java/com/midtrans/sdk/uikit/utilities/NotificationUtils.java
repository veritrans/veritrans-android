package com.midtrans.sdk.uikit.utilities;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;

import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.activities.NotificationActivity;

/**
 * Created by chetan on 31/12/15.
 */
public class NotificationUtils {
    /**
     * this constants are keys which we receive in notification data
     */
    public static final String KEY_STATUS_MESSAGE = "sm";
    public static final String KEY_TRANSACTION_ID = "ti";
    public static final String KEY_ORDER_ID = "oi";
    public static final String KEY_GROSS_AMOUNT = "ga";
    public static final String KEY_PAYMENT_TYPE = "pt";
    public static final String KEY_TRANSACTION_TIME = "tt";
    public static final String KEY_TRANSACTION_STATUS = "ts";
    public static final String KEY_STATUS_CODE = "sc";
    public static final String PREF_NOTIFICATION_COUNT = "pref_notification_count";

    /**
     * Create and show a notification containing the received notification message.
     *
     * @param context application context
     * @param data    message received from notification.
     */
    public static void sendNotification(Context context, Bundle data) {
        String statusCode = data.getString(KEY_STATUS_CODE, "");
        String statusMessage = data.getString(KEY_STATUS_MESSAGE, "");
        String transactionId = data.getString(KEY_TRANSACTION_ID, "");
        String orderId = data.getString(KEY_ORDER_ID, "");
        String grossAmount = data.getString(KEY_GROSS_AMOUNT, "");
        String paymentType = data.getString(KEY_PAYMENT_TYPE, "");
        String transactionTime = data.getString(KEY_TRANSACTION_TIME, "");
        String transactionStatus = data.getString(KEY_TRANSACTION_STATUS, "");
        TransactionResponse transactionResponse = new TransactionResponse(statusCode, statusMessage,
                transactionId, orderId, grossAmount, paymentType, transactionTime, transactionStatus);
        Intent intent = new Intent(context, NotificationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(context.getString(R.string.payment_status), transactionResponse);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentText(statusMessage)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        SharedPreferences sharedPreferences =
                PreferenceManager.getDefaultSharedPreferences(context);
        int notificationId = sharedPreferences.getInt(PREF_NOTIFICATION_COUNT, 0);
        notificationManager.notify(++notificationId, notificationBuilder.build());
        Logger.i("notificationId:" + notificationId);
        sharedPreferences.edit().putInt(PREF_NOTIFICATION_COUNT, notificationId).apply();
    }
}
