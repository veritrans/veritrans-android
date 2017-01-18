package com.midtrans.sdk.uikit.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.models.BankTransferModel;
import com.midtrans.sdk.uikit.R;

import java.util.ArrayList;

/**
 * @author rakawm
 */
public class BankTransferListAdapter extends RecyclerView.Adapter<BankTransferListAdapter.BankTransferViewHolder> {
    private static final String TAG = BankTransferListAdapter.class.getSimpleName();

    private ArrayList<BankTransferModel> mData = new ArrayList<>();
    private BankTransferAdapterListener listener;

    public BankTransferListAdapter(BankTransferAdapterListener listener) {
        this.listener = listener;
    }

    public void setData(ArrayList<BankTransferModel> banktransfers){
        this.mData.clear();
        this.mData.addAll(banktransfers);
        this.notifyDataSetChanged();
    }

    public BankTransferModel getItem(int position) {
        return mData.get(position);
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
        holder.bankDescription.setText(mData.get(position).getDescription());
        Logger.d(TAG, "Bank Item: " + mData.get(position).getBankName());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface BankTransferAdapterListener {
        void onItemClick(int position);
    }

    class BankTransferViewHolder extends RecyclerView.ViewHolder {
        TextView bankName;
        TextView bankDescription;
        ImageView bankIcon;

        BankTransferViewHolder(View itemView, final BankTransferAdapterListener listener) {
            super(itemView);
            bankName = (TextView) itemView.findViewById(R.id.text_bank_name);
            bankIcon = (ImageView) itemView.findViewById(R.id.img_bank_icon);
            bankDescription = (TextView) itemView.findViewById(R.id.text_bank_description);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onItemClick(getAdapterPosition());
                    }
                }
            });
        }

    }
}
