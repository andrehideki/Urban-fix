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

public class AlertComment {


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


    public void find(final String alertId, final Callback.SimpleAsync<AlertComment> callback) {
        DatabaseReference databaseReference = ConnectionFactory.getAlertCommentsDatabaseReference()
                .child(alertId);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        AlertComment alertComment = dataSnapshot.getValue(AlertComment.class);
                        if(alertComment != null) {
                            alertComment.setAlertId(alertId);
                            callback.onTaskDone(alertComment, true);
                        } else
                            callback.onTaskDone(null, false);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        callback.onTaskDone(null, false);

                    }
                });
    }

    public void insert(AlertComment alertComment, final Callback.SimpleAsync<AlertComment> callback) {
        DatabaseReference databaseReference = ConnectionFactory.getAlertCommentsDatabaseReference();
        databaseReference.child(alertId)
                .setValue(alertComment)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
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


    @Override
    public String toString() {
        return "AlertComment{" +
                "alertId='" + alertId + '\'' +
                ", comments=" + comments.toString() +
                '}';
    }
}

