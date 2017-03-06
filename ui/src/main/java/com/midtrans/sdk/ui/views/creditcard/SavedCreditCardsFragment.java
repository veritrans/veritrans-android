package com.midtrans.sdk.ui.views.creditcard;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.midtrans.sdk.ui.abtracts.BaseFragment;

/**
 * Created by ziahaqi on 2/26/17.
 */

public class SavedCreditCardsFragment extends BaseFragment implements CreditCardContract.SavedCreditCardsView {
    private CreditCardContract.Presenter presenter;

    public static SavedCreditCardsFragment newInstance() {
        SavedCreditCardsFragment fragment = new SavedCreditCardsFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }



    @Override
    public void setPresenter(@NonNull CreditCardContract.Presenter presenter) {
        this.presenter = presenter;
    }
}
