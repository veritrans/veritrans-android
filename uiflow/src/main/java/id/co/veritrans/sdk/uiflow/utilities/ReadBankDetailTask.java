package id.co.veritrans.sdk.uiflow.utilities;

import android.content.Context;
import android.os.AsyncTask;

import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import id.co.veritrans.sdk.coreflow.core.LocalDataHandler;
import id.co.veritrans.sdk.coreflow.core.Logger;
import id.co.veritrans.sdk.coreflow.models.BankDetail;
import id.co.veritrans.sdk.coreflow.models.BankDetailArray;
import id.co.veritrans.sdk.coreflow.models.UserDetail;
import id.co.veritrans.sdk.uiflow.R;

/**
 * Created by ziahaqi on 17/06/2016.
 */
public class ReadBankDetailTask extends AsyncTask<Void, Void ,ArrayList<BankDetail>>{
    private final ReadBankDetailCallback callback;
    private Context context;
    private UserDetail userDetail;

    public interface ReadBankDetailCallback{
        void onReadBankDetailsFinish(ArrayList<BankDetail> bankDetails, UserDetail userDetail);
    }

    public ReadBankDetailTask(Context context, ReadBankDetailCallback callback) {
        this.context = context;
        this.callback = callback;
    }


    @Override
    protected ArrayList<BankDetail> doInBackground(Void... params) {
        try {
            this.userDetail = LocalDataHandler.readObject(context.getString(R.string.user_details), UserDetail.class);
            Logger.i("userDetail:" + userDetail.getUserFullName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        ArrayList<BankDetail> bankDetails = new ArrayList<>();
        try {
            bankDetails = LocalDataHandler.readObject(context.getString(R.string.bank_details), BankDetailArray.class).getBankDetails();
            Logger.i("bankDetails:" + bankDetails.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (bankDetails.isEmpty()) {
            String json = null;
            try {
                InputStream is = context.getAssets().open("bank_details.json");
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                json = new String(buffer, "UTF-8");
                Logger.i("json:" + json);
            } catch (IOException ex) {
                ex.printStackTrace();

            }

            try {
                Gson gson = new Gson();
                bankDetails = gson.fromJson(json, BankDetailArray.class).getBankDetails();
                Logger.i("bankDetails>fromfile:" + bankDetails.size());

                LocalDataHandler.saveObject(context.getString(R.string.bank_details), bankDetails);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        return bankDetails;
    }

    @Override
    protected void onPostExecute(ArrayList<BankDetail> bankDetails) {
        super.onPostExecute(bankDetails);
        if(callback != null){
            callback.onReadBankDetailsFinish(bankDetails, userDetail);
        }
    }
}
