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
 * Created by ziahaqi on 12/27/16.
 */

public class SavedCardsAdapter extends RecyclerView.Adapter<SavedCardsAdapter.SavedCardsViewHolder> {
    private static final String TAG = BankTransferListAdapter.class.getSimpleName();

    private ArrayList<BankTransferModel> mData = new ArrayList<>();
    private SavedCardAdapterEventListener listener;

    public interface SavedCardAdapterEventListener {
        void onItemClick(int position);
    }

    public SavedCardsAdapter(SavedCardsAdapter.SavedCardAdapterEventListener listener) {
        this.listener = listener;
    }

    public void setData(ArrayList<BankTransferModel> banktransfers) {
        this.mData.clear();
        this.mData.addAll(banktransfers);
        this.notifyDataSetChanged();
    }

    public BankTransferModel getItem(int position) {
        return mData.get(position);
    }

    @Override
    public SavedCardsAdapter.SavedCardsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_row_saved_cards, parent, false);
        return new SavedCardsAdapter.SavedCardsViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(SavedCardsAdapter.SavedCardsViewHolder holder, int position) {
        holder.textCardName.setText(mData.get(position).getBankName());
        holder.textCardNumber.setText();
        holder.te.setImageResource(mData.get(position).getImage());
        Logger.d(TAG, "Bank Item: " + mData.get(position).getBankName());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }


    public static class SavedCardsViewHolder extends RecyclerView.ViewHolder {
        TextView textCardName, textCardNumber;
        ImageView imageCardType, imageCardOffer;

        public SavedCardsViewHolder(View itemView, final SavedCardsAdapter.SavedCardAdapterEventListener listener) {
            super(itemView);
            textCardName = (TextView) itemView.findViewById(R.id.text_saved_card_name);
            textCardNumber = (TextView) itemView.findViewById(R.id.text_saved_card_number);
            imageCardType = (ImageView) itemView.findViewById(R.id.image_card_type);
            imageCardOffer = (ImageView) itemView.findViewById(R.id.image_card_offer);

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
