package com.midtrans.demo;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.midtrans.demo.models.SavedCard;
import com.midtrans.sdk.corekit.models.BankType;
import com.midtrans.sdk.corekit.models.snap.BankBinsResponse;
import com.midtrans.sdk.corekit.utilities.Utils;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.widgets.AspectRatioImageView;

import java.util.ArrayList;
import java.util.List;

import ru.rambler.libs.swipe_layout.SwipeLayout;

/**
 * Created by ziahaqi on 12/27/16.
 */

public class SavedCardsAdapter extends RecyclerView.Adapter<SavedCardsAdapter.SavedCardsViewHolder> {
    private static final String TAG = SavedCardsAdapter.class.getSimpleName();

    private List<SavedCard> mData = new ArrayList<>();

    private SavedCardAdapterEventListener listener;
    private DeleteCardListener deleteCardListener;

    private List<BankBinsResponse> bankBins;

    public SavedCardsAdapter() {
        mData = new ArrayList<>();
    }

    public List<SavedCard> getData() {
        return mData;
    }

    public void setData(List<SavedCard> cards) {
        this.mData.clear();
        this.mData.addAll(cards);
        this.notifyDataSetChanged();
    }

    public void setBankBin(Context context) {
        bankBins = AppUtils.getBankBins(context);
    }

    public void removeCard(String maskedCard) {
        SavedCard saveCardRequest = searchSavedCard(maskedCard);
        if (saveCardRequest != null) {
            mData.remove(mData.indexOf(saveCardRequest));
            notifyDataSetChanged();
        }
    }

    private SavedCard searchSavedCard(String maskedCard) {
        for (SavedCard savedCard : mData) {
            if (savedCard.getMaskedCard().equals(maskedCard)) {
                return savedCard;
            }
        }
        return null;
    }

    public void setListener(SavedCardAdapterEventListener listener) {
        this.listener = listener;
    }


    public SavedCard getItem(int position) {
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
        SavedCard card = mData.get(position);
        String maskedCard = card.getMaskedCard();
        String cardType = Utils.getCardType(maskedCard);
        String bankType = getBankByBin(card.getMaskedCard().substring(0, 6));

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

        if (bankType != null && !TextUtils.isEmpty(bankType)) {
            switch (bankType) {
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

        String cardName = maskedCard.substring(0, 4);
        holder.textCardName.setText(cardType + "-" + cardName);
        String cardNumber = getMaskedCardNumber(maskedCard);
        holder.textCardNumber.setText(cardNumber);

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setDeleteCardListener(DeleteCardListener deleteCardListener) {
        this.deleteCardListener = deleteCardListener;
    }

    public interface SavedCardAdapterEventListener {
        void onItemClick();
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

            mainSaveCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null && getAdapterPosition() != RecyclerView.NO_POSITION) {
                        listener.onItemClick();
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


        }

    }

    public String getMaskedCardNumber(String maskedCard) {
        StringBuilder builder = new StringBuilder();
        String bulletMask = "●●●●●●";
        String newMaskedCard = maskedCard.replace("-", bulletMask);

        for (int i = 0; i < newMaskedCard.length(); i++) {
            if (i > 0 && i % 4 == 0) {
                builder.append(' ');
                builder.append(newMaskedCard.charAt(i));
            } else {
                builder.append(newMaskedCard.charAt(i));
            }
        }
        return builder.toString();
    }

    private String getBankByBin(String cardBin) {
        for (BankBinsResponse savedBankBin : bankBins) {
            if (savedBankBin.getBins() != null && !savedBankBin.getBins().isEmpty()) {
                for (String savedBin : savedBankBin.getBins()) {
                    if (savedBin.contains(cardBin)) {
                        String bankBin = savedBankBin.getBank();
                        if (bankBin != null) {
                            return bankBin;
                        }
                    }
                }
            }
        }
        return null;
    }
}
