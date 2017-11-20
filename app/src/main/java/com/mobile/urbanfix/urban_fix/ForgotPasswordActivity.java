package com.mobile.urbanfix.urban_fix;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPasswordActivity extends AppCompatActivity {


    private EditText emailForgotPasswordEditText;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        initViews();
    }

    @Override
    protected void onStart() {
        super.onStart();
        firebaseAuth = Connection.getFirebaseAuth();
    }

    private void initViews() {
        emailForgotPasswordEditText = (EditText) findViewById(R.id.emailForgotPasswordEditText);
    }

    public void actionResetPasswordButton( View view ) {
        final String email = emailForgotPasswordEditText.getText().toString().trim();
        System.out.println(email);
        if( !email.isEmpty() ) {
            firebaseAuth.sendPasswordResetEmail(email).
                    addOnCompleteListener(ForgotPasswordActivity.this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if( task.isSuccessful() ) {
                                showMessage( getString(R.string.forgot_password_msg_sended ) + email);
                                cleanEditText();
                            } else {
                                showMessage( getString(R.string.forgot_password_email_failed));
                                cleanEditText();
                            }
                        }
                    });
        } else {
            showMessage(getString(R.string.forgot_password_email_is_empty));
        }
    }


    private void showMessage( String msg ) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG ).show();
    }

    private void cleanEditText() {
        emailForgotPasswordEditText.setText("");
    }





}
