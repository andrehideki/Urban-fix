package com.mobile.urbanfix.urban_fix.view.fragments;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mobile.urbanfix.urban_fix.MainMVP;
import com.mobile.urbanfix.urban_fix.presenter.MyAlertsPresenter;
import com.mobile.urbanfix.urban_fix.R;

public class MyAlertsFragment extends Fragment implements MainMVP.IMyAlertsView {

    private TextView userNameTextView, numberOfAlertsTextView;
    private RecyclerView problemsRecyclerView;
    private MainMVP.IMyAlertsPresenter presenter;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_alerts, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        startMVP();

        userNameTextView = (TextView) view.findViewById(R.id.userNameTextView);
        numberOfAlertsTextView = (TextView) view.findViewById(R.id.numberOfAlertsTextView);
        problemsRecyclerView = (RecyclerView) view.findViewById(R.id.problemsRecyclerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.setupMyAlertsList(this.problemsRecyclerView, getContext());
        presenter.getUserAlerts();
        presenter.configureUserInformations();
    }

    private void startMVP() {
        this.presenter = new MyAlertsPresenter(this);
    }

    @Override
    public void setUserName(String userName) {
        this.userNameTextView.setText(userName);
    }

    @Override
    public void setNumberOfAlerts(int numberOfAlerts) {
        this.numberOfAlertsTextView.setText(""+numberOfAlerts);
    }
}
