package com.midtrans.sdk.uikit.adapters;

import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.midtrans.sdk.corekit.models.snap.ItemDetails;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.utilities.SdkUIFlowUtil;
import com.midtrans.sdk.uikit.utilities.UiKitConstants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fajar on 10/17/17.
 */

public class TransactionDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 1002;
    private static final int TYPE_ITEM = 1003;

    private List<ItemDetails> itemDetails;
    private String currency;

    public TransactionDetailsAdapter(List<ItemDetails> itemDetails) {
        init(itemDetails);
    }

    public TransactionDetailsAdapter(List<ItemDetails> itemDetails, String currency) {
        this.currency = currency;
        init(itemDetails);
    }

    private void init(List<ItemDetails> itemDetails) {
        this.itemDetails = new ArrayList<>();
        if (itemDetails != null) {
            this.itemDetails.addAll(itemDetails);
        }

        if (this.itemDetails.get(0) != null) {
            this.itemDetails.add(0, null);
        }
    }

    public void addItemDetails(ItemDetails newItem) {
        if (itemDetails == null) {
            itemDetails = new ArrayList<>();
        }

        if (newItem != null) {
            ItemDetails currentItem = findItemDetailById(newItem.getId());
            if (currentItem == null) {
                itemDetails.add(newItem);
            } else {
                currentItem.setPrice(newItem.getPrice());
                currentItem.setName(newItem.getName());
            }

            notifyDataSetChanged();
        }
    }

    private ItemDetails findItemDetailById(String id) {
        for (ItemDetails item : itemDetails) {
            if (item != null && item.getId().equals(id)) {
                return item;
            }
        }
        return null;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                final View headerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_item_header, parent, false);
                return new ItemHeaderViewHolder(headerView);
            case TYPE_ITEM:
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.transaction_item_body, parent, false);
                return new ItemDetailsViewHolder(itemView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        switch (type) {
            case TYPE_HEADER:
                break;
            case TYPE_ITEM:
                ItemDetailsViewHolder itemDetailsViewHolder = (ItemDetailsViewHolder) holder;
                ItemDetails item = itemDetails.get(position);
                itemDetailsViewHolder.item.setText(item.getName());
                itemDetailsViewHolder.quantity.setText(item.getQuantity() == 0 ? "" : String.valueOf(item.getQuantity()));
                itemDetailsViewHolder.price.setText(SdkUIFlowUtil.getFormattedAmount(
                        holder.itemView.getContext(),
                        item.getPrice(),
                        currency)
                );

                if (position % 2 != 0) {
                    itemDetailsViewHolder.itemView.setBackgroundResource(R.color.light_gray);
                }

                int amountColor;
                if (!TextUtils.isEmpty(item.getId()) && (item.getId().equals(UiKitConstants.PROMO_ID)
                        || item.getId().equals(UiKitConstants.BNI_POINT_ID)
                        || item.getId().equals(UiKitConstants.MANDIRI_POIN_ID))) {

                    amountColor = ContextCompat.getColor(itemDetailsViewHolder.itemView.getContext(), R.color.promoAmount);
                } else {
                    amountColor = ContextCompat.getColor(itemDetailsViewHolder.itemView.getContext(), R.color.dark_gray);
                }
                itemDetailsViewHolder.price.setTextColor(amountColor);

                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return itemDetails.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || itemDetails.get(position) == null) {
            return TYPE_HEADER;
        } else {
            return TYPE_ITEM;
        }
    }

    public void removeItemDetails(String itemDetailsId) {
        ItemDetails item = findItemDetailById(itemDetailsId);
        if (item != null) {
            itemDetails.remove(item);
            notifyDataSetChanged();
        }
    }

    public double getItemTotalAmount() {
        double totalAmount = 0d;
        for (ItemDetails item : itemDetails) {
            if (item != null) {
                totalAmount += item.getPrice();
            }
        }

        return totalAmount;
    }

    public List<ItemDetails> getItemDetails() {
        return itemDetails;
    }

    private class ItemDetailsViewHolder extends RecyclerView.ViewHolder {
        View background;
        TextView item;
        TextView quantity;
        TextView price;

        ItemDetailsViewHolder(View itemView) {
            super(itemView);
            background = itemView;
            item = (TextView) itemView.findViewById(R.id.item_name);
            quantity = (TextView) itemView.findViewById(R.id.item_quantity);
            price = (TextView) itemView.findViewById(R.id.item_price);
        }
    }

    private class ItemHeaderViewHolder extends RecyclerView.ViewHolder {

        ItemHeaderViewHolder(final View itemView) {
            super(itemView);
        }
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }
}

