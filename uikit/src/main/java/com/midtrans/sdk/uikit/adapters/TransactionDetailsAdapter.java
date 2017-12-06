package com.midtrans.sdk.uikit.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.midtrans.sdk.corekit.models.ItemDetails;
import com.midtrans.sdk.corekit.utilities.Utils;
import com.midtrans.sdk.uikit.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Fajar on 10/17/17.
 */

public class TransactionDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 1002;
    private static final int TYPE_ITEM = 1003;

    private List<ItemDetails> itemDetails;

    public TransactionDetailsAdapter(List<ItemDetails> itemDetails) {
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
            ItemDetails currentItem = findItemDetailByName(newItem.getName());
            if (currentItem == null) {
                itemDetails.add(newItem);
            } else {
                currentItem.setPrice(newItem.getPrice());
            }

            notifyDataSetChanged();
        }
    }

    private ItemDetails findItemDetailByName(String name) {
        for (ItemDetails item : itemDetails) {
            if (item != null && item.getName().equals(name)) {
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
                itemDetailsViewHolder.price.setText("Rp " + Utils.getFormattedAmount(item.getPrice() * item.getQuantity()));
                if (position % 2 != 0) {
                    itemDetailsViewHolder.itemView.setBackgroundResource(R.color.light_gray);
                }
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
}

