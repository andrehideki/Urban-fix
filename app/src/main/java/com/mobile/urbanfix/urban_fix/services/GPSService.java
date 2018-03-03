package com.mobile.urbanfix.urban_fix.services;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class GPSService extends Service {

    private FusedLocationProviderClient locationProvider;

    @SuppressLint("MissingPermission")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("Script", "Chegou a iniciar o service");
        locationProvider = LocationServices.getFusedLocationProviderClient(this);
        locationProvider.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                Intent it = new Intent();
                it.setAction("GPSHASBEENCONFIGURED");
                if(location != null) {
                    it.putExtra("LOCATION", location.getLatitude() + ";" + location.getLongitude());
                }
                sendBroadcast(it);
            }
        });

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


}
