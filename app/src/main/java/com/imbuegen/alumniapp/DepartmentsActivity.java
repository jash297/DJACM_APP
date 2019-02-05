package com.imbuegen.alumniapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import com.imbuegen.alumniapp.Adapters.MyDeptAdapter;
import com.imbuegen.alumniapp.Models.Department;

import java.util.ArrayList;

public class DepartmentsActivity extends BaseActivity {

    ArrayList<Department> deptList;
    ListView deptListView;
    MyDeptAdapter myDeptAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
        instantiateDeptList();
    }
    @Override
    protected void onResume() {
        super.onResume();

        loadData();
    }

    private void init(){
        deptList = new ArrayList<>();
        deptListView = findViewById(R.id.list_depts);
    }
    private void loadData() {
        deptList.clear();
        deptList.add(new Department("Computers and IT", R.mipmap.comps));
        deptList.add(new Department("Elex & Telecom", R.mipmap.extc));
        deptList.add(new Department("Electronics", R.mipmap.electronics));
        deptList.add(new Department("Mechanical", R.mipmap.mechanical));
        deptList.add(new Department("Production", R.mipmap.prod));
        deptList.add(new Department("Chemical", R.mipmap.chem));
        myDeptAdapter.notifyDataSetChanged();
    }

    private void instantiateDeptList() {

        myDeptAdapter = new MyDeptAdapter(this, deptList);



        deptListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                Department selectedDepartment = (Department) deptListView.getItemAtPosition(i);

                String selectedDeptName = selectedDepartment.getName();
                Intent companyActivityIntent = new Intent(DepartmentsActivity.this,CompanyActivity.class);
                companyActivityIntent.putExtra("deptName",selectedDeptName);
                startActivity(companyActivityIntent);

            }
        });


        deptListView.setAdapter(myDeptAdapter);
    }
}
