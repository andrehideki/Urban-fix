package com.mobile.urbanfix.urban_fix.presenter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mobile.urbanfix.urban_fix.SystemUtils;
import com.mobile.urbanfix.urban_fix.factory.ConnectionFactory;
import com.mobile.urbanfix.urban_fix.model.Problem;
import com.mobile.urbanfix.urban_fix.model.User;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class AlertPresenter implements MainMVP.IAlertPresenter {

    private User user;
    private Problem problem;
    private static final int CAMERA_PERMISSION_REQUEST = 1889;
    private static final int CAMERA_REQUEST = 1334;
    private static final int WIDTH = 480;
    private static final int HEIGTH = 640;
    private String currentPhotoPath;
    private MainMVP.IAlertView view;


    @Override
    public void initAlert() {
        this.problem = new Problem();
        this.user = User.getInstance();
        problem.setDate(new Date().toString());
        this.currentPhotoPath = "";
    }

    @Override
    public void setupSpinner(Activity activity, Spinner spinner) {
        List<String> kindOfProblemList = new ArrayList<>();
        kindOfProblemList.add( "Burcaco na estrada" );
        kindOfProblemList.add("Arvore na pista" );
        kindOfProblemList.add( "Vazamento de água" );
        kindOfProblemList.add(  "Falta de energia" );
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(activity,
                android.R.layout.simple_spinner_dropdown_item, kindOfProblemList );
        spinner.setAdapter(arrayAdapter);
    }

    @Override
    public void dispachTakePhotoIntent(Fragment fragment, MainMVP.IAlertView view) {

        this.view = view;
        Activity activity = fragment.getActivity();
        if(SystemUtils.askPermission(activity, Manifest.permission.CAMERA, CAMERA_PERMISSION_REQUEST)) {
            if(activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                takePhoto(fragment);
            } else {
                Log.i("Script", "O dispositivo não possuir camera");
            }
        } else {
            Log.i("Script", "O dispositivo não possui permissão de camera");
        }
    }

    @Override
    public void onRequestPermissionResult(Fragment fragment, int requestCode, String[] permissions, int[] grantResults) {
        Log.i("Script", "Resultado chegou:" + requestCode);
        switch (requestCode) {
            case CAMERA_PERMISSION_REQUEST: {
                if(grantResults.length>0 && grantResults[0]== PackageManager.PERMISSION_GRANTED) {
                    takePhoto(fragment);
                }
                break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CAMERA_REQUEST) {
            Log.i("Script", "Chegou request de camera");
            if(resultCode == RESULT_OK) {
                Log.i("Script", "Foto tirado com sucesso. Colocando em photoImageView");
                Bitmap bitmap = SystemUtils.getResizedBitmap(WIDTH, HEIGTH, this.currentPhotoPath);
                Log.i("Script","Salvando Bitmap: " + this.currentPhotoPath );
                SystemUtils.saveBitmap(this.currentPhotoPath, bitmap);

                view.setupPhotoImageView(bitmap);

            } else {
                Log.e("Script","Deu erro");
            }
        }
    }

    @Override
    public void cancelAlert(Fragment fragment) {
        this.problem = null;
        FragmentManager fm = fragment.getFragmentManager();
        fm.popBackStack();
    }

    @Override
    public void finishAlert(String description, int urgency) {
        if(isAlertOk(description)) {
            Uri photoUri = Uri.fromFile(new File(this.currentPhotoPath));

            DatabaseReference databaseReference = ConnectionFactory.getDatabaseReference();
            FirebaseStorage storage = ConnectionFactory.getFirebaseStorage();
            StorageReference storageReference = storage.getReference().child(user.getCpf()).
                    child(photoUri.getLastPathSegment());

            storageReference.putFile(photoUri);
            databaseReference.child("Alerts").child(user.getUUID()).child(problem.getDate()).setValue(problem);
            user.setnAlertsDone(user.getnAlertsDone() + 1);
            databaseReference.child("User").child(user.getUUID()).setValue(user);

            //showMessage(getString(R.string.alert_finishalert_ok));
            //getFragmentManager().beginTransaction().replace(R.id.mainLayout, new MapsFragment()).commit();
        }
    }

    private void takePhoto(Fragment fragment) {
        try {
            Context context = fragment.getContext();
            File f = SystemUtils.createTempFile(context);
            Uri photoUri = FileProvider.getUriForFile(context, "com.mobile.urbanfix",f);
            Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

            fragment.startActivityForResult(takePhotoIntent, CAMERA_REQUEST);

            this.currentPhotoPath = f.getAbsolutePath();
        } catch (IOException e) {
            Log.i("Script", e.getMessage());
        }
    }

    private boolean isAlertOk(String description) {//TODO Falta o tipo de problema
        return !description.isEmpty() && this.currentPhotoPath!=null;
    }
}
