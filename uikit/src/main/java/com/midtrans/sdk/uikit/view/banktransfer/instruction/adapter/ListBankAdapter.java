package com.midtrans.sdk.uikit.view.banktransfer.instruction.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.base.adapter.BaseAdapter;
import com.midtrans.sdk.uikit.widget.DefaultTextView;

import java.util.List;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class ListBankAdapter extends BaseAdapter<String> {

    private List<String> banks;
    private Context context;

    public ListBankAdapter(Context context) {
        this.context = context;
    }

    @Override
    public void addAllData(List<String> data) {
        banks.addAll(data);
        this.notifyDataSetChanged();
    }

    @Override
    public void addData(String data) {
        banks.add(data);
        this.notifyDataSetChanged();
    }

    @Override
    public String getDataAt(int position) {
        return banks.get(position);
    }

    @Override
    public List<String> getAllData() {
        return banks;
    }

    @Override
    public void setData(List<String> data) {
        this.banks = data;
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_item_bank_list, null);
        return new BankItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        BankItemViewHolder itemViewHolder = (BankItemViewHolder) holder;
        int bankIndex = position + 1;
        itemViewHolder.textView.setText(bankIndex + ".   " + banks.get(position));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        if (position % 2 == 0) {
            itemViewHolder.textView
                    .setBackgroundColor(ContextCompat.getColor(context, R.color.light_gray));
        } else {
            itemViewHolder.textView
                    .setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        }
        itemViewHolder.textView.setLayoutParams(params);
    }

    @Override
    public int getItemCount() {
        return banks.size();
    }

    private class BankItemViewHolder extends RecyclerView.ViewHolder {

        DefaultTextView textView;

        BankItemViewHolder(View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.bank_name);
        }
    }
}