package com.mobile.urbanfix.urban_fix.fragments;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.mobile.urbanfix.urban_fix.Connection;
import com.mobile.urbanfix.urban_fix.R;
import com.mobile.urbanfix.urban_fix.model.User;

public class AccountFragment extends Fragment {

    private TextView userNameTextView, emailTextView, birthTextView, numberOfAlertsTextView;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser firebaseUser;
    private DatabaseReference databaseReference;
    private User user;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        initFirebase();
        Toast.makeText(getContext(), firebaseUser.getUid() + "\n" +
                firebaseUser.getEmail(), Toast.LENGTH_LONG).show();
    }


    private void initViews() {
        userNameTextView = (TextView) getActivity().findViewById(R.id.userNameTextView);
        emailTextView = (TextView) getActivity().findViewById(R.id.emailTextView);
        birthTextView = (TextView) getActivity().findViewById(R.id.birthTextView);
        numberOfAlertsTextView = (TextView) getActivity().findViewById(R.id.numberOfAlertsTextView);
    }

    private void initFirebase() {
        firebaseAuth = Connection.getFirebaseAuth();
        databaseReference = Connection.getDatabaseReference();
        firebaseUser = Connection.getFirebaseUser();
        databaseReference.child("User").child( firebaseUser.getUid() ).
                addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                user = new User();
                System.out.println("Entrei aqui");
                user = dataSnapshot.getValue(User.class);
                System.out.println("Sai aqui");
                System.out.println(user);

                setUsersInformations();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void setUsersInformations() {
        userNameTextView.setText(user.getName());
        emailTextView.setText(user.getEmail());
        birthTextView.setText(user.getBirthDate());
        numberOfAlertsTextView.setText(getString(R.string.account_nofalerts) +  user.getnAlertsDone());
    }
}
