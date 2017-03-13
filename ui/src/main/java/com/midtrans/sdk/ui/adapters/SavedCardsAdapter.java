package com.midtrans.sdk.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.midtrans.sdk.core.models.snap.SavedToken;
import com.midtrans.sdk.core.utils.CardUtilities;
import com.midtrans.sdk.ui.R;
import com.midtrans.sdk.ui.utils.UiUtils;
import com.midtrans.sdk.ui.widgets.AspectRatioImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ziahaqi on 12/27/16.
 */

public class SavedCardsAdapter extends RecyclerView.Adapter<SavedCardsAdapter.SavedCardsViewHolder> {
    private final String TAG = getClass().getName();

    private List<SavedToken> savedCards = new ArrayList<>();

    private SavedCardAdapterEventListener listener;
    private SavedCardPromoListener promoListener;


    public interface SavedCardAdapterEventListener {
        void onItemClick(int position);
    }


    public interface SavedCardPromoListener {
        void onItemPromo(int position);
    }

    public SavedCardsAdapter() {
    }

    public List<SavedToken> getData() {
        return savedCards;
    }

    public void setData(List<SavedToken> cards) {
        this.savedCards.clear();
        this.savedCards.addAll(cards);
        this.notifyDataSetChanged();
    }

    public void setListener(SavedCardAdapterEventListener listener) {
        this.listener = listener;
    }

    public SavedToken getItem(int position) {
        return savedCards.get(position);
    }

    @Override
    public SavedCardsAdapter.SavedCardsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_saved_cards, parent, false);
        return new SavedCardsAdapter.SavedCardsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SavedCardsAdapter.SavedCardsViewHolder holder, int position) {
        SavedToken card = savedCards.get(position);
        String maskedCard = card.maskedCard;
        String cardType = CardUtilities.getCardType(maskedCard);

        switch (cardType) {
            case CardUtilities.CARD_TYPE_VISA:
                holder.imageCardType.setImageResource(R.mipmap.ic_visa);
                break;
            case CardUtilities.CARD_TYPE_MASTERCARD:
                holder.imageCardType.setImageResource(R.mipmap.ic_mastercard);
                break;
            case CardUtilities.CARD_TYPE_JCB:
                holder.imageCardType.setImageResource(R.mipmap.ic_jcb);
                break;
            case CardUtilities.CARD_TYPE_AMEX:
                holder.imageCardType.setImageResource(R.mipmap.ic_amex);
                break;
        }
        String cardName = maskedCard.substring(0, 4);
        holder.tvCardTitle.setText(cardType + "-" + cardName);
        String cardNumber = UiUtils.getMaskedCardNumber(maskedCard);
        holder.tvMaskedCarNumber.setText(cardNumber);

    }

    @Override
    public int getItemCount() {
        return savedCards.size();
    }

    class SavedCardsViewHolder extends RecyclerView.ViewHolder {
        TextView tvCardTitle, tvMaskedCarNumber;
        ImageView imageCardType;
        AspectRatioImageView ivCardOffer;

        public SavedCardsViewHolder(View itemView) {
            super(itemView);
            tvCardTitle = (TextView) itemView.findViewById(R.id.text_saved_card_name);
            tvMaskedCarNumber = (TextView) itemView.findViewById(R.id.text_saved_card_number);
            imageCardType = (ImageView) itemView.findViewById(R.id.image_card_type);
            ivCardOffer = (AspectRatioImageView) itemView.findViewById(R.id.image_card_offer);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onItemClick(getAdapterPosition());
                    }
                }
            });

            ivCardOffer.setOnClickListener(new View.OnClickListener() {
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
