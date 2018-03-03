package com.mobile.urbanfix.urban_fix.presenter;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mobile.urbanfix.urban_fix.R;
import com.mobile.urbanfix.urban_fix.SystemUtils;
import com.mobile.urbanfix.urban_fix.factory.ConnectionFactory;
import com.mobile.urbanfix.urban_fix.model.Problem;
import com.mobile.urbanfix.urban_fix.model.User;
import com.mobile.urbanfix.urban_fix.services.GPSService;
import com.mobile.urbanfix.urban_fix.services.GpsReceiver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class AlertPresenter implements  MainMVP.IAlertPresenter,
                                        MainMVP.IOnGpsPickupUserLocation,
                                        MainMVP.ICallbackPresenter {

    private transient User user;
    private transient Problem problem;
    private transient static final int CAMERA_PERMISSION_REQUEST = 1889;
    private transient static final int CAMERA_REQUEST = 1334;
    private transient static final int WIDTH = 480;
    private transient static final int HEIGTH = 640;
    private transient String currentPhotoPath;
    private transient MainMVP.IAlertView view;


    public AlertPresenter(MainMVP.IAlertView view) {
        this.view = view;
    }

    @Override
    public void initAlert(Context context) {
        this.problem = new Problem();
        Date date = new Date();
        this.user = User.getInstance();
        this.problem.setDate(date.toString());
        this.problem.setId(user.getCpf());
        this.problem.setChecked(context.getString(R.string.alert_checked_initialized) +
                new SimpleDateFormat("dd-MM-yyyy hh:mm aaa").format(date));
        this.currentPhotoPath = "";
    }

    @Override
    public void setupSpinner(Activity activity, Spinner spinner) {
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(activity,
                R.array.problems_array, android.R.layout.simple_expandable_list_item_1);
        spinner.setAdapter(spinnerAdapter);
    }

    @Override
    public void setUrgency(int urgency) {
        this.problem.setUrgency(urgency);
    }

    @Override
    public void setKindOfProblem(int position, String kindOfProblem) {
        if(position>0) {
            this.problem.setKindOfProblem(kindOfProblem);
        }
    }

    @Override
    public void setDescription(String description, TextInputLayout descriptionTextInputLayout, Context context) {
        if(!description.isEmpty()) {
            this.problem.setDescription(description);
        } else {
            descriptionTextInputLayout.setErrorEnabled(true);
            descriptionTextInputLayout.setError(context.getString(R.string.alert_description_textview_is_empty));
        }
    }

    @Override
    public void startGPS(Context context) {
        Intent gpsIntent = new Intent(context, GPSService.class);
        GpsReceiver.setPresenter(this);
        context.startService(gpsIntent);
    }

    @Override
    public void dispachTakePhotoIntent(Fragment fragment, MainMVP.IAlertView view) {

        this.view = view;
        Activity activity = fragment.getActivity();
        if(SystemUtils.askPermission(activity, Manifest.permission.CAMERA, CAMERA_PERMISSION_REQUEST)) {
            if(activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                takePhoto(fragment);
            } else {
                Log.i("Script", "O dispositivo não possui camera");
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
            if(resultCode == RESULT_OK) {
                Bitmap bitmap = SystemUtils.getResizedBitmap(WIDTH, HEIGTH, this.currentPhotoPath);
                Log.i("Script","Salvando Bitmap: " + this.currentPhotoPath );
                SystemUtils.saveBitmap(this.currentPhotoPath, bitmap);

                view.setupPhotoImageView(bitmap);
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
    public void finishAlert() {
        if(isAlertOk()) {
            //Insere problema no banco de dados
            this.problem.insert(this.problem, this);

            //Insere foto do problema no banco de dados
            this.problem.insertProblemPhoto(Uri.fromFile(new File(this.currentPhotoPath)),
                    this.user.getCpf());

            //Atualiza usuário
            this.user.setnAlertsDone(user.getnAlertsDone() + 1);
            this.user.update(this.user, this);
            Log.i("Script", "Usuário atualiado");
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

    private boolean isAlertOk() {
        return !problem.getDescription().isEmpty()
                && this.currentPhotoPath!=null
                && !this.problem.getKindOfProblem().isEmpty();
    }

    @Override
    public void onFailedGetUserLocation(Context context) {
        SystemUtils.showMessage(context, context.getString(R.string.alert_failed_get_gps_location),
                Toast.LENGTH_LONG);
    }

    @Override
    public void onSuccessGetUserLocation(String location) {
        this.problem.setLocation(location);
        this.view.onLocationDefined(location);
    }

    @Override
    public void onSuccessTask() {

    }

    @Override
    public void onFailedTask() {

    }
}
