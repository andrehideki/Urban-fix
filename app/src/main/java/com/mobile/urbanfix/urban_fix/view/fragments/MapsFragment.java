package com.mobile.urbanfix.urban_fix.view.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.mobile.urbanfix.urban_fix.factory.ConnectionFactory;
import com.mobile.urbanfix.urban_fix.view.MainActivity;
import com.mobile.urbanfix.urban_fix.R;
import com.mobile.urbanfix.urban_fix.model.Problem;

import java.util.ArrayList;

public class MapsFragment extends Fragment
        implements OnMapReadyCallback,
        GoogleMap.OnMapClickListener,
        LocationListener{

    private GoogleMap mMap;
    private LocationManager locationManager;
    private Marker currentLocationMarker;
    public static LatLng currentLocationlatLng;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;
    private ArrayList<LatLng> problemLocations;

    public static final int PERMISSION_REQUEST_LOCATION_CODE = 99;
    private static final String TAG = "MapsFragment";
    ArrayAdapter<LatLng> arrayAdapter;

    private boolean providerEnabled =false;


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
        firebaseUser = ConnectionFactory.getFirebaseUser();
        databaseReference = ConnectionFactory.getDatabaseReference();
        problemLocations = new ArrayList<>();
    }

    private void setCurrentUserLocation() {
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, false);
        @SuppressLint("MissingPermission") Location loc = locationManager.getLastKnownLocation(provider);
        this.currentLocationlatLng = new LatLng( loc.getLatitude(), loc.getLongitude());
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
            if ( providerEnabled ) setCurrentUserLocation();
            if (currentLocationlatLng != null ) MainActivity.enableFAB();
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            databaseReference.child("Alerts").child(firebaseUser.getUid()).addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                    Problem p = dataSnapshot.getValue(Problem.class);
                    String latLong[] = (p.getLocation()).split(";");
                    double lat = Double.parseDouble( latLong[0] );
                    double longi = Double.parseDouble( latLong[1]);
                    LatLng latLng = new LatLng(lat, longi);
                    problemLocations.add( latLng );
                    markOnMap(lat, longi);
                }

                @Override
                public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onChildRemoved(DataSnapshot dataSnapshot) {
                    Problem p = dataSnapshot.getValue(Problem.class);
                    String latLong[] = (p.getLocation()).split(";");
                    double lat = Double.parseDouble( latLong[0] );
                    double longi = Double.parseDouble( latLong[1]);
                    LatLng latLng = new LatLng(lat, longi);
                    problemLocations.remove( latLng );
                }

                @Override
                public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } catch ( SecurityException ex) {
            Log.e(TAG, ex.getMessage());
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        try {
            locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

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
        currentLocationlatLng = new LatLng(location.getLatitude(), location.getLongitude());
        MainActivity.enableFAB();

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
        providerEnabled = true;
        setCurrentUserLocation();
        showMessage( "Provider habilidatado" );
    }

    @Override
    public void onProviderDisabled(String provider) {
        providerEnabled = false;
        startActivity( new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS) );
    }

    private void showMessage( String msg ) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }

    private void markOnMap( double lat, double longi ) {
        MarkerOptions m = new MarkerOptions();
        m.position( new LatLng( lat , longi ));
        m.title("Novo problema");

        mMap.addMarker(m);
    }

    public static LatLng getCurrentLocationlatLng() {
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

    @Override
    public void onStop() {
        super.onStop();
        locationManager.removeUpdates(this);
    }
}
