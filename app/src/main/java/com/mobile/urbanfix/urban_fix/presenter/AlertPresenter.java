package com.mobile.urbanfix.urban_fix.presenter;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Location;
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

import com.mobile.urbanfix.urban_fix.Constants;
import com.mobile.urbanfix.urban_fix.R;
import com.mobile.urbanfix.urban_fix.SystemUtils;
import com.mobile.urbanfix.urban_fix.model.Problem;
import com.mobile.urbanfix.urban_fix.model.User;
import com.mobile.urbanfix.urban_fix.services.FetchAddressReceiver;
import com.mobile.urbanfix.urban_fix.services.FetchAddressSevice;
import com.mobile.urbanfix.urban_fix.services.GPSService;
import com.mobile.urbanfix.urban_fix.services.GpsReceiver;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class AlertPresenter implements  MainMVP.IAlertPresenter,
        MainMVP.IOnGpsPickupUserLocationAndPossibleAddresses,
                                        MainMVP.ICallbackPresenter {
    private transient User user;
    private transient Problem problem;
    private transient static final int CAMERA_PERMISSION_REQUEST = 1889;
    private transient static final int CAMERA_REQUEST = 1334;
    private transient static final int WIDTH = 480;
    private transient static final int HEIGTH = 640;
    private transient String currentPhotoPath;
    private transient MainMVP.IAlertView view;
    private transient ProgressDialog dialog;
    private transient Bitmap bitmap;


    public AlertPresenter(MainMVP.IAlertView view) {
        this.view = view;
    }

    @Override
    public void initAlert(Context context) {
        this.problem = new Problem();
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm aaa");
        this.user = User.getInstance();
        this.problem.setDate(simpleDateFormat.format(date));
        this.problem.setId(user.getCpf());
        this.problem.setChecked(false);
        this.problem.setStatus(context.getString(R.string.alert_status_alert_has_been_issued));
        this.problem.setKindOfProblem("");
        this.problem.setPhotoId(user.getCpf() + "_" + simpleDateFormat.format(date));
        this.currentPhotoPath = "";
        Log.i("Script", "User em tela realizar alerta:" + user.toString());
    }


    @Override
    public void setUrgency(int urgency) {
        Context context = view.getContext();
        if (urgency < LOW) {
            String s = context.getString(R.string.urgency_status_low);
            problem.setUrgency(s);
            view.changeUrgencyStatus(s);
        } else if (urgency < MODERATE) {
            String s = context.getString(R.string.urgency_status_moderate);
            problem.setUrgency(s);
            view.changeUrgencyStatus(s);
        } else { //Critico
            String s = context.getString(R.string.urgency_status_critical);
            problem.setUrgency(s);
            view.changeUrgencyStatus(s);
        }
    }

    @Override
    public void setKindOfProblem(int position, String kindOfProblem) {
        if(position>0) {
            this.problem.setKindOfProblem(kindOfProblem);
        }
    }

    @Override
    public void setAddress(int position, String address) {
        if(position>0) {
            this.problem.setAddress(address);
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
        FetchAddressReceiver.setPrensenter(this);
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
                bitmap = SystemUtils.getResizedBitmap(WIDTH, HEIGTH, this.currentPhotoPath);
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
    public void finishAlert(Activity activity) {/*TODO MELHORAR-> Pegar arquivo pelo file provider*/
        if(isAlertOk()) {

            dialog = new ProgressDialog(activity);
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setMessage(activity.getString(R.string.alert_inserting_alert));
            dialog.show();

            //Insere problema no banco de dados
            Log.i("Script", "Tentando inserir Problema no banco de dados.");
            this.problem.insert(this.problem, this);

            //Insere foto do problema no banco de dados
            try {
                Log.i("Script", "Tentando inserir foto no storage");
                Problem.insertProblemPhoto(this.bitmap, problem, this);
            } catch (IOException e) {
                Log.e("Script", "Deu erro!" + e.getMessage());
            }

            //Atualiza usuário
            Log.i("Script", "Tentando atualizar usuário");
            this.user.setnAlertsDone(user.getnAlertsDone() + 1);
            this.user.update(this.user, this);
            Log.i("Script", "Usuário atualiado");

            view.showMessage(view.getContext().getString(R.string.alert_issued));
            view.finishView();
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
        fetchPossibleUsersAddress();
    }

    @Override
    public void onSuccessGetUserAddresses(ArrayList<String> possibleAddressesList) {
        view.onAddressHasBeenFetched(possibleAddressesList);
    }

    @Override
    public void onFailedGetUserAddresses(Context context) {
        Log.e("Script", "Falha ao buscar localizações do usuário");
        view.showMessage(context.getString(R.string.alert_failed_get_gps_location));
    }

    @Override
    public void onSuccessTask(Constants entity, Object o) {
        if(entity == Constants.NEW_ALERT) {
            Log.i("Script", "Alerta inserido com sucesso!");
        } else if(entity == Constants.UPDATED_USER) {
            Log.i("Script", "Usuário atualizado com sucesso!");
            view.showMessage(view.getContext().getString(R.string.alert_issued));
            dialog.dismiss();
            view.finishView();
        } else if(entity == Constants.NEW_PHOTO) {
            Log.i("Script", "Foto inserida com sucesso!");
        }
    }

    @Override
    public void onFailedTask(Constants entity) {
        Context context = view.getContext();
        if (entity == Constants.NEW_ALERT) {
            Log.e("Script", "Falha ao inserir alerta!");
            dialog.dismiss();
            view.showMessage(context.getString(R.string.alert_failed_to_insert_alert));
        } else if (entity == Constants.UPDATED_USER) {
            Log.e("Script", "Falha ao atualizar usuário!");
            dialog.dismiss();
            view.showMessage(context.getString(R.string.alert_failed_to_update_user));
        } else if (entity == Constants.NEW_PHOTO) {
            Log.e("Script", "Falha ao inserir foto");
            dialog.dismiss();
            view.showMessage(view.getContext().getString(R.string.alert_failed_to_insert_photo));
        }
    }

    private void fetchPossibleUsersAddress() {
        Context context = view.getContext();
        Intent fetchUsersAddressIntent = new Intent(context, FetchAddressSevice.class).
                putExtra(User.ADDRESS, new double[]{problem.getLatitude(), problem.getLogintude()});
        FetchAddressReceiver.setPrensenter(this);
        context.startService(fetchUsersAddressIntent);
    }
}
