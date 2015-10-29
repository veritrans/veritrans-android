package id.co.veritrans.sdk.utilities;

/**
 * Created by shivam on 10/26/15.
 */

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Base64;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;

import id.co.veritrans.sdk.core.Constants;
import id.co.veritrans.sdk.core.Logger;


/**
 * Created by chetan on 14/05/15.
 */
public class Utils {

    public static boolean isNetworkAvailable(Context context) {
        try {
            ConnectivityManager connManager = (ConnectivityManager) context.getSystemService
                    (Context.CONNECTIVITY_SERVICE);
            if (connManager.getActiveNetworkInfo() != null && connManager.getActiveNetworkInfo()
                    .isAvailable() && connManager.getActiveNetworkInfo().isConnected()) {
                return true;
            }
        } catch (Exception ex) {

            ex.printStackTrace();
            return false;
        }
        return false;
    }


    public static void hideKeyboard(Context context, View editText) {
        try {
            InputMethodManager imm = (InputMethodManager) context.getSystemService(Context
                    .INPUT_METHOD_SERVICE);
            if (editText != null) {
                imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
                editText.clearFocus();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void CheckDwnloadStatus(DownloadManager downloadManager, Activity activity,
                                          long id) {

        // TODO Auto-generated method stub
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(id);
        Cursor cursor = downloadManager.query(query);
        if (cursor.moveToFirst()) {
            int columnIndex = cursor.getColumnIndex(DownloadManager.COLUMN_STATUS);
            int status = cursor.getInt(columnIndex);
            int columnReason = cursor.getColumnIndex(DownloadManager.COLUMN_REASON);
            int reason = cursor.getInt(columnReason);

            switch (status) {
                case DownloadManager.STATUS_FAILED:
                    String failedReason = "";
                    switch (reason) {
                        case DownloadManager.ERROR_CANNOT_RESUME:
                            failedReason = "ERROR_CANNOT_RESUME";
                            break;
                        case DownloadManager.ERROR_DEVICE_NOT_FOUND:
                            failedReason = "ERROR_DEVICE_NOT_FOUND";
                            break;
                        case DownloadManager.ERROR_FILE_ALREADY_EXISTS:
                            failedReason = "ERROR_FILE_ALREADY_EXISTS";
                            break;
                        case DownloadManager.ERROR_FILE_ERROR:
                            failedReason = "ERROR_FILE_ERROR";
                            break;
                        case DownloadManager.ERROR_HTTP_DATA_ERROR:
                            failedReason = "ERROR_HTTP_DATA_ERROR";
                            break;
                        case DownloadManager.ERROR_INSUFFICIENT_SPACE:
                            failedReason = "ERROR_INSUFFICIENT_SPACE";
                            break;
                        case DownloadManager.ERROR_TOO_MANY_REDIRECTS:
                            failedReason = "ERROR_TOO_MANY_REDIRECTS";
                            break;
                        case DownloadManager.ERROR_UNHANDLED_HTTP_CODE:
                            failedReason = "ERROR_UNHANDLED_HTTP_CODE";
                            break;
                        case DownloadManager.ERROR_UNKNOWN:
                            failedReason = "ERROR_UNKNOWN";
                            break;
                        default:
                            failedReason = "unknown reason";
                            break;
                    }

                    Toast.makeText(activity,
                            "FAILED: " + failedReason,
                            Toast.LENGTH_LONG).show();
                    break;
                case DownloadManager.STATUS_PAUSED:
                    String pausedReason = "";

                    switch (reason) {
                        case DownloadManager.PAUSED_QUEUED_FOR_WIFI:
                            pausedReason = "PAUSED_QUEUED_FOR_WIFI";
                            break;
                        case DownloadManager.PAUSED_UNKNOWN:
                            pausedReason = "PAUSED_UNKNOWN";
                            break;
                        case DownloadManager.PAUSED_WAITING_FOR_NETWORK:
                            pausedReason = "PAUSED_WAITING_FOR_NETWORK";
                            break;
                        case DownloadManager.PAUSED_WAITING_TO_RETRY:
                            pausedReason = "PAUSED_WAITING_TO_RETRY";
                            break;
                    }

                    Toast.makeText(activity,
                            "PAUSED: " + pausedReason,
                            Toast.LENGTH_LONG).show();
                    break;
                case DownloadManager.STATUS_PENDING:
                    Toast.makeText(activity,
                            "PENDING",
                            Toast.LENGTH_LONG).show();
                    break;
                case DownloadManager.STATUS_RUNNING:
                    Toast.makeText(activity,
                            "RUNNING",
                            Toast.LENGTH_LONG).show();
                    break;
                case DownloadManager.STATUS_SUCCESSFUL:

                    Toast.makeText(activity,
                            "SUCCESSFUL",
                            Toast.LENGTH_LONG).show();
                    break;
            }
        }
    }


    public static long startDownload(String uri, Activity activity, DownloadManager
            downloadManager, String directoryName) {

        if (uri != null) {
            Uri downloadUri = Uri.parse(uri);
            String fileName = downloadUri.getLastPathSegment();
            Logger.d("file uri is %s ", "" + downloadUri + " . " + uri);
            Logger.d("download directory path is %s ", "" + Environment.DIRECTORY_DOWNLOADS);
            Logger.d("file name  is %s ", "" + fileName);

            try {
                DownloadManager.Request request = new DownloadManager.Request(downloadUri);
                //request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_MOBILE);
                request.setAllowedOverRoaming(false);
                request.setTitle(fileName);
                //request.setDestinationInExternalFilesDir(activity, path, "" + fileName);
                request.setNotificationVisibility(DownloadManager.Request
                        .VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
                String path = Constants.DIR_APP + File.separator + "" + directoryName;
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, path +
                        File.separator + fileName + ".mp4");
                return downloadManager.enqueue(request);
            } catch (IllegalStateException ex) {
                ex.printStackTrace();
            }
        }
        return 0;
    }

    public static String getFormattedAmount(double amount) {
        try {
            return new DecimalFormat("##,##,##0").format(amount);
        } catch (NumberFormatException e) {
            return "" + amount;
        } catch (NullPointerException e) {
            return "" + amount;
        }
    }


    public static String calculateBase64(final String data) {

        try {

            final byte[] authBytes = data.getBytes("UTF-8");
            final String encoded = Base64.encodeToString(authBytes, Base64.DEFAULT);

            Logger.i("base64 of "+data+" : becomes : "+encoded);

            return encoded;

        } catch (UnsupportedEncodingException ex) {
            Logger.e("" + ex.getMessage());
        }

        return null;
    }
}