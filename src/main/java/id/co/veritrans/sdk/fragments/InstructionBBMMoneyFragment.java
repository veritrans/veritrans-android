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
import id.co.veritrans.sdk.core.Constants;
import id.co.veritrans.sdk.core.Logger;

/**
 * Created by Ankit on 12/03/15.
 */
public class InstructionBBMMoneyFragment extends Fragment implements View.OnClickListener {


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
        if (isBBMMoneyInstalled(Constants.BBM_MONEY_PACKAGE)){
            layoutGetBBMMoneyApp.setVisibility(View.GONE);
        } else {
            layoutGetBBMMoneyApp.setVisibility(View.VISIBLE);
        }

        layoutGetBBMMoneyApp.setOnClickListener(this);
    }

    public boolean isBBMMoneyInstalled(String bbmUri) {
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

    public void openPlayStore() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.MARKET_URL +
                    Constants.BBM_MONEY_PACKAGE)));
        } catch (ActivityNotFoundException activityNotFoundException) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Constants.PLAY_STORE_URL + Constants.BBM_MONEY_PACKAGE)));
        }
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.layout_get_bbm__money_app) {
            openPlayStore();
        }
    }
}
