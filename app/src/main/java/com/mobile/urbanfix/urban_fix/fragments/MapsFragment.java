package com.mobile.urbanfix.urban_fix.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mobile.urbanfix.urban_fix.MainActivity;
import com.mobile.urbanfix.urban_fix.R;

public class MapsFragment extends Fragment
        implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener,
        LocationListener {

    private GoogleMap mMap;
    private LocationManager locationManager;
    private Marker currentLocationMarker;
    public static LatLng currentLocationlatLng;


    public static final int PERMISSION_REQUEST_LOCATION_CODE = 99;
    private static final String TAG = "MapsFragment";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getLocationPermissions();
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        } catch ( SecurityException ex) {
            Log.e(TAG, ex.getMessage());
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        try {
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String provider = locationManager.getBestProvider(criteria, true);


            mMap = googleMap;
            mMap.setOnMapClickListener(this);
            mMap.setMyLocationEnabled(true);

        } catch ( SecurityException ex) {
            Log.e( TAG, "Error", ex);
        }
    }


    @Override
    public void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    @Override
    public void onMapClick(LatLng latLng) {
        showMessage( latLng.toString() );
    }



    @Override
    public void onLocationChanged(Location location) {
        Toast.makeText(getActivity(), location.getLatitude() + " | " + location.getLongitude() , Toast.LENGTH_SHORT).show();
        currentLocationlatLng = new LatLng(location.getLatitude(), location.getLongitude());
        MainActivity.enableFAB();
        mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(),location.getLongitude())));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
        showMessage( "Provider habilidatado" );
    }

    @Override
    public void onProviderDisabled(String provider) {
        Intent i = new Intent();
    }

    private void showMessage( String msg ) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    private void markOnMap( Location loc ) {
        MarkerOptions m = new MarkerOptions();
        m.position( new LatLng(loc.getLatitude(), loc.getLongitude()));
        m.title("Novo problema");


        mMap.addMarker(m);
        mMap.moveCamera(CameraUpdateFactory.newLatLng( m.getPosition() ) );
        mMap.animateCamera(CameraUpdateFactory.zoomBy(30));
    }

    public static LatLng currentLocationlatLng() {
        return currentLocationlatLng;
    }

    private void getLocationPermissions() {

        String[] permissions = { Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION};
        if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            initMap();
        }
        else
            ActivityCompat.requestPermissions(getActivity(), permissions, PERMISSION_REQUEST_LOCATION_CODE);
    }

    private void initMap() {
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }
}
