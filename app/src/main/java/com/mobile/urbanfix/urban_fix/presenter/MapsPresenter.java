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
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.mobile.urbanfix.urban_fix.R;
import com.mobile.urbanfix.urban_fix.SystemUtils;
import com.mobile.urbanfix.urban_fix.factory.ConnectionFactory;
import com.mobile.urbanfix.urban_fix.model.Problem;
import com.mobile.urbanfix.urban_fix.model.User;
import com.mobile.urbanfix.urban_fix.view.dialog.ProblemDialogFragment;
import com.mobile.urbanfix.urban_fix.view.fragments.AlertFragment;

import java.util.ArrayList;

public class MapsPresenter implements MainMVP.IMapsPresenter,
        LocationListener,
        OnMapReadyCallback,
        GoogleMap.OnMarkerClickListener {

    private MainMVP.IMapsView view;
    private LocationManager locationManager;
    private GoogleMap googleMap;
    private ArrayList<Problem> problems;
    public static final String TAG_STACK = "STACK";
    public static final int LOCATION_REQUEST_CODE = 1998;

    public MapsPresenter(MainMVP.IMapsView view) {
        this.view = view;
        this.problems = new ArrayList<>();
    }


    @Override
    public void openAlertFragment(AppCompatActivity activity) {
        AlertFragment fragment = new AlertFragment();
        FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.mainLayout, fragment);
        fragmentTransaction.addToBackStack(TAG_STACK);
        fragmentTransaction.commit();
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
    public void onRequestPermissionResult(Fragment fragment, int requestCode, String[] permissions, int[] grantResults) {

    }

    @Override
    public void initMap() {
        Fragment fragment = (Fragment) view;
        SupportMapFragment mapFragment = (SupportMapFragment) fragment.getChildFragmentManager().
                findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void loadAlertsOnMap() {
        User user = User.getInstance();
        DatabaseReference alertsDatabaseReference = ConnectionFactory.getAlertsDatabaseReference();
        alertsDatabaseReference.child(user.getUUID()).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Problem p = dataSnapshot.getValue(Problem.class);
                double lat = p.getLatitude();
                double longi = p.getLogintude();
                problems.add(p);
                int currentIndex = problems.size() - 1;
                markOnMap(lat, longi, p.getKindOfProblem(), currentIndex);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Problem p = dataSnapshot.getValue(Problem.class);
                //String latLong[] = (p.getLocation()).split(";");
                //double lat = Double.parseDouble( latLong[0] );
                //double longi = Double.parseDouble( latLong[1]);
                problems.remove(p);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
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
        view.showMessage(selectedProblem.toString());
        openProblemDialogFragment(selectedProblem);
        return false;
    }

    private void openProblemDialogFragment(Problem problem) {
        ProblemDialogFragment dialog = ProblemDialogFragment.newInstance(problem);
        dialog.show(view.getCurrentFragmentManager().beginTransaction(), "TESTE");
    }
}
