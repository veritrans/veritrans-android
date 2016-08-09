package id.co.veritrans.sdk.uiflow.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import id.co.veritrans.sdk.uiflow.R;

/**
 * Displays ATM bersama payment instruction.
 *
 * @author rakawm
 */
public class InstructionATMBersamaFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_instruction_atm_bersama, container, false);
    }
}
