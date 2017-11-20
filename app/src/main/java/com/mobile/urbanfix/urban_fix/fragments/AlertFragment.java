package com.mobile.urbanfix.urban_fix.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mobile.urbanfix.urban_fix.R;
import com.mobile.urbanfix.urban_fix.model.Problem;

import static android.app.Activity.RESULT_OK;

public class AlertFragment extends Fragment {

    public static final int REQUEST_CAPTURE = 256;
    private ImageView photoImageView;
    private Button finishButton;
    private TextView locationTextView;
    private EditText alertDescriptionEditText;

    private LocationManager locationManager;
    private LocationListener locationListener;

    private Problem problem;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_alert, container, false);

    }

    @SuppressLint("MissingPermission")
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        photoImageView  = (ImageView)   getActivity().findViewById(R.id.photoImageView);
        photoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });
        finishButton    = (Button)      getActivity().findViewById(R.id.finishButton);
        finishButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAlert();
            }
        });
        locationTextView = (TextView) getActivity().findViewById(R.id.locationTextView);
        alertDescriptionEditText = (EditText) getActivity().findViewById(R.id.alertDescriptionEditText);

        locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                String loc = "LAT: " + location.getLatitude() + " LONG: " + location.getLongitude();
                problem.setLocalizacao(loc);
                locationTextView.setText(loc);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
        locationManager.requestLocationUpdates("gps",0,0, locationListener);
    }

    private void finishAlert() {
        if(alertDescriptionEditText.getText().toString().isEmpty()) {
            Toast.makeText(getContext(), getString(R.string.alert_finishalert_error),Toast.LENGTH_LONG).show();
        } else {

        }
    }

    @SuppressLint("MissingPermission")
    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAPTURE);
        this.problem = new Problem();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            problem.setPhoto( (Bitmap) extras.get("data") );
            photoImageView.setImageBitmap(problem.getPhoto());
        }
    }
}
