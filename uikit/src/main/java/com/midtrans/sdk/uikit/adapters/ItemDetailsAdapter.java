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
    private final String orderId;
    private boolean itemShown = false;

    private List<ItemViewDetails> itemViewDetails;
    private ItemDetailListener listener;

    public interface ItemDetailListener {
        public void onItemShown();
    }

    public ItemDetailsAdapter(List<ItemViewDetails> itemViewDetails, ItemDetailListener listener, String orderId) {
        this.orderId = orderId;
        this.itemViewDetails = itemViewDetails;
        this.listener = listener;
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
                headerViewHolder.amount.setText(itemViewDetails.get(position).getValue());
                headerViewHolder.orderId.setText(this.orderId);
                if (itemViewDetails.get(position).isItemAvailable() && itemShown) {
                    headerViewHolder.containerItem.setVisibility(View.VISIBLE);
                    headerViewHolder.divider.setVisibility(View.VISIBLE);
                } else {
                    headerViewHolder.containerItem.setVisibility(View.GONE);
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
        if (itemShown) {
            return itemViewDetails.size();
        } else {
            return 1;
        }
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
            item = itemView.findViewById(R.id.item_name);
            price = itemView.findViewById(R.id.item_price);
        }
    }

    class ItemHeaderViewHolder extends RecyclerView.ViewHolder {
        TextView amount;
        TextView orderId;
        View containerItem;
        View divider;

        public ItemHeaderViewHolder(final View itemView) {
            super(itemView);
            divider = itemView.findViewById(R.id.divider);
            amount = itemView.findViewById(R.id.total_amount);
            orderId = itemView.findViewById(R.id.text_order_id);
            containerItem = itemView.findViewById(R.id.item_details_container);
        }
    }
}
