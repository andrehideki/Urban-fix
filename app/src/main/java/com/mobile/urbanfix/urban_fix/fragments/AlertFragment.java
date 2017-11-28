package com.mobile.urbanfix.urban_fix.fragments;

import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mobile.urbanfix.urban_fix.Connection;
import com.mobile.urbanfix.urban_fix.MainActivity;
import com.mobile.urbanfix.urban_fix.R;
import com.mobile.urbanfix.urban_fix.model.Problem;
import com.mobile.urbanfix.urban_fix.model.User;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class AlertFragment extends Fragment {

    public static final int REQUEST_CAPTURE = 256;
    private ImageView photoImageView;
    private TextView locationTextView;
    private EditText alertDescriptionEditText;
    private Spinner typeOfProblemSpinner;
    private FloatingActionButton finishFloatingActionButton, cancelFloatingActionButton;

    private Problem problem;
    private ArrayAdapter<String> arrayAdapter;
    private User user;

    private FirebaseUser firebaseUser;
    private FirebaseStorage firebaseStorage;
    private DatabaseReference databaseReference;

    @Override
    public void onStart() {
        super.onStart();
        Bundle b = getArguments();
        System.out.println(b.getDouble("lat"));
        System.out.println(b.getDouble("long"));
        initProblem(b.getDouble("Lat"), b.getDouble("Long"));
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_alert, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews();
        setupListerners();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initViews() {
        photoImageView  = (ImageView)   getActivity().findViewById(R.id.photoImageView);
        locationTextView = (TextView) getActivity().findViewById(R.id.locationTextView);
        alertDescriptionEditText = (EditText) getActivity().findViewById(R.id.alertDescriptionEditText);
        typeOfProblemSpinner = (Spinner) getActivity().findViewById(R.id.typeOfProblemSpinner);
        finishFloatingActionButton = (FloatingActionButton) getActivity().findViewById(R.id.finishfloatingActionButton);
        cancelFloatingActionButton = (FloatingActionButton) getActivity().findViewById(R.id.cancelfloatingActionButton);
        setupSpinner();
    }

    private void setupListerners() {
        photoImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        typeOfProblemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(problem!=null) {
                    problem.setKindOfProblem((String) parent.getItemAtPosition( position ));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        finishFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String description = alertDescriptionEditText.getText().toString().trim();
                if( !description.isEmpty() ) {
                    problem.setDescription(description);
                    finishAlert();
                }
            }
        });

        cancelFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                problem = null;
                getFragmentManager().beginTransaction().replace(R.id.mainLayout, new MapsFragment()).commit();
            }
        });
    }

    public void takePhoto() {
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

    private void setupSpinner() {
        List<String> kindOfProblemList = new ArrayList<>();
        kindOfProblemList.add( "Burcaco na estrada" );
        kindOfProblemList.add("Arvore na pista" );
        kindOfProblemList.add( "Vazamento de água" );
        kindOfProblemList.add(  "Falta de energia" );
        arrayAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_dropdown_item,
                kindOfProblemList );
        typeOfProblemSpinner.setAdapter( arrayAdapter );
    }

    private void initProblem( double lat, double log) {
        this.problem = new Problem();
        user = MainActivity.getUser();
        problem.setId(user.getCpf() + "-" + user.getnAlertsDone());
        problem.setLocation(lat + ";" + log);
        problem.setDate(new Date().toString());
        locationTextView.setText("Lat: " + lat + " : Long: " + log);
    }

    private void finishAlert() {

        firebaseUser = Connection.getFirebaseUser();
        firebaseStorage = Connection.getFirebaseStorage();
        databaseReference = Connection.getDatabaseReference();
        StorageReference storageReference = firebaseStorage.getReference();
        ByteArrayOutputStream bao = new ByteArrayOutputStream();

        //databaseReference.child("Alerts").child(problem.getId()).setValue( problem );
        databaseReference.child("Alerts").child(user.getCpf()).child(problem.getDate()).setValue( problem );
        user.setnAlertsDone( user.getnAlertsDone() + 1);
        databaseReference.child("User").child(user.getUUID()).setValue(user);
        //problem.getPhoto().compress(Bitmap.CompressFormat.PNG,100, bao);

        //storageReference.child(problem.getId());
        showMessage(getString(R.string.alert_finishalert_ok));
        getFragmentManager().beginTransaction().replace(R.id.mainLayout, new MapsFragment()).commit();

    }

    private void showMessage( String msg ) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_LONG).show();
    }




}
