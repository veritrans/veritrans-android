package com.midtrans.sdk.uikit.widgets;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.midtrans.sdk.corekit.core.MidtransSDK;
import com.midtrans.sdk.uikit.R;

/**
 * Created by chetan on 29/10/15.
 */
public class MidtransLoadingDialog extends Dialog {
    private String message = "";
    private ProgressWheel progressWheel;
    private TextView messageTv;

    public MidtransLoadingDialog(Context context) {
        super(context);
    }

    public MidtransLoadingDialog(Context context, String message) {
        super(context);
        this.message = message.trim();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.layout_material_progress);
        MidtransSDK midtransSDK = MidtransSDK.getInstance();
        progressWheel = (ProgressWheel) findViewById(R.id.progress_wheel);
        messageTv = (TextView) findViewById(R.id.text_loading_message);
        if (!TextUtils.isEmpty(message)) {
            messageTv.setVisibility(View.VISIBLE);
            messageTv.setText(message);
        } else {
            messageTv.setVisibility(View.GONE);
        }
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
    }

}
