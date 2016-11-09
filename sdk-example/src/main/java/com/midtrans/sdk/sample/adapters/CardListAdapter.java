package com.midtrans.sdk.sample.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.midtrans.sdk.corekit.models.SaveCardRequest;
import com.midtrans.sdk.sample.R;

import java.util.ArrayList;

/**
 * Created by ziahaqi on 10/18/16.
 */

public class CardListAdapter extends RecyclerView.Adapter<CardListAdapter.CardViewHolder> {

    private final CardListListener listener;
    ArrayList<SaveCardRequest> cardList = new ArrayList<>();

    public SaveCardRequest getItem(int position) {
        return cardList.get(position);
    }

    public interface CardListListener{
        public void onclick(int position);
    }
    public CardListAdapter(CardListListener listener) {
        this.listener = listener;
    }

    public void  setData(ArrayList<SaveCardRequest> cardList){
        this.cardList.clear();
        this.cardList.addAll(cardList);
        this.notifyDataSetChanged();
    }
    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_row_cardlist, parent, false);
        CardViewHolder viewHolder = new CardViewHolder(view, listener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        holder.textCardNumber.setText(cardList.get(position).getMaskedCard());
    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    static class CardViewHolder extends RecyclerView.ViewHolder{
        TextView textCardNumber;
        public CardViewHolder(View itemView, final CardListListener listener) {
            super(itemView);
            textCardNumber = (TextView) itemView.findViewById(R.id.text_cardnumber_row);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(listener != null){
                        listener.onclick(getAdapterPosition());
                    }
                }
            });
        }
    }
}
