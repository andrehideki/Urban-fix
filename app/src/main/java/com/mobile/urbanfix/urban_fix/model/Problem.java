package com.mobile.urbanfix.urban_fix.model;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mobile.urbanfix.urban_fix.factory.ConnectionFactory;

import java.io.ByteArrayOutputStream;

public class Problem {

    private String id;
    private String description;
    private String kindOfProblem;
    private String date;
    private String status;
    private String photoId;
    private boolean checked;
    private String urgency;
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


    public void insert(Problem object, final Callback.SimpleAsync<Problem> callback) {
        DatabaseReference databaseReference = ConnectionFactory.getAlertsDatabaseReference();
        databaseReference.child(object.getId()).setValue(object).
                addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            callback.onTaskDone(null, true);
                        } else {
                            callback.onTaskDone(null, false);
                        }
                    }
                });
    }


    public void insertProblemPhoto(Bitmap photoBitmap, Problem problem,
                                   final Callback.SimpleAsync<Void> callback)  {
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
                    callback.onTaskDone(null, true);
                } else {
                    callback.onTaskDone(null, false);
                }
            }
        });
    }

    public void findAllAlerts(final Callback.FetchList<Problem> callback) {
        DatabaseReference databaseReference = ConnectionFactory.getAlertsDatabaseReference();
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                Problem p = dataSnapshot.getValue(Problem.class);
                callback.onItemAdded(p);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                Problem p = dataSnapshot.getValue(Problem.class);
                callback.onItemRemoved(p);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onFailed();
            }
        });
    }

    public void donwloadAlertPhoto(final String photoId, final Callback.SimpleAsync<Bitmap> callback) {
        new Thread() {
            public void run() {
                final int oneMegabyte = 1024 * 1024;
                StorageReference storageReference = ConnectionFactory.getFirebaseStorageReference();
                storageReference.child(photoId).getBytes(oneMegabyte).
                        addOnSuccessListener(new OnSuccessListener<byte[]>() {
                            @Override
                            public void onSuccess(byte[] bytes) {
                                Bitmap photoBitMap = BitmapFactory.decodeByteArray(bytes,0, bytes.length);
                                callback.onTaskDone(photoBitMap, true);
                            }
                        }).
                        addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                callback.onTaskDone(null, false);
                            }
                        });
            }
        }.start();
    }
}
