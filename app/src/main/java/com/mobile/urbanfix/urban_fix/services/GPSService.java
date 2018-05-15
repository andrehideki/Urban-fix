package com.mobile.urbanfix.urban_fix.services;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.mobile.urbanfix.urban_fix.model.Callback;

public class GPSService extends Service {

    private FusedLocationProviderClient locationProvider;
    public static final String LAT_KEY = "LATITUDE";
    public static final String LONG_KEY = "LONGITUDE";
    public static final String ACTION = "GPSHASBEENCONFIGURED";

    @SuppressLint("MissingPermission")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("Script", "Iniciou o service de busca de latitude e longitude...");
        locationProvider = LocationServices.getFusedLocationProviderClient(this);
        locationProvider.getLastLocation().addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                Intent it = new Intent();
                it.setAction(ACTION);
                if(location != null) {
                    it.putExtra(LAT_KEY, location.getLatitude());
                    it.putExtra(LONG_KEY, location.getLongitude());
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



    public static class GpsReceiver extends BroadcastReceiver {

        private static Callback.SimpleAsync<LatLng> callback;

        @Override
        public void onReceive(Context context, Intent intent) {
            double latitude = intent.getDoubleExtra(GPSService.LAT_KEY, 0.0);
            double longitude = intent.getDoubleExtra(GPSService.LONG_KEY, 0.0);
            if(latitude != 0 && longitude !=0) {
                LatLng latLng = new LatLng(latitude, longitude);
                callback.onTaskDone(latLng, true);
            } else {
                callback.onTaskDone(null, false);
            }
        }

        public static void setCallback(Callback.SimpleAsync<LatLng> callback) {
            GpsReceiver.callback = callback;
        }
    }
}
