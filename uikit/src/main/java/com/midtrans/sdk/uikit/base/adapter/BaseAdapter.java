package com.midtrans.sdk.uikit.base.adapter;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;

public abstract class BaseAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public abstract void addAllData(List<T> data);

    public abstract void addData(T data);

    public abstract T getDataAt(int position);

    public abstract List<T> getAllData();

    public abstract void setData(List<T> data);
}