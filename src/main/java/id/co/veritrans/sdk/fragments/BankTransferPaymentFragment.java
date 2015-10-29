package id.co.veritrans.sdk.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.models.PermataBankTransferResponse;
import id.co.veritrans.sdk.widgets.TextViewFont;

/**
 * Created by shivam on 10/27/15.
 */
public class BankTransferPaymentFragment extends Fragment {

    private static PermataBankTransferResponse sPermataBankTransferResponse = null;

    private TextViewFont mTextViewVirtualAccountNumber = null;


    public static BankTransferPaymentFragment newInstance(PermataBankTransferResponse
                                                                  permataBankTransferResponse) {
        sPermataBankTransferResponse = permataBankTransferResponse;
        BankTransferPaymentFragment fragment = new BankTransferPaymentFragment();
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bank_transfer_payment, container, false);

        mTextViewVirtualAccountNumber = (TextViewFont)
                view.findViewById(R.id.text_virtual_account_number);

        if(sPermataBankTransferResponse != null){
            if(sPermataBankTransferResponse.getStatus_code().trim().equalsIgnoreCase("200") ||
                    sPermataBankTransferResponse.getStatus_code().trim().equalsIgnoreCase("201")
                    )
            mTextViewVirtualAccountNumber.setText(sPermataBankTransferResponse.getPermata_va_number());

        }else {
            mTextViewVirtualAccountNumber.setText(sPermataBankTransferResponse.getStatus_message());
        }

        return view;
    }

}
