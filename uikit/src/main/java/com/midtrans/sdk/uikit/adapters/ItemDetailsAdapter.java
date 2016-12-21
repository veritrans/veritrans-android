package com.midtrans.sdk.uikit.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.models.ItemViewDetails;

import java.util.List;

/**
 * Created by rakawm on 12/21/16.
 */

public class ItemDetailsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 1002;
    private static final int TYPE_ITEM = 1003;

    private List<ItemViewDetails> itemViewDetails;

    public ItemDetailsAdapter(List<ItemViewDetails> itemViewDetails) {
        this.itemViewDetails = itemViewDetails;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                View headerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_item_header, parent, false);
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
                headerViewHolder.amount.setText(itemViewDetails.get(position).getValue());
                if (itemViewDetails.get(position).isItemAvailable()) {
                    headerViewHolder.containerItem.setVisibility(View.VISIBLE);
                    headerViewHolder.orderTitle.setVisibility(View.VISIBLE);
                    headerViewHolder.divider.setVisibility(View.VISIBLE);
                } else {
                    headerViewHolder.containerItem.setVisibility(View.GONE);
                    headerViewHolder.orderTitle.setVisibility(View.GONE);
                    headerViewHolder.divider.setVisibility(View.GONE);
                }
                break;
            case TYPE_ITEM:
                ItemDetailsViewHolder itemDetailsViewHolder = (ItemDetailsViewHolder) holder;
                itemDetailsViewHolder.item.setText(itemViewDetails.get(position).getKey());
                itemDetailsViewHolder.price.setText(itemViewDetails.get(position).getValue());
                break;
            default:
                break;
        }
    }

    @Override
    public int getItemCount() {
        return itemViewDetails.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (itemViewDetails.get(position).getType().equalsIgnoreCase(ItemViewDetails.TYPE_ITEM_HEADER)) {
            return TYPE_HEADER;
        } else if (itemViewDetails.get(position).getType().equalsIgnoreCase(ItemViewDetails.TYPE_ITEM)) {
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
        TextView orderTitle;

        public ItemHeaderViewHolder(View itemView) {
            super(itemView);
            divider = itemView.findViewById(R.id.divider);
            amount = (TextView) itemView.findViewById(R.id.total_amount);
            containerItem = itemView.findViewById(R.id.item_details_container);
            orderTitle = (TextView) itemView.findViewById(R.id.order_title);
        }
    }
}
