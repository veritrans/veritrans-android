package com.midtrans.sdk.uikit.adapters;

import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.midtrans.sdk.corekit.models.BankType;
import com.midtrans.sdk.corekit.models.SaveCardRequest;
import com.midtrans.sdk.corekit.utilities.Utils;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.models.PromoData;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.views.creditcard.saved.SavedCreditCardActivity;
import com.midtrans.sdk.uikit.widgets.AspectRatioImageView;

import java.util.ArrayList;

import ru.rambler.libs.swipe_layout.SwipeLayout;

/**
 * Created by ziahaqi on 12/27/16.
 */

public class SavedCardsAdapter extends RecyclerView.Adapter<SavedCardsAdapter.SavedCardsViewHolder> {
    private static final String TAG = SavedCardsAdapter.class.getSimpleName();

    private ArrayList<SaveCardRequest> mData = new ArrayList<>();
    private ArrayList<PromoData> promoDatas = new ArrayList<>();

    private SavedCardAdapterEventListener listener;
    private SavedCardPromoListener promoListener;
    private DeleteCardListener deleteCardListener;

    public SavedCardsAdapter() {
        promoDatas = new ArrayList<>();
        mData = new ArrayList<>();
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

    public void removeCard(String maskedCard) {
        SaveCardRequest saveCardRequest = searchSavedCard(maskedCard);
        if (saveCardRequest != null) {
            mData.remove(mData.indexOf(saveCardRequest));
            notifyDataSetChanged();
        }
    }

    private SaveCardRequest searchSavedCard(String maskedCard) {
        for (SaveCardRequest saveCardRequest : mData) {
            if (saveCardRequest.getMaskedCard().equals(maskedCard)) {
                return saveCardRequest;
            }
        }
        return null;
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

        holder.swipeLayout.setOffset(0);
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

        if (promoDatas != null && !promoDatas.isEmpty()) {
            PromoData promoData = promoDatas.get(position);
            if (promoData != null && promoData.getPromoResponse() != null) {
                holder.imageCardOffer.setVisibility(View.VISIBLE);
            } else {
                holder.imageCardOffer.setVisibility(View.GONE);
            }
        }

        SavedCreditCardActivity creditCardFlowActivity = (SavedCreditCardActivity) holder.itemView.getContext();
        if (creditCardFlowActivity != null) {
            String bank = creditCardFlowActivity.getBankByBin(card.getMaskedCard().substring(0, 6));
            if (bank != null && !TextUtils.isEmpty(bank)) {
                switch (bank) {
                    case BankType.BCA:
                        holder.bankLogo.setImageResource(R.drawable.bca);
                        break;
                    case BankType.BNI:
                        holder.bankLogo.setImageResource(R.drawable.bni);
                        break;
                    case BankType.BRI:
                        holder.bankLogo.setImageResource(R.drawable.bri);
                        break;
                    case BankType.CIMB:
                        holder.bankLogo.setImageResource(R.drawable.cimb);
                        break;
                    case BankType.MANDIRI:
                        holder.bankLogo.setImageResource(R.drawable.mandiri);
                        break;
                    case BankType.MAYBANK:
                        holder.bankLogo.setImageResource(R.drawable.maybank);
                        break;
                    case BankType.BNI_DEBIT_ONLINE:
                        holder.bankLogo.setImageResource(R.drawable.bni);
                        break;
                    default:
                        holder.bankLogo.setImageDrawable(null);
                        break;
                }
            } else {
                holder.bankLogo.setImageDrawable(null);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
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
        TextView textCardName, textCardNumber, deleteButton;
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
