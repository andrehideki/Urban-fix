package com.mobile.urbanfix.urban_fix.model;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.mobile.urbanfix.urban_fix.factory.ConnectionFactory;
import com.mobile.urbanfix.urban_fix.MainMVP;

public class Person implements DAO<Person> {

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

    @Override
    public String toString() {
        return "Person{" +
                "nAlertsDone=" + nAlertsDone +
                ", name='" + name + '\'' +
                ", cpf='" + cpf + '\'' +
                ", birthDate='" + birthDate + '\'' +
                '}';
    }

    @Override
    public void find(String uid, final DAOCallback<Person> daoCallback) {
        DatabaseReference personDatabaseReference =
                ConnectionFactory.getPersonsDatabaseReference().child(uid);
        personDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Person person = dataSnapshot.getValue(Person.class);
                daoCallback.onObjectFinded(person);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("Script", databaseError.getDetails());
                daoCallback.onFailedTask();
            }
        });
    }

    @Override
    public void insert(Person person, final DAOCallback<Person> callback) {
        DatabaseReference personDatabaseReference =
                ConnectionFactory.getPersonsDatabaseReference().child(User.getInstance().getUID());
        personDatabaseReference.setValue(person).addOnCompleteListener(new OnCompleteListener<Void>() {
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
    public void update(Person person, final DAOCallback<Person> callback) {
        DatabaseReference databaseReference =
                ConnectionFactory.getPersonsDatabaseReference().child(User.getInstance().getUID());
        databaseReference.setValue(person).addOnCompleteListener(new OnCompleteListener<Void>() {
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
    public void delete(Person object, MainMVP.ICallbackPresenter presenter) {

    }
}
