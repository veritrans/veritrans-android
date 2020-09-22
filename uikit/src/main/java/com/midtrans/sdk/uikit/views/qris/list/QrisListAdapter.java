package com.midtrans.sdk.uikit.views.qris.list;

public class QrisListAdapter {

    private QrisListAdapterListener listener;

    public QrisListAdapter(QrisListAdapterListener listener) {
        this.listener = listener;
    }

    public interface QrisListAdapterListener {
        void onItemClick(int position);
    }

}
