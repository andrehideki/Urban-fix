package com.mobile.urbanfix.urban_fix.view.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.mobile.urbanfix.urban_fix.view.MainActivity;
import com.mobile.urbanfix.urban_fix.R;
import com.mobile.urbanfix.urban_fix.model.User;

public class AccountFragment extends Fragment {

    private TextView userNameTextView, emailTextView, birthTextView, numberOfAlertsTextView;
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
        initUser();
    }

    private void initUser() {
        //user = MainActivity.getUser();
        setUsersInformations();
    }


    private void initViews() {
        userNameTextView = (TextView) getActivity().findViewById(R.id.userNameTextView);
        emailTextView = (TextView) getActivity().findViewById(R.id.emailTextView);
        birthTextView = (TextView) getActivity().findViewById(R.id.birthTextView);
        numberOfAlertsTextView = (TextView) getActivity().findViewById(R.id.numberOfAlertsTextView);
    }


    private void setUsersInformations() {
        userNameTextView.setText(user.getName());
        emailTextView.setText(user.getEmail());
        birthTextView.setText(user.getBirthDate());
        numberOfAlertsTextView.setText(getString(R.string.account_nofalerts) +  user.getnAlertsDone());
    }
}
