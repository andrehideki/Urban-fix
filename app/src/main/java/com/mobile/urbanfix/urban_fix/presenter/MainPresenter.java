package com.mobile.urbanfix.urban_fix.presenter;

import com.mobile.urbanfix.urban_fix.Logger;
import com.mobile.urbanfix.urban_fix.MainMVP;
import com.mobile.urbanfix.urban_fix.R;
import com.mobile.urbanfix.urban_fix.factory.ConnectionFactory;

public class MainPresenter implements MainMVP.IMainPresenter {

    private MainMVP.IMainView view;
    private int subScreensOnBackStack;

    public MainPresenter(MainMVP.IMainView view) {
        this.view = view;
    }

    @Override
    public void openMapView() {
        view.showMapView();
    }

    @Override
    public void openAlertView() {
        view.showAlertView();
    }

    @Override
    public void openNoticesView() {
        view.showNoticesView();
    }

    @Override
    public void onBackStackChagend(int cont) {
        Logger.logI("Fragments restantes " + cont);
        this.subScreensOnBackStack = cont;
    }

    @Override
    public void onNavigationItemSelected(int itemId) {

        switch (itemId) {
            case R.id.nav_alert:
                openAlertView();
                break;
            case R.id.nav_notices:
                openNoticesView();
                break;
            case R.id.nav_map:
                openMapView();
                break;
            case R.id.nav_logout:
                view.showLogoffDialog();
                break;
        }

    }

    @Override
    public void onBackPressed() {
        if(subScreensOnBackStack == 1) {
            view.showLogoffDialog();
        } else {
            view.closeCurrentView();
        }
    }

    @Override
    public void doLogoff() {
        ConnectionFactory.logout();
        view.finishView();
    }
}
