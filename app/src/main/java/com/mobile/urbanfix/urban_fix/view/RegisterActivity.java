package com.mobile.urbanfix.urban_fix.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mobile.urbanfix.urban_fix.R;
import com.mobile.urbanfix.urban_fix.presenter.MainMVP;
import com.mobile.urbanfix.urban_fix.presenter.RegisterPresenter;

public class RegisterActivity   extends AppCompatActivity
                                implements  View.OnClickListener,
                                            MainMVP.IRegisterView {

    private EditText nameRegisterEditText, lastNameRegisterEditText, birthdayRegisterEditText,
                        emailRegisterEditText, passwordRegisterEditText, cpfRegisterEditText;
    private Button finishRegisterButton;

    private MainMVP.IRegisterPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        startMVP();
        nameRegisterEditText        = (EditText) findViewById(R.id.nameRegisterEditText);
        lastNameRegisterEditText    = (EditText) findViewById(R.id.lastNameRegisterEditText);
        birthdayRegisterEditText    = (EditText) findViewById(R.id.birthdayRegisterEditText);
        emailRegisterEditText       = (EditText) findViewById(R.id.emailRegisterEditText);
        cpfRegisterEditText         = (EditText) findViewById(R.id.cpfRegisterEditText);
        passwordRegisterEditText    = (EditText) findViewById(R.id.passwordRegisterEditText);
        finishRegisterButton        = (Button)   findViewById(R.id.finishAlertButton);
        finishRegisterButton.setOnClickListener(this);
    }


    @Override
    public String[] getFieldsValues() {
        String[] values = { nameRegisterEditText.getText().toString(),
                            lastNameRegisterEditText.getText().toString(),
                            cpfRegisterEditText.getText().toString(),
                            birthdayRegisterEditText.getText().toString(),
                            emailRegisterEditText.getText().toString(),
                            passwordRegisterEditText.getText().toString()};
        return values;
    }

    @Override
    public void showMessage(String msg ) {
        Toast.makeText( RegisterActivity.this, msg, Toast.LENGTH_LONG).show();
    }


    @Override
    public void onClick(View v) {
        presenter.registerUser();
    }

    private void startMVP() {
        presenter = new RegisterPresenter(this);
    }
}
