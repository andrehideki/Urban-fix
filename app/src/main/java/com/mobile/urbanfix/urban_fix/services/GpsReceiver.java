package com.mobile.urbanfix.urban_fix.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.android.gms.maps.model.LatLng;
import com.mobile.urbanfix.urban_fix.presenter.MainMVP;

public class GpsReceiver extends BroadcastReceiver {

    private static MainMVP.IOnGpsPickupUserLocationAndPossibleAddresses presenter;

    public static void setPresenter(MainMVP.IOnGpsPickupUserLocationAndPossibleAddresses presenter) {
        GpsReceiver.presenter = presenter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        double latitude = intent.getDoubleExtra(GPSService.LAT_KEY, 0.0);
        double longitude = intent.getDoubleExtra(GPSService.LONG_KEY, 0.0);
        if(latitude != 0 && longitude !=0) {
            LatLng latLng = new LatLng(latitude, longitude);
            presenter.onSuccessGetUserLocation(latLng);
        } else {
            presenter.onFailedGetUserLocation(context);
        }
    }
}
