package com.imbuegen.alumniapp.Adapters;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.imbuegen.alumniapp.AlumniInfoActivity;
import com.imbuegen.alumniapp.Models.AlumniModel;
import com.imbuegen.alumniapp.R;

import java.util.List;


public class AlumniListAdapter extends ArrayAdapter<AlumniModel> {
    private Activity context;
    private List<AlumniModel> alumniList;

    public AlumniListAdapter(Activity context, List<AlumniModel> alumniList)
    {
        super(context,R.layout.alumni_list_item,alumniList);
        this.context = context;
        this.alumniList = alumniList;
    }


    @NonNull
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {


        LayoutInflater inflater = context.getLayoutInflater();
        View listItemView = inflater.inflate(R.layout.alumni_list_item,null,true);
        listItemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(parent.getContext(),AlumniInfoActivity.class);
                i.putExtra(AlumniInfoActivity.ALUMNI_OBJ,alumniList.get(position));
                context.startActivity(i);
            }
        });
        TextView alumniNameTextView = (TextView) listItemView.findViewById(R.id.text_view_alumni_name);
        AlumniModel company = alumniList.get(position);
        alumniNameTextView.setText(company.getAlumniName());

        return listItemView;
    }
}
