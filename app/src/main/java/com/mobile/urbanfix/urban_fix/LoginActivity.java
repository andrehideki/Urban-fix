package com.mobile.urbanfix.urban_fix;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    private EditText emailLoginEditText;
    private EditText passwordLoginEditText;
    private Button registerLoginButton;
    private Button loginLoginButton;
    private Button forgotPasswordLoginButton;

    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        emailLoginEditText = (EditText) findViewById(R.id.emailLoginEditText);
        passwordLoginEditText = (EditText) findViewById(R.id.passwordLoginEditText);
        registerLoginButton = (Button) findViewById(R.id.registerLoginButton);
        registerLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openRegisterActivity();
            }
        });
        loginLoginButton = (Button)findViewById(R.id.loginLoginButton);
        loginLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Concetar ao firebase
                String email = emailLoginEditText.getText().toString().trim();
                String password = passwordLoginEditText.getText().toString().trim();
                if(emailAndPasswordValid(email, password)) {

                    firebaseAuth.signInWithEmailAndPassword(email, password).
                            addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()) {
                                        openMainActivity();
                                    } else {
                                        Toast.makeText(LoginActivity.this,
                                                getString(R.string.invalid_password_or_email), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });

                } else {
                    Toast.makeText(LoginActivity.this,
                            getString(R.string.invalid_password_or_email), Toast.LENGTH_LONG).show();
                }
            }
        });

        forgotPasswordLoginButton = (Button) findViewById(R.id.forgotPasswordLoginButton);
        forgotPasswordLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openForgotPasswordActivity();
            }
        });
    }

    private void openRegisterActivity() {
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        startActivity(intent);
    }

    private void openMainActivity() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
    }

    private void openForgotPasswordActivity() {
        startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
    }

    private boolean emailAndPasswordValid(String email, String password) {
        return !email.isEmpty()  && !password.isEmpty() && password.length() > 6;
    }
}
