package com.mobile.urbanfix.urban_fix.view;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import com.mobile.urbanfix.urban_fix.R;
import com.mobile.urbanfix.urban_fix.presenter.ForgotPasswordPresenter;
import com.mobile.urbanfix.urban_fix.presenter.MainMVP;

public class ForgotPasswordActivity extends AppCompatActivity
                                    implements  MainMVP.IForgotPasswordView,
                                                View.OnClickListener {


    private EditText emailForgotPasswordEditText;
    private MainMVP.IForgotPasswordPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        startMVP();
        emailForgotPasswordEditText = (EditText) findViewById(R.id.emailForgotPasswordEditText);
    }


    @Override
    public void cleanFields() {
        emailForgotPasswordEditText.setText("");
    }

    @Override
    public void showMessage(String msg ) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG ).show();
    }

    @Override
    public Context getContext() {
        return null;
    }

    @Override
    public String getEmail() {
        return emailForgotPasswordEditText.getText().toString();
    }


    @Override
    public void onClick(View v) {
        presenter.sendRecoverPasswordMessage();
    }

    private void startMVP() {
        presenter = new ForgotPasswordPresenter(this);
    }


}