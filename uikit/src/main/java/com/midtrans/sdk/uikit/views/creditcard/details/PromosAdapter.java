package com.midtrans.sdk.uikit.views.creditcard.details;

import android.os.Handler;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.midtrans.sdk.corekit.models.promo.Promo;
import com.midtrans.sdk.corekit.utilities.Utils;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ziahaqi on 2/12/18.
 */

public class PromosAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final PromosListener listener;
    private List<Promo> promos;

    public PromosAdapter(PromosListener listener) {
        this.promos = new ArrayList<>();
        this.listener = listener;
    }

    public void setData(List<Promo> promos) {
        if (promos != null && !promos.isEmpty()) {
            this.promos.clear();
            this.promos.addAll(promos);
            notifyDataSetChanged();
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_row_promos, parent, false);
        return new PromoHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        PromoHolder myHolder = (PromoHolder) holder;
        myHolder.bindView(promos.get(position));
    }

    @Override
    public int getItemCount() {
        return promos.size();
    }

    private void changePromoState(Promo myPromo, boolean isChecked) {
        for (Promo promo : promos) {
            if (promo.getId() == myPromo.getId()) {
                promo.setSelected(isChecked);
            } else {
                promo.setSelected(false);
            }
        }
    }

    public Promo getSeletedPromo() {
        for (Promo promo : promos) {
            if (promo.isSelected()) {
                return promo;
            }
        }
        return null;
    }

    private class PromoHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {
        private AppCompatCheckBox checkPromoName;
        private DefaultTextView discountAmount;

        public PromoHolder(View itemView) {
            super(itemView);
            checkPromoName = itemView.findViewById(R.id.check_promo_name);
            discountAmount = itemView.findViewById(R.id.text_promo_amount);
            getAdapterPosition();
        }

        public void bindView(final Promo promo) {
            checkPromoName.setOnCheckedChangeListener(null);

            checkPromoName.setText(promo.getName());
            discountAmount.setText(itemView.getContext().getString(R.string.prefix_money_negative,
                    Utils.getFormattedAmount(promo.getCalculatedDiscountAmount())));

            if (promo.isSelected()) {
                checkPromoName.setChecked(true);
            } else {
                checkPromoName.setChecked(false);
            }

            checkPromoName.setOnCheckedChangeListener(this);
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            Promo promo = promos.get(getAdapterPosition());
            changePromoState(promo, isChecked);

            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    notifyDataSetChanged();
                }
            });

            if (listener != null) {
                listener.onPromoSelected(promo);
            }
        }
    }


    public interface PromosListener {
        void onPromoSelected(Promo promo);
    }

}
