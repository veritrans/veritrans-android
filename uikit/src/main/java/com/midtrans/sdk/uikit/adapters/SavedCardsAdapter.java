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
import com.midtrans.sdk.uikit.models.PromoData;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.widgets.AspectRatioImageView;

import java.util.ArrayList;

/**
 * Created by ziahaqi on 12/27/16.
 */

public class SavedCardsAdapter extends RecyclerView.Adapter<SavedCardsAdapter.SavedCardsViewHolder> {
    private static final String TAG = SavedCardsAdapter.class.getSimpleName();

    private ArrayList<SaveCardRequest> mData = new ArrayList<>();
    private ArrayList<PromoData> promoDatas = new ArrayList<>();

    private SavedCardAdapterEventListener listener;
    private SavedCardPromoListener promoListener;

    public SavedCardsAdapter() {
    }

    public ArrayList<PromoData> getPromoDatas() {
        return promoDatas;
    }

    public void setPromoDatas(ArrayList<PromoData> promoDatas) {
        this.promoDatas = promoDatas;
    }

    public ArrayList<SaveCardRequest> getData() {
        return mData;
    }

    public void setData(ArrayList<SaveCardRequest> cards) {
        this.mData.clear();
        this.mData.addAll(cards);
        this.notifyDataSetChanged();
    }

    public void setListener(SavedCardAdapterEventListener listener) {
        this.listener = listener;
    }

    public SavedCardPromoListener getPromoListener() {
        return promoListener;
    }

    public void setPromoListener(SavedCardPromoListener promoListener) {
        this.promoListener = promoListener;
    }

    public SaveCardRequest getItem(int position) {
        return mData.get(position);
    }

    @Override
    public SavedCardsAdapter.SavedCardsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_row_saved_cards, parent, false);
        return new SavedCardsAdapter.SavedCardsViewHolder(view);
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

        PromoData promoData = promoDatas.get(position);
        if (promoData != null && promoData.getPromoResponse() != null) {
            holder.imageCardOffer.setVisibility(View.VISIBLE);
        } else {
            holder.imageCardOffer.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface SavedCardAdapterEventListener {
        void onItemClick(int position);
    }


    public interface SavedCardPromoListener {
        void onItemPromo(int position);
    }

    class SavedCardsViewHolder extends RecyclerView.ViewHolder {
        TextView textCardName, textCardNumber;
        ImageView imageCardType;
        AspectRatioImageView imageCardOffer;

        public SavedCardsViewHolder(View itemView) {
            super(itemView);
            textCardName = (TextView) itemView.findViewById(R.id.text_saved_card_name);
            textCardNumber = (TextView) itemView.findViewById(R.id.text_saved_card_number);
            imageCardType = (ImageView) itemView.findViewById(R.id.image_card_type);
            imageCardOffer = (AspectRatioImageView) itemView.findViewById(R.id.image_card_offer);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onItemClick(getAdapterPosition());
                    }
                }
            });

            imageCardOffer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (promoListener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                        promoListener.onItemPromo(getAdapterPosition());
                    }
                }
            });
        }

    }
}
