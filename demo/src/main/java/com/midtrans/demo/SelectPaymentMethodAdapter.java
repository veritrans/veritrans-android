package com.midtrans.demo;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by rakawm on 5/2/17.
 */

public class SelectPaymentMethodAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private int TYPE_HEADER = 0;
    private int TYPE_ITEM = 1;
    private List<SelectPaymentMethodViewModel> paymentMethodViewModels;
    private int color;

    public SelectPaymentMethodAdapter() {
        this.paymentMethodViewModels = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_HEADER) {
            return new HeaderPaymentMethodViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.header_payment_method, parent, false));
        }

        return new SelectPaymentMethodViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_payment_method, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final SelectPaymentMethodViewModel model = paymentMethodViewModels.get(position);

        if (TextUtils.isEmpty(model.getCategory())) {
            HeaderPaymentMethodViewHolder headerViewHolder = (HeaderPaymentMethodViewHolder) holder;
            headerViewHolder.bind(model);

        } else {
            SelectPaymentMethodViewHolder itemHolder = (SelectPaymentMethodViewHolder) holder;
            itemHolder.paymentMethod.setOnCheckedChangeListener(null);
            itemHolder.paymentMethod.setText(model.getMethodName());
            itemHolder.paymentMethod.setChecked(model.isSelected());
            itemHolder.paymentMethod.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                    model.setSelected(checked);
                    notifyDataSetChanged();
                }
            });

            if (needToAddDivider(position)) {
                itemHolder.viewDivider.setVisibility(View.VISIBLE);
            } else {
                itemHolder.viewDivider.setVisibility(View.GONE);
            }

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

                itemHolder.paymentMethod.setSupportButtonTintList(colorStateList);
            }
        }

    }

    private boolean needToAddDivider(int position) {
        int nextPosition = ++position;
        if (nextPosition < paymentMethodViewModels.size()) {
            SelectPaymentMethodViewModel nexModel = paymentMethodViewModels.get(nextPosition);
            if (nexModel != null && TextUtils.isEmpty(nexModel.getCategory())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getItemCount() {
        return paymentMethodViewModels.size();
    }

    public List<SelectPaymentMethodViewModel> getSelectedPaymentMethods() {
        List<SelectPaymentMethodViewModel> viewModels = new ArrayList<>();
        for (SelectPaymentMethodViewModel model : paymentMethodViewModels) {
            if (!TextUtils.isEmpty(model.getCategory()) && model.isSelected()) {
                viewModels.add(model);
            }
        }
        return viewModels;
    }

    public boolean isAllPaymentMethodsSelected() {
        for (SelectPaymentMethodViewModel model : paymentMethodViewModels) {
            if (!TextUtils.isEmpty(model.getCategory()) && !model.isSelected()) {
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
        private View viewDivider;

        public SelectPaymentMethodViewHolder(View itemView) {
            super(itemView);
            initViews();
        }

        private void initViews() {
            paymentMethod = (AppCompatCheckBox) itemView.findViewById(R.id.checkbox_method_name);
            viewDivider = itemView.findViewById(R.id.view_divider);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return TextUtils.isEmpty(paymentMethodViewModels.get(position).getCategory()) ? TYPE_HEADER : TYPE_ITEM;
    }

    private class HeaderPaymentMethodViewHolder extends RecyclerView.ViewHolder {
        private TextView textHeader;

        public HeaderPaymentMethodViewHolder(View viewItem) {
            super(viewItem);
            textHeader = (TextView) itemView.findViewById(R.id.text_method_header);
        }

        public void bind(SelectPaymentMethodViewModel data) {
            if (data != null) {
                textHeader.setText(data.getMethodName());
            }
        }
    }
}
