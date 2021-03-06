package com.mobile.urbanfix.urban_fix.presenter;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
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

import com.google.android.gms.maps.model.LatLng;
import com.mobile.urbanfix.urban_fix.Logger;
import com.mobile.urbanfix.urban_fix.MainMVP;
import com.mobile.urbanfix.urban_fix.R;
import com.mobile.urbanfix.urban_fix.SystemUtils;
import com.mobile.urbanfix.urban_fix.model.Callback;
import com.mobile.urbanfix.urban_fix.model.Location;
import com.mobile.urbanfix.urban_fix.model.Person;
import com.mobile.urbanfix.urban_fix.model.Problem;
import com.mobile.urbanfix.urban_fix.model.User;
import com.mobile.urbanfix.urban_fix.services.FetchAddressSevice;
import com.mobile.urbanfix.urban_fix.services.GPSService;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class AlertPresenter implements  MainMVP.IAlertPresenter {

    private transient User user;
    private transient Person person;
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
    public void initAlert() {
        this.problem = new Problem();
        this.person = Person.getInstance();
        Date date = new Date();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm aaa",
                Locale.getDefault());
        this.user = User.getInstance();
        this.problem.setDate(simpleDateFormat.format(date));
        this.problem.setId(person.getCpf() + person.getnAlertsDone());
        this.problem.setChecked(false);
        this.problem.setStatus(view.getContext().getString(R.string.alert_status_alert_has_been_issued));
        this.problem.setKindOfProblem("");
        this.problem.setPhotoId(person.getCpf() + "_" + simpleDateFormat.format(date));
        this.currentPhotoPath = "";
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
            this.problem.getLocation().setAddress(address);
        }
    }

    @Override
    public void setDescription(String description) {
        if(!description.isEmpty()) {
            this.problem.setDescription(description);
        } else {
            view.showDescriptionError();
        }
    }

    @Override
    public void startGPS() {
        Context context = view.getContext();
        final Intent gpsIntent = new Intent(context, GPSService.class);
        GPSService.GpsReceiver.setCallback(new Callback.SimpleAsync<LatLng>() {
            @Override
            public void onTaskDone(LatLng result, boolean success) {
                if(success) {
                    Location location = new Location();
                    location.setLatitude(result.latitude);
                    location.setLongitude(result.longitude);

                    problem.setLocation(location);

                    fetchPossibleUsersAddress();
                } else {
                    Logger.logE("Erro ao pegar localização do usuário");
                }
            }
        });

        context.startService(gpsIntent);
    }

    @Override
    public void dispachTakePhotoIntent() {

        Activity activity =  (Activity) view.getContext();
        if(SystemUtils.askPermission(activity, Manifest.permission.CAMERA, CAMERA_PERMISSION_REQUEST)) {
            if(activity.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
                takePhoto();
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
                    takePhoto();
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

            Log.i("Script", "Tentando inserir Problema no banco de dados.");
            problem.insert(this.problem, new Callback.SimpleAsync<Problem>() {
                @Override
                public void onTaskDone(Problem result, boolean success) {
                    if(success) {
                        Log.i("Script", "Alerta inserido com sucesso!");
                        dialog.setMessage(view.getContext().getString(R.string.alert_inserting_photo));
                        tryToInsertPhotoOnStorage();
                    } else {
                        Log.e("Script", "Falha ao inserir alerta!");
                        dialog.dismiss();
                        view.showMessage(view.getContext().getString(R.string.alert_failed_to_insert_alert));
                    }
                }
            });

        }
    }

    private void tryToInsertPhotoOnStorage() {
        Log.i("Script", "Tentando inserir foto no storage");
        problem.insertProblemPhoto(this.bitmap, problem, new Callback.SimpleAsync<Void>() {
                    @Override
                    public void onTaskDone(Void result, boolean success) {
                        if(success) {
                            Log.i("Script", "Foto inserida com sucesso!");
                            Log.i("Script", "Tentando atualizar usuário");
                            dialog.setMessage(view.getContext().getString(R.string.alert_updating_user));
                            tryToUpdateUser();
                        } else {
                            Log.e("Script", "Falha ao inserir foto");
                            dialog.cancel();
                            view.showMessage(view.getContext().getString(R.string.alert_failed_to_insert_photo));
                        }
                    }
                });
    }

    private void tryToUpdateUser() {
        this.person.setnAlertsDone(person.getnAlertsDone() + 1);
        this.person.update(this.person, new Callback.SimpleAsync<Void>() {
            @Override
            public void onTaskDone(Void result, boolean success) {
                if(success) {
                    Log.i("Script", "Usuário atualizado com sucesso!Alerta finalizado");
                    view.showMessage(view.getContext().getString(R.string.alert_issued));
                    dialog.cancel();
                    view.finishView();
                } else {
                    Context context = view.getContext();
                    view.showMessage(context.getString(R.string.alert_failed_to_update_user));
                    dialog.cancel();
                }
            }
        });
    }

    private void takePhoto() {
        try {
            Context context = view.getContext();
            File f = SystemUtils.createTempFile(context);
            Uri photoUri = FileProvider.getUriForFile(context, "com.mobile.urbanfix",f);
            Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);

            view.startForResult(takePhotoIntent, CAMERA_REQUEST);

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


    private void fetchPossibleUsersAddress() {
        FetchAddressSevice.FetchAddressReceiver.setCallback(new Callback.SimpleAsync<ArrayList<String>>() {
            @Override
            public void onTaskDone(ArrayList<String> result, boolean success) {
                if(success) {
                    Logger.logI("Sucesso ao pegar possiveis endereços do usuário");
                    view.onAddressHasBeenFetched(result);
                } else {
                    Logger.logE("Erro ao pegar endereços do usuário");
                }
            }
        });

        Context context = view.getContext();
        Intent fetchUsersAddressIntent = new Intent(context, FetchAddressSevice.class).
                putExtra(User.ADDRESS, new double[]{problem.getLocation().getLatitude(),
                        problem.getLocation().getLongitude()});
        context.startService(fetchUsersAddressIntent);
    }
}
