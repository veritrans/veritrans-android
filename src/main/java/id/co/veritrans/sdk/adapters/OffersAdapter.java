package id.co.veritrans.sdk.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.activities.OffersActivity;
import id.co.veritrans.sdk.callbacks.AnyOfferClickedListener;
import id.co.veritrans.sdk.models.OffersListModel;

/**
 * adapter for offers list recycler view.
 * <p/>
 * Created by Ankit on 12/7/15.
 */
public class OffersAdapter extends RecyclerView.Adapter<OffersAdapter
        .OfferViewHolder> {

    private static Activity sActivity;
    private static ArrayList<OffersListModel> data = null;
    private static AnyOfferClickedListener anyOfferClickedListener = null;

    public OffersAdapter(Activity activity, ArrayList<OffersListModel> data,
                         AnyOfferClickedListener anyOfferClickedListener) {
        sActivity = activity;
        OffersAdapter.data = data;
        OffersAdapter.anyOfferClickedListener = anyOfferClickedListener;
    }


    @Override
    public OfferViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_row_offers_list, parent, false);

        OfferViewHolder offerViewHolder = new OfferViewHolder(view);
        return offerViewHolder;
    }

    @Override
    public void onBindViewHolder(OfferViewHolder holder, int position) {
        holder.textViewOfferTitle.setText(data.get(position).getOfferName());
        holder.textViewOfferDescription.setText(data.get(position).getDescription());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    /**
     * public static view holder class.
     */
    public static class OfferViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {

        TextView textViewOfferTitle;
        TextView textViewOfferDescription;

        public OfferViewHolder(View itemView) {
            super(itemView);
            textViewOfferTitle = (TextView) itemView.findViewById(R.id.text_offer_title);
            textViewOfferDescription = (TextView) itemView.findViewById(R.id.text_offer_description);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            String offerType = null;
            if (data.get(getAdapterPosition()).getDuration() != null && !data.get(getAdapterPosition()).getDuration().isEmpty()) {
                offerType = OffersActivity.OFFER_TYPE_INSTALMENTS;
            } else {
                offerType = OffersActivity.OFFER_TYPE_BINPROMO;
            }

            anyOfferClickedListener.onOfferClicked(getAdapterPosition(), data.get
                    (getAdapterPosition()).getOfferName(), offerType);
        }
    }
}
