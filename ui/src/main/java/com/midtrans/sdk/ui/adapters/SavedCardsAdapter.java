package com.midtrans.sdk.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.midtrans.sdk.core.models.snap.SavedToken;
import com.midtrans.sdk.core.utils.CardUtilities;
import com.midtrans.sdk.ui.R;
import com.midtrans.sdk.ui.utils.UiUtils;
import com.midtrans.sdk.ui.widgets.AspectRatioImageView;

import java.util.ArrayList;
import java.util.List;

import ru.rambler.libs.swipe_layout.SwipeLayout;

/**
 * Created by ziahaqi on 12/27/16.
 */

public class SavedCardsAdapter extends RecyclerView.Adapter<SavedCardsAdapter.SavedCardsViewHolder> {

    private List<SavedToken> savedCards = new ArrayList<>();

    private SavedCardAdapterEventListener listener;
    private SavedCardPromoListener promoListener;
    private DeleteCardListener deleteCardListener;

    public List<SavedToken> getData() {
        return savedCards;
    }

    public void setData(List<SavedToken> cards) {
        this.savedCards.clear();
        this.savedCards.addAll(cards);
        this.notifyDataSetChanged();
    }

    public void deleteCard(String maskedCard) {
        SavedToken savedToken = searchCard(maskedCard);
        if (savedToken != null) {
            int position = savedCards.indexOf(savedToken);
            savedCards.remove(position);
        }
    }

    private SavedToken searchCard(String maskedCard) {
        for (SavedToken savedToken : savedCards) {
            if (savedToken.maskedCard.equals(maskedCard)) {
                return savedToken;
            }
        }
        return null;
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
                .inflate(R.layout.item_saved_cards, parent, false);
        return new SavedCardsAdapter.SavedCardsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(SavedCardsAdapter.SavedCardsViewHolder holder, int position) {
        SavedToken card = savedCards.get(position);
        String maskedCard = card.maskedCard;
        String cardType = CardUtilities.getCardType(maskedCard);
        holder.swipeLayout.setOffset(0);
        switch (cardType) {
            case CardUtilities.CARD_TYPE_VISA:
                holder.imageCardType.setImageResource(R.drawable.ic_visa);
                break;
            case CardUtilities.CARD_TYPE_MASTERCARD:
                holder.imageCardType.setImageResource(R.drawable.ic_mastercard);
                break;
            case CardUtilities.CARD_TYPE_JCB:
                holder.imageCardType.setImageResource(R.drawable.ic_jcb);
                break;
            case CardUtilities.CARD_TYPE_AMEX:
                holder.imageCardType.setImageResource(R.drawable.ic_amex);
                break;
        }
        String cardName = maskedCard.substring(0, 4);
        holder.textCardName.setText(cardType + "-" + cardName);
        String cardNumber = UiUtils.getMaskedCardNumber(maskedCard);
        holder.textCardNumber.setText(cardNumber);

    }

    @Override
    public int getItemCount() {
        return savedCards.size();
    }

    public void setDeleteCardListener(DeleteCardListener deleteCardListener) {
        this.deleteCardListener = deleteCardListener;
    }

    public interface SavedCardAdapterEventListener {
        void onItemClick(int position);
    }

    public interface SavedCardPromoListener {
        void onItemPromo(int position);
    }

    public interface DeleteCardListener {
        void onItemDelete(int position);
    }

    class SavedCardsViewHolder extends RecyclerView.ViewHolder {
        TextView textCardName;
        TextView textCardNumber;
        TextView deleteButton;
        ImageView imageCardType;
        AspectRatioImageView imageCardOffer;
        SwipeLayout swipeLayout;
        LinearLayout mainSaveCard;
        AspectRatioImageView bankLogo;

        public SavedCardsViewHolder(View itemView) {
            super(itemView);
            swipeLayout = (SwipeLayout) itemView.findViewById(R.id.lyt_container);
            textCardName = (TextView) itemView.findViewById(R.id.text_saved_card_name);
            textCardNumber = (TextView) itemView.findViewById(R.id.text_saved_card_number);
            deleteButton = (TextView) itemView.findViewById(R.id.txt_delete);
            imageCardType = (ImageView) itemView.findViewById(R.id.image_card_type);
            imageCardOffer = (AspectRatioImageView) itemView.findViewById(R.id.image_card_offer);
            mainSaveCard = (LinearLayout) itemView.findViewById(R.id.save_card_main);
            bankLogo = (AspectRatioImageView) itemView.findViewById(R.id.bank_logo);

            swipeLayout.setOnSwipeListener(new SwipeLayout.OnSwipeListener() {
                @Override
                public void onBeginSwipe(SwipeLayout swipeLayout, boolean moveToRight) {

                }

                @Override
                public void onSwipeClampReached(SwipeLayout swipeLayout, boolean moveToRight) {

                }

                @Override
                public void onLeftStickyEdge(SwipeLayout swipeLayout, boolean moveToRight) {

                }

                @Override
                public void onRightStickyEdge(SwipeLayout swipeLayout, boolean moveToRight) {

                }
            });

            mainSaveCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                        listener.onItemClick(getAdapterPosition());
                    }
                }
            });

            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (deleteCardListener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                        deleteCardListener.onItemDelete(getAdapterPosition());
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
