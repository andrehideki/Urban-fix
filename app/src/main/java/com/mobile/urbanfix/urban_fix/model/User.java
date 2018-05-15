package com.mobile.urbanfix.urban_fix.model;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.telecom.Call;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.mobile.urbanfix.urban_fix.factory.ConnectionFactory;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {

    private String email, password;
    private static User user;
    @Exclude
    private String uid;

    public final static String ADDRESS = "address";

    private User(){}

    public static User getInstance() {
        if(user == null) {
            user = new User();
        }
        return user;
    }

    public String getEmail() {
        return user.email;
    }

    public void setEmail(String email) {
        user.email = email;
    }

    public String getPassword() {
        return user.password;
    }

    public void setPassword(String password) {
        user.password = password;
    }
    public String getUID() {
        return user.uid;
    }

    public void setUID(String uid) {
        user.uid = uid;
    }

    public void doLogin(Activity activity, final Callback.SimpleAsync<Void> callback) {
        if(user.getEmail() != null && user.getPassword()!= null) {
            FirebaseAuth auth = ConnectionFactory.getFirebaseAuth();
            auth.signInWithEmailAndPassword(user.getEmail(), user.getPassword() ).
                    addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                user.setUID(ConnectionFactory.getFirebaseAuth().getUid());
                                callback.onTaskDone(null, true);
                            } else {
                                callback.onTaskDone(null, false);
                            }
                        }
                    });
        }

    }

    public void sendPassword(final Callback.SimpleAsync<Void> callback) {
        if(user.getEmail()!= null) {
            FirebaseAuth auth = ConnectionFactory.getFirebaseAuth();
            auth.sendPasswordResetEmail(user.getEmail()).addOnCompleteListener(
                    new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful())
                        callback.onTaskDone(null, true);
                    else
                        callback.onTaskDone(null, false);

                }
            });
        }
    }

    public void register(final Callback.SimpleAsync<Void> callback) {
        if(user.getEmail()!=null && user.getPassword()!= null) {
            FirebaseAuth auth = ConnectionFactory.getFirebaseAuth();
            auth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword()).
                    addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                callback.onTaskDone(null, true);
                            } else {
                                callback.onTaskDone(null, false);
                            }
                        }
                    });
        }
    }


    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
