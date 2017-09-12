package com.midtrans.sdk.uikit.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.widgets.DefaultTextView;

/**
 * Created by Fajar on 8/17/17.
 */

public class ListBankAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private String[] banks;
    private Context context;

    public ListBankAdapter(String[] banks, Context context) {
        this.banks = banks;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.bank_item_viewholder, null);
        return new BankItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        BankItemViewHolder itemViewHolder = (BankItemViewHolder) holder;
        int bankIndex = position + 1;
        itemViewHolder.textView.setText(bankIndex + ".   " + banks[position]);
        LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT);
        if (position % 2 == 0) {
            itemViewHolder.textView
                .setBackgroundColor(ContextCompat.getColor(context, R.color.light_gray));
        } else {
            itemViewHolder.textView
                .setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        }
        //set layout size after applying background
        itemViewHolder.textView.setLayoutParams(params);
    }

    @Override
    public int getItemCount() {
        return banks.length;
    }

    private class BankItemViewHolder extends RecyclerView.ViewHolder {

        DefaultTextView textView;

        BankItemViewHolder(View itemView) {
            super(itemView);
            textView = (DefaultTextView) itemView.findViewById(R.id.bank_name);
        }
    }
}
