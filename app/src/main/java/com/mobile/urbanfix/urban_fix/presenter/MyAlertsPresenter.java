package com.mobile.urbanfix.urban_fix.presenter;


import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.mobile.urbanfix.urban_fix.MainMVP;
import com.mobile.urbanfix.urban_fix.adapter.MyAlertsAdapter;
import com.mobile.urbanfix.urban_fix.model.Problem;
import com.mobile.urbanfix.urban_fix.model.User;

import java.util.ArrayList;

public class MyAlertsPresenter implements MainMVP.IMyAlertsPresenter, MainMVP.ICallbackListOfAlerts {

    private MainMVP.IMyAlertsView view;
    private MyAlertsAdapter adapter;
    private ArrayList<Problem> userAlerts;
    private User user;

    public MyAlertsPresenter(MainMVP.IMyAlertsView view) {
        this.view = view;
        this.userAlerts = new ArrayList<>();
        this.user = User.getInstance();
    }

    @Override
    public void getUserAlerts() {
        Problem.getUserAlerts(this.userAlerts, adapter, this);
    }

    @Override
    public void configureUserInformations() {
        //view.setUserName(user.getName());
        view.setNumberOfAlerts(userAlerts.size());
    }

    @Override
    public void setupMyAlertsList(RecyclerView myAlertsRecyclerView, Context context) {
        adapter = new MyAlertsAdapter(this.userAlerts);
        myAlertsRecyclerView.setAdapter(adapter);
        myAlertsRecyclerView.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false));
    }

    @Override
    public void onListOfAlertsChanged() {
        view.setNumberOfAlerts(userAlerts.size());
    }
}
