package com.imbuegen.alumniapp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.imbuegen.alumniapp.Adapters.UnanswerdQuestionsAdapter;
import com.imbuegen.alumniapp.Models.QuestionsModel;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class AnswerFragment extends Fragment {

    private List<QuestionsModel> unanswerdQuestionsModelList = new ArrayList<>();
    private RecyclerView recyclerView;
    private UnanswerdQuestionsAdapter mAdapter;

    FirebaseDatabase database = FirebaseDatabase.getInstance();
    ProgressBar progressBar;


    String uid;

    String path;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_answer, null);

        if (getArguments() != null) {
            path = getArguments().getString("path");
            uid = getArguments().getString("uid");
        }
        try {
            path =URLDecoder.decode( path, "UTF-8" );
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "No Path Specified", Toast.LENGTH_SHORT).show();
            return null;
        }


        getActivity().setTitle("Answer Questions");




        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
        progressBar = v.findViewById(R.id.progress_circular);


        progressBar.setVisibility(View.VISIBLE);

        mAdapter = new UnanswerdQuestionsAdapter(unanswerdQuestionsModelList, getActivity(), path);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.addItemDecoration(new DividerItemDecoration(getActivity(), DividerItemDecoration.VERTICAL));
        recyclerView.setAdapter(mAdapter);

        getUnansweredList();


        return v;


    }



    public void getUnansweredList() {

        DatabaseReference questionRef = database.getReference(path)
                                        .child("questions");

        unanswerdQuestionsModelList.clear();

        questionRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                for (DataSnapshot qSnapshot : dataSnapshot.getChildren()) {

                    if (!(qSnapshot.child("questions").getValue().equals(null))) {

                        String question = qSnapshot.child("questions").getValue().toString();
                        String answer = qSnapshot.child("answer").getValue().toString();
                        String key = qSnapshot.getKey();

                        if (answer.trim().equals("")) {
                            unanswerdQuestionsModelList.add(new QuestionsModel(question, answer,key));
                            mAdapter.notifyDataSetChanged();
                        }
                        mAdapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
                    }

                }

                if (unanswerdQuestionsModelList.size() == 0) {
                    Toast.makeText(getActivity(), "No Questions", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mAdapter.notifyDataSetChanged();
    }


}
