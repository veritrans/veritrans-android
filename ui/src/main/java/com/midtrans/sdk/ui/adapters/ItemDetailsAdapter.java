package com.midtrans.sdk.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.midtrans.sdk.ui.R;
import com.midtrans.sdk.ui.models.ItemDetail;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ziahaqi on 2/21/17.
 */

public class ItemDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 1002;
    private static final int TYPE_ITEM = 1003;
    private boolean itemShown = false;

    private List<ItemDetail> itemDetails;
    private ItemDetailListener listener;

    public void setData(List<ItemDetail> itemDetails) {
        itemDetails.clear();
        itemDetails.addAll(itemDetails);
        notifyDataSetChanged();
    }

    public interface ItemDetailListener {
        public void onItemShown();
    }

    public ItemDetailsAdapter(ItemDetailListener listener, List<ItemDetail> itemDetails) {
        this.listener = listener;
        this.itemDetails = new ArrayList<>(itemDetails);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                final View headerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_item_header, parent, false);
                headerView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        itemShown = !itemShown;
                        notifyDataSetChanged();
                        if (listener != null && itemShown) {
                            listener.onItemShown();
                        }
                    }
                });

                return new ItemHeaderViewHolder(headerView);
            case TYPE_ITEM:
                View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_item_details, parent, false);
                return new ItemDetailsViewHolder(itemView);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int type = getItemViewType(position);
        switch (type) {
            case TYPE_HEADER:
                ItemHeaderViewHolder headerViewHolder = (ItemHeaderViewHolder) holder;
                headerViewHolder.amount.setText(itemDetails.get(position).getValue());
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
    public int getItemCount() {
        if (itemShown) {
            return itemDetails.size();
        } else {
            return 1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (itemDetails.get(position).getType().equalsIgnoreCase(ItemDetail.TYPE_ITEM_HEADER)) {
            return TYPE_HEADER;
        } else if (itemDetails.get(position).getType().equalsIgnoreCase(ItemDetail.TYPE_ITEM)) {
            return TYPE_ITEM;
        }
        return super.getItemViewType(position);
    }

    class ItemDetailsViewHolder extends RecyclerView.ViewHolder {
        TextView item;
        TextView price;

        public ItemDetailsViewHolder(View itemView) {
            super(itemView);
            item = (TextView) itemView.findViewById(R.id.item_name);
            price = (TextView) itemView.findViewById(R.id.item_price);
        }
    }

    class ItemHeaderViewHolder extends RecyclerView.ViewHolder {
        TextView amount;
        View containerItem;
        View divider;

        public ItemHeaderViewHolder(final View itemView) {
            super(itemView);
            divider = itemView.findViewById(R.id.divider);
            amount = (TextView) itemView.findViewById(R.id.total_amount);
            containerItem = itemView.findViewById(R.id.item_details_container);
        }
    }
}
