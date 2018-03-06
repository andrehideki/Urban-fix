package com.mobile.urbanfix.urban_fix.presenter;


import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.mobile.urbanfix.urban_fix.adapter.MyAlertsAdapter;
import com.mobile.urbanfix.urban_fix.model.Problem;

import java.util.ArrayList;

public class MyAlertsPresenter implements MainMVP.IMyAlertsPresenter {

    private MainMVP.IMyAlertsView view;
    private MyAlertsAdapter adapter;
    private ArrayList<Problem> userAlerts;

    public MyAlertsPresenter(MainMVP.IMyAlertsView view) {
        this.view = view;
    }

    @Override
    public void getUserAlerts() {
        this.userAlerts = Problem.getUserAlerts();
    }

    @Override
    public void setupMyAlertsList(RecyclerView myAlertsRecyclerView, Context context) {
        adapter = new MyAlertsAdapter(context, this.userAlerts);
        myAlertsRecyclerView.setAdapter(adapter);
        myAlertsRecyclerView.setLayoutManager(new LinearLayoutManager(context,
                LinearLayoutManager.VERTICAL, false));
    }

}
