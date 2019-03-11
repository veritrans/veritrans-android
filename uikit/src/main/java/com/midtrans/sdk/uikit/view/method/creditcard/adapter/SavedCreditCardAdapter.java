package com.midtrans.sdk.uikit.view.method.creditcard.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.midtrans.sdk.corekit.base.enums.BankType;
import com.midtrans.sdk.corekit.core.api.snap.model.pay.request.creditcard.SaveCardRequest;
import com.midtrans.sdk.corekit.utilities.Helper;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.base.adapter.BaseAdapter;
import com.midtrans.sdk.uikit.view.method.creditcard.SavedCreditCardListActivity;
import com.midtrans.sdk.uikit.widget.AspectRatioImageView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ru.rambler.libs.swipe_layout.SwipeLayout;

public class SavedCreditCardAdapter extends BaseAdapter<SaveCardRequest> {

    private List<SaveCardRequest> mData = new ArrayList<>();
    private ArrayList<PromoData> promoDatas = new ArrayList<>();
    private SavedCardListener savedCardListener;

    public SavedCreditCardAdapter(SavedCardListener listener) {
        this.savedCardListener = listener;
        this.mData = new ArrayList<>();
        this.promoDatas = new ArrayList<>();
    }

    @Override
    public SavedCardsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_credit_card_saved_card_item_list, parent, false);
        return new SavedCardsViewHolder(view, savedCardListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        SavedCardsViewHolder holder = (SavedCardsViewHolder) viewHolder;
        SaveCardRequest card = mData.get(position);
        String maskedCard = card.getMaskedCard();
        String cardType = Helper.getCardType(maskedCard);

        holder.swipeLayout.setOffset(0);
        switch (cardType) {
            case Helper.CARD_TYPE_VISA:
                holder.imageCardType.setImageResource(R.drawable.ic_visa);
                break;
            case Helper.CARD_TYPE_MASTERCARD:
                holder.imageCardType.setImageResource(R.drawable.ic_mastercard);
                break;
            case Helper.CARD_TYPE_JCB:
                holder.imageCardType.setImageResource(R.drawable.ic_jcb);
                break;
            case Helper.CARD_TYPE_AMEX:
                holder.imageCardType.setImageResource(R.drawable.ic_amex);
                break;
        }
        String cardName = maskedCard.substring(0, 4);
        holder.textCardName.setText(cardType + "-" + cardName);
        String cardNumber = com.midtrans.sdk.uikit.utilities.Helper.getMaskedCardNumber(maskedCard);
        holder.textCardNumber.setText(cardNumber);

        if (promoDatas != null && !promoDatas.isEmpty()) {
            PromoData promoData = promoDatas.get(position);
            if (promoData != null && promoData.getPromoResponse() != null) {
                holder.imageCardOffer.setVisibility(View.VISIBLE);
            } else {
                holder.imageCardOffer.setVisibility(View.GONE);
            }
        }

        SavedCreditCardListActivity creditCardFlowActivity = (SavedCreditCardListActivity) holder.itemView.getContext();
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

    private SaveCardRequest searchSavedCard(String maskedCard) {
        for (SaveCardRequest saveCardRequest : mData) {
            if (saveCardRequest.getMaskedCard().equals(maskedCard)) {
                return saveCardRequest;
            }
        }
        return null;
    }

    public ArrayList<PromoData> getPromoDatas() {
        return promoDatas;
    }

    public void setPromoDatas(ArrayList<PromoData> promoDatas) {
        this.promoDatas.clear();
        this.promoDatas.addAll(promoDatas);
        notifyDataSetChanged();
    }

    public void removeCard(String maskedCard) {
        SaveCardRequest saveCardRequest = searchSavedCard(maskedCard);
        if (saveCardRequest != null) {
            mData.remove(mData.indexOf(saveCardRequest));
            notifyDataSetChanged();
        }
    }

    @Override
    public void addAllData(List<SaveCardRequest> data) {
        this.mData.addAll(data);
        this.notifyDataSetChanged();
    }

    @Override
    public void addData(SaveCardRequest data) {
        this.mData.add(data);
        this.notifyDataSetChanged();
    }

    @Override
    public SaveCardRequest getDataAt(int position) {
        return mData.get(position);
    }

    @Override
    public List<SaveCardRequest> getAllData() {
        return mData;
    }

    @Override
    public void setData(List<SaveCardRequest> data) {
        this.mData.clear();
        this.mData.addAll(data);
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface SavedCardListener {
        void onItemClick(int position);

        void onItemPromo(int position);

        void onItemDelete(int position);
    }

    class SavedCardsViewHolder extends RecyclerView.ViewHolder {
        TextView textCardName, textCardNumber, deleteButton;
        ImageView imageCardType;
        AspectRatioImageView imageCardOffer;
        SwipeLayout swipeLayout;
        LinearLayout mainSaveCard;
        AspectRatioImageView bankLogo;

        public SavedCardsViewHolder(View itemView, final SavedCardListener listener) {
            super(itemView);
            swipeLayout = itemView.findViewById(R.id.lyt_container);
            textCardName = itemView.findViewById(R.id.text_saved_card_name);
            textCardNumber = itemView.findViewById(R.id.text_saved_card_number);
            deleteButton = itemView.findViewById(R.id.txt_delete);
            imageCardType = itemView.findViewById(R.id.image_card_type);
            imageCardOffer = itemView.findViewById(R.id.image_card_offer);
            mainSaveCard = itemView.findViewById(R.id.save_card_main);
            bankLogo = itemView.findViewById(R.id.bank_logo);

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

            mainSaveCard.setOnClickListener(view -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onItemClick(getAdapterPosition());
                }
            });

            deleteButton.setOnClickListener(view -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onItemDelete(getAdapterPosition());
                }
            });


            imageCardOffer.setOnClickListener(view -> {
                if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                    listener.onItemPromo(getAdapterPosition());
                }
            });
        }

    }
}