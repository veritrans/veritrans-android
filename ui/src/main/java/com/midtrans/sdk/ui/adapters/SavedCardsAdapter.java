package com.midtrans.sdk.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.midtrans.sdk.core.models.BankType;
import com.midtrans.sdk.core.utils.CardUtilities;
import com.midtrans.sdk.ui.R;
import com.midtrans.sdk.ui.models.SavedCard;
import com.midtrans.sdk.ui.utils.UiUtils;
import com.midtrans.sdk.ui.widgets.AspectRatioImageView;

import java.util.ArrayList;
import java.util.List;

import ru.rambler.libs.swipe_layout.SwipeLayout;

/**
 * Created by ziahaqi on 12/27/16.
 */

public class SavedCardsAdapter extends RecyclerView.Adapter<SavedCardsAdapter.SavedCardsViewHolder> {

    private List<SavedCard> savedCards = new ArrayList<>();

    private SavedCardAdapterEventListener listener;
    private SavedCardPromoListener promoListener;
    private DeleteCardListener deleteCardListener;

    public List<SavedCard> getData() {
        return savedCards;
    }

    public void setData(List<SavedCard> cards) {
        this.savedCards.clear();
        this.savedCards.addAll(cards);
        this.notifyDataSetChanged();
    }

    public void deleteCard(String maskedCard) {
        SavedCard savedCard = searchCard(maskedCard);
        if (savedCard != null) {
            int position = savedCards.indexOf(savedCard);
            savedCards.remove(position);
        }
    }

    private SavedCard searchCard(String maskedCard) {
        for (SavedCard savedCard : savedCards) {
            if (savedCard.getSavedToken().maskedCard.equals(maskedCard)) {
                return savedCard;
            }
        }
        return null;
    }

    public void setListener(SavedCardAdapterEventListener listener) {
        this.listener = listener;
    }

    public SavedCard getItem(int position) {
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
        SavedCard card = savedCards.get(position);
        String maskedCard = card.getSavedToken().maskedCard;
        String cardType = CardUtilities.getCardType(maskedCard);
        holder.swipeLayout.setOffset(0);
        String cardName = maskedCard.substring(0, 4);
        holder.textCardName.setText(!TextUtils.isEmpty(cardType) ? cardType + "-" + cardName : cardName);
        String cardNumber = UiUtils.getMaskedCardNumber(maskedCard);
        holder.textCardNumber.setText(cardNumber);

        setCardType(cardType, holder.imageCardType);
        setBankType(card.getBank(), holder.bankLogo);
    }

    private void setCardType(String cardType, ImageView imageView) {
        switch (cardType) {
            case CardUtilities.CARD_TYPE_VISA:
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageResource(R.drawable.ic_visa);
                break;
            case CardUtilities.CARD_TYPE_MASTERCARD:
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageResource(R.drawable.ic_mastercard);
                break;
            case CardUtilities.CARD_TYPE_JCB:
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageResource(R.drawable.ic_jcb);
                break;
            case CardUtilities.CARD_TYPE_AMEX:
                imageView.setVisibility(View.VISIBLE);
                imageView.setImageResource(R.drawable.ic_amex);
                break;
            default:
                imageView.setVisibility(View.GONE);
                imageView.setImageDrawable(null);
                break;
        }
    }

    private void setBankType(String bank, ImageView imageView) {
        if (bank != null && !TextUtils.isEmpty(bank)) {
            switch (bank) {
                case BankType.BCA:
                    imageView.setImageResource(R.drawable.bca);
                    break;
                case BankType.BNI:
                    imageView.setImageResource(R.drawable.bni);
                    break;
                case BankType.BRI:
                    imageView.setImageResource(R.drawable.bri);
                    break;
                case BankType.CIMB:
                    imageView.setImageResource(R.drawable.cimb);
                    break;
                case BankType.MANDIRI:
                    imageView.setImageResource(R.drawable.mandiri);
                    break;
                case BankType.MAYBANK:
                    imageView.setImageResource(R.drawable.maybank);
                    break;
                case BankType.BNI_DEBIT_ONLINE:
                    imageView.setImageResource(R.drawable.bni);
                    break;
                default:
                    imageView.setImageDrawable(null);
                    break;
            }
        } else {
            imageView.setImageDrawable(null);
        }
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
