package com.midtrans.sdk.uiflow.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import com.midtrans.sdk.coreflow.core.Logger;
import com.midtrans.sdk.coreflow.models.BankTransferModel;
import com.midtrans.sdk.uiflow.R;

/**
 * @author rakawm
 */
public class BankTransferListAdapter extends RecyclerView.Adapter<BankTransferListAdapter.BankTransferViewHolder>{
    private static final String TAG = BankTransferListAdapter.class.getSimpleName();

    private ArrayList<BankTransferModel> mData = new ArrayList<>();
    private BankTransferAdapterListener listener;

    public BankTransferModel getItem(int position) {
        return mData.get(position);
    }

    public interface BankTransferAdapterListener{
        public void onItemClick(int position);
    }
    public BankTransferListAdapter(BankTransferAdapterListener listener, ArrayList<BankTransferModel> data) {
        this.listener = listener;
        this.mData.clear();
        this.mData.addAll(data);
    }

    @Override
    public BankTransferViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_row_bank_transfer, parent, false);
        return new BankTransferViewHolder(view, listener);
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


    public static class BankTransferViewHolder extends RecyclerView.ViewHolder {
        TextView bankName;
        ImageView bankIcon;

        public BankTransferViewHolder(View itemView, final BankTransferAdapterListener listener) {
            super(itemView);
            bankName = (TextView)itemView.findViewById(R.id.text_bank_name);
            bankIcon = (ImageView)itemView.findViewById(R.id.img_bank_icon);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        listener.onItemClick(getAdapterPosition());
                    }
                }
            });
        }

    }
}
