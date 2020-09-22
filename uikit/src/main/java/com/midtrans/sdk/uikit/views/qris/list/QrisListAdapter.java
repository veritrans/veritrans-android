package com.midtrans.sdk.uikit.views.qris.list;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.views.banktransfer.list.BankTransferListAdapter.BankTransferAdapterListener;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;

public class QrisListAdapter extends RecyclerView.Adapter<QrisListAdapter.QrisListViewHolder> {

    private QrisListAdapterListener listener;

    public QrisListAdapter(QrisListAdapterListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public QrisListViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull QrisListViewHolder qrisListViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
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

        QrisListViewHolder(View itemView, final BankTransferAdapterListener listener) {
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
