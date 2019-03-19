package com.midtrans.sdk.uikit.view.method.creditcard.details.adapter;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import com.midtrans.sdk.corekit.core.api.snap.model.paymentinfo.promo.Promo;
import com.midtrans.sdk.corekit.utilities.Logger;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.base.adapter.BaseAdapter;
import com.midtrans.sdk.uikit.base.composer.BasePaymentActivity;
import com.midtrans.sdk.uikit.utilities.CurrencyHelper;
import com.midtrans.sdk.uikit.widget.DefaultTextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.recyclerview.widget.RecyclerView;

public class PromosAdapter extends BaseAdapter<Promo> {

    private final PromoListener listener;
    private final int colorPrimer;
    private final String currency;
    private List<Promo> promos;

    public PromosAdapter(int colorPrimer, String currency, PromoListener listener) {
        this.listener = listener;
        this.colorPrimer = colorPrimer;
        this.currency = currency;
        this.promos = new ArrayList<>();
    }

    @Override
    public PromoHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_promo, parent, false);
        return new PromoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {
        PromoHolder holder = (PromoHolder) viewHolder;
        holder.bindView(promos.get(position));
    }

    @Override
    public void addAllData(List<Promo> data) {
        this.promos.addAll(data);
        this.notifyDataSetChanged();
    }

    @Override
    public void addData(Promo data) {
        this.promos.add(data);
        this.notifyDataSetChanged();
    }

    @Override
    public Promo getDataAt(int position) {
        return promos.get(position);
    }

    @Override
    public List<Promo> getAllData() {
        return promos;
    }

    @Override
    public void setData(List<Promo> data) {
        if (data != null) {
            this.promos.clear();
            this.promos.addAll(data);
            this.notifyDataSetChanged();
        }
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

    public interface PromoListener {

        void onPromoCheckedChanged(Promo promo);

        void onPromoUnavailable();
    }

    private class PromoHolder extends RecyclerView.ViewHolder implements CompoundButton.OnCheckedChangeListener {
        private AppCompatCheckBox checkPromoName;
        private DefaultTextView discountAmount;

        public PromoHolder(View itemView) {
            super(itemView);
            checkPromoName = itemView.findViewById(R.id.check_promo_name);
            discountAmount = itemView.findViewById(R.id.text_promo_amount);

            if (itemView.getContext() instanceof BasePaymentActivity) {
                try {
                    ((BasePaymentActivity) itemView.getContext()).setCheckboxStateColor(checkPromoName);
                } catch (RuntimeException e) {
                    Logger.error("renderCheckbox" + e.getMessage());
                }
            }
        }

        public void bindView(final Promo promo) {
            checkPromoName.setOnCheckedChangeListener(null);

            checkPromoName.setText(promo.getName());
            discountAmount.setText(CurrencyHelper.getFormattedNegativeAmount(
                    itemView.getContext(),
                    promo.getCalculatedDiscountAmount(),
                    currency)
            );

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

            new Handler().post(() -> notifyDataSetChanged());

            if (listener != null) {
                listener.onPromoCheckedChanged(promo);
            }
        }
    }
}