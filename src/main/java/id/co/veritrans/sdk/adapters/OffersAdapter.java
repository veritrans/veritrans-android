package id.co.veritrans.sdk.adapters;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.activities.OffersActivity;
import id.co.veritrans.sdk.callbacks.AnyOfferClickedListener;
import id.co.veritrans.sdk.models.OffersListModel;
import id.co.veritrans.sdk.widgets.TextViewFont;

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
        this.sActivity = activity;
        this.data = data;
        this.anyOfferClickedListener = anyOfferClickedListener;
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
        holder.textViewOfferTitle.setText(data.get(position).getOfferTitle());
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

        TextViewFont textViewOfferTitle;

        public OfferViewHolder(View itemView) {
            super(itemView);
            textViewOfferTitle = (TextViewFont) itemView.findViewById(R.id.text_offer_title);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            anyOfferClickedListener.onOfferClicked(getAdapterPosition(), data.get
                    (getAdapterPosition()).getOfferTitle());
        }
    }
}
