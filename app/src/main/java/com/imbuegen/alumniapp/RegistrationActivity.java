package com.imbuegen.alumniapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegistrationActivity extends BaseActivity  {
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mAlumniReference;
    private static final String TAG = "XYZ";

    BottomNavigationView navigation ;

    private TextView uidTv;
    String alumniPath;
    String dbPath;
    String uid;
    String xyz = "";


    public String getDbPath() {
        return dbPath;
    }

    public void setDbPath(String dbPath) {
        this.dbPath = dbPath;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registeration);

         navigation = (BottomNavigationView) findViewById(R.id.navigation);



        init();

        final String uid = mAuth.getUid();
        DatabaseReference userIndexRef = mDatabase.getReference("userIndex").child(uid);
        userIndexRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                xyz = alumniPath = (String) dataSnapshot.getValue();
                Bundle bundle = new Bundle();
                bundle.putString("path", xyz);
                bundle.putString("uid",uid);
                Fragment fragment = null;

                // set Fragmentclass Arguments
                fragment = new ProfileFragment();
                fragment.setArguments(bundle);
                loadFragment(fragment);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Bundle bundle = new Bundle();
        bundle.putString("path", xyz);
        bundle.putString("uid",uid);

        Fragment profileFragment =  new ProfileFragment();
        profileFragment.setArguments(bundle);
        loadFragment(profileFragment);


        getTheAlumni();


    }

    private void getTheAlumni() {
        final String uid = mAuth.getUid();

        DatabaseReference userIndexRef = mDatabase.getReference("userIndex").child(uid);
        userIndexRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
               xyz = alumniPath = (String) dataSnapshot.getValue();


                 navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                     @Override
                     public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                         Fragment fragment = null;
                         if (menuItem.getItemId() == R.id.navigation_profile)
                         {
                             Bundle bundle = new Bundle();
                             bundle.putString("path", xyz);
                             bundle.putString("uid",uid);

                             // set Fragmentclass Arguments
                             fragment = new ProfileFragment();
                             fragment.setArguments(bundle);


                         }

                         if (menuItem.getItemId() == R.id.navigation_answer)
                         {
                             Bundle bundle = new Bundle();
                             bundle.putString("path", xyz);
                             bundle.putString("uid",uid);
                             fragment = new AnswerFragment();
                             fragment.setArguments(bundle);
                         }

                         return loadFragment(fragment);
                     }
                 });



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        init();
    }

    private void init() {
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
    }


   /* @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment fragment = null;


        switch (menuItem.getItemId())
        {
            case R.id.navigation_profile:

                Bundle bundle = new Bundle();
                bundle.putString("path", getDbPath());

                // set Fragmentclass Arguments
                fragment = new ProfileFragment();
                fragment.setArguments(bundle);

                break;


            case R.id.navigation_answer:
                fragment = new AnswerFragment();
                //Intent answerIntent = new Intent(MainActivity.this,AnswerQuestionsActivity.class);
                //startActivity(answerIntent);
                break;

        }
        return loadFragment(fragment);
    }*/


    private boolean loadFragment(Fragment fragment)
    {
        if (fragment != null)
        {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container,fragment)
                    .commit();

            return true;
        }
        return false;
    }
}
