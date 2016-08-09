package id.co.veritrans.sdk.uiflow.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import id.co.veritrans.sdk.coreflow.core.Constants;
import id.co.veritrans.sdk.coreflow.core.Logger;
import id.co.veritrans.sdk.coreflow.models.BankTransferModel;
import id.co.veritrans.sdk.uiflow.R;
import id.co.veritrans.sdk.uiflow.activities.BankTransferActivity;

/**
 * @author rakawm
 */
public class BankTransferListAdapter extends RecyclerView.Adapter<BankTransferListAdapter.BankTransferViewHolder>{
    private static final String TAG = BankTransferListAdapter.class.getSimpleName();

    private static Context sContext;
    private ArrayList<BankTransferModel> mData = null;

    public BankTransferListAdapter(Context context, ArrayList<BankTransferModel> data) {
        sContext = context;
        mData = data;
    }

    @Override
    public BankTransferViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_row_bank_transfer, parent, false);
        return new BankTransferViewHolder(view);
    }

    @Override
    public void onBindViewHolder(BankTransferViewHolder holder, int position) {
        holder.bankName.setText(mData.get(position).getBankName());
        holder.bankIcon.setImageResource(mData.get(position).getImage());
        Logger.d(TAG, "Bank Item: " + mData.get(position).getBankName());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public static class BankTransferViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        TextView bankName;
        ImageView bankIcon;

        public BankTransferViewHolder(View itemView) {
            super(itemView);
            bankName = (TextView)itemView.findViewById(R.id.text_bank_name);
            bankIcon = (ImageView)itemView.findViewById(R.id.img_bank_icon);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View v) {
            TextView nameText = (TextView) v.findViewById(R.id.text_bank_name);
            String name = nameText.getText().toString().trim();
            Activity activity = (Activity)sContext;
            if(name.equals(sContext.getString(R.string.bca_bank_transfer))) {
                Intent startBankPayment = new Intent(activity, BankTransferActivity.class);
                startBankPayment.putExtra(activity.getString(R.string.position),
                        Constants.BANK_TRANSFER_BCA);

                activity.startActivityForResult(startBankPayment,
                        Constants.RESULT_CODE_PAYMENT_TRANSFER);
            } else if(name.equals(sContext.getString(R.string.permata_bank_transfer))){
                Intent startBankPayment = new Intent(activity, BankTransferActivity.class);
                startBankPayment.putExtra(activity.getString(R.string.position),
                        Constants.BANK_TRANSFER_PERMATA);

                activity.startActivityForResult(startBankPayment,
                        Constants.RESULT_CODE_PAYMENT_TRANSFER);
            } else if (name.equals(sContext.getString(R.string.all_bank_transfer))) {
                Intent startOtherBankPayment = new Intent(activity, BankTransferActivity.class);
                startOtherBankPayment.putExtra(sContext.getString(R.string.position), Constants.PAYMENT_METHOD_BANK_TRANSFER_ALL_BANK);
                activity.startActivityForResult(startOtherBankPayment, Constants.RESULT_CODE_PAYMENT_TRANSFER);
            } else {
                showMessage();
            }
        }

        public void showMessage() {
            Toast.makeText(sContext, "This feature is not implemented yet.", Toast.LENGTH_SHORT).show();
        }
    }
}
