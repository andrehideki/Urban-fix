package com.mobile.urbanfix.urban_fix.view;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.mobile.urbanfix.urban_fix.R;
import com.mobile.urbanfix.urban_fix.MainMVP;
import com.mobile.urbanfix.urban_fix.presenter.RegisterPresenter;

public class RegisterActivity   extends AppCompatActivity
                                implements  View.OnClickListener,
                                            MainMVP.IRegisterView {

    private TextInputEditText nameRegisterEditText, lastNameRegisterEditText, birthdayRegisterEditText,
                        emailRegisterEditText, passwordRegisterEditText, cpfRegisterEditText;
    private Button finishRegisterButton;

    private MainMVP.IRegisterPresenter presenter;
    private ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        startMVP();
        nameRegisterEditText        =  findViewById(R.id.nameRegisterEditText);
        lastNameRegisterEditText    =  findViewById(R.id.lastNameRegisterEditText);
        birthdayRegisterEditText    =  findViewById(R.id.birthdayRegisterEditText);
        emailRegisterEditText       =  findViewById(R.id.emailRegisterEditText);
        cpfRegisterEditText         =  findViewById(R.id.cpfRegisterEditText);
        passwordRegisterEditText    =  findViewById(R.id.passwordRegisterEditText);
        finishRegisterButton        =    findViewById(R.id.finishAlertButton);
        finishRegisterButton.setOnClickListener(this);
    }



    @Override
    public void showMessage(String msg ) {
        Toast.makeText( RegisterActivity.this, msg, Toast.LENGTH_LONG).show();
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void finishView() {
        finish();
    }

    @Override
    public void onClick(View v) {
        presenter.registerUser(nameRegisterEditText.getText().toString(),
                lastNameRegisterEditText.getText().toString(),
                cpfRegisterEditText.getText().toString(),
                birthdayRegisterEditText.getText().toString(),
                emailRegisterEditText.getText().toString(),
                passwordRegisterEditText.getText().toString());
    }

    private void startMVP() {
        presenter = new RegisterPresenter(this);
    }

    @Override
    public void showCreatingUserDialog() {
        dialog = new ProgressDialog(this);
        dialog.setMessage(getString(R.string.register_creating_new_user));
        dialog.show();
    }

    @Override
    public void showInsertingUserIntoDBDialog() {
        if(dialog != null) {
            dialog.setMessage(getString(R.string.register_inserting_user_on_db));
        }
    }

    @Override
    public void showThanksDialog() {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage(getString(R.string.register_thanks_for_register))
                .setNeutralButton(getString(R.string.register_back_button), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        finish();
                    }
                })
                .create();
        dialog.show();
    }

    @Override
    public void onInsertingUserIntoDBFailed() {
        dialog.cancel();
        showMessage(getString(R.string.register_failed_create_user));
    }

    @Override
    public void finishDialog() {
        dialog.cancel();
    }


}
