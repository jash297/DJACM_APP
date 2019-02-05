package com.imbuegen.alumniapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.imbuegen.alumniapp.Adapters.QuestionsAdapter;
import com.imbuegen.alumniapp.Models.QuestionsModel;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.ArrayList;

public class ProfileFragment extends Fragment {

    private ImageView profileImage;
    private TextView nameTv;
    private TextView joinDateTv;
    String uid = "";


    private ArrayList<QuestionsModel> questionsModels;
    private QuestionsAdapter adapter;


    private ListView mListViewAlumniSide;


    private StorageReference mStorage;




    private FirebaseDatabase database = FirebaseDatabase.getInstance();


    String path;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)  {
        mStorage = FirebaseStorage.getInstance().getReference();
        View v = inflater.inflate(R.layout.fragment_profile,null);
        if (getArguments()!=null) {
             path = getArguments().getString("path");
             uid = getArguments().getString("uid");

        }
        getActivity().setTitle("Profile");
        profileImage = v.findViewById(R.id.profile_image);
        nameTv = v.findViewById(R.id.alumniName_tv);
        joinDateTv = v.findViewById(R.id.date_join_tv);
        mListViewAlumniSide = v.findViewById(R.id.list_view_alumni_side);
        questionsModels = new ArrayList<>();
        adapter = new QuestionsAdapter(questionsModels,getContext());
        mListViewAlumniSide.setAdapter(adapter);
        profileImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cropImage();

            }
        });
        return v;
    }

    @Override
    public void onResume() {
        super.onResume();
        getDataUsingUrl();
        getProfileImage();
    }



    public void cropImage()
    {
        CropImage.activity()
                .start(getContext(), this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == getActivity().RESULT_OK)
            {
                Uri resultUri = result.getUri();
                uploadProfileImage(resultUri);
                //profileImage.setImageURI(resultUri);


            }
            else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE)
            {
                Exception error = result.getError();
            }
        }
    }



    public void getProfileImage() {

        StorageReference profileReference = mStorage.child("Profile photos").child(uid);

        if (profileReference!=null)
        {
            profileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri)
                {

                    Picasso.get().load(uri).fit().centerCrop().into(profileImage);

                    Toast.makeText(getActivity(),"Retreived from firebase storage",Toast.LENGTH_SHORT).show();


                }
            });
        }

    }



    public void getDataUsingUrl()
    {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Loading...");
        progressDialog.show();

        Log.d("TAG", "getDataUsingUrl: I am at getData");


        String url = "https://alumniapp-21db1.firebaseio.com";

        String modPath = path.replace("%20"," ");
        String fmodPath = modPath.replace("%26","&");

        String finalurl = url+fmodPath;
        DatabaseReference ref = database.getReferenceFromUrl(finalurl);


        DatabaseReference questionRef = ref.child("questions");


       ref.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


               String mName = (String) dataSnapshot.child("Name").getValue();
               String mJoinDate = String.valueOf(dataSnapshot.child("JoinDate").getValue());


               nameTv.setText(mName);
               joinDateTv.setText(mJoinDate);

           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });


       questionRef.addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

               for (DataSnapshot qSnapshot : dataSnapshot.getChildren())
               {

                   String questions = qSnapshot.child("questions").getValue().toString();
                   String answer = qSnapshot.child("answer").getValue().toString();


                   if (!(answer.equals(""))) {

                       adapter.notifyDataSetChanged();
                       questionsModels.add(new QuestionsModel(questions,answer));

                   }

                   adapter.notifyDataSetChanged();


               }

               progressDialog.dismiss();
               adapter.notifyDataSetChanged();
           }

           @Override
           public void onCancelled(@NonNull DatabaseError databaseError) {

           }
       });


    }


    public void uploadProfileImage(Uri resultUri)
    {
        final ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Uploading");
        progressDialog.show();

        final StorageReference filepath = mStorage.child("Profile photos").child(uid);
        final UploadTask uploadTask =  filepath.putFile(resultUri);

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getActivity(), "Profile Picture Uploaded", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();


            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();

                //displaying percentage in progress dialog
                progressDialog.setMessage("Uploaded " + ((int) progress) + "%...");
            }
        });


        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
            @Override
            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                if (!task.isSuccessful()) {
                    throw task.getException();
                }

                // Continue with the task to get the download URL
                return filepath.getDownloadUrl();
            }
        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
            @Override
            public void onComplete(@NonNull Task<Uri> task) {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();

                    Picasso.get().load(downloadUri).fit().centerCrop().into(profileImage);

                    Toast.makeText(getActivity(),"Retreived from firebase storage",Toast.LENGTH_SHORT).show();


                    //String downloadURL = downloadUri.toString();
                } else {
                    // Handle failures
                    // ...
                }
            }
        });




    }


}
