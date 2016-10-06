package com.midtrans.sdk.uikit.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.midtrans.sdk.uikit.R;
import com.midtrans.sdk.uikit.models.CountryCodeModel;

import java.util.ArrayList;

/**
 * Created by ziahaqi on 7/29/16.
 */
public class ListCountryAdapter extends ArrayAdapter<CountryCodeModel> {

    private final ArrayList<CountryCodeModel> data;
    private final int resourceId;
    Filter nameFilter = new Filter() {
        @Override
        public String convertResultToString(Object resultValue) {
            String str = ((CountryCodeModel) (resultValue)).getName();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                ArrayList<CountryCodeModel> suggestions = new ArrayList<>();

                for (CountryCodeModel color : data) {
                    if (color.getName().toLowerCase().startsWith(constraint.toString().toLowerCase())) {
                        suggestions.add(color);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            if (results != null && results.count > 0) {
                addAll((ArrayList<CountryCodeModel>) results.values);
            }
            notifyDataSetChanged();
        }
    };

    public ListCountryAdapter(Context context, int resource, ArrayList<CountryCodeModel> data) {
        super(context, resource);
        this.resourceId = resource;
        this.data = data;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        if (view == null) {
            view = LayoutInflater.from(parent.getContext()).inflate(resourceId, parent, false);
        }

        CountryCodeModel model = getItem(position);
        TextView textView = (TextView) view.findViewById(R.id.text_country_name_row);

        textView.setText(model.getName());
        view.setTag(model);
        return view;
    }

    @Override
    public Filter getFilter() {
        return nameFilter;
    }


}
