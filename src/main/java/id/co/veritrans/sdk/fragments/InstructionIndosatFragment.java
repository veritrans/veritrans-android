package id.co.veritrans.sdk.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import id.co.veritrans.sdk.R;

public class InstructionIndosatFragment extends Fragment {

    private EditText mEditTextPhoneNumber = null;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_instruction_indosat, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        mEditTextPhoneNumber = (EditText) view.findViewById(R.id.et_indosat_phone_number);

            mEditTextPhoneNumber.setText("08123456789");

    }


    /**
     * created to give access to phone number field from {@link id.co.veritrans.sdk.activities
     * .IndosatDompetkuActivity}.
     *
     * @return
     */
    public String getPhoneNumber() {
        if (mEditTextPhoneNumber != null) {
            return mEditTextPhoneNumber.getText().toString();
        } else {
            return null;
        }
    }

}
