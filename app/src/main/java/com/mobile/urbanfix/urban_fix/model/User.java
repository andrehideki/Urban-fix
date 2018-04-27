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
import com.google.firebase.database.Exclude;
import com.google.firebase.database.ValueEventListener;
import com.mobile.urbanfix.urban_fix.Constants;
import com.mobile.urbanfix.urban_fix.factory.ConnectionFactory;
import com.mobile.urbanfix.urban_fix.presenter.MainMVP;

import java.io.Serializable;

public class User implements Serializable, DAO<User> {

    private String email, password;
    private static User user;
    @Exclude
    private String uid;

    public final static String ADDRESS = "address";//Usado para passar a geoposição do usuário

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

    public void doLogin(Activity activity, final LoginUserCallback callback) {
        if(user.getEmail() != null && user.getPassword()!= null) {
            FirebaseAuth auth = ConnectionFactory.getFirebaseAuth();
            auth.signInWithEmailAndPassword(user.getEmail(), user.getPassword() ).
                    addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                user.setUID(ConnectionFactory.getFirebaseAuth().getUid());
                                Log.i("Script", "UID? " + user.getUID());
                                callback.onLoginSuccess();
                            } else {
                                callback.onFailedLogin();
                            }
                        }
                    });
        }

    }

    public void sendPassword(final SendPasswordCallback callback) {
        if(user.getEmail()!= null) {
            FirebaseAuth auth = ConnectionFactory.getFirebaseAuth();
            auth.sendPasswordResetEmail(user.getEmail()).addOnCompleteListener(
                    new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        callback.onSendSuccess();
                    } else {
                        callback.onFailedToSend();
                    }
                }
            });
        }
    }

    public void register(final RegisterUserCallback callback) {
        if(user.getEmail()!=null && user.getPassword()!= null) {
            FirebaseAuth auth = ConnectionFactory.getFirebaseAuth();
            auth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword()).
                    addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                FirebaseUser fUser = ConnectionFactory.getFirebaseUser();
                                User u = User.getInstance();
                                u.setUID(fUser.getUid());
                                callback.onUserRegistered();
                            } else {
                                callback.onFailedToRegisterUser();
                            }
                        }
                    });
        }
    }

    @Override
    public void find(final String userUId, final DAOCallback<User> callback) {
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
                    databaseReference.child(userUId).removeEventListener(this);
                } else {
                    Log.e("Script", "O usuário obitido é nulo!");
                    databaseReference.child(userUId).removeEventListener(this);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.i("Script", "Falha ao encontrar usuário");
                databaseReference.child(userUId).removeEventListener(this);
            }
        };
        databaseReference.child(userUId).addValueEventListener(listener);
    }

    @Override
    public void insert(final User user, final DAOCallback<User> callback) {
        FirebaseAuth auth = ConnectionFactory.getFirebaseAuth();
        auth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword()).
                addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            FirebaseUser fUser = ConnectionFactory.getFirebaseUser();
                            //user.setUUID(fUser.getUid());
                            DatabaseReference db = ConnectionFactory.getUsersDatabaseReferente();
                            //db.child(user.getUUID()).setValue(user);
                            //presenter.onSuccessTask(Constants.NEW_USER,null);
                        } else {
                            //presenter.onFailedTask(Constants.NEW_USER);
                        }
                    }
                });
    }

    @Override
    public void update(User user, final DAOCallback<User> callback) {
        DatabaseReference databaseReference = ConnectionFactory.getUsersDatabaseReferente();
        databaseReference.child(user.getUID()).
                setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()) {
                    callback.onObjectUpdated();
                } else {
                    callback.onFailedTask();
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
                "email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public interface RegisterUserCallback {
        void onUserRegistered();
        void onFailedToRegisterUser();
    }

    public interface LoginUserCallback {
        void onLoginSuccess();
        void onFailedLogin();
    }

    public interface SendPasswordCallback {
        void onSendSuccess();
        void onFailedToSend();
    }
}
