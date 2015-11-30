package id.co.veritrans.sdk.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.activities.BankTransferInstructionActivity;
import id.co.veritrans.sdk.core.Constants;
import id.co.veritrans.sdk.core.VeritransSDK;
import id.co.veritrans.sdk.widgets.TextViewFont;

/**
 * It displays payment related instructions on the screen.
 * Created by shivam on 10/27/15.
 */
public class BankTransferFragment extends Fragment {

    private TextViewFont mTextViewSeeInstruction = null;
    private EditText mEditTextEmailId = null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_bank_transfer, container, false);
        return view;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initializeViews(view);
    }

    /**
     * initializes view and adds click listener for it.
     *
     * @param view
     */
    private void initializeViews(View view) {

        mTextViewSeeInstruction = (TextViewFont)
                view.findViewById(R.id.text_see_instruction);

        mEditTextEmailId = (EditText) view.findViewById(R.id.et_email);
        VeritransSDK veritransSDK = VeritransSDK.getVeritransSDK();
        try {
            mEditTextEmailId.setText(veritransSDK.getTransactionRequest().getCustomerDetails()
                    .getEmail());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        mTextViewSeeInstruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getActivity() != null) {

                    Intent intent = new Intent(getActivity(),
                            BankTransferInstructionActivity.class);
                    intent.putExtra(Constants.POSITION, 0);
                    getActivity().startActivity(intent);

                }
            }
        });
    }


    /**
     * created to give access to email id field from {@link id.co.veritrans.sdk.activities
     * .BankTransferActivity}.
     *
     * @return
     */
    public String getEmailId() {
        if (mEditTextEmailId != null) {
            return mEditTextEmailId.getText().toString();
        } else {
            return null;
        }
    }
}