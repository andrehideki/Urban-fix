package com.mobile.urbanfix.urban_fix;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.mobile.urbanfix.urban_fix.model.User;

public class RegisterActivity extends AppCompatActivity {

    private EditText nameRegisterEditText, lastNameRegisterEditText, birthdayRegisterEditText,
                        emailRegisterEditText, passwordRegisterEditText, cpfRegisterEditText;
    private Button finishRegisterButton;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private FirebaseUser firebaseUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth = Connection.getFirebaseAuth();
        databaseReference = Connection.getDatabaseReference();
    }

    private void initViews() {
        nameRegisterEditText        = (EditText) findViewById(R.id.nameRegisterEditText);
        lastNameRegisterEditText    = (EditText) findViewById(R.id.lastNameRegisterEditText);
        birthdayRegisterEditText    = (EditText) findViewById(R.id.birthdayRegisterEditText);
        emailRegisterEditText       = (EditText) findViewById(R.id.emailRegisterEditText);
        cpfRegisterEditText         = (EditText) findViewById(R.id.cpfRegisterEditText);
        passwordRegisterEditText    = (EditText) findViewById(R.id.passwordRegisterEditText);
        finishRegisterButton        = (Button)  findViewById(R.id.finishRegisterButton);
    }

    public void actionfinishRegisterButton( View view ) {
        if( verifyRegisterEditTexts() ) {
            final User user = new User(   nameRegisterEditText.getText().toString().trim() +
                                    lastNameRegisterEditText.getText().toString().trim(),
                                    cpfRegisterEditText.getText().toString().trim(),
                                    birthdayRegisterEditText.getText().toString().trim(),
                                    emailRegisterEditText.getText().toString().trim(),
                                    passwordRegisterEditText.getText().toString().trim());

            boolean isSuccessful = false;
            firebaseAuth.
                    createUserWithEmailAndPassword( user.getEmail(), user.getPassword() ).
                    addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if( task.isSuccessful() ) {
                                firebaseUser = Connection.getFirebaseUser();
                                databaseReference.child("User").child(firebaseUser.getUid()).setValue( user );
                                showMessage( getString(R.string.register_user_succesful));
                                startActivity( new Intent( RegisterActivity.this, MainActivity.class ));
                                finish();
                            } else {
                                showMessage( getString( R.string.register_failed_create_user ));
                            }
                        }
                    });
        }
    }

    private boolean verifyRegisterEditTexts() {
        return
            !nameRegisterEditText.getText().toString().isEmpty() &&
            !lastNameRegisterEditText.getText().toString().isEmpty() &&
            !birthdayRegisterEditText.getText().toString().isEmpty() &&
            !emailRegisterEditText.getText().toString().isEmpty() &&
            !cpfRegisterEditText.getText().toString().isEmpty() &&
            !passwordRegisterEditText.getText().toString().isEmpty();
    }

    private void showMessage( String msg ) {
        Toast.makeText( RegisterActivity.this, msg, Toast.LENGTH_SHORT);
    }




}
