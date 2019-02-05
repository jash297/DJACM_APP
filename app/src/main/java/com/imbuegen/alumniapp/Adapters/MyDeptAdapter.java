package com.imbuegen.alumniapp.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.imbuegen.alumniapp.R;
import com.imbuegen.alumniapp.Models.Department;
import java.util.ArrayList;

public class MyDeptAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<Department> deptList;

    public MyDeptAdapter(Context context, ArrayList<Department> deptList) {
        this.context = context;
        this.deptList = deptList;
    }

    @Override
    public int getCount() {
        return deptList.size();
    }

    @Override
    public Object getItem(int i) {
        return deptList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        Department dp = deptList.get(i);
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        assert inflater != null;
        @SuppressLint("ViewHolder") View relativeLayoutItem = inflater.inflate(R.layout.dept_item, viewGroup, false);

        TextView nameView = relativeLayoutItem.findViewById(R.id.txt_name);
        ImageView icnView = relativeLayoutItem.findViewById(R.id.icon_dept);

        nameView.setText(dp.getName());
        icnView.setImageResource(dp.getIconPath());

        return relativeLayoutItem;
    }
}
