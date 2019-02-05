package com.imbuegen.alumniapp.Adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.imbuegen.alumniapp.R;

import java.util.List;


public class CompanyListAdapter extends ArrayAdapter<CompanyModel> {
    private Activity context;
    private List<CompanyModel> companyList;

    public CompanyListAdapter(Activity context, List<CompanyModel> companyList)
    {
        super(context,R.layout.company_list_item,companyList);
        this.context = context;
        this.companyList = companyList;
    }


    @NonNull
    @Override
    public View getView(int position,View convertView,ViewGroup parent) {


        LayoutInflater inflater = context.getLayoutInflater();
        View listItemView = inflater.inflate(R.layout.company_list_item,null,true);

        TextView companyNameTextView = (TextView) listItemView.findViewById(R.id.text_view_company_name);
        CompanyModel company = companyList.get(position);

        companyNameTextView.setText(company.getCompanyName());

        return listItemView;
    }
}
