package com.mobile.urbanfix.urban_fix.model;

import android.app.Activity;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
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

    public String getUUID() {
        return UUID;
    }

    public void setUUID(String UUID) {
        this.UUID = UUID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getnAlertsDone() {
        return nAlertsDone;
    }

    public void setnAlertsDone(int nAlertsDone) {
        this.nAlertsDone = nAlertsDone;
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
    public User find(String s, MainMVP.ICallbackPresenter presenter) {
        return null;
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
    public void update(User user, MainMVP.ICallbackPresenter presenter) {

    }

    @Override
    public void delete(User user, MainMVP.ICallbackPresenter presenter) {

    }
}
