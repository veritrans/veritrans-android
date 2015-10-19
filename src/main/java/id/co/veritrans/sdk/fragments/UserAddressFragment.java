package id.co.veritrans.sdk.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;

import id.co.veritrans.sdk.R;
import id.co.veritrans.sdk.activities.UserDetailsActivity;

public class UserAddressFragment extends Fragment {

    private EditText etAddress;
    private EditText etCity;
    private EditText etZipcode;
    private EditText etCountry;
    private CheckBox cbShippingAddress;
    private RelativeLayout shippingAddressContainer;
    private EditText etShippingAddress;
    private EditText etShippingCity;
    private EditText etShippingZipcode;
    private EditText etShippingCountry;
    private Button btnNext;





    public static UserAddressFragment newInstance() {
        UserAddressFragment fragment = new UserAddressFragment();
        return fragment;
    }

    public UserAddressFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ((UserDetailsActivity)getActivity()).getSupportActionBar().setTitle(getString(R.string.title_shipping_billing_address));


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_user_address, container, false);
        findViews(view);

        return view;
    }

    private void findViews(View view) {
        etAddress = (EditText)view.findViewById( R.id.et_address );
        etCity = (EditText)view.findViewById( R.id.et_city );
        etZipcode = (EditText)view.findViewById( R.id.et_zipcode );
        etCountry = (EditText)view.findViewById( R.id.et_country );
        cbShippingAddress = (CheckBox)view.findViewById( R.id.cb_shipping_address );
        shippingAddressContainer = (RelativeLayout)view.findViewById( R.id.shipping_address_container );
        etShippingAddress = (EditText)view.findViewById( R.id.et_shipping_address );
        etShippingCity = (EditText)view.findViewById( R.id.et_shipping_city );
        etShippingZipcode = (EditText)view.findViewById( R.id.et_shipping_zipcode );
        etShippingCountry = (EditText)view.findViewById( R.id.et_shipping_country );
        btnNext = (Button)view.findViewById( R.id.btn_next );
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validateAndSaveAddress();
            }
        });
       /* cbShippingAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cbShippingAddress.isChecked()) {
                    cbShippingAddress.setChecked(false);
                    shippingAddressContainer.setVisibility(View.VISIBLE);
                } else {
                    shippingAddressContainer.setVisibility(View.GONE);
                }
            }
        });*/
        cbShippingAddress.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    shippingAddressContainer.setVisibility(View.GONE);
                } else {
                    shippingAddressContainer.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void validateAndSaveAddress() {
        String billingAddress = etAddress.getText().toString().trim();
        String billingCity = etCity.getText().toString().trim();
        String zipcode = etZipcode.getText().toString().trim();
        String country = etCountry.getText().toString().trim();
        String shippingAddress = etShippingAddress.getText().toString().trim();
        String shippingCity = etShippingCity.getText().toString().trim();
        String shippingZipcode = etShippingZipcode.getText().toString().trim();
        String shippingCountry = etShippingCountry.getText().toString().trim();

    }

}
