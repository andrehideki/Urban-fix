package com.mobile.urbanfix.urban_fix.view.fragments;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.mobile.urbanfix.urban_fix.factory.ConnectionFactory;
import com.mobile.urbanfix.urban_fix.presenter.MainMVP;
import com.mobile.urbanfix.urban_fix.presenter.MyAlertsPresenter;
import com.mobile.urbanfix.urban_fix.view.MainActivity;
import com.mobile.urbanfix.urban_fix.adapter.MyAlertsAdapter;
import com.mobile.urbanfix.urban_fix.R;
import com.mobile.urbanfix.urban_fix.model.Problem;

import java.util.ArrayList;

public class NoticyFragment extends Fragment implements MainMVP.IMyAlertsView {

    private RecyclerView problemsRecyclerView;
    private ArrayList<Problem> problems = new ArrayList<>();
    private DatabaseReference databaseReference;
    private MyAlertsAdapter arrayAdapter;
    private MainMVP.IMyAlertsPresenter presenter;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_noticy, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startMVP();

        problemsRecyclerView = (RecyclerView) view.findViewById(R.id.problemsRecyclerView);
        presenter.getUserAlerts();
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.setupMyAlertsList(this.problemsRecyclerView, getContext());
    }

    private void initDatabase() {
        String userUUid = MainActivity.getUser().getUUID();
        databaseReference = ConnectionFactory.getProblemsDatabaseReference();
        databaseReference.child("Alerts").child(userUUid).addChildEventListener(new ChildEventListener() {
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

    private void startMVP() {
        this.presenter = new MyAlertsPresenter(this);
    }
}
