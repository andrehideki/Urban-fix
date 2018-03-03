package com.mobile.urbanfix.urban_fix.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mobile.urbanfix.urban_fix.presenter.MainMVP;

public class GpsReceiver extends BroadcastReceiver {

    private static MainMVP.IOnGpsPickupUserLocation presenter;

    public static void setPresenter(MainMVP.IOnGpsPickupUserLocation presenter) {
        GpsReceiver.presenter = presenter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String received = intent.getStringExtra("LOCATION");
        if(received != null) {
            presenter.onSuccessGetUserLocation(received);
        } else {
            presenter.onFailedGetUserLocation(context);
        }
    }
}
