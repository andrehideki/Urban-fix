package com.mobile.urbanfix.urban_fix.model;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.mobile.urbanfix.urban_fix.factory.ConnectionFactory;
import com.mobile.urbanfix.urban_fix.presenter.MainMVP;

import java.io.Serializable;

public class User implements Serializable, DAO<User> {

    private String UUID, name, cpf, birthDate, email, password;
    private int nAlertsDone;
    private static User user;

    private User(){}

    public static User getInstance() {
        if(user == null) user = new User();
        return user;
    }

    public static void setInstance(User user) {
        User.user = user;
    }

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        user.UUID = UUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        user.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        user.cpf = cpf;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        user.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        user.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        user.password = password;
    }

    public int getnAlertsDone() {
        return nAlertsDone;
    }

    public void setnAlertsDone(int nAlertsDone) {
        user.nAlertsDone = nAlertsDone;
    }

    public static void doLogin(String email, String password, Activity activity,
                               final MainMVP.ICallbackPresenter presenter) {
        FirebaseAuth auth = ConnectionFactory.getFirebaseAuth();
        auth.signInWithEmailAndPassword( email, password ).
                addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if( task.isSuccessful() ) {
                            presenter.onSuccessTask();
                        } else {
                            presenter.onFailedTask();
                        }
                    }
                });
    }

    public static void sendPassword(String email, final MainMVP.ICallbackPresenter presenter) {
        FirebaseAuth auth = ConnectionFactory.getFirebaseAuth();
        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
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
    public User find(String userUId, MainMVP.ICallbackPresenter presenter) {
        DatabaseReference databaseReference = ConnectionFactory.getDatabaseReference();
        databaseReference.child(userUId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User.user = dataSnapshot.getValue(User.class);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return User.user;
    }

    @Override
    public void insert(final User user, final MainMVP.ICallbackPresenter presenter) {
        FirebaseAuth auth = ConnectionFactory.getFirebaseAuth();
        auth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword()).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            FirebaseUser fUser = ConnectionFactory.getFirebaseUser();
                            user.setUUID(fUser.getUid());
                            DatabaseReference db = ConnectionFactory.getUsersDatabaseReferente();
                            db.child(user.getUUID()).setValue(user);
                            presenter.onSuccessTask();
                        } else {
                            presenter.onFailedTask();
                        }
                    }
                });
    }

    @Override
    public void update(User user, final MainMVP.ICallbackPresenter presenter) {
        DatabaseReference databaseReference = ConnectionFactory.getUsersDatabaseReferente();
        databaseReference.child(user.getUUID());
        databaseReference.setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
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
    public void delete(User user, MainMVP.ICallbackPresenter presenter) {

    }

    @Override
    public String toString() {
        return "User{" +
                "UUID='" + user.UUID + '\'' +
                ", name='" + user.name + '\'' +
                ", cpf='" + user.cpf + '\'' +
                ", birthDate='" + user.birthDate + '\'' +
                ", email='" + user.email + '\'' +
                ", password='" + user.password + '\'' +
                ", nAlertsDone=" + user.nAlertsDone +
                '}';
    }
}
