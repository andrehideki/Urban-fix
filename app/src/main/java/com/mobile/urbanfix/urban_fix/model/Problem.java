package com.mobile.urbanfix.urban_fix.model;


import android.graphics.Bitmap;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Adapter;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mobile.urbanfix.urban_fix.factory.ConnectionFactory;
import com.mobile.urbanfix.urban_fix.presenter.MainMVP;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

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
    public String toString() {
        return "Problem{" +
                "id='" + id + '\'' +
                ", location='" + location + '\'' +
                ", description='" + description + '\'' +
                ", kindOfProblem='" + kindOfProblem + '\'' +
                ", date='" + date + '\'' +
                ", status='" + status + '\'' +
                ", encodedImage='" + encodedImage + '\'' +
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

    public void insertProblemPhoto(Bitmap photoBitmap, String userCpf,
                                   final MainMVP.ICallbackPresenter callback) throws IOException {
        StorageReference storage = ConnectionFactory.getFirebaseStorageReference();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        photoBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] dados = baos.toByteArray();
        storage.child(userCpf).child(getId())
                .putBytes(dados)
                .addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(!task.isSuccessful()) {
                    callback.onFailedTask();
                }
            }
        });
    }

    public static ArrayList<Problem> getUserAlerts() {/*TODO IMPLEMENTAR*/
        final ArrayList<Problem> myAlerts = new ArrayList<>();
        DatabaseReference databaseReference = ConnectionFactory.getProblemsDatabaseReference();
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Problem p = dataSnapshot.getValue(Problem.class);
                Log.i("Script","Problema:" + p.toString());
                myAlerts.add(p);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                myAlerts.remove(dataSnapshot.getValue(Problem.class));
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return myAlerts;
    }
}
