package com.midtrans.sdk.uikit.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.corekit.models.TransactionResponse;
import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.activities.KlikBCAInstructionActivity;
import com.midtrans.sdk.uikit.widgets.FancyButton;

/**
 * Created by rakawm on 2/15/17.
 * Deprecated, please use {@link com.midtrans.sdk.uikit.views.bca_klikbca.status.KlikBcaStatusActivity}
 */

@Deprecated
public class KlikBCAStatusFragment extends Fragment {
    private static final String RESPONSE = "response";
    TextView expire;
    FancyButton seeInstruction;

    public static KlikBCAStatusFragment newInstance(TransactionResponse transactionResponse) {
        KlikBCAStatusFragment klikBCAStatusFragment = new KlikBCAStatusFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(RESPONSE, transactionResponse);
        klikBCAStatusFragment.setArguments(bundle);
        return klikBCAStatusFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_klik_bca_status, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        expire = (TextView) view.findViewById(R.id.expire_text);
        seeInstruction = (FancyButton) view.findViewById(R.id.btn_see_instruction);

        initExpire();
        initSeeInstruction();
    }

    private void initExpire() {
        TransactionResponse transactionResponse = (TransactionResponse) getArguments().getSerializable(RESPONSE);
        if (transactionResponse != null) {
            expire.setText(transactionResponse.getBcaKlikBcaExpiration());
        }
    }

    private void initSeeInstruction() {
        if (MidtransSDK.getInstance() != null && MidtransSDK.getInstance().getColorTheme() != null) {
            if (MidtransSDK.getInstance().getColorTheme().getPrimaryDarkColor() != 0) {
                seeInstruction.setBorderColor(MidtransSDK.getInstance().getColorTheme().getPrimaryDarkColor());
                seeInstruction.setTextColor(MidtransSDK.getInstance().getColorTheme().getPrimaryDarkColor());
            }
        }
        seeInstruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), KlikBCAInstructionActivity.class);
                startActivity(intent);
                if (MidtransSDK.getInstance().getUIKitCustomSetting() != null
                        && MidtransSDK.getInstance().getUIKitCustomSetting().isEnabledAnimation()) {
                    getActivity().overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
                }
            }
        });
    }
}
