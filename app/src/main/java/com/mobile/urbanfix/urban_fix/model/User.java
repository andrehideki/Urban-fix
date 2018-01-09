package com.mobile.urbanfix.urban_fix.model;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.mobile.urbanfix.urban_fix.factory.ConnectionFactory;
import com.mobile.urbanfix.urban_fix.presenter.MainMVP;
import com.mobile.urbanfix.urban_fix.view.MainActivity;

import java.io.Serializable;

public class User implements Serializable {

    private String UUID;
    private String name;
    private String cpf;
    private String birthDate;
    private String email;
    private String password;
    private int nAlertsDone;

    public User() {}

    public User(String name, String cpf, String birthDate, String email, String password) {
        this.name = name;
        this.cpf = cpf;
        this.birthDate = birthDate;
        this.email = email;
        this.password = password;
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
                               final MainMVP.ILoginCallbackPresenter presenter) {
        FirebaseAuth auth = ConnectionFactory.getFirebaseAuth();
        auth.signInWithEmailAndPassword( email, password ).
                addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if( task.isSuccessful() ) {
                            presenter.onSuccessLogin();
                        } else {
                            presenter.onFailedLogin();
                        }
                    }
                });
    }
}
