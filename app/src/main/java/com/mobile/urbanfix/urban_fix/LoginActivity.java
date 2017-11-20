package com.mobile.urbanfix.urban_fix;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText emailLoginEditText, passwordLoginEditText;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth = Connection.getFirebaseAuth();
    }

    public void initViews() {
        emailLoginEditText = (EditText) findViewById(R.id.emailLoginEditText);
        passwordLoginEditText = (EditText) findViewById(R.id.passwordLoginEditText);
    }



    public void actionRegisterButton( View view ) {
        startActivity( new Intent(LoginActivity.this, RegisterActivity.class ) );
    }

    public void actionLoginButton( View view ) {
        String email = emailLoginEditText.getText().toString().trim();
        String password = passwordLoginEditText.getText().toString().trim();
        firebaseAuth.signInWithEmailAndPassword( email, password).
                addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if( task.isSuccessful() ) {
                            startActivity( new Intent( LoginActivity.this, MainActivity.class));
                            finish();
                        } else {
                            showMessage( getString( R.string.invalid_password_or_email ) );
                            cleanEditText();
                        }
                    }
                });
    }

    public void actionForgotPasswordButton( View view ) {
        startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
    }

    private void showMessage( String msg ) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG ).show();
    }

    private void cleanEditText() {
        emailLoginEditText.setText("");
        passwordLoginEditText.setText("");
    }
}
