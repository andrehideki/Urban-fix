package com.mobile.urbanfix.urban_fix.model;


import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mobile.urbanfix.urban_fix.Constants;
import com.mobile.urbanfix.urban_fix.adapter.MyAlertsAdapter;
import com.mobile.urbanfix.urban_fix.factory.ConnectionFactory;
import com.mobile.urbanfix.urban_fix.presenter.MainMVP;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Problem implements DAO<Problem> {

    private String id;
    private String location;
    private String address;
    private String description;
    private String kindOfProblem;
    private String date;
    private String status;
    private String photoId;
    private boolean checked;
    private String urgency;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public double getLatitude() {
        return Double.parseDouble(this.location.split(";")[0]);
    }

    public double getLogintude() {
        return Double.parseDouble(this.location.split(";")[1]);
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getKindOfProblem() {
        return kindOfProblem;
    }

    public void setKindOfProblem(String kindOfProblem) {
        this.kindOfProblem = kindOfProblem;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPhotoId() {
        return photoId;
    }

    public void setPhotoId(String photoId) {
        this.photoId = photoId;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getUrgency() {
        return urgency;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setUrgency(String urgency) {
        this.urgency = urgency;
    }

    @Override
    public String toString() {
        return "Problem{" +
                "id='" + id + '\'' +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                ", kindOfProblem='" + kindOfProblem + '\'' +
                ", date='" + date + '\'' +
                ", status='" + status + '\'' +
                ", photoId='" + photoId + '\'' +
                ", checked='" + checked + '\'' +
                ", urgency=" + urgency +
                '}';
    }

    @Override
    public Problem find(String s, MainMVP.ICallbackPresenter presenter) {
        return null;
    }

    @Override
    public void insert(Problem object, final MainMVP.ICallbackPresenter presenter) {
        DatabaseReference databaseReference = ConnectionFactory.getAlertsDatabaseReference();
        databaseReference.child(object.getDate()).setValue(object).
                addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            presenter.onSuccessTask(Constants.NEW_ALERT, null);
                        } else {
                            presenter.onFailedTask(Constants.NEW_ALERT);
                        }
                    }
                });
    }

    @Override
    public void update(Problem object, MainMVP.ICallbackPresenter presenter) {

    }

    @Override
    public void delete(Problem object, MainMVP.ICallbackPresenter presenter) {

    }

    public static void insertProblemPhoto(Bitmap photoBitmap, Problem problem,
                                   final MainMVP.ICallbackPresenter callback) throws IOException {
        StorageReference storage = ConnectionFactory.getFirebaseStorageReference();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photoBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] dados = baos.toByteArray();
        storage.child(problem.getPhotoId())
                .putBytes(dados)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(!task.isSuccessful()) {
                    callback.onFailedTask(Constants.NEW_PHOTO);
                } else {
                    callback.onSuccessTask(Constants.NEW_PHOTO, null);
                }
            }
        });
    }

    public static void getUserAlerts(final ArrayList<Problem> myAlerts , final MyAlertsAdapter adapter,
                                     final MainMVP.ICallbackListOfAlerts callback) {
        DatabaseReference databaseReference = ConnectionFactory.getAlertsDatabaseReference();
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Problem p = dataSnapshot.getValue(Problem.class);
                Log.i("Script","Problema:" + p.toString());
                myAlerts.add(p);
                adapter.notifyDataSetChanged();
                callback.onListOfAlertsChanged();
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                int position = myAlerts.indexOf(dataSnapshot.getValue(Problem.class));
                myAlerts.remove(position);
                adapter.notifyItemRemoved(position);
                callback.onListOfAlertsChanged();
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
