package com.mobile.urbanfix.urban_fix.model;


import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mobile.urbanfix.urban_fix.factory.ConnectionFactory;
import com.mobile.urbanfix.urban_fix.presenter.MainMVP;

import java.sql.Connection;

public class Problem implements DAO<Problem> {

    private String id;
    private String location;
    private String description;
    private String kindOfProblem;
    private String date;
    private String status;
    private String encodedImage;
    private String checked;
    private int urgency;


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

    public String getEncodedImage() {
        return encodedImage;
    }

    public void setEncodedImage(String encodedImage) {
        this.encodedImage = encodedImage;
    }

    public String getChecked() {
        return checked;
    }

    public void setChecked(String checked) {
        this.checked = checked;
    }

    public int getUrgency() {
        return urgency;
    }

    public void setUrgency(int urgency) {
        this.urgency = urgency;
    }

    @Override
    public Problem find(String s, MainMVP.ICallbackPresenter presenter) {
        return null;
    }

    @Override
    public void insert(Problem object, final MainMVP.ICallbackPresenter presenter) {
        DatabaseReference databaseReference = ConnectionFactory.getProblemsDatabaseReference();
        databaseReference.child(object.getDate()).setValue(object).
                addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            presenter.onSuccessTask();
                        } else {
                            presenter.onFailedTask();
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

    public void insertProblemPhoto(Uri photoUri, String userCpf) {
        StorageReference storage = ConnectionFactory.getFirebaseStorageReference();
        storage.child(userCpf).child(photoUri.getLastPathSegment());
        storage.putFile(photoUri);
    }
}
