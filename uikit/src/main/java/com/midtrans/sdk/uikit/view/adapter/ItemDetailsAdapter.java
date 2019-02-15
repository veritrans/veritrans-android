package com.midtrans.sdk.uikit.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.base.adapter.BaseAdapter;
import com.midtrans.sdk.uikit.view.model.ItemViewDetails;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemDetailsAdapter extends BaseAdapter<ItemViewDetails> {

    private static final int TYPE_HEADER = 1002;
    private static final int TYPE_ITEM = 1003;
    private final String orderId;
    private boolean itemShown = false;

    private List<ItemViewDetails> itemDetails;
    private ItemDetailListener listener;

    public interface ItemDetailListener {
        void onItemShown();
    }

    public ItemDetailsAdapter(List<ItemViewDetails> itemDetails, String orderId, ItemDetailListener listener) {
        this.orderId = orderId;
        this.itemDetails = itemDetails;
        this.listener = listener;
        setHasStableIds(true);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                final View headerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_payment_item_header, parent, false);
                headerView.setOnClickListener(view -> {
                    itemShown = !itemShown;
                    notifyDataSetChanged();
                    if (listener != null && itemShown) {
                        listener.onItemShown();
                    }
                });

                return new ItemHeaderViewHolder(headerView);
            case TYPE_ITEM:
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_payment_item_list, parent, false);
                return new ItemDetailsViewHolder(itemView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        switch (type) {
            case TYPE_HEADER:
                ItemHeaderViewHolder headerViewHolder = (ItemHeaderViewHolder) holder;
                headerViewHolder.amount.setText(itemDetails.get(position).getValue());
                headerViewHolder.orderId.setText(this.orderId);
                if (itemDetails.get(position).isItemAvailable() && itemShown) {
                    headerViewHolder.containerItem.setVisibility(View.VISIBLE);
                    headerViewHolder.divider.setVisibility(View.VISIBLE);
                } else {
                    headerViewHolder.containerItem.setVisibility(View.GONE);
                    headerViewHolder.divider.setVisibility(View.GONE);
                }
                break;
            case TYPE_ITEM:
                ItemDetailsViewHolder itemDetailsViewHolder = (ItemDetailsViewHolder) holder;
                itemDetailsViewHolder.item.setText(itemDetails.get(position).getKey());
                itemDetailsViewHolder.price.setText(itemDetails.get(position).getValue());
                break;
            default:
                break;
        }
    }

    @Override
    public void addAllData(List<ItemViewDetails> data) {
        this.itemDetails.addAll(data);
        this.notifyDataSetChanged();
    }

    @Override
    public void addData(ItemViewDetails data) {
        this.itemDetails.add(data);
        this.notifyDataSetChanged();
    }

    @Override
    public ItemViewDetails getDataAt(int position) {
        return itemDetails.get(position);
    }

    @Override
    public List<ItemViewDetails> getAllData() {
        return itemDetails;
    }

    @Override
    public void setData(List<ItemViewDetails> data) {
        this.itemDetails = data;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (itemShown) {
            return itemDetails.size();
        } else {
            return 1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (itemDetails.get(position).getType().equalsIgnoreCase(ItemViewDetails.TYPE_ITEM_HEADER)) {
            return TYPE_HEADER;
        } else if (itemDetails.get(position).getType().equalsIgnoreCase(ItemViewDetails.TYPE_ITEM)) {
            return TYPE_ITEM;
        }
        return super.getItemViewType(position);
    }

    class ItemDetailsViewHolder extends RecyclerView.ViewHolder {
        TextView item;
        TextView price;

        ItemDetailsViewHolder(View itemView) {
            super(itemView);
            item = itemView.findViewById(R.id.item_name);
            price = itemView.findViewById(R.id.item_price);
        }
    }

    class ItemHeaderViewHolder extends RecyclerView.ViewHolder {
        TextView amount;
        TextView orderId;
        View containerItem;
        View divider;

        ItemHeaderViewHolder(final View itemView) {
            super(itemView);
            divider = itemView.findViewById(R.id.divider);
            amount = itemView.findViewById(R.id.total_amount);
            orderId = itemView.findViewById(R.id.text_order_id);
            containerItem = itemView.findViewById(R.id.item_details_container);
        }
    }
}