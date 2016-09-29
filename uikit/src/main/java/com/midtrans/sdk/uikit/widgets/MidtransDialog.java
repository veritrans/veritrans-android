package com.midtrans.sdk.uikit.widgets;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.midtrans.sdk.uikit.R;

public class MidtransDialog extends android.app.Dialog {

    private String title;
    private String message;
    private TextView titleTextView;
    private TextView messageTextView;
    private Button buttonAccept;
    private Button buttonCancel;
    private View.OnClickListener onAcceptButtonClickListener;
    private View.OnClickListener onCancelButtonClickListener;
    private Context context;
    private String positiveButtonText;
    private String negativeButtonText;
    private ImageView titleImage;
    private Drawable drawable;

    public MidtransDialog(Context context, String title, String message, String positiveButtonText,
                          String negativeButtonText) {

        super(context, R.style.TransperentTheme);

        this.title = title;
        this.context = context;
        this.message = message;
        this.positiveButtonText = positiveButtonText;
        this.negativeButtonText = negativeButtonText;
    }

    public MidtransDialog(Context context, Drawable titleDrawable, String message, String
            positiveButtonText,
                          String negativeButtonText) {
        super(context, R.style.TransperentTheme);

        this.drawable = titleDrawable;
        this.context = context;
        this.message = message;
        this.positiveButtonText = positiveButtonText;
        this.negativeButtonText = negativeButtonText;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_layout);

        this.titleTextView = (TextView) findViewById(R.id.title_tv);
        this.titleImage = (ImageView) findViewById(R.id.title_image);
        this.messageTextView = (TextView) findViewById(R.id.message_tv);

        if (title == null) {
            titleImage.setImageDrawable(drawable);
            titleImage.setVisibility(View.VISIBLE);
            titleTextView.setVisibility(View.GONE);
        } else {
            titleTextView.setVisibility(View.VISIBLE);
            titleImage.setVisibility(View.GONE);
            setTitle(title);
        }

        setMessage(message);

        this.buttonAccept = (Button) findViewById(R.id.btn_accept);
        this.buttonCancel = (Button) findViewById(R.id.btn_cancel);

        if (TextUtils.isEmpty(positiveButtonText)) {
            this.buttonAccept.setVisibility(View.GONE);
        } else {
            this.buttonAccept.setText(positiveButtonText);
        }

        if (TextUtils.isEmpty(negativeButtonText)) {
            this.buttonCancel.setVisibility(View.GONE);
        } else {
            this.buttonCancel.setText(negativeButtonText);
        }


        buttonAccept.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
                if (onAcceptButtonClickListener != null)
                    onAcceptButtonClickListener.onClick(v);
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dismiss();
                if (onCancelButtonClickListener != null)
                    onCancelButtonClickListener.onClick(v);
            }
        });
    }

    private void setMessage(String message) {
        if (TextUtils.isEmpty(message))
            messageTextView.setVisibility(View.GONE);
        else {
            messageTextView.setVisibility(View.VISIBLE);
            messageTextView.setText(message);
        }
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        if (TextUtils.isEmpty(title))
            titleTextView.setVisibility(View.GONE);
        else {
            titleTextView.setVisibility(View.VISIBLE);
            titleTextView.setText(title);
        }
    }


    public TextView getTitleTextView() {
        return titleTextView;
    }

    public void setTitleTextView(TextView titleTextView) {
        this.titleTextView = titleTextView;
    }

    public Button getButtonAccept() {
        return buttonAccept;
    }

    public void setButtonAccept(Button buttonAccept) {
        this.buttonAccept = buttonAccept;
    }

    public Button getButtonCancel() {
        return buttonCancel;
    }

    public void setButtonCancel(Button buttonCancel) {
        this.buttonCancel = buttonCancel;
    }

    public void setOnAcceptButtonClickListener(
            View.OnClickListener onAcceptButtonClickListener) {
        this.onAcceptButtonClickListener = onAcceptButtonClickListener;
        if (buttonAccept != null)
            buttonAccept.setOnClickListener(onAcceptButtonClickListener);
    }

    public void setOnCancelButtonClickListener(
            View.OnClickListener onCancelButtonClickListener) {
        this.onCancelButtonClickListener = onCancelButtonClickListener;
        if (buttonCancel != null)
            buttonCancel.setOnClickListener(onAcceptButtonClickListener);
    }

}