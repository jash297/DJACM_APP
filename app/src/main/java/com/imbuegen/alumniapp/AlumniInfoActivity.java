package com.imbuegen.alumniapp;

import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.imbuegen.alumniapp.Adapters.QuestionsAdapter;
import com.imbuegen.alumniapp.Models.AlumniModel;
import com.imbuegen.alumniapp.Models.QuestionsModel;
import com.squareup.picasso.Picasso;

import java.util.Iterator;
import java.util.Objects;


public class AlumniInfoActivity extends BaseActivity {

    public static final String ALUMNI_OBJ = "qwe";


    TextView nameTextView;
    TextView dateJoinTextView;
    TextView companyTextView;

    ImageView profileImage;


    FloatingActionButton askQuestionButton;

    ListView questionsListView;
    QuestionsAdapter adapter;


    AlumniModel mAlumni;
    private StorageReference mStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.mNoActionBar);
        setContentView(R.layout.activity_alumni_info);

        LayoutInflater inflater = getLayoutInflater();
        View profileLayout    = inflater.inflate(R.layout.profile_view, null);
        nameTextView = profileLayout.findViewById(R.id.alumniName_tv);
        dateJoinTextView = profileLayout.findViewById(R.id.date_join_tv);
        companyTextView =  profileLayout.findViewById(R.id.companyName_tv);
        questionsListView = findViewById(R.id.list);
        askQuestionButton = findViewById(R.id.ask_qustion_bt);

        profileImage = findViewById(R.id.profile_image);


        mAlumni = (AlumniModel) getIntent().getSerializableExtra(ALUMNI_OBJ);
        nameTextView.setText(mAlumni.getAlumniName());
        companyTextView.setText(mAlumni.getCompany());
        dateJoinTextView.setText("Member Since: "+mAlumni.getJoinDate());

        adapter = new QuestionsAdapter(mAlumni.getQuestionsList(),getApplicationContext());

        questionsListView.setAdapter(adapter);

        questionsListView.addHeaderView(profileLayout);




        removeQuestions();


        askQuestionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });


        mStorage = FirebaseStorage.getInstance().getReference();
        getProfileImage();

    }

    private void showDialog() {
        final AlertDialog.Builder alert = new AlertDialog.Builder(AlumniInfoActivity.this);
        final EditText edittext = new EditText(AlumniInfoActivity.this);
        alert.setMessage("Ask Question");
        alert.setView(edittext);
        alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                String question = edittext.getText().toString();

                DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference(mAlumni.getDatabaseReferencePath());

                databaseReference.child("questions").push().setValue(new QuestionsModel(question,""));

            }
        });
        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        alert.show();
    }

    private void removeQuestions() {
        Iterator<QuestionsModel> iter = mAlumni.getQuestionsList().iterator();
        while (iter.hasNext()) {
           QuestionsModel questionsModel = iter.next();
            if(questionsModel.getAnswer().trim().isEmpty()) {
                iter.remove();
            }

        }
    }


    public void getProfileImage() {

        Log.d("TAG", "getProfileImage: "+mAlumni.getUid());
        StorageReference profileReference = mStorage.child("Profile photos").child(mAlumni.getUid());
        if (profileReference!=null)  {
            profileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri)
                {
                    Picasso.get().load(uri).fit().centerCrop().into(profileImage);
                }
            });
        }
    }
}
