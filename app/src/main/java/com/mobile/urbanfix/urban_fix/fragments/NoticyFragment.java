package com.mobile.urbanfix.urban_fix.fragments;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.mobile.urbanfix.urban_fix.Connection;
import com.mobile.urbanfix.urban_fix.MainActivity;
import com.mobile.urbanfix.urban_fix.ProblemArrayAdapter;
import com.mobile.urbanfix.urban_fix.R;
import com.mobile.urbanfix.urban_fix.model.Problem;

import java.util.ArrayList;

public class NoticyFragment extends Fragment {

    private ListView problemsListView;
    private ArrayList<Problem> problems = new ArrayList<>();
    private DatabaseReference databaseReference;
    private ProblemArrayAdapter arrayAdapter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_noticy, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDatabase();

        problemsListView = (ListView) getActivity().findViewById(R.id.problemsListView);
        arrayAdapter = new ProblemArrayAdapter( getActivity(), this.problems );
        problemsListView.setAdapter(arrayAdapter);
    }



    private void initDatabase() {
        String userCpf = MainActivity.getUser().getCpf();
        databaseReference = Connection.getProblemsDatabaseReference();
        databaseReference.child("Alerts").child(userCpf).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Problem p = (dataSnapshot.getValue( Problem.class ) );
                System.out.println(p);
                problems.add(p);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                problems.remove(dataSnapshot.getValue( Problem.class));
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
