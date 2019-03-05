package com.midtrans.sdk.uikit.view.adapter;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.midtrans.sdk.corekit.core.api.merchant.model.checkout.request.optional.Item;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.base.adapter.BaseAdapter;
import com.midtrans.sdk.uikit.utilities.Constants;
import com.midtrans.sdk.uikit.utilities.CurrencyHelper;

import java.util.ArrayList;
import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class InstructionDetailAdapter extends BaseAdapter<Item> {

    private static final int TYPE_HEADER = 1002;
    private static final int TYPE_ITEM = 1003;

    private List<Item> itemDetails;
    private String currency;

    public InstructionDetailAdapter() {
        this.itemDetails = new ArrayList<>();
    }

    public InstructionDetailAdapter(String currency) {
        this.itemDetails = new ArrayList<>();
        this.currency = currency;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                final View headerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_detail_header, parent, false);
                return new ItemHeaderViewHolder(headerView);
            case TYPE_ITEM:
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_detail_body, parent, false);
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
                Item item = itemDetails.get(position);
                itemDetailsViewHolder.item.setText(item.getName());
                itemDetailsViewHolder.quantity.setText(item.getQuantity() == 0 ? "" : String.valueOf(item.getQuantity()));
                itemDetailsViewHolder.price.setText(
                        CurrencyHelper.formatAmount(
                                holder.itemView.getContext(),
                                item.getPrice(),
                                currency
                        )
                );

                if (position % 2 != 0) {
                    itemDetailsViewHolder.itemView.setBackgroundResource(R.color.light_gray);
                }

                int amountColor;
                if (!TextUtils.isEmpty(item.getId()) && (item.getId().equals(Constants.PROMO_ID)
                        || item.getId().equals(Constants.BNI_POINT_ID)
                        || item.getId().equals(Constants.MANDIRI_POIN_ID))) {

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

    @Override
    public void addAllData(List<Item> data) {
        this.itemDetails.addAll(data);
        this.notifyDataSetChanged();
    }

    @Override
    public void addData(Item data) {

    }

    @Override
    public Item getDataAt(int position) {
        return itemDetails.get(position);
    }

    @Override
    public List<Item> getAllData() {
        return itemDetails;
    }

    @Override
    public void setData(List<Item> data) {
        this.itemDetails = new ArrayList<>();
        this.itemDetails = data;
        if (this.itemDetails.get(0) != null) {
            this.itemDetails.add(0, null);
        }
        this.notifyDataSetChanged();
    }

    private Item findItemById(String id) {
        for (Item item : itemDetails) {
            if (item != null && item.getId().equals(id)) {
                return item;
            }
        }
        return null;
    }

    public void addItemDetails(Item newItem) {
        if (newItem != null) {
            Item currentItem = findItemById(newItem.getId());
            if (currentItem == null) {
                itemDetails.add(newItem);
            } else {
                currentItem.setPrice(newItem.getPrice());
                currentItem.setName(newItem.getName());
            }
            this.notifyDataSetChanged();
        }
    }

    public void removeItemWithId(String itemId) {
        Item item = findItemById(itemId);
        if (item != null) {
            itemDetails.remove(item);
            this.notifyDataSetChanged();
        }
    }

    public double getItemTotalAmount() {
        double totalAmount = 0d;
        for (Item item : itemDetails) {
            if (item != null) {
                totalAmount += item.getPrice();
            }
        }
        return totalAmount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    private class ItemDetailsViewHolder extends RecyclerView.ViewHolder {
        View background;
        TextView item;
        TextView quantity;
        TextView price;

        ItemDetailsViewHolder(View itemView) {
            super(itemView);
            background = itemView;
            item = itemView.findViewById(R.id.item_name);
            quantity = itemView.findViewById(R.id.item_quantity);
            price = itemView.findViewById(R.id.item_price);
        }
    }

    private class ItemHeaderViewHolder extends RecyclerView.ViewHolder {

        ItemHeaderViewHolder(final View itemView) {
            super(itemView);
        }
    }
}