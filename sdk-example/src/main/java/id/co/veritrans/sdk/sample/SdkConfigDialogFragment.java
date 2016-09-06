package id.co.veritrans.sdk.sample;

import android.app.Dialog;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.DialogFragment;
import android.text.TextUtils;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import id.co.veritrans.sdk.coreflow.core.Logger;
import id.co.veritrans.sdk.coreflow.core.VeritransSDK;


/**
 * Created by ziahaqi on 8/18/16.
 */
public class SdkConfigDialogFragment extends DialogFragment {
    public static final java.lang.String TAG = "SdkConfigDialogFragment";
    private Button btnSave, btnCancel;
    private EditText editSnapUrl, editMerchantUrl, editClientKey, editTimeout;
    private TextInputLayout tilSnapUrl, tilMerchantUrl, tilClientKey, tilTimeout;
    private String merchantUrl;
    private String merchantClientKey;
    private String snapUrl;
    private String requestTimeout;


    public static SdkConfigDialogFragment createInstance(){
        SdkConfigDialogFragment fragment = new SdkConfigDialogFragment();
        return fragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if(dialog != null){
            Display display = getActivity().getWindowManager().getDefaultDisplay();
            Point screenSize = new Point();
            display.getSize(screenSize);
            int width = screenSize.x;
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
            layoutParams.copyFrom(dialog.getWindow().getAttributes());
            layoutParams.width = (int) (width);
            dialog.getWindow().setAttributes(layoutParams);
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = new Dialog(getContext(), R.style.AlertDialogCustom);
        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_sdk_config, container, false);
        btnSave = (Button) view.findViewById(R.id.btn_sdkconfig_save);
        btnCancel = (Button)view.findViewById(R.id.btn_sdkconfig_cancel);
        editSnapUrl = (EditText)view.findViewById(R.id.et_sdkconfig_snap_url);
        editMerchantUrl = (EditText)view.findViewById(R.id.et_sdkconfig_merchant_url);
        editClientKey = (EditText)view.findViewById(R.id.et_sdkconfig_clientkey);
        editTimeout = (EditText)view.findViewById(R.id.et_sdkconfig_timeout);

        tilClientKey = (TextInputLayout)view.findViewById(R.id.til_sdkconfig_clientkey);
        tilMerchantUrl = (TextInputLayout)view.findViewById(R.id.til_sdkconfig_merchant_url);
        tilSnapUrl = (TextInputLayout)view.findViewById(R.id.til_sdkconfig_snap_url);
        tilTimeout = (TextInputLayout)view.findViewById(R.id.til_sdkconfig_timeout);
        Logger.d("edittimeout:first");
        initForm();

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!checkFormValidity()){
                    return;
                }
                VeritransSDK.getInstance().changeSdkConfig(snapUrl, merchantUrl,merchantClientKey, Integer.valueOf(requestTimeout));
                dismiss();
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });

        return view;
    }

    private void initForm() {
        VeritransSDK veritransSDK = VeritransSDK.getInstance();
        Logger.d("edittimeout:" + veritransSDK.getRequestTimeOut());
        Logger.d("edittimeout>view:" + editTimeout);

        editSnapUrl.setText(veritransSDK.getSdkBaseUrl());
        editMerchantUrl.setText(veritransSDK.getMerchantServerUrl());
        editClientKey.setText(veritransSDK.getClientKey());

        editTimeout.setText(String.valueOf(veritransSDK.getRequestTimeOut()));
    }

    private boolean checkFormValidity(){
        boolean isvalid = true;
        snapUrl = editSnapUrl.getText().toString().trim();
        merchantUrl = editMerchantUrl.getText().toString().trim();
        merchantClientKey = editClientKey.getText().toString().trim();
        requestTimeout = editTimeout.getText().toString().trim();

        if(TextUtils.isEmpty(snapUrl)){
            tilSnapUrl.setError(getString(R.string.error_sdkconfig_empty));
            isvalid = false;
        }
        if(TextUtils.isEmpty(merchantUrl)){
            tilMerchantUrl.setError(getString(R.string.error_sdkconfig_empty));
            isvalid = false;
        }
        if(TextUtils.isEmpty(merchantClientKey)){
            tilClientKey.setError(getString(R.string.error_sdkconfig_empty));
            isvalid = false;
        }
        if(TextUtils.isEmpty(requestTimeout)){
            tilTimeout.setError(getString(R.string.error_sdkconfig_empty));
            isvalid = false;
        }
        if(!TextUtils.isDigitsOnly(requestTimeout)){
            tilTimeout.setError(getString(R.string.error_sdkconfig_digitonly));
            isvalid = false;
        }

        return isvalid;
    }
}
