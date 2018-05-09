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
import com.mobile.urbanfix.urban_fix.adapter.MyAlertsAdapter;
import com.mobile.urbanfix.urban_fix.factory.ConnectionFactory;
import com.mobile.urbanfix.urban_fix.MainMVP;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Problem implements DAO<Problem> {

    private String id;
    private String description;
    private String kindOfProblem;
    private String date;
    private String status;
    private String photoId;
    private boolean checked;
    private String urgency;
    private int validations;
    private Location location;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
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
    public void find(String s, final DAOCallback<Problem> callback) {

    }

    @Override
    public void insert(Problem object, final DAOCallback<Problem> callback) {
        DatabaseReference databaseReference = ConnectionFactory.getAlertsDatabaseReference();
        databaseReference.child(object.getId()).setValue(object).
                addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            callback.onObjectInserted();
                        } else {
                            callback.onFailedTask();
                        }
                    }
                });
    }

    @Override
    public void update(Problem object, DAOCallback<Problem> callback) {

    }


    @Override
    public void delete(Problem object, MainMVP.ICallbackPresenter presenter) {

    }

    public void insertProblemPhoto(Bitmap photoBitmap, Problem problem,
                                   final StorageCallback callback)  {
        StorageReference storage = ConnectionFactory.getFirebaseStorageReference();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photoBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] dados = baos.toByteArray();
        storage.child(problem.getPhotoId())
                .putBytes(dados)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()) {
                    callback.onSuccess();
                } else {
                    callback.onFailed();
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

    public interface StorageCallback {
        void onSuccess();
        void onFailed();
    }
}
