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

public class RegisterActivity extends AppCompatActivity {

    private EditText nameRegisterEditText;
    private EditText lastNameRegisterEditText;
    private EditText birthdayRegisterEditText;
    private EditText emailRegisterEditText;
    private EditText passwordRegisterEditText;
    private Button finishRegisterButton;

    private FirebaseAuth firebaseAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firebaseAuth = FirebaseAuth.getInstance();

        nameRegisterEditText        = (EditText) findViewById(R.id.nameRegisterEditText);
        lastNameRegisterEditText    = (EditText) findViewById(R.id.lastNameRegisterEditText);
        birthdayRegisterEditText    = (EditText) findViewById(R.id.birthdayRegisterEditText);
        emailRegisterEditText       = (EditText) findViewById(R.id.emailRegisterEditText);
        passwordRegisterEditText    = (EditText) findViewById(R.id.passwordRegisterEditText);
        finishRegisterButton        = (Button)  findViewById(R.id.finishRegisterButton);
        finishRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = emailRegisterEditText.getText().toString().trim();
                String password = passwordRegisterEditText.getText().toString().trim();

                //Verica validade do email e da senha()

                firebaseAuth.createUserWithEmailAndPassword(email, password).
                        addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(),"Registrado com sucesso",Toast.LENGTH_LONG).show();
                                    openMainActivity();
                                } else {
                                    Toast.makeText(getApplicationContext(),
                                            getString(R.string.register_failed_create_user), Toast.LENGTH_LONG).show();
                                }
                            }
                        });

            }
        });

    }


    private void openLoginActivity() {
        Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    private void openMainActivity() {
        startActivity(new Intent(RegisterActivity.this, MainActivity.class));
    }

}
