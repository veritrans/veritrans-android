package com.midtrans.sdk.uikit.views.qris.list;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.midtrans.sdk.corekit.core.Logger;
import com.midtrans.sdk.corekit.models.snap.EnabledPayment;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.models.Qris;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;
import java.util.ArrayList;
import java.util.List;

public class QrisListAdapter extends RecyclerView.Adapter<QrisListAdapter.QrisListViewHolder> {

    private QrisListAdapterListener listener;
    private List<Qris> mData = new ArrayList<>();

    QrisListAdapter(QrisListAdapterListener listener) {
        this.listener = listener;
    }

    public void setData(List<Qris> qrisList) {
        this.mData.clear();
        if (qrisList != null) {
            this.mData.addAll(qrisList);
        }
        this.notifyDataSetChanged();
    }

    public Qris getItem(int position) {
        return mData.get(position);
    }

    @NonNull
    @Override
    public QrisListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.
            from(parent.getContext()).
            inflate(R.layout.layout_row_payment_methods, parent, false);
        return new QrisListViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull QrisListViewHolder holder, int position) {
        Qris qris = mData.get(position);
        if (qris != null) {
            holder.qrisName.setText(mData.get(position).getName());
            holder.qrisDescription.setText(mData.get(position).getDescription());
            holder.qrisIcon.setImageResource(mData.get(position).getImage());
            Logger.d(this.getClass().getSimpleName(), "Qris Item: " + mData.get(position).getName());

            disablePaymentView(holder, qris);
        }
    }

    private void disablePaymentView(QrisListViewHolder holder, Qris qris) {
        if (!TextUtils.isEmpty(qris.getStatus())
            && qris.getStatus().equals(EnabledPayment.STATUS_DOWN)) {
            holder.layoutPaymentUnavailable.setVisibility(View.VISIBLE);
            holder.itemView.setClickable(false);
            holder.textUnavailable.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public interface QrisListAdapterListener {
        void onItemClick(int position);
    }

    class QrisListViewHolder extends RecyclerView.ViewHolder {
        TextView qrisName;
        TextView qrisDescription;
        ImageView qrisIcon;
        DefaultTextView textUnavailable;
        LinearLayout layoutPaymentUnavailable;

        QrisListViewHolder(View itemView, final QrisListAdapterListener listener) {
            super(itemView);
            qrisName = itemView.findViewById(R.id.text_payment_method_name);
            qrisIcon = itemView.findViewById(R.id.img_payment_method_icon);
            qrisDescription = itemView.findViewById(R.id.text_payment_method_description);
            textUnavailable = itemView.findViewById(R.id.text_option_unavailable);
            layoutPaymentUnavailable = itemView.findViewById(R.id.layout_payment_unavailable);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (listener != null) {
                        listener.onItemClick(getAdapterPosition());
                    }
                }
            });
        }
    }

}
