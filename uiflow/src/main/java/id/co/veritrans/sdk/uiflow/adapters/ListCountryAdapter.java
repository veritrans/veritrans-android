package id.co.veritrans.sdk.uiflow.adapters;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import java.util.ArrayList;

import id.co.veritrans.sdk.coreflow.core.Logger;
import id.co.veritrans.sdk.uiflow.R;
import id.co.veritrans.sdk.uiflow.models.CountryCodeModel;

/**
 * Created by ziahaqi on 7/29/16.
 */
public class ListCountryAdapter extends ArrayAdapter<CountryCodeModel> {

    private final ArrayList<CountryCodeModel> data;
    private final int resourceId;

    public ListCountryAdapter(Context context, int resource, ArrayList<CountryCodeModel> data) {
        super(context, resource, data);
        this.resourceId = resource;
        this.data = data;
        Logger.i("country:count:" + data.size());

    }

    private static class ViewHolder{
        TextView textCountry;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CountryCodeModel model = data.get(position);
        ViewHolder holder;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(parent.getContext(), resourceId, null);
            holder.textCountry = (TextView)convertView.findViewById(R.id.text_country_code_row);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder)convertView.getTag();
        }
        holder.textCountry.setText(model.getName());
        return convertView;
    }

    public Filter getFilter() {
       return new Filter() {
            @Override
            public CharSequence convertResultToString(Object resultValue) {
                return super.convertResultToString(resultValue);
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                Logger.d("country:start");

                FilterResults results = new FilterResults();
                ArrayList<CountryCodeModel> list = new ArrayList<>(data);
                Logger.d("country:constraint>list:size:" + list.size());

                if(constraint == null || constraint.length() == 0){
                    results.values = list;
                    results.count = list.size();
                    Logger.d("country:constraint:null");

                } else {
                    Logger.d("country:constraint:" + constraint);
                    String PrefixString = constraint.toString().toLowerCase();
                    Logger.d("country:constraint>after:" + constraint);

                    final ArrayList<CountryCodeModel> newValues = new ArrayList<>();

                    for(CountryCodeModel model : list){
                        String ValueText = model.getName().toLowerCase();
                        if(ValueText.startsWith(PrefixString))
                            newValues.add(model);
                    }
                    results.values = newValues;
                    results.count = newValues.size();
                }
                Logger.d("country:constraint>resultfilter:" + results.count);

                return results;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                clear();
                Logger.d("country:constraint>resultfilter>publisha:" + filterResults.count);

                if(filterResults.count > 0)
                    addAll(((ArrayList<CountryCodeModel>) filterResults.values));
//            else
//                notifyDataSetInvalidated();
            }
        };
    }


}
