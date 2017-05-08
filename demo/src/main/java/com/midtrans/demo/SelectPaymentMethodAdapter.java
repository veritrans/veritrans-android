package com.midtrans.demo;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rakawm on 5/2/17.
 */

public class SelectPaymentMethodAdapter extends RecyclerView.Adapter<SelectPaymentMethodAdapter.SelectPaymentMethodViewHolder> {
    private List<SelectPaymentMethodViewModel> paymentMethodViewModels;
    private int color;

    public SelectPaymentMethodAdapter() {
        this.paymentMethodViewModels = new ArrayList<>();
    }

    @Override
    public SelectPaymentMethodViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new SelectPaymentMethodViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment_method, parent, false));
    }

    @Override
    public void onBindViewHolder(SelectPaymentMethodViewHolder holder, int position) {
        final SelectPaymentMethodViewModel model = paymentMethodViewModels.get(position);
        holder.paymentMethod.setOnCheckedChangeListener(null);
        holder.paymentMethod.setText(model.getMethodName());
        holder.paymentMethod.setChecked(model.isSelected());
        holder.paymentMethod.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                model.setSelected(checked);
                notifyDataSetChanged();
            }
        });

        if (color != 0) {
            int[][] states = new int[][]{
                    new int[]{-android.R.attr.state_checked},
                    new int[]{android.R.attr.state_checked},
            };

            int[] colors = new int[]{
                    Color.GRAY,
                    color,
            };
            ColorStateList colorStateList = new ColorStateList(
                    states,
                    colors
            );

            holder.paymentMethod.setSupportButtonTintList(colorStateList);
        }
    }

    @Override
    public int getItemCount() {
        return paymentMethodViewModels.size();
    }

    public List<SelectPaymentMethodViewModel> getSelectedPaymentMethods() {
        List<SelectPaymentMethodViewModel> viewModels = new ArrayList<>();
        for (SelectPaymentMethodViewModel model : paymentMethodViewModels) {
            if (model.isSelected()) {
                viewModels.add(model);
            }
        }
        return viewModels;
    }

    public boolean isAllPaymentMethodsSelected() {
        for (SelectPaymentMethodViewModel model : paymentMethodViewModels) {
            if (!model.isSelected()) {
                return false;
            }
        }
        return true;
    }

    public void setPaymentMethodViewModels(List<SelectPaymentMethodViewModel> viewModels) {
        paymentMethodViewModels = viewModels;
        notifyDataSetChanged();
    }

    public void setColor(int color) {
        this.color = color;
        notifyDataSetChanged();
    }

    class SelectPaymentMethodViewHolder extends RecyclerView.ViewHolder {
        private AppCompatCheckBox paymentMethod;

        public SelectPaymentMethodViewHolder(View itemView) {
            super(itemView);
            initViews();
        }

        private void initViews() {
            paymentMethod = (AppCompatCheckBox) itemView.findViewById(R.id.checkbox_method_name);
        }
    }
}
