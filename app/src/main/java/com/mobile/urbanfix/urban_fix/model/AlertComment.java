package com.mobile.urbanfix.urban_fix.model;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.google.firebase.database.ValueEventListener;
import com.mobile.urbanfix.urban_fix.Logger;
import com.mobile.urbanfix.urban_fix.MainMVP;
import com.mobile.urbanfix.urban_fix.factory.ConnectionFactory;

import java.util.ArrayList;
import java.util.List;

public class AlertComment implements DAO<AlertComment>{


    private String alertId;
    private List<Comment> comments = new ArrayList<>();

    public AlertComment() {}
    public AlertComment(String alertId) {
        this.alertId = alertId;
    }

    public AlertComment(String alertId, List<Comment> comments) {
        this.alertId = alertId;
        this.comments = comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public void insertComment(Comment comment) {
        if(comments != null && comment != null) {
            comments.add(comment);
        }
    }

    public void setAlertId(String alertId) {
        this.alertId = alertId;
    }

    public String getAlertId() {
        return alertId;
    }

    public List<Comment> getComments() {
        return comments;
    }

    @Exclude
    public int getCommentsSize() {
        return comments != null ? comments.size() : 0;
    }

    @Override
    public void find(final String alertId, final DAOCallback<AlertComment> callback) {
        DatabaseReference databaseReference = ConnectionFactory.getAlertCommentsDatabaseReference()
                .child(alertId);
        databaseReference.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        AlertComment alertComment = dataSnapshot.getValue(AlertComment.class);
                        if(alertComment != null) {
                            alertComment.setAlertId(alertId);
                            Logger.logI("Finded:" + alertComment.toString());
                            callback.onObjectFinded(alertComment);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Logger.logI("Entrou aqui");
                        callback.onFailedTask();
                    }
                });
    }

    @Override
    public void insert(AlertComment alertComment, final DAOCallback<AlertComment> callback) {
        DatabaseReference databaseReference = ConnectionFactory.getAlertCommentsDatabaseReference();
        databaseReference.child(alertId).setValue(alertComment).addOnCompleteListener(new OnCompleteListener<Void>() {
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
    public void update(AlertComment object, DAOCallback<AlertComment> callback) {

    }

    @Override
    public void delete(AlertComment object, MainMVP.ICallbackPresenter presenter) {

    }

    @Override
    public String toString() {
        return "AlertComment{" +
                "alertId='" + alertId + '\'' +
                ", comments=" + comments.toString() +
                '}';
    }
}

