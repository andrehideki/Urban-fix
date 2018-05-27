package com.mobile.urbanfix.urban_fix.presenter;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mobile.urbanfix.urban_fix.Logger;
import com.mobile.urbanfix.urban_fix.MainMVP;
import com.mobile.urbanfix.urban_fix.R;
import com.mobile.urbanfix.urban_fix.SystemUtils;
import com.mobile.urbanfix.urban_fix.model.Callback;
import com.mobile.urbanfix.urban_fix.model.Problem;
import com.mobile.urbanfix.urban_fix.view.fragments.AlertDialogFragment;

import java.util.ArrayList;

public class MapsPresenter implements MainMVP.IMapsPresenter,
        LocationListener,
        OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener, Callback.FetchList<Problem> {

    private MainMVP.IMapsView view;
    private LocationManager locationManager;
    private GoogleMap googleMap;
    private ArrayList<Problem> problems;
    private static final int LOCATION_REQUEST_CODE = 1998;

    public MapsPresenter(MainMVP.IMapsView view) {
        this.view = view;
        this.problems = new ArrayList<>();
    }


    @Override
    public void openAlertFragment() {
        view.showMapsView();
    }

    @Override
    public void getLocationPermissions(Activity activity) {
        SystemUtils.askPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION, LOCATION_REQUEST_CODE);
    }

    @Override
    public void startLocationListener(Activity activity) {
        try {
            this.locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
            this.locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        } catch (SecurityException e) {
            Log.e("Script", e.getMessage());
        }
    }

    @Override
    public void stopLocationListener() {
        this.locationManager.removeUpdates(this);
    }

    @Override
    public void onRequestPermissionResult(Fragment fragment, int requestCode, String[] permissions, int[] grantResults) {}

    @Override
    public void initMap() {
        Fragment fragment = (Fragment) view;
        SupportMapFragment mapFragment = (SupportMapFragment) fragment.getChildFragmentManager().
                findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void loadAlertsOnMap() {
        Problem p = new Problem();
        p.findAllAlerts(this);
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {
        Activity activity = (Activity) view.getContext();
        getLocationPermissions(activity);
        Intent intent = new Intent((Settings.ACTION_LOCATION_SOURCE_SETTINGS));
        activity.startActivity(intent);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i("Script", "Mapa pronto e carregado");
        try {
            this.googleMap = googleMap;
            this.googleMap.setMyLocationEnabled(true);
            this.googleMap.setOnMarkerClickListener(this);
            loadAlertsOnMap();
        } catch (SecurityException e) {
            Log.e("Script", e.getMessage());
        }
    }

    private void markOnMap(double lat, double longi, String title, int index) {
        MarkerOptions m = new MarkerOptions();
        m.position(new LatLng( lat ,longi));
        m.title(title);
        Marker marker = googleMap.addMarker(m);
        marker.setTag(index);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        int index = (int) marker.getTag();
        Problem selectedProblem = problems.get(index);
        openProblemDialogFragment(selectedProblem);
        return false;
    }

    private void openProblemDialogFragment(Problem problem) {
        AlertDialogPresenter.setProblem(problem);
        AlertDialogFragment dialog = AlertDialogFragment.newInstance(problem);
        dialog.show(view.getCurrentFragmentManager().beginTransaction(), "TESTE");
    }


    @Override
    public void onItemAdded(Problem result) {
        double lat = result.getLocation().getLatitude();
        double longi = result.getLocation().getLongitude();
        problems.add(result);
        int currentIndex = problems.size() - 1;
        markOnMap(lat, longi, result.getKindOfProblem(), currentIndex);
    }

    @Override
    public void onItemRemoved(Problem result) {
        problems.remove(result);
    }

    @Override
    public void onFailed() {
        Logger.logE("Deu erro na busca dos alertas.");
    }
}