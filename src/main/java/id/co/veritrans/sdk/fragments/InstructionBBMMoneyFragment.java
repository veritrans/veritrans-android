package id.co.veritrans.sdk.fragments;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.core.Logger;

/**
 * Created by Ankit on 12/03/15.
 */
public class InstructionBBMMoneyFragment extends Fragment implements View.OnClickListener {

    private static final String BBM_MONEY_PACKAGE = "com.monitise.client.android.bbmmoney";
    private static final String MARKET_URL = "market://details?id=";
    private static final String PLAY_STORE_URL = "https://play.google.com/store/apps/details?id=";
    private LinearLayout layoutGetBBMMoneyApp = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_instruction_bbm_money, container, false);
        initialiseView(view);
        return view;
    }

    private void initialiseView(View view){
        layoutGetBBMMoneyApp = (LinearLayout) view.findViewById(R.id.layout_get_bbm__money_app);
        if (isBBMMoneyInstalled(BBM_MONEY_PACKAGE)){
            layoutGetBBMMoneyApp.setVisibility(View.GONE);
        } else {
            layoutGetBBMMoneyApp.setVisibility(View.VISIBLE);
        }

        layoutGetBBMMoneyApp.setOnClickListener(this);
    }

    private boolean isBBMMoneyInstalled(String bbmUri) {
        boolean isInstalled = false;
        PackageManager pm = getActivity().getPackageManager();
        try {
            pm.getPackageInfo(bbmUri, PackageManager.GET_ACTIVITIES);
            isInstalled = true;
        } catch (PackageManager.NameNotFoundException e) {
            isInstalled = false;
        }

        return isInstalled;
    }

    private void openPlayStore() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(MARKET_URL + BBM_MONEY_PACKAGE)));
        } catch (ActivityNotFoundException activityNotFoundException) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(PLAY_STORE_URL + BBM_MONEY_PACKAGE)));
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.layout_get_bbm__money_app) {
            openPlayStore();
        }
    }
}
