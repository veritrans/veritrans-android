package com.midtrans.sdk.uikit.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.midtrans.sdk.corekit.models.SaveCardRequest;
import com.midtrans.sdk.corekit.utilities.Utils;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;

import java.util.ArrayList;

/**
 * Created by ziahaqi on 12/27/16.
 */

public class SavedCardsAdapter extends RecyclerView.Adapter<SavedCardsAdapter.SavedCardsViewHolder> {
    private static final String TAG = SavedCardsAdapter.class.getSimpleName();

    private ArrayList<SaveCardRequest> mData = new ArrayList<>();
    private SavedCardAdapterEventListener listener;

    public interface SavedCardAdapterEventListener {
        void onItemClick(int position);
    }

    public SavedCardsAdapter(SavedCardsAdapter.SavedCardAdapterEventListener listener) {
        this.listener = listener;
    }

    public void setData(ArrayList<SaveCardRequest> cards) {
        this.mData.clear();
        this.mData.addAll(cards);
        this.notifyDataSetChanged();
    }

    public SaveCardRequest getItem(int position) {
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
        SaveCardRequest card = mData.get(position);
        String maskedCard = card.getMaskedCard();
        String cardType = Utils.getCardType(maskedCard);

        switch (cardType) {
            case Utils.CARD_TYPE_VISA:
                holder.imageCardType.setImageResource(R.drawable.ic_visa);
                break;
            case Utils.CARD_TYPE_MASTERCARD:
                holder.imageCardType.setImageResource(R.drawable.ic_mastercard);
                break;
            case Utils.CARD_TYPE_JCB:
                holder.imageCardType.setImageResource(R.drawable.ic_jcb);
                break;
            case Utils.CARD_TYPE_AMEX:
                holder.imageCardType.setImageResource(R.drawable.ic_amex);
                break;
        }
        String cardName = maskedCard.substring(0, 4);
        holder.textCardName.setText(cardType + "-" + cardName);
        String cardNumber = SdkUIFlowUtil.getMaskedCardNumber(maskedCard);
        holder.textCardNumber.setText(cardNumber);
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
