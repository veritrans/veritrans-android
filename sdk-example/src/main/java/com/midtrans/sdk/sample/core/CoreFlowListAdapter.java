package com.midtrans.sdk.sample.core;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import com.midtrans.sdk.sample.R;

/**
 * Created by rakawm on 6/2/16.
 */
public class CoreFlowListAdapter extends RecyclerView.Adapter<CoreFlowListAdapter.CoreFlowViewHolder> {

    private List<CoreViewModel> data;

    public CoreFlowListAdapter() {
        this.data = new ArrayList<>();
    }

    public CoreFlowListAdapter(List<CoreViewModel> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void setData(List<CoreViewModel> data) {
        this.data.clear();
        this.data.addAll(data);
    }

    public CoreViewModel getItem(int position) {
        return data.get(position);
    }

    @Override
    public CoreFlowViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CoreFlowViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_core_flow, parent, false));
    }

    @Override
    public void onBindViewHolder(CoreFlowViewHolder holder, int position) {
        CoreViewModel model = data.get(position);
        holder.title.setText(model.getTitle());
        holder.logo.setImageResource(model.getImage());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void clear() {
        this.data.clear();
    }

    class CoreFlowViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView logo;

        public CoreFlowViewHolder(View itemView) {
            super(itemView);
            title = (TextView) itemView.findViewById(R.id.title);
            logo = (ImageView) itemView.findViewById(R.id.logo);
        }
    }
}
