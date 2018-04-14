package com.mobile.urbanfix.urban_fix.presenter;

import android.content.DialogInterface;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.mobile.urbanfix.urban_fix.Constants;
import com.mobile.urbanfix.urban_fix.R;
import com.mobile.urbanfix.urban_fix.SystemUtils;
import com.mobile.urbanfix.urban_fix.factory.ConnectionFactory;
import com.mobile.urbanfix.urban_fix.model.User;
import com.mobile.urbanfix.urban_fix.view.fragments.AlertFragment;
import com.mobile.urbanfix.urban_fix.view.fragments.MapsFragment;
import com.mobile.urbanfix.urban_fix.view.fragments.MyAlertsFragment;

public class MainPresenter implements MainMVP.IMainPresenter, MainMVP.ICallbackPresenter {

    private MainMVP.IMainView view;
    public static final String TAG_STACK = "STACK";
    public FragmentManager fragmentManager;

    public MainPresenter(MainMVP.IMainView view) {
        this.view = view;
    }

    @Override
    public void initializeUser() {
        User user = User.getInstance();
    }

    @Override
    public void openMapView(AppCompatActivity activity) {
        MapsFragment mapsFragment = new MapsFragment();
        if(fragmentManager == null) fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.mainLayout, mapsFragment).commit();
        activity.setTitle(R.string.fragment_map_title);
    }


    @Override
    public void openAlertView(AppCompatActivity activity) {
        AlertFragment alertFragment = new AlertFragment();
        if(fragmentManager == null) fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.mainLayout, alertFragment).commit();
        activity.setTitle(R.string.fragment_alert_title);
    }

    @Override
    public void openNoticesView(AppCompatActivity activity) {
        MyAlertsFragment fragment = new MyAlertsFragment();
        if(fragmentManager == null) fragmentManager = activity.getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        ft.replace(R.id.mainLayout, fragment).commit();
        activity.setTitle(R.string.fragment_noticy_title);
    }

    @Override
    public void openStatisticsView(AppCompatActivity activity) {
        SystemUtils.showMessage(activity, "Função ainda não desenvolvida", Toast.LENGTH_LONG);
    }

    @Override
    public void doLogoff(AppCompatActivity activity) {
        showLogoffDialog(activity);
    }

    private void showLogoffDialog(final AppCompatActivity activity) {
        AlertDialog.Builder builder;
        builder = new AlertDialog.Builder(activity);

        builder.setTitle(activity.getString( R.string.main_loggof_alert_title))
                .setMessage(activity.getString( R.string.main_loggof_alert_message ) )
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ConnectionFactory.logout();
                        activity.finish();
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.setIcon(R.drawable.ic_warning);
        builder.show();
    }

    @Override
    public void onSuccessTask(Constants task, Object o) {

    }

    @Override
    public void onFailedTask(Constants task) {
        Log.e("Script", "Erro");
    }
}
