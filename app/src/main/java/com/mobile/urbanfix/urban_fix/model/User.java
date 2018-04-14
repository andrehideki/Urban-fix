package com.mobile.urbanfix.urban_fix.model;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.mobile.urbanfix.urban_fix.Constants;
import com.mobile.urbanfix.urban_fix.factory.ConnectionFactory;
import com.mobile.urbanfix.urban_fix.presenter.MainMVP;

import java.io.Serializable;

public class User implements Serializable, DAO<User> {

    private String UUID, name, cpf, birthDate, email, password;
    private int nAlertsDone;
    private static User user;
    public final static String ADDRESS = "address";//Usado para passar a geoposição do usuário

    private User(){}

    public static User getInstance() {
        if(user == null) {
            user = new User();
        }
        return user;
    }

    public static void setInstance(User user) {
        User.user = user;
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
                            presenter.onSuccessTask(Constants.DO_LOGIN, null);
                        } else {
                            presenter.onFailedTask(Constants.DO_LOGIN);
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
                    presenter.onSuccessTask(Constants.SEND_PASSWORD,null);
                } else {
                    presenter.onFailedTask(Constants.SEND_PASSWORD);
                }
            }
        });
    }

    @Override
    public User find(final String userUId, final MainMVP.ICallbackPresenter presenter) {
        Log.i("Script", "Buscando usuário com uid: " + userUId);
        final User[] u = new User[1];
        final DatabaseReference databaseReference = ConnectionFactory.getUsersDatabaseReferente();
        ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                u[0] = dataSnapshot.getValue(User.class);
                Log.i("Script", dataSnapshot.getValue(User.class).toString());
                if (u[0] != null) {
                    Log.i("Script", "Pegando dados do usuário: " + u[0].toString());
                    presenter.onSuccessTask(Constants.FIND_USER, u[0]);
                    databaseReference.child(userUId).removeEventListener(this);
                } else {
                    Log.e("Script", "O usuário obitido é nulo!");
                    presenter.onFailedTask(Constants.FIND_USER);
                    databaseReference.child(userUId).removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("Script", "Falha ao encontrar usuário");
                presenter.onFailedTask(Constants.FIND_USER);
                databaseReference.child(userUId).removeEventListener(this);
            }
        };
        databaseReference.child(userUId).addValueEventListener(listener);
        return u[0];
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
                            presenter.onSuccessTask(Constants.NEW_USER,null);
                        } else {
                            presenter.onFailedTask(Constants.NEW_USER);
                        }
                    }
                });
    }

    @Override
    public void update(User user, final MainMVP.ICallbackPresenter presenter) {
        DatabaseReference databaseReference = ConnectionFactory.getUsersDatabaseReferente();
        databaseReference.child(user.getUUID()).
                setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    presenter.onSuccessTask(Constants.UPDATED_USER, null);
                } else {
                    presenter.onFailedTask(Constants.UPDATED_USER);
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
