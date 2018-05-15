package com.mobile.urbanfix.urban_fix.model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.mobile.urbanfix.urban_fix.factory.ConnectionFactory;
import com.mobile.urbanfix.urban_fix.MainMVP;

import java.util.ArrayList;

public class Person  {

    private int nAlertsDone;
    private String name, cpf, birthDate;
    private static Person instance;

    private Person() {}

    public static Person getInstance() {
        if(instance == null) instance = new Person();
        return instance;
    }

    public int getnAlertsDone() {
        return nAlertsDone;
    }

    public void setnAlertsDone(int nAlertsDone) {
        this.nAlertsDone = nAlertsDone;
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

    public void find(String uid, final Callback.SimpleAsync<Person> callback) {

        final DatabaseReference personDatabaseReference =
                ConnectionFactory.getPersonsDatabaseReference().child(uid);
        final ValueEventListener listener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Person person = dataSnapshot.getValue(Person.class);
                if(person != null) {
                    callback.onTaskDone(person, true);
                } else {
                    callback.onTaskDone(null, false);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                callback.onTaskDone(null, false);
            }
        };
        personDatabaseReference.addListenerForSingleValueEvent(listener);
    }

    public void insert(Person person, final Callback.SimpleAsync<Person> callback) {
        DatabaseReference personDatabaseReference =
                ConnectionFactory.getPersonsDatabaseReference().child(User.getInstance().getUID());
        personDatabaseReference.setValue(person).addOnCompleteListener(new OnCompleteListener<Void>() {
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

    public void update(Person person, final Callback.SimpleAsync<Void> callback) {
        DatabaseReference databaseReference =
                ConnectionFactory.getPersonsDatabaseReference().child(User.getInstance().getUID());
        databaseReference.setValue(person).addOnCompleteListener(new OnCompleteListener<Void>() {
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


    public void getPersonAlerts(final Callback.FetchList<Problem> callback) {

        final String cpf = getInstance().getCpf();

        DatabaseReference databaseReference = ConnectionFactory.getAlertsDatabaseReference();
        databaseReference.orderByKey().startAt(cpf).addChildEventListener(new ChildEventListener() {
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

    @Override
    public String toString() {
        return "Person{" +
                "nAlertsDone=" + nAlertsDone +
                ", name='" + name + '\'' +
                ", cpf='" + cpf + '\'' +
                ", birthDate='" + birthDate + '\'' +
                '}';
    }
}
