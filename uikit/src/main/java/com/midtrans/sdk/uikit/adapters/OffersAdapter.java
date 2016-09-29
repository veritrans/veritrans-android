package com.midtrans.sdk.uikit.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.midtrans.sdk.corekit.models.OffersListModel;
import com.midtrans.sdk.uikit.R;

import java.util.ArrayList;

/**
 * adapter for offers list recycler view.
 * <p/>
 * Created by Ankit on 12/7/15.
 */
public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter.OfferViewHolder> {

    ArrayList<OffersListModel> mData = null;

    public OffersAdapter(ArrayList<OffersListModel> data) {
        mData = data;
    }

    public OffersListModel getItemAtPosition(int position) {
        return mData.get(position);
    }

    @Override
    public OfferViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_row_offers_list, parent, false);

        OfferViewHolder offerViewHolder = new OfferViewHolder(view);
        return offerViewHolder;
    }

    @Override
    public void onBindViewHolder(OfferViewHolder holder, int position) {
        holder.textViewOfferTitle.setText(mData.get(position).getOfferName());
        holder.textViewOfferDescription.setText(mData.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    /**
     * public static view holder class.
     */
    public static class OfferViewHolder extends RecyclerView.ViewHolder {

        TextView textViewOfferTitle;
        TextView textViewOfferDescription;

        public OfferViewHolder(View itemView) {
            super(itemView);
            textViewOfferTitle = (TextView) itemView.findViewById(R.id.text_offer_title);
            textViewOfferDescription = (TextView) itemView.findViewById(R.id.text_offer_description);
        }
    }
}
