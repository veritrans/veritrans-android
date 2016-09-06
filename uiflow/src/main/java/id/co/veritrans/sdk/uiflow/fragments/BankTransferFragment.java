package id.co.veritrans.sdk.uiflow.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import id.co.veritrans.sdk.coreflow.core.LocalDataHandler;
import id.co.veritrans.sdk.coreflow.core.VeritransSDK;
import id.co.veritrans.sdk.coreflow.models.UserDetail;
import id.co.veritrans.sdk.uiflow.R;
import id.co.veritrans.sdk.uiflow.activities.BankTransferInstructionActivity;

/**
 * It displays payment related instructions on the screen.
 * Created by shivam on 10/27/15.
 */
public class BankTransferFragment extends Fragment {

    private Button btnSeeInstruction = null;
    private EditText mEditTextEmailId = null;
    private UserDetail userDetail;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_bank_transfer, container, false);
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        initializeViews(view);
    }

    /**
     * initializes view and adds click listener for it.
     *
     * @param view  view that needed to be initialized
     */
    private void initializeViews(View view) {

        btnSeeInstruction = (Button) view.findViewById(R.id.btn_see_instruction);
        try {
            userDetail = LocalDataHandler.readObject(getString(R.string.user_details), UserDetail.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        mEditTextEmailId = (EditText) view.findViewById(R.id.et_email);
        VeritransSDK veritransSDK = VeritransSDK.getInstance();
        try {
            mEditTextEmailId.setText(userDetail.getEmail());
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        if (veritransSDK != null) {
            mEditTextEmailId.setHintTextColor(veritransSDK.getThemeColor());
        }
        btnSeeInstruction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (getActivity() != null) {
                    Intent intent = new Intent(getActivity(), BankTransferInstructionActivity.class);
                    intent.putExtra(BankTransferInstructionActivity.BANK, getArguments().getString(BankTransferInstructionActivity.BANK));
                    getActivity().startActivity(intent);

                }
            }
        });
    }


    /**
     * created to give access to email id field from {@link id.co.veritrans.sdk.activities
     * .BankTransferActivity}.
     *
     * @return email identifier
     */
    public String getEmailId() {
        if (mEditTextEmailId != null) {
            return mEditTextEmailId.getText().toString();
        } else {
            return null;
        }
    }
}